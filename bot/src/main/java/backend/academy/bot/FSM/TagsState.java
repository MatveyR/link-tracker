package backend.academy.bot.FSM;

import backend.academy.bot.Clients.LinkClient;
import backend.academy.bot.Data.Models.UserFSM;
import backend.academy.bot.Data.Models.UserStep;
import backend.academy.bot.Handlers.BotUpdateHandler;
import backend.academy.bot.HumanMessages.HumanMessages;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TagsState implements FSMState {
    private final LinkClient linkClient;

    @Override
    public String handleInput(String[] input, UserFSM fsm, UserFSMContext context) {
        if (!input[0].equals(BotUpdateHandler.SKIP_CALLBACK_DATA)) {
            fsm.tags(List.of(input));
        }
        context.setState(new FiltersState(linkClient));
        fsm.step(UserStep.FILTERS);
        return HumanMessages.ENTER_LINK_FILTERS.toString();
    }
}
