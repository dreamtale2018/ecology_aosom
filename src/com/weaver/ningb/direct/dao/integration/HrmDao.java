package com.weaver.ningb.direct.dao.integration;

import java.util.List;
import java.util.Map;

/**
 * 考勤 DAO
 * 
 * @author liberal
 *
 */
public interface HrmDao {

	/**
	 * 获取考勤机数据集 (打卡)
	 * 
	 * @param deadline
	 * 					截止日期
	 * @return 考勤数据集
	 */
	public List<Map<String, String>> getAttendance(String deadline);
	
	/**
	 * 获取考勤数据集 (包括请假, 出差, 销假等), 根据对应表单
	 * 
	 * @param tablename
	 * 					表单名称
	 * @param sqlFields
	 * 					查询固定字段
	 * @param attendanceid
	 * 					考勤类型 id
	 * @return 考勤数据集
	 */
	public List<Map<String, String>> getAttendanceByWorkflow(String tablename, 
			String sqlFields, String attendanceid);
	
	/**
	 * 获取考勤数据集 (包括请假, 出差等), 根据对应表单
	 * 
	 * @param tablename
	 * 					表单名称
	 * @param sqlFields
	 * 					查询固定字段
	 * @param sqlCondition
	 * 					查询固定条件
	 * @param userid
	 * 					用户 id
	 * @param deadline
	 * 					当前日期
	 * @return 考勤数据集
	 */
	public List<Map<String, Object>> getAttendanceByWorkflow(String tablename, 
			String sqlFields, String sqlCondition, String userid, String deadline);
	
	/**
	 * 获取考勤数据集 (包括请假, 出差等), 根据对应表单
	 * 
	 * @param tablename
	 * 					表单名称
	 * @param sqlFields
	 * 					查询固定字段
	 * @param sqlCondition
	 * 					查询固定条件
	 * @param userid
	 * 					用户 id
	 * @param deadline
	 * 					当前日期
	 * @return 考勤数据集
	 */
	public List<Map<String, Object>> getAttendanceByWorkflows(String tablename, 
			String sqlFields, String sqlCondition, String userid, String deadline);
	
	/**
	 * 获取考勤数据集 (哺乳假), 根据对应表单
	 * 
	 * @param tablename
	 * 					表单名称
	 * @param deadline
	 * 					当前日期
	 * @return 考勤数据集
	 */
	public List<Map<String, String>> getAttendanceByLactation(String tablename, 
			String deadline);
	
	/**
	 * 获取考勤数据集 (打卡 (签到, 签退), 请假, 出差, 销假, 哺乳假), 根据对应表单
	 * 
	 * @param signType
	 * 					考勤类型
	 * @param tablename
	 * 					表单名称
	 * @param deadline
	 * 					当前日期
	 * @return 考勤数据集
	 */
	public List<Map<String, String>> getAttendanceByOther(String signType, 
			String tablename, String deadline);
	
	/**
	 * 获取考勤数据集 (打卡 (签到, 签退), 请假, 出差, 销假, 哺乳假), 根据对应表单
	 * 
	 * <p>如果开始结束时间为空则获取所有的打卡数据
	 * 
	 * @param signType
	 * 					考勤类型
	 * @param tablename
	 * 					表单名称
	 * @param startDate
	 * 					开始日期
	 * @param startTime
	 * 					开始时间, 系统排班时间
	 * @param endDate
	 * 					结束日期
	 * @param endTime
	 * 					结束时间, 系统排班时间
	 * @return 考勤数据集
	 */
	public List<Map<String, String>> getAttendanceByOther(String signType,
			String tablename, String startDate, String startTime, String endDate,
			String endTime);
	
	/**
	 * 获取考勤数据集 (未打卡), 根据对应表单
	 * 
	 * @param type
	 * 					类型：0.早上   1.晚上   2.全部   3.报表早上   4.报表晚上   5.报表全部
	 * @param signType
	 * 					考勤类型
	 * @param tablename
	 * 					表单名称
	 * @param deadline
	 * 					当前日期
	 * @param subcompanyname
	 * 					指定分部
	 * @return 考勤数据集
	 */
	public List<Map<String, String>> getAttendanceByNoSign(int type, String signType,
			String tablename, String deadline, String subcompanyname);
	
	/**
	 * 获取工作时间
	 * 
	 * @param scheduleType
	 * 					工作时间类别
	 * @param relatedid
	 * 					相关 id, 可能为公司, 分部, 部门或人力资源
	 * @param date
	 * 					当前日期
	 * @return 工作时间设置信息
	 */
	public Map<String, Object> getAttendanceScheduleTimes(String scheduleType, 
			String relatedid, String date);
	
	/**
	 * 获取用户信息, 根据用户 id
	 * 
	 * @param userid
	 * 					用户 id
	 * @return 用户信息
	 */
	public Map<String, String> getUser(String userid);
	
	/**
	 * 获取用户信息, 根据用户账号
	 * 
	 * @param loginid
	 * 					用户账号
	 * @return 用户信息
	 */
	public Map<String, String> getUserByLoginid(String loginid);
	
	/**
	 * 更新建模考勤数据执行状态
	 * 
	 * @param tablename
	 * 					表单名称
	 * @param id
	 * 					建模考勤数据 id
	 * @param execDate
	 * 					执行日期
	 * @param execTime
	 * 					执行时间
	 * @param execStatus
	 * 					执行状态：0.成功   -1.失败   -2.异常
	 * @param execMessage
	 * 					执行描述
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean updateAttendance(String tablename, String id, String execDate,
			String execTime, String execStatus, String execMessage);
	
}
