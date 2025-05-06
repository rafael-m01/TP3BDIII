package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
// remplace par ton entité réelle

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
        List<String> noms =lireFichier("TP3RJ/src/main/resources/Nom.txt");

        List<String> prenoms = lireFichier("TP3RJ/src/main/resources/prenom.txt");

        Set<String> clients = genererPersonnesUniques(prenoms, noms, 5000);
        Set<String> infirmieres = genererPersonnesUniques(prenoms, noms, 100);

        // Affichage
        int count = 1;
        for (String fullName : clients) {
            System.out.println(count++ + ". " + fullName);
        }
        for (String fullName : infirmieres) {
            System.out.println(count++ + ". " + fullName);
        }



        // Test simple
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            System.out.println("Hibernate fonctionne !");
            session.getTransaction().commit();



        }

        sessionFactory.close();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();



            session.getTransaction().commit();



        }

        sessionFactory.close();




    }
    // Génère les combinaisons uniques
    public static Set<String> genererPersonnesUniques(List<String> prenoms, List<String> noms, int nombre) {
        Set<String> resultats = new LinkedHashSet<>();
        Random random = new Random();

        int maxCombinations = prenoms.size() * noms.size();
        if (nombre > maxCombinations) {
            throw new IllegalArgumentException("Impossible de générer " + nombre + " noms uniques avec seulement " + maxCombinations + " combinaisons possibles.");
        }

        while (resultats.size() < nombre) {
            String prenom = prenoms.get(random.nextInt(prenoms.size()));
            String nom = noms.get(random.nextInt(noms.size()));
            resultats.add(prenom + " " + nom); // ajoute seulement si unique
        }

        return resultats;
    }
    private static void listerInfirmieres(SessionFactory sessionFactory) {
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
