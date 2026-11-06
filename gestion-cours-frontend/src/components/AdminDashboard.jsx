import { Link } from 'react-router-dom'

function AdminDashboard() {
  return (
    <div>
      <h1>Tableau de bord admin</h1>
      <ul>
      
  <li><Link to="/admin/cours">Gérer les cours</Link></li>
  <li><Link to="/admin/enseignants">Gérer les enseignants (modifier, supprimer)</Link></li>
  <li><Link to="/admin/comptes-en-attente">Comptes en attente</Link></li>
  <li><Link to="/admin/referentiel">Référentiel</Link></li>

      </ul>
    </div>
  )
}

export default AdminDashboard