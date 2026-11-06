import { Routes, Route } from 'react-router-dom'
import Accueil from './components/Accueil.jsx'
import ConnexionAdmin from './components/ConnexionAdmin.jsx'
import ConnexionProfesseur from './components/ConnexionProfesseur.jsx'
import Inscription from './components/Inscription.jsx'
import RouteProtegee from './components/RouteProtegee.jsx'
import AdminLayout from './components/AdminLayout.jsx'
import AdminDashboard from './components/AdminDashboard.jsx'
import CoursListe from './components/CoursListe.jsx'
import CoursForm from './components/CoursForm.jsx'
import EnseignantListe from './components/EnseignantListe.jsx'
import EnseignantForm from './components/EnseignantForm.jsx'
import CompteEnAttente from './components/CompteEnAttente.jsx'
import Referentiel from './components/Referentiel.jsx'
import ProfesseurDashboard from './components/ProfesseurDashboard.jsx'
import Consultation from './components/Consultation.jsx'
function App() {
  return (
    <div className="app">
      <Routes>
        <Route path="/consultation" element={<Consultation />} />
        <Route path="/" element={<Accueil />} />
        <Route path="/connexion-admin" element={<ConnexionAdmin />} />
        <Route path="/connexion-professeur" element={<ConnexionProfesseur />} />
        <Route path="/inscription" element={<Inscription />} />
        <Route path="/admin" element={<RouteProtegee roleRequis="ADMIN"><AdminLayout /></RouteProtegee>}>
          <Route index element={<AdminDashboard />} />
          <Route path="cours" element={<CoursListe />} />
          <Route path="cours/ajouter" element={<CoursForm />} />
          <Route path="cours/modifier/:id" element={<CoursForm />} />
          <Route path="enseignants" element={<EnseignantListe />} />
          <Route path="enseignants/ajouter" element={<EnseignantForm />} />
          <Route path="enseignants/modifier/:telephone" element={<EnseignantForm />} />
          <Route path="comptes-en-attente" element={<CompteEnAttente />} />
          <Route path="referentiel" element={<Referentiel />} />
        </Route>
        <Route path="/professeur" element={<RouteProtegee roleRequis="PROFESSEUR"><ProfesseurDashboard /></RouteProtegee>} />
      </Routes>
    </div>
  )
}

export default App