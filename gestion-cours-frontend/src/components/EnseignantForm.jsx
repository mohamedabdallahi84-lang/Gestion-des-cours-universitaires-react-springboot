import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import enseignantService from '../services/enseignantService'

function EnseignantForm() {
  const [enseignant, setEnseignant] = useState({ telephone: '', nom: '' })
  const [erreur, setErreur] = useState(null)
  const navigate = useNavigate()
  // Présent uniquement si l'URL est "/enseignants/modifier/xxx".
  const { telephone } = useParams()
  const modeEdition = Boolean(telephone)

  useEffect(() => {
    if (modeEdition) {
      enseignantService.getByTelephone(telephone).then((reponse) => setEnseignant(reponse.data))
    }
  }, [telephone, modeEdition])

  const handleChange = (e) => {
    const { name, value } = e.target
    setEnseignant({ ...enseignant, [name]: value })
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    setErreur(null)
    const operation = modeEdition
      ? enseignantService.update(telephone, enseignant)
      : enseignantService.create(enseignant)

    operation
      .then(() => navigate(-1))
      .catch((err) => setErreur(err.response?.data?.message || "Une erreur est survenue."))
  }

  return (
    <div className="page">
      <h1>{modeEdition ? "Modifier l'enseignant" : 'Ajouter un enseignant'}</h1>
      {erreur && <p style={{ color: 'red' }}>{erreur}</p>}

      <form onSubmit={handleSubmit}>
        <label>Téléphone<br/>
          {/* En modification, le téléphone est la clé primaire : non modifiable. */}
          <input name="telephone" value={enseignant.telephone} onChange={handleChange} required disabled={modeEdition} />
        </label><br/><br/>

        <label>Nom<br/>
          <input name="nom" value={enseignant.nom} onChange={handleChange} required />
        </label><br/><br/>

        <button type="submit">{modeEdition ? 'Enregistrer' : 'Ajouter'}</button>{' '}
        <button type="button" onClick={() => navigate(-1)}>Annuler</button>
      </form>
    </div>
  )
}

export default EnseignantForm