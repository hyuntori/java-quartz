package quartzProject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class ExecuteJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		/* [연습-param]
		String jobName = "";	
		try {
			jobName = (String) arg0.getScheduler().getContext().get("name");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		*/
		System.out.println("작업이 실행합니다요");
	}

}
