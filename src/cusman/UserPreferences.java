/*
 * Restricted License.
 * No dispersal allowed.
 */
package cusman;

/**
 *
 * @author Pieter Stragier <pstragier@gmail.be>
 */
import java.util.prefs.Preferences;

public class UserPreferences {
  private Preferences prefs;

  public void setPreference() {
    // This will define a node in which the preferences can be stored
    prefs = Preferences.userRoot().node(this.getClass().getName());
    String ID1 = "Seizoen";
    String ID2 = "StartOfYear";

    // First we will get the values
    // Define a boolean value
    System.out.println(prefs.get(ID1, "2018"));
    // Define a string with default "Hello World
    System.out.println(prefs.get(ID2, "15"));

    // now set the values
    prefs.put(ID1, "2018");
    prefs.put(ID2, "15");

    // Delete the preference settings for the first value
    prefs.remove(ID1);

  }

  public static void main(String[] args) {
    UserPreferences test = new UserPreferences();
    test.setPreference();
  }
}