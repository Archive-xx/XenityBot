package dev.gardeningtool.xenity.event.impl;

import dev.gardeningtool.xenity.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

/**
 *
 * @author Gardening_Tool
 */
@Getter
@AllArgsConstructor
public class MemberQuitEvent extends Event {

    /**
     * The user who left the server
     */
    private User user;

    /**
     * The server which they left
     */
    private Guild server;
}
