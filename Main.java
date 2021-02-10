/*Last Update: 9 Feb 2021

  The main method of the project.

  Contributing Author: Austin Matias
 */

import org.json.JSONObject;

import java.sql.SQLOutput;

public class Main {

    private static final BungieAPI testAPI= new BungieAPI();

    public static void main(String[] args) {
        String displayName = "MightyMatias";

        System.out.println("Assignment essentials (API output and relevant data parsed):");
        System.out.println();
        String memId = testAPI.getMembershipId(displayName);
        int memType = testAPI.getMembershipType(displayName);

        System.out.println("Membership ID: " + memId);
        System.out.println("Membership Type: " + memType);

        System.out.println();

        System.out.println("Something extra I did because I was enjoying playing around with APIs:");
        System.out.println();
        System.out.println("The Destiny 2 characters for " + displayName + " are:");
        printCharacterInfo(testAPI, testAPI.getCharacters(memId, memType), memId, memType);
    }

    public static void printCharacterInfo(BungieAPI _testAPI, String[] _characterArray, String _membershipId, int _membershipType){
        for (int i = 0; i < _characterArray.length; i++){

            int lightLevel = _testAPI.getCharacterLightLevel(_characterArray[i],_membershipId, _membershipType);
            String raceType = _testAPI.getRaceType(_characterArray[i],_membershipId, _membershipType);
            String genderType = _testAPI.getGenderType(_characterArray[i],_membershipId, _membershipType);
            String classType = _testAPI.getClassType(_characterArray[i],_membershipId, _membershipType);
            String lastPlayed = _testAPI.getLastPlayed(_characterArray[i],_membershipId, _membershipType);

            System.out.println(lightLevel + " " + raceType +  " " +genderType +  " " +classType + ", last played " + lastPlayed);
        }
    }

}
