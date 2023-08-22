package Commands;

import Listeners.RankInLol;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CommandManager extends ListenerAdapter {

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
                showRankInLol(event,summonerName);
            } catch (ParseException e) {
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
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
    public void showRankInLol(@NotNull SlashCommandInteractionEvent event, String summonerName) throws ParseException {
        RankInLol rank = new RankInLol();
        String id = rank.getEncryptedSummonerId(summonerName);


        JSONParser parse = new JSONParser();
        JSONObject dataSoloObject = new JSONObject();
        JSONObject dataFlexObject = new JSONObject();
        JSONObject data = new JSONObject();
        System.out.println(rank.getInfoAboutAccount(id).size());
        if (rank.getInfoAboutAccount(id).size() == 3) {
            data = (JSONObject) parse.parse(String.valueOf(rank.getInfoAboutAccount(id).get(0)));
            if(data.get("queueType").equals("RANKED_SOLO_5x5")){
                dataSoloObject = data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataSoloObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataSoloObject.get("queueType").toString() + "\n" + "Ranga: " + dataSoloObject.get("tier").toString() + " " + dataSoloObject.get("rank").toString() + "\nIlość punktów: " + dataSoloObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataSoloObject.get("wins").toString() + "\nIlość przegranych: " + dataSoloObject.get("losses").toString()).queue();
            }else if (data.get("queueType").equals("RANKED_FLEX_SR")){
                dataFlexObject=data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataFlexObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataFlexObject.get("queueType").toString() + "\n" + "Ranga: " + dataFlexObject.get("tier").toString() + " " + dataFlexObject.get("rank").toString() + "\nIlość punktów: " + dataFlexObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataFlexObject.get("wins").toString() + "\nIlość przegranych: " + dataFlexObject.get("losses").toString()).queue();
            }
            data = (JSONObject) parse.parse(String.valueOf(rank.getInfoAboutAccount(id).get(1)));
            if(data.get("queueType").equals("RANKED_SOLO_5x5")){
                dataSoloObject=data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataSoloObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataSoloObject.get("queueType").toString() + "\n" + "Ranga: " + dataSoloObject.get("tier").toString() + " " + dataSoloObject.get("rank").toString() + "\nIlość punktów: " + dataSoloObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataSoloObject.get("wins").toString() + "\nIlość przegranych: " + dataSoloObject.get("losses").toString()).queue();

            }else if (data.get("queueType").equals("RANKED_FLEX_SR")){
                dataFlexObject=data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataFlexObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataFlexObject.get("queueType").toString() + "\n" + "Ranga: " + dataFlexObject.get("tier").toString() + " " + dataFlexObject.get("rank").toString() + "\nIlość punktów: " + dataFlexObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataFlexObject.get("wins").toString() + "\nIlość przegranych: " + dataFlexObject.get("losses").toString()).queue();

            }
            data = (JSONObject) parse.parse(String.valueOf(rank.getInfoAboutAccount(id).get(2)));
            if(data.get("queueType").equals("RANKED_SOLO_5x5")){
                dataSoloObject=data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataSoloObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataSoloObject.get("queueType").toString() + "\n" + "Ranga: " + dataSoloObject.get("tier").toString() + " " + dataSoloObject.get("rank").toString() + "\nIlość punktów: " + dataSoloObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataSoloObject.get("wins").toString() + "\nIlość przegranych: " + dataSoloObject.get("losses").toString()).queue();

            }else if (data.get("queueType").equals("RANKED_FLEX_SR")){
                dataFlexObject=data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataFlexObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataFlexObject.get("queueType").toString() + "\n" + "Ranga: " + dataFlexObject.get("tier").toString() + " " + dataFlexObject.get("rank").toString() + "\nIlość punktów: " + dataFlexObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataFlexObject.get("wins").toString() + "\nIlość przegranych: " + dataFlexObject.get("losses").toString()).queue();

            }

        } else if (rank.getInfoAboutAccount(id).size() == 2) {
            data = (JSONObject) parse.parse(String.valueOf(rank.getInfoAboutAccount(id).get(0)));
            if(data.get("queueType").equals("RANKED_SOLO_5x5")){
                dataSoloObject=data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataSoloObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataSoloObject.get("queueType").toString() + "\n" + "Ranga: " + dataSoloObject.get("tier").toString() + " " + dataSoloObject.get("rank").toString() + "\nIlość punktów: " + dataSoloObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataSoloObject.get("wins").toString() + "\nIlość przegranych: " + dataSoloObject.get("losses").toString()).queue();

            }else if (data.get("queueType").equals("RANKED_FLEX_SR")){
                dataFlexObject=data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataFlexObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataFlexObject.get("queueType").toString() + "\n" + "Ranga: " + dataFlexObject.get("tier").toString() + " " + dataFlexObject.get("rank").toString() + "\nIlość punktów: " + dataFlexObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataFlexObject.get("wins").toString() + "\nIlość przegranych: " + dataFlexObject.get("losses").toString()).queue();

            }
            data = (JSONObject) parse.parse(String.valueOf(rank.getInfoAboutAccount(id).get(1)));
            if(data.get("queueType").equals("RANKED_SOLO_5x5")){
                dataSoloObject=data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataSoloObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataSoloObject.get("queueType").toString() + "\n" + "Ranga: " + dataSoloObject.get("tier").toString() + " " + dataSoloObject.get("rank").toString() + "\nIlość punktów: " + dataSoloObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataSoloObject.get("wins").toString() + "\nIlość przegranych: " + dataSoloObject.get("losses").toString()).queue();

            }else if (data.get("queueType").equals("RANKED_FLEX_SR")){
                dataFlexObject=data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataFlexObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataFlexObject.get("queueType").toString() + "\n" + "Ranga: " + dataFlexObject.get("tier").toString() + " " + dataFlexObject.get("rank").toString() + "\nIlość punktów: " + dataFlexObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataFlexObject.get("wins").toString() + "\nIlość przegranych: " + dataFlexObject.get("losses").toString()).queue();

            }
        }else if (rank.getInfoAboutAccount(id).size() == 1){
            data = (JSONObject) parse.parse(String.valueOf(rank.getInfoAboutAccount(id).get(0)));
            if(data.get("queueType").equals("RANKED_SOLO_5x5")){
                dataSoloObject=data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataSoloObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataSoloObject.get("queueType").toString() + "\n" + "Ranga: " + dataSoloObject.get("tier").toString() + " " + dataSoloObject.get("rank").toString() + "\nIlość punktów: " + dataSoloObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataSoloObject.get("wins").toString() + "\nIlość przegranych: " + dataSoloObject.get("losses").toString()).queue();

            }else if (data.get("queueType").equals("RANKED_FLEX_SR")){
                dataFlexObject=data;
                event.getGuild().getTextChannelById("832636783161901156").sendMessage("Nick: " + dataFlexObject.get("summonerName").toString() + "\n" + "Kolejka: " + dataFlexObject.get("queueType").toString() + "\n" + "Ranga: " + dataFlexObject.get("tier").toString() + " " + dataFlexObject.get("rank").toString() + "\nIlość punktów: " + dataFlexObject.get("leaguePoints").toString() + "\nIlość wygranych: " + dataFlexObject.get("wins").toString() + "\nIlość przegranych: " + dataFlexObject.get("losses").toString()).queue();

            }else{
                event.getGuild().getTextChannelById("832636783161901156").sendMessage(summonerName+" nie rozegrał rankedów").queue();
            }
        }else{
            event.getGuild().getTextChannelById("832636783161901156").sendMessage(summonerName+" nie rozegrał rankedów").queue();
        }


    }
    public void showStreak(@NotNull SlashCommandInteractionEvent event){
        String last = null,line = null;
        int i = 0;
        Path path = Paths.get("D:\\puniekbot\\src\\main\\resources\\dates.txt");
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
        Path path = Paths.get("D:\\puniekbot\\src\\main\\resources\\highestStreak.txt");
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
}
