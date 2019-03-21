package weaver.interfaces.workflow.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

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
 * 采购合同取消流程供应链助理节点提交后，明细数据传入oracle关闭合同行<br>
 * 归档, 将数据推送给 Oracle
 * 
 * @author ycj
 *
 */
public class ProductCghtCloseArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductCghtCloseArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	RecordSet rs = new RecordSet();
	
	String sql = "";
	
	@Override
	public String execute(RequestInfo request) {
	    
		try {
			String workflowid = request.getWorkflowid();
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			Map<String, String> mainMap = info.getMainMap();
			String ywst = Util.null2String(mainMap.get("YWST")); 												// 业务实体
			sql = "select ywst from uf_YWSTDYBZ where id = '" + ywst + "'";
			rs.execute(sql);
			if(rs.next()){
				ywst = rs.getString("ywst");
			}
			// 业务实体																				
			if(ywst.indexOf("遨森电子商务股份有限公司")!=-1){
				ywst = "CHN";
			}else if(ywst.indexOf("遨森国际发展有限公司")!=-1){
				ywst = "HKI";
			}
			
			// 合同号关闭信息
			List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
			
			// 获取流程明细表 1
			List<Map<String, String>> detailAList = info.getDetailMap("1");
			if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					
					String hztDetailA = Util.null2String(detailAMap.get("HZT"));								// 行状态
					if("0".equals(hztDetailA)){
						OracleProductOrder po = new OracleProductOrder();
						List<Map<String, String>> detailContentList = new ArrayList<Map<String, String>>();
						Map<String, String> headContentMap = new HashMap<String, String>();
						String qxhthDetailA = Util.null2String(detailAMap.get("GXHTH"));						// 购销合同号
						headContentMap.put("ou_name", ywst);
						headContentMap.put("po_num", qxhthDetailA);
						String hanghDetailA = Util.null2String(detailAMap.get("HANGH"));						// 行号
						String hhDetailA = oracleManager.getHhmc(Util.null2String(detailAMap.get("HH")));		// 货号文本
						if("0".equals(hztDetailA)){
							hztDetailA = "CLOSED";
						}else if("1".equals(hztDetailA)){
							hztDetailA = "INVOICE CLOSED";
						}else if("2".equals(hztDetailA)){
							hztDetailA = "RECEIVE CLOSED";
						}
						String qxyyDetailA = Util.null2String(WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "QXYY", detailAMap.get("QXYY")));								
																												// 取消原因
						String qxyybzDetailA = Util.null2String(detailAMap.get("BZ"));							// 取消原因备注
						
						Map<String, String> detailContentMap = new HashMap<String, String>();
						detailContentMap.put("po_line_id", "0");
						detailContentMap.put("line_num", hanghDetailA);
						detailContentMap.put("item_code", hhDetailA);
						detailContentMap.put("status_code", hztDetailA);
						detailContentMap.put("remark", qxyyDetailA+qxyybzDetailA);
						
						detailContentList.add(detailContentMap);
						po.setHeadContentMap(headContentMap);
						po.setDetailContentList(detailContentList);
						poList.add(po);
					}
				}
			}
			
			
			if (poList != null && poList.size() > 0) {
				OracleResult<String, String> result = oracleManager.pushProductClose(poList);
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
		} catch (Exception e) {
			logger.error("Failure: ", e);
			request.getRequestManager().setMessage("操作失败 (-1)");
			request.getRequestManager().setMessagecontent("提交异常, 请联系系统管理员.");
			return Action.FAILURE_AND_CONTINUE;
		}
		return Action.SUCCESS;
	}
	
}
