package org.example;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Disponibilite")

public class Disponibilite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_disponibilite;

    @Column(name ="heure_debut", nullable = false)
    private Date heureDebut;

    @Column(name ="heure_fin", nullable = false)
    private Date heureFin;

    @Temporal(TemporalType.DATE)
    @Column(name ="date_disponibilite", nullable = false)
    private Date dateDisponibilite;

    @ManyToOne
    @JoinColumn(name = "id_infirmiere", nullable = false)
    private Infirmiere id_infirmiere;

    public Disponibilite() {}
    public Disponibilite(int id_disponibilite, Date heureDebut, Date heureFin, Date dateDisponibilite, Infirmiere id_infirmiere) {
        this.id_disponibilite = id_disponibilite;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.dateDisponibilite = dateDisponibilite;
        this.id_infirmiere = id_infirmiere;
    }

    public int getId_disponibilite() {
        return id_disponibilite;
    }

    public void setId_disponibilite(int id_disponibilite) {
        this.id_disponibilite = id_disponibilite;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public Date getDateDisponibilite() {
        return dateDisponibilite;
    }

    public void setDateDisponibilite(Date dateDisponibilite) {
        this.dateDisponibilite = dateDisponibilite;
    }

    public Infirmiere getId_infirmiere() {
        return id_infirmiere;
    }

    public void setId_infirmiere(Infirmiere id_infirmiere) {
        this.id_infirmiere = id_infirmiere;
    }
}
