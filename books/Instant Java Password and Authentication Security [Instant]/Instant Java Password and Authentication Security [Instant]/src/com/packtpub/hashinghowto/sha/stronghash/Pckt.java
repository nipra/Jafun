package com.packtpub.hashinghowto.sha.stronghash;

import static com.packtpub.hashinghowto.salt.SaltUsageSample.getSalt;
import static com.packtpub.hashinghowto.sha.stronghash.PBKDF2.PRNG_ALGORITHM;
import static com.packtpub.hashinghowto.sha.stronghash.PBKDF2.SALT_BYTES;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Fernando G. Mayoral for http://www.packtpub.com
 */
public class Pckt {

    public static void test(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String password = "www.packtpub.com";
        String storedPassword = "1000:ffd0236a2fe202f2374786bce5aab61fe99b9208015b8899:d23a8dc3834d53cbf213cc0f329fe61e3188954809b6c57d6177890f52db56d0c71dea1b73bba52940f82a93676b9572069e8497459d3f48277eeb85abc0f6ac";
        System.out.println("Password: " + password);
        System.out.println("Stored authentication: " + storedPassword);
        if (isValidUser(password, storedPassword)) {
            System.out.println("Is a valid user!");
        } else {
            System.out.println("Is not a valid user!");
        }
    }

    public static String generateStrongHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //We want to iterate over the hash lots of times, to make it harder to crack...
        int iterations = 1000;
        //1.1 Get the password as a char array
        char[] passwordAsCharArray = password.toCharArray();
        //1.2 Create a byte array to save the salt
        byte[] salt = getSalt();
        //1.3 Create a password-based encryption key spec
        //it needs the password to hash as a char array, the salt as a byte array, the amount of iterations,
        //and the amount of bits (64 bytes * 8 bits = 512 bits hash)
        PBEKeySpec spec = new PBEKeySpec(passwordAsCharArray, salt, iterations, 64 * 8);
        //1.4 Create a secret key factory for the "PBKDF2WithHmacSHA1" algorithm
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        //1.5 Compute the password to generate a hash
        byte[] hash = skf.generateSecret(spec).getEncoded();
        //1.6 Return the generate hash formatted as "iterations:salt:hash"
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    public static Boolean isValidUser(String password, String storedAuthentication) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //1.1 Get the password as a char array
        char[] passwordAsCharArray = password.toCharArray();
        //1.2 We split the original authentication data to regenerate the hash using the same parameters
        //Remember it's formatted as "iterations:salt:hash" in hex string format
        String[] params = storedAuthentication.split(":");
        //Get the iterations
        int iterations = Integer.parseInt(params[0]);
        //Get the salt
        byte[] salt = fromHex(params[1]);
        //Get the hash
        byte[] hash = fromHex(params[2]);
        //1.3 Create a password-based encryption key spec
        //it needs the password to hash as a char array, the salt as a byte array, the amount of iterations,
        //and the amount of bits (which is the length of the hash)
        PBEKeySpec spec = new PBEKeySpec(passwordAsCharArray, salt, iterations, hash.length);
        //1.4 Create a secret key factory for the "PBKDF2WithHmacSHA1" algorithm
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        //1.5 Compute the hash of the provided password, using the same salt, iteration count, and hash length
        byte[] testHash = skf.generateSecret(spec).getEncoded();
        //1.6 Compare the hashes. The password is correct if both hashes match.
        return slowEquals(hash, testHash);
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        //Always use a secure random generator
        SecureRandom secureRandom = SecureRandom.getInstance(PRNG_ALGORITHM);
        //Create array for the salt (24 bytes length)
        byte[] salt = new byte[SALT_BYTES];
        //Get a random salt
        secureRandom.nextBytes(salt);
        //Return the generated salt
        return salt;
    }

    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param array the byte array to convert
     * @return a length*2 character string encoding the byte array
     */
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        //Store the variable diff the diference between the length of the byte array "a" and the length of the byte array "b"
        //if they are equal this will get a zero value
        int diff = a.length ^ b.length;
        // Iterates over both arrays and compares each byte
        for (int i = 0; i < a.length && i < b.length; i++) {
            // Inclusive assignment(if diff == 0, it assigns to diff the result of comparing two bytes, if they are equal it assigns a 0, else another number)
            diff |= a[i] ^ b[i];
        }
        //Returns the result of comparing diff to 0 (if there are no differences it will return true)
        return diff == 0;
    }
}
