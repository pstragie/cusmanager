/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author pieter
 */
public class JavaFxCusManager extends Application {
    private GameSchedulePersistence gsp;
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

    @FXML
    private void handleNew() {
        //gsp.getScheduleData().clear();
        gsp.setScheduleFilePath(null);
    }
    
    @FXML
    private void handleSave() {
        File scheduleFile = gsp.getScheduleFilePath();
        if (scheduleFile != null) {
            gsp.saveScheduleDataToFile(scheduleFile);
        } else {
            System.out.println("Did not find file.");
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
