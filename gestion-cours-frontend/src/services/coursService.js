import api from './api'

const BASE = '/api/cours'

const coursService = {
  getAll: () => api.get(BASE),
  getById: (id) => api.get(`${BASE}/${id}`),
  create: (cours) => api.post(BASE, cours),
  update: (id, cours) => api.put(`${BASE}/${id}`, cours),
  remove: (id) => api.delete(`${BASE}/${id}`),
  rechercher: (nom) => api.get(`${BASE}/recherche`, { params: { nom } }),
}

export default coursService