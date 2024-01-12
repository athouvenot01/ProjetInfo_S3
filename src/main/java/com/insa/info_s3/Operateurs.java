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
        private int idmachine;
        private int idetatoperateur;
        
        public Operateur(int idmachine, String nom, String prenom, int idetatoperateur) {
            this.prenom = prenom;
            this.nom = nom;
            this.idmachine = idmachine;
            this.idetatoperateur = idetatoperateur;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getNom() {
            return nom;
        }

        public int getIdmachine() {
            return idmachine;
        }

        public int getIdetatoperateur() {
            return idetatoperateur;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public void setIdmachine(int idmachine) {
            this.idmachine = idmachine;
        }

        public void setIdetatoperateur(int idetatoperateur) {
            this.idetatoperateur = idetatoperateur;
        }
        
    }
}
