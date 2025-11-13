package it.solobanking.backend.service;

import it.solobanking.backend.model.Conto;
import it.solobanking.backend.model.Transazione;
import it.solobanking.backend.repository.ContoRepository;
import it.solobanking.backend.repository.TransazioneRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ContoService {

    private final ContoRepository contoRepository;
    private final TransazioneRepository transazioneRepository;

    public ContoService(ContoRepository contoRepository, TransazioneRepository transazioneRepository) {
        this.contoRepository = contoRepository;
        this.transazioneRepository = transazioneRepository;
    }

    public Double getSaldo(String iban) {
        Optional<Conto> contoOpt = contoRepository.findByIban(iban);
        if (contoOpt.isEmpty()) {
            throw new RuntimeException("Conto non trovato");
        }
        return contoOpt.get().getSaldo();
    }

    @Transactional
    public void eseguiBonifico(String ibanMittente, String ibanDestinatario, Double importo, String causale) {
        Conto mittente = contoRepository.findByIban(ibanMittente)
                .orElseThrow(() -> new RuntimeException("Conto mittente non trovato"));
        Conto destinatario = contoRepository.findByIban(ibanDestinatario)
                .orElseThrow(() -> new RuntimeException("Conto destinatario non trovato"));

        if (mittente.getSaldo() < importo) {
            throw new RuntimeException("Saldo insufficiente");
        }

        mittente.setSaldo(mittente.getSaldo() - importo);
        destinatario.setSaldo(destinatario.getSaldo() + importo);

        contoRepository.save(mittente);
        contoRepository.save(destinatario);

        Transazione t1 = new Transazione(mittente, -importo, causale, LocalDateTime.now());
        Transazione t2 = new Transazione(destinatario, importo, causale, LocalDateTime.now());
        transazioneRepository.save(t1);
        transazioneRepository.save(t2);
    }
}
