import api from './api'

const BASE = '/api/enseignants'

const enseignantService = {
  getAll: () => api.get(BASE),
  create: (enseignant) => api.post(BASE, enseignant),
  getByTelephone: (telephone) => api.get(`${BASE}/${telephone}`),
  update: (telephone, enseignant) => api.put(`${BASE}/${telephone}`, enseignant),
  remove: (telephone) => api.delete(`${BASE}/${telephone}`),
}

export default enseignantService