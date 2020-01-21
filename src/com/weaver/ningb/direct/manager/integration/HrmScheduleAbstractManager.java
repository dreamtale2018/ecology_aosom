package com.weaver.ningb.direct.manager.integration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import weaver.conn.RecordSet;
import weaver.general.Util;

import com.weaver.ningb.core.Environment;
import com.weaver.ningb.core.util.FormModeUtils;
import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.direct.dao.integration.HrmDao;
import com.weaver.ningb.direct.dao.integration.impl.HrmDaoImpl;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.formmode.core.FormModeManager;
import com.weaver.ningb.soa.formmode.core.FormModeManagerImpl;
import com.weaver.ningb.soa.formmode.entity.FormModeResult;

/**
 * 考勤基类
 * 
 * @author liberal
 *
 */
public abstract class HrmScheduleAbstractManager {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	
	protected Map<String, Object> envMap = null;
	protected HrmDao dao;
	protected FormModeManager formmodeManagerService;
	protected RecordSet rs;

	
	public HrmScheduleAbstractManager() {
		try {
			this.envMap = load();
			this.dao = new HrmDaoImpl(envMap.get("datasourceid") + "", isDebugEnabled());
			
			this.formmodeManagerService = new FormModeManagerImpl("ws");
			
			this.rs = new RecordSet();
		} catch (Exception e) {
			logger.error("init Exception: ", e);
		}
	}
	
	
	/**
	 * 当前是否为调试模式：true.是      false.否
	 */
	protected boolean isDebugEnabled() {
		Object debug = envMap.get("debug");
		if (debug == null) return false;
		return (debug + "").equals("true");
	}
	
	
	/**
	 * 获取考勤类型
	 * 
	 * @param typeId
	 * 					考勤类型 id
	 * @return 考勤类型字符串
	 */
	protected String getAttendanceSignType(int typeId) {
		String[] signTypes = new String[]{"", "sign", "leave", "absence", "evection", "evectionadjust", "lactation"};
		if (typeId < 0 || typeId >= signTypes.length) return null;
		return signTypes[typeId];
	}
	
	/**
	 * 获取考勤默认工作时间
	 * 
	 * @param key
	 * 					开始或者结束工作时间
	 * @return 时间信息
	 */
	protected String getAttendanceDateConfig(String key) {
		return getPropString(key);
	}
	
	/**
	 * 根据考勤类型获取, 获取对应类型表单数据
	 * 
	 * @param typeId
	 * 					请假类型
	 * @return 表单数据
	 */
	protected Map<String, String> getAttendanceData(String typeId) {
		return getAttendanceData(typeId, null);
	}
	
	/**
	 * 根据考勤类型获取, 获取对应类型表单数据
	 * 
	 * @param typeId
	 * 					请假类型
	 * @return 表单数据
	 */
	protected Map<String, String> getAttendanceData(String typeId, Boolean isCondition) {
		String workflowid = "";
		String tablename = "";
		String sqlFields = "";
		String sqlCondition = "";
		if (StringUtils.isBlank(typeId)) {
		} else if ("2".equals(typeId)) {
			workflowid = WORKFLOW_QJD_ID;
			sqlFields = " kqlx, sqr xm, ejzz bm, szfb gs, requestid xglc, sjqjksrq ksrq, sjqjkssj kssj,"
					+ " sjqjjsrq jsrq, sjqjjssj jssj, sjqjsjhj ts, '" + typeId + "' kqlx ";
			sqlCondition = "sqr = ? and sjqjksrq <= ? and sjqjjsrq >= ? ";
		} else if ("3".equals(typeId)) {
			workflowid = WORKFLOW_XJD_ID;
			sqlFields = " kqlx, sqr xm, ejzz bm, szfbyc gs, requestid xglc, sjqjsjfw ksrq, sjkssjyc kssj,"
					+ " z2 jsrq, sjjssj jssj, sjsjhj ts, '" + typeId + "' kqlx ";
		} else if ("4".equals(typeId)) {
			workflowid = WORKFLOW_CCSQD_ID;
			sqlFields = " kqlx, sqr xm, ejzz bm, szfb gs, requestid xglc, sjsjfw ksrq, sjsj kssj, sjz jsrq,"
					+ " sjsjz jssj, sjccsjhj ts, '" + typeId + "' kqlx ";
			sqlCondition = "sqr = ? and sjsjfw <= ? and sjz >= ? ";
		} else if ("5".equals(typeId)) {
			workflowid = WORKFLOW_CCRQTZSQD_ID;
			sqlFields = " kqlx, sqr xm, ejzz bm, szfz gs, requestid xglc, sjsjfw ksrq, sjkssj kssj, z1 jsrq,"
					+ " sjjssj jssj, sjsjhj ts, '" + typeId + "' kqlx ";
		} else {}
		if (!StringUtils.isBlank(workflowid)) tablename = WorkflowUtils.getTablename(workflowid);
		
		Map<String, String> map = new HashMap<String, String>(3);
		map.put("workflowid", workflowid);
		map.put("tablename", tablename);
		map.put("sqlFields", sqlFields);
		if (isCondition != null && isCondition) map.put("sqlCondition", sqlCondition);
		map.put("attendanceid", typeId);
		return map;
	}
	
