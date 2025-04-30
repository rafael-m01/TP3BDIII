package org.example;
import javax.persistence.*;


@Entity
@Table(name = "Client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Long id;

    @Column(nullable = false, length = 20)
    private String nom;

    @Column(nullable = false, length = 25)
    private String courriel;

    @Column(name = "mot_de_passe", nullable = false, length = 25)
    private String motDePasse;
}
