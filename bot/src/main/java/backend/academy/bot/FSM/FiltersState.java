package backend.academy.bot.FSM;

import backend.academy.bot.Clients.LinkClient;
import backend.academy.bot.Data.DTO.Requests.AddLinkRequest;
import backend.academy.bot.Data.Models.UserFSM;
import backend.academy.bot.Data.Models.UserStep;
import backend.academy.bot.Handlers.BotUpdateHandler;
import backend.academy.bot.HumanMessages.HumanMessages;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class FiltersState implements FSMState {
    private final LinkClient linkClient;

    @Override
    public String handleInput(String[] input, UserFSM fsm, UserFSMContext context) {
        if (!input[0].equals(BotUpdateHandler.SKIP_CALLBACK_DATA)) {
            fsm.filters(List.of(input));
        }

        AddLinkRequest request = new AddLinkRequest(
            fsm.link(),
            fsm.tags(),
            fsm.filters()
        );
        linkClient.addLink(fsm.chatId(), request);

        context.setState(new CompletedState());
        fsm.step(UserStep.COMPLETED);
        return HumanMessages.TRACK_LINK_COMPLETED.toString();
    }
}
