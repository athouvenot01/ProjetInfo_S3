/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

/**
 *
 * @author loicrosian
 */
public class Operations {
    public static class Operation {
        private int idtype;
        private int idproduit;
        
        public Operation (int idproduit, int idtype) {
            this.idproduit = idproduit;
            this.idtype = idtype;
            
        }

        public int getIdtype() {
            return idtype;
        }

        public int getIdproduit() {
            return idproduit;
        }

        public void setIdtype(int idtype) {
            this.idtype = idtype;
        }

        public void setIdproduit(int idproduit) {
            this.idproduit = idproduit;
        }
        
    }
}
