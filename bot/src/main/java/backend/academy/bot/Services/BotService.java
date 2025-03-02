package backend.academy.bot.Services;

import backend.academy.bot.Configs.BotConfig;
import backend.academy.bot.Handlers.BotUpdateHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class BotService {
    private final TelegramBot bot;
    private final BotUpdateHandler updateHandler;

    public BotService(BotConfig botConfig) {
        this.bot = new TelegramBot(botConfig.telegramToken());
        this.updateHandler = new BotUpdateHandler(bot);
    }

    @PostConstruct
    public void init() {
        setBotCommands();

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                updateHandler.handleUpdate(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void setBotCommands() {
        BotCommand[] commands = new BotCommand[] {
            new BotCommand("/start", "Запуск бота"),
            new BotCommand("/help", "Доступные команды"),
            new BotCommand("/track", "Начать отслеживать ресурс. Укажите URL"),
            new BotCommand("/untrack", "Перестать отслеживать ресурс. Укажите URL"),
            new BotCommand("/list", "Показать список отслеживаемых ресурсов"),
        };

        bot.execute(new SetMyCommands(commands));
    }
}
