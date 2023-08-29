package Listeners;

import Model.FootballPlayerModel;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FootballQuiz extends ListenerAdapter {

public List<FootballPlayerModel> getPlayers() throws IOException, CsvValidationException {
    CSVReader reader = new CSVReader(new FileReader("src/resources/top5_leagues_player.csv"));

    List<FootballPlayerModel> players = new ArrayList<FootballPlayerModel>();
    String[] record = null;
    while((record = reader.readNext()) != null){
        FootballPlayerModel player = new FootballPlayerModel();
        player.setId(record[0]);
        player.setName(record[1]);
       player.setFullName(record[2]);
        player.setAge(record[3]);
        player.setPosition(record[9]);
        player.setClub(record[12]);
        player.setLeague(record[17]);
        players.add(player);
    }

    return players;
}
public FootballPlayerModel choosePlayerForQuiz(List<FootballPlayerModel> players){

    FootballPlayerModel player ;
    Random random = new Random();
    int upperbound = players.size();
    int randomNumber;

        randomNumber = random.nextInt(upperbound);

        player = players.get(randomNumber);

    return player;
}

}


