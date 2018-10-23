package weaver.interfaces.schedule;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.weaver.ningb.direct.manager.integration.HrmScheduleManager;

/**
 * 提醒考勤信息 (打卡)
 * 目前每天晚上 20:00 执行一次：0 0 20 * * ?
 * 
 * @author liberal
 *
 */
public class AttendanceSmsPmJob extends BaseCronJob {
	
	private static final Log logger = LogFactory.getLog(AttendanceSmsPmJob.class);
	
	
	private HrmScheduleManager manager = null;
	
	
	@Override
	public void execute() {
		String task = "AttendanceSmsPmJob";
		try {
			long startTime = System.currentTimeMillis();
			if (manager == null) manager = new HrmScheduleManager();
			logger.info("---------------- " + task + " start ----------------");
			boolean flag = manager.sendAttendanceSign(1, new Date(System.currentTimeMillis()));
			if (flag) {
				logger.info("Successfully");
			} else {
				logger.info("Failure");
			}
			long endTime = System.currentTimeMillis();
			logger.info("times = " + (endTime - startTime) / 1000 + " s");
			logger.info("---------------- " + task + " end ----------------");
		} finally {
			manager = null;
		}
	}

}
