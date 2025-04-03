package backend.academy.bot.Handlers;

import backend.academy.bot.Clients.ChatClient;
import backend.academy.bot.Clients.LinkClient;
import backend.academy.bot.Data.DTO.Requests.AddLinkRequest;
import backend.academy.bot.HumanMessages.HumanMessages;
import backend.academy.bot.Data.Models.UserFSM;
import backend.academy.bot.Data.Models.UserStep;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class UserFSMHandler {
    private final ChatClient scrapperClient;
    private final LinkClient linkClient;

    public String handleInput(String[] input, UserFSM fsm) {
        try {
            return switch (fsm.step()) {
                case INIT -> handleInitStep(input, fsm);
                case TAGS -> handleTagsStep(input, fsm);
                case FILTERS -> handleFiltersStep(input, fsm);
                case COMPLETED -> HumanMessages.TRACK_LINK_COMPLETED.toString();
            };
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage().contains("Чат уже зарегистрирован")
                ? handleInitStep(input, fsm)
                : HumanMessages.ERROR.toString();
        }
    }

    private String handleInitStep(String[] input, UserFSM fsm) {
        try {
            registerUserIfNeeded(fsm.chatId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (input.length < 2 || !isValidUrl(input[1])) {
            return HumanMessages.TRACK_LINK_ERROR.toString();
        }

        fsm.step(UserStep.TAGS);
        fsm.link(input[1]);
        return HumanMessages.ENTER_LINK_TAGS.toString();
    }

    private String handleTagsStep(String[] input, UserFSM fsm) {
        if (!input[0].equals("skip")) {
            fsm.tags(List.of(input));
        }
        fsm.step(UserStep.FILTERS);
        return HumanMessages.ENTER_LINK_FILTERS.toString();
    }

    private String handleFiltersStep(String[] input, UserFSM fsm) {
        if (!input[0].equals("skip")) {
            fsm.filters(List.of(input));
        }

        trackLink(fsm);
        fsm.step(UserStep.COMPLETED);
        return HumanMessages.TRACK_LINK_COMPLETED.toString();
    }

    private void registerUserIfNeeded(Long chatId) {
        try {
            scrapperClient.registerChat(chatId);
        } catch (Exception e) {
            throw e;
        }
    }

    private void trackLink(UserFSM fsm) {
        AddLinkRequest request = new AddLinkRequest(
            fsm.link(),
            fsm.tags(),
            fsm.filters()
        );
        linkClient.addLink(fsm.chatId(), request);
    }

    private boolean isValidUrl(String url) {
        return url.matches("^(https?|ftp)://[^\\s/$.?#].\\S*$");
    }
}
