package Listeners;

import Model.AccountModel;
import Model.FlexModel;
import Model.SoloModel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.web.client.RestTemplate;

public class RankInLol extends ListenerAdapter {
    String apiKey = System.getenv("API_KEY");


    public String getEncryptedSummonerId(String summonerName) {
        System.out.println(apiKey);
        AccountModel account = new AccountModel();
          String url = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName + "?api_key="+apiKey;
          try{
              RestTemplate restTemplate = new RestTemplate();
              AccountModel data = restTemplate.getForObject(url,AccountModel.class);
              if(data != null){
                  account = data;
              }
          } catch (Exception e) {
              e.printStackTrace();
          }


        return account.getId().toString();
    }
    public SoloModel getInfoAboutSoloQueue(String encryptedSummonerId) {

       SoloModel solo = new SoloModel();
        String url = "https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId + "?api_key="+apiKey;
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
        String url = "https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId + "?api_key="+apiKey;
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

