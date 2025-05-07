package org.example;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
        //configuration.setProperty("hibernate.hbm2ddl.auto", "update"); // ou validate, create-drop, etc.
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
        List<String> noms = lireFichier("Nom.txt");

        List<String> prenoms = lireFichier("Prenom.txt");

        Set<String> clients = genererPersonnesUniques(prenoms, noms, 5000);
        Set<String> infirmieres = genererPersonnesUniques(prenoms, noms, 100);


        // Test simple
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (String valeur: clients) {

                int i = 0;
                String[] nomComplet = valeur.split(" ");
                Client client = new Client();
                client.setPrenom(nomComplet[0]);
                client.setNom(nomComplet[1]);
                client.setCourriel(client.getPrenom() + "." + client.getNom() + "@tp3.com");
                client.setMotDePasse(hashMotDePasse(client.getNom() + client.getCourriel() + "123"));

                session.save(client);
                i++;

                if (i % 50 == 0) {
                    session.flush();
                    session.clear();
                }
            }
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (String valeur: infirmieres) {

                int conteur = 0;
                String[] nomComplet = valeur.split(" ");
                Infirmiere infirmiere = new Infirmiere();
                infirmiere.setPrenom(nomComplet[0]);
                infirmiere.setNom(nomComplet[1]);

                session.save(infirmiere);
                session.flush();            // force l'envoi vers la BD
                session.refresh(infirmiere); // recharge infirmiere avec son ID généré

                for (int i = 0; i < 199; i++) {
                    Disponibilite disponibilite = genererDisponibiliteAleatoire();
                    disponibilite.setId_infirmiere(infirmiere); // infirmiere avec id non-null
                    session.save(disponibilite);
                }

                conteur++;

                if (conteur % 50 == 0) {
                    session.flush();
                    session.clear();
                }
            }

            session.getTransaction().commit();
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(new Service("Prélèvement sanguin",15, 40.00));
            session.save(new Service("Lavage d'oreille",25, 45.00));
            session.save(new Service("Injection de médication",10, 40.00));
            session.save(new Service("Extraction de points de suture",15, 45.00));
            session.save(new Service("Suivi de plaies",20, 40.00));
            session.save(new Service("Évaluation de la condition de santé", 30, 65.00));

            session.flush();
            session.clear();
            session.getTransaction().commit();

        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Infirmiere> infirmieresBD = session.createQuery("FROM Infirmiere", Infirmiere.class).getResultList();
            List<Service> serviceBD = session.createQuery("FROM Service", Service.class).getResultList();

            // Mélanger la liste d'infirmières pour éviter un traitement ordonné.
            Collections.shuffle(infirmieresBD);

            for (Infirmiere infirmiere : infirmieresBD) {
                // Important:  No need for  i  here, and the loop iterates through each infirmiere

                if (infirmieresBD.indexOf(infirmiere) < (int) (infirmieresBD.size() * 0.6)) {
                    // 60% : tous les services
                    for (Service service : serviceBD) {
                        InfirmiereService infirmiereService = new InfirmiereService(new InfirmiereServiceId(infirmiere.getId_infirmiere(), service.getId()));
                        infirmiereService.setDureePersonalisee(0);
                        session.save(infirmiereService);
                    }
                } else if (infirmieresBD.indexOf(infirmiere) < (int) (infirmieresBD.size() * 0.8)) {
                    // 20% : uniquement "Prélèvement sanguin" ou "Suivi de plaies" avec durée personnalisée
                    for (Service service : serviceBD) {
                        String nom = service.getNom();
                        if ("Prélèvement sanguin".equals(nom) || "Suivi de plaies".equals(nom)) { // Use .equals()
                            InfirmiereService is = new InfirmiereService(new InfirmiereServiceId(infirmiere.getId_infirmiere(), service.getId()));
                            is.setDureePersonalisee(5);
                            session.save(is);
                        }
                    }
                } else {
                    // Derniers 20% : uniquement "Lavage d'oreille" avec durée négative
                    for (Service service : serviceBD) {
                        if ("Lavage d'oreille".equals(service.getNom())) {  // Use .equals()
                            InfirmiereService is = new InfirmiereService(new InfirmiereServiceId(infirmiere.getId_infirmiere(), service.getId()));
                            is.setDureePersonalisee(-5);
                            session.save(is);
                        }
                    }
                }
                session.flush();
                session.clear();
            }

            session.getTransaction().commit();
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Client> clientsBD = session.createQuery("FROM Client", Client.class).getResultList();
            List<Infirmiere> infirmieresBD = session.createQuery("FROM Infirmiere", Infirmiere.class).getResultList();
            List<Service> services = session.createQuery("FROM Service", Service.class).getResultList();

            Random random = new Random();
            int count = 0;

            for (int i = 0; i < 10000; i++) {
                Client client = clientsBD.get(random.nextInt(clientsBD.size()));
                Infirmiere infirmiere = infirmieresBD.get(random.nextInt(infirmieresBD.size()));
                Service service = services.get(random.nextInt(services.size()));

                // Générer une date aléatoire entre 2025-01-01 et 2026-12-31
                LocalDate startDate = LocalDate.of(2025, 1, 1);
                LocalDate endDate = LocalDate.of(2026, 12, 31);
                long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
                LocalDate randomDate = startDate.plusDays(random.nextInt((int) daysBetween + 1));

                // Heure de début aléatoire entre 08:00 et 16:30
                int hour = 8 + random.nextInt(9); // 8 à 16 inclus
                int minute = random.nextBoolean() ? 0 : 30; // 00 ou 30
                LocalTime heureDebut = LocalTime.of(hour, minute);

                // Fusionner la date et l'heure
                LocalDateTime dateTimeDebut = LocalDateTime.of(randomDate, heureDebut);
                LocalDateTime dateTimeFin = dateTimeDebut.plusMinutes(service.getDuree());

                RendezVous rdv = new RendezVous();
                rdv.setClient(client);
                rdv.setInfirmiere(infirmiere);
                rdv.setService(service);
                rdv.setDate(randomDate);
                rdv.setHeureDebut(Timestamp.valueOf(dateTimeDebut));
                rdv.setHeureFin(Timestamp.valueOf(dateTimeFin));

                session.save(rdv);

                if (++count % 100 == 0) {
                    session.flush();
                    session.clear();
                }
            }

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

    public static int saisirOption(String message) {
        Scanner sc = new Scanner(System.in);
        System.out.println(message);
        return sc.nextInt();
    }

    public static List<String> lireFichier(String chemin) {
        List<String> mots = new ArrayList<>();

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(chemin)) {
            if (input == null) {
                System.err.println("Fichier introuvable : " + chemin);
                return mots;
            }

            List<String> lignes = new BufferedReader(new InputStreamReader(input))
                    .lines().toList();

            for (String ligne : lignes) {
                String[] motsLigne = ligne.split("\\W+");
                for (String mot : motsLigne) {
                    if (!mot.isEmpty()) {
                        mots.add(mot);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture : " + e.getMessage());
        }

        return mots;
    }
    public static Disponibilite genererDisponibiliteAleatoire() {
        Random random = new Random();

        // Date aléatoire entre 2025-01-01 et 2026-12-31
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 31);
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        LocalDate randomDate = startDate.plusDays(random.nextInt((int) days + 1));

        // Heure de début aléatoire entre 08:00 et 17:00
        int startHour = 8 + random.nextInt(10); // 8h à 17h (inclut)
        int startMinute = random.nextBoolean() ? 0 : 30; // 00 ou 30

        LocalTime heureDebut = LocalTime.of(startHour, startMinute);

        // Durée aléatoire entre 30 min et 2h
        int dureeMinutes = (random.nextInt(4) + 1) * 30;
        LocalTime heureFin = heureDebut.plusMinutes(dureeMinutes);
        if (heureFin.isAfter(LocalTime.of(18, 0))) {
            heureFin = LocalTime.of(18, 0); // ne pas dépasser 18h
        }

        // Convertir en java.util.Date
        LocalDateTime dateTimeDebut = LocalDateTime.of(randomDate, heureDebut);
        LocalDateTime dateTimeFin = LocalDateTime.of(randomDate, heureFin);

        Disponibilite disponibilite = new Disponibilite();
        disponibilite.setDateDisponibilite(java.sql.Date.valueOf(randomDate));
        disponibilite.setHeureDebut( Timestamp.valueOf(dateTimeDebut));
        disponibilite.setHeureFin( Timestamp.valueOf(dateTimeFin));
        return disponibilite;
    }
    public static String hashMotDePasse(String motDePasse) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(motDePasse.getBytes());

            // Convertir les bytes en hexadécimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur de hachage SHA-256", e);
        }
    }
}

