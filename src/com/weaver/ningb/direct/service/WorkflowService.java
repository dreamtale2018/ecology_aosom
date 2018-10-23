package com.weaver.ningb.direct.service;

/**
 * 货号申请 WebService
 * 
 * @author liberal
 *
 */
public interface WorkflowService {

	/**
	 * 更新货号申请单货号
	 * 
	 * @param requestid
	 * 					流程 requestid
	 * @param detailid
	 * 					流程明细 id
	 * @param code
	 * 					流程货号
	 * @return 操作结果：true.成功      false.失败      exception.异常
	 */
	public String updateCode(String requestid, String detailid, String code);
	
}
