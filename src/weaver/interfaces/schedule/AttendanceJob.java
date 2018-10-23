package weaver.interfaces.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.weaver.ningb.direct.manager.integration.HrmScheduleDiffManager;
import com.weaver.ningb.direct.manager.integration.HrmScheduleManager;

/**
 * 同步考勤信息
 * 1.考勤报表, 同步 (打卡 (签到, 签退), 请假, 出差, 销假, 哺乳假)
 * 2.考勤差异处理
 * 目前每天 23:20 执行一次：0 20 23 * * ?
 * 
 * @author liberal
 *
 */
public class AttendanceJob extends BaseCronJob {

	private static final Log logger = LogFactory.getLog(AttendanceJob.class);
	
	
	private HrmScheduleManager manager = null;
	private HrmScheduleDiffManager diffManager = null;
	
	
	@Override
	public void execute() {
		String task = "AttendanceJob";
		try {
			long startTime = System.currentTimeMillis();
			logger.info("---------------- " + task + " start ----------------");
			
			// 同步考勤信息到报表中
			if (manager == null) manager = new HrmScheduleManager();
			logger.info("---------------- report start ----------------");
			boolean reportFlag = manager.syncAttendanceToReport(new Date(System.currentTimeMillis()));
			if (reportFlag) {
				logger.info("report Successfully");
			} else {
				logger.info("report Failure");
			}
			long reportEndTime = System.currentTimeMillis();
			logger.info("report times = " + (reportEndTime - startTime) / 1000 + " s");
			logger.info("---------------- report  end  ----------------");
			
			// 考勤差异处理
			if (diffManager == null) diffManager = new HrmScheduleDiffManager();
			logger.info("---------------- diff start ----------------");
			boolean diffFlag = diffManager.diffAttendance(getDateStr(new Date(System.currentTimeMillis())), getDateStr(new Date(System.currentTimeMillis())));
			if (diffFlag) {
				logger.info("diff Successfully");
			} else {
				logger.info("diff Failure");
			}
			logger.info("---------------- diff  end  ----------------");
			long endTime = System.currentTimeMillis();
			logger.info("times = " + (endTime - startTime) / 1000 + " s");
			logger.info("---------------- " + task + " end ----------------");
		} finally {
			manager = null;
			diffManager = null;
		}
	}
	
	protected String getDateStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
}
