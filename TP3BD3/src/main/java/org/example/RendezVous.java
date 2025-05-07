package org.example;
import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Rendez_Vous")
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rendez_vous")
    private int id;

    @Column(name = "rendez_vous_date", nullable = false)
    private LocalDate date;

    @Column(name = "rendez_vous_heure_debut", nullable = false)
    private Timestamp heureDebut;

    @Column(name = "rendez_vous_heure_fin", nullable = false)
    private Timestamp heureFin;

    @ManyToOne
    @JoinColumn(name = "Client_id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "Infirmiere_id_infirmiere", nullable = false)
    private Infirmiere infirmiere;

    @ManyToOne
    @JoinColumn(name = "Service_id_service", nullable = false)
    private Service service;

    public RendezVous() {}

    public RendezVous(int id, LocalDate date, Timestamp heureDebut, Timestamp heureFin, Client client, Infirmiere infirmiere, Service service) {
        this.id = id;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.client = client;
        this.infirmiere = infirmiere;
        this.service = service;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Timestamp getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Timestamp heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Timestamp getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Timestamp heureFin) {
        this.heureFin = heureFin;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Infirmiere getInfirmiere() {
        return infirmiere;
    }

    public void setInfirmiere(Infirmiere infirmiere) {
        this.infirmiere = infirmiere;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
