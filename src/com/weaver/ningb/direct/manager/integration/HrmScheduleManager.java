package com.weaver.ningb.direct.manager.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import weaver.general.Util;
import weaver.sms.CustomSmsService;

import com.weaver.ningb.core.util.FormModeUtils;

/**
 * 考勤 Manager
 * 
 * @author liberal
 *
 */
public class HrmScheduleManager extends HrmScheduleAbstractManager {
	
	/**
	 * 同步考勤信息 (打卡) 到建模数据中, 操作类型为全部
	 * 
	 * @param startDate
	 * 					开始时间
	 * @param endDate
	 * 					结束时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceSignAllToFormmode(String startDate, String endDate) {
		return syncAttendanceSignToFormmode(2, startDate, endDate);
	}
	
	/**
	 * 同步考勤信息 (打卡) 到建模数据中
	 * 
	 * @param type
	 * 					操作类型：0.早上   1.晚上   2.全部
	 * @param startDate
	 * 					开始时间
	 * @param endDate
	 * 					结束时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceSignToFormmode(int type, String startDate, String endDate) {
		boolean flag = true;
		if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
			logger.info("syncAttendanceSignToFormmode startDate and endDate is null");
		} else if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			String deadline = StringUtils.isBlank(startDate) ? endDate : startDate;
			flag = syncAttendanceSignToFormmode(type, deadline);
		} else {
			int diffDay = differentDays(startDate, endDate);
			for (int i = 0; i <= diffDay; i++) {
				String deadline = getDateStr(getDateStr(startDate), i);
				boolean tempFlag = syncAttendanceSignToFormmode(type, deadline);
				if (!tempFlag) 
					logger.info(String.format("syncAttendanceSignToFormmode detail Failure: deadline = %s", 
							deadline));
			}
		}
		return flag;
	}
	
	/**
	 * 同步考勤信息 (打卡) 到建模数据中, 早上
	 * 
	 * @param date
	 * 					指定时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceSignAMToFormmode(Date date) {
		return syncAttendanceSignToFormmode(0, getDateStr(date));
	}
	
	/**
	 * 同步考勤信息 (打卡) 到建模数据中, 晚上
	 * 
	 * @param date
	 * 					指定时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceSignPMToFormmode(Date date) {
		return syncAttendanceSignToFormmode(1, getDateStr(date));
	}
	
	/**
	 * 同步考勤信息 (打卡) 到建模数据中
	 * 
	 * @param type
	 * 					操作类型：0.早上   1.晚上   2.全部
	 * @param dateStr
	 * 					指定时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceSignToFormmode(int type, String dateStr) {
		boolean flag = true;
		try {
			if (StringUtils.isBlank(dateStr)) {
				logger.info("syncAttendanceSignToFormmode date is null");
				return flag;
			}
			
			// 获取考勤机数据
			List<Map<String, String>> list = dao.getAttendance(dateStr);
			if (list == null || list.size() <= 0) {
				logger.info("syncAttendanceSignToFormmode list is null.");
				return flag;
			}
			
			// 保存考勤信息到建模数据中
			flag = saveAttendanceSign(type, list);
			if (!flag) {
				logger.info("syncAttendanceSignToFormmode saveAttendanceSign Failure.");
				return flag;
			}
			
			// 保存考勤信息 (免打卡人员) 到建模数据中
			if (type == 0 || type == 2) {
				flag = saveAttendanceSignByDefault(dateStr);
			}
		} catch (Exception e) {
			logger.error("syncAttendanceSignToFormmode Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 同步考勤信息 (流程数据, 请假, 出差, 销假, 出差调整) 到建模数据中
	 * 
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceWorkflowToFormmode() {
		List<Integer> signTypes = new ArrayList<Integer>();
		signTypes.add(2);
		signTypes.add(3);
		signTypes.add(4);
		signTypes.add(5);
		return syncAttendanceWorkflowToFormmode(signTypes);
	}
	
	/**
	 * 同步考勤信息 (流程数据, 包括请假, 出差, 销假等) 到建模数据中
	 * 
	 * @param signTypes
	 * 					考勤信息类型
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceWorkflowToFormmode(List<Integer> signTypes) {
		boolean flag = true;
		try {
			for (int signType : signTypes) {
				Map<String, String> map = getAttendanceData(signType + "");
				if (map == null || map.size() <= 0) continue;
				
				String tablename = map.get("tablename");
				String sqlFields = map.get("sqlFields");
				List<Map<String, String>> list = dao.getAttendanceByWorkflow(tablename, sqlFields, signType + "");
				if (list == null || list.size() <= 0) {
					logger.info(String.format("syncAttendanceWorkflowToFormmode list is null (signType = %s)",
							signType));
					continue;
				}
				
				boolean saveFlag = saveAttendanceWorkflow(list);
				if (!saveFlag) 
					logger.info(String.format("syncAttendanceWorkflowToFormmode list Failure (signType = %s)",
						signType));
				
				// clear
				if (list != null) list.clear();
			}
		} catch (Exception e) {
			flag = false;
			logger.error("syncAttendanceWorkflowToFormmode Exception: ", e);
		}
		return flag;
	}
	
	/**
	 * 同步考勤信息 (哺乳假) 到建模数据中
	 * 
	 * @param startDate
	 * 					开始时间
	 * @param endDate
	 * 					结束时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceLactationToFormmode(String startDate, String endDate) {
		boolean flag = true;
		if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
			logger.info("syncAttendanceLactationToFormmode startDate and endDate is null");
		} else if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			String deadline = StringUtils.isBlank(startDate) ? endDate : startDate;
			flag = syncAttendanceLactationToFormmode(deadline);
		} else {
			int diffDay = differentDays(startDate, endDate);
			for (int i = 0; i <= diffDay; i++) {
				String deadline = getDateStr(getDateStr(startDate), i);
				boolean tempFlag = syncAttendanceLactationToFormmode(deadline);
				if (!tempFlag) 
					logger.info(String.format("syncAttendanceLactationToFormmode detail Failure: deadline = %s", 
							deadline));
			}
		}
		return flag;
	}
	
	/**
	 * 同步考勤信息 (哺乳假) 到建模数据中
	 * 
	 * @param date
	 * 					指定日期
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceLactationToFormmode(Date date) {
		return syncAttendanceLactationToFormmode(getDateStr(date));
	}
	
	/**
	 * 同步考勤信息 (哺乳假) 到建模数据中
	 * 
	 * @param dateStr
	 * 					指定日期
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceLactationToFormmode(String dateStr) {
		boolean flag = true;
		List<Map<String, String>> list = null;
		try {
			if (StringUtils.isBlank(dateStr)) {
				logger.info("syncAttendanceLactationToFormmode date is null");
				return flag;
			}
			
			String modeId = getPropString("modeid");		// 模块 id
			String modeCreator = getPropString("modecreator");
															// 模块数据创建用户 id
			String tablename = FormModeUtils.getTablename(modeId);		// 模块表单
			String lactationtablename = FormModeUtils.getTablename(getPropString("lactationmodeid"));
			String lactationattendanceid = getPropString("lactationattendanceid");
			list = dao.getAttendanceByLactation(lactationtablename, dateStr);
			if (list == null || list.size() <= 0) {
				logger.info("syncAttendanceLactationToFormmode list is null.");
				return flag;
			}

			// 整理公共数据
			Map<String, String> mainMap = new HashMap<String, String>(9);
			mainMap.put("kqlx", lactationattendanceid);
			mainMap.put("ksrq", dateStr);
			mainMap.put("jsrq", dateStr);
			
			for (Map<String, String> map : list) {
				String xm = map.get("xm");
				String bm = map.get("bm");
				String gs = map.get("gs");
				String kssj = map.get("kssj");
				String jssj = map.get("jssj");
				
				// 整理非公共数据
				mainMap.put("xm", xm);
				mainMap.put("bm", bm);
				mainMap.put("gs", gs);
				mainMap.put("kssj", kssj);
				mainMap.put("jssj", jssj);
				
				boolean existFlag = queryAttendanceBySign(0, tablename, lactationattendanceid, xm, dateStr);
				if (existFlag) {
					logger.info(String.format("syncAttendanceLactationToFormmode is exist (userid = %s, date = %s)", 
							xm, dateStr));
					continue;
				}
				
				boolean tempFlag = saveAttendance("syncAttendanceLactationToFormmode", modeId, modeCreator, mainMap);
				if (!tempFlag) 
					logger.info(String.format("syncAttendanceLactationToFormmode Failure (userid = %s)",
						xm));
			}
		} catch (Exception e) {
			logger.error("syncAttendanceLactationToFormmode Exception: ", e);
			flag = false;
		} finally {
			if (list != null) list.clear();
		}
		return flag;
	}
	
	/**
	 * 同步考勤信息 (打卡 (签到, 签退), 请假, 出差, 销假, 出差调整, 哺乳假) 到考勤报表中
	 * 
	 * <p>同步考勤建模数据到报表中
	 * 
	 * @param date
	 * 					指定时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceToReport(Date date) {
		String dateStr = getDateStr(date);
		return syncAttendanceToReport(dateStr, dateStr);
	}
	
	/**
	 * 同步考勤信息 (打卡 (签到, 签退), 请假, 出差, 销假, 出差调整, 哺乳假) 到考勤报表中
	 * 
	 * <p>同步考勤建模数据到报表中
	 * 
	 * @param dateStr
	 * 					指定时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceToReport(String dateStr) {
		return syncAttendanceToReport(dateStr, dateStr);
	}
	
	/**
	 * 同步考勤信息 (打卡 (签到, 签退), 请假, 出差, 销假, 出差调整, 哺乳假) 到考勤报表中
	 * 
	 * <p>同步考勤建模数据到报表中
	 * 
	 * @param startDate
	 * 					开始时间
	 * @param endDate
	 * 					结束时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceToReport(String startDate, String endDate) {
		boolean flag = true;
		HrmScheduleReportManager reportManager = null;
		try {
			reportManager = new HrmScheduleReportManager();
			// 打卡 (签到, 签退), 请假, 出差, 销假, 出差调整, 哺乳假
			List<Integer> signTypes = new ArrayList<Integer>();
			signTypes.add(1);
			signTypes.add(2);
			signTypes.add(3);
			signTypes.add(4);
			signTypes.add(5);
			signTypes.add(6);
			flag = reportManager.syncAttendanceToReport(signTypes, startDate, endDate);
		} finally {
			if (reportManager != null) {
				reportManager.commit();
				reportManager = null;
			}
		}
		return flag;
	}
	
	/**
	 * 同步考勤信息 (打卡 (签到, 签退), 请假, 出差, 销假, 哺乳假) 到考勤报表中
	 * 
	 * <p>同步考勤建模数据到报表中
	 * 
	 * @param signTypes
	 * 					考勤信息类型
	 * @param startDate
	 * 					开始时间
	 * @param endDate
	 * 					结束时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceToReport(List<Integer> signTypes, String startDate,
			String endDate) {
		boolean flag = true;
		HrmScheduleReportManager reportManager = null;
		try {
			reportManager = new HrmScheduleReportManager();
			flag =  reportManager.syncAttendanceToReport(signTypes, startDate, endDate);
		} finally {
			if (reportManager != null) {
				reportManager.commit();
				reportManager = null;
			}
		}
		return flag;
	}
	
	/**
	 * 对于未打卡的人员进行短信提醒
	 * 
	 * @param type
	 * 					类型：0.早上   1.晚上   2.全部
	 * @param date
	 * 					指定时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean sendAttendanceSign(int type, Date date) {
		return sendAttendanceSign(type, getDateStr(date));
	}
	
	/**
	 * 对于未打卡的人员进行短信提醒
	 * 
	 * @param type
	 * 					类型：0.早上   1.晚上   2.全部
	 * @param dateStr
	 * 					指定时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean sendAttendanceSign(int type, String dateStr) {
		boolean flag = true;
		String task = "sendAttendanceSign";
		try {
			if (StringUtils.isBlank(dateStr)) {
				logger.info(task + " date is null");
				return flag;
			}
			
			String signType = getPropString("attendanceid");
			String tablename = FormModeUtils.getTablename(getPropString("modeid"));
			List<Map<String, String>> list = dao.getAttendanceByNoSign(type, signType,
					tablename, dateStr, HRM_SUBCOMPANY_ID);
			if (list == null || list.size() <= 0) {
				logger.info(task + " list is null.");
				return flag;
			}
			
			CustomSmsService css = new CustomSmsService();
			
			// 考勤打卡信息是否推送短信提醒标识：true.推送短信   false.不推送短信, 只将需要推送人信息记录到日志中
			String msgFlag = getPropString("scheduleremindflag");
			
			// 检测未打卡人数是否超过异常数量, 如果未配置默认异常数量为 100：是.提醒指定人员      否.提醒未打卡人员
			int size = 100;
			String sizeStr = getPropString("schedulesize");
			if (!StringUtils.isBlank(sizeStr)) size = Integer.parseInt(sizeStr);
			if (list.size() >= size) {
				logger.info(String.format(task + " list size = %s.", list.size()));
				
				// 用户打卡信息异常, 如果已配置指定用户, 发送信息给指定用户
				String userid = getPropString("schedulereminduser");
				if (StringUtils.isBlank(userid)) return flag;
				String msg = getPropString("scheduleremindmsg");
				Map<String, String> map = dao.getUser(userid);
				if (map == null || map.size() <= 0) return flag;
				String mobile = map.get("mobile");
				if (StringUtils.isBlank(mobile)) return flag;
				if (!StringUtils.isBlank(msgFlag) && "true".equals(msgFlag)) {
					css.sendSMS(userid, mobile, msg);
				} else {
					logger.info(String.format(task + " userid = %s, " + msg, userid));
				}
				return flag;
			}
			
			// 处理不需要进行未打卡提醒的情况
			list = handleAttendanceBySendSign(type, dateStr, list);
			logger.info(task + " type = " + type + ", size = " + list.size());
			
			String msg = "";			// 提醒信息模板
			if (type == 0) {
				msg = getPropString("scheduleremindammsg");
			} else {
				msg = getPropString("scheduleremindpmmsg");
			}
			msg = String.format(msg, dateStr);
			for (Map<String, String> map : list) {
				String userid = map.get("id");
				String mobile = map.get("mobile");
				if (StringUtils.isBlank(mobile)) continue;
				if (!StringUtils.isBlank(msgFlag) && "true".equals(msgFlag)) {
					logger.info(String.format(task + " userid = %s", userid));
					boolean tempFlag = css.sendSMS(userid, mobile, msg);
					if (!tempFlag) logger.info(String.format(task + " sendSMS Failure (userid = %s)", 
							userid));
				} else {
					logger.info(String.format(task + " userid = %s, "
							+ msg, userid));
				}
			}
		} catch (Exception e) {
			logger.error(task + " Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	
	/**
	 * 保存考勤信息 (打卡) 到建模数据中
	 * 
	 * @param type
	 * 					操作类型：0.早上   1.晚上   2.全部
	 * @param list
	 * 					考勤数据
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean saveAttendanceSign(int type, List<Map<String, String>> list) {
		boolean flag = true;
		try {
			String modeId = getPropString("modeid");		// 模块 id
			String modeCreator = getPropString("modecreator");
															// 模块数据创建用户 id
			String tablename = FormModeUtils.getTablename(modeId);		// 模块表单
			String attendanceid = getPropString("attendanceid");
			for (Map<String, String> map : list) {
				if (map == null || map.size() <= 0) continue;
				
				Map<String, String> mainMap = createAttendanceDataBySign(type, attendanceid, map);
				if (mainMap == null || mainMap.size() <= 0) continue;
				String userid = mainMap.get("xm");			// 用户 id
				
				// 处理打开时间, 如果时间为空则直接返回
				String deadline;							// 打卡时间
				if (type == 2) {
					deadline = StringUtils.isBlank(mainMap.get("ksrq")) ? mainMap.get("jsrq") : mainMap.get("ksrq");
				} else if (type == 1) {
					deadline = mainMap.get("jsrq");
				} else if (type == 0) {
					deadline = mainMap.get("ksrq");
				} else {
					deadline = null;
				}
				if (StringUtils.isBlank(deadline)) continue;
				
				// 检测打卡数据, 操作类型为全部时, 需要进行比对
				// 1.如果对比完全相等则直接返回, 否则进入第二步
				// 2.判断建模中是否存在, 不存在则新建, 否则修改
				boolean existFlag;
				if (type == 2) {
					Map<String, String> oldMap = queryAttendanceBySignToMap(type, tablename, attendanceid, userid, deadline);
					boolean compareFlag = compareAttendanceBySign(oldMap, mainMap);
					if (isDebugEnabled()) logger.info("compareAttendanceBySign compareFlag: " + compareFlag);
					if (compareFlag) continue;		// 同步数据与已存在数据完全相等, 直接返回
					existFlag = queryAttendanceBySign(type, tablename, attendanceid, userid, deadline);
					
				// 操作类型为其他时, 不需要进行比对
				} else {
					existFlag = queryAttendanceBySign(type, tablename, attendanceid, userid, deadline);
				}
				
				// 保存打卡数据
				boolean tempFlag = true;
				if (!existFlag) {
					tempFlag = saveAttendance("saveAttendanceSign", modeId, modeCreator, mainMap);
				} else {
					tempFlag = updateAttendanceBySign(type, tablename, attendanceid, mainMap);
				}
				if (!tempFlag) 
					logger.info(String.format("saveAttendanceSign Failure (workcode = %s)",
							map.get("workcode")));
			}
		} catch (Exception e) {
			logger.error("saveAttendanceSign Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 保存考勤信息 (包括请假, 出差, 销假等) 到建模数据中
	 * 
	 * @param list
	 * 					考勤数据集
	 * @return boolean：true.操作成功      false.操作失败
	 */
	private boolean saveAttendanceWorkflow(List<Map<String, String>> list) {
		boolean flag = true;
		try {
			String modeId = getPropString("modeid");		// 模块 id
			String modeCreator = getPropString("modecreator");
															// 模块数据创建用户 id
			for (Map<String, String> map : list) {
				if (map == null || map.size() <= 0) continue;
				boolean tempFlag = saveAttendance("saveAttendanceWorkflow", modeId, modeCreator, map);
				if (!tempFlag) 
					logger.info(String.format("syncAttendanceWorkflowToFormmode Failure (userid = %s, requestid = %s.)",
						map.get("xm"), map.get("xglc")));
			}
		} catch (Exception e) {
			logger.error("saveAttendanceWorkflow Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 保存考勤信息 (免打卡人员) 到建模数据中
	 * 
	 * @param dateStr
	 * 					指定时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	private boolean saveAttendanceSignByDefault(String dateStr) {
		boolean flag = true;
		try {
			String ignoreusers = getPropString("ignoreusers");
			if (StringUtils.isBlank(ignoreusers)) return flag;
			String[] ignoreusersArr = ignoreusers.split(",");
			if (ignoreusersArr == null || ignoreusersArr.length <= 0) return flag;
			
			String attendanceid = getPropString("attendanceid");
															// 考勤类型
			String modeId = getPropString("modeid");		// 模块 id
			String modeCreator = getPropString("modecreator");
															// 模块数据创建用户 id
			String tablename = FormModeUtils.getTablename(getPropString("modeid"));
			
			// 整理公共数据
			Map<String, String> attendanceScheduleTimes = getAttendanceScheduleTimes(dao.getAttendanceScheduleTimes("4",
					HRM_SUBCOMPANY_ID, dateStr), dateStr);
			String starttime = attendanceScheduleTimes.get("starttime");
			String endtime = attendanceScheduleTimes.get("endtime");
			Map<String, String> mainMap = new HashMap<String, String>(9);
			mainMap.put("kqlx", attendanceid);
			mainMap.put("ksrq", dateStr);
			mainMap.put("kssj", starttime);
			mainMap.put("jsrq", dateStr);
			mainMap.put("jssj", endtime);
			mainMap.put("ts", "1");
			
			for (String user : ignoreusersArr) {
				if (StringUtils.isBlank(user)) continue;
				
				Map<String, String> userMap = dao.getUserByLoginid(user);
				if (userMap == null || userMap.size() <= 0) continue;
				String userid = userMap.get("userid");
				String subcompanyid = userMap.get("subcompanyid");
				String departmentid = userMap.get("departmentid");
				if (StringUtils.isBlank(userid)) continue;
				
				// 整理非公共数据
				mainMap.put("xm", userid);
				mainMap.put("bm", departmentid);
				mainMap.put("gs", subcompanyid);
				
				boolean existFlag = queryAttendanceBySign(0, tablename, attendanceid, userid, dateStr);
				if (existFlag) {
					logger.info(String.format("saveAttendanceSignByDefault is exist (userid = %s, syncDate = %s)", 
							userid, dateStr));
					continue;
				}
				
				boolean tempFlag = saveAttendance("saveAttendanceSignByDefault", modeId, modeCreator, mainMap);
				if (!tempFlag) logger.info(String.format("saveAttendanceSignByDefault Failure (userid = %s)",
						mainMap.get("xm")));
			}
		} catch (Exception e) {
			logger.error("saveAttendanceSignByDefault Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	
	/**
	 * 查询考勤 (打卡) 信息是否存在
	 * 
	 * @param type
	 * 					查询类型：0.查询开始日期   1.查询结束日期   2.查询如果开始日期为空则匹配结束日期
	 * @param tablename
	 * 					表单名称
	 * @param attendanceid
	 * 					考勤类型
	 * @param userid
	 * 					用户 id
	 * @param deadline
	 * 					截止日期
	 * @return boolean：true.存在      false.不存在
	 */
	private boolean queryAttendanceBySign(int type, String tablename, 
			String attendanceid, String userid, String deadline) {
		boolean flag = false;
		try {
			String sql = "select 1 from " + tablename + " "
					+ "where xm = '" + userid + "' and kqlx = '" + attendanceid + "' ";
			
			if (type == 0) {
				sql += "and nvl(ksrq, jsrq) = '" + deadline + "' ";
			} else if (type == 1) {
				sql += "and nvl(ksrq, jsrq) = '" + deadline + "' ";
			} else if (type == 2) {
				sql += "and nvl(ksrq, jsrq) = '" + deadline + "' ";
			}
			
			if (isDebugEnabled()) logger.info("queryAttendanceBySign sql: " + sql);
			rs.execute(sql);
			if (rs.next()) flag = true;
		} catch (Exception e) {
			logger.error("queryAttendanceBySign Exception: ", e);
		}
		return flag;
	}
	
	/**
	 * 查询考勤 (打卡) 信息
	 * 
	 * @param type
	 * 					查询类型：0.查询开始日期   1.查询结束日期   2.查询如果开始日期为空则匹配结束日期
	 * @param tablename
	 * 					表单名称
	 * @param attendanceid
	 * 					考勤类型
	 * @param userid
	 * 					用户 id
	 * @param deadline
	 * 					截止日期
	 * @return 打卡考勤信息
	 */
	private Map<String, String> queryAttendanceBySignToMap(int type, String tablename, 
			String attendanceid, String userid, String deadline) {
		Map<String, String> map = null;
		try {
			String sql = "select * from " + tablename + " "
					+ "where xm = '" + userid + "' and kqlx = '" + attendanceid + "' ";
			
			if (type == 0) {
				sql += "and nvl(ksrq, jsrq) = '" + deadline + "' ";
			} else if (type == 1) {
				sql += "and nvl(ksrq, jsrq) = '" + deadline + "' ";
			} else if (type == 2) {
				sql += "and nvl(ksrq, jsrq) = '" + deadline + "' ";
			}
			
			if (isDebugEnabled()) logger.info("queryAttendanceBySignToMap sql: " + sql);
			rs.execute(sql);
			if (rs.next()) {
				String ksrq = rs.getString("ksrq");
				String kssj = rs.getString("kssj");
				String jsrq = rs.getString("jsrq");
				String jssj = rs.getString("jssj");
				
				map = new HashMap<String, String>();
				map.put("ksrq", ksrq);
				map.put("kssj", kssj);
				map.put("jsrq", jsrq);
				map.put("jssj", jssj);
			}
		} catch (Exception e) {
			logger.error("queryAttendanceBySignToMap Exception: ", e);
		}
		return map;
	}
	
	/**
	 * 更新考勤 (打卡) 信息
	 * 
	 * @param type
	 * 					更新类型：0.更新开始日期, 时间   1.更新结束日期, 时间   2.更新开始日期, 时间和结束日期, 时间
	 * @param tablename
	 * 					表单名称
	 * @param attendanceid
	 * 					考勤类型
	 * @param map
	 * 					考勤数据
	 * @return boolean：true.存在      false.不存在
	 */
	private boolean updateAttendanceBySign(int type, String tablename,
			String attendanceid, Map<String, String> map) {
		boolean flag = true;
		try {
			String userid = map.get("xm");				// 用户 id
			String ksrq = map.get("ksrq");				// 开始日期
			String kssj = map.get("kssj");				// 开始时间
			String jsrq = map.get("jsrq");				// 结束日期
			String jssj = map.get("jssj");				// 结束时间
			
			// 拼接语句
			String sqlSet = "set ";
			String sqlCondition = "where xm = '" + userid + "' and kqlx = '" + attendanceid + "' ";
			if (type == 0) {
				if (StringUtils.isBlank(ksrq)) return flag;
				sqlSet += "ksrq = '" + ksrq + "', kssj = '" + kssj + "' ";
				sqlCondition += "and nvl(ksrq, jsrq) = '" + ksrq + "' ";
			} else if (type == 1) {
				if (StringUtils.isBlank(jsrq)) return flag;
				sqlSet += "jsrq = '" + jsrq + "', jssj = '" + jssj + "' ";
				sqlCondition += "and nvl(ksrq, jsrq) = '" + jsrq + "' ";
			} else if (type == 2) {
				if (StringUtils.isBlank(ksrq) && StringUtils.isBlank(jsrq)) return flag;
				if (StringUtils.isBlank(ksrq)) {
					sqlSet += "jsrq = '" + jsrq + "', jssj = '" + jssj + "' ";
					sqlCondition += "and nvl(ksrq, jsrq) = '" + jsrq + "' ";
				} else if (StringUtils.isBlank(jsrq)) {
					sqlSet += "ksrq = '" + ksrq + "', kssj = '" + kssj + "' ";
					sqlCondition += "and nvl(ksrq, jsrq) = '" + ksrq + "' ";
				} else {
					sqlSet += "ksrq = '" + ksrq + "', kssj = '" + kssj + "', "
							+ "jsrq = '" + jsrq + "', jssj = '" + jssj + "' ";
					sqlCondition += "and nvl(ksrq, jsrq) = '" + ksrq + "' ";
				}
			}
			String sql = "update " + tablename + " " + sqlSet + " " + sqlCondition;
			if (isDebugEnabled()) logger.info("updateAttendanceBySign sql: " + sql);
			flag = rs.executeUpdate(sql);
		} catch (Exception e) {
			logger.error("updateAttendanceBySign Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 创建考勤 (打卡) 主表数据
	 * 
	 * @param type
	 * 					类型：0.保存开始日期, 时间   1.保存结束日期, 时间   2.保存开始日期, 时间和结束日期, 时间
	 * @param map
	 * 					考勤数据
	 * @return 考勤主表数据
	 */
	private Map<String, String> createAttendanceDataBySign(int type, String attendanceid, 
			Map<String, String> map) {
		String subcompanyid = "";					// 分部 id
		String departmentid = "";					// 部门 id
		String userid = "";							// 用户 id
		String workcode = map.get("workcode");		// 用户账号
		Map<String, String> userMap = dao.getUserByLoginid(map.get("workcode"));
		if (userMap == null || userMap.size() <= 0) {
			logger.info(String.format("createAttendanceDataBySign workcode = %s user is not exist",
					workcode));
			return null;
		} else {
			subcompanyid = userMap.get("subcompanyid");
			departmentid = userMap.get("departmentid");
			userid = userMap.get("userid");
		}
		String[] startTimes = getTimes(map.get("starttime"));
													// 开始时间
		String[] endTimes = getTimes(map.get("endtime"));
													// 结束时间
		
		Map<String, String> mainMap = new HashMap<String, String>();
		mainMap.put("kqlx", attendanceid);
		mainMap.put("gs", subcompanyid);
		mainMap.put("bm", departmentid);
		mainMap.put("xm", userid);
		
		if (type == 0) {
			if (startTimes == null) return mainMap;
			mainMap.put("ksrq", startTimes[0]);
			mainMap.put("kssj", startTimes[1]);
		} else if (type == 1) {
			if (endTimes == null) return mainMap;
			mainMap.put("jsrq", endTimes[0]);
			mainMap.put("jssj", endTimes[1]);
		} else if (type == 2) {
			if (startTimes == null && endTimes == null) return mainMap;
			if (startTimes != null) {
				mainMap.put("ksrq", startTimes[0]);
				mainMap.put("kssj", startTimes[1]);
			}
			if (endTimes != null) {
				mainMap.put("jsrq", endTimes[0]);
				mainMap.put("jssj", endTimes[1]);
			}
		}
		return mainMap;
	}
	
	/**
	 * 处理存在考勤流程 (请假, 出差) 类型数据, 并且时间段在打卡时间段内的数据
	 * 
	 * <p>此类人员不进行未打卡提醒
	 * 
	 * @param type
	 * 					类型：0.早上   1.晚上   2.全部
	 * @param dateStr
	 * 					指定时间
	 * @param list
	 * 					未打卡人员数据
	 * @return 处理后的未打卡人员数据
	 */
	private List<Map<String, String>> handleAttendanceBySendSign(int type, String dateStr, 
			List<Map<String, String>> list) {
		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
		
		// 获取排班数据
		Map<String, Object> oldMap = new HashMap<String, Object>(2);
		Map<String, String> attendanceScheduleTimes = getAttendanceScheduleTimes(dao.getAttendanceScheduleTimes("4",
				HRM_SUBCOMPANY_ID, dateStr), dateStr);
		oldMap.put("kssj", attendanceScheduleTimes.get("starttime"));
		oldMap.put("jssj", attendanceScheduleTimes.get("endtime"));
		// 获取请假数据
		Map<String, String> attendanceMapLeave = getAttendanceData("2", true);
		// 获取出差数据
		Map<String, String> attendanceMapEvection = getAttendanceData("4", true);
					
		for (Map<String, String> map : list) {
			String userid = map.get("id");
			
			// 考勤流程总数据
			List<Map<String, Object>> scheduleList = new ArrayList<Map<String, Object>>();
			
			// 获取请假类型数据 
			String tablenameLeave = attendanceMapLeave.get("tablename");
			String sqlFieldsLeave = attendanceMapLeave.get("sqlFields");
			String sqlConditionLeave = attendanceMapLeave.get("sqlCondition");
			List<Map<String, Object>> listLeave = dao.getAttendanceByWorkflows(tablenameLeave,
					sqlFieldsLeave, sqlConditionLeave, userid, dateStr);
			if (listLeave != null && listLeave.size() > 0) scheduleList.addAll(listLeave);
			
			// 获取出差类型数据
			String tablenameEvection = attendanceMapEvection.get("tablename");
			String sqlFieldsEvection = attendanceMapEvection.get("sqlFields");
			String sqlConditionEvection = attendanceMapEvection.get("sqlCondition");
			List<Map<String, Object>> listEvection = dao.getAttendanceByWorkflows(tablenameEvection,
					sqlFieldsEvection, sqlConditionEvection, userid, dateStr);
			if (listEvection != null && listEvection.size() > 0) scheduleList.addAll(listEvection);
			
			// 如果考勤流程数据为空表示需要进行未打卡提醒
			if (scheduleList == null || scheduleList.size() <= 0) {
				newList.add(map);
				continue;
			}
			
			// type 为 0：早上, 如果排班时间大于考勤流程时间, 表示用户以进行流程提交不需要进行未打卡提醒
			// type 为 1：晚上, 如果排班时间小于考勤流程时间, 表示用户以进行流程提交不需要进行未打卡提醒
			boolean compareFlag = false;
			for (Map<String, Object> scheduleMap : scheduleList) {
				boolean signFlag = compareScheduleSign(type, oldMap, scheduleMap);
				if (signFlag) {
					compareFlag = true;
					break;
				}
			}
			if (!compareFlag) newList.add(map);
		}
		return newList;
	}
	
	/**
	 * 比较考勤流程数据大小
	 * 
	 * @param type
	 * 					类型：0.早上   1.晚上 
	 * @param oldMap
	 * 					考勤流程数据
	 * @param newMap
	 * 					考勤流程数据
	 * @return boolean：
	 * 					1.(type 为 0) true.old 大于  new      false.new 大于 old
	 * 					2.(type 为 1) true.new 大于 old       false.old 大于 new
	 */
	private boolean compareScheduleSign(int type, Map<String, Object> oldMap, 
			Map<String, Object> newMap) {
		boolean flag = false;
		if (oldMap == null || newMap == null) return flag;
		String sj = "";
		if (type == 0) {
			sj = "jssj";
		} else if (type == 1) {
			sj = "kssj";
		} else {}
		if (StringUtils.isBlank(sj)) return flag;
		String oldSj = Util.null2String(oldMap.get(sj) == null ? "" : oldMap.get(sj) + "", "");
		String newSj = Util.null2String(newMap.get(sj) == null ? "" : newMap.get(sj) + "", "");
		if (!StringUtils.isBlank(oldSj)
				&& !StringUtils.isBlank(newSj)) {
			boolean signFlag = newSj.compareTo("12:00") <= 0;
			// 早上
			if (type == 0 && signFlag) {
				if (oldSj.compareTo(newSj) > 0) flag = true;
			
			// 晚上
			} else if (type == 1 && !signFlag) {
				if (oldSj.compareTo(newSj) < 0) flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 比较建模已存在数据与当前同步用户的打卡数据是否完全相等, 完全相等则不进行操作
	 * 
	 * @param oldMap
	 * 					建模打卡数据
	 * @param newMap
	 * 					同步打卡数据
	 * @return boolean：true.完全相等      false.不完全相等
	 */
	private boolean compareAttendanceBySign(Map<String, String> oldMap, 
			Map<String, String> newMap) {
		boolean flag = false;
		if (oldMap == null || newMap == null) return flag;
		String oldKsrq = Util.null2String(oldMap.get("ksrq"), "");
		String oldKssj = Util.null2String(oldMap.get("kssj"), "");
		String oldJsrq = Util.null2String(oldMap.get("jsrq"), "");
		String oldJssj = Util.null2String(oldMap.get("jssj"), "");
		String newKsrq = Util.null2String(newMap.get("ksrq"), "");
		String newKssj = Util.null2String(newMap.get("kssj"), "");
		String newJsrq = Util.null2String(newMap.get("jsrq"), "");
		String newJssj = Util.null2String(newMap.get("jssj"), "");
		if (oldKsrq.equals(newKsrq)
				&& oldKssj.equals(newKssj)
				&& oldJsrq.equals(newJsrq)
				&& oldJssj.equals(newJssj)) {
			flag = true;
		}
		return flag;
	}
	
	
	/**
	 * 将时间类型字符串拆分为日期, 时间字符串数组
	 * @param times
	 * @return
	 */
	private String[] getTimes(String times) {
		String dateStr = "";
		String timeStr = "";
		if (!StringUtils.isBlank(times)) {
			String[] timeArr = times.split(" ");
			if (timeArr != null && timeArr.length == 2) {
				dateStr = timeArr[0];
				timeStr = timeArr[1];
			}
		}
		if (StringUtils.isBlank(dateStr) && StringUtils.isBlank(timeStr)) {
			return null;
		} else {
			return new String[]{dateStr, timeStr};
		}
	}
	
	/**
	 * 获取考勤工作时间设置
	 * 
	 * @param map
	 * 					系统考勤时间设置
	 * @param dateStr
	 * 					指定时间
	 * @return 工作时间配置
	 */
	public Map<String, String> getAttendanceScheduleTimes(Map<String, Object> map,
			String dateStr) {
		String starttime = "";
		String endtime = "";
		// 不存在则使用默认时间
		if (map == null || map.size() <= 0) {
			starttime = getPropString("schedulestarttime");
			endtime = getPropString("scheduleendtime");
		} else {
			int dayForWeek = getWeekOfDate(getDateStr(dateStr));
			if (dayForWeek == 1) {
				starttime = map.get("monstarttime1") == null ? null : map.get("monstarttime1") + "";
				endtime =  map.get("monendtime2") == null ? null : map.get("monendtime2") + "";
			} else if (dayForWeek == 2) {
				starttime = map.get("tuestarttime1") == null ? null : map.get("tuestarttime1") + "";
				endtime =  map.get("tueendtime2") == null ? null : map.get("tueendtime2") + "";
			} else if (dayForWeek == 3) {
				starttime = map.get("wedstarttime1") == null ? null : map.get("wedstarttime1") + "";
				endtime =  map.get("wedendtime2") == null ? null : map.get("wedendtime2") + "";
			} else if (dayForWeek == 4) {
				starttime = map.get("thustarttime1") == null ? null : map.get("thustarttime1") + "";
				endtime =  map.get("thuendtime2") == null ? null : map.get("thuendtime2") + "";
			} else if (dayForWeek == 5) {
				starttime = map.get("fristarttime1") == null ? null : map.get("fristarttime1") + "";
				endtime =  map.get("friendtime2") == null ? null : map.get("friendtime2") + "";
			} else if (dayForWeek == 6) {
				starttime = map.get("satstarttime1") == null ? null : map.get("satstarttime1") + "";
				endtime =  map.get("satendtime2") == null ? null : map.get("satendtime2") + "";
			} else if (dayForWeek == 7) {
				starttime = map.get("sunstarttime1") == null ? null : map.get("sunstarttime1") + "";
				endtime =  map.get("sunendtime2") == null ? null : map.get("sunendtime2") + "";
			} else {
			}
		}
		if (StringUtils.isBlank(starttime)) starttime = getPropString("schedulestarttime");
		if (StringUtils.isBlank(endtime)) endtime = getPropString("scheduleendtime");
		
		Map<String, String> result = new HashMap<String, String>(2);
		result.put("starttime", starttime);
		result.put("endtime", endtime);
		return result;
	}
	
}
