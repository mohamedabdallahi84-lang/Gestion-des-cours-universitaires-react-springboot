import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import authService from '../services/authService'
import { useAuth } from '../context/AuthContext.jsx'

function ConnexionAdmin() {
  const [nomUtilisateur, setNomUtilisateur] = useState('')
  const [motDePasse, setMotDePasse] = useState('')
  const [erreur, setErreur] = useState(null)
  const { connecter } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = (e) => {
    e.preventDefault()
    setErreur(null)
    authService.connexionAdmin({ nomUtilisateur, motDePasse })
      .then((reponse) => {
        connecter(reponse.data.jeton, reponse.data.role, nomUtilisateur, 'Administrateur')
        navigate('/admin')
      })
      .catch((err) => setErreur(err.response?.data?.message || "Erreur de connexion."))
  }

  return (
    <div className="page">
      <h1>Connexion administrateur</h1>
      {erreur && <p style={{ color: 'red' }}>{erreur}</p>}
      <form onSubmit={handleSubmit}>
        <label>Nom d'utilisateur<br/>
          <input value={nomUtilisateur} onChange={(e) => setNomUtilisateur(e.target.value)} required />
        </label><br/><br/>
        <label>Mot de passe<br/>
          <input type="password" value={motDePasse} onChange={(e) => setMotDePasse(e.target.value)} required />
        </label><br/><br/>
        <button type="submit">Se connecter</button>
      </form>
    </div>
  )
}

export default ConnexionAdmin