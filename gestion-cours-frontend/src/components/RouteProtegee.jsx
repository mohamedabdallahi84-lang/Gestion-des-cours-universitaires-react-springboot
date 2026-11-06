import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

// roleRequis : "ADMIN" ou "PROFESSEUR", le rôle nécessaire pour voir cette page.
function RouteProtegee({ roleRequis, children }) {
  const { utilisateur } = useAuth()

  // Si personne n'est connecté, ou que le rôle ne correspond pas, retour à l'accueil.
  if (!utilisateur || utilisateur.role !== roleRequis) {
    return <Navigate to="/" replace />
  }

  return children
}

export default RouteProtegee