package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashSHA256 {

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());

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

    public static void main(String[] args) {
        String password = "motDePasse123";
        String hashed = hashPassword(password);
        System.out.println("Mot de passe haché : " + hashed);
    }
}
