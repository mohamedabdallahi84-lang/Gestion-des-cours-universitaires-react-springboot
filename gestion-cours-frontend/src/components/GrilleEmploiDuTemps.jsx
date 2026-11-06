const JOURS = ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi']
const HORAIRES = [
  '08:30 - 09:30', '09:30 - 10:30', '10:30 - 11:30', '11:30 - 12:30',
  '14:00 - 15:00', '15:00 - 16:00', '16:00 - 17:00', '17:00 - 18:00',
  '08:30 - 10:30', '10:30 - 12:30', '14:00 - 16:00', '16:00 - 18:00',
]

function GrilleEmploiDuTemps({ cours }) {
  const trouverCours = (jour, horaire) => cours.filter((c) => c.jour === jour && c.horaire === horaire)

  return (
    <table border="1" cellPadding="8">
      <thead>
        <tr><th>Horaire</th>{JOURS.map((j) => <th key={j}>{j}</th>)}</tr>
      </thead>
      <tbody>
        {HORAIRES.map((horaire) => (
          <tr key={horaire}>
            <td>{horaire}</td>
            {JOURS.map((jour) => (
              <td key={jour}>
                {trouverCours(jour, horaire).map((c) => <div key={c.id}>{c.nom}</div>)}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  )
}

export default GrilleEmploiDuTemps