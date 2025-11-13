package it.solobanking.backend.repository;

import it.solobanking.backend.model.Conto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ContoRepository extends JpaRepository<Conto, Long> {
    Optional<Conto> findByIban(String iban);
    List<Conto> findByUtenteId(Long utenteId);
}
