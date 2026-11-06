import axios from 'axios'

const API_URL = 'http://localhost:8080/api/enseignants'

const enseignantService = {
  getAll: () => axios.get(API_URL),
  create: (enseignant) => axios.post(API_URL, enseignant),
  getByTelephone: (telephone) => axios.get(`${API_URL}/${telephone}`),
  update: (telephone, enseignant) => axios.put(`${API_URL}/${telephone}`, enseignant),
  remove: (telephone) => axios.delete(`${API_URL}/${telephone}`),
}

export default enseignantService