package com.weaver.ningb.direct.entity.integration;

import java.io.Serializable;

/**
 * Oracle 执行结果
 * 
 * @author liberal
 *
 */
public class OracleResult<TRequest, TResponse> implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String type;			// 调用类型
	private String task;			// 调用方法
	private String code;			// 返回编码：0.成功
	private String message;			// 返回信息
	private TRequest request;		// 请求信息
	private TResponse response;		// 返回信息
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TRequest getRequest() {
		return request;
	}

	public void setRequest(TRequest request) {
		this.request = request;
	}

	public TResponse getResponse() {
		return response;
	}

	public void setResponse(TResponse response) {
		this.response = response;
	}
	
}
