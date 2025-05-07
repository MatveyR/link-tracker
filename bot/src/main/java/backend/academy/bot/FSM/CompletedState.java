package backend.academy.bot.FSM;

import backend.academy.bot.Data.Models.UserFSM;
import backend.academy.bot.HumanMessages.HumanMessages;

public class CompletedState implements FSMState {
    @Override
    public String handleInput(String[] input, UserFSM fsm, UserFSMContext context) {
        return HumanMessages.TRACK_LINK_COMPLETED.toString();
    }
}
