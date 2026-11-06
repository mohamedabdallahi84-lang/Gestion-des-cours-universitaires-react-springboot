//Importe la librairie Axios, utilisée pour faire des requêtes HTTP (GET, POST, PUT, DELETE) vers ton backend.
import axios from 'axios'

// Une seule instance axios pour TOUTES les requêtes vers le back-end.
const api = axios.create({
  baseURL: 'http://localhost:8080',
})

// Un "intercepteur" : du code qui s'exécute automatiquement avant CHAQUE
// requête envoyée via cette instance. Ici, on ajoute le jeton (s'il existe)
// dans l'en-tête "Authorization", pour que les routes protégées fonctionnent.
api.interceptors.request.use((config) => {
  const jeton = localStorage.getItem('jeton')
  if (jeton) {
    config.headers.Authorization = `Bearer ${jeton}`
  }
  return config
})

export default api