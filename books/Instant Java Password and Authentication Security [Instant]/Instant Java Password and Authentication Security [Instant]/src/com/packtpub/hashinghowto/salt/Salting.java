package com.packtpub.hashinghowto.salt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample functions and methods for salting.
 * @author Fernando G. Mayoral for http://www.packtpub.com
 */
public class Salting {

    //Pseudo random number generator algorithm
    public static final String PRNG_ALGORITHM = "SHA1PRNG";
    //Salt length (min:16)
    public static final int SALT_LENGTH = 16;
    //Used to organize the hash parameters
    public static final int SALT_INDEX = 0;
    public static final int HASH_INDEX = 1;

    /**
     * Generates a hash using SHA-512 with the given salt as a formatted string "salt:hash".
     *
     * @param password the password to salt and hash as a string
     * @return string the hash as a string formatted as "salt:hash"
     * @throws NoSuchAlgorithmException
     */
    public static String getFullSaltedHashSHA512(String password) throws NoSuchAlgorithmException {
        //Get a random secure salt
        byte[] salt = getSalt();
        //Computes the password with the salt to generate the hash
        byte[] byteDigestPsw = getSaltedHashSHA512(password, salt);
        //Get the hashed password in hex
        String strDigestPsw = toHex(byteDigestPsw);
        //Get the salt in hex
        String strSalt = toHex(salt);
        //Return the salt and the hash formatted as "salt:hash"
        return strSalt + ":" + strDigestPsw;
    }

    /**
     * Generates a hash using SHA-512 with the given salt.
     *
     * @param password the password to salt and hash as a string
     * @param salt the randomly generated secure hash as a byte array
     * @return byte[] the hash as a byte array
     */
    public static byte[] getSaltedHashSHA512(String password, byte[] salt) {
        try {
            //1.2 Get a SHA-512 MessageDigest
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            //1.3 Add the salt to the MessageDigest
            md.update(salt);
            //1.4 Digest the password, notice that we get the password as a byte array
            byte byteData[] = md.digest(password.getBytes());
            //Reset the MessageDigest, just in case
            md.reset();
            //1.5 Return the hashed password
            return byteData;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger("SHA-512").log(Level.SEVERE, "SHA-512 is not a valid algorithm name", ex);
            return null;
        }
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
        //Create array for the salt (16 bytes length)
        byte[] salt = new byte[SALT_LENGTH];
        //Get a random salt
        secureRandom.nextBytes(salt);
        //Return the generated salt
        return salt;
    }

    /**
     * Compares two byte arrays in length-constant time to avoid timing attacks.
     *
     * @param psw1 first password
     * @param psw2 second password
     * @return boolean
     */
    public static boolean slowEquals(byte[] psw1, byte[] psw2) {
        //Get the byte arrays length difference
        int diff = psw1.length ^ psw2.length;
        //iterates over both passwords until one of them is fully checked
        for (int i = 0; i < psw1.length && i < psw2.length; i++) {
            //for each byte array position get the difference
            diff |= psw1[i] ^ psw2[i];
        }
        //returns true if difference == 0, false otherwise
        return diff == 0;
    }

    public static byte[] fromHex(String hex) {
        //Create a byte array with half of the hex string length
        byte[] binary = new byte[hex.length() / 2];
        //For 0 to byte array length
        for (int i = 0; i < binary.length; i++) {
            //Parse 2 chars from base 16 to base 2
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        //return the byte array
        return binary;
    }

    public static String toHex(byte[] array) {
        //Create a new BigInteger with the byte array
        BigInteger bi = new BigInteger(1, array);
        //Get the big integer as a string
        String hex = bi.toString(16);
        //Calculate the padding length
        int paddingLength = (array.length * 2) - hex.length();
        //If there is any padding
        if (paddingLength > 0) {
            //Format the padding length and concatenate the hex string
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            //Else just return the hex string
            return hex;
        }
    }
}
