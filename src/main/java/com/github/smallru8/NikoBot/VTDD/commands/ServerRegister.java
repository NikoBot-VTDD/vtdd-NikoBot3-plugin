package com.github.smallru8.NikoBot.VTDD.commands;

import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.Embed;
import com.github.smallru8.NikoBot.StdOutput;
import com.github.smallru8.NikoBot.VTDD.SQL.VTDData;
import com.github.smallru8.util.RegularExpression;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;

public class ServerRegister {

	private VTDData sql;
	
	public ServerRegister(VTDData sql) {
		this.sql = sql;
	}
	
	/**
	 * bot離開伺服器 刪檔
	 * @param gle
	 */
	public void recv_serverLeave(GuildLeaveEvent gle) {
		String serverID = gle.getGuild().getId();
		sql.delTag(serverID);//刪除伺服器的Tag binding
		String[] users = sql.getMAP(serverID);//這個伺服器有訂閱記錄的人
		sql.delMAPByServer(serverID);//刪掉這個server的MAP
		sql.delDCServer(serverID);//刪除伺服器
		
		for(String user:users) {
			if(!sql.isMAPExistByUser(user))//沒有在其他伺服器有訂閱資料
				sql.delVerifyStatusByUser(user);//清除
		}
		
	}
	
	/**
	 * bot加入伺服器 建檔
	 * @param gje
	 */
	public void recv_serverJoin(GuildJoinEvent gje) {
		sql.addDCServer(gje.getGuild().getId());
	}
	
	/**
	 * 有人退出伺服器
	 * @param e
	 */
	public void recv_member(GuildMemberRemoveEvent e) {
		String memberId = e.getMember().getId();
		if(sql.isUserExist(memberId)) {//有註冊在REGUSER才處理
			sql.delMAP(e.getGuild().getId(), memberId);//刪MAP
			
			if(!sql.isMAPExistByUser(memberId))//沒有在其他伺服器有訂閱資料
				sql.delVerifyStatusByUser(memberId);//清除
		}
	}
	
	/**
	 * role從伺服器上被刪除
	 * @param e
	 */
	public void recv_role(RoleDeleteEvent e) {
		Role r = e.getRole();
		if(sql.isTagExistById(e.getGuild().getId(), r.getId()));
			sql.delTagById(e.getGuild().getId(), r.getId());
	}
	
	//TODO 使用者被移除role
	
	/**
	 * 指令接收
	 * @param e
	 */
	public void recv_msg(GuildMessageReceivedEvent e) {
		//管理員
		Message msg = e.getMessage();
		String tmp;
		String cmdRaw = msg.getContentRaw();
		if(cmdRaw.startsWith("/vtdd ")&&Core.ADMINS.isAdmin(e.getGuild().getId(), e.getAuthor().getId())) {
			List<String> cmd = Arrays.asList(cmdRaw.split(" "));
			Iterator<String> cmdIt = cmd.iterator();
			cmdIt.next();
			if(!cmdIt.hasNext()) {
				//cmdIt.remove();
				msg.getChannel().sendMessage("Command error.").queue();
			}else {
				tmp = cmdIt.next();
				//CH
				if(tmp.equalsIgnoreCase("ch")) {
					CMDProcess_ch(cmdIt,msg);
				}
			}
		}
	}
	
	private void CMDProcess_ch(Iterator<String> cmdIt,Message msg) {
		try {
			String tmp = cmdIt.next();
			//Show all channel
			//Show this server's channel
			if(tmp.equalsIgnoreCase("ls")) {
				String[] channelLs = sql.getChannel();
				String sum = "";
				for(int i=0;i<channelLs.length;i++) {
					sum+=":small_blue_diamond: ";
					sum+=channelLs[i]+"\n";
				}
				Embed.EmbedSender(Color.PINK, msg.getChannel(), ":page_facing_up: Available channel", sum);
				
				channelLs = sql.getServerVTNickname(msg.getGuild().getId());
				sum = "";
				for(int i=0;i<channelLs.length;i++) {
					sum+=":small_orange_diamond: ";
					sum+=channelLs[i]+"\n";
				}
				Embed.EmbedSender(Color.PINK, msg.getChannel(), ":bookmark_tabs: Subscription channel", sum);
			}
			else if(tmp.length() <= 20 && sql.isChannelExist(tmp)){//channel is exist
				String channelNickname = tmp;
				
				//add/update tag 也要更新tag狀態
				if(cmdIt.next().equalsIgnoreCase("set") && cmdIt.next().equalsIgnoreCase("tag")) {
					String previousTagID = sql.getTagID(msg.getGuild().getId(), channelNickname);
					tmp = cmdIt.next();//raw TagID
					String rawTagID = tmp;
					tmp = tmp.replace("<", "");
					tmp = tmp.replace("&", "");
					tmp = tmp.replace("@", "");
					tmp = tmp.replace(">", "");
					String tagID = tmp;
					
					//如果有舊的身分組刪除前先記錄起來
					Role r = null;
					if(previousTagID!=null)
						r = msg.getGuild().getRoleById(previousTagID);
					List<Member> members = null;
					if(r!=null)
						members = msg.getGuild().getMembersWithRoles(r);
					
					//砍舊tag
					if(previousTagID!=null&&!tagID.equals(previousTagID)&&r!=null) {
						members = msg.getGuild().getMembersWithRoles(r);
						for(Member m:members){
							msg.getGuild().removeRoleFromMember(m, r).queue();
						}
					}

					
					if(tagID.length()<=20 && RegularExpression.isDigitOnly(tagID)) {
						sql.addTag(msg.getGuild().getId(), channelNickname, tagID);
						StdOutput.infoPrintln("Server: "+msg.getGuild().getId()+", Add TAG: "+channelNickname+"="+tagID);
						msg.getChannel().sendMessage("Add binding "+channelNickname+":"+rawTagID).queue();
					}
					
					//原本有舊的身分組要加新的回去
					//加新tag
					if(previousTagID!=null&&!tagID.equals(previousTagID)) {
						r = msg.getGuild().getRoleById(tagID);//新的身分組
						for(Member m:members){
							msg.getGuild().addRoleToMember(m, r).queue();
						}
					}

				}
				//remove tag
				else if(cmdIt.next().equalsIgnoreCase("remove")) {
					sql.delTag(msg.getGuild().getId(), channelNickname);
					StdOutput.infoPrintln("Server: "+msg.getGuild().getId()+", Remove TAG: "+channelNickname);
					msg.getChannel().sendMessage("Remove "+channelNickname+"'s tag binging.").queue();
					msg.getChannel().sendMessage("You can remove the tag form your server.").queue();
				}
				
				
			}
		}catch(NoSuchElementException nsee) {
			//cmdIt.remove();
			msg.getChannel().sendMessage("Command error.").queue();
		}
		//cmdIt.remove();
	}
	
}
