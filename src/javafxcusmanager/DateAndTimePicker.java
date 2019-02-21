/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

/**
 *
 * @author pieter
 */
import java.util.Calendar;
import java.util.Locale;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.scene.control.CalendarPicker;
 
public class DateAndTimePicker extends Application {
 
    private Stage stage;
    private DatePicker checkInDatePicker;
    
    public DateAndTimePicker() {
        
    }
 
    @Override
    public void start(Stage primaryStage) {
        CalendarPicker dateTime = new CalendarPicker();
        dateTime.withCalendar(Calendar.getInstance());
        dateTime.withShowTime(Boolean.TRUE);
        dateTime.withLocale(Locale.ENGLISH);
        dateTime.calendarProperty().addListener(new ChangeListener<Calendar>() {

            @Override
            public void changed(ObservableValue<? extends Calendar> ov, Calendar t, Calendar t1) {
                System.out.println("Selected date: "+t1.getTime().toString());
            }
        });
    
        StackPane root = new StackPane();
    root.getChildren().add(dateTime);

    Scene scene = new Scene(root, 300, 250);
    primaryStage.setTitle("Date & Time from JFXtras 2.2");
    primaryStage.setScene(scene);
    primaryStage.show();
    }
    
   
}