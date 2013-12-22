package com.packtpub.hashinghowto.salt;

/**
 * Sample User Model.
 *
 * @author Fernando G. Mayoral for http://www.packtpub.com
 */
public class User {
    //Unique id

    private Long id;
    //The login account
    private String username;
    //The password
    private String password;
    //The salt
    private String salt;

    /**
     * Constructor using username, password and salt.
     */
    public User(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    /**
     * Constructor using all fields.
     */
    public User(Long id, String username, String password, String salt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    /**
     * Empty constructor.
     */
    public User() {
    }

    // Beyond this point setters and getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
