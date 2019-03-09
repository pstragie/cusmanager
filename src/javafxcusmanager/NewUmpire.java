/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

/** Shows the umpire details
 * @author Pieter Stragier
 * @author www.pws-solutions.be
 * @version 1.0
 * @since 1.0
 */
import java.util.ArrayList;
import java.util.Comparator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author pieter
 */
public class NewUmpire {
    /** Creates a pane with the fields for umpire details
	 * Paneel met invoervakken voor nieuwe club
	 */
    public NewUmpire() {
        System.out.println("NewUmpire class called");
    }
    // Attributen
    private boolean confirmed = false;
    public TextField voornaamtf, familienaamtf, licentietf, clubtf, straattf, huisnummertf, postcodetf, stadtf, afdelingentf, telefoontf, emailtf;
    private Label vnaamLabel, fnaamLabel, licentieLabel, clubLabel, straatLabel, huisnrLabel, pcLabel, stadLabel, afdelingenLabel, telefoonLabel, emailLabel, actiefLabel;
    
    public CheckBox actiefCheckbox;
    private final ListView afdelingListview = new ListView();
    private ObservableList<Club> clubs;
    private Database database;
    private ObservableList<Afdeling> afdelingen;
    public ObservableList<Afdeling> afdelingenArray;
    public ComboBox<Club> clubComboBox;
    private Boolean nieuw;
    private ObservableList<Umpire> umpireList;
    /** Creates a vertical box with textfields and buttons
     * 
     * @param umpire Selected Umpire
     * @param afdelingen All afdelingen
     * @param nieuw Boolean nieuwe umpire
     * @param clubs All clubs
     * @param umpires All umpires
     * @return A Vertical Box with umpire details textfields and buttons.
     */
    public VBox NewUmpire(Umpire umpire, ObservableList<Afdeling> afdelingen, Boolean nieuw, ObservableList<Club> clubs, ObservableList<Umpire> umpires) {
	    this.afdelingen = afdelingen;
            this.clubs = clubs;
            this.nieuw = nieuw;
            System.out.println("Umpire: " + umpire);
            database = new Database();
            umpireList = FXCollections.observableArrayList(database.getUmpireFromDatabase(umpire.getUmpireNaam()));
            Comparator<Umpire> umpireComparator = Comparator.comparing(Umpire::getUmpireNaam);
            
            umpireList.sort(umpireComparator);
            umpireList.addListener(new ListChangeListener<Umpire>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Umpire> change) {
                    while(change.next()) {
                        if(change.wasUpdated()) {
                            
                        } else
                            if (change.wasPermutated()) {
                                
                            }
                            else
                                change.getRemoved().forEach((remitem) -> {
                                    System.out.println("umpire remitem: " + remitem);
                                });
                        change.getAddedSubList().forEach((additem) -> {
                            System.out.println("umpire additem: " + additem);
                        });
                    }
                }
            });
            
            afdelingenArray = FXCollections.observableArrayList(umpire.getUmpireAfdelingen());
            
            afdelingenArray.addListener(new ListChangeListener<Afdeling>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Afdeling> change) {
                    while(change.next()) {
                        if(change.wasUpdated()) {
                            
                        } else
                            if (change.wasPermutated()) {
                                
                            }
                            else
                                change.getRemoved().forEach((remitem) -> {
                                    System.out.println("afdeling umpire remitem: " + remitem);
                                });
                        change.getAddedSubList().forEach((additem) -> {
                            System.out.println("afdeling umpire additem: " + additem);
                        });
                    }
                }
            });
            VBox verbox = new VBox(5);
            Label detailLabel = new Label("Details");
            detailLabel.setPadding(new Insets(0, 0, 0, 5));
            detailLabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
            verbox.setPadding(new Insets(0, 0, 0, 5));
            verbox.getChildren().add(detailLabel);
            
            // Schakel lay-outmanager uit
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER_LEFT);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));

		// Maak de tekstvakken
                
		voornaamtf = new TextField( );
                voornaamtf.setAlignment(Pos.CENTER_LEFT);
                voornaamtf.setMinWidth(60.0);
		familienaamtf = new TextField ( );
		familienaamtf.setAlignment(Pos.CENTER_LEFT);
                licentietf = new TextField();
                licentietf.setAlignment(Pos.CENTER_LEFT);
		
                
		straattf = new TextField ( );
		straattf.setAlignment(Pos.CENTER_LEFT);
		huisnummertf = new TextField ( );
		huisnummertf.setAlignment(Pos.CENTER_LEFT);
		postcodetf = new TextField ( );
		postcodetf.setAlignment(Pos.CENTER_LEFT);
		stadtf = new TextField ( );
		stadtf.setAlignment(Pos.CENTER_LEFT);
		telefoontf = new TextField();
                telefoontf.setAlignment(Pos.CENTER_LEFT);
                emailtf = new TextField();
                emailtf.setAlignment(Pos.CENTER_LEFT);
                actiefCheckbox = new CheckBox();
                if (!nieuw) {
                    System.out.println("umpireList: " + umpireList);
                    System.out.println("huisnummer: " + umpireList.get(0).getUmpireHuisnummer());
                    voornaamtf.setText(umpireList.get(0).getUmpireVoornaam());
                    familienaamtf.setText(umpireList.get(0).getUmpireNaam());
                    straattf.setText(umpireList.get(0).getUmpireStraat());
                    huisnummertf.setText(umpireList.get(0).getUmpireHuisnummer());
                    postcodetf.setText(umpireList.get(0).getUmpirePostcode());
                    stadtf.setText(umpireList.get(0).getUmpireStad());
                    licentietf.setText(umpireList.get(0).getUmpireLicentie());
                    licentietf.setDisable(true);
                    telefoontf.setText(umpireList.get(0).getUmpireTelefoon());
                    emailtf.setText(umpireList.get(0).getUmpireEmail());
                    actiefCheckbox.setSelected(umpireList.get(0).getActief());
                    afdelingenArray.setAll(umpireList.get(0).getUmpireAfdelingen());
                    // Combobox
                    clubComboBox = new ComboBox();
                        // List with afdelingen

                        ObservableList<Club> data = FXCollections.observableArrayList(clubs);

                        clubComboBox.getItems().addAll(data);
                        Club clnaam = umpireList.get(0).getUmpireClub();
                        clubComboBox.setValue(clnaam);

                } else {
                    voornaamtf.setText("");
                    familienaamtf.setText("");
                    licentietf.setDisable(false);
                    clubComboBox.setValue(null);
                    straattf.setText("");
                    huisnummertf.setText("");
                    postcodetf.setText("");
                    stadtf.setText("");
                    licentietf.setText("");
                    telefoontf.setText("");
                    emailtf.setText("");
                    actiefCheckbox.setSelected(true);
                    afdelingenArray.clear();
                }
                
		// Maak de labels
		vnaamLabel = new Label( "Voornaam" );
		fnaamLabel = new Label( "Familienaam" );
                telefoonLabel = new Label("Telefoon");
                emailLabel = new Label("Email");
                afdelingenLabel = new Label( "Afdelingen" );
		clubLabel = new Label( "Club" );
		straatLabel = new Label( "Straat" );
		huisnrLabel = new Label( "Huisnummer" );
		pcLabel = new Label( "Postcode" );
		stadLabel = new Label( "Stad" );
		licentieLabel = new Label( "Licentie" );
                actiefLabel = new Label("Actief");
		
                
		
                
		
		
	
                ObservableList<Afdeling> afddata = FXCollections.observableArrayList(afdelingen);
                
                afdelingListview.setItems(afddata);
                //afdelingListview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                
                
                afdelingListview.setCellFactory(CheckBoxListCell.forListView((Afdeling item) -> {
                    BooleanProperty observable = new SimpleBooleanProperty();
                    //System.out.println("umpire afdelingen: " + umpire.getUmpireAfdelingen());
                    
                    ArrayList<String> tmpafd = new ArrayList<>();
                    afdelingenArray.forEach(a -> {
                        tmpafd.add(a.getAfdelingsNaam());
                    });
                    Boolean present = tmpafd.contains(item.getAfdelingsNaam());
                    if (present) {
                        observable.set(Boolean.TRUE);
                    } else {
                        observable.set(Boolean.FALSE);
                    }
                    
                    
                    observable.addListener((obs, wasSelected, isNowSelected) -> {
                        System.out.println("Check box for "+item+" changed from "+wasSelected+" to "+isNowSelected);
                        if (wasSelected) {
                            System.out.println("wasSelected: " + afdelingenArray);
                            System.out.println("index: " + afdelingenArray.indexOf(item));
                            
                            afdelingenArray.removeIf(t -> t.getAfdelingsNaam().equals(item.getAfdelingsNaam()));
                            System.out.println("after remove: " + afdelingenArray);
                        } 
                        if (isNowSelected) {
                            System.out.println("isNowSelected");
                            afdelingenArray.add(item);
                        }
                        
                    });
                    return observable ;
            }));
                
                
                
                
                
		grid.add(vnaamLabel, 0, 1 );
		grid.add(voornaamtf, 1, 1 );
		grid.add(fnaamLabel, 0, 2 );
		grid.add(familienaamtf, 1, 2 );
                grid.add(licentieLabel, 0, 3);
                grid.add(licentietf, 1, 3);
		grid.add(clubLabel, 0, 4 );
		grid.add(clubComboBox, 1, 4 );
		grid.add(straatLabel, 0, 5 );
		grid.add(straattf, 1, 5 );
		grid.add(huisnrLabel, 0, 6 );
		grid.add(huisnummertf, 1, 6 );
		grid.add(pcLabel, 0, 7 );
		grid.add(postcodetf, 1, 7 );
		grid.add(stadLabel, 0, 8 );
		grid.add(stadtf, 1, 8 );
                grid.add(telefoonLabel, 0, 9);
                grid.add(telefoontf, 1, 9);
                grid.add(emailLabel, 0, 10);
                grid.add(emailtf, 1, 10);
		grid.add(actiefLabel, 0, 11);
                grid.add(actiefCheckbox, 1, 11);
                grid.add(afdelingenLabel, 2, 0 , 1, 1);
                grid.add(afdelingListview, 2, 1 , 1, afdelingen.size());
		
            verbox.getChildren().add(grid);
            return verbox;
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
	    this.confirmed = confirmed;
	}

	public String getInputFieldText() {
	    return voornaamtf.getText();
	}
        
        public String getVoornaam() {
            return voornaamtf.getText();
        }
        public Boolean getActiefCheckBoxValue() {
            return actiefCheckbox.selectedProperty().get();
        }
        public Club getComboBoxValue() {
            return clubComboBox.getValue();
        }
        public String getLicentieValue() {
            return licentietf.getText();
        }
        public Umpire getCurrentUmpire() {
            return umpireList.get(0);
        }
    }

