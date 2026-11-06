import { useEffect, useState } from 'react'
import api from '../services/api'

function Referentiel() {
  const [filieres, setFilieres] = useState([])
  const [semestres, setSemestres] = useState([])
  const [groupes, setGroupes] = useState([])
  const [nomFiliere, setNomFiliere] = useState('')
  const [nomSemestre, setNomSemestre] = useState('')
  const [nouveauGroupe, setNouveauGroupe] = useState({ nom: '', filiereId: '', semestreId: '' })
  const [erreur, setErreur] = useState(null)

  const chargerTout = () => {
    api.get('/api/admin/filieres').then((r) => setFilieres(r.data))
    api.get('/api/admin/semestres').then((r) => setSemestres(r.data))
    api.get('/api/admin/groupes').then((r) => setGroupes(r.data))
  }
  useEffect(() => { chargerTout() }, [])

  const ajouterFiliere = (e) => {
    e.preventDefault()
    api.post('/api/admin/filieres', { nom: nomFiliere }).then(() => { setNomFiliere(''); chargerTout() }).catch(() => setErreur("Erreur."))
  }
  const ajouterSemestre = (e) => {
    e.preventDefault()
    api.post('/api/admin/semestres', { nom: nomSemestre }).then(() => { setNomSemestre(''); chargerTout() }).catch(() => setErreur("Erreur."))
  }
  const ajouterGroupe = (e) => {
    e.preventDefault()
    api.post('/api/admin/groupes', nouveauGroupe).then(() => { setNouveauGroupe({ nom: '', filiereId: '', semestreId: '' }); chargerTout() }).catch(() => setErreur("Erreur."))
  }

  return (
    <div>
      <h1>Filières, semestres et groupes</h1>
      {erreur && <p style={{ color: 'red' }}>{erreur}</p>}

      <h2>Filières</h2>
      <ul>{filieres.map((f) => <li key={f.id}>{f.nom}</li>)}</ul>
      <form onSubmit={ajouterFiliere}>
        <input placeholder="Nom de la filière" value={nomFiliere} onChange={(e) => setNomFiliere(e.target.value)} required />
        <button type="submit">Ajouter</button>
      </form>

      <h2>Semestres</h2>
      <ul>{semestres.map((s) => <li key={s.id}>{s.nom}</li>)}</ul>
      <form onSubmit={ajouterSemestre}>
        <input placeholder="Nom du semestre (ex: S1)" value={nomSemestre} onChange={(e) => setNomSemestre(e.target.value)} required />
        <button type="submit">Ajouter</button>
      </form>

      <h2>Groupes</h2>
      <ul>{groupes.map((g) => <li key={g.id}>{g.filiere?.nom} - {g.semestre?.nom} - {g.nom}</li>)}</ul>
      <form onSubmit={ajouterGroupe}>
        <select value={nouveauGroupe.filiereId} onChange={(e) => setNouveauGroupe({ ...nouveauGroupe, filiereId: e.target.value })} required>
          <option value="">-- Filière --</option>
          {filieres.map((f) => <option key={f.id} value={f.id}>{f.nom}</option>)}
        </select>
        <select value={nouveauGroupe.semestreId} onChange={(e) => setNouveauGroupe({ ...nouveauGroupe, semestreId: e.target.value })} required>
          <option value="">-- Semestre --</option>
          {semestres.map((s) => <option key={s.id} value={s.id}>{s.nom}</option>)}
        </select>
        <input placeholder="Nom du groupe" value={nouveauGroupe.nom} onChange={(e) => setNouveauGroupe({ ...nouveauGroupe, nom: e.target.value })} required />
        <button type="submit">Ajouter</button>
      </form>
    </div>
  )
}

export default Referentiel