package com.github.smallru8.NikoBot.VTDD.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.smallru8.NikoBot.StdOutput;
import com.github.smallru8.NikoBot.VTDD.Config;
import com.github.smallru8.NikoBot.VTDD.VTDD;

public class SQL {

	private String host;
	private String name;
	private String passwd;
	
	public SQL() {
		try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
		    StdOutput.errorPrintln("com.mysql.cj.jdbc.Driver not found.");
		} 
		Config conf = VTDD.conf;
		host = "jdbc:mysql://"+conf.ip+":"+conf.port+"/"+conf.db;
		name = conf.name;
		passwd = conf.passwd;
		checkTable();
	}
	
	protected Connection getSQLConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(host,name,passwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	protected void checkTable() {
		Connection conn = getSQLConnection();
		String query = "SHOW TABLES LIKE 'VTDD_REGUSER';";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) {
				ps.close();
				rs.close();
				query = "CREATE TABLE VTDD_REGUSER (\r\n" + 
						"	DiscordID VARCHAR(20) NOT NULL,\r\n" + 
						"	RefToken VARCHAR(150) NOT NULL,\r\n" + 
						"	Verify BOOLEAN,\r\n" + 
						"	TS TIMESTAMP,\r\n" + 
						"	PRIMARY KEY (DiscordID)\r\n" + 
						")CHARACTER SET=utf8mb4;";
				ps = conn.prepareStatement(query);
				ps.executeUpdate();
			}else {
				ps.close();
				rs.close();
			}
			
			query = "SHOW TABLES LIKE 'VTDD_SERVER';";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if(!rs.next()) {
				ps.close();
				rs.close();
				query = "CREATE TABLE VTDD_SERVER (\r\n" + 
						"	ServerID VARCHAR(20) NOT NULL, \r\n" + 
						"	MsgChannel VARCHAR(20), \r\n" + 
						"	VoteChannel VARCHAR(20), \r\n" + 
						"	VoteMsgID VARCHAR(20), \r\n" + 
						"	PRIMARY KEY (ServerID)\r\n" + 
						")CHARACTER SET=utf8mb4;";
				ps = conn.prepareStatement(query);
				ps.executeUpdate();
			}else {
				ps.close();
				rs.close();
			}
			
			query = "SHOW TABLES LIKE 'VTDD_CHANNEL';";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if(!rs.next()) {
				ps.close();
				rs.close();
				query = "CREATE TABLE VTDD_CHANNEL (\r\n" + 
						"	Nickname VARCHAR(20) NOT NULL,\r\n" + 
						"	VideoID VARCHAR(20),\r\n" + 
						"	Emoji VARCHAR(20),\r\n" + 
						"	PRIMARY KEY (Nickname)\r\n" + 
						")CHARACTER SET=utf8mb4;";
				ps = conn.prepareStatement(query);
				ps.executeUpdate();
			}else {
				ps.close();
				rs.close();
			}
			
			query = "SHOW TABLES LIKE 'VTDD_VERIFY';";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if(!rs.next()) {
				ps.close();
				rs.close();
				query = "CREATE TABLE VTDD_VERIFY (\r\n" + 
						"	DiscordID VARCHAR(20) NOT NULL,\r\n" + 
						"	Nickname VARCHAR(20) NOT NULL,\r\n" + 
						"	TS TIMESTAMP,\r\n" + 
						"	STATUS BOOLEAN,\r\n" + 
						"	REF INT,\r\n" +
						"	PRIMARY KEY (DiscordID,Nickname),\r\n" + 
						"	FOREIGN KEY (DiscordID) REFERENCES VTDD_REGUSER(DiscordID),\r\n" + 
						"	FOREIGN KEY (Nickname) REFERENCES VTDD_CHANNEL(Nickname)\r\n" + 
						")CHARACTER SET=utf8mb4;";
				ps = conn.prepareStatement(query);
				ps.executeUpdate();
			}else {
				ps.close();
				rs.close();
			}
			
			query = "SHOW TABLES LIKE 'VTDD_TAG';";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if(!rs.next()) {
				ps.close();
				rs.close();
				query = "CREATE TABLE VTDD_TAG (\r\n" + 
						"	ServerID VARCHAR(20) NOT NULL,\r\n" + 
						"	Nickname VARCHAR(20) NOT NULL,\r\n" + 
						"	TagID VARCHAR(20) NOT NULL,\r\n" + 
						"	PRIMARY KEY (ServerID,Nickname),\r\n" + 
						"	FOREIGN KEY (ServerID) REFERENCES VTDD_SERVER(ServerID),\r\n" + 
						"	FOREIGN KEY (Nickname) REFERENCES VTDD_CHANNEL(Nickname)\r\n" + 
						")CHARACTER SET=utf8mb4;";
				ps = conn.prepareStatement(query);
				ps.executeUpdate();
			}else {
				ps.close();
				rs.close();
			}
			
			query = "SHOW TABLES LIKE 'VTDD_MAP';";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if(!rs.next()) {
				ps.close();
				rs.close();
				query = "CREATE TABLE VTDD_MAP (\r\n" + 
						"	DiscordID VARCHAR(20) NOT NULL,\r\n" + 
						"	ServerID VARCHAR(20) NOT NULL,\r\n" + 
						"	PRIMARY KEY (DiscordID,ServerID),\r\n" + 
						"	FOREIGN KEY (DiscordID) REFERENCES VTDD_REGUSER(DiscordID),\r\n" + 
						"	FOREIGN KEY (ServerID) REFERENCES VTDD_SERVER(ServerID),\r\n" + 
						")CHARACTER SET=utf8mb4;";
				ps = conn.prepareStatement(query);
				ps.executeUpdate();
			}else {
				ps.close();
				rs.close();
			}
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
