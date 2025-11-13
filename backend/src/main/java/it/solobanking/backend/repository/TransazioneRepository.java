package it.solobanking.backend.repository;

import it.solobanking.backend.model.Transazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransazioneRepository extends JpaRepository<Transazione, Long> {
    List<Transazione> findByContoId(Long contoId);
}
