/*
 * Restricted License.
 * No dispersal allowed.
 */
package javafxcusmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import org.json.*;
/**
 *
 * @author Pieter Stragier <pstragier@gmail.be>
 */
public class GoogleDistance {
    
    private String BingMapsKey = "AniQzYSLC5sy_lUsvXN39KXycmYp-Yo4uft7C9jp67W4Mt-CHKWuCOiUsoyKiePd"; 
    private String BingMapsAPIKey = "LwvKKpyMS0B0X3uTAp0Y~q47SlJVuQ98QE6zlT_3gUA~AknQEc5Uo1JkymduJ8gUGcWDwPb5ZBifrtZM_R4-Q4ieHb__2JdktKpc6npU0WhZ";
    private Database database = new Database();
    public GoogleDistance() {
        
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
        if (lat == null || lon == null) {
            System.out.println("lat, lon not present");
            umpLatLong = getLocationUmpire(ump);
            database.updateUmpireLocationInDatabase(ump.getUmpireLicentie(), Double.toString((Double) umpLatLong.getKey()), Double.toString((Double) umpLatLong.getValue()));
        } else {
            umpLatLong = new Pair(lon, lat);
        }
        clubs.forEach(c -> {
            clubLatLongArray.add(getLocationClub(c));
        });
        String latUmp = (String) umpLatLong.getKey();
        String longUmp = (String) umpLatLong.getValue();
        System.out.println("latUmp = " + latUmp + ", longUmp = " + longUmp);
        String totalDistance = "";
        
        
        
        
        try {
            //URL url = new URL("http://dev.virtualearth.net/REST/v1/Routes?wayPoint.1={wayPpoint1}&viaWaypoint.2={viaWaypoint2}&waypoint.3={waypoint3}&wayPoint.n={waypointN}&heading={heading}&optimize={optimize}&avoid={avoid}&distanceBeforeFirstTurn={distanceBeforeFirstTurn}&routeAttributes={routeAttributes}&timeType={timeType}&dateTime={dateTime}&maxSolutions={maxSolutions}&tolerances={tolerances}&distanceUnit={distanceUnit}&key={'" + BingMapsKey + "'}");
            //URL url = new URL("https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins={lat0,long0;lat1,lon1;latM,lonM}&destinations={lat0,lon0;lat1,lon1;latN,longN}&travelMode={travelMode}&startTime={startTime}&timeUnit={timeUnit}&key={'"+BingMapsAPIKey+"'}");
            URL url = new URL("https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=47.6044,-122.3345&destinations=45.5347,-122.6231;47.4747,-122.2057&travelMode=driving&key=LwvKKpyMS0B0X3uTAp0Y~q47SlJVuQ98QE6zlT_3gUA~AknQEc5Uo1JkymduJ8gUGcWDwPb5ZBifrtZM_R4-Q4ieHb__2JdktKpc6npU0WhZ");

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
                        System.out.println("Latitude = " + latitude + ", Longitude = " + longitude + ", Distance: " + distance);
                    }
                }
            }
            
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(GoogleDistance.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return totalDistance;
    }
    
    private Pair getLocationUmpire(Umpire ump) throws IOException {
        Pair umpPair = new Pair<>(0.0, 0.0);
        String straat = ump.getUmpireStraat();
        String huisnummer = ump.getUmpireHuisnummer();
        String postcode = ump.getUmpirePostcode();
        String stad = ump.getUmpireStad();
        String land = "Belgium";
        
        try {
            URL url = new URL("http://dev.virtualearth.net/REST/v1/Locations/BE/"+postcode+"/"+stad+"/"+straat+"?includeNeighborhood=True&include={includeValue}&maxResults=1&key=LwvKKpyMS0B0X3uTAp0Y~q47SlJVuQ98QE6zlT_3gUA~AknQEc5Uo1JkymduJ8gUGcWDwPb5ZBifrtZM_R4-Q4ieHb__2JdktKpc6npU0WhZ");
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
            Logger.getLogger(GoogleDistance.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        return umpPair;
    }
        
    private Pair getLocationClub(Club club) {
        Pair clubPair = new Pair<>(0.0, 0.0);
        
        return clubPair;
    }

    
    
    
}
