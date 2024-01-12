/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

/**
 *
 * @author Emilien
 */
public class PosteDeTravail {
    public static class PosteDeTravaille {
        private int idmachine;
        private int idoperateur;
        private int id;
        
        public PosteDeTravaille (int id, int idmachine,int idoperateur) {
            this.id = id;
            this.idmachine = idmachine;
            this.idoperateur=idoperateur;
            
        }

        public int getIdmachine() {
            return idmachine;
        }

        public void setIdmachine(int idmachine) {
            this.idmachine = idmachine;
        }

        public int getIdoperateur() {
            return idoperateur;
        }

        public void setIdoperateur(int idoperateur) {
            this.idoperateur = idoperateur;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
        
    }
}
