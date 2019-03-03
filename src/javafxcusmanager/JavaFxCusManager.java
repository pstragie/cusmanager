/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import insidefx.undecorator.Undecorator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author pieter
 */
public class JavaFxCusManager extends Application {
    //Stage stage = new Stage();
    //MainPanel testMainPanel = new MainPanel();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainPanel mainPanel = new MainPanel();
        StackPane root = new StackPane();
        
        //StackPane root = FXMLLoader.load(getClass().getResource("Libraries/Undecorator.jar/delopapp/classic/ClientArea.fxml"));
        root.getChildren().add(mainPanel.MainPanel());
        //Undecorator undecorator = new Undecorator(primaryStage, root);
 
        // Default theme
        //undecorator.getStylesheets().add("skin/undecorator.css");
        //undecorator.setFadeInTransition();
         
        Scene scene = new Scene(root, 2000, 500);
        scene.setFill(Color.TRANSPARENT);
        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("css/BorderStyles.css").toExternalForm());
        primaryStage.setTitle("CUS Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        
        try {
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/cusdb;create=false;user=pstragier;password=Isabelle30?");
            Statement stmt = con.createStatement();
            
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM APP.Afdelingen");
            while(rs.next()) {
                String s = rs.getString("afdelingsnaam");
                System.out.println(s);
            }
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        }
        
        
        launch(args);
    }
    
}
