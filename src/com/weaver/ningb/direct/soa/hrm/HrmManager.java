package com.weaver.ningb.direct.soa.hrm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.general.Util;

/**
 * 人员 Manager
 * 
 * @author liberal
 *
 */
public class HrmManager {
	
	private static final Log logger = LogFactory.getLog(HrmManager.class);
	
	
	private RecordSetTrans rst;
	
	
	public HrmManager() {
		rst = new RecordSetTrans();
		rst.setAutoCommit(false);
	}
	
	
	/**
	 * 人员自定义信息类型
	 * 1.BASE, 基础资料
	 * 2.PERSON, 个人信息
	 * 3.WORK, 工作信息
	 * 
	 * @author liberal
	 *
	 */
	public static enum HrmData {BASE, PERSON, WORK}
	
	
	/**
	 * 更新用户信息
	 * 
	 * @param workcode
	 * 					用户编码
	 * @param map
	 * 					人员基本信息
	 * @param customMap
	 * 					人员自定义信息
	 * @return boolean：true.保存成功      false.保存失败
	 */
	public boolean updateData(String workcode, Map<String, String> map, 
			Map<HrmData, Map<String, String>> customMap) {
		boolean flag = true;
		
		// 未查到用户 id, 直接返回
		String userid = getUserid(workcode);
		if (StringUtils.isBlank(userid)) return flag;
		
		// 更新人员基本信息
		flag = update(userid, map);
		if (!flag) return flag;
		
		// 保存人员自定义信息
		for (Iterator<Entry<HrmData, Map<String, String>>> it = customMap.entrySet().iterator(); 
				it.hasNext();) {
			Map.Entry<HrmData, Map<String, String>> entry = it.next();
			HrmData key = entry.getKey();
			Map<String, String> value = entry.getValue();
			
			boolean customFlag = saveCustomData(userid, key, value);
			if (!customFlag) {
				this.rst.rollback();
				return customFlag;
			}
		}
		
		this.rst.commit();
		this.rst.setAutoCommit(true);		// not need
		
		return flag;
	}
	
	
	/**
	 * 更新用户基本信息
	 * 
	 * @param userid
	 * 					用户 id
	 * @param map
	 * 					用户基本信息
	 * @return boolean：true.保存成功      false.保存失败
	 */
	private boolean update(String userid, Map<String, String> map) {
		boolean flag = false;
		try {
			if (map == null || map.size() <= 0) return true;
			
			String sqlFields = "";
			for (Iterator<Entry<String, String>> it = map.entrySet().iterator(); 
					it.hasNext();) {
				Map.Entry<String, String> entry = it.next();
				String key = entry.getKey();
				String value = entry.getValue();
				
				if (StringUtils.isBlank(key)) continue;
				
				if (StringUtils.isBlank(sqlFields)) {
					sqlFields = key;
				} else {
					sqlFields += "," + key;
				}
				sqlFields += "=";
				if (StringUtils.isBlank(value)) {
					sqlFields += "''";
				} else {
					sqlFields += "'" + value + "'";
				}
			}
			
			String sql = "update hrmresource set " + sqlFields + " "
					+ "where id = '" + userid + "'";
			logger.info("save sql: " + sql);
			flag = rst.executeUpdate(sql);
		} catch (Exception e) {
			logger.error("save Exception: ", e);
		}
		return flag;
	}
	
	/**
	 * 保存人员自定义信息
	 * 
	 * @param userid
	 * 					人员 id
	 * @param hrmData
	 * 					HrmData
	 * @param map
	 * 					自定义数据
	 * @return boolean：true.保存成功      false.保存失败
	 */
	public boolean saveCustomData(String userid, HrmData hrmData, 
			Map<String, String> map) {
		boolean flag = true;
		String scopeid = getScopeid(hrmData);
		if (StringUtils.isBlank(scopeid)) return flag;
		
		String scope = "HrmCustomFieldByInfoType";			// 人员范围标识
		boolean checkFlag = checkExists(userid, scope, scopeid);
		if (!checkFlag) {
			flag = saveData(userid, scope, scopeid, map);
		} else {
			flag = updateData(userid, scope, scopeid, map);
		}
		return flag;
	}
	
	/**
	 * 保存自定义信息
	 * 
	 * @param userid
	 * 					用户 id
	 * @param scope
	 * 					范围
	 * @param scopeid
	 * 					范围 id
	 * @param map
	 * 					自定义数据
	 * @return boolean：true.保存成功      false.保存失败
	 */
	private boolean saveData(String userid, String scope, String scopeid, 
			Map<String, String> map) {
		boolean flag = false;
		try {
			Map<String, Map<String, String>> customTypeMap = getCustomType(scope, scopeid);
			String sqlFields = "";
			String sqlValues = "";
			for (Iterator<Entry<String, String>> it = map.entrySet().iterator(); 
					it.hasNext();) {
				Map.Entry<String, String> entry = it.next();
				String key = entry.getKey();
				String value = entry.getValue();
				
				if (StringUtils.isBlank(key)) continue;
				
				// 获取自定义字段信息, 如果为空表示数据库不存在此自定义字段, 不进行处理
				Map<String, String> customTypeChildMap = customTypeMap.get(key);
				if (customTypeChildMap == null || customTypeChildMap.size() <= 0) continue;
				
				if (StringUtils.isBlank(sqlFields)) {
					sqlFields = key;
				} else {
					sqlFields += "," + key;
				}
				if (StringUtils.isBlank(sqlValues)) {
					if (StringUtils.isBlank(value)) {
						sqlValues = "''";
					} else {
						sqlValues = "'" + value + "'";
					}
				} else {
					if (StringUtils.isBlank(value)) {
						sqlValues += ", ''";
					} else {
						sqlValues += "," + "'" + value + "'";
					}
				}
			}
			
			String sql = "insert into cus_fielddata (scope, scopeid, id, " + sqlFields + ") "
					+ "values('" + scope + "', '" + scopeid + "', '" + userid + "', " + sqlValues + ")";
			logger.info("saveData sql: " + sql);
			flag = rst.executeUpdate(sql);
		} catch (Exception e) {
			logger.error("saveData Exception: ", e);
		}
		return flag;
	}
	
