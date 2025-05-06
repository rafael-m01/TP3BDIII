package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;



import java.util.List;
import java.util.Scanner;
// remplace par ton entité réelle

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        // Configuration des propriétés Hibernate
        configuration.setProperty("hibernate.connection.driver_class", "oracle.jdbc.OracleDriver");
        configuration.setProperty("hibernate.connection.url", "jdbc:oracle:thin:@//localhost:1521/FREEPDB1"); // adapte l'URL
        configuration.setProperty("hibernate.connection.username", "TP3RJ");
        configuration.setProperty("hibernate.connection.password", "Soleil01");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update"); // ou validate, create-drop, etc.
        configuration.setProperty("hibernate.show_sql", "true");

        // Ajoute tes classes annotées (@Entity)
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(Disponibilite.class);
        configuration.addAnnotatedClass(InfirmiereService.class);
        configuration.addAnnotatedClass(RendezVous.class);
        configuration.addAnnotatedClass(Service.class);
        configuration.addAnnotatedClass(Infirmiere.class);// Ajoute toutes tes entités ici

        // Crée le ServiceRegistry
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        // Crée le SessionFactory
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        //extraire Noms:
        List<String> noms = new ArrayList<>();

        noms =lireFichier("TP3RJ/src/main/resources/Nom.txt");

        List<String> mots = noms;


        // Test simple
        Scanner scanner = new Scanner(System.in);
        int choix;
        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Lister les infirmières");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    ajouterClient(scanner);
                    break;
                case 2:
                    listerInfirmieres();
                    break;
                case 0:
                    System.out.println("Fin du programme.");
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        } while (choix != 0);

        sessionFactory.close();
        scanner.close();
    }


    private static void ajouterClient(Scanner scanner) {
        System.out.println("\n--- Ajouter un client ---");
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        System.out.print("Courriel : ");
        String courriel = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        Client client = new Client();
        client.setNom(nom);
        client.setCourriel(courriel);
        client.setMotDePasse(motDePasse);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(client);
            session.getTransaction().commit();
            System.out.println("Client ajouté avec succès !");



        }

        sessionFactory.close();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();



            session.getTransaction().commit();



        }
    }

    private static void listerInfirmieres() {
        System.out.println("\n--- Liste des infirmières ---");
        try (Session session = sessionFactory.openSession()) {
            List<Infirmiere> infirmieres = session.createQuery("from Infirmiere", Infirmiere.class).getResultList();
            for (Infirmiere i : infirmieres) {
                System.out.println("ID: " + i.getId_infirmiere() + " | Nom: " + i.getNom());
            }
        }
    }

    public static int saisirOption(String message){
        Scanner sc = new Scanner(System.in);
        System.out.println(message);
        return sc.nextInt();
    }
    public static List<String> lireFichier(String chemin){
        List<String> mots = new ArrayList<>();

        try {
            // Lire toutes les lignes du fichier
            List<String> lignes = Files.readAllLines(Paths.get(chemin));

            for (String ligne : lignes) {
                // Découper chaque ligne en mots (en se basant sur les espaces et ponctuations)
                String[] motsLigne = ligne.split("\\W+"); // \\W+ = tout ce qui n'est pas un caractère alphanumérique

                for (String mot : motsLigne) {
                    if (!mot.isEmpty()) {
                        mots.add(mot);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }

        return mots;
    }

    }
