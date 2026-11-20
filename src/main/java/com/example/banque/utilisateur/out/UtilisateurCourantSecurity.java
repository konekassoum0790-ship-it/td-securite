package com.example.banque.utilisateur.out;

import com.example.banque.utilisateur.Utilisateur;
import com.example.banque.utilisateur.UtilisateurRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UtilisateurCourantSecurity implements UtilisateurCourant {

    private final UtilisateurRepository utilisateurs;

    public UtilisateurCourantSecurity(UtilisateurRepository utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    @Override
    public Optional<Utilisateur> get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Optional.empty();
        }
        return utilisateurs.findByLogin(auth.getName());
    }
}