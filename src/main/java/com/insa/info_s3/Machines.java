/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

/**
 *
 * @author Emilien
 */
public class Machines {
    public static class Machine {
        private String ref;
        private String des;
        private int puissance;
        private int id;
        private int etatmachine;

        public Machine(int id, String ref, String des, int puissance , int etatmachine) {
            this.id = id;
            this.ref = ref;
            this.des = des;
            this.puissance = puissance;
            this.etatmachine =etatmachine;
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

        

        public int getPuissance() {
            return puissance;
        }

        public void setPuissance(int puissance) {
            this.puissance = puissance;
        }

       

        public int getEtatmachine() {
            return etatmachine;
        }

        public void setEtatmachine(int etatmachine) {
            this.etatmachine = etatmachine;
        }
        
    }
}
