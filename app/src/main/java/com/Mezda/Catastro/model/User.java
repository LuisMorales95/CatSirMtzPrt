package com.Mezda.Catastro.model;

/**
 * @author Audacity IT Solutions Ltd.
 * @class User
 * @brief data structure class for storing user information
 */

public class User {

    private String id;
    private String name;
    private String email;
    private String genderId;
    private String phonenumber;
    private String username;
    private String password;
    
    public User() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getGenderId() {
        return genderId;
    }
    
    public void setGenderId(String genderId) {
        this.genderId = genderId;
    }
    
    public String getPhonenumber() {
        return phonenumber;
    }
    
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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
}
