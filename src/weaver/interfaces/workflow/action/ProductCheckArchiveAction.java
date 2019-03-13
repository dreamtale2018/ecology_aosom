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
 * 产品检验单数据到oracle<br>
 * 归档, 将数据推送给 Oracle
 * 
 * @author ycj
 *
 */
public class ProductCheckArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductCheckArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	RecordSet rs = new RecordSet();
	@Override
	public String execute(RequestInfo request) {
		try {
			String workflowid = request.getWorkflowid();
			
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			Map<String, String> mainMap = info.getMainMap();
			String tid = Util.null2String(mainMap.get("TID"));																// 头ID 
			String sqr = Util.null2String(mainMap.get("SQR"));	
			if(!"".equals(sqr)){
	        	String sql = "select workcode from hrmresource where id = '" + sqr + "'";
		        rs.execute(sql);
		        if (rs.next()) {
		        	sqr = Util.null2String(rs.getString("workcode"));
		        }
	        }
			String jyrq = Util.null2String(mainMap.get("JYRQ"));															// 检验日期
			
			// 货号信息
			List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
			OracleProductOrder po = new OracleProductOrder();
			List<Map<String, String>> detailContentList = new ArrayList<Map<String, String>>();
			Map<String, String> headContentMap = new HashMap<String, String>();
			headContentMap.put("oa_qc_doc_num", tid);
			headContentMap.put("approved_user_name", sqr);
			headContentMap.put("approved_date", jyrq);
			headContentMap.put("status_name", "已分配");
			
			// 获取流程明细表 1
			List<Map<String, String>> detailAList = info.getDetailMap("1");
			if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					
					String headidDetailA = Util.null2String(detailAMap.get("HEADID"));	// 采购头ID
					String lineidDetailA = Util.null2String(detailAMap.get("LINEID"));	// 采购行ID
					String hhidDetailA = Util.null2String(detailAMap.get("HHID"));		// 质检单行号ID
					String jyjgDetailA = Util.null2String(WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "JYJG", detailAMap.get("JYJG")));		
																						// 检验结果
					//String pdjgDetailA = Util.null2String(detailAMap.get("PDJG"));	// 判定结果
					String pdjgDetailA = "";											// 判定结果
					if("已免检".equals(jyjgDetailA)){
						jyjgDetailA = "EXEMPTION";
						pdjgDetailA = "NULL";
					}else if("不合格".equals(jyjgDetailA)){
						jyjgDetailA = "FAIL";
						pdjgDetailA = "REWORK";
					}else if("未质检".equals(jyjgDetailA)){
						jyjgDetailA = "NULL";
						pdjgDetailA = "NOTHING";
					}else if("合格".equals(jyjgDetailA)){
						jyjgDetailA = "STANDARD";
						pdjgDetailA = "RCV";
					}
					String wcslDetailA = Util.null2String(detailAMap.get("WCSL"));		// 完成数量
					String bzDetailA = Util.null2String(detailAMap.get("BZ"));			// 备注
					
					Map<String, String> detailContentMap = new HashMap<String, String>();
					detailContentMap.put("po_header_id", headidDetailA);
					detailContentMap.put("po_line_id", lineidDetailA);
					detailContentMap.put("qc_doc_num_detail", hhidDetailA);
					detailContentMap.put("qc_result_code", jyjgDetailA);
					detailContentMap.put("qc_treatment_code", pdjgDetailA);
					detailContentMap.put("qc_confirmed_qty", wcslDetailA);
					detailContentMap.put("start_date_active_str", jyrq);
					detailContentMap.put("end_date_active_str", jyrq);
					detailContentMap.put("task_Date_str", jyrq);
					detailContentMap.put("memo", bzDetailA);
					
					detailContentList.add(detailContentMap);
					
				}
			}
			po.setHeadContentMap(headContentMap);
			po.setDetailContentList(detailContentList);
			
			poList.add(po);
			
			if (poList == null || poList.size() <= 0) {
				request.getRequestManager().setMessage("操作失败 (-2)");
				request.getRequestManager().setMessagecontent("数据推送失败; {流程信息为空}; 如有疑问, 请联系系统管理员.");
				return Action.FAILURE_AND_CONTINUE;
			}
			
			OracleResult<String, String> result = oracleManager.pushProductCheck(poList);
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
		} catch (Exception e) {
			logger.error("Failure: ", e);
			request.getRequestManager().setMessage("操作失败 (-1)");
			request.getRequestManager().setMessagecontent("提交异常, 请联系系统管理员.");
			return Action.FAILURE_AND_CONTINUE;
		}
		return Action.SUCCESS;
	}
	
}