	/**
	 * 判断是否存在于考勤报表中
	 * 
	 * @param userid
	 * 					用户 id
	 * @param userType
	 * 					用户类型
	 * @param signType
	 * 					签到类型
	 * @param signDate
	 * 					签到时间
	 * @param isInCom
	 * 					是否来自外部
	 * @return boolean：true.存在      false.不存在
	 */
	protected boolean checkScheduleSign(String userid, String userType, 
			String signType, String signDate, String isInCom) {
		boolean flag = false;
		String sql = "select 1 from HrmScheduleSign "
				+ "where userid = '" + userid + "' and userType = '" + userType + "' "
				+ "and signType = '" + signType + "' and signDate = '" + signDate + "' and isInCom = '" + isInCom + "' ";
		if (isDebugEnabled()) logger.info("checkScheduleSign sql: " + sql);
		rs.execute(sql);
		if (rs.next()) flag = true;
		return flag;
	}
	
	/**
	 * 判断是否存在于考勤报表中
	 * 
	 * @param userid
	 * 					用户 id
	 * @param userType
	 * 					用户类型
	 * @param signType
	 * 					签到类型
	 * @param signDate
	 * 					签到时间
	 * @param isInCom
	 * 					是否来自外部
	 * @return 报表中打卡考勤信息
	 */
	protected Map<String, String> checkScheduleSignToMap(String userid, String userType, 
			String signType, String signDate, String isInCom) {
		Map<String, String> map = null;
		try {
			String sql = "select * from HrmScheduleSign "
					+ "where userid = '" + userid + "' and userType = '" + userType + "' "
					+ "and signType = '" + signType + "' and signDate = '" + signDate + "' and isInCom = '" + isInCom + "' ";
			if (isDebugEnabled()) logger.info("checkScheduleSignToMap sql: " + sql);
			rs.execute(sql);
			if (rs.next()) {
				String id = rs.getString("id");
				String signTime = rs.getString("signTime");
				
				map = new HashMap<String, String>(2);
				map.put("id", id);
				map.put("signTime", signTime);
			}
		} catch (Exception e) {
			logger.error("checkScheduleSignToMap Exception: ", e);
		}
		return map;
	}
	
