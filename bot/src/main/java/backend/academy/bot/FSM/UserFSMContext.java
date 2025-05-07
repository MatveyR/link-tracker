package backend.academy.bot.FSM;

import backend.academy.bot.Data.Models.UserFSM;

public class UserFSMContext {
    private FSMState currentState;

    public UserFSMContext(FSMState initialState) {
        this.currentState = initialState;
    }

    public void setState(FSMState newState) {
        this.currentState = newState;
    }

    public String handleInput(String[] input, UserFSM fsm) {
        return currentState.handleInput(input, fsm, this);
    }
}
