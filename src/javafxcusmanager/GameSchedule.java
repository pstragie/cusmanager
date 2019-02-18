/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

/**
 *
 * @author pieter
 */
public class GameSchedule {
    
    private static final int MAX_ITEMS = 3;
    private final TableView<Game> table = new TableView<>();
    private ObservableList<Game> data = FXCollections.observableArrayList(
        new Game("Frogs", "Wolverines", "Isabelle Verelst"),
        new Game("Wielsbeke", "Doornik", "Pieter Stragier")
    );
    FilteredList<Game> filteredData = new FilteredList<>(
            data,
            game -> data.indexOf(game) < MAX_ITEMS
    );
    // Constructor
    public GameSchedule() {
    
    }
    
    public VBox createCalendar(String periode) {
        VBox calendarbox = new VBox();
        // Get empty Weekend game model
            
        /*
            table.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // data is dragged over the target 
                Dragboard db = event.getDragboard();
                if (event.getDragboard().hasString()){
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
                }
            });
        */
            /*
            table.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (event.getDragboard().hasString()) {            

                    String text = db.getString();
                    data.add(new Game(text, "iets", "iets anders"));
                    table.setItems(data);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
                } 
            });  
           */
            
            final Label label = new Label("Game Schedule");
            label.setFont(new Font("Arial", 20));

            table.setEditable(true);
            table.setDisable(false);
            
            
            /*
            Callback<TableColumn, TableCell> cellFactory = 
                    new Callback<TableColumn, TableCell>() {
                        public TableCell call(TableColumn p) {
                            return new EditingCell();
                        }
                    };
            */
            TableColumn homeTeamCol = new TableColumn("Home team");
            homeTeamCol.prefWidthProperty().bind(table.widthProperty().divide(3));
            homeTeamCol.setCellValueFactory(
                new PropertyValueFactory<Game, String>("homeTeamName"));
            homeTeamCol.setCellFactory(e -> {
                TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        // Make sure you call super.updateItem, or you might get really weird bugs.
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                            setGraphic(null);
                        }
                    }
                    };
                
