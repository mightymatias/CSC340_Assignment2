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

    //The API's base URL.
    private final String baseURL = "https://www.bungie.net/Platform/";

    public String getMembershipId(String _displayName){
        String callAction = "Destiny2/SearchDestinyPlayer/";
        //-1 is the integer used by Bungie's API to search all platforms
        int membershipType = -1;
        String urlString = this.baseURL + callAction  + membershipType + "/" + _displayName;

        StringBuilder content = connectAndReturnContents(urlString);
        //Print out the complete JSON string.
        System.out.println("Output: " + content.toString());
        try{
            //Parse that object into a usable Java JSON object.
            JSONObject obj = new JSONObject(content.toString());
            //Print out membershipId
            return obj.getJSONArray("Response").getJSONObject(0).getString("membershipId");
        } catch(Exception ex){
            return "Error: " + ex;
        }
    }

    public int getMembershipType(String _displayName){
        String callAction = "Destiny2/SearchDestinyPlayer/";
        //-1 is the integer used by Bungie's API to search all platforms
        int membershipType = -1;
        String urlString = this.baseURL + callAction  + membershipType + "/" + _displayName;

        StringBuilder content = connectAndReturnContents(urlString);
        //Print out the complete JSON string.
        System.out.println("Output: " + content.toString());
        try{
            //Parse that object into a usable Java JSON object.
            JSONObject obj = new JSONObject(content.toString());
            //Print out membershipId
            return obj.getJSONArray("Response").getJSONObject(0).getInt("membershipType");
        } catch(Exception ex){
            return -2;
        }
    }

    public String[] getCharacters(String _membershipId, int _membershipType){
        String urlString = this.baseURL + "Destiny2/" + _membershipType + "/Profile/" + _membershipId + "/?components=100";
        StringBuilder content = connectAndReturnContents(urlString);
        String[] characterArray = new String[3];
        try{
            JSONObject obj = new JSONObject(content.toString());
            for (int i = 0; i < characterArray.length; i++){
                String characterId = obj.getJSONObject("Response").getJSONObject("profile").getJSONObject("data").getJSONArray("characterIds").getString(i);
                characterArray[i] = characterId;
            }
            return characterArray;
        } catch (Exception ex){
            return characterArray;
        }
    }

    public int getCharacterLightLevel(String _characterId, String _membershipId, int _membershipType){
        String urlString = this.baseURL + "Destiny2/" + _membershipType + "/Profile/" + _membershipId + "/Character/" + _characterId + "/?components=200";
        StringBuilder content = connectAndReturnContents(urlString);
        try{
            JSONObject obj = new JSONObject(content.toString());
            return obj.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("light");
        } catch(Exception ex){
            return -1;
        }
    }

    public String getRaceType(String _characterId, String _membershipId, int _membershipType){
        String urlString = this.baseURL + "Destiny2/" + _membershipType + "/Profile/" + _membershipId + "/Character/" + _characterId + "/?components=200";
        StringBuilder content = connectAndReturnContents(urlString);
        try{
            JSONObject obj = new JSONObject(content.toString());
            int raceInt =  obj.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("raceType");

            switch (raceInt){
                case 0:
                    return "Human";
                case 1:
                    return "Awoken";
                case 2:
                    return "Exo";
                default:
                    return "Unknown";

            }
        } catch(Exception ex){
            return "[Race Failed]";
        }
    }

    public String getClassType(String _characterId, String _membershipId, int _membershipType){
        String urlString = this.baseURL + "Destiny2/" + _membershipType + "/Profile/" + _membershipId + "/Character/" + _characterId + "/?components=200";
        StringBuilder content = connectAndReturnContents(urlString);
        try{
            JSONObject obj = new JSONObject(content.toString());
            int classInt = obj.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("classType");

            switch (classInt){
                case 0:
                    return "Titan";
                case 1:
                    return "Hunter";
                case 2:
                    return "Warlock";
                default:
                    return "Unknown";

            }
        } catch(Exception ex){
            return "[Class Failed]";
        }
    }

    public String getGenderType(String _characterId, String _membershipId, int _membershipType){
        String urlString = this.baseURL + "Destiny2/" + _membershipType + "/Profile/" + _membershipId + "/Character/" + _characterId + "/?components=200";
        StringBuilder content = connectAndReturnContents(urlString);
        try{
            JSONObject obj = new JSONObject(content.toString());
            int genderInt =  obj.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getInt("genderType");

            switch (genderInt){
                case 0:
                    return "Male";
                case 1:
                    return "Female";
                default:
                    return "Unknown";

            }
        } catch(Exception ex){
            return "[Gender Failed]";
        }
    }

    public String getLastPlayed(String _characterId, String _membershipId, int _membershipType){
        String urlString = this.baseURL + "Destiny2/" + _membershipType + "/Profile/" + _membershipId + "/Character/" + _characterId + "/?components=200";
        StringBuilder content = connectAndReturnContents(urlString);
        try{
            JSONObject obj = new JSONObject(content.toString());
            return obj.getJSONObject("Response").getJSONObject("character").getJSONObject("data").getString("dateLastPlayed");
        } catch(Exception ex){
            return "[Last Played Failed]";
        }
    }

    public StringBuilder connectAndReturnContents(String _urlString){
        try{
            //Make the connection.
            URL url = new URL(_urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty(this.apiHeader, this.apiKey);

            //Examine the response code.
            int status = con.getResponseCode();
            if (status != 200){
                return new StringBuilder("Error: API request failed. Status: ").append(status);
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
                return content;
            }
        } catch (Exception e){
            return new StringBuilder("Error: ").append(e);
        }
    }
}
