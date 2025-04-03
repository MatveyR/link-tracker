package backend.academy.bot.Data.Models;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserFSM {
    private Long chatId;
    private String link;
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    @Builder.Default
    private List<String> filters = new ArrayList<>();
    @Builder.Default
    private UserStep step = UserStep.INIT;
}
