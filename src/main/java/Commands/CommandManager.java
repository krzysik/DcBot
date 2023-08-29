package Commands;

import Listeners.FootballQuiz;
import Listeners.RankInLol;
import Model.FlexModel;
import Model.FootballPlayerModel;
import Model.SoloModel;
import com.opencsv.exceptions.CsvValidationException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
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
    Button epl;
    Button ligue1;
    Button serieA;
    Button laLiga;
    Button bundesliga;
    Button end;
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        String command = event.getName();
        if(command.equals("streak")){
            showStreak(event);
        }else if(command.equals("farmazon")){
            addFarmazons(event);
        }else if(command.equals("farmazons")){
            showFarmazons(event);
        }else if (command.equals("longeststreak")){
            showHighestStreak(event);
        }else if (command.equals("ranga")){
            OptionMapping messageOption = event.getOption("nick");
            String summonerName = messageOption.getAsString();
            try {
                showRankInSolo(event,summonerName);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }else if (command.equals("quiz")){
            try {

                startFootballQuiz(event);
                round = event;
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

}



    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event){
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("streak","Pokazuje aktualny streak Puniowskiego"));
        commandData.add(Commands.slash("farmazon","Dodaje kolejnego farmazona"));
        commandData.add(Commands.slash("farmazons", "Wyświetla ilość farmazonów"));
        commandData.add(Commands.slash("longeststreak","Pokazuje największy streak Puniowskiego (narazie nic nie robi)"));
        commandData.add(Commands.slash("ranga","Pokazuje range gracza w league of legends").addOption(OptionType.STRING,"nick","nick",true));
        commandData.add(Commands.slash("quiz","Zacznij Quiz piłkarski"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    public void startFootballQuiz(@NotNull SlashCommandInteractionEvent event) throws CsvValidationException, IOException {

        FootballQuiz quiz = new FootballQuiz();
         correctPlayer = quiz.choosePlayerForQuiz(quiz.getPlayers());
          epl = Button.primary("EPL","EPL");
         ligue1 = Button.primary("Ligue1","Ligue1");
         serieA = Button.primary("SerieA","SerieA");
         laLiga = Button.primary("LaLiga","LaLiga");
         bundesliga = Button.primary("Bundesliga","Bundesliga");
         end = Button.danger("End","End");
         embed = new EmbedBuilder();
            embed.setTitle("Runda: "+rounds);
            if(correctPlayer.getFullName().equals("")){
                embed.setDescription("Zgadnij w jakiej lidze gra ten piłkarz! "+correctPlayer.getName());
            }else{
                embed.setDescription("Zgadnij w jakiej lidze gra ten piłkarz! "+correctPlayer.getFullName());
            }
            event.getGuild().getTextChannelById("832636783161901156").sendMessage("Runda: "+rounds).setEmbeds(embed.build()).addActionRow(
                    epl,
                    ligue1,
                    serieA,
                    laLiga,
                    bundesliga
                    ).addActionRow(end).queue();

         }
    public void showRankInSolo(@NotNull SlashCommandInteractionEvent event, String summonerName) throws ParseException {

        RankInLol rank = new RankInLol();
        String id = rank.getEncryptedSummonerId(summonerName);
        SoloModel solo = rank.getInfoAboutSoloQueue(id);
        FlexModel flex = rank.getInfoAboutFlexQueue(id);
        if(solo.getSummonerName() != null){
            event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + solo.getSummonerName().toString() + "\n" + "Kolejka: " + solo.getQueueType().toString() + "\n" + "Ranga: " + solo.getTier().toString() + " " + solo.getRank().toString() + "\nIlość punktów: " +solo.getLeaguePoints() + "\nIlość wygranych: " + solo.getWins() + "\nIlość przegranych: " + solo.getLosses()).queue();

        }else{
            event.getGuild().getTextChannelById("832636783161901156").sendMessage("Gracz nie rozegrał rankedów w kolejce solo/duo").queue();

        }
        if(flex.getSummonerName() != null){
            event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + flex.getSummonerName().toString() + "\n" + "Kolejka: " + flex.getQueueType().toString() + "\n" + "Ranga: " + flex.getTier().toString() + " " + flex.getRank().toString() + "\nIlość punktów: " +flex.getLeaguePoints() + "\nIlość wygranych: " + flex.getWins() + "\nIlość przegranych: " + flex.getLosses()).queue();

        }else{
            event.getGuild().getTextChannelById("832636783161901156").sendMessage("Gracz nie rozegrał rankedów w kolejce flex").queue();

        }

    }

    public void showStreak(@NotNull SlashCommandInteractionEvent event){
        String last = null,line = null;
        int i = 0;
        Path path = Paths.get("C:\\Users\\Pollock\\IdeaProjects\\DcBot1\\src\\main\\resources\\dates.txt");
        try{
            File obj = new File(String.valueOf(path));
            Scanner reader = new Scanner(obj);
            while((line = reader.nextLine()) !=null){
                last=line;
                i++;
            }

        }catch (FileNotFoundException e){
            System.out.println("File dont exists");
        }catch(NoSuchElementException e){

        }
        if(i>1){
            event.getGuild().getTextChannelById("832636783161901156").sendMessage("Aktualny streak puniowskiego wynosi:"+i+" dni").queue();
        }else {
            event.getGuild().getTextChannelById("832636783161901156").sendMessage("Aktualny streak puniowskiego wynosi:" + i + "dzień").queue();
        }

    }

    private void showHighestStreak(SlashCommandInteractionEvent event) {
        String last = null,line;
        Path path = Paths.get("C:\\Users\\Pollock\\IdeaProjects\\DcBot1\\src\\main\\resources\\highestStreak.txt");
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

        event.getGuild().getTextChannelById("832636783161901156").sendMessage("Największy streak Puniowskiego wynosi:"+last+" dni.").queue();

    }

    private void addFarmazons(SlashCommandInteractionEvent event) {
        int last = 0;
        String line = null;

        Path path = Paths.get("D:\\puniekbot\\src\\main\\resources\\farmazon.txt");
        try{
            File obj = new File(String.valueOf(path));
            Scanner reader = new Scanner(obj);

            while((line = reader.nextLine()) !=null){
                last=Integer.parseInt(line);
                last++;
            }

        }catch (FileNotFoundException e){
            System.out.println("File dont exists");
        }catch(NoSuchElementException e){

        }
        saveFarmazon(Integer.toString(last));


        event.getGuild().getTextChannelById("832636783161901156").sendMessage("Aktualna ilość wypowiedzianych farmazonów na discordzie wynosi:"+last).queue();


    }

    private void showFarmazons(@NotNull SlashCommandInteractionEvent event) {
        String last = null,line = null;
        Path path = Paths.get("D:\\puniekbot\\src\\main\\resources\\farmazon.txt");
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


        event.getGuild().getTextChannelById("832636783161901156").sendMessage("Aktualna ilość wypowiedzianych farmazonów na discordzie wynosi:"+last).queue();

    }

    public void saveFarmazon(String far){
        Path path = Paths.get("D:\\puniekbot\\src\\main\\resources\\farmazon.txt");
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
            event.getGuild().getTextChannelById("832636783161901156").sendMessage("Poprawna odpowiedź"+"\n Ilość punktów: "+points).queue();
            disableButtons(event);
            if(rounds>10){
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Koniec quizu"+"\n Ilość punktów: "+points).queue();
                disableButtons(event);
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
            event.getGuild().getTextChannelById("832636783161901156").sendMessage("Koniec quizu"+"\n Ilość punktów: "+points).queue();
            disableButtons(event);
        }else{
            rounds++;
        event.getGuild().getTextChannelById("832636783161901156").sendMessage("Zła odpowiedź"+"\n Ilość punktów: "+points).queue();
            disableButtons(event);

            if(rounds>10){
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Koniec quizu"+"\n Ilość punktów: "+points).queue();
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
public void disableButtons(ButtonInteractionEvent event){
            event.getGuild().getTextChannelById("832636783161901156").editMessageEmbedsById(event.getGuild().getTextChannelById("832636783161901156").getLatestMessageId()).setEmbeds(embed.build()).setActionRow(
                    Button.danger("EPL","EPL").asDisabled(),
                    Button.danger("Ligue1","Ligue1").asDisabled(),
                    Button.danger("SerieA","SerieA").asDisabled(),
                    Button.danger("LaLiga","LaLiga").asDisabled(),
                    Button.danger("Bundesliga","Bundesliga").asDisabled()
            ).setActionRow(end.asDisabled()).queue();
}

}
