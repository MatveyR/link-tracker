package backend.academy.bot.Handlers;

import backend.academy.bot.HumanMessages.HumanMessages;
import backend.academy.bot.Models.UserFSM;
import backend.academy.bot.Models.UserStep;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BotUpdateHandler {
    private final TelegramBot bot;
    private final Map<Long, UserFSM> userFSMs = new HashMap<>();
    private final UserFSMHandler userFSMHandler = new UserFSMHandler();

    public BotUpdateHandler(TelegramBot bot) {
        this.bot = bot;
    }

    public void handleUpdate(final Update update) {
        if (update.callbackQuery() != null) {
            handleCallbackQuery(update.callbackQuery());
        }
        if (update.message() == null) {
            return;
        }
        if (update.message().text() == null) {
            handleSendError(update);
        }

        final String[] messageText = update.message().text().trim().split(" ");

        if (userFSMs.containsKey(update.message().from().id())) {
            UserFSM fsm = userFSMs.get(update.message().from().id());
            handleFMSScenario(update.message().from().id(), fsm, messageText);
            return;
        }

        switch (messageText[0]) {
            case "/start":
                handleSayHello(update);
                break;
            case "/help":
                handleHelp(update);
                break;
            case "/track":
                handleStartTrackScenario(update);
                break;
            default:
                handleSendError(update);
                break;
        }
    }

    private void handleSayHello(final Update update) {
        bot.execute(new SendMessage(update.message().from().id(), HumanMessages.HELLO.toString()));
    }

    private void handleSendError(final Update update) {
        bot.execute(new SendMessage(update.message().from().id(), HumanMessages.ERROR.toString()));
    }

    private void handleHelp(final Update update) {
        bot.execute(new SendMessage(update.message().from().id(), HumanMessages.HELP.toString()));
    }

    private void handleStartTrackScenario(final Update update) {
        String URLRegex = "^(https?|ftp)://[^\\s/$.?#].\\S*$";

        final String[] messageText = update.message().text().trim().split(" ");
        if (messageText.length < 2 || !messageText[1].matches(URLRegex)) {
            bot.execute(new SendMessage(update.message().from().id(), HumanMessages.TRACK_LINK_ERROR.toString()));
            return;
        }

        UserFSM userFSM = new UserFSM();
        userFSMs.put(update.message().from().id(), userFSM);

        handleFMSScenario(update.message().from().id(), userFSM, messageText);
    }

    private void handleFMSScenario(final long chatId, UserFSM userFSM, String[] messageText) {
        String response = userFSMHandler.handleInput(messageText, userFSM);

        if (userFSM.step() != UserStep.COMPLETED) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("Пропустить");
            inlineKeyboardButton.setCallbackData("skip");
            inlineKeyboardMarkup.addRow(inlineKeyboardButton);

            bot.execute(
                new SendMessage(chatId, response)
                    .replyMarkup(inlineKeyboardMarkup)
            );
        } else {
            bot.execute(new SendMessage(chatId, response));
            userFSMs.remove(chatId);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.from().id();
        if (Objects.equals(callbackQuery.data(), "skip") && userFSMs.containsKey(chatId)) {
            UserFSM userFSM = userFSMs.get(chatId);
            String[] input = {callbackQuery.data()};
            handleFMSScenario(chatId, userFSM, input);
        } else {
            bot.execute(new SendMessage(chatId, HumanMessages.ERROR.toString()));
        }
        bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
    }
}
