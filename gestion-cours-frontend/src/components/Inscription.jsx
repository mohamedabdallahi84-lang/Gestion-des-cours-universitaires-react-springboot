import { useState } from 'react'
import { Link } from 'react-router-dom'
import authService from '../services/authService'

function Inscription() {
  const [donnees, setDonnees] = useState({ telephone: '', nom: '', motDePasse: '' })
  const [message, setMessage] = useState(null)
  const [erreur, setErreur] = useState(null)

  const handleChange = (e) => setDonnees({ ...donnees, [e.target.name]: e.target.value })

  const handleSubmit = (e) => {
    e.preventDefault()
    setErreur(null)
    authService.inscription(donnees)
      .then((reponse) => setMessage(reponse.data.message))
      .catch((err) => setErreur(err.response?.data?.message || "Erreur lors de l'inscription."))
  }

  return (
    <div className="page">
      <h1>Créer un compte professeur</h1>
      {erreur && <p style={{ color: 'red' }}>{erreur}</p>}
      {message ? (
        <div>
          <p>{message}</p>
          <Link to="/">Retour à l'accueil</Link>
        </div>
      ) : (
        <form onSubmit={handleSubmit}>
          <label>Téléphone<br/>
            <input name="telephone" value={donnees.telephone} onChange={handleChange} required />
          </label><br/><br/>
          <label>Nom<br/>
            <input name="nom" value={donnees.nom} onChange={handleChange} required />
          </label><br/><br/>
          <label>Mot de passe<br/>
            <input type="password" name="motDePasse" value={donnees.motDePasse} onChange={handleChange} required />
          </label><br/><br/>
          <button type="submit">S'inscrire</button>
        </form>
      )}
    </div>
  )
}

export default Inscription