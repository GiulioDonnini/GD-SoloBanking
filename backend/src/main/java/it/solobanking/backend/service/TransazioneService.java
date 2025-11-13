package it.solobanking.backend.service;

import it.solobanking.backend.model.Transazione;
import it.solobanking.backend.repository.TransazioneRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransazioneService {

    private final TransazioneRepository transazioneRepository;

    public TransazioneService(TransazioneRepository transazioneRepository) {
        this.transazioneRepository = transazioneRepository;
    }

    public List<Transazione> listaPerConto(Long contoId) {
        return transazioneRepository.findByContoId(contoId);
    }
}
