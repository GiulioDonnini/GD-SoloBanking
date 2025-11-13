package it.solobanking.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "conti")
public class Conto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String iban;

    @Column(nullable = false)
    private Double saldo;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    public Conto() {}

    public Conto(String iban, Double saldo, Utente utente) {
        this.iban = iban;
        this.saldo = saldo;
        this.utente = utente;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
