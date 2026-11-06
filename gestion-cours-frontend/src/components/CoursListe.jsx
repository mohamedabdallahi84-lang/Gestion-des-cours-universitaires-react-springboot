import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import coursService from '../services/coursService'

function CoursListe() {
  const [coursList, setCoursList] = useState([])
  const [recherche, setRecherche] = useState('')
  const [erreur, setErreur] = useState(null)

  useEffect(() => {
    chargerTousLesCours()
  }, [])

  const chargerTousLesCours = () => {
    coursService
      .getAll()
      .then((reponse) => setCoursList(reponse.data))
      .catch(() => setErreur('Impossible de charger la liste des cours.'))
  }

  const handleRecherche = (e) => {
    e.preventDefault()
    if (recherche.trim() === '') {
      chargerTousLesCours()
      return
    }
    coursService
      .rechercher(recherche)
      .then((reponse) => setCoursList(reponse.data))
      .catch(() => setErreur('La recherche a échoué.'))
  }

  const handleSupprimer = (id) => {
    if (!window.confirm('Supprimer ce cours ?')) return
    coursService
      .remove(id)
      .then(() => setCoursList(coursList.filter((c) => c.id !== id)))
      .catch(() => setErreur('La suppression a échoué.'))
  }

  return (
    <div className="page">
      <h1>Liste des cours</h1>

      <Link to="/admin/cours/ajouter">+ Ajouter un cours</Link>
      {' | '}<Link to="/admin/enseignants">Gérer les enseignants</Link>

      <form onSubmit={handleRecherche}>
        <input
          type="text"
          placeholder="Rechercher par nom de cours..."
          value={recherche}
          onChange={(e) => setRecherche(e.target.value)}
        />
        <button type="submit">Rechercher</button>
      </form>

      {erreur && <p style={{ color: 'red' }}>{erreur}</p>}

      <table border="1" cellPadding="8">
        <thead>
          <tr>
            <th>Nom</th>
            <th>Enseignant</th>
            <th>Filière</th>
            <th>Semestre</th>
            <th>Groupe</th>
            <th>Crédits</th>
            <th>Jour</th>
            <th>Horaire</th>
            <th>Date</th>
            <th>Statut</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {coursList.map((cours) => (
            <tr key={cours.id}>
              <td>{cours.nom}</td>
              <td>{cours.enseignant ? cours.enseignant.nom : ''}</td>
              {/* filiere et semestre viennent maintenant du groupe, plus de cours directement */}
              <td>{cours.groupe ? cours.groupe.filiere.nom : ''}</td>
              <td>{cours.groupe ? cours.groupe.semestre.nom : ''}</td>
              <td>{cours.groupe ? cours.groupe.nom : ''}</td>
              <td>{cours.credits}</td>
              <td>{cours.jour}</td>
              <td>{cours.horaire}</td>
              <td>{cours.date}</td>
              <td>{cours.statut}</td>
              <td>
                <Link to={`/admin/cours/modifier/${cours.id}`}>Modifier</Link>
                {' | '}
                <button onClick={() => handleSupprimer(cours.id)}>Supprimer</button>
              </td>
            </tr>
          ))}
          {coursList.length === 0 && (
            <tr><td colSpan="11">Aucun cours trouvé.</td></tr>
          )}
        </tbody>
      </table>
    </div>
  )
}

export default CoursListe