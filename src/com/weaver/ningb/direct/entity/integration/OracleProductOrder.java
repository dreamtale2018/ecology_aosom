package com.weaver.ningb.direct.entity.integration;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 报价单信息
 * 
 * @author liberal
 *
 */
public class OracleProductOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> headContentMap;
	private List<Map<String, String>> detailContentList;
	
	public Map<String, String> getHeadContentMap() {
		return headContentMap;
	}
	public void setHeadContentMap(Map<String, String> headContentMap) {
		this.headContentMap = headContentMap;
	}
	public List<Map<String, String>> getDetailContentList() {
		return detailContentList;
	}
	public void setDetailContentList(List<Map<String, String>> detailContentList) {
		this.detailContentList = detailContentList;
	}
	
}
