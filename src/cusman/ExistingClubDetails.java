/*
 * Restricted License.
 * No dispersal allowed.
 */
package cusman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author Pieter Stragier <pstragier@gmail.be>
 */
public class ExistingClubDetails {
    
        /**
	 * @param args
	 */
	
	

    
	
        /* Paneel met invoervakken voor nieuwe club */
	// Attributen
	private boolean confirmed = false;
        private ObservableList<String> afdelingen;
        private ObservableList<Club> clubs;
        private ApiLocationDistance apiLocationdistance;
	public TextField clubnaamtf, ligatf, websitetf, clubnummertf, clubemailtf, clubtelefoontf, voorzittertf, straattf, huisnummertf, postcodetf, stadtf, lattf, lontf;
	private Label clubLabel,  ligaLabel, websiteLabel, clubnummerLabel, clubemailLabel, clubtelefoonLabel, voorzitterLabel, straatLabel, huisnrLabel, pcLabel, stadLabel, latLabel, lonLabel, landLabel;
	private Button opslaan, annuleren, locatieBepalen;
        private ComboBox landcodeComboBox;
        private Database database;
        // Constructor
        public ExistingClubDetails(ObservableList clubs) {
            this.clubs = clubs;
            database = new Database();
            apiLocationdistance = new ApiLocationDistance();
        }

        private ObservableList getLandcodes() {
            database = new Database();
            ObservableList landcodes = FXCollections.observableArrayList(database.getLandcodesFromDatabase());
            
            return landcodes;
        }
        