	/**
	 * 时间字符串比较
	 * 
	 * @param oldTime
	 * 					原来时间
	 * @param newTime
	 * 					当前时间
	 * @param compareTo
	 * 					gt.大于当前时间则返回 true
	 * 					lt.小于当前时间则返回 true
	 * @return boolean：true.小于      false.不小于
	 */
	protected boolean compareScheduleSign(String oldTime, String newTime,
			String compareTo) {
		boolean flag = false;
		String oldJssj = Util.null2String(oldTime, "");
		String newJssj = Util.null2String(newTime, "");
		if ("gt".equals(compareTo)) {
			if (oldJssj.compareTo(newJssj) > 0) flag = true;
		} else if ("lt".equals(compareTo)) {
			if (oldJssj.compareTo(newJssj) < 0) flag = true;
		} else {
			if (oldJssj.compareTo(newJssj) == 0) flag = true;
		}
		return flag;
	}
	
	
	/**
	 * 保存考勤信息到建模数据中
	 * 
	 * @param task
	 * 					任务名称
	 * @param modeId
	 * 					模块 id
	 * @param modeCreator
	 * 					模块数据创建用户 id
	 * @param map
	 * 					考勤数据
	 * @return boolean：true.操作成功      false.操作失败
	 */
	protected boolean saveAttendance(String task, String modeId,
			String modeCreator, Map<String, String> map) {
		boolean flag = true;
		try {
			String right = "Y";
			FormModeResult<Integer> result = formmodeManagerService.save(modeId, modeCreator, right, map);
			if (result == null || !"0".equals(result.getCode())) {
				logger.info(task + " formmodeManager save Failure.");
				flag = false;
			}
		} catch (Exception e) {
			logger.error(task + " Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	
	/**
	 * 保存建模考勤数据执行状态
	 * 
	 * @param id
	 * 					建模考勤数据 id
	 * @param status
	 * 					执行状态：0.成功   -1.失败   -2.异常
	 * @param message
	 * 					执行描述
	 * @return boolean：true.操作成功      false.操作失败
	 */
	@SuppressWarnings("deprecation")
	protected boolean execLog(String id, String status, String message) {
		Calendar calendar = Calendar.getInstance();
		String execDate = Util.add0(calendar.get(Calendar.YEAR), 4) + "-" + Util.add0(calendar.get(Calendar.MONTH) + 1, 2) + "-" + Util.add0(calendar.get(Calendar.DAY_OF_MONTH), 2);
        String execTime = Util.add0(calendar.getTime().getHours(), 2) + ":" + Util.add0(calendar.getTime().getMinutes(), 2);
        String tablename = FormModeUtils.getTablename(getPropString("modeid"));
        return dao.updateAttendance(tablename, id, execDate, execTime, status, message);
	}
	
	
    protected Date getDateStr(String dateStr) {
    	return getDate(dateStr, "yyyy-MM-dd");
	}
    
    protected Date getDate(String dateStr, String format) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(dateStr);
		} catch (ParseException e) {}
		return date;
	}
    
    protected String getDateStr(Date date) {
    	return getDateStr(date, "yyyy-MM-dd");
	}
    
    protected String getDateStr(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
     * 两个时间相差天数
     * 
     * @param beginDate
     * 					开始时间    
     * @param endDate
     * 					结束时间
     * @return 相差天数
     */
    protected int differentDays(String beginDate, String endDate) {
		return differentDays(getDateStr(beginDate), getDateStr(endDate));
	}
	
	/**
     * 两个时间相差天数
     * 
     * @param beginDate
     * 					开始时间    
     * @param endDate
     * 					结束时间
     * @return 相差天数
     */
    protected int differentDays(Date beginDate, Date endDate) {
    	if (beginDate == null || endDate == null) return 0;
    	
    	Calendar cal1 = Calendar.getInstance();
    	cal1.setTime(beginDate);
    	
    	Calendar cal2 = Calendar.getInstance();
    	cal2.setTime(endDate);
    	int day1 = cal1.get(Calendar.DAY_OF_YEAR);
    	int day2 = cal2.get(Calendar.DAY_OF_YEAR);
    	
    	int year1 = cal1.get(Calendar.YEAR);
    	int year2 = cal2.get(Calendar.YEAR);
    	
    	if (year1 != year2) { // 同一年
    		int timeDistance = 0;
    		for (int i = year1; i < year2; i++) {
    			if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {	// 闰年
    				timeDistance += 366;
    			} else {	// 不是闰年
    				timeDistance += 365;
    			}
    		}
    		return timeDistance + (day2 - day1);
    	} else {	// 不同年
    		return day2 - day1;
    	}
    }
	
    protected String getDateStr(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, days);
		return getDateStr(cal.getTime());
	}
    
    protected String getDate(Date first, Date second, Date third, String format, String type) {
		long end = 0;
		if ("am".equals(type)) {
			end = first.getTime() + second.getTime() - third.getTime();
		} else {
			end = first.getTime() - second.getTime() + third.getTime();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(end);
		return getDateStr(calendar.getTime(), format);
	}
    
    /**
     * 获取时间对于星期几
     * 
     * @param date
     * 					时间
     * @return 星期几
     */
    protected int getWeekOfDate(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int dayForWeek = 0;
    	if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
    		dayForWeek = 7;
    	} else {
    		dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    	}
    	return dayForWeek;
    }
	
	
	/**
	 * 获取配置文件值
	 */
	protected String getPropString(String key) {
		Object value = getPropObject(key);
		return value == null ? null : value + "";
	}
	
	/**
	 * 获取配置文件值
	 */
	protected Object getPropObject(String key) {
		if (envMap == null || envMap.size() <= 0) return null;
		return envMap.get(key);
	}
	
	/**
	 * 加载配置文件
	 */
	private Map<String, Object> load() {
		Map<String, Object> map = new HashMap<String, Object>();
		InputStream in = null;
		try {
			String path = Environment.getInstance().getAbsolutePathToProp("hrm.properties");
			
			Properties prop = new Properties();
			in = new BufferedInputStream(new FileInputStream(new File(path)));
			prop.load(new InputStreamReader(in, "UTF-8"));
			
			map.put("debug", prop.get("hrm.debug"));
			map.put("datasourceid", prop.get("hrm.datasourceid"));
			map.put("modeid", prop.get("hrm.modeid"));
			map.put("modecreator", prop.get("hrm.modecreator"));
			map.put("attendanceid", prop.get("hrm.attendanceid"));
			map.put("lactationmodeid", prop.get("hrm.lactation.modeid"));
			map.put("lactationattendanceid", prop.get("hrm.lactation.attendanceid"));
			map.put("schedulestarttime", prop.get("hrm.schedule.starttime"));
			map.put("scheduleendtime", prop.get("hrm.schedule.endtime"));
			map.put("schedulesize", prop.get("hrm.schedule.size"));
			map.put("schedulereminduser", prop.get("hrm.schedule.reminduser"));
			map.put("scheduleremindmsg", prop.get("hrm.schedule.remindmsg"));
			map.put("scheduleremindflag", prop.get("hrm.schedule.remindflag"));
			map.put("scheduleremindammsg", prop.get("hrm.schedule.remindammsg"));
			map.put("scheduleremindpmmsg", prop.get("hrm.schedule.remindpmmsg"));
			map.put("ignoreusers", prop.get("hrm.ignore.users"));
		} catch (Exception e) {
			logger.error("load Exception: ", e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error("load InputStream close: ", e);
				}
		}
		return map;
	}
	
	
	/**
	 * 考勤分部 ids
	 */
	protected static final String HRM_SUBCOMPANY_IDS = "21,30";
	/**
	 * 考勤分部 id
	 */
	protected static final String HRM_SUBCOMPANY_ID = "21";
	
	/**
	 * 请假单流程 id
	 */
	protected static final String WORKFLOW_QJD_ID = "141";
	
	/**
	 * 出差/外出/外出培训申请单流程 id
	 */
	protected static final String WORKFLOW_CCSQD_ID = "142";
	
	/**
	 * 销假单流程 id
	 */
	protected static final String WORKFLOW_XJD_ID = "143";
	
	/**
	 * 出差日期调整申请单流程 id
	 */
	protected static final String WORKFLOW_CCRQTZSQD_ID = "146";

}
