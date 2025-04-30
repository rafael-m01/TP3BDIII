package org.example;
import javax.persistence.*;
import java.time.Duration;

@Entity
@Table(name = "Service")

public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service")
    private int id;

    @Column(name = "nom_service", nullable = false, length = 25)
    private String nom;

    @Column(nullable = false)
    private Duration duree;

    @Column(nullable = false)
    private Double prix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Duration getDuree() {
        return duree;
    }

    public void setDuree(Duration duree) {
        this.duree = duree;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }
}
