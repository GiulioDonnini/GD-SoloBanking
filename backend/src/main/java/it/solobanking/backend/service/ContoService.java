package it.solobanking.backend.service;

import it.solobanking.backend.model.Conto;
import it.solobanking.backend.model.Utente;
import it.solobanking.backend.repository.ContoRepository;
import it.solobanking.backend.repository.UtenteRepository;
import it.solobanking.backend.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ContoService {

    private final ContoRepository contoRepository;
    private final UtenteRepository utenteRepository;
    private final TransazioneService transazioneService;
    private final JwtService jwtService;

    public ContoService(ContoRepository contoRepository,
                        UtenteRepository utenteRepository,
                        TransazioneService transazioneService,
                        JwtService jwtService) {
        this.contoRepository = contoRepository;
        this.utenteRepository = utenteRepository;
        this.transazioneService = transazioneService;
        this.jwtService = jwtService;
    }

    public Double getSaldo(String iban) {
        Conto conto = contoRepository.findByIban(iban)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conto non trovato"));
        return conto.getSaldo();
    }

    public Conto getContoUtente(HttpServletRequest request) {
        String email = jwtService.estraiEmailRichiesta(request);
        if (email == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido o mancante");

        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato"));

        return contoRepository.findByUtenteId(utente.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conto non trovato"));
    }

    @Transactional
    public void eseguiBonifico(String ibanMittente, String ibanDestinatario, Double importo, String descrizione) {
        Conto mittente = contoRepository.findByIban(ibanMittente)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conto mittente non trovato"));
        Conto destinatario = contoRepository.findByIban(ibanDestinatario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conto destinatario non trovato"));

        if (mittente.getSaldo() < importo)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insufficiente");

        mittente.setSaldo(mittente.getSaldo() - importo);
        destinatario.setSaldo(destinatario.getSaldo() + importo);

        contoRepository.save(mittente);
        contoRepository.save(destinatario);

        transazioneService.creaTransazione(mittente, -importo, descrizione, LocalDateTime.now());
        transazioneService.creaTransazione(destinatario, importo, descrizione, LocalDateTime.now());
    }
}
