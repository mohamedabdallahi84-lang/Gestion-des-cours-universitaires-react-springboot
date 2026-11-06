import { Outlet, Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

function AdminLayout() {
  const { deconnecter } = useAuth()

  return (
    <div className="page">
     <nav>
  <Link to="/admin">Accueil</Link>{' | '}
  <Link to="/admin/cours">Cours</Link>{' | '}
  <Link to="/admin/enseignants">Enseignants</Link>{' | '}
  <Link to="/admin/comptes-en-attente">Comptes en attente</Link>{' | '}
  <Link to="/admin/referentiel">Filières/Semestres/Groupes</Link>{' | '}
  <button onClick={deconnecter}>Se déconnecter</button>
</nav>
      <hr/>
      {/* Outlet affiche la sous-page actuelle (dashboard, liste des cours, etc.) */}
      <Outlet />
    </div>
  )
}

export default AdminLayout