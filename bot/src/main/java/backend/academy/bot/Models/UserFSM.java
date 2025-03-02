package backend.academy.bot.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFSM {
    private UserStep step = UserStep.INIT;
    private String link;
    private String[] tags = {};
    private String[] filters = {};
}