	// Constructor
	public Pane clubPanel(Club club) {
		// Schakel lay-outmanager uit
		GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER_LEFT);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(10, 10, 10, 10));

		// Maak de tekstvakken
                
		clubnaamtf = new TextField(club.getClubNaam());
                clubnaamtf.setAlignment(Pos.CENTER_LEFT);
                clubnaamtf.setMinWidth(250.0);
                clubnummertf = new TextField(club.getClubNummer());
                clubnummertf.setAlignment(Pos.CENTER_LEFT);
                clubnummertf.setEditable(false);
                clubnummertf.setDisable(true);
		voorzittertf = new TextField (club.getVoorzitter());
		voorzittertf.setAlignment(Pos.CENTER_LEFT);
                clubemailtf = new TextField(club.getClubEmail());
                clubemailtf.setAlignment(Pos.CENTER_LEFT);
                clubtelefoontf = new TextField(club.getClubTelefoon());
                clubtelefoontf.setAlignment(Pos.CENTER_LEFT);
		straattf = new TextField (club.getClubStraat());
		straattf.setAlignment(Pos.CENTER_LEFT);
		huisnummertf = new TextField (club.getClubStraatNummer());
		huisnummertf.setAlignment(Pos.CENTER_LEFT);
		postcodetf = new TextField (club.getClubPostcode());
		postcodetf.setAlignment(Pos.CENTER_LEFT);
		stadtf = new TextField (club.getClubStad());
		stadtf.setAlignment(Pos.CENTER_LEFT);
                landcodeComboBox = new ComboBox(getLandcodes());
                landcodeComboBox.getSelectionModel().select(club.getLandCode());
		ligatf = new TextField (club.getLiga());
                ligatf.setAlignment(Pos.CENTER_LEFT);
                websitetf = new TextField (club.getClubWebsite());
                websitetf.setAlignment(Pos.CENTER_LEFT);
                lattf = new TextField (database.getLatitudeFromClubDatabase(club.getClubNummer()));
                lattf.setDisable(Boolean.TRUE);
                lontf = new TextField (database.getLongitudeFromClubDatabase(club.getClubNummer()));
                lontf.setDisable(true);
		// Maak de labels
		clubLabel = new Label( "Clubnaam" );
                clubLabel.setMinWidth(100);
		clubnummerLabel = new Label( "Club nummer" );
		voorzitterLabel = new Label( "Voorzitter" );
                clubemailLabel = new Label( "Email" );
                clubtelefoonLabel = new Label( "Telefoon" );
		straatLabel = new Label( "Straat" );
		huisnrLabel = new Label( "Huisnummer" );
		pcLabel = new Label( "Postcode" );
		stadLabel = new Label( "Stad" );
                landLabel = new Label ("Landcode");
		ligaLabel = new Label ("Liga");
                websiteLabel = new Label ("Website");
                latLabel = new Label ("Latitude");
                lonLabel = new Label ("Longitude");
                
		opslaan = new Button( "Opslaan" );
                opslaan.setPrefWidth(100);
		annuleren = new Button( "Annuleren" );
                annuleren.setPrefWidth(100);
                locatieBepalen = new Button("Locatie bepalen");
		locatieBepalen.setPrefWidth(150);
                HBox horButtonBox = new HBox(5);
                horButtonBox.getChildren().addAll(locatieBepalen, opslaan, annuleren);
		opslaan.setOnAction((ActionEvent event) -> {
                    confirmed = true;
                    ArrayList<Team> emptyArray = new ArrayList<>();
                    System.out.println(club);
                    int clindex = clubs.indexOf(club);
                    clubs.get(clindex).setClubNaam(clubnaamtf.getText());
                    clubs.get(clindex).setLiga(ligatf.getText());
                    clubs.get(clindex).setVoorzitter(voorzittertf.getText());
                    clubs.get(clindex).setClubStraat(straattf.getText());
                    clubs.get(clindex).setClubStraatNummer(huisnummertf.getText());
                    clubs.get(clindex).setClubPostcode(postcodetf.getText());
                    clubs.get(clindex).setClubStad(stadtf.getText());
                    clubs.get(clindex).setLandCode(landcodeComboBox.getSelectionModel().getSelectedItem().toString());
                    clubs.get(clindex).setClubEmail(clubemailtf.getText());
                    clubs.get(clindex).setClubTelefoon(clubtelefoontf.getText());
                    clubs.get(clindex).setClubWebsite(websitetf.getText());
                    Club c = clubs.get(clindex);
                    database.updateClubToDatabase(c.getClubNaam(), c.getVoorzitter(), c.getClubNummer(), c.getClubStraat(), c.getClubStraatNummer(), c.getClubPostcode(), c.getClubStad(), c.getLandCode(), c.getClubEmail(), c.getClubTelefoon(), c.getLiga(), c.getClubWebsite(), c.getVisible());
                    // Close window
                    Stage stage = (Stage) opslaan.getScene().getWindow();
                    stage.close();
                    
                });
		annuleren.setOnAction((ActionEvent event) -> {
                    // Close window
                    Stage stage = (Stage) annuleren.getScene().getWindow();
                    stage.close();
		});
	
                locatieBepalen.setOnAction((loc) -> {
                    Pair p = new Pair(0.0, 0.0);
                    try {
                        p = apiLocationdistance.getLocationClub(club);
                    } catch (IOException ex) {
                        Logger.getLogger(ExistingClubDetails.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        String latitude = Double.toString((Double) p.getKey());
                        String longitude = Double.toString((Double) p.getValue());
                        database.updateClubLocationInDatabase(club.getClubNummer(), latitude, longitude);
                        int cindex = clubs.indexOf(club);
                        clubs.get(cindex).setLatitude(latitude);
                        clubs.get(cindex).setLongitude(longitude);
                        lattf.setText(latitude);
                        lontf.setText(longitude);
                    }
                });
		// Bepaal van alle componenten de plaats en afmeting
                
		grid.add(clubLabel, 0, 1 );
		grid.add(clubnaamtf, 1, 1 );
		grid.add(clubnummerLabel, 0, 2 );
		grid.add(clubnummertf, 1, 2 );
		grid.add(voorzitterLabel, 0, 3 );
		grid.add(voorzittertf, 1, 3 );
                grid.add(clubemailLabel, 0, 4);
                grid.add(clubemailtf, 1, 4);
                grid.add(clubtelefoonLabel, 0, 5);
                grid.add(clubtelefoontf, 1, 5);
		grid.add(straatLabel, 0, 6 );
		grid.add(straattf, 1, 6 );
		grid.add(huisnrLabel, 0, 7 );
		grid.add(huisnummertf, 1, 7 );
		grid.add(pcLabel, 0, 8 );
		grid.add(postcodetf, 1, 8 );
		grid.add(stadLabel, 0, 9 );
		grid.add(stadtf, 1, 9 );
                grid.add(landLabel, 0, 10);
                grid.add(landcodeComboBox, 1, 10);
                grid.add(ligaLabel, 0, 11);
                grid.add(ligatf, 1, 11);
                grid.add(websiteLabel, 0, 12);
                grid.add(websitetf, 1, 12);
                grid.add(latLabel, 0, 13);
                grid.add(lattf, 1, 13);
                grid.add(lonLabel, 0, 14);
                grid.add(lontf, 1, 14);
                grid.add(horButtonBox, 0, 16, 2, 1);
                //grid.add(locatieBepalen, 0, 15);
		//grid.add(opslaan, 1, 15 );
		//grid.add(annuleren, 2, 15 );
		
		return grid;
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
	    this.confirmed = confirmed;
	}

	public String getInputFieldText() {
	    return clubnaamtf.getText();
	}
}
