package weaver.interfaces.workflow.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.datacontract.schemas._2004._07.MH_EBSOAWcfService.UpdateStatusJson;

import weaver.conn.RecordSet;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.general.Util;
import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.direct.entity.integration.OracleProductOrder;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * 产品终止申请单流程03节点提交后，国别终止数据传入oracle<br>
 * 将数据推送给 Oracle
 * 
 * @author ycj
 *
 */
public class ProductTerminateArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductTerminateArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	String LCBH = "";						//	流程编号
	
	RecordSet rs = new RecordSet();
	RecordSet rs1 = new RecordSet();
	@Override
	public String execute(RequestInfo request) {
		try {
			String workflowid = request.getWorkflowid();
			String requestid = request.getRequestid();
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			Map<String, String> mainMap = info.getMainMap();
			
			String LCBH = Util.null2String(mainMap.get("LCBH"));
			
			UpdateStatusJson pinfo = new UpdateStatusJson();
			
			String nodeid = "";
			String sql = "select currentnodeid from workflow_requestbase where requestid = '" + requestid + "'";
			rs.execute(sql);
			if(rs.next()){
				nodeid = Util.null2String(rs.getString("currentnodeid"));
			}
			pinfo.setPrimaryKey(LCBH);
			pinfo.setWorkFlowID(workflowid);
			pinfo.setSpldID(nodeid);
			pinfo.setUpdateType("ITEMCLOSE");
			
			// 获取流程明细表 1
			List<Map<String, String>> detailAList = info.getDetailMap("1");
			if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					
					String gbxsyjDetailA = Util.null2String(detailAMap.get("GBXSYJ"));					// 国别销售意见
					if("0".equals(gbxsyjDetailA)){
						String hhDetailA = Util.null2String(detailAMap.get("HH"));						// 货号
						hhDetailA = oracleManager.getHhmc(hhDetailA);
						String xzzgjDetailA = Util.null2String(WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "XZZGJ", detailAMap.get("XZZGJ")));
						// 需终止国别
						OracleProductOrder po = new OracleProductOrder();
						Map<String, String> headContentMap = new HashMap<String, String>();
						String orgidDetailA = "";
						String sql1 = "select organization_id from uf_gb where org_information5 = '" + xzzgjDetailA + "'";
						rs1.execute(sql1);
						if(rs1.next()){
							orgidDetailA = Util.null2String(rs1.getString("organization_id"));
						}
						headContentMap.put("org_id", orgidDetailA);
						headContentMap.put("item", hhDetailA);
						
						po.setHeadContentMap(headContentMap);
						String oaItemJson = oracleManager.createRequestJson3(po);
						OracleResult<String, String> result = oracleManager.updateStatusV2(oaItemJson,pinfo,"PushProductTermination");
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
			}
		} catch (Exception e) {
			logger.error("Failure: ", e);
			request.getRequestManager().setMessage("操作失败 (-1)");
			request.getRequestManager().setMessagecontent("提交异常, 请联系系统管理员.");
			return Action.FAILURE_AND_CONTINUE;
		}
		return Action.SUCCESS;
	}
	
}
