package com.github.smallru8.NikoBot.VTDD.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.smallru8.NikoBot.VTDD.VTDD;

/**
 * Exchange data with SQL server
 * @author smallru8
 *
 */
public class VTDData extends SQL{
	
	public VTDData() {
		super();
	}
	
	/**
	 * get user's youtube reftoken
	 * @param discordID
	 * @return
	 */
	public String getRefTokenById(String discordID) {
		String ret = null;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT RefToken FROM VTDD_REGUSER WHERE DiscordID=?;";
			
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, discordID);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
				ret = rs.getString(1);
			
			ps.close();
			conn.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public void setVote(String serverID,String ChID,String msgID) {
		try {
			Connection conn = getSQLConnection();
			String query = "UPDATE VTDD_SERVER SET VoteChanne=?,VoteMsgID=? WHERE ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, ChID);
			ps.setString(2, msgID);
			ps.setString(3, serverID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setVoteChannelID(String serverID,String ChID) {
		try {
			Connection conn = getSQLConnection();
			String query = "UPDATE VTDD_SERVER SET VoteChanne=? WHERE ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, ChID);
			ps.setString(2, serverID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} 
	
	public void setVoteMsgID(String serverID,String MsgID) {
		try {
			Connection conn = getSQLConnection();
			String query = "UPDATE VTDD_SERVER SET VoteMsgID=? WHERE ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, MsgID);
			ps.setString(2, serverID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setCommandCh(String serverID,String chID) {
		try {
			Connection conn = getSQLConnection();
			String query = "UPDATE VTDD_SERVER SET MsgChannel=? WHERE ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, chID);
			ps.setString(2, serverID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Map<String,String[]> getAllCommandCh(){
		Map<String,String[]> retm = new HashMap<String,String[]>();
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT ServerID,MsgChannel,VoteChannel,VoteMsgID FROM VTDD_SERVER;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String[] s = {rs.getString(2),rs.getString(3),rs.getString(4)};
				retm.put(rs.getString(1),s);
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retm;
	}
	
	/**
	 * Is the user existing
	 * @param discordId
	 * @return
	 */
	public boolean isUserExist(String discordId) {
		boolean ret = false;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT DiscordID FROM VTDD_REGUSER WHERE DiscordID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, discordId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) 
				ret = true;
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Add a discord server
	 * @param id
	 */
	public void addDCServer(String id) {
		if(!isDCServerExist(id)) {
			try {
				Connection conn = getSQLConnection();
				String query = "INSERT INTO VTDD_SERVER(ServerID) VALUES(?);";
				PreparedStatement ps;
				ps = conn.prepareStatement(query);
				ps.setString(1, id);
				ps.executeUpdate();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * has a discord server joined
	 * @param id
	 * @return
	 */
	public boolean isDCServerExist(String id) {
		boolean ret = false;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT * FROM VTDD_SERVER WHERE ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) 
				ret = true;
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	
	/**
	 * get all discord server
	 * @return
	 */
	public String[] getDCServer() {
		try {
			ArrayList<String> tmp = new ArrayList<String>();
			Connection conn = getSQLConnection();
			String query = "SELECT ServerID FROM VTDD_SERVER;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				tmp.add(rs.getString(1));
			}
			
			ps.close();
			conn.close();
			return tmp.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Remove discord server
	 * @param id
	 */
	public void delDCServer(String id) {
		try {
			Connection conn = getSQLConnection();
			String query = "DELETE FROM VTDD_SERVER WHERE ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setChannelEMOJI(String channelNickname,String emoji) {
		if(isChannelExist(channelNickname)) {
			try {
				Connection conn = getSQLConnection();
				String query = "UPDATE VTDD_CHANNEL SET Emoji=? WHERE Nickname=?;";
				PreparedStatement ps;
				ps = conn.prepareStatement(query);
				ps.setString(1, emoji);
				ps.setString(2, channelNickname);
				ps.executeUpdate();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getChannelEMOJI(String channelNickname) {
		String ret = null;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT Emoji FROM VTDD_CHANNEL WHERE Nickname=?;";
			
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, channelNickname);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
				ret = rs.getString(1);
			
			ps.close();
			conn.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * get channel's video id
	 * @param channelNickname
	 * @return
	 */
	public String getChannelVideoId(String channelNickname) {
		String ret = null;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT VideoID FROM VTDD_CHANNEL WHERE Nickname=?;";
			
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, channelNickname);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
				ret = rs.getString(1);
			
			ps.close();
			conn.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public String getChannelbyEmoji(String emoji) {
		String ret = null;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT Nickname FROM VTDD_CHANNEL WHERE Emoji=?;";
			
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, emoji);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
				ret = rs.getString(1);
			
			ps.close();
			conn.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * get all available channel
	 * @return
	 */
	public String[] getChannelAndEmoji() {
		try {
			ArrayList<String> tmp = new ArrayList<String>();
			Connection conn = getSQLConnection();
			String query = "SELECT Emoji,Nickname FROM VTDD_CHANNEL;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				tmp.add(rs.getString(1)+" "+rs.getString(2));
			}
				
			ps.close();
			conn.close();
			return tmp.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Add a new channel
	 * @param channelNickname
	 * @param videoID
	 */
	public void addChannel(String channelNickname,String videoID) {
		if(!isChannelExist(channelNickname)) {
			try {
				Connection conn = getSQLConnection();
				String query = "INSERT INTO VTDD_CHANNEL(Nickname,VideoID) VALUES(?,?);";
				PreparedStatement ps;
				ps = conn.prepareStatement(query);
				ps.setString(1, channelNickname);
				ps.setString(2, videoID);
				ps.executeUpdate();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * is a channel exist
	 * @param channelNickname
	 * @return
	 */
	public boolean isChannelExist(String channelNickname) {
		boolean ret = false;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT Nickname FROM VTDD_CHANNEL WHERE Nickname=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, channelNickname);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) 
				ret = true;
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * remove a channel
	 * @param channelNickname
	 */
	public void delChannel(String channelNickname) {
		try {
			Connection conn = getSQLConnection();
			String query = "DELETE FROM VTDD_CHANNEL WHERE Nickname=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, channelNickname);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get all channels and their video id
	 * @return
	 */
	public Map<String,String> getChannelMap(){
		Map<String,String> retm = new HashMap<String,String>();//channelNickname, VideoID
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT Nickname,VideoID FROM VTDD_CHANNEL;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				retm.put(rs.getString(1), rs.getString(2));
			}
			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retm;
	}
	
	/**
	 * get the users who need to check the membership status
	 * @return user, channelNicknames......
	 */
	public Map<String,ArrayList<String>> getExpiredUser(){
		Map<String,ArrayList<String>> retm = new HashMap<String,ArrayList<String>>();//DiscordID,channelNickname
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT DiscordID,Nickname FROM VTDD_VERIFY WHERE TIMESTAMPDIFF(DAY, TS, CURRENT_TIMESTAMP) > "+VTDD.conf.verifyDayInterval+";";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				if(retm.containsKey(rs.getString(1))) {
					retm.get(rs.getString(1)).add(rs.getString(2));
				}
				else {
					retm.put(rs.getString(1), new ArrayList<String>());
					retm.get(rs.getString(1)).add(rs.getString(2));
				}
			}
			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retm;
	}
	
	/**
	 * get a user's subscribe record 
	 * @param discordId
	 * @return
	 */
	public String[] getSubStatusByUser(String discordId) {
		try {
			ArrayList<String> tmp = new ArrayList<String>();
			Connection conn = getSQLConnection();
			String query = "SELECT Nickname FROM VTDD_VERIFY WHERE DiscordID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, discordId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				tmp.add(rs.getString(1));
			}
			
			ps.close();
			conn.close();
			return tmp.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Remove a user's subscribe record
	 * @param discordID
	 */
	public void delVerifyStatusByUser(String discordID) {
		try {
			Connection conn = getSQLConnection();
			String query = "DELETE FROM VTDD_VERIFY WHERE DiscordID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, discordID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新引用次數,使用者在幾個伺服器有這項認證
	 * @param discordID
	 * @param channelNickname
	 * @param i
	 */
	public void updateVerifyStatusREF(String discordID,String channelNickname,int i) {
		try {
			Connection conn = getSQLConnection();
			String query = "UPDATE VTDD_VERIFY SET TS=TS,REF=REF+? WHERE DiscordID=? and Nickname=?;";	
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setInt(1, i);
			ps.setString(2, discordID);
			ps.setString(3, channelNickname);
			ps.executeUpdate();
			ps.close();
			conn.close();
			checkVerifyStatusREF(discordID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 確認還有被引用
	 * @param discordID
	 * @param i
	 */
	public void checkVerifyStatusREF(String discordID) {
		try {
			Connection conn = getSQLConnection();
			String query = "DELETE FROM VTDD_VERIFY WHERE DiscordID=? AND REF<1;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, discordID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add new subscribe record to a user
	 * @param discordID
	 * @param channelNickname
	 */
	public void addVerifyStatus(String discordID,String channelNickname) {
		if(!isVerifyStatusExist(discordID,channelNickname)) {
			try {
				Connection conn = getSQLConnection();
				String query = "INSERT INTO VTDD_VERIFY(DiscordID,Nickname,REF) VALUES(?,?,1);";
				PreparedStatement ps;
				ps = conn.prepareStatement(query);
				ps.setString(1, discordID);
				ps.setString(2, channelNickname);
				ps.executeUpdate();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Update verify status and time stamp
	 * @param discordID
	 * @param channelNickname
	 * @param b
	 */
	public void updateVerifyStatus(String discordID,String channelNickname,boolean b) {
		addVerifyStatus(discordID,channelNickname);
		try {
			Connection conn = getSQLConnection();
			String query = "UPDATE VTDD_VERIFY SET TS=CURRENT_TIMESTAMP,STATUS=? WHERE DiscordID=? and Nickname=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setBoolean(1, b);
			ps.setString(2, discordID);
			ps.setString(3, channelNickname);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Update verify status and time stamp(doesn't check whether the data was existing)
	 * @param discordID
	 * @param channelNickname
	 * @param b
	 */
	public void updateVerifyStatusNocheck(String discordID,String channelNickname,boolean b) {
		//addVerifyStatus(discordID,channelNickname);
		try {
			Connection conn = getSQLConnection();
			String query = "UPDATE VTDD_VERIFY SET TS=CURRENT_TIMESTAMP,STATUS=? WHERE DiscordID=? and Nickname=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setBoolean(1, b);
			ps.setString(2, discordID);
			ps.setString(3, channelNickname);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test if a user has already subscribed 
	 * @return
	 */
	public boolean isVerifyStatusExist(String discordID,String channelNickname) {
		boolean ret = false;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT DiscordID FROM VTDD_VERIFY WHERE DiscordID=? and Nickname=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, discordID);
			ps.setString(2, channelNickname);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) 
				ret = true;
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Remove a user's subscribe record
	 * Must check the user didn't have any subscribe in any server
	 * @param discordID
	 * @param channelNickname
	 */
	public void delVerifyStatus(String discordID,String channelNickname) {
		try {
			Connection conn = getSQLConnection();
			String query = "DELETE FROM VTDD_VERIFY WHERE Nickname=? and DiscordID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, channelNickname);
			ps.setString(2, discordID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	
	/**
	 * get a server's all vts' nickname
	 * @param serverID
	 * @return String(NickName)
	 */
	public String[] getServerVTNickname(String serverID) {
		try {
			ArrayList<String> tmp = new ArrayList<String>();
			Connection conn = getSQLConnection();
			String query = "SELECT Nickname FROM VTDD_TAG WHERE ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, serverID);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				tmp.add(rs.getString(1));
			}
			
			ps.close();
			conn.close();
			return tmp.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String,String> getServerVTNicknameAndEmoji(String serverID){
		Map<String,String> retm = new HashMap<String,String>();
		try {
			Connection conn = getSQLConnection();
			String query = 
					"SELECT VTDD_CHANNEL.Nickname,VTDD_CHANNEL.Emoji "
					+ "FROM (SELECT * FROM VTDD_TAG WHERE ServerID=?) AS T1 "
					+ "INNER JOIN VTDD_CHANNEL "
					+ "ON T1.Nickname=VTDD_CHANNEL.Nickname;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, serverID);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				retm.put(rs.getString(1), rs.getString(2));
			}
			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retm;
	}
	
	public String getChannelNicknameByTagID(String serverID,String tagID) {
		String ret = null;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT Nickname FROM VTDD_TAG WHERE ServerID=? and TagID=?;";
			
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, serverID);
			ps.setString(2, tagID);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
				ret = rs.getString(1);
			
			ps.close();
			conn.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * get a server's vt's tag id
	 * @param serverID
	 * @param channelNickname
	 * @return
	 */
	public String getTagID(String serverID,String channelNickname) {
		String ret = null;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT TagID FROM VTDD_TAG WHERE ServerID=? and Nickname=?;";
			
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, serverID);
			ps.setString(2, channelNickname);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
				ret = rs.getString(1);
			
			ps.close();
			conn.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * remove a server's tag by tag's id
	 * @param serverID
	 * @param tagID
	 */
	public void delTagById(String serverID,String tagID) {
		try {
			Connection conn = getSQLConnection();
			String query = "DELETE FROM VTDD_TAG WHERE TagID=? and ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, tagID);
			ps.setString(2, serverID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test if target server has set the target channel's tag(search by tag id)
	 * @param serverID
	 * @param tagID
	 * @return
	 */
	public boolean isTagExistById(String serverID,String tagID) {
		boolean ret = false;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT TagID FROM VTDD_TAG WHERE ServerID=? and TagID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, serverID);
			ps.setString(2, tagID);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) 
				ret = true;
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Test if target server has set the target channel's tag(search by channel nickname)
	 * @param serverID
	 * @param channelNickname
	 * @return
	 */
	public boolean isTagExist(String serverID,String channelNickname) {
		boolean ret = false;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT TagID FROM VTDD_TAG WHERE ServerID=? and Nickname=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, serverID);
			ps.setString(2, channelNickname);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) 
				ret = true;
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Edit/Add this server's channel tag
	 * @param serverID
	 * @param channelNickname
	 * @param TagID
	 */
	public void addTag(String serverID,String channelNickname,String TagID) {
		if(!isTagExist(serverID,channelNickname)) {
			try {
				Connection conn = getSQLConnection();
				String query = "INSERT INTO VTDD_TAG(ServerID,Nickname,TagID) VALUES(?,?,?);";
				PreparedStatement ps;
				ps = conn.prepareStatement(query);
				ps.setString(1, serverID);
				ps.setString(2, channelNickname);
				ps.setString(3, TagID);
				ps.executeUpdate();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {//Update data if exist
			try {
				Connection conn = getSQLConnection();
				String query = "UPDATE VTDD_TAG SET TagID=? WHERE ServerID=? and Nickname=?;";
				PreparedStatement ps;
				ps = conn.prepareStatement(query);
				ps.setString(1, TagID);
				ps.setString(2, serverID);
				ps.setString(3, channelNickname);
				ps.executeUpdate();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Remove target channel's tag in target server 
	 * @param serverID
	 * @param channelNickname
	 */
	public void delTag(String serverID,String channelNickname) {
		try {
			Connection conn = getSQLConnection();
			String query = "DELETE FROM VTDD_TAG WHERE Nickname=? and ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, channelNickname);
			ps.setString(2, serverID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove all tag of channels in this server
	 * @param serverID
	 */
	public void delTag(String serverID) {
		try {
			Connection conn = getSQLConnection();
			String query = "DELETE FROM VTDD_TAG WHERE ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, serverID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get the users who subscribe any channel in target server  
	 * @param serverID
	 * @return
	 */
	public String[] getMAP(String serverID) {
		try {
			ArrayList<String> tmp = new ArrayList<String>();
			Connection conn = getSQLConnection();
			String query = "SELECT DiscordID FROM VTDD_MAP WHERE ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, serverID);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				tmp.add(rs.getString(1));
			}
			
			ps.close();
			conn.close();
			return tmp.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Bind user and server, when a user first subscribes channel in a server
	 * @param serverID
	 * @param discordID
	 */
	public void addMAP(String serverID,String discordID) {
		if(!isMAPExist(serverID,discordID)) {
			try {
				Connection conn = getSQLConnection();
				String query = "INSERT INTO VTDD_MAP(ServerID,DiscordID) VALUES(?,?);";
				PreparedStatement ps;
				ps = conn.prepareStatement(query);
				ps.setString(1, serverID);
				ps.setString(2, discordID);
				ps.executeUpdate();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Test if a user subscribes any channel in target server 
	 * @param serverID
	 * @param discordID
	 * @return
	 */
	public boolean isMAPExist(String serverID,String discordID) {
		boolean ret = false;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT DiscordID FROM VTDD_MAP WHERE ServerID=? and DiscordID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, serverID);
			ps.setString(2, discordID);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) 
				ret = true;
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Test if a user subscribes any channel in any server
	 * @param discordID
	 * @return
	 */
	public boolean isMAPExistByUser(String discordID) {
		boolean ret = false;
		try {
			Connection conn = getSQLConnection();
			String query = "SELECT DiscordID FROM VTDD_MAP WHERE DiscordID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, discordID);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) 
				ret = true;
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Remove User id and server id Map, when a user leaves server.
	 * @param serverID
	 * @param discordID
	 */
	public void delMAP(String serverID,String discordID) {
		try {
			Connection conn = getSQLConnection();
			String query = "DELETE FROM VTDD_MAP WHERE ServerID=? and DiscordID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, serverID);
			ps.setString(2, discordID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove User id and server id Map, when a server kicks bot.
	 * @param serverID
	 */
	public void delMAPByServer(String serverID) {
		try {
			Connection conn = getSQLConnection();
			String query = "DELETE FROM VTDD_MAP WHERE ServerID=?;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, serverID);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Search server id and its channel tag id by using user's discord id
	 * @param discordID
	 * @param channelNickname
	 * @return ServerID,TagID
	 */
	public Map<String,String> getServerTagByUserandNickname(String discordID,String channelNickname) {
		Map<String,String> retm = new HashMap<String,String>();
		try {
			Connection conn = getSQLConnection();
			String query = 
					"SELECT T1.ServerID,T1.TagID "
					+ "FROM (SELECT * FROM VTDD_TAG WHERE Nickname=?) AS T1 "
					+ "INNER JOIN (SELECT * FROM VTDD_MAP WHERE DiscordID=?) AS T2 "
					+ "ON T1.ServerID=T2.ServerID;";
			PreparedStatement ps;
			ps = conn.prepareStatement(query);
			ps.setString(1, channelNickname);
			ps.setString(2, discordID);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				retm.put(rs.getString(1), rs.getString(1));
			}
			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retm;
	}
}
