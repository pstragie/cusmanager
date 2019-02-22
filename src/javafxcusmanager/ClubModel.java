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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author pieter
 */
public class ClubModel {
    
        private final ObjectProperty<ListCell<String>> dragSource = new SimpleObjectProperty<>();
        private ArrayList<String> clublijstPerafdeling;
        private Clubs clublijst;
        
    // Constructor
	public ClubModel() {


	}

	public VBox createClubContent(String afd) {
            ListView<String> clubListview = new ListView<>();
            clublijstPerafdeling = new ArrayList<>();
            clublijst = new Clubs();
            Map<String, String> clubmap = clublijst.getList();
            clubmap.forEach((k,val) ->  {
                if (val.equals(afd)) {
                    clublijstPerafdeling.add(k);
                }
            });
            ObservableList<String> data = FXCollections.<String>observableArrayList(clublijstPerafdeling);
            clubListview.getItems().addAll(data);
            clubListview.setPrefSize(150, 800);
            clubListview.setOrientation(Orientation.VERTICAL);
            clubListview.setCellFactory(lv -> {
                ListCell<String> cell = new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                    }
                };
                cell.setOnDragDetected(event -> {
                   if (! cell.isEmpty()) {
                       Dragboard db = cell.startDragAndDrop(TransferMode.COPY);
                       ClipboardContent cc = new ClipboardContent();
                       cc.putString(cell.getItem());
                       db.setContent(cc);
                       dragSource.set(cell);
                   }
               });

               cell.setOnDragOver(event -> {
                   Dragboard db = event.getDragboard();
                   if (db.hasString()) {
                       event.acceptTransferModes(TransferMode.COPY);
                   }
               });

               //cell.setOnDragDone(event -> clubListview.getItems().remove(cell.getItem()));

               cell.setOnDragDropped(event -> {
                   Dragboard db = event.getDragboard();
                   if (db.hasString() && dragSource.get() != null) {
                       // in this example you could just do
                       // listView.getItems().add(db.getString());
                       // but more generally:

                       ListCell<String> dragSourceCell = dragSource.get();
                       clubListview.getItems().add(dragSourceCell.getItem());
                       event.setDropCompleted(true);
                       dragSource.set(null);
                   } else {
                       event.setDropCompleted(false);
                   }
               });

               return cell ;
            });
            //clubListview.getItems().addAll(clublijstPerafdeling);
            
            VBox clubsBox = new VBox();
            clubsBox.getChildren().add(clubListview); // Add listview to Vertical Box
            
            return clubsBox; // return VBox with listview of clubs per afdeling
        }
        
        
}
