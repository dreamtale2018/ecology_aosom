package weaver.interfaces.workflow.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.datacontract.schemas._2004._07.MH_EBSOAWcfService.UpdateStatusJson;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.direct.entity.integration.OracleProductOrder;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * IT系统账号/权限申请单流程数据<br>
 * 结束节点, 将数据推送给 Oracle自动创建账号
 * 
 * @author liberal
 *
 */
public class HrmImportOracleAction implements Action {
	
	private static final Log logger = LogFactory.getLog(PurchaseRejectAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	RecordSet rs = new RecordSet();
	@Override
	public String execute(RequestInfo request) {
		try {
			String workflowid = request.getWorkflowid();
			String requestid = request.getRequestid();
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			Map<String, String> mainMap = info.getMainMap();
			
			String LCBH = Util.null2String(mainMap.get("LCBH"));
			
			// 禁出港口信息
			List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
			
			UpdateStatusJson pinfo = new UpdateStatusJson();
			
			String nodeid = oracleManager.getCurrentNodeId(requestid);
			pinfo.setPrimaryKey(LCBH);
			pinfo.setWorkFlowID(workflowid);
			pinfo.setSpldID(nodeid);
			pinfo.setUpdateType("PEOPLE");
			
			String oracle = Util.null2String(mainMap.get("ORACLE"));	// ORACLE
			if("1".equals(oracle)){
				String gh = Util.null2String(mainMap.get("GH"));			// 工号
				String xm = Util.null2String(mainMap.get("SQR"));	        // 姓名ID
				String lastname = oracleManager.getRymc(xm);				// 姓名
				String cnName = oracleManager.getChineseMsg(lastname);
				String enName = oracleManager.getEnglishMsg(lastname);
				String xbDetail = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "XB", mainMap.get("XB")));	
				// 性别
				if("女".equals(xbDetail)){
					xbDetail = "F";
				}else{
					xbDetail = "M";
				}
				OracleProductOrder po = new OracleProductOrder();
				Map<String, String> headContentMap = new HashMap<String, String>();
				headContentMap.put("employee_number", gh);
				headContentMap.put("cn_name", cnName);
				headContentMap.put("en_name", enName);
				headContentMap.put("sex", xbDetail);
				headContentMap.put("key", "123456a");
				headContentMap.put("pda", "N");
				
				po.setHeadContentMap(headContentMap);
				poList.add(po);
				
				
				if (poList != null && poList.size() > 0) {
					OracleResult<String, String> result = oracleManager.updateStatusV2(poList,pinfo,"PushHrmImportOracle");
					if (result == null || !"0".equals(result.getCode())) {
						request.getRequestManager().setMessage("操作失败 (-3)");
						if (result == null) {
							request.getRequestManager().setMessagecontent("数据推送失败, 请联系系统管理员.");
						} else {
							request.getRequestManager().setMessagecontent(String.format("数据推送失败; {%s}; 如有疑问, 请联系系统管理员.",
									result.getMessage()));
						}
						return Action.FAILURE_AND_CONTINUE;
					} 
				}
			}
		} catch (Exception e) {
			logger.error("Failure: ", e);
			request.getRequestManager().setMessage("操作失败 (10020001)");
			request.getRequestManager().setMessagecontent("提交异常, 请联系系统管理员.");
			return Action.FAILURE_AND_CONTINUE;
		}
		return Action.SUCCESS;
	}

}
