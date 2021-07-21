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
	public static String giveRole(String serverID,String discordID,String channelNickname) {//DONE
		String ret = "";
		if(VTDD.vtdd.isChannelExist(channelNickname)&&VTDD.vtdd.isTagExist(serverID, channelNickname)) {//頻道存在&&伺服器有訂閱
			String roleID = VTDD.vtdd.getTagID(serverID, channelNickname);
			Guild g = Core.botAPI.getGuildById(serverID);
			if(!VTDD.vtdd.isMAPExist(serverID, discordID,channelNickname)) {//User沒訂閱過這個頻道
				//TODO 如果有verify紀錄就跳過驗證,因為驗證沒過會自動刪掉
				if(VTDD.conf.ytapi.verify(VTDD.vtdd.getRefTokenById(discordID), VTDD.vtdd.getChannelVideoId(channelNickname))) {//驗證
					VTDD.vtdd.addMAP(serverID, discordID,channelNickname);//給Map
					g.addRoleToMember(channelNickname, g.getRoleById(roleID)).queue();//驗證通過給群組
					VTDD.vtdd.updateVerifyStatus(discordID, channelNickname, true);//更新驗證狀態
					VTDD.vtdd.updateVerifyStatusREF(discordID, channelNickname, 1);//引用+1
					ret = "<@"+discordID+"> subscribe channel: "+channelNickname+", :white_check_mark: Verified!!";
				}else {
					ret = "<@"+discordID+"> subscribe channel: "+channelNickname+", :x: verification failed!!";
				}
			}
		}else {
			ret = "Channel not found or this server wasn't setting this channel's role.";
		}
		return ret;
	}
	
	/**
	 * User remove role
	 * @param serverID
	 * @param discordID
	 * @param channelNickname
	 * @return
	 */
	public static String removeRolefromUser(String serverID,String discordID,String channelNickname) {//DONE
		String ret = "";	
		if(VTDD.vtdd.isChannelExist(channelNickname)&&VTDD.vtdd.isTagExist(serverID, channelNickname)) {//頻道存在&&伺服器有訂閱
			Guild g = Core.botAPI.getGuildById(serverID);
			String roleID = VTDD.vtdd.getTagID(serverID, channelNickname);
			if(VTDD.vtdd.isMAPExist(serverID, discordID,channelNickname)) {//User有訂閱這個頻道
				g.removeRoleFromMember(discordID, g.getRoleById(roleID)).queue();//從使用者身上移除role
				VTDD.vtdd.delMAP(serverID, discordID, channelNickname);//拔MAP
				VTDD.vtdd.updateVerifyStatusREF(discordID, channelNickname, -1);//引用-1
			}
			
		}else {
			ret = "Channel not found or this server wasn't setting this channel's role.";
		}
		return ret;
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
						message.clearReactions(emoji).queue();//清掉訂閱的EMOJI
						VTDD.vtdd.delTag(serverID, channelNickname);//拔Tag
					});
				});
			}
		}
	}
	
	
	
	
}
