package com.github.smallru8.NikoBot.VTDD;

import com.github.smallru8.NikoBot.VTDD.commands.Help;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
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
	
	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {//使用者role被刪除
		String serverID = event.getGuild().getId();
		for(Role r:event.getRoles()) {
			String channelNickname = VTDD.vtdd.getChannelNicknameByTagID(serverID, r.getId());
			if(channelNickname!=null) {
				VTDD.vtdd.updateVerifyStatusREF(event.getUser().getId(), channelNickname, -1);
			}
		}
	}
	
	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {//使用者被新增role
		String serverID = event.getGuild().getId();
		for(Role r:event.getRoles()) {
			String channelNickname = VTDD.vtdd.getChannelNicknameByTagID(serverID, r.getId());
			if(channelNickname!=null) {
				VTDD.vtdd.updateVerifyStatusREF(event.getUser().getId(), channelNickname, 1);
			}
		}
	}
	
	//指令操作
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent gmre) {//加入退出群組等等USER指令
		if(!gmre.getAuthor().isBot()) {
			if(gmre.getChannel().getId().equalsIgnoreCase(VTDD.cmdChID.getChID(gmre.getGuild().getId())))//在指定頻道才處理
				VTDD.UR.recv_msg(gmre);//user command
			
			VTDD.SR.recv_msg(gmre);//admin command
			Help.recv_msg(gmre);//Help
		}
	}
	
}
