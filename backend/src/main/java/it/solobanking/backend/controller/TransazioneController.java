package it.solobanking.backend.controller;

import it.solobanking.backend.model.Transazione;
import it.solobanking.backend.service.TransazioneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transazioni")
public class TransazioneController {

    private final TransazioneService transazioneService;

    public TransazioneController(TransazioneService transazioneService) {
        this.transazioneService = transazioneService;
    }

    @GetMapping("/{contoId}")
    public ResponseEntity<List<Transazione>> getTransazioni(@PathVariable Long contoId) {
        List<Transazione> movimenti = transazioneService.listaPerConto(contoId);
        return ResponseEntity.ok(movimenti);
    }
}
