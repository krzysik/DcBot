package Commands;

import Listeners.FootballQuiz;
import Listeners.RankInLol;
import Listeners.RockPaperScissors;
import Model.FootballPlayerModel;
import Model.QueueModel;
import com.opencsv.exceptions.CsvValidationException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class CommandManager extends ListenerAdapter {

    FootballPlayerModel correctPlayer;
    EmbedBuilder embed;
    SlashCommandInteractionEvent round;
    int rounds = 1;
    int points = 0;
    String channelId = "832636783161901156";
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        switch(command){
            case "streak" :
                showStreak(event);
                break;

            case "farmazon" :
                addFarmazons(event);
                break;

            case "farmazons" :
                showFarmazons(event);
                break;

            case "longeststreak" :
                showHighestStreak(event);
                break;

            case "ranga" :
                OptionMapping messageOption = event.getOption("nick");
                OptionMapping messageOption1 = event.getOption("tag");
                OptionMapping messageOption2 = event.getOption("queue");
                String summonerName = messageOption.getAsString();
                String summonerTag = messageOption1.getAsString();
                String queueType = messageOption2.getAsString();

                if (queueType.equalsIgnoreCase("solo")) {
                    queueType = "RANKED_SOLO_5x5";
                } else if (queueType.equalsIgnoreCase("flex")) {
                    queueType = "RANKED_FLEX_SR";
                } else {
                    event.getGuild().getTextChannelById(channelId)
                            .sendMessage("Proszę podać poprawny typ kolejki: `solo` lub `flex`.").queue();
                    return;
                }
                try {
                    showRanks(event, summonerName,summonerTag,queueType);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "quiz" :
                try {

                    startFootballQuiz(event);
                    round = event;
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "graj" :
                OptionMapping messageOption3 = event.getOption("nick");
                String enemy = messageOption3.getAsString();
                RockPaperScissors.playRockPaperScissors(event, enemy);
                break;

            case "wybierz" :
                RockPaperScissors.playerChoice(event);
                break;

        }

    }





    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event){
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("streak","Pokazuje aktualny streak Puniowskiego"));
        commandData.add(Commands.slash("farmazon","Dodaje kolejnego farmazona"));
        commandData.add(Commands.slash("farmazons", "Wyświetla ilość farmazonów"));
        commandData.add(Commands.slash("longeststreak","Pokazuje największy streak Puniowskiego (narazie nic nie robi)"));
        commandData.add(Commands.slash("ranga","Pokazuje range gracza w league of legends").addOption(OptionType.STRING,"nick","nick",true).addOption(OptionType.STRING,"tag","tag",true).addOption(OptionType.STRING,"queue","queue",true));
        commandData.add(Commands.slash("quiz","Zacznij Quiz piłkarski"));
        commandData.add(Commands.slash("graj","Zacznij gre w kamień, papier, nożyce").addOption(OptionType.STRING,"nick","nick",true));
        commandData.add(Commands.slash("wybierz", "Wybierz opcję: kamień, papier, nożyce").addOption(OptionType.STRING, "opcja", "Wybierz kamień, papier lub nożyce", true));
        commandData.add(Commands.slash("play","Odtwarzaj muzyke z linku").addOption(OptionType.STRING, "link","Link do utworu",true));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event){
        String name = event.getVoiceState().getMember().getUser().getAsMention();
        String message = null;

        if(event.getNewValue() != null && event.getOldValue() == null){
            message = "Witaj! " +name;
        }else if(event.getNewValue() == null && event.getOldValue() != null){
            message = "Żegnaj! " +name;
        }

        if(message != null){
            event.getGuild().getTextChannelById(channelId).sendMessage(message).queue();
        }
    }
    public void startFootballQuiz(@NotNull SlashCommandInteractionEvent event) throws CsvValidationException, IOException {
        FootballQuiz quiz = new FootballQuiz();
         correctPlayer = quiz.choosePlayerForQuiz(quiz.getPlayers());
         embed = new EmbedBuilder();

         String playerName = correctPlayer.getFullName().isEmpty() ? correctPlayer.getName() : correctPlayer.getFullName();
         embed.setDescription("Zgadnij w jakiej lidze gra ten piłkarz! "+playerName);

            event.getGuild().getTextChannelById(channelId).sendMessage("Runda: "+rounds)
                    .setEmbeds(embed.build())
                    .addActionRow(
                        Button.primary("EPL","EPL"),
                        Button.primary("Ligue1","Ligue1"),
                        Button.primary("SerieA","SerieA"),
                        Button.primary("LaLiga","LaLiga"),
                        Button.primary("Bundesliga","Bundesliga")
                    )
                    .addActionRow(Button.danger("End","End"))
                    .queue();

    }
    public void showRanks(@NotNull SlashCommandInteractionEvent event, String summonerName, String summonerTag, String queueType) throws ParseException {

        RankInLol rank = new RankInLol();
        String id = rank.getEncryptedSummonerId(summonerName,summonerTag);
        QueueModel queue = rank.getInfoAboutRanks(id,queueType);

        if(queue != null){
            event.getGuild().getTextChannelById(channelId).sendMessage("Nick: " + summonerName + "\n" + "Kolejka: " + queue.getQueueType().toString() + "\n" + "Ranga: " + queue.getTier().toString() + " " + queue.getRank().toString() + "\nIlość punktów: " +queue.getLeaguePoints() + "\nIlość wygranych: " + queue.getWins() + "\nIlość przegranych: " + queue.getLosses()).queue();

        }else{
            event.getGuild().getTextChannelById(channelId).sendMessage("Gracz nie rozegrał rankedów w kolejce "+ queueType).queue();
        }
    }



    public void showStreak(@NotNull SlashCommandInteractionEvent event){
        int i = 0;
        Path path = Paths.get("src/resources/dates.txt");

        try (Scanner reader = new Scanner(path.toFile())) {
            while (reader.hasNextLine()) {
                reader.nextLine();
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Plik nie istnieje");
        }

        if(i>1){
            event.getGuild().getTextChannelById(channelId).sendMessage("Aktualny streak puniowskiego wynosi:"+i+" dni").queue();
        }else {
            event.getGuild().getTextChannelById(channelId).sendMessage("Aktualny streak puniowskiego wynosi:" + i + "dzień").queue();
        }

    }

    private void showHighestStreak(SlashCommandInteractionEvent event) {
        String last = null,line;
        Path path = Paths.get("src/resources/highestStreak.txt");
        try{
            File obj = new File(String.valueOf(path));
            Scanner reader = new Scanner(obj);
            while((line = reader.nextLine()) !=null){
                last=line;
            }

        }catch (FileNotFoundException e){
            System.out.println("File dont exists");
        }catch(NoSuchElementException e){

        }

        event.getGuild().getTextChannelById(channelId).sendMessage("Największy streak Puniowskiego wynosi:"+last+" dni.").queue();

    }


    private void addFarmazons(SlashCommandInteractionEvent event) {
        int count = 0;

        Path path = Paths.get("src/resources/farmazon.txt");
        try (Scanner reader = new Scanner(path.toFile())) {
            while (reader.hasNextLine()) {
                count = Integer.parseInt(reader.nextLine());
            }
            count++;
        } catch (FileNotFoundException e) {
            System.out.println("Plik nie istnieje");
        } catch (NumberFormatException e) {
            System.out.println("Błąd formatu liczby w pliku");
        }
        saveFarmazon(Integer.toString(count));


        event.getGuild().getTextChannelById(channelId).sendMessage("Aktualna ilość wypowiedzianych farmazonów na discordzie wynosi:"+count).queue();


    }

    private void showFarmazons(@NotNull SlashCommandInteractionEvent event) {
        String last = null;
        Path path = Paths.get("src/resources/farmazon.txt");
        try (Scanner reader = new Scanner(path.toFile())) {
            while (reader.hasNextLine()) {
                last = reader.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Plik nie istnieje");
        }

        String message = last != null ? last : "Brak farmazonów do wyświetlenia.";
        event.getGuild().getTextChannelById(channelId).sendMessage("Aktualna ilość wypowiedzianych farmazonów na discordzie wynosi:"+message).queue();

    }

    public void saveFarmazon(String far){
        Path path = Paths.get("src/resources/farmazon.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE)) {
            writer.write(far);

        } catch (IOException ioe) {
            System.err.format("IOException: %s%n", ioe);
        }

    }
public void onButtonInteraction(@NotNull ButtonInteractionEvent event){


        if(event.getButton().getId().equals(correctPlayer.getLeague())){
            rounds++;
            points++;
            event.getGuild().getTextChannelById(channelId).sendMessage("Poprawna odpowiedź"+"\n Ilość punktów: "+points).queue();
            disableButtons(event,correctPlayer.getLeague());
            if(rounds>10){
                event.getGuild().getTextChannelById(channelId).sendMessage("Koniec quizu"+"\n Ilość punktów: "+points).queue();
                disableButtons(event,correctPlayer.getLeague());
            }else {
                try {
                    startFootballQuiz(round);
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else if(event.getButton().getId().equals("End")){
            event.getGuild().getTextChannelById(channelId).sendMessage("Koniec quizu"+"\nIlość punktów: "+points).queue();
            disableButtons(event,correctPlayer.getLeague());
        }else{
            rounds++;
        event.getGuild().getTextChannelById(channelId).sendMessage("Zła odpowiedź"+"\nIlość punktów: "+points+"\nPoprawna odpowiedź to : "+correctPlayer.getLeague()).queue();

            disableButtons(event,correctPlayer.getLeague());

            if(rounds>10){
                event.getGuild().getTextChannelById(channelId).sendMessage("Koniec quizu"+"\nIlość punktów: "+points).queue();
                points=0;
                rounds=0;
            }else {
                try {
                    startFootballQuiz(round);
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            }

        }
public void disableButtons(ButtonInteractionEvent event,String correctAnswer){

        String[] leagues = {"EPL","Ligue1","SerieA","LaLiga","Bundesliga"};
        Button[] buttons = new Button[leagues.length];

        for(int i = 0; i< leagues.length; i++){
            Button button = Button.danger(leagues[i],leagues[i]).asDisabled();
            if(leagues[i].equals(correctAnswer)){
                button = Button.success(leagues[i],leagues[i]).asDisabled();
            }
            buttons[i] = button;
        }
        event.getGuild().getTextChannelById(channelId)
            .editMessageEmbedsById(event.getGuild().getTextChannelById(channelId).getLatestMessageId())
            .setEmbeds(embed.build())
            .setActionRow(buttons)
            .queue();

        }
}



