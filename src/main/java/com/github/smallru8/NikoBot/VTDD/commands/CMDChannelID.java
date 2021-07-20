package com.github.smallru8.NikoBot.VTDD.commands;

import java.util.Map;

import com.github.smallru8.NikoBot.VTDD.VTDD;

/**
 * cache
 * @author smallru8
 *
 */
public class CMDChannelID {
	
	//<ServerID, <ChID,VoteChannel,VoteMsgID>>
	private Map<String,String[]> ServerCMDChMap;
	
	public CMDChannelID() {
		ServerCMDChMap = VTDD.vtdd.getAllCommandCh();
	}
	
	public void addServer(String serverID) {
		ServerCMDChMap.put(serverID, new String[3]);
	}
	
	public void removeServer(String serverID) {
		ServerCMDChMap.remove(serverID);
	}
	
	public void updateChID(String serverID,String ChID) {
		ServerCMDChMap.get(serverID)[0]=ChID;
		VTDD.vtdd.setCommandCh(serverID, ChID);
	}
	
	public void updateVote(String serverID,String ChID,String msgID) {
		ServerCMDChMap.get(serverID)[1]=ChID;
		ServerCMDChMap.get(serverID)[2]=msgID;
		VTDD.vtdd.setVote(serverID, ChID, msgID);
	}
	
	public void updateVoteMsgID(String serverID,String msgID) {
		ServerCMDChMap.get(serverID)[2]=msgID;
		VTDD.vtdd.setVoteMsgID(serverID, msgID);
	}
	
	public void updateVoteChannel(String serverID,String ChID) {
		ServerCMDChMap.get(serverID)[1]=ChID;
		VTDD.vtdd.setVoteChannelID(serverID, ChID);
	}
	
	public String getChID(String serverID) {
		return ServerCMDChMap.get(serverID)[0];
	}
	
	public String getVoteMsgID(String serverID) {
		return ServerCMDChMap.get(serverID)[2];
	}
	
	public String getVoteChannel(String serverID) {
		return ServerCMDChMap.get(serverID)[1];
	}
	
}
