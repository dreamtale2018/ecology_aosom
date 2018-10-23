package weaver.interfaces.schedule;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.weaver.ningb.direct.manager.integration.HrmScheduleManager;

/**
 * 同步考勤信息 (哺乳假)
 * 目前 23 点执行一次：0 0 23 * * ?
 * 
 * @author liberal
 *
 */
public class AttendanceLactationJob extends BaseCronJob {
	
	private static final Log logger = LogFactory.getLog(AttendanceLactationJob.class);
	
	
	private HrmScheduleManager manager = null;
	

	@Override
	public void execute() {
		String task = "AttendanceLactationJob";
		try {
			long startTime = System.currentTimeMillis();
			if (manager == null) manager = new HrmScheduleManager();
			logger.info("---------------- " + task + " start ----------------");
			boolean flag = manager.syncAttendanceLactationToFormmode(new Date(System.currentTimeMillis()));
			if (flag) {
				logger.info("Successfully");
			} else {
				logger.info("Failure");
			}
			long endTime = System.currentTimeMillis();
			logger.info("times = " + (endTime - startTime) + " ms");
			logger.info("---------------- " + task + " end ----------------");
		} finally {
			manager = null;
		}
	}

}