                cell.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    // data is dragged over the target 
                    Dragboard db = event.getDragboard();
                    if (event.getDragboard().hasString()){
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                    }
                });
                
                // HERE IS HOW TO GET THE ROW AND CELL INDEX
                cell.setOnDragDetected(eh -> {
                    // Get the row index of this cell
                    int rowIndex = cell.getIndex();
                    System.out.println("Cell row index: " + rowIndex);

                    // Get the column index of this cell.
                    int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
                    System.out.println("Cell column index: " + columnIndex);
                });
        
                cell.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    int rowIndex = cell.getIndex();
                    System.out.println("Cell row index: " + rowIndex);

                    // Get the column index of this cell.
                    int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
                    System.out.println("Cell column index: " + columnIndex);
                    
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (event.getDragboard().hasString()) {            

                        String text = db.getString();
                        
                        if(rowIndex < 0 || rowIndex >= data.size()) {
                            int sprong = rowIndex - data.size();
                            for(int i=0; i<sprong; i++) {
                                data.add(new Game(null, null, null));
                            }
                            data.add(new Game(text, null, null));
                        } else {
                            data.set(rowIndex, new Game(text, data.get(rowIndex).visitingteam.get(), data.get(rowIndex).umpire.get()));

                        }
                        table.setItems(data);
                        success = true;
                    }
                    event.setDropCompleted(success);
                    event.consume();
                    } 
                });  
                return cell;
            });
            
            

            //homeTeamCol.setCellFactory(cellFactory);
            /*homeTeamCol.setOnEditCommit(new EventHandler<CellEditEvent<Game, String>>() {
                @Override
                public void handle(CellEditEvent<Game, String> t) {
                    ((Game) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    ).setHomeTeamName(t.getNewValue());
                }
            });*/
            TableColumn visitingTeamCol = new TableColumn("Visiting team");
            visitingTeamCol.prefWidthProperty().bind(table.widthProperty().divide(3));
            visitingTeamCol.setCellValueFactory(
                new PropertyValueFactory<Game, String>("visitingTeamName")
                );
            visitingTeamCol.setCellFactory(e -> {
                TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        // Make sure you call super.updateItem, or you might get really weird bugs.
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                            setGraphic(null);
                        }
                    }
                    };
                
                cell.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    // data is dragged over the target 
                    Dragboard db = event.getDragboard();
                    if (event.getDragboard().hasString()){
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                    }
                });
                
                // HERE IS HOW TO GET THE ROW AND CELL INDEX
                cell.setOnDragDetected(eh -> {
                    // Get the row index of this cell
                    int rowIndex = cell.getIndex();
                    System.out.println("Cell row index: " + rowIndex);

                    // Get the column index of this cell.
                    int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
                    System.out.println("Cell column index: " + columnIndex);
                });
        
                cell.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    int rowIndex = cell.getIndex();
                    System.out.println("Cell row index: " + rowIndex);

                    // Get the column index of this cell.
                    int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
                    System.out.println("Cell column index: " + columnIndex);
                    
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (event.getDragboard().hasString()) {            

                        String text = db.getString();
                        if(rowIndex < 0 || rowIndex >= data.size()) {
                            data.add(new Game(null, text, null));
                        } else {
                            data.set(rowIndex, new Game(data.get(rowIndex).hometeam.get(), text, data.get(rowIndex).umpire.get()));
                        }
                        table.setItems(data);
                        success = true;
                    }
                    event.setDropCompleted(success);
                    event.consume();
                    } 
                });  
                return cell;
            });
            //visitingTeamCol.setCellFactory(cellFactory);
            /*visitingTeamCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Game, String>>() {
                @Override
                public void handle(CellEditEvent<Game, String> t) {
                    ((Game) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setVisitingTeamName(t.getNewValue());
                    }
                }
            );*/
            TableColumn umpireCol = new TableColumn("Umpire");
            umpireCol.prefWidthProperty().bind(table.widthProperty().divide(3));
            umpireCol.setCellValueFactory(
                new PropertyValueFactory<Game, String>("umpireName")
            );
            umpireCol.setCellFactory(e -> {
                TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        // Make sure you call super.updateItem, or you might get really weird bugs.
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                            setGraphic(null);
                        }
                    }
                    };
                
                cell.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    // data is dragged over the target 
                    Dragboard db = event.getDragboard();
                    if (event.getDragboard().hasString()){
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                    }
                });
                
                // HERE IS HOW TO GET THE ROW AND CELL INDEX
                cell.setOnDragDetected(eh -> {
                    // Get the row index of this cell
                    int rowIndex = cell.getIndex();
                    System.out.println("Cell row index: " + rowIndex);

                    // Get the column index of this cell.
                    int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
                    System.out.println("Cell column index: " + columnIndex);
                });
        
                cell.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    int rowIndex = cell.getIndex();
                    System.out.println("Cell row index: " + rowIndex);

                    // Get the column index of this cell.
                    int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
                    System.out.println("Cell column index: " + columnIndex);
                    
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (event.getDragboard().hasString()) {            

                        String text = db.getString();
                        if(rowIndex < 0 || rowIndex >= data.size()) {
                            data.add(new Game(null, null, text));
                        } else {
                            data.set(rowIndex, new Game(data.get(rowIndex).hometeam.get(), data.get(rowIndex).visitingteam.get(), text));
                        }
                        table.setItems(data);
                        success = true;
                    }
                    event.setDropCompleted(success);
                    event.consume();
                    } 
                });  
                return cell;
            });
            //umpireCol.setCellFactory(cellFactory);
            /*umpireCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Game, String>>() {
                @Override
                public void handle(CellEditEvent<Game, String> t) {
                    ((Game) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setUmpire1(t.getNewValue());
                    }
                }
            );
            */
            System.out.println("Data for table: " + data);
            
            table.setItems(data);
            table.setFixedCellSize(25);
            table.setMaxHeight(128);
            table.setMinHeight(128);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            
            //table.prefHeight(100 + 45);
            //table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(table.getFixedCellSize()).add(45));

            System.out.println("table data: " + table.getItems());
            table.getColumns().addAll(homeTeamCol, visitingTeamCol, umpireCol);

            table.getSelectionModel().setCellSelectionEnabled(true);
            
            calendarbox.setPadding(new Insets(0, 0, 0, 0));
            calendarbox.getChildren().addAll(label, table);
        
        
        return calendarbox;
    }
    
    
    public static class Game {
        private final SimpleStringProperty hometeam;
        private final SimpleStringProperty visitingteam;
        private final SimpleStringProperty umpire;

        private Game(String homeTeamName, String visitingTeamName, String umpireName) {
            this.hometeam = new SimpleStringProperty(homeTeamName);
            this.visitingteam = new SimpleStringProperty(visitingTeamName);
            this.umpire = new SimpleStringProperty(umpireName);
        }

        public String getHomeTeamName() {
            return hometeam.get();
        }
        public void setHomeTeamName(String homeTeamName) {
            hometeam.set(homeTeamName);
        }

        public String getVisitingTeamName() {
            return visitingteam.get();
        }
        public void setVisitingTeamName(String visitingTeamName) {
            visitingteam.set(visitingTeamName);
        }

        public String getUmpireName() {
            return umpire.get();
        }
        public void setUmpireName(String umpireName) {
            umpire.set(umpireName);
        }
        
    }
    
    class EditingCell extends TableCell<Game, String> {
 
        private TextField textField;
 
        public EditingCell() {
        }
 
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
 
            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
 
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, 
                    Boolean arg1, Boolean arg2) {
                        if (!arg2) {
                            commitEdit(textField.getText());
                        }
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

}
