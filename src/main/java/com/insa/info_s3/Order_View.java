/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import com.insa.info_s3.Achats.Achat;
import com.insa.info_s3.Clients.Client;
import com.insa.info_s3.Commande.commande;
import static com.insa.info_s3.GestionBDD.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Emilien
 */

@Route(value = "Order_View", layout = UI.class)
public class Order_View extends VerticalLayout {
    
    private Grid<commande> grid = new Grid<>();
    private Grid<Achat> detailsGrid = new Grid<>();
    private Button ajoutCommandeButton = new Button("Ajout d'une commande", VaadinIcon.PLUS.create());
    private Button supprimerCommandeButton = new Button("Supprimer commande", VaadinIcon.TRASH.create());
    private Button boutonIntegrer = new Button("Plus de détail");

    public Order_View() throws SQLException {
        
        Connection con = GestionBDD.connectSurServeurM3();

            ComboBox<Client> clientComboBox = new ComboBox<>("Choisir un client");
            List<Client> clients = GetClients(con);
            clientComboBox.setItems(clients);
            clientComboBox.setItemLabelGenerator(Client::toString);
            supprimerCommandeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            ajoutCommandeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            // Ajouter un événement de sélection de combobox
            clientComboBox.addValueChangeListener(event -> {
                Client selectedClient = event.getValue();
                Notification.show("Client sélectionné : " + selectedClient.getNom());

                try {
                    List<commande> commandes = GetCommandeById(con, selectedClient.getId());
                    grid.removeAllColumns(); // Supprime toutes les colonnes existantes
                    grid.addColumn(commande::getId).setHeader("Id");
                    grid.addColumn(commande::getMontant).setHeader("Prix de vente");
                    grid.addColumn(commande::getMontantTVA).setHeader("Prix de vente");


                    // Ajouter la colonne de boutons intégrés
                    grid.addComponentColumn(commande -> {
                        Button boutonIntegrer = new Button("Détail");
                        boutonIntegrer.addClickListener(e -> {
                            // Logique pour afficher les détails de la commande dans un Dialog
                            showOrderDetailsDialog(commande,con);
                        });
                        return boutonIntegrer;
                    }).setHeader("Détail");

                    grid.setItems(commandes);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            
            supprimerCommandeButton.addClickListener(click -> {
                Set<commande> selectedItems = grid.getSelectedItems();
                
                if(selectedItems.isEmpty()) {
                    Notification.show("Aucune ligne selectionnée");
                
                } else {
                    commande selectedBean = selectedItems.iterator().next();
                    int value = selectedBean.getId();
                    try {GestionBDD.deleteMachine(con, value);} catch (SQLException ex){ex.printStackTrace();}
                    try {UpdateCommandes(con);} catch (SQLException ex){ex.printStackTrace();}
                    
                    Notification.show("Commande "+ selectedBean.getId()+" supprimée avec succès" , 5000, Notification.Position.TOP_CENTER);
                }
            });
            
            // Envelopper les boutons dans un layout horizontal
            HorizontalLayout buttonsLayout = new HorizontalLayout(ajoutCommandeButton, supprimerCommandeButton);
            buttonsLayout.setSpacing(true); // Ajouter de l'espace entre les boutons

            // Ajouter des composants à la mise en page verticale
            add(
                    clientComboBox,
                    grid,
                    buttonsLayout // Ajouter le layout horizontal ici
            );
    }

    
    private void showOrderDetailsDialog(commande selectedCommande,Connection con) {
        // Créer un Dialog avec une Grid pour afficher les détails de la commande
        Dialog detailsDialog = new Dialog();
        detailsDialog.setWidth("800px"); // Définir la largeur du Dialog, ajustez selon vos besoins
        detailsDialog.setHeight("500px"); // Définir la hauteur du Dialog, ajustez selon vos besoins
        detailsDialog.setCloseOnEsc(true);
        detailsDialog.setCloseOnOutsideClick(true);

        // Créer une nouvelle Grid pour afficher les détails
        try {
                List<Achat> achats = GetAchat(con, selectedCommande.getId());
                detailsGrid.removeAllColumns(); // Supprime toutes les colonnes existantes
                detailsGrid.addColumn(Achat::getId).setHeader("Id");
                detailsGrid.addColumn(Achat::getProduit).setHeader("Produit");
                detailsGrid.addColumn(Achat::getQuantité).setHeader("Quantité");
                detailsGrid.addColumn(Achat::getMontant).setHeader("Montant");

                detailsGrid.setItems(achats);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        // Ajouter d'autres colonnes ou personnaliser selon vos besoins

        // Ajouter la Grid à l'intérieur du Dialog
        detailsDialog.add(new H3("Détails de la Commande"), detailsGrid);

        // Ajouter les données de la commande sélectionnée à la Grid

        // Ouvrir le Dialog
        detailsDialog.open();
    }
    
    
    private void UpdateCommandes (Connection con) throws SQLException{
        List<commande> Commande = GestionBDD.GetCommande(con);
        grid.setItems(Commande);
    }
    
}

