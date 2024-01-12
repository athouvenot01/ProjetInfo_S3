/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.getTableValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author loicrosian
 */

@Route(value = "")
public class UI extends AppLayout implements RouterLayout{
    
    public UI() {
        //contenu de la page de l'interface utilisateur
        try (Connection con = GestionBDD.connectSurServeurM3()){
            
            DrawerToggle toggle = new DrawerToggle();

            H1 title = new H1("Base de donnée");
            title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

            SideNav nav = getSideNav();

            Scroller scroller = new Scroller(nav);
            //scroller.setClassName(LumoUtility.Padding.SMALL);

            addToDrawer(scroller);
            addToNavbar(toggle, title);
            /*H2 title = new H2 ("Binvenue dans la base donnée");
            Button produit = new Button ("produit");
            Button machine = new Button ("machine"); 
            Button realise = new Button ("réalise ");
            Button typeoperation = new Button ("type d'opération");
        
            produit.addClickListener(click ->{
                List<String> chaines = Arrays.asList(
                "Chaine 1",
                "Chaine 2",
                "Chaine 3"
            );
            Grid<String> grid = new Grid<>();
            grid.setItems(chaines);
            
            grid.addColumn(String::toString).setHeader("Colonne de Chaînes");

            add(grid);
            });
       
            machine.addClickListener(click ->{
                // Créez un composant Grid
                Grid<Object> liste_machine = new Grid<>(Object.class);
                
                // Ajoutez les colonnes au Grid
                liste_machine.setColumns();
                
                // Ajoutez les lignes au Grid
                liste_machine.setItems();
                
                // Ajoutez le Grid à la mise en page
                add(liste_machine);
            });
         
            realise.addClickListener(click ->{
               // Créez un composant Grid
                Grid<Object> liste_realise = new Grid<>(Object.class);
                
                // Ajoutez les colonnes au Grid
                liste_realise.setColumns();
                
                // Ajoutez les lignes au Grid
                liste_realise.setItems();
                
                // Ajoutez le Grid à la mise en page
                add(liste_realise);
            });
          
            typeoperation.addClickListener(click ->{
                // Créez un composant Grid
                Grid<Object> liste_typeoperation = new Grid<>(Object.class);
                
                // Ajoutez les colonnes au Grid
                liste_typeoperation.setColumns();
                
                // Ajoutez les lignes au Grid
                liste_typeoperation.setItems();
                
                // Ajoutez le Grid à la mise en page
                add(liste_typeoperation);
            });
        
           
            add(
                title, 
                new VerticalLayout(produit, machine, realise, typeoperation),
                new RouterLink("Table produit", produit_View.class),
                new RouterLink("Table machine", machine_View.class)
                        );
            */
        }
        catch (SQLException ex) {
            System.out.println("probleme : " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }       
    
    private SideNav getSideNav() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(
                //new SideNavItem("Dashboard", "/dashboard",
                        //VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Orders", "/Order_View", VaadinIcon.CART.create()),
                
                new SideNavItem("Clients", "/clients_View", 
                        VaadinIcon.USERS.create()),
                new SideNavItem("Machine", "/machine_View",
                        VaadinIcon.COGS.create()),
                new SideNavItem("Produit", "/produit_View",
                        VaadinIcon.PACKAGE.create()),
                new SideNavItem("Opération", "/operation_View",
                        VaadinIcon.HAMMER.create()),
                new SideNavItem("Opérateur", "/operateur_View",
                        VaadinIcon.USER.create()),
                new SideNavItem("Matériaux", "/matériau_View",
                        VaadinIcon.STOCK.create()),
                new SideNavItem("Poste de Travail", "/posteTravail_View",
                        VaadinIcon.DESKTOP.create()));
                //new SideNavItem("Analytics", "/analytics",
                        //VaadinIcon.CHART.create()));
        return sideNav;
    }
    
}
