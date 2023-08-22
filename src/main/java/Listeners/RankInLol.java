package Listeners;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;

import java.net.URL;
import java.util.Scanner;


public class RankInLol extends ListenerAdapter {

    public String getEncryptedSummonerId(String summonerName) {
        JSONObject dataObject = null;
        try {

            URL url = new URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName + "?api_key=RGAPI-8a601150-2637-4228-ab3a-420bd00f570e");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }

                scanner.close();

                JSONParser parser = new JSONParser();
                dataObject = (JSONObject) parser.parse(String.valueOf(informationString));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataObject.get("id").toString();
    }
    public JSONArray getInfoAboutAccount(String encryptedSummonerId) {
        JSONObject countryData = new JSONObject();
        StringBuilder informationString = null;
        JSONArray dataObject = null;
        try {

            URL url = new URL("https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId + "?api_key=RGAPI-8a601150-2637-4228-ab3a-420bd00f570e");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }

                scanner.close();

                //JSON simple library Setup with Maven is used to convert strings to JSON
                JSONParser parse = new JSONParser();
                dataObject = (JSONArray) parse.parse(String.valueOf(informationString));
                //Get the first JSON object in the JSON array
              //  System.out.println(dataObject.get(1));

            //    countryData = (JSONObject) dataObject.get(1);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(dataObject == null){
            return null;
        }

        return dataObject;
    }
}
