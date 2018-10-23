package com.weaver.ningb.direct.manager.integration;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import weaver.conn.RecordSetTrans;

import com.weaver.ningb.core.util.FormModeUtils;
import com.weaver.ningb.core.util.WorkflowUtils;

/**
 * 考勤报表处理 Manager
 * 
 * @author liberal
 *
 */
public class HrmScheduleReportManager extends HrmScheduleAbstractManager {

	private RecordSetTrans rst;
	
	
	public HrmScheduleReportManager() {
		rst = new RecordSetTrans();
		rst.setAutoCommit(false);
	}
	
	
	/**
	 * 同步考勤信息 (打卡 (签到, 签退), 请假, 出差, 销假, 哺乳假, 出差调整) 到考勤报表中
	 * 
	 * @param signTypes
	 * 					考勤信息类型
	 * @param startDate
	 * 					开始时间
	 * @param endDate
	 * 					结束时间
	 * @return boolean：操作成功      false.操作失败
	 */
	public boolean syncAttendanceToReport(List<Integer> signTypes,
			String startDate, String endDate) {
		boolean flag = true;
		if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
			logger.info("syncAttendanceToReport startDate and endDate is null");
		} else if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			String deadline = StringUtils.isBlank(startDate) ? endDate : startDate;
			flag = syncAttendanceToReport(signTypes, deadline);
		} else {
			int diffDay = differentDays(startDate, endDate);
			for (int i = 0; i <= diffDay; i++) {
				String deadline = getDateStr(getDateStr(startDate), i);
				boolean tempFlag = syncAttendanceToReport(signTypes, deadline);
				if (!tempFlag) 
					logger.info(String.format("syncAttendanceToReport detail Failure: deadline = %s", 
							deadline));
			}
		}
		return flag;
	}
	
	/**
	 * 同步考勤信息 (打卡 (签到, 签退), 请假, 出差, 销假, 哺乳假, 出差调整) 到考勤报表中
	 * 
	 * @param signTypes
	 * 					考勤信息类型
	 * @param dateStr
	 * 					指定时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncAttendanceToReport(List<Integer> signTypes, String dateStr) {
		boolean flag = true;
		try {
			if (StringUtils.isBlank(dateStr)) {
				logger.info("syncAttendanceToReport date is null");
				return flag;
			}
			
			String tablename = FormModeUtils.getTablename(getPropString("modeid"));
			for (int iSignType : signTypes) {
				String signType = getAttendanceSignType(iSignType);
				if (StringUtils.isBlank(signType)) {
					logger.info(String.format("syncAttendanceToReport signType is null (iSignType = %s)", 
							iSignType));
					continue;
				}
				
				List<Map<String, String>> list = dao.getAttendanceByOther(iSignType + "", 
						tablename, dateStr);
				if (list == null || list.size() <= 0) {
					logger.info(String.format("syncAttendanceToReport list is null (signType = %s)", 
							signType));
					continue;
				}
				
				boolean tempFlag = saveAttendanceReports(list, signType);
				if (!tempFlag) logger.info(String.format("syncAttendanceToReport saveAttendanceReports Failure (signType = %s)", 
						signType));
				
				// clear
				if (list != null) list.clear();
			}
		} catch (Exception e) {
			logger.error("syncAttendanceToReport Failure: ", e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 提交事务
	 * @return boolean：true.提交成功      false.提交失败
	 * @see RecordSetTrans#commit()
	 */
	public boolean commit() {
		this.rst.commit();
		return true;
	}
	
	
	/**
	 * 保存考勤数据集到考勤报表中
	 * 
	 * @param list
	 * 					考勤数据集
	 * @param signType
	 * 					考勤类型：打卡 (签到, 签退), 请假, 出差, 销假, 哺乳假
	 * @return boolean：true.保存成功      false.保存失败
	 */
	private boolean saveAttendanceReports(List<Map<String, String>> list, String signType) {
		boolean flag = true;
		for (Map<String, String> map : list) {
			boolean tempFlag = saveAttendanceReport(map, signType);
			if (!tempFlag) logger.info(String.format("saveAttendanceReport Failure (signType = %s)", 
					signType));
		}
		return flag;
	}
	
	/**
	 * 保存考勤数据到考勤报表中
	 * 
	 * @param map
	 * 					考勤数据
	 * @param signType
	 * 					考勤类型
	 * @return boolean：true.操作成功      false.操作失败
	 */
	private boolean saveAttendanceReport(Map<String, String> map, String signType) {
		boolean flag = true;
		if (StringUtils.isBlank(signType)) {
			logger.info("saveAttendanceReport signType is null.");
			return flag;
		} else if ("sign".equals(signType)) {
			flag = saveAttendanceReportBySign(map);
		} else if ("leave".equals(signType)) {
			flag = saveAttendanceReportByLeave(map);
		} else if ("evection".equals(signType)) {
			flag = saveAttendanceReportByEvection(map);
		} else if ("absence".equals(signType)) {
			flag = saveAttendanceReportByAbsence(map);
		} else if ("evectionadjust".equals(signType)) {
			flag = saveAttendanceReportByEvectionAdjust(map);
		} else if ("lactation".equals(signType)) {
			flag = saveAttendanceReportByLactation(map);
		} else {
		}
		return flag;
	}
	
	/**
	 * 保存考勤数据 (打卡 (签到, 签退)) 到考勤报表中
	 * 
	 * @param map
	 * 					考勤数据
	 * @return boolean：true.操作成功      false.操作失败
	 */
	private boolean saveAttendanceReportBySign(Map<String, String> map) {
		boolean flag = true;
		try {
			String userType = "1";			// 用户类型
			String signInType = "1";		// 签到类型
			String signOutType = "2";		// 签退类型
			String clientAddress = "0";		// 客户端 ip
			String isInCom = "1";			// 是否来自外部
			
			String userid = map.get("xm");
			String ksrq = map.get("ksrq");
			String kssj = map.get("kssj");
			String jsrq = map.get("jsrq");
			String jssj = map.get("jssj");
			
			String sql = "insert into HrmScheduleSign(userid, userType, signType, signDate, signTime, clientAddress, isInCom) "
					+ "values(?, ?, ?, ?, ?, ?, ?)";
			String sqlUpdate = "update HrmScheduleSign set signTime = ? where id = ?";
			
			// 签到
			if (!StringUtils.isBlank(ksrq) && !StringUtils.isBlank(kssj)) {
				boolean existFlag = checkScheduleSign(userid, userType, signInType, ksrq, isInCom);
				if (!existFlag) {
					Object[] argsIn = new Object[]{userid, userType, signInType, ksrq, kssj, clientAddress, isInCom};
					if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportBySign signIn sql: " + sql.replaceAll("\\?", "%s"), 
							argsIn));
					flag = rst.executeUpdate(sql, argsIn);
				} else {
					Map<String, String> checkSignMap = checkScheduleSignToMap(userid, userType, signInType, ksrq, isInCom);
					// 比较考勤报表中已存在时间是否大于当前时间, 不大于则不进行操作
					boolean existOpFlag = compareScheduleSign(checkSignMap.get("signTime"), kssj, "gt");
					// 需要更新当前时间
					if (existOpFlag) {
						Object[] argsUpdateIn = new Object[]{kssj, checkSignMap.get("id")};
						if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportBySign signIn update sql: " + sqlUpdate.replaceAll("\\?", "%s"),
								argsUpdateIn));
						flag = rst.executeUpdate(sqlUpdate, argsUpdateIn);
					} else {
						if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportBySign signIn is exist (userid = %s, signType = %s, signDate = %s).", 
								userid, signInType, ksrq));
					}
				}
			}
			
			// 签退
			if (!StringUtils.isBlank(jsrq) && !StringUtils.isBlank(jssj)) {
				boolean existFlag = checkScheduleSign(userid, userType, signOutType, jsrq, isInCom);
				if (!existFlag) {
					Object[] argsOut = new Object[]{userid, userType, signOutType, jsrq, jssj, clientAddress, isInCom};
					if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportBySign signOut sql: " + sql.replaceAll("\\?", "%s"), 
							argsOut));
					flag = rst.executeUpdate(sql, argsOut);
				} else {
					Map<String, String> checkSignMap = checkScheduleSignToMap(userid, userType, signOutType, jsrq, isInCom);
					// 比较考勤报表中已存在时间是否小于当前时间, 不小于则不进行操作
					boolean existOpFlag = compareScheduleSign(checkSignMap.get("signTime"), jssj, "lt");
					// 需要更新当前时间
					if (existOpFlag) {
						Object[] argsUpdateIn = new Object[]{jssj, checkSignMap.get("id")};
						if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportBySign signOut update sql: " + sqlUpdate.replaceAll("\\?", "%s"),
								argsUpdateIn));
						flag = rst.executeUpdate(sqlUpdate, argsUpdateIn);
					} else {
						if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportBySign signOut is exist (userid = %s, signType = %s, signDate = %s).", 
								userid, signOutType, jsrq));
					}
				}
			}
		} catch (Exception e) {
			logger.error("saveAttendanceReportBySign Exception: ", e);
		}
		return flag;
	}
	
	/**
	 * 保存考勤数据 (请假) 到考勤报表中
	 * 
	 * @param map
	 * 					考勤数据
	 * @return boolean：true.操作成功      false.操作失败
	 */
	private boolean saveAttendanceReportByLeave(Map<String, String> map) {
		boolean flag = true;
		try {
			String id = map.get("id");
			String resourceid = map.get("xm");
			String departmentid = map.get("bm");
			String fromDate = map.get("ksrq");
			String fromTime = map.get("kssj");
			String toDate = map.get("jsrq");
			String toTime = map.get("jssj");
			String requestid = map.get("xglc");
			
			// 处理用户申请了两条相同日期和时间的请假流程的情况
			boolean flagCheck = false;
			String sqlCheck = "select 1 from Bill_BoHaiLeave where resourceid = ? and fromDate = ? "
					+ "and fromTime = ? and toDate = ? and toTime = ?";
			Object[] argsCheck = new Object[]{resourceid, fromDate, fromTime, toDate, toTime};
			rs.executeQuery(sqlCheck, argsCheck);
			if (rs.next()) flagCheck = true;
			if (flagCheck) {
				logger.info(String.format("saveAttendanceReportByLeave is exist, sql: " + sqlCheck.replaceAll("\\?", "%s"),
						argsCheck));
				return flagCheck;
			}
			
			// 获取请假相关说明信息
			String leaveType = null;
			String leaveDays = "0";
			String leaveReason = null;
			String leaveTablename = WorkflowUtils.getTablenameByRequestid(requestid);
			String leaveSql = "select * from " + leaveTablename + " "
					+ "where requestid = '" + requestid + "'";
			rs.execute(leaveSql);
			if (rs.next()) {
				leaveType = rs.getString("qjlx2");
				leaveDays = rs.getString("sjqjsjhj");
				if (StringUtils.isBlank(leaveDays)) leaveDays = "0";
				leaveReason = rs.getString("qjsy");
			}
			
			// 获取用户上级
			String manager = "";
			String hrmSql = "select * from hrmresource where id = '" + resourceid + "'";
			rs.execute(hrmSql); 
			if (rs.next()) manager = rs.getString("managerid");
			
			// 保存请假信息
			String sql = "insert into Bill_BoHaiLeave(resourceid, departmentid, leaveType, newleavetype, fromDate, fromTime, toDate, toTime,"
					+ " leaveDays, leaveReason, requestid, manager) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			Object[] args = new Object[]{resourceid, departmentid, leaveType, leaveType, fromDate, fromTime, toDate, 
					toTime, leaveDays, leaveReason, requestid, manager};
			if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportByLeave sql: " + sql.replaceAll("\\?", "%s"),
					args));
			flag = rst.executeUpdate(sql, args);
			
			// 全部销假时会删除对应的请假流程数据, 更新标识以避免重复数据
			if (flag) execLog(id, "0", "成功");
		} catch (Exception e) {
			logger.error("saveAttendanceReportByLeave Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 保存考勤数据 (出差) 到考勤报表中
	 * 
	 * @param map
	 * 					考勤数据
	 * @return boolean：true.操作成功      false.操作失败
	 */
	private boolean saveAttendanceReportByEvection(Map<String, String> map) {
		boolean flag = true;
		try {
			String resourceid = map.get("xm");
			String departmentid = map.get("bm");
			String fromDate = map.get("ksrq");
			String fromTime = map.get("kssj");
			String toDate = map.get("jsrq");
			String toTime = map.get("jssj");
			String requestid = map.get("xglc");

			// 处理用户申请了两条相同日期和时间的出差流程的情况
			boolean flagCheck = false;
			String sqlCheck = "select 1 from Bill_BoHaiEvection where resourceid = ? and fromDate = ? "
					+ "and fromTime = ? and toDate = ? and toTime = ?";
			Object[] argsCheck = new Object[]{resourceid, fromDate, fromTime, toDate, toTime};
			rs.executeQuery(sqlCheck, argsCheck);
			if (rs.next()) flagCheck = true;
			if (flagCheck) {
				logger.info(String.format("saveAttendanceReportByEvection is exist, sql: " + sqlCheck.replaceAll("\\?", "%s"),
						argsCheck));
				return flagCheck;
			}
			
			// 获取出差相关说明信息
			String evectionReason = "";
//			String evectionTablename = workflowManager.getTablenameByRequestid(requestid);
//			String evectionSql = "select * from " + evectionTablename + " "
//					+ "where requestid = '" + requestid + "'";
//			rs.execute(evectionSql);
//			if (rs.next()) evectionReason = rs.getString("");
			
			String sql = "insert into Bill_BoHaiEvection(resourceid, departmentid, fromDate, fromTime, toDate, toTime, evectionReason, requestid) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?)";
			Object[] args = new Object[]{resourceid, departmentid, fromDate, fromTime, toDate, toTime,
					evectionReason, requestid};
			if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportByEvection sql: " + sql.replaceAll("\\?", "%s"),
					args));
			flag = rst.executeUpdate(sql, args);
		} catch (Exception e) {
			logger.error("saveAttendanceReportByEvection Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 保存考勤数据 (销假) 到考勤报表中
	 * 
	 * @param map
	 * 					考勤数据
	 * @return boolean：true.操作成功      false.操作失败
	 */
	private boolean saveAttendanceReportByAbsence(Map<String, String> map) {
		boolean flag = true;
		try {
			String id = map.get("id");
			String resourceid = map.get("xm");
			String fromDate = map.get("ksrq");
			String fromTime = map.get("kssj");
			String toDate = map.get("jsrq");
			String toTime = map.get("jssj");
			String requestid = map.get("xglc");
			
			// 获取请假及销假流程相关信息
			String absenceType = "";		// 销假类型：0.部分销假   1.全部销假
			String leaveRequestid = "";		// 请假流程 id
			String leaveTablename = WorkflowUtils.getTablenameByRequestid(requestid);
			String leaveSql = "select * from " + leaveTablename + " "
					+ "where requestid = '" + requestid + "'";
			rs.execute(leaveSql);
			if (rs.next()) {
				absenceType = rs.getString("xjlx");
				leaveRequestid = rs.getString("yqjdh");
			}
			if (StringUtils.isBlank(leaveRequestid)) return flag;
			
			// 获取请假考勤报表相关信息
			String leaveResourceid = "";
			String leaveReportSql = "select * from Bill_BoHaiLeave where requestid = '" + leaveRequestid + "'";
			rs.execute(leaveReportSql);
			if (rs.next()) leaveResourceid = rs.getString("resourceid");
			if (StringUtils.isBlank(leaveResourceid)) {
				logger.info("leaveReportSql resourceid is null.");
				return flag;
			}
			
			// 判断销假类型是否为全部销假
			// 是, 表示销出所有请假, 直接删除原请假信息
			// 否, 表示销部分请假, 直接更新原请假信息
			if (!StringUtils.isBlank(absenceType) && "1".equals(absenceType)) {
				String sql = "delete Bill_BoHaiLeave where requestid = ?";
				if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportByAbsence sql: " + sql.replaceAll("\\?", "%s"),
						leaveRequestid));
				flag = rst.executeUpdate(sql, leaveRequestid);
			} else {
				String sql = "update Bill_BoHaiLeave set fromDate = ?, fromTime = ?, "
						+ "toDate = ?, toTime = ? where requestid = ?";
				if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportByAbsence sql: " + sql.replaceAll("\\?", "%s"),
						fromDate, fromTime, toDate, toTime, leaveRequestid));
				flag = rst.executeUpdate(sql, fromDate, fromTime, toDate, toTime, leaveRequestid);
			}
			
			if (flag) execLog(id, "0", "成功");
		} catch (Exception e) {
			logger.error("saveAttendanceReportByAbsence Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 保存考勤数据 (出差调整) 到考勤报表中
	 * 
	 * @param map
	 * 					考勤数据
	 * @return boolean：true.操作成功      false.操作失败
	 */
	private boolean saveAttendanceReportByEvectionAdjust(Map<String, String> map) {
		boolean flag = true;
		try {
			String id = map.get("id");
			String resourceid = map.get("xm");
			String fromDate = map.get("ksrq");
			String fromTime = map.get("kssj");
			String toDate = map.get("jsrq");
			String toTime = map.get("jssj");
			String requestid = map.get("xglc");
			
			// 获取出差及出差调整流程相关信息
			String adjustType = "";				// 调整类型：0.调整日期,   1.未出差, 即取消出差
			String evectionRequestid = "";		// 出差流程 id
			String evectionTablename = WorkflowUtils.getTablenameByRequestid(requestid);
			String evectionSql = "select * from " + evectionTablename + " "
					+ "where requestid = '" + requestid + "'";
			rs.execute(evectionSql);
			if (rs.next()) {
				adjustType = rs.getString("tzlx");
				evectionRequestid = rs.getString("ysqdh");
			}
			if (StringUtils.isBlank(evectionRequestid)) return flag;
			
			// 获取出差考勤报表相关信息
			String evectionResourceid = "";
			String evectionReportSql = "select * from Bill_BoHaiEvection where requestid = '" + evectionRequestid + "'";
			rs.execute(evectionReportSql);
			if (rs.next()) evectionResourceid = rs.getString("resourceid");
			if (StringUtils.isBlank(evectionResourceid)) {
				logger.info("evectionReportSql resourceid is null.");
				return flag;
			}
			
			// 判断出差调整类型是否为取消出差
			// 是, 直接删除原出差信息
			// 否, 直接更新原出差信息
			if (!StringUtils.isBlank(adjustType) && "1".equals(adjustType)) {
				String sql = "delete Bill_BoHaiEvection where requestid = ?";
				if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportByEvectionAdjust sql: " + sql.replaceAll("\\?", "%s"),
						evectionRequestid));
				flag = rst.executeUpdate(sql, evectionRequestid);
			} else {
				String sql = "update Bill_BoHaiEvection set fromDate = ?, fromTime = ?, "
						+ "toDate = ?, toTime = ? where requestid = ?";
				if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportByEvectionAdjust sql: " + sql.replaceAll("\\?", "%s"),
						fromDate, fromTime, toDate, toTime, evectionRequestid));
				flag = rst.executeUpdate(sql, fromDate, fromTime, toDate, toTime, evectionRequestid);
			}
			
			if (flag) execLog(id, "0", "成功");
		} catch (Exception e) {
			logger.error("saveAttendanceReportByEvectionAdjust Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 保存考勤数据 (哺乳假) 到考勤报表中
	 * 
	 * @param map
	 * 					考勤数据
	 * @return boolean：true.操作成功      false.操作失败
	 */
	private boolean saveAttendanceReportByLactation(Map<String, String> map) {
		boolean flag = true;
		try {
			String id = map.get("id");
			String userid = map.get("xm");
			String ksrq = map.get("ksrq");
			String kssj = map.get("kssj");
			String jsrq = map.get("jsrq");
			String jssj = map.get("jssj");
			
			// 上午
			if (jssj.compareTo("12:00:00") <= 0) {
				String signInSql = "select * from HrmScheduleSign "
						+ "where userid = '" + userid + "' and signDate = '" + ksrq + "' "
						+ "and signType = '1'";
				String signId = "";
				String signDate = "";
				String signTime = "";
				rs.execute(signInSql);
				if (rs.next()) {
					signId = rs.getString("id");
					signDate = rs.getString("signDate");
					signTime = rs.getString("signTime");
				}
				if (StringUtils.isBlank(signId)) return flag;
				String format = "yyyy-MM-dd HH:mm";
				Date signD = getDate(signDate + " " + signTime, format);	// 早上打卡时间
				Date signBegin = getDate(ksrq + " " + kssj, format);		// 哺乳假开始时间
				Date signEnd = getDate(jsrq + " " + jssj, format);			// 哺乳假结束时间
				
				// TODO 更新打卡时间, 目前不考虑处理之后打卡时间是否在工作时间内
				String signResult = getDate(signD, signBegin, signEnd, format, "am");
				String signResultTime = signResult.split(" ")[1];
				if (StringUtils.isBlank(signResultTime)) return flag;
				String sql = "update HrmScheduleSign set signTime = ? where id = ? and signType = '1'";
				if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportByLactation sql: " + sql.replaceAll("\\?", "%s"),
						signResultTime, signId));
				flag = rst.executeUpdate(sql, signResultTime, signId);
				
			// 下午
			} else {
				String signOutSql = "select * from HrmScheduleSign "
						+ "where userid = '" + userid + "' and signDate = '" + ksrq + "' "
						+ "and signType = '2'";
				String signId = "";
				String signDate = "";
				String signTime = "";
				rs.execute(signOutSql);
				if (rs.next()) {
					signId = rs.getString("id");
					signDate = rs.getString("signDate");
					signTime = rs.getString("signTime");
				}
				if (StringUtils.isBlank(signId)) return flag;
				String format = "yyyy-MM-dd HH:mm";
				Date signD = getDate(signDate + " " + signTime, format);	// 下午打卡时间
				Date signBegin = getDate(ksrq + " " + kssj, format);		// 哺乳假开始时间
				Date signEnd = getDate(jsrq + " " + jssj, format);			// 哺乳假结束时间
				
				// TODO 更新打卡时间, 目前不考虑处理之后打卡时间是否在工作时间内
				String signResult = getDate(signD, signBegin, signEnd, format, "pm");
				String signResultTime = signResult.split(" ")[1];
				if (StringUtils.isBlank(signResultTime)) return flag;
				String sql = "update HrmScheduleSign set signTime = ? where id = ? and signType = '2'";
				if (isDebugEnabled()) logger.info(String.format("saveAttendanceReportByLactation sql: " + sql.replaceAll("\\?", "%s"),
						signResultTime, signId));
				flag = rst.executeUpdate(sql, signResultTime, signId);
			}
			
			if (flag) execLog(id, "0", "成功");
		} catch (Exception e) {
			logger.error("saveAttendanceReportByLactation Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
}
