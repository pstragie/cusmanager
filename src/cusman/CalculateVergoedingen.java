/*
 * Restricted License.
 * No dispersal allowed.
 */
package cusman;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author Pieter Stragier <pstragier@gmail.be>
 */
public class CalculateVergoedingen {
    
    private MainPanel mainpanel;
    private Database database;
    private Preferences pref;
    private ObservableList<Umpire> umpires;
    private ObservableList<Club> clubs;
    private ObservableList<Vergoedingskosten> vergoedingen;
    private ObservableList<Game> games;
    private ObservableList<Afdeling> afdelingen;
    private Label umpLabel, periodfrom, periodto, periode, totaalLabel;
    private ComboBox umpCombo;
    private TextField periodFrom, periodTo, totaalBedrag;
    private Button berekenUmpire, berekenAlleUmpires, exporteer, sluiten;
    private ObservableList<Vergoeding> observableVergoedingList = FXCollections.observableArrayList();
    private Boolean all;
    private VBox leftBox = new VBox(10);
    private VBox overzicht = new VBox(10);
    private Double totaalAanUitbetalingen = 0.0;
    
    public CalculateVergoedingen(ObservableList<Umpire> umpires, ObservableList<Club> clubs, ObservableList<Vergoedingskosten> vergoedingen, ObservableList<Game> games, ObservableList<Afdeling> afdelingen) {
        this.umpires = umpires;
        this.clubs = clubs;
        this.vergoedingen = vergoedingen;
        this.games = games;
        this.afdelingen = afdelingen;
        mainpanel = new MainPanel();
        database = new Database();
        pref = Preferences.userNodeForPackage(AppSettings.class);
        all = Boolean.FALSE;
        overzicht.prefHeight(500);
    }
    
