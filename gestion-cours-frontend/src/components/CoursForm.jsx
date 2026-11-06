import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import coursService from '../services/coursService'
import enseignantService from '../services/enseignantService'
import api from '../services/api'

const HORAIRES_VALIDES = [
  '08:30 - 09:30', '09:30 - 10:30', '10:30 - 11:30', '11:30 - 12:30',
  '14:00 - 15:00', '15:00 - 16:00', '16:00 - 17:00', '17:00 - 18:00',
  '08:30 - 10:30', '10:30 - 12:30', '14:00 - 16:00', '16:00 - 18:00',
]

function versChampDate(dateApi) {
  if (!dateApi) return ''
  const [jour, mois, annee] = dateApi.split('-')
  return `${annee}-${mois}-${jour}`
}
function versDateApi(dateChamp) {
  if (!dateChamp) return ''
  const [annee, mois, jour] = dateChamp.split('-')
  return `${jour}-${mois}-${annee}`
}

const coursVide = { nom: '', enseignant: '', groupeId: '', credits: 1, horaire: '', date: '' }

function CoursForm() {
  const [cours, setCours] = useState(coursVide)
  const [enseignants, setEnseignants] = useState([])
  const [groupes, setGroupes] = useState([])
  const [erreur, setErreur] = useState(null)
  const { id } = useParams()
  const navigate = useNavigate()
  const modeEdition = Boolean(id)

  useEffect(() => {
    enseignantService.getAll().then((r) => setEnseignants(r.data))
    api.get('/api/admin/groupes').then((r) => setGroupes(r.data))
  }, [])

  useEffect(() => {
    if (modeEdition) {
      coursService.getById(id).then((reponse) => {
        const c = reponse.data
        setCours({
          nom: c.nom,
          enseignant: c.enseignant ? c.enseignant.telephone : '',
          groupeId: c.groupe ? c.groupe.id : '',
          credits: c.credits,
          horaire: c.horaire,
          date: versChampDate(c.date),
        })
      })
    }
  }, [id, modeEdition])

  const handleChange = (e) => {
    const { name, value } = e.target
    setCours({ ...cours, [name]: name === 'credits' ? Number(value) : value })
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    setErreur(null)
    const coursAEnvoyer = {
      nom: cours.nom,
      enseignant: { telephone: cours.enseignant },
      groupe: { id: Number(cours.groupeId) },
      credits: cours.credits,
      horaire: cours.horaire,
      date: versDateApi(cours.date),
    }
    const operation = modeEdition ? coursService.update(id, coursAEnvoyer) : coursService.create(coursAEnvoyer)
    operation
      .then(() => navigate('/admin/cours'))
      .catch((err) => setErreur(err.response?.data?.message || "Une erreur est survenue."))
  }

  return (
    <div className="page">
      <h1>{modeEdition ? 'Modifier le cours' : 'Ajouter un cours'}</h1>
      {erreur && <p style={{ color: 'red' }}>{erreur}</p>}
      <form onSubmit={handleSubmit}>
        <label>Nom du cours<br/><input name="nom" value={cours.nom} onChange={handleChange} required /></label><br/><br/>
        <label>Enseignant<br/>
          <select name="enseignant" value={cours.enseignant} onChange={handleChange} required>
            <option value="">-- Choisir --</option>
            {enseignants.map((e) => <option key={e.telephone} value={e.telephone}>{e.nom} ({e.telephone})</option>)}
          </select>
        </label><br/><br/>
        <label>Groupe<br/>
          <select name="groupeId" value={cours.groupeId} onChange={handleChange} required>
            <option value="">-- Choisir --</option>
            {groupes.map((g) => <option key={g.id} value={g.id}>{g.filiere?.nom} - {g.semestre?.nom} - {g.nom}</option>)}
          </select>
        </label><br/><br/>
        <label>Crédits<br/><input type="number" name="credits" value={cours.credits} onChange={handleChange} min="1" required /></label><br/><br/>
        <label>Horaire<br/>
          <select name="horaire" value={cours.horaire} onChange={handleChange} required>
            <option value="">-- Choisir --</option>
            {HORAIRES_VALIDES.map((h) => <option key={h} value={h}>{h}</option>)}
          </select>
        </label><br/><br/>
        <label>Date<br/><input type="date" name="date" value={cours.date} onChange={handleChange} required /></label><br/><br/>
        <button type="submit">{modeEdition ? 'Enregistrer' : 'Ajouter'}</button>{' '}
        <button type="button" onClick={() => navigate('/admin/cours')}>Annuler</button>
      </form>
    </div>
  )
}

export default CoursForm