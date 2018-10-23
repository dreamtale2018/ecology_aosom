package weaver.interfaces.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.weaver.ningb.direct.manager.integration.HrmScheduleManager;

/**
 * 同步考勤信息 (打卡)
 * 目前每天早上 8:40 执行一次：0 40 8 * * ?
 * 
 * @author liberal
 *
 */
public class AttendanceAmJob extends BaseCronJob {

	private static final Log logger = LogFactory.getLog(AttendanceAmJob.class);
	
	
	private HrmScheduleManager manager = null;
	
	
	@Override
	public void execute() {
		String task = "AttendanceAmJob";
		try {
			long startTime = System.currentTimeMillis();
			if (manager == null) manager = new HrmScheduleManager();
			logger.info("---------------- " + task + " start ----------------");
			
			Date currentDate = new Date(System.currentTimeMillis());		// 当前时间
			
			// 同步前一天数据
			String beforeDate = getDateStr(currentDate, -1);
			logger.info("---------------- " + task + " before start ----------------");
			boolean beforeFlag = manager.syncAttendanceSignAllToFormmode(beforeDate, beforeDate);
			if (beforeFlag) {
				logger.info("before Successfully");
			} else {
				logger.info("before Failure");
			}
			long beforeEndTime = System.currentTimeMillis();
			logger.info("before times = " + (beforeEndTime - startTime) / 1000 + " s");
			logger.info("---------------- " + task + " before end ----------------");
			
			// 同步当天早上数据
			boolean flag = manager.syncAttendanceSignAMToFormmode(currentDate);
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
	
	
	private String getDateStr(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, days);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
	
}
