/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

/**
 *
 * @author loicrosian
 */
public class Operateurs {
    
    public static class Operateur {
        private String prenom;
        private String nom;
        private int id;
        private String etatOperateur;
        
        public Operateur(int id, String nom, String prenom, String etatOperateur) {
            this.prenom = prenom;
            this.nom = nom;
            this.id = id;
            this.etatOperateur = etatOperateur;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getNom() {
            return nom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getEtatOperateur() {
            return etatOperateur;
        }

        public void setEtatOperateur(String etatOperateur) {
            this.etatOperateur = etatOperateur;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return prenom + " " + nom ;
        }
      
       
    }
}
