package backend.academy.bot.Handlers;

import backend.academy.bot.Commands.BotCommand;
import backend.academy.bot.Commands.CommandFactory;
import backend.academy.bot.Data.DTO.Responses.ListLinksResponse;
import backend.academy.bot.Data.Models.UserFSM;
import backend.academy.bot.Data.Models.UserStep;
import backend.academy.bot.FSM.UserFSMContext;
import backend.academy.bot.HumanMessages.HumanMessages;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BotUpdateHandler {
    private final TelegramBot bot;
    private final UserFSMContext fsmContext;
    private final CommandFactory commandFactory;

    public static final String SKIP_CALLBACK_DATA = "skip";

    @Getter
    private final Map<Long, UserFSM> userFSMs = new ConcurrentHashMap<>();

    public void handleUpdate(Update update) {
        if (update == null) return;

        try {
            if (update.callbackQuery() != null) {
                handleCallback(update.callbackQuery());
                return;
            }

            if (update.message() == null || update.message().text() == null) {
                sendError(update.message().chat().id());
                return;
            }

            handleMessage(update.message());
        } catch (Exception e) {
            if (update.message() != null) {
                sendError(update.message().chat().id());
            }
        }
    }

    private void handleMessage(Message message) {
        Long chatId = message.chat().id();
        String[] parts = message.text().trim().split(" ");

        if (userFSMs.containsKey(chatId)) {
            handleFsmState(chatId, parts);
            return;
        }

        String commandName = parts[0];
        log.info(commandName);
        BotCommand command = commandFactory.getCommand(commandName);
        command.execute(chatId, parts, this);
    }

    public void handleFsmState(Long chatId, String[] input) {
        UserFSM fsm = userFSMs.get(chatId);
        try {
            String response = fsmContext.handleInput(input, fsm);

            if (fsm.step() == UserStep.COMPLETED) {
                userFSMs.remove(chatId);
                sendMessage(chatId, response);
            } else {
                sendMessageWithSkipButton(chatId, response);
            }
        } catch (Exception e) {
            sendMessage(chatId, HumanMessages.ERROR.toString());
        }
    }

    public String formatLinksList(ListLinksResponse response) {
        if (response.links().isEmpty()) {
            return HumanMessages.NO_LINKS.toString();
        }

        StringBuilder sb = new StringBuilder(HumanMessages.LINKS_HEADER.toString());
        response.links().forEach(link -> sb.append("\n").append(link.url()));
        return sb.toString();
    }

    private void handleCallback(CallbackQuery callback) {
        if (SKIP_CALLBACK_DATA.equals(callback.data())
                && userFSMs.containsKey(callback.from().id())) {
            handleFsmState(callback.from().id(), new String[] {SKIP_CALLBACK_DATA});
        }
        answerCallback(callback.id());
    }

    public void sendMessage(Long chatId, String text) {
        bot.execute(new SendMessage(chatId, text));
    }

    private void sendMessageWithSkipButton(Long chatId, String text) {
        InlineKeyboardMarkup keyboard =
                new InlineKeyboardMarkup(new InlineKeyboardButton("Пропустить").callbackData(SKIP_CALLBACK_DATA));
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    public void sendError(Long chatId) {
        sendMessage(chatId, HumanMessages.ERROR.toString());
    }

    private void answerCallback(String callbackId) {
        bot.execute(new AnswerCallbackQuery(callbackId));
    }
}
