package com.packtpub.hashinghowto.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides Message Digest 5 features.
 *
 * @author Fernando G. Mayoral for http://www.packtpub.com
 */
public class HashingMD5 {

    /**
     * Creates a MD5 Hash from a String.
     *
     * @param data: The String we want to hash
     * @return MD5 hash String value in hex format
     * @throws java.security.NoSuchAlgorithmException
     */
    public static String createMD5HashFromString(String data) throws NoSuchAlgorithmException {
        //Get bytes from string, and invokes 'createMD5HashFromBytes'
        byte[] hashBytes = HashingMD5.createMD5HashFromBytes(data.getBytes());
        //convert the byte to hex format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Creates a MD5 Hash from a byte array.
     *
     * @param dataBytes: The value in bytes we want to hash
     * @return MD5 hash value in bytes
     * @throws java.security.NoSuchAlgorithmException
     */
    public static byte[] createMD5HashFromBytes(byte[] dataBytes) throws NoSuchAlgorithmException {
        //This is the algorithm name
        String algorithm = "MD5";
        try {
            //Create a new instance of MessageDigest for the MD5 Algorithm
            MessageDigest md = MessageDigest.getInstance(algorithm);
            //Use the MessageDigest to digest the bytes of the value you want to hash
            //This will return the hash value in bytes
            return md.digest(dataBytes);
        } catch (NoSuchAlgorithmException ex) {
            //It's unlikely that MD5 Algorithm is not present, but it could happen if the algorithm name is misspelled, 
            //and it must be catched and logged.
            Logger.getLogger(HashingMD5.class.getName()).log(Level.SEVERE, "\"{0}\" is not a valid algorithm name.", algorithm);
            throw ex;
        }
    }
}
