package bot.discordbot;

import bot.config.BotProperties;
import bot.db.repository.RepositoryContainer;
import bot.discordbot.command.Help;
import bot.discordbot.command.PingPong;
import bot.discordbot.event.UserCounter;
import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscordBot {

  public static final String PREFIX = "!";
  public final BotProperties botProperties;
  private final RepositoryContainer repositoryContainer;

  @Autowired
  public DiscordBot(BotProperties botProperties,
      RepositoryContainer repositoryContainer) {
    this.botProperties = botProperties;
    this.repositoryContainer = repositoryContainer;
  }

  @PostConstruct
  public void startBot() throws LoginException {
    // start getting a bot account set up
    new JDABuilder(AccountType.BOT)
        // set the token
        .setToken(botProperties.getSetting().getToken())

        // set the game for when the bot is loading
        .setStatus(OnlineStatus.ONLINE)
        .setActivity(Activity.playing(PREFIX + "help"))
        .setAutoReconnect(true)

        // add the listeners
        .addEventListeners(new PingPong())
        .addEventListeners(new UserCounter(repositoryContainer))
        .addEventListeners(new Help(botProperties))
        .addEventListeners()
        // start it up!
        .build();
  }
}
