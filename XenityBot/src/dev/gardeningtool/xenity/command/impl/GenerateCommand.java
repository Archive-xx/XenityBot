package dev.gardeningtool.xenity.command.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.gardeningtool.xenity.command.ICommand;
import dev.gardeningtool.xenity.database.DatabaseConnection;
import dev.gardeningtool.xenity.stock.StockManager;
import dev.gardeningtool.xenity.util.MessageUtil;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class GenerateCommand implements ICommand {

    private DatabaseConnection databaseConnection;
    private StockManager stockManager;
    private static final String SYNTAX = "Invalid usage! Try generate (account type)";
    private Cache<Long, Long> lastCommandTimes;

    public GenerateCommand(DatabaseConnection databaseConnection, StockManager stockManager) {
        this.databaseConnection = databaseConnection;
        this.stockManager = stockManager;
        lastCommandTimes = CacheBuilder.newBuilder().concurrencyLevel(4).expireAfterAccess(5, TimeUnit.MINUTES).build();
    }

    @Override
    public String getName() {
        return "generate";
    }

    @Override
    public String getDescription() {
        return "Generate an account";
    }

    @Override
    public void onCommand(String[] args, User user, TextChannel channel) {
        try {
            if (!databaseConnection.hasActiveSubscription(user.getIdLong())) {
                channel.sendMessage(MessageUtil.formattedMessage(new Color(75, 10, 166), "Error", null, "You don't have an active subscription!")).submit();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (args.length != 2) {
            channel.sendMessage(MessageUtil.formattedMessage(new Color(75, 10, 166), "Error", null, SYNTAX)).submit();
            return;
        }
        long userId = user.getIdLong();
        long time = System.currentTimeMillis();
        StockManager.AccountType accountType = StockManager.AccountType.getTypeFromName(args[1]);
        boolean isAllowedToGenerate = !lastCommandTimes.asMap().containsKey(userId) || lastCommandTimes.asMap().get(userId) < System.currentTimeMillis() - (1000 * 120);
        if (!isAllowedToGenerate) {
            channel.sendMessage(MessageUtil.formattedMessage(new Color(75, 10, 166), "Error", null, "You are on cooldown! You have to wait 2 minutes from the\nlast time you executed this command.")).submit();
            return;
        }
        lastCommandTimes.put(userId, time);
        user.openPrivateChannel().queue((privateChannel) -> {
            String account = stockManager.getAlt(accountType);
            if (account == null) {
                channel.sendMessage(MessageUtil.formattedMessage(new Color(75, 10, 166), "Error", null, "We are currently out of stock of " + accountType.getName() + " accounts.")).submit();
                return;
            }
            privateChannel.sendMessage(MessageUtil.formattedMessage(new Color(194, 17, 106), null, null, "**Your generated " + accountType.getName() + " account**:\n" + account)).submit();
            channel.sendMessage(MessageUtil.formattedMessage(new Color(194, 17, 106), null, null, "The generated account has been sent to you in DMs!")).submit();
        });
    }
}
