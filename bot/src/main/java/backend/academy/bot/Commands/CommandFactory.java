package backend.academy.bot.Commands;

import backend.academy.bot.Clients.LinkClient;
import backend.academy.bot.Data.Models.ChatCommands;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<String, BotCommand> commands = new HashMap<>();

    public CommandFactory(LinkClient linkClient) {
        commands.put(ChatCommands.START.command(), new StartCommand());
        commands.put(ChatCommands.HELP.command(), new HelpCommand());
        commands.put(ChatCommands.TRACK.command(), new TrackCommand());
        commands.put(ChatCommands.UNTRACK.command(), new UntrackCommand(linkClient));
        commands.put(ChatCommands.LIST.command(), new ListCommand(linkClient));
    }

    public BotCommand getCommand(String commandName) {
        return commands.getOrDefault(commandName, new DefaultCommand());
    }
}
