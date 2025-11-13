package it.solobanking.backend.service;

import it.solobanking.backend.model.Utente;
import it.solobanking.backend.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public Utente registraUtente(Utente utente) {
        return utenteRepository.save(utente);
    }

    public Optional<Utente> trovaPerEmail(String email) {
        return utenteRepository.findByEmail(email);
    }
}

