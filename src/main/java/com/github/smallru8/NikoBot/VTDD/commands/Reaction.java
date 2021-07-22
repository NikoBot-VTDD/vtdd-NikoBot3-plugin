package com.github.smallru8.NikoBot.VTDD.commands;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.VTDD.VTDD;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public class Reaction {

	//收回reaction 取消訂閱
	public static void removeRole(MessageReactionRemoveEvent event) {//DONE
		if(VTDD.vtdd.isUserExist(event.getUserId())) {//有註冊在REGUSER才處理
			String channelNickname = VTDD.vtdd.getChannelbyEmoji(event.getReactionEmote().getEmoji());
			
			if(channelNickname!=null) {
				UserJoinLeave.removeRolefromUser(event.getGuild().getId(), event.getUserId(), channelNickname);
			}
		}
	}
	
	//訂閱 reaction
	public static void addRole(MessageReactionAddEvent event) {//DONE
		if(VTDD.vtdd.isUserExist(event.getUserId())) {//有註冊在REGUSER才處理
			String channelNickname = VTDD.vtdd.getChannelbyEmoji(event.getReactionEmote().getEmoji());
	
			if(channelNickname!=null) {
				if(!UserJoinLeave.giveRole(event.getGuild().getId(), event.getUserId(), channelNickname)) {
					//沒通過, remove reaction
					removeReaction(event.getGuild().getId(),event.getUserId(),channelNickname);
				}
			}
		}else {//沒註冊
			event.getUser().openPrivateChannel().queue(channel -> {
				channel.sendMessage("Hello, before using VTDD service, you need to bind Discord and Youtube account here: " + VTDD.conf.verifyURL).queue();
			});
			//remove reaction
			event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
				message.removeReaction(event.getReactionEmote().getEmoji(), event.getUser()).queue();//remove reaction
			});
		}
	}
	
	/**
	 * remove someone's all vote reaction in a server
	 * @param g
	 * @param u
	 */
	public static void removeReaction(Guild g,User u) {
		g.getTextChannelById(VTDD.cmdChID.getVoteChannel(g.getId())).retrieveMessageById(VTDD.cmdChID.getVoteMsgID(g.getId())).queue(message -> {
			message.getReactions().forEach(action -> {
				action.removeReaction(u).queue();
			});
		});
	}
	
	/**
	 * remove someone's vote reaction in a server
	 * @param serverID
	 * @param userID
	 * @param channelNickname
	 */
	public static void removeReaction(String serverID,String userID,String channelNickname) {
		Core.botAPI.getGuildById(serverID).getTextChannelById(VTDD.cmdChID.getVoteChannel(serverID)).retrieveMessageById(VTDD.cmdChID.getVoteMsgID(serverID)).queue(message -> {
			message.removeReaction(VTDD.vtdd.getChannelEMOJI(channelNickname), Core.botAPI.getUserById(userID)).queue();
		});
	}
}
