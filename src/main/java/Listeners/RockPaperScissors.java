package Listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RockPaperScissors extends ListenerAdapter {

    private static final Map<String,String> playerChoices = new HashMap<>();
    private static final String[] options = {"kamień","papier","nożyce"};
    private static String player1Id;
    private static String player2Id;
     public static boolean gameStarted = false;
    public static void playRockPaperScissors(SlashCommandInteractionEvent event,String enemy) {
        player1Id = event.getUser().getId();
        List<Member> members = event.getGuild().getMembers();
        for (Member member : members) {
            if (member.getEffectiveName().equalsIgnoreCase(enemy)) {
              player2Id = member.getId();
            }
        }
        playerChoices.clear();
        gameStarted = true;
            if (player2Id != null) {
                event.reply("Gra rozpoczęta! Użyj `/wybierz [kamień/papier/nożyce]` aby wybrać.").queue();
                event.getChannel().sendMessage("<@" + player1Id + "> gra z <@" + player2Id + ">!").queue();
            } else {
                event.reply("Musisz podać przeciwnika! Użyj `/gra [@użytkownik]`.").queue();
            }


    }
public static void playerChoice(SlashCommandInteractionEvent event){
        if(gameStarted == false){
            event.reply("Musisz najpierw rozpocząć grę za pomocą `/graj @użytkownik`.").queue();
        }else{
            String userId = event.getUser().getId();
            if (!userId.equals(player1Id) && !userId.equals(player2Id)) {
                event.reply("Tylko gracze w grze mogą użyć tej komendy!").queue();
                return;
            }
    if (playerChoices.size() < 2) {
        if (isValidChoice(event.getOption("opcja").getAsString())) {
            playerChoices.put(event.getUser().getId(), event.getOption("opcja").getAsString());

            if (playerChoices.size() == 2) {
                determineWinner(event);
            }
        } else {
            event.reply("Nieprawidłowy wybór! Wybierz kamień, papier lub nożyce.").queue();
        }
    } else {
        event.reply("Gra już zakończona! Użyj `/graj` aby zagrać ponownie.").queue();
    }}
}


    private static boolean isValidChoice(String choice) {
        for (String option : options) {
            if (option.equals(choice)) {
                return true;
            }
        }
        return false;
    }

    private static void determineWinner(SlashCommandInteractionEvent event) {
        String player1Choice = playerChoices.get(player1Id);
        String player2Choice = playerChoices.get(player2Id);

        String resultMessage;
        if (player1Choice.equals(player2Choice)) {
            resultMessage = "Remis! Obaj gracze wybrali " + player1Choice + ".";
        } else if ((player1Choice.equals("kamień") && player2Choice.equals("nożyce")) ||
                (player1Choice.equals("papier") && player2Choice.equals("kamień")) ||
                (player1Choice.equals("nożyce") && player2Choice.equals("papier"))) {
            resultMessage = "<@" + player1Id + "> wygrywa!";
        } else {
            resultMessage = "<@" + player2Id + "> wygrywa!";
        }

        event.getChannel().sendMessage(resultMessage).queue();
        playerChoices.clear();
        player1Id = null;
        player2Id = null;
    }
}



