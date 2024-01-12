/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

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
    private Grid<Commande> grid = new Grid<>();
    public Order_View () throws SQLException{
        try (Connection con = GestionBDD.connectSurServeurM3()){
             ComboBox<String> clientComboBox = new ComboBox<>("Choisir un client");
             List<String> clients = Arrays.asList("Client 1", "Client 2", "Client 3");
             clientComboBox.setItems(clients);

        // Ajouter un événement de sélection de combobox
       clientComboBox.addValueChangeListener(event -> {
            String selectedClient = event.getValue();
            Notification.show("Client sélectionné : " + selectedClient);
        });

        // Ajouter des composants à la mise en page
        add(
                clientComboBox
        );
    }
        
        }
        
    }


