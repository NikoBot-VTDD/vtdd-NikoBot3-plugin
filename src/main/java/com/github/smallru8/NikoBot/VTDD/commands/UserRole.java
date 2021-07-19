package com.github.smallru8.NikoBot.VTDD.commands;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.github.smallru8.NikoBot.VTDD.VTDD;
import com.github.smallru8.NikoBot.VTDD.SQL.VTDData;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UserRole {

	private VTDData sql;
	
	public UserRole(VTDData sql) {
		this.sql = sql;
	}
	
	/**
	 * User輸入指令處理
	 * @param e
	 */
	public void recv_msg(GuildMessageReceivedEvent e) {
		Message msg = e.getMessage();
		String tmp;
		String cmdRaw = msg.getContentRaw();
		if(cmdRaw.startsWith("/vtdd ")) {
			List<String> cmd = Arrays.asList(cmdRaw.split(" "));
			Iterator<String> cmdIt = cmd.iterator();
			cmdIt.next();
			if(!cmdIt.hasNext()) {
				//cmdIt.remove();
				msg.getChannel().sendMessage("Command error.").queue();
			}else {
				tmp = cmdIt.next();
				//join
				if(tmp.equalsIgnoreCase("join")) {// /vtdd join
					CMDProcess_join(cmdIt,msg);
				}
				//leave
				else if(tmp.equalsIgnoreCase("leave")) {// /vtdd leave
					CMDProcess_leave(cmdIt,msg);
				}
			}
		}
	}
	
	/**
	 * 加身分組
	 * @param cmdIt
	 * @param msg
	 */
	private void CMDProcess_join(Iterator<String> cmdIt,Message msg) {
		try {
			Guild g = msg.getGuild();
			String tmp = cmdIt.next();
			String serverID = g.getId();
			if(sql.isChannelExist(tmp)&&sql.isTagExist(g.getId(), tmp)) {//頻道存在&&伺服器有訂閱
				String userID = msg.getAuthor().getId();
				String roleID = sql.getTagID(serverID, tmp);
				
				if(!g.getMembersWithRoles(g.getRoleById(roleID)).contains(g.getMemberById(userID))) {//User沒訂閱過這個頻道
					if(!sql.isMAPExist(serverID, userID))//沒有MAP紀錄
						sql.addMAP(serverID, userID);
					
					if(VTDD.conf.ytapi.verify(sql.getRefTokenById(userID), sql.getChannelVideoId(tmp))) {//驗證
						g.addRoleToMember(userID, g.getRoleById(roleID)).queue();//驗證通過給群組
						sql.updateVerifyStatus(userID, tmp, true);//更新驗證狀態
						
						msg.getChannel().sendMessage("<@"+userID+"> subscribe channel: "+tmp+", :white_check_mark: Verified!!").queue();
					}else {
						msg.getChannel().sendMessage("<@"+userID+"> subscribe channel: "+tmp+", :x: verification failed!!").queue();
					}
				}
			}else {
				msg.getChannel().sendMessage("Channel not found or this server wasn't setting this channel's role.").queue();
			}
			
			
			//cmdIt.remove();
		}catch(NoSuchElementException nsee) {
			//cmdIt.remove();
			msg.getChannel().sendMessage("Command error. usage: /vtdd join <channel nickname>").queue();
		}
	}
	
	/**
	 * 退身分組
	 * @param cmdIt
	 * @param msg
	 */
	private void CMDProcess_leave(Iterator<String> cmdIt,Message msg) {
		try {
			Guild g = msg.getGuild();
			String tmp = cmdIt.next();
			String serverID = g.getId();
			
			if(sql.isChannelExist(tmp)&&sql.isTagExist(g.getId(), tmp)) {//頻道存在&&伺服器有訂閱
				String userID = msg.getAuthor().getId();
				String roleID = sql.getTagID(serverID, tmp);
				
				g.removeRoleFromMember(userID, g.getRoleById(roleID)).queue();//從使用者身上移除role
				
			}else {
				msg.getChannel().sendMessage("Channel not found or this server wasn't setting this channel's role.").queue();
			}
			
			//cmdIt.remove();
		}catch(NoSuchElementException nsee) {
			//cmdIt.remove();
			msg.getChannel().sendMessage("Command error. usage: /vtdd join <channel nickname>").queue();
		}
	}
}
