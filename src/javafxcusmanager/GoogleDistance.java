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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pieter Stragier <pstragier@gmail.be>
 */
public class GoogleDistance {
    
    private String BingMapsAPIKey = "AniQzYSLC5sy_lUsvXN39KXycmYp-Yo4uft7C9jp67W4Mt-CHKWuCOiUsoyKiePd"; 
    public GoogleDistance() {
        
    }
    
    /** Calculate single distance between umpire home and club address
     * 
     * @param umpire
     * @param club
     * @return String (of distance in km)
     */
    public String calculateDistance() throws IOException {
        String totalDistance = "";
        
        try {
            //URL url = new URL("http://dev.virtualearth.net/REST/v1/Routes?wayPoint.1={wayPpoint1}&viaWaypoint.2={viaWaypoint2}&waypoint.3={waypoint3}&wayPoint.n={waypointN}&heading={heading}&optimize={optimize}&avoid={avoid}&distanceBeforeFirstTurn={distanceBeforeFirstTurn}&routeAttributes={routeAttributes}&timeType={timeType}&dateTime={dateTime}&maxSolutions={maxSolutions}&tolerances={tolerances}&distanceUnit={distanceUnit}&key={'" + BingMapsKey + "'}");
            //URL url = new URL("https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins={lat0,long0;lat1,lon1;latM,lonM}&destinations={lat0,lon0;lat1,lon1;latN,longN}&travelMode={travelMode}&startTime={startTime}&timeUnit={timeUnit}&key={'"+BingMapsAPIKey+"'}");
            URL url = new URL("https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=47.6044,-122.3345;47.6731,-122.1185;47.6149,-122.1936&destinations=45.5347,-122.6231;47.4747,-122.2057&travelMode=driving&key={'"+BingMapsAPIKey+"'}");

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
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(GoogleDistance.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return totalDistance;
    }
    
    public class FullResponseBuilder {
        public String getFullResponse(HttpURLConnection con) throws IOException {
            StringBuilder fullResponseBuilder = new StringBuilder();

            // read status and message

            // read headers

            // read response content

            return fullResponseBuilder.toString();
        }
    }
}
