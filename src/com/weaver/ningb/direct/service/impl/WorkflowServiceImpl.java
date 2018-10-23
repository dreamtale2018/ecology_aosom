package com.weaver.ningb.direct.service.impl;

import org.apache.commons.logging.Log;

import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.direct.dao.workflow.ProductCodeDao;
import com.weaver.ningb.direct.dao.workflow.impl.ProductCodeDaoImpl;
import com.weaver.ningb.direct.service.WorkflowService;
import com.weaver.ningb.logging.LogFactory;

public class WorkflowServiceImpl implements WorkflowService {

	private static final Log logger = LogFactory.getLog(WorkflowServiceImpl.class);
	
	private ProductCodeDao dao = new ProductCodeDaoImpl();
	
	@Override
	public String updateCode(String requestid, String detailid, String code) {
		String result = "true";
		String task = "updateCode";
		try {
			String tablename = WorkflowUtils.getDetailTablenameByRequestid(requestid, 1);
			boolean flag = dao.updateDetail(tablename, detailid, "HH", code);
			result = flag + "";
		} catch (Exception e) {
			logger.error(task + String.format(" Exception: requestid = %s, detailid = %s, code = %s: ", requestid, detailid, code));
			result = "exception";
		}
		return result;
	}

}
