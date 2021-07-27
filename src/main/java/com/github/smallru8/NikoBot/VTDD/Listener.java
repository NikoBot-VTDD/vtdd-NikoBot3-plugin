package com.github.smallru8.NikoBot.VTDD;

import com.github.smallru8.NikoBot.VTDD.commands.Help;
import com.github.smallru8.NikoBot.VTDD.commands.Reaction;
import com.github.smallru8.NikoBot.VTDD.commands.UserJoinLeave;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter{

	//Discord操作
	
	@Override
	public void onRoleDelete(RoleDeleteEvent rde) {//身分組刪除時檢查是否是訂閱用身分組
		UserJoinLeave.removeRolefromServer(rde);//DONE
	}
	
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent gmre){//使用者退出伺服器 要刪資料
		VTDD.SR.recv_member(gmre);//DONE
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {//退出伺服器 刪資料
		VTDD.SR.recv_serverLeave(event);//DONE
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {//加入伺服器
		VTDD.SR.recv_serverJoin(event);//DONE
	}

	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {//user取消訂閱role
		if(!event.getUser().isBot()&&VTDD.cmdChID.getVoteChannel(event.getGuild().getId()).equalsIgnoreCase(event.getChannel().getId())&&VTDD.cmdChID.getVoteMsgID(event.getGuild().getId()).equals(event.getMessageId()))
			Reaction.removeRole(event);//DONE
	}
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {//user訂閱role
		if(!event.getUser().isBot()&&VTDD.cmdChID.getVoteChannel(event.getGuild().getId()).equalsIgnoreCase(event.getChannel().getId())&&VTDD.cmdChID.getVoteMsgID(event.getGuild().getId()).equals(event.getMessageId())) {
			Reaction.addRole(event);
		}
	}
	
	//指令操作
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent gmre) {//ADMIN指令
		if(!gmre.getAuthor().isBot()) {
			VTDD.SR.recv_msg(gmre);//admin command
			Help.recv_msg(gmre);//Help
		}
	}
	
}
