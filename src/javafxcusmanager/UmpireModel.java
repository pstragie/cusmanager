/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author pieter
 */
public class UmpireModel {
    
    public ObservableList<Umpire> umpirelijstPerafdeling;
    private Umpires umpirelijst;
    private ObservableList<Umpire> umpires;
    private MainPanel mainPanel;
    private final ObjectProperty<ListCell<Umpire>> dragSource = new SimpleObjectProperty<>();
    
    // Constructor
    public UmpireModel(ObservableList<Umpire> umpires) {
        this.umpires = umpires;
    }
    
    
    /** Create Umpire list
     * 
     * @param afd
     * @return VBox with listview 
     */
    public VBox createUmpireContent(Afdeling afd) {
            ListView<Umpire> umpireListview = new ListView<>();
            umpirelijstPerafdeling = FXCollections.observableArrayList();
            umpirelijstPerafdeling.addListener((ListChangeListener.Change<? extends Umpire> change) -> { 
                while(change.next()) {
                    if(change.wasUpdated()) {
                        System.out.println("Update umpire detected");
                        // Write to file

                    } else
                        if (change.wasPermutated()) {
                            System.out.println("Was permutated: umpire");
                        } else {
                            if (change.wasAdded()) {
                                System.out.println("Data " + change + " was added to umpires");
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
                        if (a.getAfdelingsNaam().equals(afd.getAfdelingsNaam()) && u.getActief()) {
                            umpirelijstPerafdeling.add(u);
                        }
                    }
                });
            }
            
            umpireListview.getItems().addAll(umpirelijstPerafdeling);
            umpireListview.setPrefSize(150, 800);
            umpireListview.setOrientation(Orientation.VERTICAL);
            umpireListview.setCellFactory(param -> {
                ListCell<Umpire> cell = new ListCell<Umpire>() {
                    @Override
                    protected void updateItem(Umpire item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getUmpireNaam() + " " + item.getUmpireVoornaam());
                        }
                    }
                };
                cell.setOnDragDetected(event -> {
                   if (! cell.isEmpty()) {
                       Dragboard db = cell.startDragAndDrop(TransferMode.COPY);
                       ClipboardContent cc = new ClipboardContent();
                       cc.putString(cell.getItem().getUmpireNaam() + " " + cell.getItem().getUmpireVoornaam());
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

                        ListCell<Umpire> dragSourceCell = dragSource.get();
                        umpireListview.getItems().add(dragSourceCell.getItem());
                        event.setDropCompleted(true);
                        dragSource.set(null);
                    } else {
                        event.setDropCompleted(false);
                    }
                });
                cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                
                @Override
                public void handle(ContextMenuEvent menuEvent) {
                    System.out.println("Context menu requested.");
                    final ContextMenu cm = new ContextMenu();
                    MenuItem verbergen = new MenuItem("Umpire actief/niet actief");
                    cm.getItems().add(verbergen);
                    verbergen.setOnAction(wiscel -> {
                        System.out.println("Verberg umpire");
                        int newIndex = umpires.indexOf(umpires.get(cell.getIndex()));
                        if (umpires.get(newIndex).getActief()) {
                            umpires.get(newIndex).setActief(Boolean.FALSE);
                        } else {
                            umpires.get(newIndex).setActief(Boolean.TRUE);
                        }
                        
                    });
                    
                    cell.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(cell.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                }
                }); 
                return cell;
            });
            /*
            umpireListview.setCellFactory(event -> {
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

                @Override
                public Cell<String> call(ListView<Umpire> param) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            */
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
