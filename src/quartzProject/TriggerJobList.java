package quartzProject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

public class TriggerJobList {

	// 기본설정
	static int sec = 5; // 반복 초: default 1초
	
	/**************
	 * Validation *
	 *************/
	public static void validation() {
		
		Process process = null;
		ProcessBuilder processbid = null;
		String whoami = ""; // 로그인 User Id
		String authority = ""; // 권한
		String tsharkProcNum = ""; // 프로세스 Id
		ArrayList<String> command = new ArrayList<>(); // 명령어 ArrayList
		command.add("whoami");
		command.add("id");

		for (int i = 0; i < command.size(); i++) {
			String cmd = command.get(i); // 명령어
			processbid = new ProcessBuilder("bash","-c",cmd);
			processbid.redirectErrorStream(true);

			try {	
				process = processbid.start();
				process.waitFor();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";

				while ((line = reader.readLine()) != null) {
					if (cmd.equals("whoami")) {
						whoami = line;
						command.set(1, "id "+whoami +"| awk '{print $1$2}' | sed 's/[^0-9]//g'");
						System.out.println("로그인계정: " + whoami);
					} else if(cmd.indexOf("id") != -1){
						authority = line;
						if (authority.equals("00")) {
							System.out.println("root 계정으로 로그인하셨습니다. 작업을 시작합니다.");
						} else {
							System.out.println("root 계정으로 로그인 부탁드립니다.");
							System.exit(-1);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("예외 발생");

			} finally {
				process.destroy();
			}

		}

	}
	
	
	/*************
	 * CronList 로직
	 *************/
	public static void cronList() {
		Scheduler schedulerList;
		try {
			schedulerList = new StdSchedulerFactory().getScheduler();
			for (String groupName : schedulerList.getJobGroupNames()) {

				for (JobKey jobKey : schedulerList.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();

					// get job's trigger
					List<Trigger> triggers = (List<Trigger>) schedulerList.getTriggersOfJob(jobKey);
					Date nextFireTime = triggers.get(0).getNextFireTime();
					System.out.println("================= 작업리스트 =================");
					for (int i = 0; i < triggers.size(); i++) {
						System.out.println(
								"[jobName] : " + jobName + " [groupName] : " + jobGroup + " - " + nextFireTime);
					}
					System.out.println("===========================================");
				}
			}

		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	
	/******************
	 * Main
	 * @param args
	 *****************/
	public static void main(String args[]) {
		// 루트 권한 체크
		validation();

		/* Job Apply */
		JobDetail job = JobBuilder.newJob(ExecuteJob.class).withIdentity("delTshark", "daily").build();

		/* Trigger List */
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("t_delTshark", "daily")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(sec).repeatForever()).build();

		/* scheduler */
		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException e) {
			e.printStackTrace();
			System.out.println("스케줄러 에러");
		}

	}
}
