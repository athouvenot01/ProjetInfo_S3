/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

/**
 *
 * @author Emilien
 */
public class Commande {
    public static class commande {
    private int id ;
    private int idClient;
    private double montant;
    private double montantTVA;
    public commande (int id , int idClient,double montant,double montantTVA){
    this.id=id;
    this.idClient=idClient;
    this.montant=montant;
    this.montantTVA=montantTVA;
    }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIdClient() {
            return idClient;
        }

        public void setIdClient(int idClient) {
            this.idClient = idClient;
        }

        public double getMontant() {
            return montant;
        }

        public void setMontant(double montant) {
            this.montant = montant;
        }

        public double getMontantTVA() {
            return montantTVA;
        }

        public void setMontantTVA(double montantTVA) {
            this.montantTVA = montantTVA;
        }
        
    
    }
 }
