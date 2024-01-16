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

            H1 title = new H1("Chaîne de production");
            title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

            SideNav nav = getSideNav();

            Scroller scroller = new Scroller(nav);
            

            addToDrawer(scroller);
            addToNavbar(toggle, title);
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
                new SideNavItem("Commandes", "/Order_View", VaadinIcon.CART.create()),
                
                new SideNavItem("Clients", "/clients_View", 
                        VaadinIcon.USERS.create()),
                new SideNavItem("Machine", "/machine_View",
                        VaadinIcon.COGS.create()),
                new SideNavItem("Produit", "/produit_View",
                        VaadinIcon.PACKAGE.create()),
                new SideNavItem("Type d'opération", "/operation_View",
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
