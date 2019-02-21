/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author pieter
 */
public class JavaFxCusManager extends Application {
    Stage stage = new Stage();
    MainPanel testMainPanel = new MainPanel();
    @Override
    public void start(Stage primaryStage) {
        MainPanel mainPanel = new MainPanel();
        StackPane root = new StackPane();
        root.getChildren().add(mainPanel.MainPanel());
        
        Scene scene = new Scene(root, 1600, 800);
        scene.getStylesheets().add(getClass().getResource("css/BorderStyles.css").toExternalForm());
        primaryStage.setTitle("CUS Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
