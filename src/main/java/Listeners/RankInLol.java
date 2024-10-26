package Listeners;

import Model.AccountModel;
import Model.QueueModel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.web.client.RestTemplate;

public class RankInLol extends ListenerAdapter {
    String apiKey = "RGAPI-e8957bf8-8d81-4cd5-9b77-72576384afdd";


    public String getEncryptedSummonerId(String summonerName, String summonerTag) {
        String puuid = null;
        AccountModel account = fetchAccountByRiotId(summonerName, summonerTag);

        if (account != null) {
            puuid = account.getPuuid();
            account = fetchAccountByPuuid(puuid);
        }

        return (account != null) ? account.getId() : null;
    }

    private AccountModel fetchAccountByRiotId(String summonerName, String summonerTag) {
        String url = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/" + summonerName + "/" + summonerTag + "?api_key=" + apiKey;
        return fetchAccountData(url);
    }

    private AccountModel fetchAccountByPuuid(String puuid) {
        String url = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/" + puuid + "?api_key=" + apiKey;
        return fetchAccountData(url);
    }

    private AccountModel fetchAccountData(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(url, AccountModel.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public QueueModel getInfoAboutRanks(String encryptedSummonerId,String queueType) {
        String url = "https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId + "?api_key=" + apiKey;


        try {
            RestTemplate restTemplate = new RestTemplate();
            QueueModel[] data = restTemplate.getForObject(url, QueueModel[].class);

            if (data != null) {
                for (QueueModel entry : data) {
                    if (queueType.equals(entry.getQueueType())) {
                        return entry; // Zwracamy od razu, gdy znajdziemy odpowiedni wpis
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Zwracamy null, jeśli nie znaleziono odpowiedniego wpisu
    }

}

