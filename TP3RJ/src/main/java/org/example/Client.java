package org.example;
import javax.persistence.*;


@Entity
@Table(name = "Client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private int id;

    @Column(name="nom", nullable = false, length = 20)
    private String nom;

    @Column(name="prenom", nullable = false, length = 20)
    private String prenom;

    @Column(name="courriel", nullable = false, length = 25)
    private String courriel;

    @Column(name = "mot_de_passe", nullable = false, length = 25)
    private String motDePasse;

    public Client() {}
    public Client(String nom, String prenom, String courriel, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.courriel = courriel;
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
