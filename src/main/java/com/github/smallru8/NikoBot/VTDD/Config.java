package com.github.smallru8.NikoBot.VTDD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	public String ip = "";
	public String port = "";
	public String name = "";
	public String passwd = "";
	public String db = "";
	public int verifyDayInterval = 2;
	
	private String DEVELOPER_KEY = "";
	private String clientID = "";
	private String clientSecret = "";
	
	public YTAPI ytapi;
	
	public Config() {
		File f = new File("conf.d/vtdd.conf");
		if(!f.exists()) {
			try {
				FileWriter fw = new FileWriter(f);
				fw.write("#vtdd SQL setting\n");
				fw.write("ip=127.0.0.1\n");
				fw.write("port=3306\n");
				fw.write("name=ddata\n");
				fw.write("passwd=passwd\n");
				fw.write("db=DData\n");
				fw.write("#Google API\n");
				fw.write("DEVELOPER_KEY=key\n");
				fw.write("clientID=id\n");
				fw.write("clientSecret=secret\n");
				fw.write("VerifyDayInterval=2\n");
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void loadConfig() {
		try {
			InputStream is = new FileInputStream("conf.d/vtdd.conf");
			Properties pro = new Properties();
			pro.load(is);
			ip = pro.getProperty("ip","127.0.0.1");
			port = pro.getProperty("port","3306");
			name = pro.getProperty("name","ddata");
			passwd = pro.getProperty("passwd","passwd");
			db = pro.getProperty("db","DData");
			DEVELOPER_KEY = pro.getProperty("DEVELOPER_KEY","Key");
			clientID = pro.getProperty("clientID","id");
			clientSecret = pro.getProperty("clientSecret","secret");
			verifyDayInterval = Integer.parseInt(pro.getProperty("VerifyDayInterval","2"));
			ytapi = new YTAPI(DEVELOPER_KEY,clientID,clientSecret);
			pro.clear();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
