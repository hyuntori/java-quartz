package quartzProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ExecuteJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		// tshark kill
		Process process = null;
		ProcessBuilder processbid = null;
		ArrayList<String> command = new ArrayList<>(); // 명령어 ArrayList
		command.add("ps -ef | grep 'tshark -i em1 -z' | awk '{print $2}'");
		command.add("killall tshark");
		String tsharkProcNum = "";
		
		for (int i = 0; i < command.size(); i++) {
			String cmd = command.get(i);
			processbid = new ProcessBuilder("bash","-c",cmd);
			processbid.redirectErrorStream(true);

			try {	
				process = processbid.start();
				process.waitFor();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";

				while ((line = reader.readLine()) != null) {
					if(cmd.indexOf("ps -ef") != -1) {
						tsharkProcNum = line;
						System.out.println("현재 실행된 Tshark ID:"+tsharkProcNum);
						break;
					}else{
						System.out.println(line);
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("예외 발생");
		
			}finally{
				process.destroy();
			}
		}
			
		// 3초동안 긁어오는 시간 줌
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// tshark.json 파일 삭제
		processbid = new ProcessBuilder("bash","-c","rm -rf tshark-all.json");
		processbid.redirectErrorStream(true);
		try {
			process = processbid.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			process.destroy();
		}

		
		// 다시 실행
		System.out.println("프로세스 다시 실행");
		processbid = new ProcessBuilder("bash","-c","tshark -i em1 -z expert -T ek > tshark-all.json");
		processbid.redirectErrorStream(false);
		try {
			process = processbid.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			process.destroy();
		}
	}

}
