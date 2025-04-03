package backend.academy.bot.Handlers;

import backend.academy.bot.Clients.LinkClient;
import backend.academy.bot.Data.DTO.Requests.RemoveLinkRequest;
import backend.academy.bot.Data.DTO.Responses.ListLinksResponse;
import backend.academy.bot.Data.Models.UserFSM;
import backend.academy.bot.Data.Models.UserStep;
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

@RequiredArgsConstructor
public class BotUpdateHandler {
    private final TelegramBot bot;
    private final UserFSMHandler fsmHandler;
    private final LinkClient linkClient;

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

        switch (parts[0]) {
            case "/start" -> handleStart(chatId);
            case "/help" -> handleHelp(chatId);
            case "/track" -> handleTrack(chatId, parts);
            case "/untrack" -> handleUntrack(chatId, parts);
            case "/list" -> handleList(chatId);
            default -> sendError(chatId);
        }
    }

    private void handleFsmState(Long chatId, String[] input) {
        UserFSM fsm = userFSMs.get(chatId);
        try {
            String response = fsmHandler.handleInput(input, fsm);

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

    private void handleTrack(Long chatId, String[] parts) {
        if (parts.length < 2) {
            sendMessage(chatId, HumanMessages.TRACK_LINK_ERROR.toString());
            return;
        }

        UserFSM fsm = UserFSM.builder()
            .chatId(chatId)
            .build();

        userFSMs.put(chatId, fsm);
        handleFsmState(chatId, parts);
    }

    private void handleUntrack(Long chatId, String[] parts) {
        if (parts.length < 2) {
            sendMessage(chatId, HumanMessages.UNTRACK_ERROR.toString());
            return;
        }

        try {
            linkClient.removeLink(chatId, new RemoveLinkRequest(parts[1]));
            sendMessage(chatId, HumanMessages.UNTRACK_COMPLETED.toString());
        } catch (Exception e) {
            sendMessage(chatId, HumanMessages.UNTRACK_ERROR.toString());
        }
    }

    private void handleList(Long chatId) {
        try {
            ListLinksResponse response = linkClient.getAllLinks(chatId);
            String message = formatLinksList(response);
            sendMessage(chatId, message);
        } catch (Exception e) {
            sendMessage(chatId, HumanMessages.LIST_ERROR.toString());
        }
    }

    private String formatLinksList(ListLinksResponse response) {
        if (response.links().isEmpty()) {
            return HumanMessages.NO_LINKS.toString();
        }

        StringBuilder sb = new StringBuilder(HumanMessages.LINKS_HEADER.toString());
        response.links().forEach(link ->
            sb.append("\n").append(link.url())
        );
        return sb.toString();
    }

    private void handleCallback(CallbackQuery callback) {
        if ("skip".equals(callback.data()) && userFSMs.containsKey(callback.from().id())) {
            handleFsmState(callback.from().id(), new String[]{"skip"});
        }
        answerCallback(callback.id());
    }

    private void sendMessage(Long chatId, String text) {
        bot.execute(new SendMessage(chatId, text));
    }

    private void sendMessageWithSkipButton(Long chatId, String text) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
            new InlineKeyboardButton("Пропустить").callbackData("skip")
        );
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    private void sendError(Long chatId) {
        sendMessage(chatId, HumanMessages.ERROR.toString());
    }

    private void answerCallback(String callbackId) {
        bot.execute(new AnswerCallbackQuery(callbackId));
    }

    private void handleStart(Long chatId) {
        sendMessage(chatId, HumanMessages.HELLO.toString());
    }

    private void handleHelp(Long chatId) {
        sendMessage(chatId, HumanMessages.HELP.toString());
    }
}
