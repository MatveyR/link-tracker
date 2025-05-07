package backend.academy.bot.Services;

import backend.academy.bot.Clients.ChatClient;
import backend.academy.bot.Configs.BotAppConfig;
import backend.academy.bot.Data.DTO.Requests.LinkUpdateRequest;
import backend.academy.bot.Data.Models.ChatCommands;
import backend.academy.bot.Handlers.BotUpdateHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotUpdateService {
    private final BotAppConfig botConfig;
    private final ChatClient chatClient;
    private final BotUpdateHandler updateHandler;
    private TelegramBot bot;

    @PostConstruct
    public void init() {
        this.bot = new TelegramBot(botConfig.telegramToken());
        configureBot();
    }

    private void configureBot() {
        try {
            setBotCommands();
            initUpdatesListener();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при конфигурации бота");
        }
    }

    private void setBotCommands() {
        try {
            BotCommand[] commands = {
                new BotCommand(ChatCommands.START.command(), ChatCommands.START.description()),
                new BotCommand(ChatCommands.HELP.command(), ChatCommands.HELP.description()),
                new BotCommand(ChatCommands.TRACK.command(), ChatCommands.TRACK.description()),
                new BotCommand(ChatCommands.UNTRACK.command(), ChatCommands.UNTRACK.description()),
                new BotCommand(ChatCommands.LIST.command(), ChatCommands.LIST.description()),
            };
            bot.execute(new SetMyCommands(commands));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void initUpdatesListener() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(updateHandler::handleUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> log.error(e.getMessage()));
    }

    public void handleIncomingUpdate(LinkUpdateRequest update) {
        if (update == null || CollectionUtils.isEmpty(update.tgChatIds())) {
            return;
        }

        String message = formatUpdateMessage(update);
        log.info(message);
        update.tgChatIds().forEach(chatId -> sendNotification(chatId, message));
    }

    private String formatUpdateMessage(LinkUpdateRequest update) {
        return String.format("""
                🔔 *Обновление ссылки*

                🔗 [%s](%s)
                📝 %s
                """,
            escapeMarkdown(update.url()),
            update.url(),
            escapeMarkdown(update.description())
        );
    }

    private String escapeMarkdown(String text) {
        return text.replace("*", "\\*")
            .replace("_", "\\_")
            .replace("`", "\\`");
    }

    private void sendNotification(Long chatId, String message) {
        try {
            bot.execute(new SendMessage(chatId, message).parseMode(ParseMode.Markdown));
        } catch (Exception e) {
            handleFailedNotification(chatId, e);
        }
    }

    private void handleFailedNotification(Long chatId, Exception e) {
        if (e.getMessage() != null && e.getMessage().contains("bot was blocked by the user")) {
            try {
                chatClient.deleteChat(chatId);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }
}
