/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cusman;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author pieter
 */
public class CUSMan extends Application {
    //Stage stage = new Stage();
    //MainPanel testMainPanel = new MainPanel();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        try { Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("Started DERBY in embedded mode");
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found when starting embedded driver");
        }
        
        MainPanel mainPanel = new MainPanel();
        StackPane root = new StackPane();
        
        //StackPane root = FXMLLoader.load(getClass().getResource("Libraries/Undecorator.jar/delopapp/classic/ClientArea.fxml"));
        root.getChildren().add(mainPanel.MainPanel());
        //Undecorator undecorator = new Undecorator(primaryStage, root);
 
        // Default theme
        //undecorator.getStylesheets().add("skin/undecorator.css");
        //undecorator.setFadeInTransition();
         
        Scene scene = new Scene(root, 2000, 800);
        scene.setFill(Color.TRANSPARENT);
        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("css/BorderStyles.css").toExternalForm());
        
        primaryStage.setTitle("CUS Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException ex) {
                Logger.getLogger(CUSMan.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    /**
     * @param args the command line arguments
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        
        
        
        
        launch(args);
    }
    
}
