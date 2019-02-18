/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author pieter
 */
public class UmpireModel {
    
    private ArrayList<String> umpirelijstPerafdeling;
    private Umpires umpirelijst;
    private final ObjectProperty<ListCell<String>> dragSource = new SimpleObjectProperty<>();
    
    // Constructor
    public UmpireModel () {
        
    }
    
    public VBox createUmpireContent(String afd) {
            ListView<String> umpireListview = new ListView<>();
            umpirelijstPerafdeling = new ArrayList<>();
            umpirelijst = new Umpires();
            Map<String, String> umpiremap = umpirelijst.getList();
            System.out.println("umpiremap:" + umpiremap);
            umpiremap.forEach((k,val) ->  {
                if (val.equals(afd)) {
                    System.out.println(k);
                    umpirelijstPerafdeling.add(k);
                }
            });
            System.out.println("umpirelijstPerafdeling" + "(" + afd + "): " + umpirelijstPerafdeling);
            ObservableList<String> data = FXCollections.<String>observableArrayList(umpirelijstPerafdeling);
            umpireListview.getItems().addAll(data);
            umpireListview.setPrefSize(150, 800);
            umpireListview.setOrientation(Orientation.VERTICAL);
            umpireListview.setCellFactory(lv -> {
                ListCell<String> cell = new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                    }
                };
                cell.setOnDragDetected(event -> {
                   if (! cell.isEmpty()) {
                       Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                       ClipboardContent cc = new ClipboardContent();
                       cc.putString(cell.getItem());
                       db.setContent(cc);
                       dragSource.set(cell);
                   }
               });

               cell.setOnDragOver(event -> {
                   Dragboard db = event.getDragboard();
                   if (db.hasString()) {
                       event.acceptTransferModes(TransferMode.MOVE);
                   }
               });

               cell.setOnDragDone(event -> umpireListview.getItems().remove(cell.getItem()));

               cell.setOnDragDropped(event -> {
                   Dragboard db = event.getDragboard();
                   if (db.hasString() && dragSource.get() != null) {
                       // in this example you could just do
                       // listView.getItems().add(db.getString());
                       // but more generally:

                       ListCell<String> dragSourceCell = dragSource.get();
                       umpireListview.getItems().add(dragSourceCell.getItem());
                       event.setDropCompleted(true);
                       dragSource.set(null);
                   } else {
                       event.setDropCompleted(false);
                   }
               });

               return cell ;
            });
            //umpireListview.getItems().addAll(umpirelijstPerafdeling);
            
            VBox umpiresBox = new VBox();
            umpiresBox.getChildren().add(umpireListview); // Add listview to Vertical Box
            
            return umpiresBox; // return VBox with listview of clubs per afdeling
        }
}
