package com.weaver.ningb.direct.manager.integration;

import java.util.Map;

/**
 * Oracle 数据同步过滤器
 * 
 * @author liberal
 *
 */
public interface OracleFilter {
	
	/**
	 * 判断接口返回数据是否需要同步
	 * 
	 * @param query
	 * 					接口信息
	 * @param name
	 * 					接口返回数据名称
	 * @param value
	 * 					接口返回数据值
	 * @return boolean：true.同步      false.不同步
	 */
	public boolean accepts(Map<String, String> query, String name, String value);

}
