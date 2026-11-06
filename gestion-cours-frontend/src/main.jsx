// React est la bibliothèque de base, nécessaire pour utiliser JSX
// (la syntaxe qui ressemble à du HTML dans le code JavaScript).
import React from 'react'

// ReactDOM est la bibliothèque qui sait "afficher" un composant React
// à l'intérieur d'une vraie page HTML, dans le navigateur.
import ReactDOM from 'react-dom/client'

// BrowserRouter est le composant fourni par react-router-dom
// qui active la navigation entre les vues sans rechargement de page,
// en lisant et modifiant l'URL du navigateur.
import { BrowserRouter } from 'react-router-dom'

// On importe notre composant principal App, qu'on va écrire/modifier juste après.
import App from './App.jsx'
import { AuthProvider } from './context/AuthContext.jsx'
// Le fichier de styles CSS de l'application (généré par Vite, on le garde).
import './index.css'

// document.getElementById('root') récupère la balise <div id="root"></div>
// qui se trouve dans le fichier index.html (le point d'ancrage de toute l'application).
// .createRoot(...) prépare cet endroit pour y afficher du React.
ReactDOM.createRoot(document.getElementById('root')).render(

  // React.StrictMode est un mode de développement qui aide à détecter
  // des erreurs ou mauvaises pratiques plus tôt (il n'affecte pas le résultat final affiché).
  <React.StrictMode>

    {/* On enveloppe TOUTE l'application dans BrowserRouter :
        c'est ce qui permet à n'importe quel composant à l'intérieur
        de connaître l'URL actuelle et de naviguer sans recharger la page. */}
    <BrowserRouter>
 <AuthProvider>
      {/* Notre composant principal, qui contient toute la logique de l'application. */}
      <App />
</AuthProvider>
    </BrowserRouter>
  </React.StrictMode>,
)