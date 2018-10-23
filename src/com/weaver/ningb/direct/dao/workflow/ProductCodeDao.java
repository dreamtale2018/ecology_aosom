package com.weaver.ningb.direct.dao.workflow;

/**
 * 货号申请 DAO
 * 
 * @author liberal
 *
 */
public interface ProductCodeDao {
	
	/**
	 * 更新流程主表数据
	 * 
	 * @param tablename
	 * 					流程自定义表单名称
	 * @param requestid
	 * 					流程 requestid
	 * @param key
	 * 					更新字段
	 * @param value
	 * 					更新字段值
	 * @return boolean：true.成功      false.失败
	 */
	public boolean update(String tablename, String requestid,
			String key, String code);
	
	/**
	 * 更新流程明细表数据
	 * 
	 * @param tablename
	 * 					流程自定义表单名称
	 * @param detailid
	 * 					流程明细表 id
	 * @param key
	 * 					更新字段
	 * @param value
	 * 					更新字段值
	 * @return boolean：true.成功      false.失败
	 */
	public boolean updateDetail(String tablename, String detailid,
			String key, String value);
	
}
