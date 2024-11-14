package com.java;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class UserKey {
    private int userId;
    private String privateKey;
    private String publicKey;

    public UserKey(int userId) {
        this.userId = userId;
        generateKeys(); // Génère et stocke les clés à l'initialisation
    }

    public int getUserId() {
        return userId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void generateKeys() {
        try {
            // Génération de la paire de clés RSA
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048); // Longueur des clés (2048 bits)
            KeyPair pair = keyGen.generateKeyPair();

            // Encodage des clés en Base64 pour stockage
            this.privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
            this.publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encryptData(String data) {
        try {
            // Décode la clé publique et initialise le chiffreur
            PublicKey pubKey = decodePublicKey(this.publicKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            // Chiffrement des données
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Retourne null en cas d'échec
        }
    }

    public String decryptData(String encryptedData) {
        try {
            // Décode la clé privée et initialise le chiffreur pour le déchiffrement
            PrivateKey privKey = decodePrivateKey(this.privateKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privKey);

            // Déchiffrement des données
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Retourne null en cas d'échec
        }
    }

    // Méthodes pour décoder les clés stockées en Base64
    private PublicKey decodePublicKey(String publicKeyStr) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
    }

    private PrivateKey decodePrivateKey(String privateKeyStr) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
    }
}
