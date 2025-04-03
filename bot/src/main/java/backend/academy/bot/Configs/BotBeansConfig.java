package backend.academy.bot.Configs;

import backend.academy.bot.Clients.ChatClient;
import backend.academy.bot.Clients.LinkClient;
import backend.academy.bot.Handlers.BotUpdateHandler;
import backend.academy.bot.Handlers.UserFSMHandler;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotBeansConfig {
    @Bean
    public UserFSMHandler userFSMHandler(ChatClient chatClient, LinkClient linkClient) {
        return new UserFSMHandler(chatClient, linkClient);
    }

    @Bean
    public BotUpdateHandler botUpdateHandler(TelegramBot telegramBot,
                                             UserFSMHandler fsmHandler,
                                             LinkClient linkClient) {
        return new BotUpdateHandler(telegramBot, fsmHandler, linkClient);
    }
}
