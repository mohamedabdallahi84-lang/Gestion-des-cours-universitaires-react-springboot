package com.example.gestion_cours.securite;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Un filtre s'exécute AVANT chaque requête arrivant sur le serveur,
// pour vérifier si la personne est authentifiée.
@Component
@RequiredArgsConstructor
public class FiltreAuthentification extends OncePerRequestFilter {

    private final GestionnaireSessions gestionnaireSessions;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Le jeton est envoyé par le client dans l'en-tête HTTP "Authorization",
        // sous la forme "Bearer <jeton>".
        String enTete = request.getHeader("Authorization");

        if (enTete != null && enTete.startsWith("Bearer ")) {
            String jeton = enTete.substring(7); // enlève "Bearer " pour garder juste le jeton
            InfoConnexion info = gestionnaireSessions.getInfoConnexion(jeton);

            if (info != null) {
                // On informe Spring Security que cette requête est authentifiée,
                // avec le rôle préfixé par "ROLE_" (convention attendue par Spring Security).
                var authentication = new UsernamePasswordAuthenticationToken(
                        info.getIdentifiant(), null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + info.getRole())));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // On laisse la requête continuer son chemin normal après ce filtre.
        filterChain.doFilter(request, response);
    }
}