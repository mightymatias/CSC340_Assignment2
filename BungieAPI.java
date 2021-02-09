/*Last Update: 9 Feb 2021

  Methods that interact with Bungie.net's API.

  Contributing Author: Austin Matias
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import org.json.*;

public class BungieAPI {
    //The header name that needs to be passed along with the API key in any requests.
    private final String apiHeader = "X-API-KEY";
    //The API key for the application.
    private final String apiKey = "e388b48e17034a0d8edcff768b858fb5";

    public void getMembershipId(){
        String baseURL = "https://www.bungie.net/Platform/";
        String callAction = "Destiny2/SearchDestinyPlayer/";
        int membershipType = -1;
        String displayName = "MightyMatias";
        String urlString = baseURL + callAction  + membershipType + "/" + displayName;
        URL url;

        try{
            //Make the connection.
            url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty(this.apiHeader, this.apiKey);

            //Examine the response code.
            int status = con.getResponseCode();
            if (status != 200){
                System.out.println("Error: API request failed. Status: " + status);
            } else {
                //Parsing input stream into a text string
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null){
                    content.append(inputLine);
                }
                //Close the connections.
                in.close();
                con.disconnect();
                //Print out the complete JSON string.
                System.out.println("Output: " + content.toString());
                //Parse that object into a usable Java JSON object.
                JSONObject obj = new JSONObject(content.toString());
                //Print out membershipId
                String membershipId = obj.getJSONArray("Response").getJSONObject(0).getString("membershipId");
                System.out.println("Membership ID: " + membershipId);
            }

        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }
}
