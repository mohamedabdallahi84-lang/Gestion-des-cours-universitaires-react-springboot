import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import enseignantService from '../services/enseignantService'

function EnseignantListe() {
  const [enseignants, setEnseignants] = useState([])
  const [erreur, setErreur] = useState(null)

  useEffect(() => {
    enseignantService.getAll()
      .then((reponse) => setEnseignants(reponse.data))
      .catch(() => setErreur("Impossible de charger la liste des enseignants."))
  }, [])

  const handleSupprimer = (telephone) => {
    if (!window.confirm('Supprimer cet enseignant ?')) return
    enseignantService.remove(telephone)
      .then(() => setEnseignants(enseignants.filter((e) => e.telephone !== telephone)))
      .catch(() => setErreur("Suppression impossible (cet enseignant a peut-être encore des cours assignés)."))
  }

  return (
    <div className="page">
      <h1>Liste des enseignants</h1>
      <Link to="/admin/enseignants/ajouter">+ Ajouter un enseignant</Link>
      <br/><br/>

      {erreur && <p style={{ color: 'red' }}>{erreur}</p>}

      <table border="1" cellPadding="8">
        <thead>
          <tr><th>Téléphone</th><th>Nom</th><th>Actions</th></tr>
        </thead>
        <tbody>
          {enseignants.map((e) => (
            <tr key={e.telephone}>
              <td>{e.telephone}</td>
              <td>{e.nom}</td>
              <td>
                <Link to={`/admin/enseignants/modifier/${e.telephone}`}>Modifier</Link>
                {' | '}
                <button onClick={() => handleSupprimer(e.telephone)}>Supprimer</button>
              </td>
            </tr>
          ))}
          {enseignants.length === 0 && <tr><td colSpan="3">Aucun enseignant trouvé.</td></tr>}
        </tbody>
      </table>
    </div>
  )
}

export default EnseignantListe