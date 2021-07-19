package com.github.smallru8.NikoBot.VTDD;

import com.github.smallru8.NikoBot.VTDD.commands.Help;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter{

	//Discord操作
	
	@Override
	public void onRoleDelete(RoleDeleteEvent rde) {//身分組刪除時檢查是否是訂閱用身分組
		VTDD.SR.recv_role(rde);
	}
	
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent gmre){//使用者退出伺服器 要刪資料
		VTDD.SR.recv_member(gmre);
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {//退出伺服器 刪資料
		VTDD.SR.recv_serverLeave(event);
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {//加入伺服器
		VTDD.SR.recv_serverJoin(event);
	}
	
	//指令操作
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent gmre) {//加入退出群組等等USER指令
		if(!gmre.getAuthor().isBot()) {
			VTDD.UR.recv_msg(gmre);//user command
			VTDD.SR.recv_msg(gmre);//admin command
			Help.recv_msg(gmre);//Help
		}
	}
	
}