	/**
	 * 更新自定义信息
	 * 
	 * @param userid
	 * 					用户 id
	 * @param scope
	 * 					范围
	 * @param scopeid
	 * 					范围 id
	 * @param map
	 * 					自定义数据
	 * @return boolean：true.更新成功      false.更新失败
	 */
	private boolean updateData(String userid, String scope, String scopeid, 
			Map<String, String> map) {
		boolean flag = false;
		try {
			Map<String, Map<String, String>> customTypeMap = getCustomType(scope, scopeid);
			String sqlFields = "";
			for (Iterator<Entry<String, String>> it = map.entrySet().iterator(); 
					it.hasNext();) {
				Map.Entry<String, String> entry = it.next();
				String key = entry.getKey();
				String value = entry.getValue();
				
				if (StringUtils.isBlank(key)) continue;
				
				// 获取自定义字段信息, 如果为空表示数据库不存在此自定义字段, 不进行处理
				Map<String, String> customTypeChildMap = customTypeMap.get(key);
				if (customTypeChildMap == null || customTypeChildMap.size() <= 0) continue;
				
				if (StringUtils.isBlank(sqlFields)) {
					sqlFields = key;
				} else {
					sqlFields += "," + key;
				}
				sqlFields += "=";
				if (StringUtils.isBlank(value)) {
					sqlFields += "''";
				} else {
					sqlFields += "'" + value + "'";
				}
			}
			
			String sql = "update cus_fielddata set " + sqlFields + " "
					+ "where id = '" + userid + "' and scope = '" + scope + "' and scopeid = '" + scopeid + "'";
			logger.info("updateData sql: " + sql);
			flag = rst.executeUpdate(sql);
		} catch (Exception e) {
			logger.error("updateData Exception: ", e);
		}
		return flag;
	}
	
	
	/**
	 * 获取范围 id
	 * 
	 * @param hrmData
	 * 					HrmData
	 * @return String
	 */
	private String getScopeid(HrmData hrmData) {
		String result = "";
		if (hrmData == HrmData.BASE) {
			result = "-1";
		} else if (hrmData == HrmData.PERSON) {
			result = "1";
		} else if (hrmData == HrmData.WORK) {
			result = "3";
		} else {
		}
		return result;
	}
	
	
	/**
	 * 获取用户 id, 根据用户编码
	 * 
	 * @param workcode
	 * 					用户编码
	 * @return String
	 */
	private String getUserid(String workcode) {
		String userid = "";
		RecordSet rs = new RecordSet();
		String sql = "select id from hrmresource where workcode = '" + workcode + "'";
		rs.execute(sql);
		if (rs.next()) userid = rs.getString("id");
		return userid;
	}
	
	/**
	 * 获取自定义字段信息
	 * 
	 * @param scope
	 * 					范围
	 * @param scopeid
	 * 					范围 id
	 * @return Map
	 */
	private Map<String, Map<String, String>> getCustomType(String scope, String scopeid) {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		RecordSet rs = new RecordSet();
		String sql = "select t1.fieldid, t1.hrm_fieldlable, t1.ismand, t2.fielddbtype, t2.fieldhtmltype, "
				+ "t2.type, t1.dmlurl "
				+ "from cus_formfield t1 "
				+ "join cus_formdict t2 on t1.fieldid = t2.id "
				+ "where t1.scope = '" + scope + "' and t1.scopeid = '" + scopeid + "' "
				+ "order by t1.fieldorder";
		rs.execute(sql);
		while (rs.next()) {
			String fieldid = Util.null2String(rs.getString("fieldid"));
			String fielddbtype = Util.null2String(rs.getString("fielddbtype"));
			String fieldhtmltype = Util.null2String(rs.getString("fieldhtmltype"));
			String type = Util.null2String(rs.getString("type"));
			String dmlurl = Util.null2String(rs.getString("dmlurl"));
			
			Map<String, String> childMap = new HashMap<String, String>();
			childMap.put("fieldid", fieldid);
			childMap.put("fielddbtype", fielddbtype);
			childMap.put("fieldhtmltype", fieldhtmltype);
			childMap.put("type", type);
			childMap.put("dmlurl", dmlurl);
			map.put("field" + fieldid, childMap);
		}
		return map;
	}
	
	
	/**
	 * 判断人员自定义信息记录是否已经添加
	 * 
	 * @param userid
	 * 					用户 id
	 * @param scope
	 * 					范围
	 * @param scopeid
	 * 					范围 id
	 * @return boolean：true.存在      false.不存在
	 */
	private boolean checkExists(String userid, String scope, String scopeid) {
		boolean flag = false;
		RecordSet rs = new RecordSet();
		String sql = "select 1 from cus_fielddata "
				+ "where id = '" + userid + "' and scope = '" + scope + "' "
				+ "and scopeid = '" + scopeid + "' ";
		rs.execute(sql);
		if (rs.next()) flag = true;
		return flag;
	}
	
}
