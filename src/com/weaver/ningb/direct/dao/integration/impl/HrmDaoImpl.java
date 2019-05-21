package com.weaver.ningb.direct.dao.integration.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.weaver.ningb.dao.DataAccessException;
import com.weaver.ningb.direct.dao.integration.HrmDao;
import com.weaver.ningb.jdbc.core.JdbcTemplate;
import com.weaver.ningb.jdbc.core.RowMapper;
import com.weaver.ningb.jdbc.oa.JdbcTemplateFactory;
import com.weaver.ningb.logging.LogFactory;

public class HrmDaoImpl implements HrmDao {

	private static final Log logger = LogFactory.getLog(HrmDaoImpl.class);
	
	
	private JdbcTemplate jdbcTemplate;
	private JdbcTemplate jdbcTemplateDataSource;
	private String datasourceId;			// 数据源 id
	private boolean isDebug;				// 是否打印日志

	
	public HrmDaoImpl(String datasourceId) {
		this.datasourceId = datasourceId;
		this.jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		this.jdbcTemplateDataSource = JdbcTemplateFactory.getJdbcTemplate(datasourceId);
	}
	
	public HrmDaoImpl(String datasourceId, boolean isDebug) {
		this.datasourceId = datasourceId;
		this.isDebug = isDebug;
		this.jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		this.jdbcTemplateDataSource = JdbcTemplateFactory.getJdbcTemplate(datasourceId);
	}
	
	
	@Override
	public List<Map<String, String>> getAttendance(String deadline) {
		List<Map<String, String>> list = null;
		String task = "getAttendance";
		try {
			String sql = "select attendance_empcode workcode,"
					+ "(select max(b.attendance_date + ' ' + b.attendance_time) "
					+ "from hr_attendance b where b.attendance_empcode = a.attendance_empcode "
					+ "and b.attendance_date + ' ' + b.attendance_time < b.attendance_date + ' ' + '12:00:00' "
					+ "and b.attendance_date = ?) starttime,"
					+ "(select max(b.attendance_date + ' ' + b.attendance_time) "
					+ "from hr_attendance b where b.attendance_empcode = a.attendance_empcode "
					+ "and b.attendance_date + ' ' + b.attendance_time >= b.attendance_date + ' ' + '15:00:00' "
					+ "and b.attendance_date = ?) endtime "
					+ "from hr_attendance a "
					+ "group by attendance_empcode";
			
			if (isDebug()) logger.info(String.format("getAttendance sql: " + sql.replaceAll("\\?", "%s"),
					deadline, deadline));
			
			list = jdbcTemplateDataSource.query(sql, new RowMapper<Map<String, String>>() {

				@Override
				public Map<String, String> mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					String workcode = rs.getString("workcode");		// 员工 loginid
					String starttime = rs.getString("starttime");	// 员工早上打卡时间
					String endtime = rs.getString("endtime");		// 员工晚上打卡时间
					
					Map<String, String> map = new HashMap<String, String>(3);
					map.put("workcode", workcode);
					map.put("starttime", starttime);
					map.put("endtime", endtime);
					return map;
				}
				
			}, deadline, deadline);
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return list;
	}
	
