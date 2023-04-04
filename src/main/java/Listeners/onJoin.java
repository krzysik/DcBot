package Listeners;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class onJoin extends ListenerAdapter {

  public void onMessageReceived(@NotNull MessageReceivedEvent event){
    String message = event.getMessage().getContentRaw();
    if(message.contains("ping")){
      event.getChannel().sendMessage("pong").queue();
    }
  }
  public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event){
    String name = event.getVoiceState().getMember().getUser().getName();
    LocalDate currentDate = LocalDate.now();
    String curDate = currentDate.toString();
    Path datyPath = Paths.get("D:\\puniekbot\\src\\main\\resources\\dates.txt");
    Path highestStreakPath = Paths.get("D:\\puniekbot\\src\\main\\resources\\highestStreak.txt");
    int i = Integer.valueOf(readDate(datyPath).get("count"));

    if(name.equals("Puniek")){
    try {
      if (findDifference(readDate(datyPath).get("date"), curDate) >= 2) {
        if(i>1) {

          event.getGuild().getTextChannelById("832636783161901156").sendMessage("Przerwano streak (streak puniowskiego wynosił:" + i + " dni)").queue();

            }else{
              event.getGuild().getTextChannelById("832636783161901156").sendMessage("Przerwano streak (streak puniowskiego wynosił:" + i + " dzień)").queue();
            }

        if (readStreak(highestStreakPath) < i) {
          saveHighestStreak(i);
        }

        PrintWriter writer = new PrintWriter(String.valueOf(datyPath));
        writer.print("");
        writer.close();
        i=1;
        saveDate();

      }else if(findDifference(readDate(datyPath).get("date"),curDate)==1){

        saveDate();
        i++;

          if (readStreak(highestStreakPath) < i) {
            saveHighestStreak(i);
          }

        event.getGuild().getTextChannelById("832636783161901156").sendMessage("Aktualny streak puniowskiego wynosi:"+i +" dni").queue();

      }else{
          if (readStreak(highestStreakPath) < i) {
           saveHighestStreak(i);
          }
        event.getGuild().getTextChannelById("832636783161901156").sendMessage("Aktualny streak puniowskiego wynosi:"+i+" dni").queue();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    }
    }

  public HashMap<String,String> readDate(Path path){
    //Pobranie ostatniej daty z pliku dates.txt i liczy ilość lini w pliku.
    String last = null, line = null;
    int i = 0;
    try{
      File obj = new File(String.valueOf(path));
      Scanner reader = new Scanner(obj);
      if(!reader.hasNextLine()){
        saveDate();
      }else{
        while((line=reader.nextLine())!=null){
          last=line;
          i++;
        }
      }
    }catch (FileNotFoundException e){
      System.out.println("File dont exists");
    }catch(NoSuchElementException e){

    }
    String count = Integer.toString(i);
    HashMap<String,String> date = new HashMap<>();
    date.put("date",last);
    date.put("count",count);
    return date;
  }
  public void saveDate(){
    LocalDate currentDate = LocalDate.now();
    String curDate = currentDate.toString();
    Path path = Paths.get("D:\\puniekbot\\src\\main\\resources\\dates.txt");

    try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
      writer.write( curDate+"\n");
    } catch (IOException e) {
      System.err.format("IOException: %s%n", e);
    }
  }

  public void saveHighestStreak(int i) {
    String streak;
    Path path = Paths.get("D:\\puniekbot\\src\\main\\resources\\highestStreak.txt");

    try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE)) {
      writer.write(streak = Integer.toString(i));
    } catch (IOException e) {
      System.err.format("IOException: %s%n", e);
    }
  }

    public int readStreak(Path path){
     String line = null, last =null;
      try{
        File obj = new File(String.valueOf(path));
        Scanner reader = new Scanner(obj);
        while((line = reader.nextLine())!=null){
          last=line;
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }catch(NoSuchElementException e){

      }
      int highestStreak = Integer.parseInt(last);
      return highestStreak;
    }


  static long findDifference(String start_date,
                             String end_date)
  {
    // SimpleDateFormat converts the
    // string format to date object
    SimpleDateFormat sdf
            = new SimpleDateFormat(
            "yyyy-MM-dd");
    long difference = 0;
    try {

      // parse method is used to parse
      // the text from a string to
      // produce the date
      Date d1 = sdf.parse(start_date);
      Date d2 = sdf.parse(end_date);

      //Calculate time difference
      // in milliseconds
      long difference_In_Time
              = d2.getTime() - d1.getTime();

      //Calculate time difference in seconds,
      // minutes, hours, years, and days

       long difference_In_Days
              = TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time)
              % 365;

      long difference_In_Years
              = TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time)
              / 365l;

      difference=difference_In_Days;

    }
    catch (ParseException e) {
      e.printStackTrace();
    }

    return difference;
  }

}