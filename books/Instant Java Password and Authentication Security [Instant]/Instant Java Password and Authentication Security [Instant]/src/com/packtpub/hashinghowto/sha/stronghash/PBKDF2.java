package com.packtpub.hashinghowto.sha.stronghash;

import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author Fernando G. Mayoral for http://www.packtpub.com
 */
public class PBKDF2 {
    //PBKDF2 Algorithm
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    //Indexes used to organize parameters
    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;
    //Algorithm parameters
    //Pseudo random number generator algorithm
    public static final String PRNG_ALGORITHM = "SHA1PRNG";
    public static final int SALT_BYTES = 24;// The salt byte length 
    public static final int HASH_BYTES = 64;// The hash byte length 
    public static final int PBKDF2_ITERATIONS = 1000; //The amount of iterations to generate the hash

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password the password to hash as a string
     * @return a salted PBKDF2 hash of the password
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static String createHash(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(password.toCharArray());
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password the password to hash as a char array
     * @return a salted PBKDF2 hash of the password
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static String createHash(char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Generate a random salt
        byte[] salt = getSalt();

        // Hash the password
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTES);
        // format iterations:salt:hash
        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
    }

    /**
     * Gets a secure randomly generated salt
     *
     * @return byte[]
     * @throws NoSuchAlgorithmException
     */
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

    /**
     * Validates a password using a hash.
     *
     * @param password the password to check as a string
     * @param goodHash the hash of the valid password as a string
     * @return true if the password is correct, false if not
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static boolean isValidPassword(String password, String goodHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return isValidPassword(password.toCharArray(), goodHash);
    }

    /**
     * Validates a password using a hash.
     *
     * @param password the password to check as a char array
     * @param storedAuthentication the hash of the valid password, original salt, and iteration, as a string
     * @return true if the password is correct, false if not
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static boolean isValidPassword(char[] password, String storedAuthentication)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Decode the hash into its parameters
        String[] params = storedAuthentication.split(":");
        int iterations = Integer.parseInt(params[ITERATION_INDEX]);
        byte[] salt = fromHex(params[SALT_INDEX]);
        byte[] hash = fromHex(params[PBKDF2_INDEX]);
        // Compute the hash of the provided password, using the same salt,
        // iteration count, and hash length
        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(hash, testHash);
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method is used so that password hashes cannot be extracted from an on-line system using
     * a timing attack and then attacked off-line.
     *
     * @param a the first byte array
     * @param b the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        //Store the variable diff the diference between the length of the byte array "a" and the length of the byte array "b"
        //if they are equal this will get a zero value
        int diff = a.length ^ b.length;
        // Iterates over both arrays and compares each byte
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    /**
     * Computes the PBKDF2 hash of a password.
     *
     * @param password the password to hash.
     * @param salt the salt
     * @param iterations the iteration count (slowness factor)
     * @param bytes the length of the hash to compute in bytes
     * @return the PBDKF2 hash of the password
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param hex the hex string
     * @return the hex string decoded into a byte array
     */
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
}
