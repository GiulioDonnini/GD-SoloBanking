package it.solobanking.backend.controller;

import it.solobanking.backend.model.Utente;
import it.solobanking.backend.repository.UtenteRepository;
import it.solobanking.backend.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Utente utente) {
        if (utenteRepository.findByEmail(utente.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email già registrata");
        }

        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utenteRepository.save(utente);
        return ResponseEntity.ok("Registrazione completata con successo");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utente richiesta) {
        Optional<Utente> userOpt = utenteRepository.findByEmail(richiesta.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Utente non trovato");
        }

        Utente user = userOpt.get();
        if (!passwordEncoder.matches(richiesta.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Password errata");
        }

        String token = jwtService.generaToken(user.getEmail());
        return ResponseEntity.ok(token);
    }
}