	@Override
	public List<Map<String, String>> getAttendanceByWorkflow(String tablename,
			String sqlFields, String attendanceid) {
		List<Map<String, String>> list = null;
		String task = "getAttendanceByWorkflow";
		try {
//			String sqlFields = " kqlx, xm, bm, gs, xglc, ksrq, kssj, jsrq, jssj, ts ";
			String sql = "select " + sqlFields + " "
					+ "from " + tablename + " "
					+ "where requestid not in (select xglc from uf_kqjlb where kqlx = '" + attendanceid + "') "
					// TODO 存在性能问题
					+ "and requestid in (select requestid from workflow_requestbase where currentnodetype = 3) ";
			
			if (isDebug()) logger.info(task + " sql: " + sql);
			
			list = jdbcTemplate.query(sql, new RowMapper<Map<String, String>>() {

				@Override
				public Map<String, String> mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					String kqlx = rs.getString("kqlx");
					String xm = rs.getString("xm");
					String bm = rs.getString("bm");
					String gs = rs.getString("gs");
					String ksrq = rs.getString("ksrq");
					String kssj = rs.getString("kssj");
					String jsrq = rs.getString("jsrq");
					String jssj = rs.getString("jssj");
					String ts = rs.getString("ts");
					String xglc = rs.getString("xglc");
					
					// 过滤掉时间为空的流程
					if (StringUtils.isBlank(ksrq) && StringUtils.isBlank(jsrq)) return null;
					
					Map<String, String> map = new HashMap<String, String>(9);
					map.put("kqlx", kqlx);
					map.put("xm", xm);
					map.put("bm", bm);
					map.put("gs", gs);
					map.put("ksrq", ksrq);
					map.put("kssj", kssj);
					map.put("jsrq", jsrq);
					map.put("jssj", jssj);
					map.put("ts", ts);
					map.put("xglc", xglc);
					return map;
				}
				
			});
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getAttendanceByWorkflow(String tablename, 
			String sqlFields, String sqlCondition, String userid, String deadline) {
		List<Map<String, Object>> list = null;
		String task = "getAttendanceByWorkflow";
		try {
			String sql = "select " + sqlFields + " "
					+ "from " + tablename + " "
					+ "where " + sqlCondition + " "
					// 过滤掉未归档流程数据
					+ "and requestid in (select a.requestid from workflow_requestbase a where a.requestid = requestid and a.currentnodetype = 3) ";
			Object[] args = new Object[]{userid, deadline, deadline};
			
			if (isDebug()) logger.info(String.format(task + " sql: " + sql.replaceAll("\\?", "%s"),
					args));
			
			list = jdbcTemplate.queryForList(sql, args);
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getAttendanceByWorkflows(String tablename, 
			String sqlFields, String sqlCondition, String userid, String deadline) {
		List<Map<String, Object>> list = null;
		String task = "getAttendanceByWorkflows";
		try {
			String sql = "select " + sqlFields + " "
					+ "from " + tablename + " "
					+ "where " + sqlCondition + " "
					// 过滤掉未归档流程数据
					+ "and requestid in (select a.requestid from workflow_requestbase a where a.requestid = requestid and a.currentnodetype = 3) ";
			Object[] args = new Object[]{userid, deadline, deadline};
			
			if (isDebug()) logger.info(String.format(task + " sql: " + sql.replaceAll("\\?", "%s"),
					args));
			
			list = jdbcTemplate.queryForList(sql, args);
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return list;
	}
	
	@Override
	public List<Map<String, String>> getAttendanceByLactation(String tablename, 
			String deadline) {
		List<Map<String, String>> list = null;
		String task = "getAttendanceByLactation";
		try {
			String sql = "select * "
					+ "from " + tablename + " "
					+ "where ksrq <= ? and jsrq >= ? ";
			
			if (isDebug()) logger.info(String.format(task + " sql: " + sql.replaceAll("\\?", "%s"), 
					deadline, deadline));
			
			list = jdbcTemplate.query(sql, new RowMapper<Map<String, String>>() {

				@Override
				public Map<String, String> mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					String xm = rs.getString("xm");
					String bm = rs.getString("bm");
					String gs = rs.getString("gs");
					String kssj = rs.getString("kssj");
					String jssj = rs.getString("jssj");
					
					Map<String, String> map = new HashMap<String, String>(5);
					map.put("xm", xm);
					map.put("bm", bm);
					map.put("gs", gs);
					map.put("kssj", kssj);
					map.put("jssj", jssj);
					return map;
				}
				
			}, deadline, deadline);
		} catch (Exception e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return list;
	}
	
	@Override
	public List<Map<String, String>> getAttendanceByOther(String signType, 
			String tablename, String deadline) {
		List<Map<String, String>> list = null;
		String task = "getAttendanceByOther";
		try {
			String sql = "select * from " + tablename + " "
				+ "where kqlx = ? and nvl(execStatus, -1) != 0 ";
			// 处理考勤数据存在只打卡一次的情况
			if ("1".equals(signType)) {
				sql += "and nvl(ksrq, jsrq) = ? and nvl(ksrq, jsrq) = ? ";
			// 去除已经保存到请假的数据
			} else if ("2".equals(signType)) {
				sql += "and ksrq <= ? and jsrq >= ? "
					+ "and xglc not in (select requestid from Bill_BoHaiLeave)";
			} else if ("3".equals(signType)) {
				sql += "and ksrq <= ? and jsrq >= ? ";
			// 去除已经保存到出差的数据
			} else if ("4".equals(signType)) {
				sql += "and ksrq <= ? and jsrq >= ? "
					+ "and xglc not in (select requestid from Bill_BoHaiEvection)";
			} else if ("5".equals(signType)) {
				sql += "and ksrq <= ? and jsrq >= ? ";
			} else {
				sql += "and ksrq <= ? and jsrq >= ? ";
			}
			
			if (isDebug()) logger.info(String.format(task + " sql: " + sql.replaceAll("\\?", "%s"), 
					signType, deadline, deadline));
			
			list = jdbcTemplate.query(sql, new RowMapper<Map<String, String>>() {

				@Override
				public Map<String, String> mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					String id = rs.getString("id");
					String xm = rs.getString("xm");
					String bm = rs.getString("bm");
					String gs = rs.getString("gs");
					String ksrq = rs.getString("ksrq");
					String kssj = rs.getString("kssj");
					String jsrq = rs.getString("jsrq");
					String jssj = rs.getString("jssj");
					String ts = rs.getString("ts");
					String xglc = rs.getString("xglc");
					
					Map<String, String> map = new HashMap<String, String>(10);
					map.put("id", id);
					map.put("xm", xm);
					map.put("bm", bm);
					map.put("gs", gs);
					map.put("ksrq", ksrq);
					map.put("kssj", kssj);
					map.put("jsrq", jsrq);
					map.put("jssj", jssj);
					map.put("ts", ts);
					map.put("xglc", xglc);
					return map;
				}
				
			}, signType, deadline, deadline);
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return list;
	}
	
	@Override
	public List<Map<String, String>> getAttendanceByOther(String signType,
			String tablename, String startDate, String startTime, String endDate,
			String endTime) {
		List<Map<String, String>> list = null;
		String task = "getAttendanceByOther";
		try {
			
			String sql = "select * from " + tablename + " "
				+ "where kqlx = ? ";
			Object[] args;
			
			// 处理考勤数据存在只打卡一次的情况
			if ("1".equals(signType)) {
				// 如果开始结束时间为空则获取所有的打卡数据
				if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
					sql += "and (ksrq is null or jsrq is null)";
					args = new Object[]{signType};
				} else {
					sql += "and nvl(ksrq, jsrq) >= ? and nvl(ksrq, jsrq) <= ? "
						+ "and ("
						+ "ksrq is null or jsrq is null "
						// 处理上午请假, 迟到打卡上班的问题; 目前应该不存在此种情况
						//+ "or (ksrq is not null and kssj > ?) "
						// TODO 处理下午请假, 提前打卡下班的问题
						//+ "or (jsrq is not null and jssj < ?) "
						+ ")";
					args = new Object[]{signType, startDate, endDate};
				}
			// 去除已经保存到请假的数据
			} else if ("2".equals(signType)) {
				sql += "and ksrq <= ? and jsrq >= ? "
					+ "and xglc not in (select requestid from Bill_BoHaiLeave)";
				args = new Object[]{signType, startDate, endDate};
			// 去除已经保存到出差的数据
			} else if ("4".equals(signType)) {
				sql += "and ksrq <= ? and jsrq >= ? "
					+ "and xglc not in (select requestid from Bill_BoHaiEvection)";
				args = new Object[]{signType, startDate, endDate};
			} else {
				sql += "and ksrq <= ? and jsrq >= ? ";
				args = new Object[]{signType, startDate, endDate};
			}
			
			if (isDebug()) logger.info(String.format(task + " sql: " + sql.replaceAll("\\?", "%s"), 
					args));
			
			list = jdbcTemplate.query(sql, new RowMapper<Map<String, String>>() {

				@Override
				public Map<String, String> mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					String xm = rs.getString("xm");
					String bm = rs.getString("bm");
					String gs = rs.getString("gs");
					String ksrq = rs.getString("ksrq");
					String kssj = rs.getString("kssj");
					String jsrq = rs.getString("jsrq");
					String jssj = rs.getString("jssj");
					String ts = rs.getString("ts");
					String xglc = rs.getString("xglc");
					
					Map<String, String> map = new HashMap<String, String>(9);
					map.put("xm", xm);
					map.put("bm", bm);
					map.put("gs", gs);
					map.put("ksrq", ksrq);
					map.put("kssj", kssj);
					map.put("jsrq", jsrq);
					map.put("jssj", jssj);
					map.put("ts", ts);
					map.put("xglc", xglc);
					return map;
				}
				
			}, args);
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return list;
	}
	
	@Override
	public List<Map<String, String>> getAttendanceByNoSign(int type, String signType,
			String tablename, String deadline, String subcompanyname) {
		List<Map<String, String>> list = null;
		String task = "getAttendanceByNoSign";
		try {
			String sql = "select a.id, a.lastname, a.mobile "
					+ "from hrmresource a "
					+ "where a.subcompanyid1 in ('" + subcompanyname + "') "
					+ "and a.status in (0, 1, 2, 3) ";
			if (type == 0) {
				sql += "and not exists (select 1 from " + tablename + " where xm in (a.id) "
						+ "and kqlx = '" + signType + "' and ksrq = '" + deadline + "')";
			} else if (type == 1) {
				sql += "and not exists (select 1 from " + tablename + " where xm in (a.id) "
						+ "and kqlx = '" + signType + "' and jsrq = '" + deadline + "')";
			} else if (type == 2) {
				sql += "and not exists (select 1 from " + tablename + " where xm in (a.id) "
						+ "and kqlx = '" + signType + "' and nvl(ksrq, jsrq) = '" + deadline + "')";
			} else if (type == 3) {
				
			} else if (type == 4) {
				
			} else if (type == 5) {
				sql += "and not exists (select 1 from HrmScheduleSign where userid in (a.id) "
						+ "and signType in (1, 2) and signDate = '" + deadline + "')";
			}
			
			if (isDebug()) logger.info(task + " sql: " + sql);
			
			list = jdbcTemplate.query(sql, new RowMapper<Map<String, String>>() {

				@Override
				public Map<String, String> mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					String id = rs.getString("id");
					String lastname = rs.getString("lastname");
					String mobile = rs.getString("mobile");
					
					Map<String, String> map = new HashMap<String, String>(3);
					map.put("id", id);
					map.put("lastname", lastname);
					map.put("mobile", mobile);
					return map;
				}
				
			});
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return list;
	}
	
	@Override
	public Map<String, Object> getAttendanceScheduleTimes(String scheduleType,
			String relatedid, String date) {
		Map<String, Object> map = null;
		String task = "getAttendanceScheduleTimes";
		try {
			String sql = "select * from hrmschedule "
					+ "where scheduletype = '" + scheduleType + "' and relatedid = '" + relatedid + "' "
					+ "and validedatefrom <= '" + date + "' and validedateto >= '" + date + "' ";
			
			if (isDebug()) logger.info(task + " sql: " + sql);
			
			map = jdbcTemplate.queryForMap(sql);
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return map;
	}
	
	@Override
	public Map<String, String> getUser(String userid) {
		Map<String, String> map = null;
		String task = "getUser";
		try {
			String sql = "select id, mobile, subcompanyid1, departmentid "
					+ "from hrmresource where id = ?";
			
			map = jdbcTemplate.queryForObject(sql, new RowMapper<Map<String, String>>() {

				@Override
				public Map<String, String> mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					String userid = rs.getString("id");
					String mobile = rs.getString("mobile");
					String subcompanyid =rs.getString("subcompanyid1");
					String departmentid = rs.getString("departmentid");
					
					Map<String, String> map = new HashMap<String, String>(3);
					map.put("userid", userid);
					map.put("mobile", mobile);
					map.put("subcompanyid", subcompanyid);
					map.put("departmentid", departmentid);
					return map;
				}
				
			}, userid);
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return map;
	}
	
	@Override
	public Map<String, String> getUserByLoginid(String loginid) {
		Map<String, String> map = null;
		String task = "getUserByLoginid";
		try {
			if (!StringUtils.isBlank(loginid)) loginid = loginid.trim();
			
			String sql = "select id, subcompanyid1, departmentid "
					+ "from hrmresource where loginid = ?";
			
			if (isDebug()) logger.info(String.format(task + " sql: " + sql.replaceAll("\\?", "%s."), 
					loginid));
			
			map = jdbcTemplate.queryForObject(sql, new RowMapper<Map<String, String>>() {

				@Override
				public Map<String, String> mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					String userid = rs.getString("id");
					String subcompanyid = rs.getString("subcompanyid1");
					String departmentid = rs.getString("departmentid");
					
					Map<String, String> map = new HashMap<String, String>(3);
					map.put("userid", userid);
					map.put("subcompanyid", subcompanyid);
					map.put("departmentid", departmentid);
					return map;
				}
				
			}, loginid);
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return map;
	}
	
	@Override
	public boolean updateAttendance(String tablename, String id, String execDate,
			String execTime, String execStatus, String execMessage) {
		boolean flag = false;
		String task = "updateAttendance";
		try {
			String sql = "update " + tablename + " set execDate = ?, execTime = ?,"
					+ " execStatus = ?, execMessage = ? where id = ?";
			
			if (isDebug()) logger.info(String.format(task + " sql: " + sql.replaceAll("\\?", "%s"), 
					execDate, execTime, execStatus, execMessage, id));
			
			int iFlag = jdbcTemplate.update(sql, execDate, execTime, execStatus, execMessage, id);
			flag = iFlag > 0;
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return flag;
	}


	public String getDatasourceId() {
		return datasourceId;
	}
	
	public boolean isDebug() {
		return isDebug;
	}
	
}
