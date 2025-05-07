package backend.academy.bot.Configs;

import backend.academy.bot.Clients.ChatClient;
import backend.academy.bot.Clients.LinkClient;
import backend.academy.bot.Commands.CommandFactory;
import backend.academy.bot.FSM.FiltersState;
import backend.academy.bot.FSM.InitState;
import backend.academy.bot.FSM.UserFSMContext;
import backend.academy.bot.Handlers.BotUpdateHandler;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotBeansConfig {
    @Bean
    public FiltersState filtersState(LinkClient linkClient) {
        return new FiltersState(linkClient);
    }

    @Bean
    public InitState initState(ChatClient chatClient, LinkClient linkClient) {
        return new InitState(chatClient, linkClient);
    }

    @Bean
    public UserFSMContext userFSMContext(InitState initState) {
        return new UserFSMContext(initState);
    }

    @Bean
    public CommandFactory commandFactory(LinkClient linkClient) {
        return new CommandFactory(linkClient);
    }

    @Bean
    public BotUpdateHandler botUpdateHandler(TelegramBot telegramBot,
                                             UserFSMContext fsmContext,
                                             CommandFactory commandFactory) {
        return new BotUpdateHandler(telegramBot, fsmContext, commandFactory);
    }
}
