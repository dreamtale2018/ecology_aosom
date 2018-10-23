package com.weaver.ningb.direct.entity.integration;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 产品货号信息
 * 
 * @author liberal
 *
 */
public class OracleProductCode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> itemContentMap;
	private List<Map<String, String>> boxContentList;
	private List<Map<String, String>> bomContentList;
	private List<Map<String, String>> ouContentList;
	
	public Map<String, String> getItemContentMap() {
		return itemContentMap;
	}

	public void setItemContentMap(Map<String, String> itemContentMap) {
		this.itemContentMap = itemContentMap;
	}

	public List<Map<String, String>> getBoxContentList() {
		return boxContentList;
	}

	public void setBoxContentList(List<Map<String, String>> boxContentList) {
		this.boxContentList = boxContentList;
	}

	public List<Map<String, String>> getBomContentList() {
		return bomContentList;
	}

	public void setBomContentList(List<Map<String, String>> bomContentList) {
		this.bomContentList = bomContentList;
	}

	public List<Map<String, String>> getOuContentList() {
		return ouContentList;
	}

	public void setOuContentList(List<Map<String, String>> ouContentList) {
		this.ouContentList = ouContentList;
	}
	
}
