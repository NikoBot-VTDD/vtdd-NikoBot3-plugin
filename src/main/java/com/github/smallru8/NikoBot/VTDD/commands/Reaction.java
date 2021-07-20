package com.github.smallru8.NikoBot.VTDD.commands;

import com.github.smallru8.NikoBot.VTDD.VTDD;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public class Reaction {

	//收回reaction 取消訂閱
	public static void removeRole(MessageReactionRemoveEvent event) {
		String channelNickname = VTDD.vtdd.getChannelbyEmoji(event.getReactionEmote().getEmoji());
		if(channelNickname!=null) {
			Guild g = event.getGuild();
			g.removeRoleFromMember(event.getUserId(), g.getRoleById(VTDD.vtdd.getTagID(g.getId(), channelNickname))).queue();
		}
	}
	
	//訂閱 reaction
	public static void addRole(MessageReactionAddEvent event) {
		String channelNickname = VTDD.vtdd.getChannelbyEmoji(event.getReactionEmote().getEmoji());
		if(channelNickname!=null) {
			Guild g = event.getGuild();
			g.addRoleToMember(event.getUserId(), g.getRoleById(VTDD.vtdd.getTagID(g.getId(), channelNickname))).queue();
		}
	}
	
}
