package dev.gardeningtool.xenity.listener;

import dev.gardeningtool.xenity.Xenity;
import dev.gardeningtool.xenity.config.Config;
import dev.gardeningtool.xenity.event.Event;
import dev.gardeningtool.xenity.event.Listener;

import dev.gardeningtool.xenity.event.impl.ServerMessageEvent;

/**
 *
 * @author Gardening_Tool
 */
public class CommandListener implements Listener {

    private Xenity xenity;
    private String prefix;

    public CommandListener(Xenity xenity, Config config) {
        this.xenity = xenity;
        this.prefix = config.getBotPrefix();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof ServerMessageEvent) {
            ServerMessageEvent sm = (ServerMessageEvent) event;
            String message = sm.getMessage();
            if (message.startsWith(prefix)) {
                String cmd = message.replace(prefix, "");
                String[] args = cmd.contains(" ") ? cmd.split(" ") : new String[] {};
                xenity.getCommandManager().handleCommand(cmd.contains(" ") ? cmd.split(" ")[0] : cmd, args, sm.getSender(), sm.getChannel());
            }
        }
    }
}
