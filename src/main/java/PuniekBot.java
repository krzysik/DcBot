import Commands.CommandManager;
import Listeners.onJoin;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class PuniekBot {
    private final ShardManager shardManager;
    private final AudioPlayerManager playerManager;
    private final Map<Long, AudioPlayer> players;

    public PuniekBot() throws LoginException {
        String token = System.getenv("BOT_TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Puńka"));
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.enableIntents(GatewayIntent.GUILD_VOICE_STATES);
        shardManager = builder.build();

        shardManager.addEventListener(new onJoin(),new CommandManager());
        playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        players = new HashMap<>();
    }

    public ShardManager getShardManager(){
        return shardManager;
    }

    public static void main(String[] args){
        try {
            PuniekBot bot = new PuniekBot();
        }catch(LoginException e){
            System.out.println("Bot token is invalid");
        }
    }
}
