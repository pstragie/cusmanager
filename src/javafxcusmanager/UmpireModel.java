/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author pieter
 */
public class UmpireModel {
    
    private ObservableList<String> umpirelijstPerafdeling;
    private Umpires umpirelijst;
    private ObservableList<Umpire> umpires;

    private final ObjectProperty<ListCell<String>> dragSource = new SimpleObjectProperty<>();
    
    // Constructor
    public UmpireModel(ObservableList umpires) {
        this.umpires = umpires;
    }
    
    
    
    public VBox createUmpireContent(String afd) {
            ListView<String> umpireListview = new ListView<>();
            umpirelijstPerafdeling = FXCollections.observableArrayList();
            umpirelijstPerafdeling.addListener((ListChangeListener.Change<? extends String> change) -> { 
            while(change.next()) {
                if(change.wasUpdated()) {
                    System.out.println("Update umpire detected");
                    // Write to file

                } else
                    if (change.wasPermutated()) {
                        System.out.println("Was permutated: umpire");
                    } else {
                        if (change.wasAdded()) {
                            //System.out.println("Data " + change + " was added to clubs");
                            // Write to database

                        }
                    }

            }
        });
            if (umpires == null) {
                // Geen umpires in de lijst
            } else {
                
                umpires.forEach(u -> {
                    // For each afdeling get the umpires
                    ArrayList<Afdeling> arrayAfd = u.getUmpireAfdelingen();
                    for(Afdeling a : arrayAfd) {
                        if (a.getAfdelingsNaam().equals(afd)) {
                            umpirelijstPerafdeling.add(u.getUmpireNaam());
                        }
                    }
                });
            }
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
                       event.acceptTransferModes(TransferMode.MOVE);
                   }
               });

               //cell.setOnDragDone(event -> umpireListview.getItems().remove(cell.getItem()));

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
            Text placeHolder = new Text( " Geen umpires in deze afdeling." );
                    placeHolder.setFont( Font.font( null, FontWeight.BOLD, 14 ) );
                    BooleanBinding bb = Bindings.isEmpty ( umpirelijstPerafdeling );
                    placeHolder.visibleProperty().bind( bb );
                    placeHolder.managedProperty().bind( bb );
            umpiresBox.getChildren().add(placeHolder);
            umpiresBox.getChildren().add(umpireListview); // Add listview to Vertical Box
            
            return umpiresBox; // return VBox with listview of clubs per afdeling
        }
}
