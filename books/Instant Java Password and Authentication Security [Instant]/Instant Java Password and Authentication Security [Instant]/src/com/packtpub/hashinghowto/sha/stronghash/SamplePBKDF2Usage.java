package com.packtpub.hashinghowto.sha.stronghash;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 *
 * @author Fernando G. Mayoral for http://www.packtpub.com
 */
public class SamplePBKDF2Usage {

    public static void test(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String correctPassword = "www.packtpub.com";
        System.out.println("Original password: " + correctPassword);
        String wrongPassword = "wrong_password";
        String hash = PBKDF2.createHash(correctPassword);
        System.out.println("Hash: " + hash);

        System.out.println("Testing password:" + wrongPassword + " - It should fail");
        if (PBKDF2.isValidPassword(wrongPassword, hash)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed!");
        }

        System.out.println("Testing password:" + correctPassword + " - It should be successful");
        if (PBKDF2.isValidPassword(correctPassword, hash)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed!");
        }
    }
}
