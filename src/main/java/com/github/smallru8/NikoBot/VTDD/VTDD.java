package com.github.smallru8.NikoBot.VTDD;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.VTDD.Quartz.VerifyScheduler;
import com.github.smallru8.NikoBot.VTDD.SQL.VTDData;
import com.github.smallru8.NikoBot.VTDD.commands.ServerRegister;
import com.github.smallru8.NikoBot.VTDD.commands.UserRole;
import com.github.smallru8.NikoBot.plugins.PluginsInterface;

/**
 * 
 * @author smallru8
 *
 */
public class VTDD implements PluginsInterface{

	private String pluginName = "VTDD";
	public static Config conf;
	public static VTDData vtdd;
	public static ServerRegister SR;
	public static UserRole UR;
	public static VerifyScheduler vs;
	
	@Override
	public void onDisable() {
		vs.stop();
	}

	@Override
	public void onEnable() {	
		conf = new Config();
		conf.loadConfig();
		vtdd = new VTDData();
		SR = new ServerRegister(vtdd);
		UR = new UserRole(vtdd);
		
		Core.botAPI.addEventListener(new Listener());
		
		vs = new VerifyScheduler();
		vs.start();
	}
	
	@Override
	public String pluginsName() {
		return pluginName;
	}

}
