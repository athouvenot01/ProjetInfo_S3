/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

/**
 *
 * @author Emilien
 */
public class Produit {
    public static class Produits {
        private int id;
        private String ref;
        private String des;
        private int materiaux;
        

        public Produits (int id, String ref, String des, int materiaux) {
            this.id = id;
            this.ref = ref;
            this.des = des;
            this.materiaux = materiaux;
           
        }
        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
         public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public int getMateriaux() {
            return materiaux;
        }

        public void setMateriaux(int etatmachine) {
            this.materiaux = etatmachine;
        }
        
    }
}
