/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import com.insa.info_s3.Clients.Client;
import com.insa.info_s3.Commande.commande;
import static com.insa.info_s3.GestionBDD.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Emilien
 */
@Route(value = "Order_View", layout = UI.class)
public class Order_View extends VerticalLayout {
    private Grid<commande> grid = new Grid<>();
    public Order_View () throws SQLException{
    Connection con = GestionBDD.connectSurServeurM3();
            
             ComboBox<Client> clientComboBox = new ComboBox<>("Choisir un client");
             List<Client> clients = GetClients(con);
             clientComboBox.setItems(clients);
             clientComboBox.setItemLabelGenerator(Client::toString);

        // Ajouter un événement de sélection de combobox
       clientComboBox.addValueChangeListener(event -> {
    Client selectedClient = event.getValue();
    Notification.show("Client sélectionné : " + selectedClient.getNom());

    try {
        List<commande> commandes = GetCommande(con, selectedClient.getId());
        grid.removeAllColumns(); // Supprime toutes les colonnes existantes
        grid.addColumn(commande::getId).setHeader("Id");
        grid.addColumn(commande::getMontant).setHeader("Montant");
        grid.setItems(commandes);

        grid.setVisible(true);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
});


        // Ajouter des composants à la mise en page
        add(
                clientComboBox,
                grid
        );
    
        
        }
        
    }


