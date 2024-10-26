package Model;

public class QueueModel {
    private String summonerName;
    private String queueType;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;
    private String summonerId;

    public QueueModel() {

    }

    public String getSummonerName() {
        return summonerName;
    }

    public String getQueueType() {
        return queueType;
    }

    public String getTier() {
        return tier;
    }

    public String getRank() {
        return rank;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }
    public String getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }

    @Override
    public String toString() {
        return String.format("Nick: %s, Kolejka: %s, Ranga: %s %s, Ilość punktów: %d, Ilość wygranych: %d, Ilość przegranych: %d",
                summonerName, queueType, tier, rank, leaguePoints, wins, losses);
    }

}
