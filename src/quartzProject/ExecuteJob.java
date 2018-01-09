package quartzProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ExecuteJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		// tSharkDump 삭제
		System.out.println("하하");
		
		
		/* [연습-param]
		String jobName = "";	
		try {
			jobName = (String) arg0.getScheduler().getContext().get("name");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		*/
		
		
		
		
	    
	}

}
