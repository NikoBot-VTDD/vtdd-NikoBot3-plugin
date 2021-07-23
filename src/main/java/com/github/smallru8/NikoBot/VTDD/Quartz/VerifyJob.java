package com.github.smallru8.NikoBot.VTDD.Quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.github.smallru8.NikoBot.StdOutput;
import com.github.smallru8.NikoBot.VTDD.VTDD;


/**
 * 驗證
 * @author smallru8
 *
 */
public class VerifyJob implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		StdOutput.infoPrintln("Starting daily verify...");
		VTDD.conf.ytapi.verifyAll();
		StdOutput.infoPrintln("Daily verify... Done!");
	}

}
