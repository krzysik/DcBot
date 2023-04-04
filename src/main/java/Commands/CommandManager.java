package Commands;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

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
        }

}



    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event){
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("streak","Pokazuje aktualny streak Puniowskiego"));
        commandData.add(Commands.slash("farmazon","Dodaje kolejnego farmazona"));
        commandData.add(Commands.slash("farmazons", "Wyświetla ilość farmazonów"));
        commandData.add(Commands.slash("longeststreak","Pokazuje największy streak Puniowskiego (narazie nic nie robi)"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
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
