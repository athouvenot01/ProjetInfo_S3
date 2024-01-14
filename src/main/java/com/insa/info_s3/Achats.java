/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

/**
 *
 * @author Emilien
 */
public class Achats {
    public static class Achat {
        private String Produit;
        private int id;
        private double montant;
        private int quantité;

        public Achat(int id, String Produit,int quantité,double montant) {
            this.id = id;
            this.Produit = Produit;
            this.montant= montant;
            this.quantité=quantité;   
        }

        public String getProduit() {
            return Produit;
        }

        public void setProduit(String Produit) {
            this.Produit = Produit;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getMontant() {
            return montant;
        }

        public void setMontant(double montant) {
            this.montant = montant;
        }

        public int getQuantité() {
            return quantité;
        }

        public void setQuantité(int quantité) {
            this.quantité = quantité;
        }
        
    }
}
