/*
 * Restricted License.
 * No dispersal allowed.
 */
package cusman;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.derby.client.am.Decimal;

/**
 *
 * @author Pieter Stragier <pstragier@gmail.be>
 */
public class Vergoedingskosten {
    private final SimpleStringProperty afdeling;
    private final SimpleStringProperty euro;
    
    public Vergoedingskosten(String afdelingString, String euroDec) {
        this.afdeling = new SimpleStringProperty(afdelingString);
        this.euro = new SimpleStringProperty(euroDec);
    }
    
    public String getAfdeling() {
        return afdeling.get();
    }
    public void setAfdeling(String afdelingString) {
        afdeling.set(afdelingString);
    }
    public String getEuroDec() {
        return euro.get();
    }
    public void setEuroDec(String euroDec) {
        euro.set(euroDec);
    }
    
    public StringProperty afdelingProperty() {
        return afdeling;
    }
    public StringProperty euroProperty() {
        return euro;
    }
}
