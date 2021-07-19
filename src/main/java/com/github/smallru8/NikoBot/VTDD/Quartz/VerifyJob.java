package com.github.smallru8.NikoBot.VTDD.Quartz;

import java.util.ArrayList;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.StdOutput;
import com.github.smallru8.NikoBot.VTDD.VTDD;

import net.dv8tion.jda.api.entities.Guild;


/**
 * 驗證
 * @author smallru8
 *
 */
public class VerifyJob implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		StdOutput.infoPrintln("Starting daily verify...");
		Map<String,String> channelMap = VTDD.vtdd.getChannelMap();//channelNickname, VideoID
		//需要重新驗證的user
		Map<String,ArrayList<String>> userMap = VTDD.vtdd.getExpiredUser();//DiscordID, [channelNicknames...]
		
		for (Map.Entry<String, ArrayList<String>> userCnameMap : userMap.entrySet()) {
			String reftoken = VTDD.vtdd.getRefTokenById(userCnameMap.getKey());
			
			for(String channelNickname:userCnameMap.getValue()) {
				String videoID = channelMap.get(channelNickname);
				if(VTDD.conf.ytapi.verify(reftoken, videoID)) {//YT驗證
					VTDD.vtdd.updateVerifyStatusNocheck(userCnameMap.getKey(), channelNickname, true);
				}
				else{//沒通過, 刪認證 拔role
					Map<String,String> ServerTag = VTDD.vtdd.getServerTagByUserandNickname(userCnameMap.getKey(),channelNickname);
					for (Map.Entry<String, String> entry : ServerTag.entrySet()) {//拔這個user在各個server的對應role
						Guild g = Core.botAPI.getGuildById(entry.getKey());
						g.removeRoleFromMember(userCnameMap.getKey(), g.getRoleById(entry.getValue())).queue();//拔role
					}
					//刪認證
					VTDD.vtdd.delVerifyStatus(userCnameMap.getKey(), channelNickname);
					
				}
				
			}
			
		}
		StdOutput.infoPrintln("Daily verify... Done!");
	}

}
