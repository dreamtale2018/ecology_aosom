package weaver.interfaces.workflow.action;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetItemORGQtyInfo;

import weaver.conn.RecordSet;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.general.Util;
import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * 产品终止申请单流程03节点提交后，获取SKU 对应的供应链数量<br>
 * 获取 Oracle中的供应链数量并更新
 * 
 * @author ycj
 *
 */
public class ProductCpzzQtyArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductCpzzQtyArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	RecordSet rs = new RecordSet();
	RecordSet rs1 = new RecordSet();
	@Override
	public String execute(RequestInfo request) {
		try {
			String workflowid = request.getWorkflowid();
			ActionInfo info = ActionUtils.getActionInfo(request);
			
			// 获取流程明细表 1
			List<Map<String, String>> detailAList = info.getDetailMap("1");
			if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					
					String mxhhDetailA = Util.null2String(detailAMap.get("id"));					// 行号
					String hhDetailA = Util.null2String(detailAMap.get("HH"));						// 货号
					hhDetailA = oracleManager.getHhmc(hhDetailA);
					String xzzgjDetailA = Util.null2String(WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "XZZGJ", detailAMap.get("XZZGJ")));
					// 需终止国别
					String orgidDetailA = "";
					String sql1 = "select organization_id from uf_gb where org_information5 = '" + xzzgjDetailA + "'";
					rs1.execute(sql1);
					if(rs1.next()){
						orgidDetailA = Util.null2String(rs1.getString("organization_id"));
					}
					GetItemORGQtyInfo pinfo = new GetItemORGQtyInfo();
					pinfo.setORGID(Integer.parseInt(orgidDetailA));
					pinfo.setItemName(hhDetailA);
					
					OracleResult<String, String> result = oracleManager.getItemORGQtyV2(pinfo,"GetProductCpzzQty");
					if (result == null || !"0".equals(result.getCode())) {
						request.getRequestManager().setMessage("操作失败 (-3)");
						if (result == null) {
							request.getRequestManager().setMessagecontent("数据推送失败, 请联系系统管理员.");
						} else {
							request.getRequestManager().setMessagecontent(String.format("数据推送失败; {%s}; 如有疑问, 请联系系统管理员.",
									result.getMessage()));
						}
						return Action.FAILURE_AND_CONTINUE;
					} else{
						String response = result.getResponse();
						JSONArray jsonArray = JSONArray.fromObject(response);     	//把字符串转成json数组
						JSONObject processNote = (JSONObject) jsonArray.get(0);   	//取第一个json对象  
					    Integer wlck = (Integer) processNote.get("OnHandQty");    
					    Integer gckc = (Integer) processNote.get("SUPPLIER_INV_QUANTITY");    
					    Integer zz = (Integer) processNote.get("UNCOMPLETE_QUANTITY");    
					    String sql = "update formtable_main_95_dt1 set WLCK = '" + wlck + "',GCKC = '" + gckc +
					                 "',ZZ = '" + zz + "' where id = '" + mxhhDetailA + "'";
					    rs.execute(sql);
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
