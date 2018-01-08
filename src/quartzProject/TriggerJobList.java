package quartzProject;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class TriggerJobList {

	public static void main(String args[]) {
		
		int sec = 1; // default 1초
		
		try {
			sec = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("초는 숫자형태여야 합니다. (-_-);;");
			e.printStackTrace();
		}

		/* Job Apply */
		JobDetail job = JobBuilder.newJob(ExecuteJob.class)
				.withIdentity("delTshark", "daily").build();
		
		/* Trigger List */
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("t_delTshark", "daily")
				.withSchedule(
					SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(sec).repeatForever())
				.build();
		
		/* scheduler */
		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			//scheduler.getContext().put("name", "delTshark"); // [연습-param] execute 넘길 Parameter. 
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			System.out.println("스케줄러 에러");
		}
	}
}
