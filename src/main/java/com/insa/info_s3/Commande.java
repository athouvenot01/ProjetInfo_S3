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
    
    public commande (int id , int idClient){
    this.id=id;
    this.idClient=idClient;
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
    
    
    }
 }
