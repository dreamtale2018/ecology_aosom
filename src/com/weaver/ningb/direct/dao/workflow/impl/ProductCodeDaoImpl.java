package com.weaver.ningb.direct.dao.workflow.impl;

import org.apache.commons.logging.Log;

import weaver.conn.RecordSet;

import com.weaver.ningb.dao.DataAccessException;
import com.weaver.ningb.direct.dao.workflow.ProductCodeDao;
import com.weaver.ningb.jdbc.core.JdbcTemplate;
import com.weaver.ningb.jdbc.oa.JdbcTemplateFactory;
import com.weaver.ningb.logging.LogFactory;

public class ProductCodeDaoImpl implements ProductCodeDao {

	private static final Log logger = LogFactory.getLog(ProductCodeDaoImpl.class);
	
	private JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate(); 
	
	@Override
	public boolean update(String tablename, String requestid, 
			String key, String value) {
		boolean flag = false;
		String task = "update";
		RecordSet rs = new RecordSet();
		try {
			//String sql = "update " + tablename + " set " + key + " = ? where requestid = ? ";
			String sql = "update " + tablename + " set " + key + " = '" + value + "' where requestid = '" + requestid + "'";
			flag = rs.execute(sql);
			//int iFlag = jdbcTemplate.update(sql, value, requestid);
			//flag = iFlag > 0;
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateDetail(String tablename, String detailid,
			String key, String value) {
		boolean flag = false;
		String task = "updateDetail";
		RecordSet rs = new RecordSet();
		try {
			//String sql = "update " + tablename + " set " + key + " = ? where id = ? ";
			String sql = "update " + tablename + " set " + key + " = '" + value + "' where id = '" + detailid + "'";
			logger.info(task + ": " + sql);
			flag = rs.execute(sql);
			//int iFlag = jdbcTemplate.update(sql, value, detailid);
			//flag = iFlag > 0;
		} catch (DataAccessException e) {
			logger.error(task + " Exception: " + e.getMessage());
		}
		return flag;
	}

}
