package com.example.etumoov;

public class User {
    public String nom, prenom, email, password;

    public User(String nom, String prenom, String email, String ps){
        this.nom = nom;
        this.email = email;
        this.password = ps;
        this.prenom = prenom;
    }

    public String getEmail(){
        return this.email;
    }
    public String getNom(){
        return this.nom;
    }
    public String getPassword(){
        return this.password;
    }
    public String getPrenom(){
        return this.prenom;
    }
}
