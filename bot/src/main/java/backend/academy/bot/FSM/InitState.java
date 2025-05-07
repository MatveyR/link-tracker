package backend.academy.bot.FSM;

import backend.academy.bot.Clients.ChatClient;
import backend.academy.bot.Clients.LinkClient;
import backend.academy.bot.Data.Models.UserFSM;
import backend.academy.bot.Data.Models.UserStep;
import backend.academy.bot.HumanMessages.HumanMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class InitState implements FSMState {
    private static final String VALID_URL_REGEX = "^(https?|ftp)://[^\\s/$.?#].\\S*$";
    private final ChatClient chatClient;
    private final LinkClient linkClient;

    @Override
    public String handleInput(String[] input, UserFSM fsm, UserFSMContext context) {
        try {
            chatClient.registerChat(fsm.chatId());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if (input.length < 2 || !input[1].matches(VALID_URL_REGEX)) {
            return HumanMessages.TRACK_LINK_ERROR.toString();
        }

        fsm.link(input[1]);
        fsm.step(UserStep.TAGS);
        context.setState(new TagsState(linkClient));
        return HumanMessages.ENTER_LINK_TAGS.toString();
    }
}
