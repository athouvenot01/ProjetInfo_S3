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
        private String refMachine;
        private String NomOpe;
        private int id;
        
        public PosteDeTravaille (int id, String refMachine,String NomOpe) {
            this.id = id;
            this.refMachine = refMachine;
            this.NomOpe=NomOpe;
            
        }

        public String getRefMachine() {
            return refMachine;
        }

        public void setRefMachine(String refMachine) {
            this.refMachine = refMachine;
        }

        public String getNomOpe() {
            return NomOpe;
        }

        public void setNomOpe(String NomOpe) {
            this.NomOpe = NomOpe;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        
        
    }
}
