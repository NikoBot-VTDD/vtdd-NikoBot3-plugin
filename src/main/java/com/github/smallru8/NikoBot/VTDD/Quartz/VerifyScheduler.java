package com.github.smallru8.NikoBot.VTDD.Quartz;

import java.util.TimeZone;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class VerifyScheduler {
	
	private JobDetail job;
	private Trigger tri;
	private Scheduler scheudler;
	
	public VerifyScheduler() {
		//verify job
		job = JobBuilder.newJob(VerifyJob.class).build();
		//4AM everyday 
		tri = TriggerBuilder.newTrigger().withIdentity("verifyScheduler").withSchedule(CronScheduleBuilder.cronSchedule("0 00 4 1/1 * ? *").inTimeZone(TimeZone.getTimeZone("Asia/Taipei"))).build();
		try {
			scheudler = StdSchedulerFactory.getDefaultScheduler();
			scheudler.scheduleJob(job, tri);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		try {
			scheudler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			scheudler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
}
