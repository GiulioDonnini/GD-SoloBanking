package it.solobanking.backend.controller;

import it.solobanking.backend.model.Conto;
import it.solobanking.backend.service.ContoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conto")
public class ContoController {

    private final ContoService contoService;

    public ContoController(ContoService contoService) {
        this.contoService = contoService;
    }

    @GetMapping("/personale")
    public ResponseEntity<?> getContoUtente(HttpServletRequest request) {
        try {
            Conto conto = contoService.getContoUtente(request);
            return ResponseEntity.ok(conto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/saldo/{iban}")
    public ResponseEntity<Double> getSaldo(@PathVariable String iban) {
        Double saldo = contoService.getSaldo(iban);
        return ResponseEntity.ok(saldo);
    }

    @PostMapping("/bonifico")
    public ResponseEntity<String> eseguiBonifico(@RequestBody BonificoRequest request) {
        contoService.eseguiBonifico(
                request.getIbanMittente(),
                request.getIbanDestinatario(),
                request.getImporto(),
                request.getDescrizione()
        );
        return ResponseEntity.ok("Bonifico eseguito correttamente");
    }

    public static class BonificoRequest {
        private String ibanMittente;
        private String ibanDestinatario;
        private Double importo;
        private String descrizione;

        public String getIbanMittente() { return ibanMittente; }
        public void setIbanMittente(String ibanMittente) { this.ibanMittente = ibanMittente; }

        public String getIbanDestinatario() { return ibanDestinatario; }
        public void setIbanDestinatario(String ibanDestinatario) { this.ibanDestinatario = ibanDestinatario; }

        public Double getImporto() { return importo; }
        public void setImporto(Double importo) { this.importo = importo; }

        public String getDescrizione() { return descrizione; }
        public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    }
}
