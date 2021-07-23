package com.github.smallru8.NikoBot.VTDD.commands;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.VTDD.VTDD;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;

public class UserJoinLeave {

	/**
	 * give role to user and verify
	 * @param serverID
	 * @param discordID
	 * @param channelNickname
	 * @return
	 */
	public static boolean giveRole(String serverID,String discordID,String channelNickname) {//DONE
		
		if(VTDD.vtdd.isChannelExist(channelNickname)&&VTDD.vtdd.isTagExist(serverID, channelNickname)) {//頻道存在&&伺服器有訂閱
			String roleID = VTDD.vtdd.getTagID(serverID, channelNickname);
			Guild g = Core.botAPI.getGuildById(serverID);
			if(!VTDD.vtdd.isMAPExist(serverID, discordID,channelNickname)) {//User沒訂閱過這個頻道
				//如果有verify紀錄就跳過驗證,因為驗證沒過會自動刪掉
				boolean verifyFlag = false;
				if(VTDD.vtdd.isVerifyStatusExist(discordID, channelNickname)||(verifyFlag = VTDD.conf.ytapi.verifyUser(discordID, channelNickname))==true) {//驗證
					if(VTDD.vtdd.addMAP(serverID, discordID,channelNickname)) {//給Map
						VTDD.vtdd.updateVerifyStatusREF(discordID, channelNickname, 1);//引用+1
					}
					g.addRoleToMember(discordID, g.getRoleById(roleID)).queue();//驗證通過給群組
					
					if(verifyFlag)//有跑驗證且通過再刷新
						VTDD.vtdd.updateVerifyStatus(discordID, channelNickname, true);//更新驗證狀態
					
					Core.botAPI.getUserById(discordID).openPrivateChannel().queue(channel -> {
						channel.sendMessage("In "+serverID+", Channel: "+channelNickname+", :white_check_mark: Verified!!").queue();
					});
					return true;
				}else {
					Core.botAPI.getUserById(discordID).openPrivateChannel().queue(channel -> {
						channel.sendMessage("In "+serverID+", Channel: "+channelNickname+", :x: verification failed!!").queue();
					});
					return false;
				}
			}else {
				return true;//已經訂閱過(保留)
			}
		}
		
		return false;
	}
	
	/**
	 * User remove role
	 * @param serverID
	 * @param discordID
	 * @param channelNickname
	 * @return true if remove success
	 */
	public static boolean removeRolefromUser(String serverID,String discordID,String channelNickname) {//DONE
		
		if(VTDD.vtdd.isChannelExist(channelNickname)&&VTDD.vtdd.isTagExist(serverID, channelNickname)) {//頻道存在&&伺服器有訂閱
			//System.out.println("RM");///
			Guild g = Core.botAPI.getGuildById(serverID);
			String roleID = VTDD.vtdd.getTagID(serverID, channelNickname);
			if(VTDD.vtdd.isMAPExist(serverID, discordID,channelNickname)) {//User有訂閱這個頻道
				g.removeRoleFromMember(discordID, g.getRoleById(roleID)).queue();//從使用者身上移除role
				VTDD.vtdd.delMAP(serverID, discordID, channelNickname);//拔MAP
				VTDD.vtdd.updateVerifyStatusREF(discordID, channelNickname, -1);//引用-1
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Role被管理員直接移除沒經過bot處理才要執行這段
	 * @param rde
	 */
	public static void removeRolefromServer(RoleDeleteEvent rde) {//DONE
		Guild g = rde.getGuild();
		Role r = rde.getRole();
		if(VTDD.vtdd.isTagExistById(g.getId(), r.getId())) {
			String serverID = g.getId();
			String channelNickname = VTDD.vtdd.getChannelNicknameByTagID(serverID, r.getId());
			String emoji = VTDD.vtdd.getChannelEMOJI(channelNickname);
			String voteCh = VTDD.cmdChID.getVoteChannel(serverID);
			String voteMsg = VTDD.cmdChID.getVoteMsgID(serverID);
			if(voteCh!=null&&voteMsg!=null) {
				g.getTextChannelById(voteCh).retrieveMessageById(voteMsg).queue(message -> {
					message.retrieveReactionUsers(emoji).queue(users -> {
						users.forEach(user -> {
							VTDD.vtdd.delMAP(serverID, user.getId(), channelNickname);//拔MAP
							VTDD.vtdd.updateVerifyStatusREF(user.getId(), channelNickname, -1);//引用-1
						});
						message.removeReaction(emoji).queue();//清掉訂閱的EMOJI
						VTDD.vtdd.delTag(serverID, channelNickname);//拔Tag
					});
				});
			}
		}
	}
}
