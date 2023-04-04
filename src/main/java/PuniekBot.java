import Commands.CommandManager;
import Listeners.onJoin;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class PuniekBot {
    private final ShardManager shardManager;

    public PuniekBot() throws LoginException {
        String token = "MTA3NzU3ODk5NjY5MjMwMzk3Mg.Gzok2M.kowp2mDCET1_agqkL2OwzESjM4JfAC-xPvVNek";
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
