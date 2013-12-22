package com.packtpub.hashinghowto.main;

import com.packtpub.hashinghowto.md5.HashingMD5;
import com.packtpub.hashinghowto.salt.Salting;
import com.packtpub.hashinghowto.sha.HashingSHA;
import com.packtpub.hashinghowto.sha.stronghash.PBKDF2;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Fernando G. Mayoral for http://www.packtpub.com
 */
public class Main {

    /**
     * This is just a general test class.
     * @param args
     */
    public static void main(String[] args) {
        try {
            String value = "www.packtpub.com";
            System.out.println("Value to hash: " + value);
            md5Test(value);
            sha1Test(value);
            sha256Test(value);
            sha384Test(value);
            sha512Test(value);
            saltSha512Test(value);
            PBKDF2Test(value);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void md5Test(String value) throws NoSuchAlgorithmException {
        String hashOne = HashingMD5.createMD5HashFromString(value);
        System.out.println("MD5 Hash:     " + hashOne);
    }

    private static void sha1Test(String value) throws NoSuchAlgorithmException {
        String hashOne = HashingSHA.createSHA1HashFromString(value);
        System.out.println("SHA-1 Hash:   " + hashOne);
    }

    private static void sha256Test(String value) throws NoSuchAlgorithmException {
        String hashOne = HashingSHA.createSHA256HashFromString(value);
        System.out.println("SHA-256 Hash: " + hashOne);
    }

    private static void sha384Test(String value) throws NoSuchAlgorithmException {
        String hashOne = HashingSHA.createSHA384HashFromString(value);
        System.out.println("SHA-384 Hash: " + hashOne);
    }

    private static void sha512Test(String value) throws NoSuchAlgorithmException {
        String hashOne = HashingSHA.createSHA512HashFromString(value);
        System.out.println("SHA-512 Hash: " + hashOne);
    }

    private static void saltSha512Test(String value) throws NoSuchAlgorithmException {
        String hashOne = Salting.getFullSaltedHashSHA512(value);
        System.out.println("Salted SHA-512 Hash: " + hashOne);
    }

    private static void PBKDF2Test(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String hashOne = PBKDF2.createHash(password);
        System.out.println("PBKDF2WithHmacSHA1 Hash: " + hashOne);
    }
}
