package com.insa.info_s3;

import com.insa.info_s3.Achats.Achat;
import com.insa.info_s3.Clients.Client;
import com.insa.info_s3.Commande.commande;
import static com.insa.info_s3.GestionBDD.*;
import com.insa.info_s3.Produit.Produits;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

@Route(value = "Order_View", layout = UI.class)
public class Order_View extends VerticalLayout {

    private Grid<commande> grid = new Grid<>();
    private Grid<Achat> detailsGrid = new Grid<>();
    private Grid<Produits> ProduitGrid = new Grid<>();
    private Button ajoutCommandeButton = new Button("Ajout d'une commande", VaadinIcon.PLUS.create());
    private Button supprimerCommandeButton = new Button("Supprimer commande", VaadinIcon.TRASH.create());
    private Button boutonIntegrer = new Button("Plus de détail");
    private Button CréationCommande = new Button("Valider la commande");
    private Grid<Achat> AchatGrid = new Grid<>();
    private List<Achat> Achats = new ArrayList<>();
    ComboBox<Client> clientComboBox = new ComboBox<>("Choisir un client");

    public Order_View() throws SQLException {
        Connection con = GestionBDD.connectSurServeurM3();

        H2 titre_View = new H2("Registre des commandes");
        List<Client> clients = GetClients(con);
        clientComboBox.setItems(clients);
        clientComboBox.setItemLabelGenerator(Client::toString);
        supprimerCommandeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        ajoutCommandeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        ajoutCommandeButton.addClickListener(event -> {
            showOrderCreationDialog(con);
        });
         supprimerCommandeButton.addClickListener(event -> {
            Set<commande> selectedItems = grid.getSelectedItems();

            for (commande selectedCommande : selectedItems) {
                int idCommande = selectedCommande.getId();
                try {
                    // Utilisez idCommande ou selectedCommande directement pour supprimer la commande
                    deleteCommande(con, idCommande);
                    List<Achat> achats = GetAchat(con, selectedCommande.getId());
                    for (Achat achat : achats) {
                        deleteAchat(con, achat.getId());
                    }
                    Client ClientSelectionne =clientComboBox.getValue();
                    UpdateCommandes(con, ClientSelectionne.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Gérer les erreurs de suppression de commande
                }
    }
            
             });

        clientComboBox.addValueChangeListener(event -> {
            Client selectedClient = event.getValue();
            Notification.show("Client sélectionné : " + selectedClient.getNom());

            try {
                List<commande> commandes = GetCommande(con, selectedClient.getId());
                grid.removeAllColumns();
                grid.addColumn(commande::getId).setHeader("Id");
                grid.addColumn(commande::getMontant).setHeader("Prix de production");
                grid.addColumn(commande::getMontantTVA).setHeader("Prix de vente");

                grid.addComponentColumn(commande -> {
                    Button boutonIntegrer = new Button("Détail");
                    boutonIntegrer.addClickListener(e -> {
                        showOrderDetailsDialog(commande, con);
                    });
                    return boutonIntegrer;
                }).setHeader("Détail");

                grid.setItems(commandes);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout(ajoutCommandeButton, supprimerCommandeButton);
        buttonsLayout.setSpacing(true);

        add(
            clientComboBox,
            grid,
            buttonsLayout
        );
    }

    private void showOrderCreationDialog(Connection con) {
        Dialog detailsDialog = new Dialog();
        detailsDialog.setWidth("1600px");
        detailsDialog.setHeight("700px");
        detailsDialog.setCloseOnEsc(true);
        detailsDialog.setCloseOnOutsideClick(true);

        try {
            List<Produits> produits = GetProduits(con);
            ProduitGrid.removeAllColumns();
            ProduitGrid.addColumn(Produits::getRef).setHeader("ref").setWidth("5px");
            ProduitGrid.addColumn(Produits::getDes).setHeader("des").setWidth("5px");
            ProduitGrid.addColumn(Produits::getMateriaux).setHeader("Matériaux").setWidth("5px");
            ProduitGrid.addColumn(Produits::getPoids).setHeader("Poids").setWidth("5px");
            ProduitGrid.addComponentColumn(produit -> {
            // Créez un champ de quantité (IntegerField) pour chaque ligne
            IntegerField quantiteField = new IntegerField("");
            quantiteField.setWidth("65px");

            // Créez le bouton "Ajout"
            Button ajoutButton = new Button("Ajout");

            ajoutButton.addClickListener(e -> {
                // Obtenez la quantité saisie dans le champ
                int quantite = quantiteField.getValue();

                // Obtenez l'objet Produits associé à la ligne
                Produits produitSelectionne = produit;

                // Appelez votre méthode NewAchat avec les valeurs appropriées
                try { 
                NewAchat(con, quantite, produitSelectionne.getDes());}
                catch (SQLException ex) {
                ex.printStackTrace();
                }
            });

    // Retournez le composant qui inclut le champ de quantité et le bouton
    return new HorizontalLayout(quantiteField, ajoutButton);
}).setHeader("Ajout");




            ProduitGrid.setItems(produits);
            AchatGrid.setItems(Achats);
            AchatGrid.addColumn(Achat::getProduit).setHeader("Produit");
            AchatGrid.addColumn(Achat::getQuantité).setHeader("quantité");
            AchatGrid.addColumn(Achat::getMontant).setHeader("montant");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        CréationCommande.addClickListener(e -> {
            Client ClientSelectionne =clientComboBox.getValue();
            
            try {
            int Idcommande = createCommande(con,ClientSelectionne.getId());
            for (Achat achat : Achats) {
                createAchat(con, getIdProduitByDesProduit(con, achat.getProduit()), achat.getQuantité(), Idcommande);
                detailsDialog.close();
                UpdateCommandes(con, ClientSelectionne.getId());
            }
            }catch (SQLException ex){ ex.printStackTrace();}
            
        });
        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(ProduitGrid,AchatGrid);
        gridLayout.setSpacing(true);
        
        detailsDialog.add(new H3("Nouvelle Commande"), gridLayout,CréationCommande);
            
        detailsDialog.open();
    }

    private void showOrderDetailsDialog(commande selectedCommande, Connection con) {
        Dialog detailsDialog = new Dialog();
        detailsDialog.setWidth("800px");
        detailsDialog.setHeight("500px");
        detailsDialog.setCloseOnEsc(true);
        detailsDialog.setCloseOnOutsideClick(true);

        try {
            List<Achat> achats = GetAchat(con, selectedCommande.getId());
            detailsGrid.removeAllColumns();
            detailsGrid.addColumn(Achat::getId).setHeader("Id");
            detailsGrid.addColumn(Achat::getProduit).setHeader("Produit");
            detailsGrid.addColumn(Achat::getQuantité).setHeader("Quantité");
            detailsGrid.addColumn(Achat::getMontant).setHeader("Montant");

            detailsGrid.setItems(achats);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        detailsDialog.add(new H3("Détails de la Commande"), detailsGrid);
        detailsDialog.open();
    }

    private void UpdateCommandes(Connection con, int idClient) throws SQLException {
        List<commande> Commande = GestionBDD.GetCommande(con, idClient);
        grid.setItems(Commande);
    }

    private void NewAchat(Connection con, int quantite, String Produit) throws SQLException {
        System.out.println("od");
        Achat nouvelAchat = new Achat(0, Produit, quantite, PrixAchat(con,getIdProduitByDesProduit(con, Produit), quantite));
        Achats.add(nouvelAchat);
        AchatGrid.setItems(Achats);
    }
}
