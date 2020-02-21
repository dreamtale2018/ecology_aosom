package weaver.interfaces.workflow.action;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetItemORGQtyInfo;

import weaver.conn.RecordSet;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.general.Util;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * 产品投诉表子表流程02节点提交后，获取SKU 对应的供应链数量<br>
 * 获取 Oracle中的供应链数量并更新
 * 
 * @author ycj
 *
 */
public class ProductCptszbQtyArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductCptszbQtyArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	RecordSet rs = new RecordSet();
	RecordSet rs1 = new RecordSet();
	@Override
	public String execute(RequestInfo request) {
		try {
			String requestid = request.getRequestid();
			ActionInfo info = ActionUtils.getActionInfo(request);
			
			// 获取流程主表数据
			Map<String, String> mainMap = info.getMainMap();
			String xsgb = Util.null2String(mainMap.get("XSGB")).trim().toUpperCase();					// 销售国别
			String hh = Util.null2String(mainMap.get("HH"));											// 货号
			hh = oracleManager.getHhmc(hh);
			// 需终止国别
			String orgid = "";
			String sql1 = "select organization_id from uf_gb where org_information5 = '" + xsgb + "'";
			rs1.execute(sql1);
			if(rs1.next()){
				orgid = Util.null2String(rs1.getString("organization_id"));
			}
			GetItemORGQtyInfo pinfo = new GetItemORGQtyInfo();
			pinfo.setORGID(Integer.parseInt(orgid));
			pinfo.setItemName(hh);
			
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
			    Integer wlck = (Integer) processNote.get("SUPPLIER_INV_QUANTITY");    
			    Integer gckc = (Integer) processNote.get("UNCOMPLETE_QUANTITY");    
			    Integer zz = (Integer) processNote.get("OnHandQty");    
			    String sql = "update formtable_main_158 set " + xsgb + "WLC = '" + wlck + "'," + xsgb + "GCKC = '" + gckc +
			                 "'," + xsgb + "ZZ = '" + zz + "' where requestid = '" + requestid + "'";
			    rs.execute(sql);
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
