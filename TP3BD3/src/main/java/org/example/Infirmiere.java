
package org.example;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Infirmiere")
public class Infirmiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_infirmiere;

    @Column(name= "nom", nullable = false)
    private String nom;

    @Column(name= "prenom", nullable = false)
    private String prenom;

    public Infirmiere() {}

    public Infirmiere(int id_infirmiere, String nom, String prenom) {
        this.id_infirmiere = id_infirmiere;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getId_infirmiere() {
        return id_infirmiere;
    }

    public void setId_infirmiere(int id_infirmiere) {
        this.id_infirmiere = id_infirmiere;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
