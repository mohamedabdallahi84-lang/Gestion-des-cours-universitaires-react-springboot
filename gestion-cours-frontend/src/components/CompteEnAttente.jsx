import { useEffect, useState } from 'react'
import api from '../services/api'
/*enAttente : liste des comptes professeurs en attente de validation.
groupes : liste des groupes disponibles (filière/semestre/nom), pour le formulaire d'attribution d'accès.
acces : objet qui représente le formulaire en cours de remplissage (téléphone + groupe choisi).
erreur / message : pour afficher un message d'erreur (rouge) ou de succès (vert).*/
function CompteEnAttente() {
  const [enAttente, setEnAttente] = useState([])
  const [groupes, setGroupes] = useState([])
  const [acces, setAcces] = useState({ telephone: '', groupeId: '' })
  const [erreur, setErreur] = useState(null)
  const [message, setMessage] = useState(null)

  const charger = () => {
    api.get('/api/admin/enseignants-en-attente').then((r) => setEnAttente(r.data))
    api.get('/api/admin/groupes').then((r) => setGroupes(r.data))
  }
  useEffect(() => { charger() }, [])

  const accepter = (telephone) => api.put(`/api/admin/enseignants/${telephone}/accepter`).then(charger)
  const refuser = (telephone) => api.put(`/api/admin/enseignants/${telephone}/refuser`).then(charger)

  const donnerAcces = (e) => {
    e.preventDefault()
    setMessage(null)
    api.post('/api/admin/acces', acces)
      .then(() => setMessage("Accès attribué."))
      .catch((err) => setErreur(err.response?.data?.message || "Erreur."))
  }

  return (
    <div>
      <h1>Comptes en attente</h1>
      {erreur && <p style={{ color: 'red' }}>{erreur}</p>}
      <table border="1" cellPadding="8">
        <thead><tr><th>Téléphone</th><th>Nom</th><th>Actions</th></tr></thead>
        <tbody>
          {enAttente.map((e) => (
            <tr key={e.telephone}>
              <td>{e.telephone}</td><td>{e.nom}</td>
              <td><button onClick={() => accepter(e.telephone)}>Accepter</button>{' '}<button onClick={() => refuser(e.telephone)}>Refuser</button></td>
            </tr>
          ))}
          {enAttente.length === 0 && <tr><td colSpan="3">Aucun compte en attente.</td></tr>}
        </tbody>
      </table>

      <h2>Attribuer un accès</h2>
      {message && <p style={{ color: 'green' }}>{message}</p>}
      <form onSubmit={donnerAcces}>
        <input placeholder="Téléphone" value={acces.telephone} onChange={(e) => setAcces({ ...acces, telephone: e.target.value })} required />
        <select value={acces.groupeId} onChange={(e) => setAcces({ ...acces, groupeId: e.target.value })} required>
          <option value="">-- Groupe --</option>
          {groupes.map((g) => <option key={g.id} value={g.id}>{g.filiere?.nom} - {g.semestre?.nom} - {g.nom}</option>)}
        </select>
        <button type="submit">Donner accès</button>
      </form>
    </div>
  )
}

export default CompteEnAttente