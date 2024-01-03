/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.insa.info_s3;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author loicrosian
 */

@SpringBootApplication
@PWA(name = "Gestion Base de donnée", shortName = "Gestion BDD")
@Theme("Lumo")
public class Interface_projet implements AppShellConfigurator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Interface_projet.class, args);
    }
    
}
