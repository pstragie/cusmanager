/*
 * Restricted License.
 * No dispersal allowed.
 */
package javafxcusmanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private ObservableList<Vergoeding> vergoedingen;
    private ObservableList<Game> games;
    private ObservableList<Afdeling> afdelingen;
    private Label umpLabel, periodfrom, periodto, periode;
    private ComboBox umpCombo;
    private TextField periodFrom, periodTo;
    private Button berekenUmpire, exporteer, sluiten;
    
    public CalculateVergoedingen(ObservableList<Umpire> umpires, ObservableList<Club> clubs, ObservableList<Vergoeding> vergoedingen, ObservableList<Game> games, ObservableList<Afdeling> afdelingen) {
        this.umpires = umpires;
        this.clubs = clubs;
        this.vergoedingen = vergoedingen;
        this.games = games;
        this.afdelingen = afdelingen;
        mainpanel = new MainPanel();
        database = new Database();
        pref = Preferences.userNodeForPackage(AppSettings.class);

    }
    
    /** Calculate vergoedingen
     * 
     * @return 
     */
    public HBox calculateVergoedingen() {
        HBox mainHBox = new HBox(5);
        
        // Left options
        VBox leftBox = new VBox(5);
        GridPane grid = new GridPane();
        umpLabel = new Label("Umpire");
        periode = new Label("Periode");
        periodfrom = new Label("Van: ");
        periodto = new Label("Tot: ");
        periodFrom = new TextField("10/03/2018");
        periodTo = new TextField("30/04/2018");
        umpCombo = new ComboBox(umpires);
        
        grid.add(umpLabel, 0, 1);
        grid.add(umpCombo, 1, 1);
        grid.add(periode, 0, 2);
        grid.add(periodfrom, 0, 3);
        grid.add(periodFrom, 1, 3);
        grid.add(periodto, 0, 4);
        grid.add(periodTo, 1, 4);
        
        leftBox.getChildren().add(grid);
        
        HBox buttonBox = new HBox(5);
        berekenUmpire = new Button("Bereken");
        exporteer = new Button("Exporteer");
        sluiten = new Button("Sluiten");
        
        berekenUmpire.setOnAction(bereken -> {
            toonBerekeningen((Umpire) umpCombo.getSelectionModel().getSelectedItem(), mainpanel.stringToLocalDate(periodFrom.getText()), mainpanel.stringToLocalDate(periodTo.getText()));
        });
        sluiten.setOnAction(sluit -> {
           Stage stage = (Stage) sluiten.getScene().getWindow();
           stage.close();
        });
        exporteer.setOnAction(export -> {
            
        });
        
        buttonBox.getChildren().addAll(berekenUmpire, exporteer, sluiten);
        leftBox.getChildren().add(buttonBox);
        mainHBox.getChildren().add(leftBox);
        
        // Right calculations
        VBox rightBox = new VBox();
        rightBox.getChildren().add(toonBerekeningen((Umpire) umpCombo.getSelectionModel().getSelectedItem(), mainpanel.stringToLocalDate(periodFrom.getText()), mainpanel.stringToLocalDate(periodTo.getText())));
        mainHBox.getChildren().add(rightBox);
        
        return mainHBox;
    }
    
    private VBox toonBerekeningen(Umpire ump, LocalDate dfrom, LocalDate dto) {
        VBox vboxVergoedingen = new VBox();
        Double kmOne = 0.0; // kilometers
        Double kmAll = 0.0;
        Double kmVergoedingOne = 0.0; // Berekening vergoeding km
        Double kmVergoedingAll = 0.0;
        Double gameVergoedingOne = 0.0;
        Double gameVergoedingAll = 0.0;
        Double totaalOne = 0.0;
        Double totaalAll = 0.0;
        Double kmvergoeding = Double.parseDouble((database.getVergoedingFromDatabase("km")).replace(",", ".")); // Bedrag per km
        Map<String, String> afdelingsVergoeding = new HashMap<>();
        ArrayList<Umpire> umpArray = new ArrayList<>();
        // Fill dictionary (map)
        for (Afdeling afd : afdelingen) {
            String verg = database.getVergoedingFromDatabase(afd.getAfdelingsNaam());
            afdelingsVergoeding.put(afd.getAfdelingsNaam(), verg);
        }
        if (umpCombo.getSelectionModel().getSelectedItem() == null) {
            umpArray.addAll(umpires);
        } else {
            umpArray.add(ump);
        }
        
        for (Umpire u : umpArray) {
            kmOne = 0.0;
            gameVergoedingOne = 0.0;
            for (Game g : games) {
                System.out.println("Club = " + g.getHomeClub());
                System.out.println("umpire name = " + g.getPlateUmpireName());
                if (g.getPlateUmpireName().equals(u.getUmpireNaam() + " " + u.getUmpireVoornaam())) {
                    String clubnr = g.getHomeClub().getClubNummer();
                    Double d = Double.parseDouble(database.getDistFromUmpireClub(u.getUmpireLicentie(), clubnr));
                    System.out.println("clubnummer = " + clubnr + ", distance = " + d);
                    kmOne += d;
                    kmAll += d;
                    gameVergoedingOne += Double.parseDouble(afdelingsVergoeding.get(g.getAfdelingString()));
                    gameVergoedingAll += Double.parseDouble(afdelingsVergoeding.get(g.getAfdelingString()));
                }
            }
            System.out.println("Aantal kilometers "+ u.getUmpireVoornaam() + " = " + kmOne * 2 + " km");
            kmVergoedingOne = kmOne * 2 * kmvergoeding;
            System.out.println("Kilometervergoeding " + u.getUmpireVoornaam() + " = " + kmOne * 2 * kmvergoeding + " €");
            System.out.println("Vergoeding volgens afdeling " + u.getUmpireVoornaam() + " = " + gameVergoedingOne + "€");
            totaalOne = kmVergoedingOne + gameVergoedingOne;
            System.out.println("Totaalbedrag 1 umpire " + u.getUmpireVoornaam() + " = " + totaalOne);
        }
        System.out.println("Aantal kilometers alle umpires = " + kmAll * 2);
        kmVergoedingAll = kmAll * 2 * kmVergoedingAll;
        System.out.println("Kilometervergoeding alle umpires = " + kmAll * 2 * kmvergoeding + " €");
        System.out.println("Afdelingvergoeding alle umpires = " + gameVergoedingAll + " €");
        totaalAll = kmVergoedingAll + gameVergoedingAll;
        System.out.println("Totaalbedrag alle umpires = " + totaalAll);
        
        return vboxVergoedingen;
    }
}
