package backend.academy.bot.FSM;

import backend.academy.bot.Data.Models.UserFSM;

public interface FSMState {
    String handleInput(String[] input, UserFSM fsm, UserFSMContext context);
}
