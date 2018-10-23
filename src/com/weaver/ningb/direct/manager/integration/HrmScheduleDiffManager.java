package com.weaver.ningb.direct.manager.integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import weaver.conn.ConnStatementNew;
import weaver.general.Util;
import weaver.wechat.util.Utils;

import com.weaver.ningb.core.util.FormModeUtils;

/**
 * 考勤差异处理 Manager
 * 
 * @author liberal
 *
 */
public class HrmScheduleDiffManager extends HrmScheduleAbstractManager {
	
	/**
	 * 考勤差异处理, 包括
	 * 1.考勤差异 (打卡 (签到, 签退)) 处理
	 * 2.考勤差异 (打卡 (签到, 签退)) 处理, 处理不存在打卡数据, 但是早上, 晚上的考勤流程 (请假, 出差) 同时存在的问题
	 * 3.考勤差异 (打卡 (签到, 签退)) 与流程相关处理, 包括请假, 出差类型
	 * 4.考勤差异 (请假, 出差, 销假, 出差调整) 处理, 处理流程数据不同步的问题
	 * 
	 * @param startDate
	 * 					开始时间
	 * @param endDate
	 * 					结束时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean diffAttendance(String startDate, String endDate) {
		boolean flag = true;
		
		// 考勤差异 (打卡 (签到, 签退)) 处理
		flag = diffAttendanceSign(startDate, endDate);
		if (!flag) {
			logger.info("diffAttendanceAll diffAttendanceSign Failure");
			return flag;
		}
		
		// 考勤差异 (打卡 (签到, 签退)) 处理, 处理不存在打卡数据, 但是早上, 晚上的考勤流程 (请假, 出差) 同时存在的问题
		flag  = diffAttendanceSignByNoSign(startDate, endDate);
		if (!flag) {
			logger.info("diffAttendanceAll diffAttendanceSignByNoSign Failure");
			return flag;
		}
		
		// 考勤差异 (打卡 (签到, 签退)) 与流程相关处理, 包括请假, 出差类型
		flag = diffAttendanceSignByWorkflow(startDate, endDate);
		if (!flag) {
			logger.info("diffAttendanceAll diffAttendanceSignByWorkflow Faliure");
			return flag;
		}
		
		// 考勤差异 (请假, 出差, 销假, 出差调整) 处理, 处理流程数据不同步的问题
		flag = diffAttendanceWorkflowToReport(startDate, endDate);
		if (!flag) logger.info("diffAttendanceAll diffAttendanceWorkflowToReport Faliure");
		
		return flag;
	}
	
	/**
	 * 考勤差异 (打卡 (签到, 签退)) 处理, 处理旧 OA 考勤数据可能存在更新的问题
	 * 
	 * @param startDate
	 * 					开始时间
	 * @param endDate
	 * 					结束时间
	 * @return boolean：操作成功      false.操作失败
	 */
	public boolean diffAttendanceSign(String startDate, String endDate) {
		boolean flag = true;
		if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
			logger.info("diffAttendanceSign startDate and endDate is null");
		} else if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			String deadline = StringUtils.isBlank(startDate) ? endDate : startDate;
			flag = diffAttendanceSign(deadline);
		} else {
			int diffDay = differentDays(startDate, endDate);
			for (int i = 0; i <= diffDay; i++) {
				String deadline = getDateStr(getDateStr(startDate), i);
				boolean tempFlag = diffAttendanceSign(deadline);
				if (!tempFlag) 
					logger.info(String.format("diffAttendanceSign detail Failure: deadline = %s", 
							deadline));
			}
		}
		return flag;
	}
	
	/**
	 * 考勤差异 (打卡 (签到, 签退)) 处理, 处理旧 OA 考勤数据可能存在更新的问题
	 * 
	 * @param dateStr
	 * 					指定时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean diffAttendanceSign(String dateStr) {
		boolean flag = true;
		List<Map<String, String>> list = null;
		try {
			if (StringUtils.isBlank(dateStr)) {
				logger.info("diffAttendanceSign date is null");
				return flag;
			}
			
			list = dao.getAttendance(dateStr);
			if (list == null || list.size() <= 0) {
				logger.info("diffAttendanceSign getAttendance is null");
				return flag;
			}
			
			int type = 2;
			flag = new HrmScheduleManager().saveAttendanceSign(type, list);
		} catch (Exception e) {
			logger.error("diffAttendanceSign Exception: ", e);
			flag = false;
		} finally {
			if (list != null) list.clear();
		}
		return flag;
	}
	
	/**
	 * 考勤差异 (打卡 (签到, 签退)) 处理, 处理不存在打卡数据, 但是早上, 晚上的考勤流程 (请假, 出差) 同时存在的问题
	 * 
	 * <p>考勤流程头尾存在的情况
	 * 
	 * @param startDate
	 * 					开始时间
	 * @param endDate
	 * 					结束时间
	 * @return boolean：操作成功      false.操作失败
	 */
	public boolean diffAttendanceSignByNoSign(String startDate, String endDate) {
		boolean flag = true;
		if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
			logger.info("diffAttendanceSignByNoSign startDate and endDate is null");
		} else if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			String deadline = StringUtils.isBlank(startDate) ? endDate : startDate;
			flag = diffAttendanceSignByNoSign(deadline);
		} else {
			int diffDay = differentDays(startDate, endDate);
			for (int i = 0; i <= diffDay; i++) {
				String deadline = getDateStr(getDateStr(startDate), i);
				boolean tempFlag = diffAttendanceSignByNoSign(deadline);
				if (!tempFlag) 
					logger.info(String.format("diffAttendanceSignByNoSign detail Failure: deadline = %s", 
							deadline));
			}
		}
		return flag;
	}
	
	/**
	 * 考勤差异 (打卡 (签到, 签退)) 处理, 处理未打卡, 但是早上, 晚上的考勤流程 (请假, 出差) 同时存在的问题
	 * 
	 * <p>考勤流程头尾存在的情况
	 * 
	 * @param dateStr
	 * 					指定时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean diffAttendanceSignByNoSign(String dateStr) {
		boolean flag = true;
		String task = "diffAttendanceSignByNoSign";
		ConnStatementNew csn = null;
		List<Map<String, String>> list = null;
		try {
			if (StringUtils.isBlank(dateStr)) {
				logger.info(task + " date is null");
				return flag;
			}
			
			int type = 5;
			String signType = getPropString("attendanceid");
			String tablename = FormModeUtils.getTablename(getPropString("modeid"));
			list = dao.getAttendanceByNoSign(type, signType,
					tablename, dateStr, HRM_SUBCOMPANY_ID);
			if (list == null || list.size() <= 0) {
				logger.info(task + " list is null.");
				return flag;
			}
			
			String sql = "insert into HrmScheduleSign(userid, userType, signType, signDate, signTime, clientAddress, isInCom) "
					+ "values(?, ?, ?, ?, ?, ?, ?)";
			String userType = "1";			// 用户类型
			String signInType = "1";		// 签到类型
			String signOutType = "2";		// 签退类型
			String clientAddress = "0";		// 客户端 ip
			String isInCom = "1";			// 是否来自外部
			
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
				
				// 如果考勤流程数据为空或者小于两条直接返回不进行处理
				if (scheduleList == null || scheduleList.size() < 2) continue;
				
				// 获取早上, 晚上打卡数据; 进行差异对比
				// 1.早上获取最早时间的, 流程结束时间对比取最早
				// 2.晚上获取最晚时间的, 流程开始时间对比取最晚
				Map<String, Object> signInMap = null;
				Map<String, Object> signOutMap = null;
				for (int i = 0; i < scheduleList.size(); i++) {
					Map<String, Object> scheduleMap = scheduleList.get(i);
					boolean signInFlag = compareScheduleSign(0, signInMap, scheduleMap);
					if (signInFlag) signInMap = scheduleMap;
					boolean signOutFlag = compareScheduleSign(1, signOutMap, scheduleMap);
					if (signOutFlag) signOutMap = scheduleMap;
				}
				if (signInMap == null || signInMap.size() <= 0
						|| signOutMap == null || signOutMap.size() <= 0) continue;
				
				if (csn == null) {
					csn = new ConnStatementNew();
					csn.setStatementSql(sql);
				}
				
				setScheduleSignValue(csn, sql, userid, userType, signInType, dateStr, 
						Util.null2String(signInMap.get("jssj")), clientAddress, isInCom);
				setScheduleSignValue(csn, sql, userid, userType, signOutType, dateStr, 
						Util.null2String(signOutMap.get("kssj")), clientAddress, isInCom);
			}
			
			if (csn != null) csn.executeBatch();
		} catch (Exception e) {
			logger.error("diffAttendanceSign Exception: ", e);
			flag = false;
		} finally {
			if (csn != null) csn.close();
			if (list != null) list.clear();
		}
		return flag;
	}
	
	/**
	 * 考勤差异 (打卡 (签到, 签退)) 与流程相关处理, 包括请假, 出差类型
	 * 
	 * @param startDate
	 * 					开始时间
	 * @return boolean：true.操作成功      false.操作失败
	 * @see #diffAttendanceSignByWorkflow(String, String)
	 */
	public boolean diffAttendanceSignByWorkflow(String startDate) {
		return diffAttendanceSignByWorkflow(startDate, startDate);
	}
	
	/**
	 * 考勤差异 (打卡 (签到, 签退)) 与流程相关处理, 包括请假, 出差类型
	 * 
	 * <p>对应情况包括 (以请假类型为例)
	 * 1.早上请假半天, 下午上班不进行签到打卡, 只进行签退打卡
	 * 2.下午请假半天, 早上上班只进行签到打卡, 不进行签退打卡
	 * 3.请假几个小时 (0.25 天), 可能包括早上请假几个小时, 下午请假几个小时, 中间时段请假几个小时
	 * 
	 * <p>解决方案 (以请假类型为例), 获取请假流程的实际时间更新到对应不打卡的时间段
	 * 1.早上不签到, 将请假流程的实际请假开始时间更新到签到时间
	 * 2.下午不签退, 将请假流程的实际请假结束时间更新到签退时间
	 * 3.请假几个小时
	 * 	3.1.早上请假几个小时, 将请假流程的实际请假开始时间更新到签到时间
	 * 	3.2.下午请假几个小时, 将请假流程的实际请假结束时间更新到签退时间
	 * 	3.3.中间时段请假几个小时, 暂时不处理
	 * 
	 * @param startDate
	 * 					开始时间
	 * @param endDate
	 * 					结束时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean diffAttendanceSignByWorkflow(String startDate, String endDate) {
		boolean flag = true;
		ConnStatementNew csn = null;
		ConnStatementNew csnUpdate = null;
		List<Map<String, String>> list = null;
		try {
			if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
				logger.info("diffAttendanceSignByWorkflow start and end date is null");
				return flag;
			}
			if (StringUtils.isBlank(startDate)) startDate = endDate;
			if (StringUtils.isBlank(endDate)) endDate = startDate;
			
			String tablename = FormModeUtils.getTablename(getPropString("modeid"));
			String signType = "1";			// 打卡考勤类型 id
			String sql = "insert into HrmScheduleSign(userid, userType, signType, signDate, signTime, clientAddress, isInCom) "
					+ "values(?, ?, ?, ?, ?, ?, ?)";
			String sqlUpdate = "update HrmScheduleSign set signTime = ? where id = ?";
			String userType = "1";			// 用户类型
			String signInType = "1";		// 签到类型
			String signOutType = "2";		// 签退类型
			String clientAddress = "0";		// 客户端 ip
			String isInCom = "1";			// 是否来自外部
			
			// 获取请假数据
			Map<String, String> attendanceMapLeave = getAttendanceData("2", true);
			// 获取出差数据
			Map<String, String> attendanceMapEvection = getAttendanceData("4", true);
			
			// 考勤信息, 建模数据
			String endTime = getAttendanceDateConfig("scheduleendtime");		// 考勤下班工作时间
			list = dao.getAttendanceByOther(signType, tablename, startDate, null, endDate, endTime);
			if (list != null && list.size() > 0) {
				for (Map<String, String> map : list) {
					String userid = map.get("xm");
					String ksrq = map.get("ksrq");
					String jsrq = map.get("jsrq");
					
					int type = -1;			// 类型：0.签到时间为空   1.签退时间为空   2.签退时间小于考勤下班实际时间
					String deadline = "";	// 需要同步时日期：type=0.为签到时间   type=1.为签退时间   type=2.签到签退时间都可以
					if (StringUtils.isBlank(ksrq)) {
						type = 0;
						deadline = jsrq;
					} else if (StringUtils.isBlank(jsrq)) {
						type = 1;
						deadline = ksrq;
					} else {
						type = 2;
						deadline = jsrq;
					}
					
					// 数据处理, 如果请假类型数据不为空则只设置请假类型, 否则设置出差类型数据
					String tablenameLeave = attendanceMapLeave.get("tablename");
					String sqlFieldsLeave = attendanceMapLeave.get("sqlFields");
					String sqlConditionLeave = attendanceMapLeave.get("sqlCondition");
					List<Map<String, Object>> listLeave = dao.getAttendanceByWorkflow(tablenameLeave,
							sqlFieldsLeave, sqlConditionLeave, userid, deadline);
					// 请假类型
					if (listLeave != null && listLeave.size() > 0) {
						if (csn == null) {
							csn = new ConnStatementNew();
							csn.setStatementSql(sql);
						}
						
						// 排序
						if (listLeave.size() > 1) {
							Collections.sort(listLeave, new Comparator<Map<String, Object>>() {
								
								@Override
								public int compare(Map<String, Object> o1,
										Map<String, Object> o2) {
									if (o1 == null || o1.size() <= 0
											|| o2 == null || o2.size() <= 0) return 0;
									return Utils.null2String(o1.get("kssj"))
											.compareTo(Utils.null2String(o2.get("kssj")));
								}
								
							});
						}
						Map<String, Object> mapLeave = listLeave.get(0);
						
						// 签到时间
						if (type == 0) {
							boolean existFlag = checkScheduleSign(userid, userType, signInType, deadline, isInCom);
							if (!existFlag) {
								String leaveDate = mapLeave.get("jsrq") + "";		// 使用结束日期, 结束日期大于等于开始日期
								String leaveTime = mapLeave.get("kssj") + "";
								String currentTime = getDiffTime(deadline, leaveDate, leaveTime, 0);
								setScheduleSignValue(csn, sql, userid, userType, signInType, deadline, currentTime, clientAddress, isInCom);
							}
							
						// 签退时间
						} else if (type == 1) {
							boolean existFlag = checkScheduleSign(userid, userType, signOutType, deadline, isInCom);
							if (!existFlag) {
								String leaveDate = mapLeave.get("jsrq") + "";		// 使用结束日期, 结束日期大于等于开始日期
								String leaveTime = mapLeave.get("jssj") + "";
								String currentTime = getDiffTime(deadline, leaveDate, leaveTime, 1);
								setScheduleSignValue(csn, sql, userid, userType, signOutType, deadline, currentTime, clientAddress, isInCom);
							}
							
						// 签退时间小于考勤下班实际时间, 例如签退时间为 15:30, 请假时间为 0.25 小时; 则需要更新
						} else if (type == 2) {
							Map<String, String> checkSignMap = checkScheduleSignToMap(userid, userType, signOutType, deadline, isInCom);
							boolean checkFlag = compareScheduleSign(checkSignMap, mapLeave, endTime);
							if (checkFlag) {
								if (csnUpdate == null) {
									csnUpdate = new ConnStatementNew();
									csnUpdate.setStatementSql(sqlUpdate);
								}
								String leaveDate = mapLeave.get("jsrq") + "";		// 使用结束日期, 结束日期大于等于开始日期
								String leaveTime = mapLeave.get("jssj") + "";
								String currentTime = getDiffTime(deadline, leaveDate, leaveTime, 1);
								String id = checkSignMap.get("id");
								setScheduleSignValue(csnUpdate, sqlUpdate, currentTime, id);
							}
						}
						
					// 出差类型
					} else {
						String tablenameEvection = attendanceMapEvection.get("tablename");
						String sqlFieldsEvection = attendanceMapEvection.get("sqlFields");
						String sqlConditionEvection = attendanceMapEvection.get("sqlCondition");
						List<Map<String, Object>> listEvection = dao.getAttendanceByWorkflow(tablenameEvection,
								sqlFieldsEvection, sqlConditionEvection, userid, deadline);
						if (listEvection != null && listEvection.size() > 0) {
							if (csn == null) {
								csn = new ConnStatementNew();
								csn.setStatementSql(sql);
							}
							
							// 排序
							if (listEvection.size() > 1) {
								Collections.sort(listEvection, new Comparator<Map<String, Object>>() {
									
									@Override
									public int compare(Map<String, Object> o1,
											Map<String, Object> o2) {
										if (o1 == null || o1.size() <= 0
												|| o2 == null || o2.size() <= 0) return 0;
										return Utils.null2String(o1.get("kssj"))
												.compareTo(Utils.null2String(o2.get("kssj")));
									}
									
								});
							}
							Map<String, Object> mapEvection = listEvection.get(0);
							
							// 签到时间
							if (type == 0) {
								boolean existFlag = checkScheduleSign(userid, userType, signInType, deadline, isInCom);
								if (!existFlag) {
									String evectionDate = mapEvection.get("jsrq") + "";		// 使用结束日期, 结束日期大于等于开始日期
									String evectionTime = mapEvection.get("kssj") + "";
									String currentTime = getDiffTime(deadline, evectionDate, evectionTime, 0);
									setScheduleSignValue(csn, sql, userid, userType, signInType, deadline, currentTime, clientAddress, isInCom);
								}
								
							// 签退时间
							} else if (type == 1) {
								boolean existFlag = checkScheduleSign(userid, userType, signOutType, deadline, isInCom);
								if (!existFlag) {
									String evectionDate = mapEvection.get("jsrq") + "";		// 使用结束日期, 结束日期大于等于开始日期
									String evectionTime = mapEvection.get("jssj") + "";
									String currentTime = getDiffTime(deadline, evectionDate, evectionTime, 1);
									setScheduleSignValue(csn, sql, userid, userType, signOutType, deadline, currentTime, clientAddress, isInCom);
								}
							
							// 签退时间小于考勤下班实际时间, 例如签退时间为 15:30, 请假时间为 0.25 小时; 则需要更新
							} else if (type == 2) {
								Map<String, String> checkSignMap = checkScheduleSignToMap(userid, userType, signOutType, deadline, isInCom);
								boolean checkFlag = compareScheduleSign(checkSignMap, mapEvection, endTime);
								if (checkFlag) {
									if (csnUpdate == null) {
										csnUpdate = new ConnStatementNew();
										csnUpdate.setStatementSql(sqlUpdate);
									}
									String evectionDate = mapEvection.get("jsrq") + "";		// 使用结束日期, 结束日期大于等于开始日期
									String evectionTime = mapEvection.get("jssj") + "";
									String currentTime = getDiffTime(deadline, evectionDate, evectionTime, 1);
									String id = checkSignMap.get("id");
									setScheduleSignValue(csnUpdate, sqlUpdate, currentTime, id);
								}
							}
						}
					}
				}
				
				if (csn != null) csn.executeBatch();
				if (csnUpdate != null) csnUpdate.executeBatch();
			}
		} catch (Exception e) {
			logger.error("diffAttendance Exception: ", e);
			flag = false;
		} finally {
			if (list != null) list.clear();
		}
		return flag;
	}
	
	/**
	 * 考勤差异 (请假, 出差, 销假, 出差调整) 处理, 处理流程数据不同步的问题
	 * 
	 * <p>同步考勤建模数据到报表中
	 * 
	 * @param startDate
	 * 					开始时间
	 * @param endDate
	 * 					结束时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean diffAttendanceWorkflowToReport(String startDate, String endDate) {
		boolean flag = true;
		HrmScheduleReportManager reportManager = null;
		try {
			reportManager = new HrmScheduleReportManager();
			// 请假, 出差, 销假, 出差调整
			List<Integer> signTypes = new ArrayList<Integer>(3);
			signTypes.add(2);
			signTypes.add(3);
			signTypes.add(4);
			signTypes.add(5);
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
	 * 设置打开预处理参数值
	 * 
	 * @param ConnStatementNew
	 * @param sql
	 * 					执行语句
	 * @param userid
	 * 					用户 id
	 * @param userType
	 * 					用户类型
	 * @param signType
	 * 					签到类型：1.签到   2.签退
	 * @param signDate
	 * 					签到日期
	 * @param signTime
	 * 					签到时间
	 * @param clientAddress
	 * 					客户端 ip
	 * @param isInCom
	 * 					是否来自外部：1.是
	 */
	private void setScheduleSignValue(ConnStatementNew csn, String sql, String userid, String userType,
			String signType, String signDate, String signTime, String clientAddress, String isInCom) {
		try {
			if (isDebugEnabled()) logger.info(String.format("setScheduleSignValue sql: " + sql.replaceAll("\\?", "%s"),
					userid, userType, signType, signDate, signTime, clientAddress, isInCom));
				
			csn.setString(1, userid);
			csn.setString(2, userType);
			csn.setString(3, signType);
			csn.setString(4, signDate);
			csn.setString(5, signTime);
			csn.setString(6, clientAddress);
			csn.setString(7, isInCom);
			csn.addBatch();
		} catch (Exception e) {
			logger.error("setScheduleSignValue Exception: " + e.getMessage());
		}
	}
	
	/**
	 * 设置打开预处理参数值
	 * 
	 * @param ConnStatementNew
	 * @param sql
	 * 					执行语句
	 * @param signTime
	 * 					签到时间
	 * @param id
	 * 					考勤 id
	 */
	private void setScheduleSignValue(ConnStatementNew csn, String sql,
			String signTime, String id) {
		try {
			if (isDebugEnabled()) logger.info(String.format("setScheduleSignValue sql: " + sql.replaceAll("\\?", "%s"),
					signTime, id));
				
			csn.setString(1, signTime);
			csn.setString(2, id);
			csn.addBatch();
		} catch (Exception e) {
			logger.error("setScheduleSignValue Exception: " + e.getMessage());
		}
	}
	
	/**
	 * 获取考勤流程相关时间, 处理跨天的问题
	 * 
	 * <p>比如, 请假时间为 1 日 到 5 日下午 3 点, 当前 3 日早上打了卡, 下午未打卡; 
	 * 考勤差异 (打卡 (签到, 签退)) 与流程相关处理, 应该同步到报表中的下午 (签退) 打卡时间是 3 号下午排班时间而不是 5 日下午 3 点时间
	 * 
	 * @param cardDate
	 * 					考勤日期
	 * @param attendanceDate
	 * 					流程日期
	 * @param attendanceTime
	 * 					流程时间
	 * @param type
	 * 					类型：0.上午      1.下午
	 * @return 流程时间, 跨天返回排班时间, 否则返回流程时间
	 */
	private String getDiffTime(String cardDate, String attendanceDate, 
			String attendanceTime, int type) {
		String result = "";
		cardDate = Util.null2String(cardDate);
		attendanceDate = Util.null2String(attendanceDate);
		boolean flag = false;				// 跨天的问题标识：false.跨天      true.未跨天
		if (!StringUtils.isBlank(cardDate)
				&& !StringUtils.isBlank(attendanceDate)
				&& cardDate.compareTo(attendanceDate) == 0) {
			flag = true;
		}
		if (flag) {
			// 早上
			if (type == 0) {
				// 处理跨天考勤流程, 类型为早上, 但是开始时间为下午的情况
				// 例如：2018-04-26 14:30 到 2018-04-27 13:00; 04-27 补全早上签到的情况
				boolean signFlag = attendanceTime.compareTo("12:00") <= 0;
				if (!signFlag) {
					// 获取考勤排班时间
					HrmScheduleManager hrmScheduleManager = new HrmScheduleManager();
					Map<String, String> attendanceScheduleTimes = hrmScheduleManager.getAttendanceScheduleTimes(
							dao.getAttendanceScheduleTimes("4", HRM_SUBCOMPANY_ID, cardDate), cardDate);
					result = attendanceScheduleTimes.get("starttime");
				} else {
					result = attendanceTime;
				}
			} else {
				result = attendanceTime;
			}
		} else {
			// 获取考勤排班时间
			HrmScheduleManager hrmScheduleManager = new HrmScheduleManager();
			Map<String, String> attendanceScheduleTimes = hrmScheduleManager.getAttendanceScheduleTimes(
					dao.getAttendanceScheduleTimes("4", HRM_SUBCOMPANY_ID, cardDate), cardDate);
			// 获取早上排班时间
			if (type == 0) {
				result = attendanceScheduleTimes.get("starttime");
			// 获取下午排班时间
			} else {
				result = attendanceScheduleTimes.get("endtime");
			}
		}
		return result;
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
		if (newMap == null) return flag;
		String sj = "";
		if (type == 0) {
			sj = "jssj";
		} else if (type == 1) {
			sj = "kssj";
		} else {}
		
		if (StringUtils.isBlank(sj)) return flag;
		String newSj = Util.null2String(newMap.get(sj) == null ? "" : newMap.get(sj) + "", "");
		// 处理首次 oldMap 为空，newMap 不为空的情况
		if (oldMap == null || oldMap.size() <= 0
				&& newMap != null && newMap.size() > 0) {
			boolean signFlag = newSj.compareTo("12:00") <= 0;
			// 早上
			if (type == 0 && signFlag) {
				flag = true;
				
			// 晚上
			} else if (type == 1 && !signFlag) {
				flag = true;
			}
		}
		
		if (oldMap == null) return flag;
		String oldSj = Util.null2String(oldMap.get(sj) == null ? "" : oldMap.get(sj) + "", "");
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
	 * 比较考勤报表中已存在数据是否小于当前流程 (请假等) 数据, 不小于则不进行操作
	 * 
	 * @param oldMap
	 * 					建模打卡数据
	 * @param newMap
	 * 					当前流程数据
	 * @param endTime
	 * 					考勤排班下午下班时间
	 * @return boolean：true.完全相等      false.不完全相等
	 */
	private boolean compareScheduleSign(Map<String, String> oldMap, 
			Map<String, Object> newMap, String endTime) {
		boolean flag = false;
		if (oldMap == null || newMap == null) return flag;
		String oldJssj = Util.null2String(oldMap.get("jssj"), "");
		String newJssj = Util.null2String(newMap.get("jssj") == null ? "" : newMap.get("jssj") + "", "");
		if (!StringUtils.isBlank(oldJssj)
				&& !StringUtils.isBlank(newJssj)
				&& oldJssj.compareTo(newJssj) < 0) flag = true;
		return flag;
	}
	
}