    /** Calculate vergoedingen
     * 
     * @return 
     */
    public HBox calculateVergoedingen() {
        HBox mainHBox = new HBox(10);
        mainHBox.setPadding(new Insets(10, 10, 10, 10));
        // Left options
        GridPane grid = new GridPane();
        grid.setHgap(5);
        umpLabel = new Label("Umpire ");
        periode = new Label("Periode ");
        periodfrom = new Label("Van: ");
        periodto = new Label("Tot: ");
        periodFrom = new TextField("10/03/2018");
        periodTo = new TextField("30/04/2018");
        umpCombo = new ComboBox(umpires);
        umpCombo.getSelectionModel().select(umpires.get(0));
        totaalLabel = new Label("Totaal: ");
        totaalBedrag = new TextField("");
        totaalBedrag.setEditable(false);
        grid.add(umpLabel, 0, 1);
        grid.add(umpCombo, 1, 1);
        //grid.add(periode, 2, 1);
        //grid.add(periodfrom, 3, 1);
        //grid.add(periodFrom, 4, 1);
        //grid.add(periodto, 5, 1);
        //grid.add(periodTo, 6, 1);
        HBox totBox = new HBox(10);
        totBox.setPadding(new Insets(10, 10, 10, 0));
        totaalBedrag.setText(Double.toString(totaalAanUitbetalingen));
        totBox.getChildren().addAll(totaalLabel, totaalBedrag);
        leftBox.getChildren().add(grid);
        leftBox.getChildren().add(overzicht);
        leftBox.getChildren().add(totBox);
        HBox buttonBox = new HBox(5);
        berekenUmpire = new Button("Bereken umpire");
        berekenAlleUmpires = new Button("Bereken alle umpires");
        exporteer = new Button("Exporteer");
        sluiten = new Button("Sluiten");
        
        berekenUmpire.setOnAction(bereken -> {
            all = Boolean.FALSE;
            ArrayList<Umpire> umpArray = new ArrayList<>();
            Umpire ump = (Umpire) umpCombo.getSelectionModel().getSelectedItem();
            umpArray.add(ump);
            berekenVergoedingen(umpArray, mainpanel.stringToLocalDate(periodFrom.getText()), mainpanel.stringToLocalDate(periodTo.getText()));
            observableVergoedingList.clear();
            observableVergoedingList.addAll(database.getUitbetalingenFromDatabase(umpArray.get(0).getUmpireLicentie()));
            totaalAanUitbetalingen = 0.0;
            observableVergoedingList.forEach(d -> {
                totaalAanUitbetalingen += d.getTotaal();
            });
            totaalBedrag.setText(round(totaalAanUitbetalingen, 2) + " €");
            leftBox.getChildren().remove(overzicht);
            overzicht.getChildren().clear();
            overzicht.getChildren().add(showTable(observableVergoedingList));
            overzicht.getChildren().add(showDetailGrid(ump));
            leftBox.getChildren().add(overzicht);
        });
        berekenAlleUmpires.setOnAction(bereken -> {
            all = Boolean.TRUE;
            ArrayList<Umpire> umpArray = new ArrayList<>(umpires);
            berekenVergoedingen(umpArray, mainpanel.stringToLocalDate(periodFrom.getText()), mainpanel.stringToLocalDate(periodTo.getText()));
            observableVergoedingList.clear();
            observableVergoedingList.addAll(database.getAllUitbetalingenFromDatabase());
            totaalAanUitbetalingen = 0.0;
            observableVergoedingList.forEach(d -> {
                totaalAanUitbetalingen += d.getTotaal();
            });
            totaalBedrag.setText(round(totaalAanUitbetalingen, 2) + " €");
            leftBox.getChildren().remove(overzicht);
            overzicht.getChildren().clear();
            overzicht.getChildren().add(showTable(observableVergoedingList));
            leftBox.getChildren().add(overzicht);
        });
        sluiten.setOnAction(sluit -> {
           Stage stage = (Stage) sluiten.getScene().getWindow();
           stage.close();
        });
        exporteer.setOnAction(export -> {
            try {
                database.exportUitbetalingenToWorksheet();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CalculateVergoedingen.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                database.exportUitbetalingUmpireToWorksheet(umpires, afdelingen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CalculateVergoedingen.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        buttonBox.getChildren().addAll(berekenUmpire, berekenAlleUmpires, exporteer, sluiten);
        leftBox.getChildren().add(buttonBox);
        mainHBox.getChildren().add(leftBox);
        
        // Right calculations
        //VBox rightBox = new VBox();
        //ArrayList<Umpire> umpArray = new ArrayList<>();
        //umpArray.add((Umpire) umpCombo.getSelectionModel().getSelectedItem());
        //rightBox.getChildren().add(toonBerekeningen(umpArray, mainpanel.stringToLocalDate(periodFrom.getText()), mainpanel.stringToLocalDate(periodTo.getText())));
        //mainHBox.getChildren().add(rightBox);
        
        return mainHBox;
    }
    
    private void berekenVergoedingen(ArrayList<Umpire> ump, LocalDate dfrom, LocalDate dto) {
        
        Double kmOne = 0.0; // kilometers
        Double kmAll = 0.0;
        Double kmVergoedingOne = 0.0; // Berekening vergoeding km
        Double kmVergoedingAll = 0.0;
        Double gameVergoedingOne = 0.0;
        Double gameVergoedingAll = 0.0;
        Double totaalOne = 0.0;
        Double totaalAll = 0.0;
        Double kmvergoeding = Double.parseDouble((database.getVergoedingenFromDatabase("km")).replace(",", ".")); // Bedrag per km
        Map<String, String> afdelingsVergoeding = new HashMap<>();
        ArrayList<Umpire> umpArray = new ArrayList<>();
        // Fill dictionary (map)
        for (Afdeling afd : afdelingen) {
            String verg = database.getVergoedingenFromDatabase(afd.getAfdelingsNaam());
            afdelingsVergoeding.put(afd.getAfdelingsNaam(), verg);
        }
        umpArray.addAll(ump);
        
        
        for (Umpire u : umpArray) {
            kmOne = 0.0;
            gameVergoedingOne = 0.0;
            Integer aantalwed = 0;
            for (Game g : games) {
                if (g.getHomeClub() != null) {
                    if (g.getPlateUmpire() != null || g.getBase1Umpire() != null || g.getBase2Umpire() != null || g.getBase3Umpire() != null) {
                        //System.out.println("Club = " + g.getHomeClub().getClubNaam());
                        //System.out.println("umpire name = " + g.getPlateUmpire().getUmpireVoornaam() + " " + g.getPlateUmpire().getUmpireNaam());
                        if ((g.getPlateUmpire() != null && g.getPlateUmpire().getUmpireLicentie().equals(u.getUmpireLicentie())) || (g.getBase1Umpire() != null && g.getBase1Umpire().getUmpireLicentie().equals(u.getUmpireLicentie())) || (g.getBase1Umpire() != null && g.getBase2Umpire().getUmpireLicentie().equals(u.getUmpireLicentie())) || (g.getBase1Umpire() != null && g.getBase3Umpire().getUmpireLicentie().equals(u.getUmpireLicentie()))) {
                            aantalwed += 1;
                            String clubnr = g.getHomeClub().getClubNummer();
                            Double d = Double.parseDouble(database.getDistFromUmpireClub(u.getUmpireLicentie(), clubnr));
                            System.out.println("clubnummer = " + clubnr + ", distance = " + d);
                            kmOne += d;
                            kmAll += d;
                            gameVergoedingOne += Double.parseDouble(afdelingsVergoeding.get(g.getAfdelingString()));
                            gameVergoedingAll += Double.parseDouble(afdelingsVergoeding.get(g.getAfdelingString()));
                        }
                    }
                }
            }
            System.out.println("Aantal kilometers "+ u.getUmpireVoornaam() + " = " + kmOne * 2 + " km");
            kmVergoedingOne = kmOne * 2 * kmvergoeding;
            System.out.println("Kilometervergoeding " + u.getUmpireVoornaam() + " = " + kmOne * 2 * kmvergoeding + " €");
            System.out.println("Vergoeding volgens afdeling " + u.getUmpireVoornaam() + " = " + gameVergoedingOne + "€");
            totaalOne = kmVergoedingOne + gameVergoedingOne;
            System.out.println("Totaalbedrag 1 umpire " + u.getUmpireVoornaam() + " = " + totaalOne);
            // Umpire: Naam, aantal games, km, km €, game €, totaal
            
            try {
                if (database.checkIfUitbetalingExists(u)) {
                    // update
                    database.updateUitbetalingenToDatabase(u, kmOne, kmVergoedingOne, aantalwed, gameVergoedingOne, totaalOne, Boolean.FALSE);
                } else {
                    // insert
                    System.out.println("kmOne type = " + kmOne.getClass().getName());
                    System.out.println("kmE type = " + kmVergoedingOne.getClass().getName());
                    System.out.println("aw type = " + aantalwed.getClass().getName());
                    System.out.println("verg type = " + gameVergoedingOne.getClass().getName());
                    System.out.println("tot type = " + totaalOne.getClass().getName());
                    database.insertUitbetalingToDatabase(u, kmOne, kmVergoedingOne, aantalwed, gameVergoedingOne, totaalOne, Boolean.FALSE);
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(CalculateVergoedingen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Aantal kilometers alle umpires = " + kmAll * 2);
        kmVergoedingAll = kmAll * 2 * kmVergoedingAll;
        System.out.println("Kilometervergoeding alle umpires = " + kmAll * 2 * kmvergoeding + " €");
        System.out.println("Afdelingvergoeding alle umpires = " + gameVergoedingAll + " €");
        totaalAll = kmVergoedingAll + gameVergoedingAll;
        System.out.println("Totaalbedrag alle umpires = " + totaalAll);
        
    }
    
    private TableView showTable(ObservableList<Vergoeding> vergoedingen) {

        TableView vergTable = new TableView(vergoedingen);
        vergTable.setEditable(false);
        if (all) {
            vergTable.setPrefHeight(600);
        } else {
            vergTable.setPrefHeight(55);
        }
        vergTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        vergTable.setPrefWidth(700);
        TableColumn umpireCol = new TableColumn("Umpire");
        TableColumn aantalWCol = new TableColumn("# games");
        TableColumn kmCol = new TableColumn("km");
        TableColumn kmEuroCol = new TableColumn("€ km");
        TableColumn gameEuroCol = new TableColumn("€ games");
        TableColumn totaalUCol = new TableColumn("Totaal");
        
        umpireCol.setCellValueFactory(
            new PropertyValueFactory<>("umpire"));
        aantalWCol.setCellValueFactory(
            new PropertyValueFactory<>("aantalwedstrijden"));
        kmCol.setCellValueFactory(
            new PropertyValueFactory<>("kilometers"));
        kmEuroCol.setCellValueFactory(
            new PropertyValueFactory<>("kmeuro"));
        gameEuroCol.setCellValueFactory(
            new PropertyValueFactory<>("wedstrijdvergoeding"));
        totaalUCol.setCellValueFactory(
            new PropertyValueFactory<>("totaal"));
        
        umpireCol.setCellFactory(e -> {
            TableCell<ObservableList<Vergoeding>, Umpire> cell = new TableCell<ObservableList<Vergoeding>, Umpire> () {
                @Override
                public void updateItem(Umpire item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setStyle("");
                        setGraphic(null);
                    } else {
                        setText(item.getUmpireVoornaam() + " " + item.getUmpireNaam());
                        setStyle("");
                        setGraphic(null);
                    }
                }
                };
            
            return cell;
        });
        aantalWCol.setCellFactory(e -> {
            TableCell<ObservableList<Vergoeding>, Integer> cell = new TableCell<ObservableList<Vergoeding>, Integer> () {
                @Override
                public void updateItem(Integer item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setStyle("");
                        setGraphic(null);
                    } else {
                        setText(Integer.toString(item));
                        setStyle("");
                        setGraphic(null);
                    }
                }
                };
            
            return cell;
        });
        kmCol.setCellFactory(e -> {
            TableCell<ObservableList<Vergoeding>, Double> cell = new TableCell<ObservableList<Vergoeding>, Double> () {
                @Override
                public void updateItem(Double item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setStyle("");
                        setGraphic(null);
                    } else {
                        setText(Double.toString(round(item, 3)));
                        setStyle("");
                        setGraphic(null);
                    }
                }
                };
            
            return cell;
        });
        kmEuroCol.setCellFactory(e -> {
            TableCell<ObservableList<Vergoeding>, Double> cell = new TableCell<ObservableList<Vergoeding>, Double> () {
                @Override
                public void updateItem(Double item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setStyle("");
                        setGraphic(null);
                    } else {
                        setText(Double.toString(round(item, 2)) + " €");
                        setStyle("");
                        setGraphic(null);
                    }
                }
                };
            
            return cell;
        });
        gameEuroCol.setCellFactory(e -> {
            TableCell<ObservableList<Vergoeding>, Double> cell = new TableCell<ObservableList<Vergoeding>, Double> () {
                @Override
                public void updateItem(Double item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setStyle("");
                        setGraphic(null);
                    } else {
                        setText(Double.toString(round(item, 2)) + " €");
                        setStyle("");
                        setGraphic(null);
                    }
                }
                };
            
            return cell;
        });
        totaalUCol.setCellFactory(e -> {
            TableCell<ObservableList<Vergoeding>, Double> cell = new TableCell<ObservableList<Vergoeding>, Double> () {
                @Override
                public void updateItem(Double item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setStyle("");
                        setGraphic(null);
                    } else {
                        setText(Double.toString(round(item, 2)) + " €");
                        setStyle("");
                        setGraphic(null);
                    }
                }
                };
            
            return cell;
        });
        TableColumn<Vergoeding, Vergoeding> detailsCol = new TableColumn("Details");
        detailsCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        detailsCol.setCellFactory(param -> new TableCell<Vergoeding, Vergoeding>() {
            private final Button detailButton = new Button("Details");
            @Override
            protected void updateItem(Vergoeding verg, boolean empty) {
                super.updateItem(verg, empty);
                if (verg == null || !all) {
                    setGraphic(null);
                    return;
                }
                setGraphic(detailButton);
                detailButton.setOnAction(event -> {
                    all = Boolean.FALSE;
                    int vindex = observableVergoedingList.indexOf(verg);
                    ArrayList<Umpire> umpArray = new ArrayList<>();
                    umpArray.addAll(umpires);
                    berekenVergoedingen(umpArray, mainpanel.stringToLocalDate(periodFrom.getText()), mainpanel.stringToLocalDate(periodTo.getText()));
                    observableVergoedingList.clear();
                    observableVergoedingList.addAll(database.getUitbetalingenFromDatabase(umpArray.get(vindex).getUmpireLicentie()));
                    totaalAanUitbetalingen = 0.0;
                    observableVergoedingList.forEach(d -> {
                        totaalAanUitbetalingen += d.getTotaal();
                    });
                    totaalBedrag.setText(round(totaalAanUitbetalingen, 2) + " €");
                    leftBox.getChildren().remove(overzicht);
                    overzicht.getChildren().clear();
                    overzicht.getChildren().add(showTable(observableVergoedingList));
                    overzicht.getChildren().add(showDetailGrid(umpArray.get(vindex)));
                    leftBox.getChildren().add(overzicht);
                });
                
            }
        });
        
        
        vergTable.getColumns().addAll(umpireCol, aantalWCol, kmCol, kmEuroCol, gameEuroCol, totaalUCol, detailsCol);

        
        
        
        return vergTable;
        
        
    }
    
    private static double round(Double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();

    }
    
    private TextArea showDetailGrid(Umpire ump) {
        //GridPane grid = new GridPane();
        
        
        String s = ump.getUmpireVoornaam() + " " + ump.getUmpireNaam();
        s += "\n\n";
        Double kms = database.getUitbetalingenFromDatabase(ump.getUmpireLicentie()).getKilometers();
        s += "Totaal aantal kilometers: " + round(kms, 3);
        
        ArrayList<Pair> numGames = new ArrayList<>();
        numGames.addAll(numberOfGamesCalled(ump));
        for (Afdeling afd : afdelingen) {
            s += "\n" + String.format("%-" + 16 + "s", afd.getAfdelingsNaam());
            for (Pair p : numGames) {
                if ((Afdeling) p.getKey() == afd) {
                    Integer i = (Integer) p.getValue();
                    Double v = Double.parseDouble(database.getVergoedingenFromDatabase(afd.getAfdelingsNaam()));
                    s += String.format("%-2s", Integer.toString(i));
                    s += " x " + String.format("%-4s", Double.toString(v)) + " = " + Double.toString(i * v);
                }
            }
        }
        
        s += "\n\nVergoeding kilometers = " + round(database.getUitbetalingenFromDatabase(ump.getUmpireLicentie()).getKmeuro(), 2) + " €";
        s += "\nVergoeding wedstrijden = " + round(database.getUitbetalingenFromDatabase(ump.getUmpireLicentie()).getWedstrijdvergoeding(), 2) + " €";
        s += "\nTotaalbedrag vergoeding = " + round(database.getUitbetalingenFromDatabase(ump.getUmpireLicentie()).getTotaal(), 2) + " €";
                
        
        
        TextArea textarea = new TextArea(s);
        Font font = new Font(java.awt.Font.MONOSPACED, 14);
        textarea.setFont(font);
        textarea.setPrefHeight(600);
        textarea.setEditable(false);
        //grid.add(textarea, 0, 1);
        return textarea;
    }
    
    private ArrayList<Pair> numberOfGamesCalled(Umpire ump) {
        System.out.println("Afdelingen: " + afdelingen);
        System.out.println("Games: " + games);
        ArrayList<Pair> numberOfGames = new ArrayList<>();
        for (Afdeling afd : afdelingen) {
            Integer number = 0;
            for (Game game : games) {
                if (game.getAfdelingString().equals(afd.getAfdelingsNaam())) {
                    //System.out.println("Afdeling = " + game.getAfdelingString() + ", Plate umpire = " + game.getPlateUmpire().getUmpireLicentie());
                    if (game.getPlateUmpire().getUmpireLicentie().equals(ump.getUmpireLicentie()) || game.getBase1Umpire().getUmpireLicentie().equals(ump.getUmpireLicentie()) || game.getBase2Umpire().getUmpireLicentie().equals(ump.getUmpireLicentie()) || game.getBase3Umpire().getUmpireLicentie().equals(ump.getUmpireLicentie())) {
                        number += 1;
                        System.out.println("Game found: " + number);
                    }
                }
            }
            Pair p = new Pair(afd, number);
            numberOfGames.add(p);
        }
        System.out.println("numberOfGames = " + numberOfGames);
        return numberOfGames;
    }
}
