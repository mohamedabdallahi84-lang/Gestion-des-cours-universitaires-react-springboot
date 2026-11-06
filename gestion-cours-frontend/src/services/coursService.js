// axios est la bibliothèque qui permet d'envoyer des requêtes HTTP
// (GET, POST, PUT, DELETE) depuis le code JavaScript du navigateur.
import axios from 'axios'

// L'adresse de base de ton API Spring Boot, pour tout ce qui concerne les cours.
const API_URL = 'http://localhost:8080/api/cours'

// On regroupe toutes les fonctions liées aux cours dans un seul objet,
// pour pouvoir faire "coursService.getAll()" depuis n'importe quel composant.
const coursService = {

  // Besoin "Afficher" (tous les cours) : envoie un GET vers /api/cours.
  getAll: () => axios.get(API_URL),

  // Besoin "Afficher" (un cours précis) : GET vers /api/cours/{id}.
  // Les ` ` (backticks) permettent d'insérer une variable dans le texte avec ${...}.
  getById: (id) => axios.get(`${API_URL}/${id}`),

  // Besoin "Ajouter" : envoie un POST avec l'objet cours en corps de requête.
  create: (cours) => axios.post(API_URL, cours),

  // Besoin "Modifier" : envoie un PUT vers /api/cours/{id} avec les nouvelles données.
  update: (id, cours) => axios.put(`${API_URL}/${id}`, cours),

  // Besoin "Supprimer" : envoie un DELETE vers /api/cours/{id}.
  remove: (id) => axios.delete(`${API_URL}/${id}`),

  // Besoin "Rechercher" : envoie un GET vers /api/cours/recherche?nom=...
  // { params: { nom } } ajoute automatiquement "?nom=valeur" à l'URL.
  rechercher: (nom) => axios.get(`${API_URL}/recherche`, { params: { nom } }),
}

// On exporte cet objet pour pouvoir l'importer dans d'autres fichiers.
export default coursService