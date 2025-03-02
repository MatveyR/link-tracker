package backend.academy.bot.Handlers;

import backend.academy.bot.HumanMessages.HumanMessages;
import backend.academy.bot.Models.UserFSM;
import backend.academy.bot.Models.UserStep;

public class UserFSMHandler {
    public String handleInput(String[] input, UserFSM fsm) {
        switch (fsm.step()) {
            case UserStep.INIT:
                fsm.step(UserStep.TAGS);
                fsm.link(input[1]);
                return HumanMessages.ENTER_LINK_TAGS.toString();
            case UserStep.TAGS:
                fsm.step(UserStep.FILTERS);
                if (!input[0].equals("skip")) {
                    fsm.tags(input);
                }
                return HumanMessages.ENTER_LINK_FILTERS.toString();
            case UserStep.FILTERS:
                fsm.step(UserStep.COMPLETED);
                if (!input[0].equals("skip")) {
                    fsm.filters(input);
                }
                return HumanMessages.TRACK_LINK_COMPLETED.toString();
            default:
                return HumanMessages.TRACK_LINK_COMPLETED.toString();
        }
    }
}
