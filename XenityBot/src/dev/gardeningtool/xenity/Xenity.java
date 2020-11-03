package dev.gardeningtool.xenity;

import dev.gardeningtool.xenity.command.CommandManager;
import dev.gardeningtool.xenity.command.impl.GenerateCommand;
import dev.gardeningtool.xenity.command.impl.SubscriptionCommand;
import dev.gardeningtool.xenity.config.Config;
import dev.gardeningtool.xenity.database.DatabaseConnection;
import dev.gardeningtool.xenity.event.bus.EventBus;
import dev.gardeningtool.xenity.event.listener.EventListener;
import dev.gardeningtool.xenity.listener.CommandListener;

import dev.gardeningtool.xenity.stock.StockManager;
import lombok.Getter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

/**
 *
 * @author Gardening_Tool
 *
 * This is a Discord bot I coded for my friend's
 * alt generator server. With his permission I
 * opensourced this, feel free to have a look.
 */
@Getter
public class Xenity {

    private Config config;

    private CommandManager commandManager;

    private DatabaseConnection databaseConnection;

    private EventBus eventBus;

    private EventListener eventListener;

    private JDA jda;

    private StockManager stockManager;

    public Xenity() throws LoginException {
        config = new Config();
        databaseConnection = new DatabaseConnection(config);
        commandManager = new CommandManager();
        eventBus = new EventBus();
        stockManager = new StockManager();
        jda = new JDABuilder().setToken(config.getBotToken()).setActivity(Activity.playing(config.getBotStatus())).build();
        eventListener = new EventListener(eventBus);
        jda.addEventListener(eventListener);
        registerEventHandlers();
        registerCommands();
    }

    private void registerCommands() {
        commandManager.registerCommand(new SubscriptionCommand(databaseConnection));
        commandManager.registerCommand(new GenerateCommand(databaseConnection, stockManager));
    }

    private void registerEventHandlers() {
        eventBus.subscribe(new CommandListener(this, config));
    }
}
