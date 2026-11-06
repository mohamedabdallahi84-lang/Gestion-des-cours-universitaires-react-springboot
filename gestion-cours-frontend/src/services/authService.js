//Importe l'instance Axios configurée qu'on vient de voir (avec la baseURL et l'intercepteur du token).

import api from './api'
// C'est un objet qui regroupe toutes les fonctions liées à l'authentification, chacune correspondant à un endpoint précis de le  backend Spring Boot
const authService = {
  inscription: (donnees) => api.post('/api/auth/inscription', donnees),
  connexionAdmin: (donnees) => api.post('/api/auth/connexion-admin', donnees),
  connexionProfesseur: (donnees) => api.post('/api/auth/connexion-professeur', donnees),
}

export default authService