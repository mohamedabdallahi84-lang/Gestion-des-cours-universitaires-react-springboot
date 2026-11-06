import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../services/api'
import GrilleEmploiDuTemps from './GrilleEmploiDuTemps.jsx'

function Consultation() {
  const [filieres, setFilieres] = useState([])
  const [semestres, setSemestres] = useState([])
  const [groupes, setGroupes] = useState([])
  const [emploi, setEmploi] = useState(null)

  const [filiereId, setFiliereId] = useState('')
  const [semestreId, setSemestreId] = useState('')
  const [groupeId, setGroupeId] = useState('')

  useEffect(() => {
    api.get('/api/public/filieres').then((r) => setFilieres(r.data))
    api.get('/api/public/semestres').then((r) => setSemestres(r.data))
  }, [])

  useEffect(() => {
    if (filiereId && semestreId) {
      api.get('/api/public/groupes', { params: { filiereId, semestreId } }).then((r) => setGroupes(r.data))
      setGroupeId('')
      setEmploi(null)
    }
  }, [filiereId, semestreId])

  useEffect(() => {
    if (groupeId) {
      api.get('/api/public/emploi-du-temps', { params: { groupeId } }).then((r) => setEmploi(r.data))
    }
  }, [groupeId])

  return (
    <div className="page">
      <h1>Consulter l'emploi du temps</h1>
      <Link to="/">Retour à l'accueil</Link><br/><br/>

      <label>Filière<br/>
        <select value={filiereId} onChange={(e) => setFiliereId(e.target.value)}>
          <option value="">-- Choisir --</option>
          {filieres.map((f) => <option key={f.id} value={f.id}>{f.nom}</option>)}
        </select>
      </label><br/><br/>

      <label>Semestre<br/>
        <select value={semestreId} onChange={(e) => setSemestreId(e.target.value)}>
          <option value="">-- Choisir --</option>
          {semestres.map((s) => <option key={s.id} value={s.id}>{s.nom}</option>)}
        </select>
      </label><br/><br/>

      {groupes.length > 0 && (
        <label>Groupe<br/>
          <select value={groupeId} onChange={(e) => setGroupeId(e.target.value)}>
            <option value="">-- Choisir --</option>
            {groupes.map((g) => <option key={g.id} value={g.id}>{g.nom}</option>)}
          </select>
        </label>
      )}

      {emploi && (
        <div style={{ marginTop: '20px' }}>
          <GrilleEmploiDuTemps cours={emploi.filter((c) => c.statut === 'CONFIRME')} />
        </div>
      )}
    </div>
  )
}

export default Consultation