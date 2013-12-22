package com.packtpub.hashinghowto.sha;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides Secure Hashing Algorithms features.
 *
 * @author Fernando G. Mayoral for http://www.packtpub.com
 */
public class HashingSHA {

    //Note: As you can notice, when using message digest, the code is always the same, we only change the algorithm's name
    //so it's a good idea to just create a generic method and pass the algorithm name as a parameter.
    //SHA1 Algorithm name
    private static final String SHA1 = "SHA1";
    //SHA256 Algorithm name
    private static final String SHA256 = "SHA-256";
    //SHA384 Algorithm name
    private static final String SHA384 = "SHA-384";
    //SHA512 Algorithm name
    private static final String SHA512 = "SHA-512";

    /**
     * Creates a SHA-1 Hash from a String.
     *
     * @param data: The String we want to hash
     * @return hash String value in hex format
     * @throws java.security.NoSuchAlgorithmException
     */
    public static String createSHA1HashFromString(String data) throws NoSuchAlgorithmException {
        return createSHAXHashFromString(data, SHA1);
    }

    /**
     * Creates a SHA-256 Hash from a String.
     *
     * @param data: The String we want to hash
     * @return hash String value in hex format
     * @throws java.security.NoSuchAlgorithmException
     */
    public static String createSHA256HashFromString(String data) throws NoSuchAlgorithmException {
        return createSHAXHashFromString(data, SHA256);
    }

    /**
     * Creates a SHA-384 Hash from a String.
     *
     * @param data: The String we want to hash
     * @return hash String value in hex format
     * @throws java.security.NoSuchAlgorithmException
     */
    public static String createSHA384HashFromString(String data) throws NoSuchAlgorithmException {
        return createSHAXHashFromString(data, SHA384);
    }

    /**
     * Creates a SHA-512 Hash from a String.
     *
     * @param data: The String we want to hash
     * @return hash String value in hex format
     * @throws java.security.NoSuchAlgorithmException
     */
    public static String createSHA512HashFromString(String data) throws NoSuchAlgorithmException {

        return createSHAXHashFromString(data, SHA512);
    }

    /**
     * Creates a SHA-X Hash from a String. Where "X" can be 1/256/384/512
     *
     * @param data: The String we want to hash
     * @param algorithm: The algorithm's name to use
     * @return hash String value in hex format
     * @throws java.security.NoSuchAlgorithmException
     */
    public static String createSHAXHashFromString(String data, String algorithm) throws NoSuchAlgorithmException {
        //Get the 'data' String as a byte array and call 'createSHAXHashFromBytes'
        byte[] hashBytes = HashingSHA.createSHAXHashFromBytes(data.getBytes(), algorithm);
        //convert the byte to hex format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Creates a SHA-X Hash from a byte array. Where "X" can be 1/256/384/512
     *
     * @param dataBytes The dataBytes in bytes we want to hash
     * @param algorithm: The algorithm's name to use
     * @return hash value in bytes
     * @throws java.security.NoSuchAlgorithmException
     */
    public static byte[] createSHAXHashFromBytes(byte[] dataBytes, String algorithm) throws NoSuchAlgorithmException {

        try {
            //Create a new instance of MessageDigest for the SHA-1 Algorithm
            MessageDigest md = MessageDigest.getInstance(algorithm);
            //Use the MessageDigest to digest the bytes of the value you want to hash
            //This will return the hash value in bytes
            return md.digest(dataBytes);
        } catch (NoSuchAlgorithmException ex) {
            //It's unlikely that SHA-1 Algorithm is not present, but it could happen, and it must be catched and logged.
            Logger.getLogger(HashingSHA.class.getName()).log(Level.SEVERE, "\"" + algorithm + "\" is not a valid algorithm name!", ex);
            throw ex;
        }
    }
}
