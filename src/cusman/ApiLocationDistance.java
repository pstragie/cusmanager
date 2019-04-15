/*
 * Restricted License.
 * No dispersal allowed.
 */
package cusman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.json.*;
/**
 *
 * @author Pieter Stragier <pstragier@gmail.be>
 */
public class ApiLocationDistance {
    
    private String BingMapsKey = "AniQzYSLC5sy_lUsvXN39KXycmYp-Yo4uft7C9jp67W4Mt-CHKWuCOiUsoyKiePd"; 
    private String BingMapsAPIKey = "LwvKKpyMS0B0X3uTAp0Y~q47SlJVuQ98QE6zlT_3gUA~AknQEc5Uo1JkymduJ8gUGcWDwPb5ZBifrtZM_R4-Q4ieHb__2JdktKpc6npU0WhZ";
    private Database database = new Database();
    public ApiLocationDistance() {
        
    }
    
    /** Calculate single distance between umpire home and club address
     * 
     * @param ump
     * @param clubs
     * @return
     * @throws IOException 
     */
    public String calculateDistance(Umpire ump, ArrayList<Club> clubs) throws IOException {
        
        Pair umpLatLong = new Pair<>("", "");
        Pair clubLatLong = new Pair<>("", "");
        ArrayList<Pair> clubLatLongArray = new ArrayList<>();
        String lat = database.getLatitudeFromUmpireDatabase(ump.getUmpireLicentie());
        String lon = database.getLongitudeFromUmpireDatabase(ump.getUmpireLicentie());
        if (lat == null || lon == null || lat.equals(Double.toString(0.0)) || lon.equals(Double.toString(0.0))) {
            System.out.println("lat, lon not present");
            umpLatLong = getLocationUmpire(ump);
            database.updateUmpireLocationInDatabase(ump.getUmpireLicentie(), Double.toString((Double) umpLatLong.getKey()), Double.toString((Double) umpLatLong.getValue()));
        } else {
            umpLatLong = new Pair(lat, lon);
        }
        for (Club c : clubs) {
            String clat = database.getLatitudeFromClubDatabase(c.getClubNummer());
            String clon = database.getLongitudeFromClubDatabase(c.getClubNummer());
            Boolean force = Boolean.FALSE;
            if (clat == null || clon == null || clat.equals(Double.toString(0.0)) || clon.equals(Double.toString(0.0)) || Objects.equals(force, Boolean.TRUE)) {
                System.out.println(c.getClubNaam() + ": lat or lon club unknown or forced to recalculate location!");
                clubLatLong = getLocationClub(c);
                clubLatLongArray.add(clubLatLong);
                database.updateClubLocationInDatabase(c.getClubNummer(), Double.toString((Double) clubLatLong.getKey()), Double.toString((Double) clubLatLong.getValue()));
            } else {
                System.out.println(c.getClubNaam() + ": lat and lon club in database.");
                clubLatLongArray.add(new Pair(clat, clon));
            }
            
        }
        System.out.println("clubLatLongArray = " + clubLatLongArray);
        String latUmp = (String) umpLatLong.getKey();
        String longUmp = (String) umpLatLong.getValue();
        System.out.println("latUmp = " + latUmp + ", longUmp = " + longUmp);
        String totalDistance = "";
        
        String originString = latUmp + "," + longUmp;
        System.out.println("Origin String = " + originString);
        String destinationsString = "";
            
        ArrayList<String> destinationsArray = new ArrayList<>();
        for (Pair p : clubLatLongArray) {
            String clat = (String) p.getKey();
            String clon = (String) p.getValue();
            destinationsString = String.join(",", clat, clon);
            destinationsArray.add(destinationsString);
        }
        
        String destinationsLatLon = String.join(";", destinationsArray);
        System.out.println("Destinations String = " + destinationsLatLon);
        
        try {
            //URL url = new URL("http://dev.virtualearth.net/REST/v1/Routes?wayPoint.1={wayPpoint1}&viaWaypoint.2={viaWaypoint2}&waypoint.3={waypoint3}&wayPoint.n={waypointN}&heading={heading}&optimize={optimize}&avoid={avoid}&distanceBeforeFirstTurn={distanceBeforeFirstTurn}&routeAttributes={routeAttributes}&timeType={timeType}&dateTime={dateTime}&maxSolutions={maxSolutions}&tolerances={tolerances}&distanceUnit={distanceUnit}&key={'" + BingMapsKey + "'}");
            //URL url = new URL("https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins={lat0,long0;lat1,lon1;latM,lonM}&destinations={lat0,lon0;lat1,lon1;latN,longN}&travelMode={travelMode}&startTime={startTime}&timeUnit={timeUnit}&key={'"+BingMapsAPIKey+"'}");
            URL url = new URL("https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins="+originString+"&destinations="+destinationsLatLon+"&travelMode=driving&key=LwvKKpyMS0B0X3uTAp0Y~q47SlJVuQ98QE6zlT_3gUA~AknQEc5Uo1JkymduJ8gUGcWDwPb5ZBifrtZM_R4-Q4ieHb__2JdktKpc6npU0WhZ");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Ronse,Belgium&destinations=Ghent,Belgium&key=YOUR_API_KEY
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
              new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println("inputLine = " + inputLine);
            System.out.println("content = " + content);
            String jsoncontent = content.toString();
            JSONObject obj = new JSONObject(jsoncontent);
            // read status and message
            String authResult = (String) obj.getString("authenticationResultCode");
            System.out.println("authentication = " + authResult);
                    
            // Get resourceSets
            JSONArray resourceSets = (JSONArray) obj.get("resourceSets"); 
            for (int i = 0; i < resourceSets.length(); i++) {
                JSONArray resources = resourceSets.getJSONObject(i).getJSONArray("resources");
                for (int j = 0; j < resources.length(); j++) {
                    JSONArray results = resources.getJSONObject(j).getJSONArray("results");
                    JSONArray origins = resources.getJSONObject(j).getJSONArray("origins");
                    JSONArray destinations = resources.getJSONObject(i).getJSONArray("destinations");
                    for (int k = 0; k < results.length(); k++) {
                        String distance = Float.toString(results.getJSONObject(k).getFloat("travelDistance"));
                        //String latitude = destinations.getJSONObject(k).getString("latitude");
                        String latitude = Float.toString(destinations.getJSONObject(k).getFloat("latitude"));
                        String longitude = Float.toString(destinations.getJSONObject(k).getFloat("longitude"));
                        System.out.println("club to look for: " + clubs.get(k));
                        System.out.println("Ump: " + ump.getUmpireLicentie()+ ", club: " + clubs.get(i).getClubNummer() + ", Latitude = " + latitude + ", Longitude = " + longitude + ", Distance: " + distance);

                        storeDistance(ump, clubs.get(k), distance);
                    }
                }
            }
            
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(ApiLocationDistance.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return totalDistance;
    }
    
    public Pair getLocationUmpire(Umpire ump) throws IOException {
        Pair umpPair = new Pair<>(0.0, 0.0);
        String straat = ump.getUmpireStraat();
        straat = straat.replace(" ", "_");
        String huisnummer = ump.getUmpireHuisnummer();
        String postcode = ump.getUmpirePostcode();
        String stad = ump.getUmpireStad();
        stad = stad.replace(" ", "_");
        String land = "Belgium";
        
        try {
            URL url = new URL("http://dev.virtualearth.net/REST/v1/Locations/BE/"+postcode+"/"+stad+"/"+straat+"?includeNeighborhood=0&include=ciso2&maxResults=1&key=LwvKKpyMS0B0X3uTAp0Y~q47SlJVuQ98QE6zlT_3gUA~AknQEc5Uo1JkymduJ8gUGcWDwPb5ZBifrtZM_R4-Q4ieHb__2JdktKpc6npU0WhZ");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
              new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println("inputLine = " + inputLine);
            System.out.println("content = " + content);
            String jsoncontent = content.toString();
            JSONObject obj = new JSONObject(jsoncontent);
            
            // read status and message
            String authResult = (String) obj.getString("authenticationResultCode");
            System.out.println("authentication = " + authResult);
              
            ArrayList<Double> latlongArray = new ArrayList<>();
            // Get resourceSets
            JSONArray resourceSets = (JSONArray) obj.get("resourceSets"); 
            for (int i = 0; i < resourceSets.length(); i++) {
                JSONArray resources = resourceSets.getJSONObject(i).getJSONArray("resources");
                for (int j = 0; j < resources.length(); j++) {
                    JSONObject point = resources.getJSONObject(i).getJSONObject("point");
                    JSONArray coord = point.getJSONArray("coordinates");
                    System.out.println("coord: " + coord);
                    for (int k = 0; k < coord.length(); k++) {
                        latlongArray.add(coord.getDouble(k));
                    }  
                        
                    
                }
            }
            umpPair = new Pair(latlongArray.get(0), latlongArray.get(1));
        } catch (MalformedURLException ex) {
            Logger.getLogger(ApiLocationDistance.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        return umpPair;
    }
        
    public Pair getLocationClub(Club club) throws IOException {
        System.out.println("Getting location for club {}.".format(club.getClubNaam()));
        Pair clubPair = new Pair<>(0.0, 0.0);
        String straat = club.getClubStraat();
        straat = straat.replace(" ", "_");
        String huisnummer = club.getClubStraatNummer();
        String postcode = club.getClubPostcode();
        String stad = club.getClubStad();
        String land = club.getLandCode();
        stad = stad.replace(" ", "_");
        try {
            URL url = new URL("http://dev.virtualearth.net/REST/v1/Locations/"+land+"/"+postcode+"/"+stad+"/"+straat+"?includeNeighborhood=1&include=ciso2&maxResults=1&key=LwvKKpyMS0B0X3uTAp0Y~q47SlJVuQ98QE6zlT_3gUA~AknQEc5Uo1JkymduJ8gUGcWDwPb5ZBifrtZM_R4-Q4ieHb__2JdktKpc6npU0WhZ");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
              new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println("inputLine = " + inputLine);
            System.out.println("content = " + content);
            String jsoncontent = content.toString();
            JSONObject obj = new JSONObject(jsoncontent);
            
            // read status and message
            String authResult = (String) obj.getString("authenticationResultCode");
            System.out.println("authentication = " + authResult);
              
            ArrayList<Double> latlongArray = new ArrayList<>();
            // Get resourceSets
            JSONArray resourceSets = (JSONArray) obj.get("resourceSets"); 
            for (int i = 0; i < resourceSets.length(); i++) {
                JSONArray resources = resourceSets.getJSONObject(i).getJSONArray("resources");
                for (int j = 0; j < resources.length(); j++) {
                    JSONObject point = resources.getJSONObject(i).getJSONObject("point");
                    JSONArray coord = point.getJSONArray("coordinates");
                    System.out.println("coord: " + coord);
                    for (int k = 0; k < coord.length(); k++) {
                        latlongArray.add(coord.getDouble(k));
                    }  
                        
                    
                }
            }
            clubPair = new Pair(latlongArray.get(0), latlongArray.get(1));
        } catch (MalformedURLException ex) {
            Logger.getLogger(ApiLocationDistance.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        return clubPair;
    }

    
    private void storeDistance(Umpire ump, Club club, String distance) {
        try {
            if (database.checkIfDistanceExists(ump, club)) {
                System.out.println("Distance for club/ump exists... update");
                database.updateDistanceToDatabase(ump, club, distance);
            } else {
                System.out.println("Distance for club/ump does not exist... insert");
                database.insertDistanceToDatabase(ump, club, distance);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ApiLocationDistance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public VBox showMap(Umpire ump, Club club, Double w, Double h) throws IOException {
        VBox mapBox = new VBox();
        ArrayList<String> pushpinArray = new ArrayList<>();
        String centerMap = "50.8503,4.3517";
        String umpPushpin = ump.getLatitude() + "," + ump.getLongitude();
        String clubPushpin = club.getLatitude() + "," + club.getLongitude();
        System.out.println("Width & Height = " + w + ", " + h);
        try {
            URL url = new URL("https://dev.virtualearth.net/REST/V1/Imagery/Map/Road/"+centerMap+"/8?mapSize=600,400&format=png&pushpin="+umpPushpin+";50;1&pushpin="+clubPushpin+";64;2&pushpin=51.73732,3.8573;64;3&key=LwvKKpyMS0B0X3uTAp0Y~q47SlJVuQ98QE6zlT_3gUA~AknQEc5Uo1JkymduJ8gUGcWDwPb5ZBifrtZM_R4-Q4ieHb__2JdktKpc6npU0WhZ");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            boolean backgroundLoading = true;
            // The image is being loaded in the background
            String urls = "https://dev.virtualearth.net/REST/V1/Imagery/Map/Road/"+centerMap+"/8?mapSize=600,400&format=png&pushpin="+umpPushpin+";50;1&pushpin="+clubPushpin+";64;2&pushpin=51.73732,3.8573;64;3&key=LwvKKpyMS0B0X3uTAp0Y~q47SlJVuQ98QE6zlT_3gUA~AknQEc5Uo1JkymduJ8gUGcWDwPb5ZBifrtZM_R4-Q4ieHb__2JdktKpc6npU0WhZ";
            Image image = new Image(urls, backgroundLoading);
            
            final ImageView imageview = new ImageView();
            imageview.setImage(image);
            
            mapBox.getChildren().add(imageview);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ApiLocationDistance.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return mapBox;
    }
}
