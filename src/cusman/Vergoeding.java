/*
 * Restricted License.
 * No dispersal allowed.
 */
package cusman;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/** Object Vergoeding
 * 
 * @author Pieter Stragier
 */
public class Vergoeding {
    private final ObjectProperty<Umpire> umpire;
    private final SimpleDoubleProperty kilometers;
    private final SimpleDoubleProperty kmeuro;
    private final SimpleIntegerProperty aantalwedstrijden;
    private final SimpleDoubleProperty wedstrijdvergoeding;
    private final SimpleDoubleProperty totaal;
    private Boolean uitbetaald;
    
    /** Vergoeding
     * 
     * @param umpireU
     * @param kmDouble
     * @param kmEuroDouble
     * @param aantalwedstrijdenInteger
     * @param wedstrijdvergoedingDouble
     * @param totaalDouble
     * @param uitbetaaldBool 
     */
    public Vergoeding(Umpire umpireU, Double kmDouble, Double kmEuroDouble, Integer aantalwedstrijdenInteger, Double wedstrijdvergoedingDouble, Double totaalDouble, Boolean uitbetaaldBool) {
        this.umpire = new SimpleObjectProperty<>(this, "umpirelicentie", umpireU);
        this.kilometers = new SimpleDoubleProperty(kmDouble);
        this.kmeuro = new SimpleDoubleProperty(kmEuroDouble);
        this.aantalwedstrijden = new SimpleIntegerProperty(aantalwedstrijdenInteger);
        this.wedstrijdvergoeding = new SimpleDoubleProperty(wedstrijdvergoedingDouble);
        this.totaal = new SimpleDoubleProperty(totaalDouble);
        this.uitbetaald = new Boolean(uitbetaaldBool);
    }
    
    public Umpire getUmpire() {
        return umpire.get();
    }
    public void setUmpire(Umpire umpireU) {
        umpire.set(umpireU);
    }
    
    public Double getKilometers() {
        return kilometers.get();
    }
    public void setKilometers(Double kmDouble) {
        kilometers.set(kmDouble);
    }
    
    public Double getKmeuro() {
        return kmeuro.get();
    }
    public void setKmeuro(Double kmEuroDouble) {
        kmeuro.set(kmEuroDouble);
    }
    
    public Integer getAantalwedstrijden() {
        return aantalwedstrijden.get();
    }
    public void setAantalwedstrijden(Integer aantalwedstrijdenInteger) {
        aantalwedstrijden.set(aantalwedstrijdenInteger);
    }
    
    public Double getWedstrijdvergoeding() {
        return wedstrijdvergoeding.get();
    }
    public void setWedstrijdvergoeding(Double wedstrijdvergoedingDouble) {
        wedstrijdvergoeding.set(wedstrijdvergoedingDouble);
    }
    
    public Double getTotaal() {
        return totaal.get();
    }
    public void setTotaal(Double totaalDouble) {
        totaal.set(totaalDouble);
    }
    
    public Boolean getUitbetaald() {
        return uitbetaald.booleanValue();
    }
    public void setUitbetaald(Boolean uitbetaaldBool) {
        uitbetaald = uitbetaaldBool;
    }
    
    public ObjectProperty umpireProperty() {
        return umpire;
    }
    public DoubleProperty kilometersProperty() {
        return kilometers;
    }
    public DoubleProperty kmeuroProperty() {
        return kmeuro;
    }
    public IntegerProperty aantalwedstrijdenProperty() {
        return aantalwedstrijden;
    }
    public DoubleProperty wedstrijdvergoedingProperty() {
        return wedstrijdvergoeding;
    }
    public DoubleProperty totaalProperty() {
        return totaal;
    }
    
}


