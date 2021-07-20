package com.github.smallru8.NikoBot.VTDD.commands;

import java.awt.Color;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.Embed;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Help {
	
	private static String adminCmd = 
			"Show all available channel : /vtdd ch ls \n" +
			"Set channel's membership only role : /vtdd ch <name> set tag <@role> \n" +
			"Remove channel's membership only role : /vtdd ch <name> remove \n" +
			"Set command channel : /vtdd set <#channel> \n" +
			"Get role by reaction : /vtdd vote \n";
	private static String userCmd = 
			"User Command:\n" +
			"Get role : /vtdd join <name> \n" +
			"Remove role : /vtdd leave <name> \n";
	
	public static void recv_msg(GuildMessageReceivedEvent e) {
		Message msg = e.getMessage();
		if(Core.ADMINS.isAdmin(e.getGuild().getId(), msg.getAuthor().getId())&&msg.getContentRaw().startsWith("/vtdd help")) {
			Embed.EmbedSender(Color.PINK, msg.getChannel(), ":regional_indicator_m: Admin Command", adminCmd);
			Embed.EmbedSender(Color.PINK, msg.getChannel(), ":regional_indicator_u: Admin Command", userCmd);
		}
	}
	
}
