import { createContext, useContext, useState } from 'react'

// Un Context permet de partager une donnée (ici : qui est connecté) à TOUS
// les composants de l'application, sans avoir à la transmettre manuellement
// de composant en composant.
const AuthContext = createContext(null)

// Relit l'utilisateur déjà connecté depuis localStorage au démarrage,
// pour rester connecté même après avoir fermé/rouvert le navigateur.
function lireUtilisateurStocke() {
  const donnees = localStorage.getItem('utilisateur')
  return donnees ? JSON.parse(donnees) : null
}

export function AuthProvider({ children }) {
  const [utilisateur, setUtilisateur] = useState(lireUtilisateurStocke())

  // Appelée après une connexion réussie (admin ou professeur).
  const connecter = (jeton, role, identifiant, nom) => {
    const nouvelUtilisateur = { role, identifiant, nom }
    localStorage.setItem('jeton', jeton)
    localStorage.setItem('utilisateur', JSON.stringify(nouvelUtilisateur))
    setUtilisateur(nouvelUtilisateur)
  }

  const deconnecter = () => {
    localStorage.removeItem('jeton')
    localStorage.removeItem('utilisateur')
    setUtilisateur(null)
  }

  return (
    <AuthContext.Provider value={{ utilisateur, connecter, deconnecter }}>
      {children}
    </AuthContext.Provider>
  )
}

// Hook personnalisé : permet à n'importe quel composant d'utiliser
// "const { utilisateur } = useAuth()" pour savoir qui est connecté.
export function useAuth() {
  return useContext(AuthContext)
}