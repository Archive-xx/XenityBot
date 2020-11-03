package dev.gardeningtool.xenity.event.listener;

import dev.gardeningtool.xenity.Xenity;
import dev.gardeningtool.xenity.event.bus.EventBus;
import dev.gardeningtool.xenity.event.impl.MemberJoinEvent;
import dev.gardeningtool.xenity.event.impl.MemberQuitEvent;
import dev.gardeningtool.xenity.event.impl.ServerMessageEvent;

import lombok.AllArgsConstructor;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 *
 * @author Gardening_Tool
 */
@AllArgsConstructor
public class EventListener extends ListenerAdapter {

	private EventBus eventBus;

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try {
			eventBus.handleEvent(new ServerMessageEvent(event.getAuthor(), event.getMessage().getContentRaw(), (TextChannel) event.getChannel()));
		} catch (Exception ignored) {}
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		eventBus.handleEvent(new MemberJoinEvent(event.getUser(), event.getGuild()));
	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		eventBus.handleEvent(new MemberQuitEvent(event.getUser(), event.getGuild()));
	}
}
