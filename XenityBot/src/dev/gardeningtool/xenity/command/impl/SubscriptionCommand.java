package dev.gardeningtool.xenity.command.impl;

import dev.gardeningtool.xenity.command.ICommand;
import dev.gardeningtool.xenity.database.DatabaseConnection;
import dev.gardeningtool.xenity.util.MessageUtil;

import lombok.AllArgsConstructor;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.sql.SQLException;

@AllArgsConstructor
public class SubscriptionCommand implements ICommand {

    private DatabaseConnection databaseConnection;
    private static final String SYNTAX = "Improper syntax!\nTry: subscription (add/remove/check) (user) [days]";

    @Override
    public String getName() {
        return "subscription";
    }

    @Override
    public String getDescription() {
        return "Manage the subscription of a user";
    }

    @Override
    public void onCommand(String[] args, User user, TextChannel channel) {
        if (args.length < 3) {
            channel.sendMessage(MessageUtil.formattedMessage(new Color(145, 11, 1), "Error", null, SYNTAX)).submit();
            return;
        }
        String action = args[1];
        long userId = Long.parseLong(args[2]);
        switch(action) {
            case "add": {
                if (args.length != 4) {
                    channel.sendMessage(MessageUtil.formattedMessage(new Color(145, 11, 1), "Error", null, SYNTAX)).submit();
                    return;
                }
                int days = Integer.parseInt(args[3]);
                try {
                    databaseConnection.addSubscription(userId, days);
                    channel.sendMessage(MessageUtil.formattedMessage(new Color(75, 10, 166), "Success", null, "Successfully granted <@" + userId + "> a subscription for " + days + " day" + (days != 1 ? "s" : "") + "!")).submit();
                } catch (SQLException exc) {
                    channel.sendMessage(MessageUtil.formattedMessage(new Color(145, 11, 1), "Error", null, SYNTAX)).submit();
                    exc.printStackTrace();
                }
                return;
            }
            case "remove": {
                if (args.length != 3) {
                    channel.sendMessage(MessageUtil.formattedMessage(new Color(145, 11, 1), "Error", null, SYNTAX)).submit();
                    return;
                }
                try {
                    if (!databaseConnection.hasActiveSubscription(userId)) {
                        channel.sendMessage(MessageUtil.formattedMessage(new Color(145, 11, 1), "Error", null, "<@" + userId + "> does not have an active generator subscription!")).submit();
                        return;
                    }
                    databaseConnection.removeSubscription(userId);
                    channel.sendMessage(MessageUtil.formattedMessage(new Color(75, 10, 166), "Success", null, "Successfully removed <@" + userId + ">'s subscription")).submit();
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    channel.sendMessage(MessageUtil.formattedMessage(new Color(145, 11, 1), "Error", null, "An unexpected database error has occured")).submit();
                }
                return;
            }
            case "check": {
                try {
                if (!databaseConnection.hasActiveSubscription(userId)) {
                    channel.sendMessage(MessageUtil.formattedMessage(new Color(145, 11, 1), "Result", null, "<@" + userId + "> does not have an active generator subscription!")).submit();
                    return;
                }
                channel.sendMessage(MessageUtil.formattedMessage(new Color(75, 10, 166), "Result", null, "<@" + userId + "> currently has an active generator subscription!")).submit();
            } catch (SQLException exc) {
                exc.printStackTrace();
                channel.sendMessage(MessageUtil.formattedMessage(new Color(145, 11, 1), "Error", null, "An unexpected database error has occured")).submit();
            }
                return;
            }
            default: {
                channel.sendMessage(MessageUtil.formattedMessage(new Color(145, 11, 1), "Error", null, SYNTAX)).submit();
            }
        }
    }
}
