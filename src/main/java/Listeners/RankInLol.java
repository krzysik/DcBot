package Listeners;

import Model.FlexModel;
import Model.SoloModel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;

import java.net.URL;
import java.util.Scanner;


public class RankInLol extends ListenerAdapter {

    public String getEncryptedSummonerId(String summonerName) {
        JSONObject dataObject = null;
        try {

            URL url = new URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName + "?api_key=RGAPI-71b42d47-d84b-4cc8-9284-c7814fec28b7");

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
    public SoloModel getInfoAboutSoloQueue(String encryptedSummonerId) {

       SoloModel solo = new SoloModel();
        String url = "https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId + "?api_key=RGAPI-71b42d47-d84b-4cc8-9284-c7814fec28b7";
        try {
            RestTemplate restTemplate = new RestTemplate();
            SoloModel[] data = restTemplate.getForObject(url, SoloModel[].class);

            if (data != null) {
                for (SoloModel entry : data) {
                    if ("RANKED_SOLO_5x5".equals(entry.getQueueType())) {
                        solo = entry;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return solo;
    }

    public FlexModel getInfoAboutFlexQueue(String encryptedSummonerId) {

        FlexModel flex = new FlexModel();
        String url = "https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId + "?api_key=RGAPI-71b42d47-d84b-4cc8-9284-c7814fec28b7";
        try {
            RestTemplate restTemplate = new RestTemplate();
            FlexModel[] data = restTemplate.getForObject(url, FlexModel[].class);

            if (data != null) {
                for (FlexModel entry : data) {
                    if ("RANKED_FLEX_SR".equals(entry.getQueueType())) {
                        flex = entry;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flex;
    }

}

