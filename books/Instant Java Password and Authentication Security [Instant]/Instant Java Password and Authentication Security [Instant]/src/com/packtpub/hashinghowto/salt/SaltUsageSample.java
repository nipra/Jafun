package com.packtpub.hashinghowto.salt;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample salt usage.
 *
 * @author Fernando G. Mayoral for http://www.packtpub.com
 */
public class SaltUsageSample {

    /**
     * With this method you can test how salting works
     */
    public static void test() {
        try {
            String username = "packtpub";
            String correctPassword = "packtpub_password";
            String wrongPassword = "samplePassword";
            System.out.println("Create user:" + username);
            createUser(username, correctPassword);
            System.out.println("***Login fail test***");
            isValidUser(username, wrongPassword);
            System.out.println("***Successfull login test***");
            isValidUser(username, correctPassword);
        } catch (IOException ex) {
            Logger.getLogger(SaltUsageSample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createUser(String login, String password) {
        // Create a byte array to save the salt
        byte[] byteSalt = null;
        try {
            //Create a new random salt
            byteSalt = getSalt();
        } catch (NoSuchAlgorithmException ex) {
            // This could happen if we choose a not supported algorithm
            Logger.getLogger(SaltUsageSample.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Digest the password with with salt
        byte[] byteDigestPsw = Salting.getSaltedHashSHA512(password, byteSalt);
        //Get the hashed password in hex
        String strDigestPsw = toHex(byteDigestPsw);
        //Get the salt in hex
        String strSalt = toHex(byteSalt);
        //Create the new user to save the data.
        User user = new User();
        //Now we need to save the following data into a new user:
        //"login" (the login user)
        user.setUsername(login);
        //"strDigestPsw" (hashed password) 
        user.setPassword(strDigestPsw);
        //"strSalt" (in order to generate this salted hash again to compare, we will need the original salt)
        user.setSalt(strSalt);
        //Save the user into the database!
        saveUser(user);
    }

    public static boolean isValidUser(String login, String password) throws IOException {
        //Retrieve the user from the database
        User user = retrieveUser(login);
        //Get the salt used with the password when the user was created
        String strOriginalSalt = user.getSalt();
        //Get the bytes from the salt, remember it's in hex format
        byte[] byteSalt = fromHex(strOriginalSalt);
        //Digest the login password with the original salt
        byte[] loginPassword = Salting.getSaltedHashSHA512(password, byteSalt);
        //Get the stored password for comparing
        byte[] storedPassword = fromHex(user.getPassword());
        //Compare the incoming password with the stored password.
        boolean result = Salting.slowEquals(loginPassword, storedPassword);
        if (result) {
            System.out.println("Successfull login!");
        } else {
            System.out.println("Login failed!");
        }
        return result;
    }

    private static void saveUser(User user) {
        //Let's assume that the user was saved in the database and it now haves an id
        user.setId(1L);
        System.out.println("User created - Username: " + user.getUsername() + "\n | Password: " + user.getPassword() + "\n | Salt: " + user.getSalt());
        //We will save it in a local hashmap just for the purpose of this example
        databaseSample.put(user.getUsername(), user);
    }

    private static User retrieveUser(String login) {
        return databaseSample.get(login);
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param hex the hex string
     * @return the hex string decoded into a byte array
     */
    public static byte[] fromHex(String hex) {
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
    public static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        //Always use a secure random generator
        SecureRandom secureRandom = SecureRandom.getInstance(Salting.PRNG_ALGORITHM);
        //Create array for the salt (16 bytes length)
        byte[] salt = new byte[Salting.SALT_LENGTH];
        //Get a random salt
        secureRandom.nextBytes(salt);
        //Return the generated salt
        return salt;
    }

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
    private static Map<String, User> databaseSample = new HashMap<String, User>();
}
