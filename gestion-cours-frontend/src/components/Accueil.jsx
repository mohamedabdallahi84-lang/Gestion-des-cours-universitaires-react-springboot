// Link : composant de react-router pour naviguer entre les pages sans recharger toute l'application (contrairement à une balise <a> classique).
// useAuth : un hook personnalisé qui vient de ton AuthContext, qui donne accès à l'état de connexion global (qui est connecté, avec quel rôle, etc.).

import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

function Accueil() {
  // utilisateur : objet contenant les infos de la personne connectée (ou null/undefined si personne n'est connecté).
// deconnecter : fonction pour se déconnecter (vue déjà dans ProfesseurDashboard.jsx).
  const { utilisateur, deconnecter } = useAuth()

  return (
    <div className="page">
      <h1>Gestion des cours universitaires</h1>

      {utilisateur ? (
        <div>
          <p>Connecté en tant que {utilisateur.nom || utilisateur.identifiant} ({utilisateur.role})</p>
          {utilisateur.role === 'ADMIN' && <p><Link to="/admin">Aller au tableau de bord admin</Link></p>}
          {utilisateur.role === 'PROFESSEUR' && <p><Link to="/professeur">Aller à mon espace professeur</Link></p>}
          <button onClick={deconnecter}>Se déconnecter</button>
        </div>
      ) : (
        <div>
          <p><Link to="/connexion-admin">Connexion administrateur</Link></p>
          <p><Link to="/connexion-professeur">Connexion professeur</Link></p>
          <p><Link to="/inscription">Créer un compte professeur</Link></p>
          <p><Link to="/consultation">Consulter l'emploi du temps (sans connexion)</Link></p>
        </div>
      )}
    </div>
  )
}

export default Accueil