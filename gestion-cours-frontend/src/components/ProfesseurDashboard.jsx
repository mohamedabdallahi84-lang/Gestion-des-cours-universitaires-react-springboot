import { useEffect, useState } from 'react'
import api from '../services/api'
import { useAuth } from '../context/AuthContext.jsx'
import GrilleEmploiDuTemps from './GrilleEmploiDuTemps.jsx'

function ProfesseurDashboard() {
  const [emploi, setEmploi] = useState([])
  const [erreur, setErreur] = useState(null)
  const { utilisateur, deconnecter } = useAuth()

  const charger = () => api.get('/api/professeur/mon-emploi').then((r) => setEmploi(r.data)).catch(() => setErreur("Erreur."))
  useEffect(() => { charger() }, [])

  const confirmer = (id) => api.put(`/api/professeur/cours/${id}/confirmer`).then(charger)
  const refuser = (id) => api.put(`/api/professeur/cours/${id}/refuser`).then(charger)

  return (
    <div className="page">
      <h1>Mon emploi du temps</h1>
      <p>{utilisateur?.nom}</p>
      <button onClick={deconnecter}>Se déconnecter</button>
      {erreur && <p style={{ color: 'red' }}>{erreur}</p>}

      <h2>Vue d'ensemble</h2>
   <GrilleEmploiDuTemps cours={emploi.filter((c) => c.statut !== 'REFUSE')} />

      <h2>Détail et actions</h2>
      <table border="1" cellPadding="8">
        <thead><tr><th>Cours</th><th>Groupe</th><th>Jour</th><th>Horaire</th><th>Date</th><th>Statut</th><th>Actions</th></tr></thead>
        <tbody>
          {emploi.map((c) => (
            <tr key={c.id}>
              <td>{c.nom}</td>
              <td>{c.groupe ? `${c.groupe.filiere.nom} - ${c.groupe.semestre.nom} - ${c.groupe.nom}` : ''}</td>
              <td>{c.jour}</td><td>{c.horaire}</td><td>{c.date}</td><td>{c.statut}</td>
              <td>{c.statut === 'EN_ATTENTE' && (<><button onClick={() => confirmer(c.id)}>Confirmer</button>{' '}<button onClick={() => refuser(c.id)}>Refuser</button></>)}</td>
            </tr>
          ))}
          {emploi.length === 0 && <tr><td colSpan="7">Aucun cours.</td></tr>}
        </tbody>
      </table>
    </div>
  )
}

export default ProfesseurDashboard