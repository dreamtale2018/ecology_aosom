package weaver.interfaces.workflow.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import weaver.soa.workflow.request.RequestInfo;

import com.weaver.general.Util;
import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.direct.entity.integration.OracleProductCode;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * 货号申请流程<br>
 * 归档, 将数据推送给 Oracle
 * 
 * @author ycj
 *
 */
public class ProductCode1ArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductCode1ArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	@Override
	public String execute(RequestInfo request) {
		try {
			String workflowid = request.getWorkflowid();
			
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			Map<String, String> mainMap = info.getMainMap();
			// 货号信息
			List<OracleProductCode> pcList = new ArrayList<OracleProductCode>();
			// 获取流程明细表 1
			List<Map<String, String>> detailAList = info.getDetailMap("1");
			if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					
					OracleProductCode pc = new OracleProductCode();
					
					String hhDetailA = detailAMap.get("HH");		// 货号
					Map<String, String> itemContentMap = new HashMap<String, String>();
					itemContentMap.put("item_code", hhDetailA);
					
					String ussl1DetailA = detailAMap.get("US");		// US 下单数量
					String casl1DetailA = detailAMap.get("CA");		// CA 下单数量
					String uksl1DetailA = detailAMap.get("UK");		// UK 下单数量
					String desl1DetailA = detailAMap.get("DE");		// DE 下单数量
					String frsl1DetailA = detailAMap.get("FR");		// FR 下单数量
					String itsl1DetailA = detailAMap.get("IT");		// IT 下单数量
					String essl1DetailA = detailAMap.get("ES");		// ES 下单数量
					String usjjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "USJJSX", mainMap.get("USJJSX")));		
																		// US季节属性
					usjjsxDetailA = oracleManager.getChineseMsg(usjjsxDetailA);
					String usjrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "USJRSX", mainMap.get("USJRSX")));	
																		// US节日属性
					usjrsxDetailA = oracleManager.getChineseMsg(usjrsxDetailA);
					String cajjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "CAJJSX", mainMap.get("CAJJSX")));		
																		// CA季节属性
					cajjsxDetailA = oracleManager.getChineseMsg(cajjsxDetailA);
					String cajrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "CAJRSX", mainMap.get("CAJRSX")));	
																		// CA节日属性
					cajrsxDetailA = oracleManager.getChineseMsg(cajrsxDetailA);
					String ukjjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "UKJJSX", mainMap.get("UKJJSX")));		
																		// UK季节属性
					ukjjsxDetailA = oracleManager.getChineseMsg(ukjjsxDetailA);
					String ukjrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "UKJRSX", mainMap.get("UKJRSX")));	
																		// UK节日属性
					ukjrsxDetailA = oracleManager.getChineseMsg(ukjrsxDetailA);
					String dejjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "DEJJSX", mainMap.get("DEJJSX")));		
																		// DE季节属性
					dejjsxDetailA = oracleManager.getChineseMsg(dejjsxDetailA);
					String dejrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "DEJRSX", mainMap.get("DEJRSX")));	
																		// DE节日属性
					dejrsxDetailA = oracleManager.getChineseMsg(dejrsxDetailA);
					String frjjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "FRJJSX", mainMap.get("FRJJSX")));		
																		// FR季节属性
					frjjsxDetailA = oracleManager.getChineseMsg(frjjsxDetailA);
					String frjrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "FRJRSX", mainMap.get("FRJRSX")));	
																		// FR节日属性
					frjrsxDetailA = oracleManager.getChineseMsg(frjrsxDetailA);
					String itjjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "ITJJSX", mainMap.get("ITJJSX")));		
																		// IT季节属性
					itjjsxDetailA = oracleManager.getChineseMsg(itjjsxDetailA);
					String itjrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "ITJRSX", mainMap.get("ITJRSX")));	
																		// IT节日属性
					itjrsxDetailA = oracleManager.getChineseMsg(itjrsxDetailA);
					String esjjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "ESJJSX", mainMap.get("ESJJSX")));		
																		// ES季节属性
					esjjsxDetailA = oracleManager.getChineseMsg(esjjsxDetailA);
					String esjrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "ESJRSX", mainMap.get("ESJRSX")));	
																		// ES节日属性
					esjrsxDetailA = oracleManager.getChineseMsg(esjrsxDetailA);
					// 内容处理
					List<Map<String, String>> ouContentList = new ArrayList<Map<String, String>>(1);
					if (ussl1DetailA!=null && !"".equals(ussl1DetailA)) {
						ouContentList.add(addOu("US", usjjsxDetailA, usjrsxDetailA));
					}
					if (casl1DetailA!=null && !"".equals(casl1DetailA)) {
						ouContentList.add(addOu("CA", cajjsxDetailA, cajrsxDetailA));
					}
					if (uksl1DetailA!=null && !"".equals(uksl1DetailA)) {
						ouContentList.add(addOu("GB", ukjjsxDetailA, ukjrsxDetailA));
					}
					if (desl1DetailA!=null && !"".equals(desl1DetailA)) {
						ouContentList.add(addOu("DE", dejjsxDetailA, dejrsxDetailA));
					}
					if (frsl1DetailA!=null && !"".equals(frsl1DetailA)) {
						ouContentList.add(addOu("FR", frjjsxDetailA, frjrsxDetailA));
					}
					if (itsl1DetailA!=null && !"".equals(itsl1DetailA)) {
						ouContentList.add(addOu("IT", itjjsxDetailA, itjrsxDetailA));
					}
					if (essl1DetailA!=null && !"".equals(essl1DetailA)) {
						ouContentList.add(addOu("ES", esjjsxDetailA, esjrsxDetailA));
					}
					
					pc.setItemContentMap(itemContentMap);
					pc.setOuContentList(ouContentList);
					
					pcList.add(pc);
				}
			}
			
			if (pcList == null || pcList.size() <= 0) {
				request.getRequestManager().setMessage("操作失败 (-2)");
				request.getRequestManager().setMessagecontent("数据推送失败; {流程信息为空}; 如有疑问, 请联系系统管理员.");
			}
			
			OracleResult<String, String> result = oracleManager.pushProductCode1(pcList);
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
	
	/**
	 * 添加国别信息项
	 * 
	 * @param ouName
	 * 					国别
	 * @param seasonCode
	 * 					季节属性
	 * @param festivalAttribute1
	 * 					节日属性
	 * @return 国别信息项
	 */
	private Map<String, String> addOu(String ouName, String seasonCode, String festivalAttribute1) {
		Map<String, String> ouContentMap = new HashMap<String, String>();
		ouContentMap.put("ou_name", ouName);
		ouContentMap.put("season_code", seasonCode);
		ouContentMap.put("festival_attribute1", festivalAttribute1);
		ouContentMap.put("festival_attribute2", null);
		ouContentMap.put("festival_attribute3", null);
		ouContentMap.put("brand_code", null);			
		ouContentMap.put("shiptype_code", null);		// 快递方式
		ouContentMap.put("tariff", null);				// 关税
		ouContentMap.put("item_status_code", null);		// 物料状态
		return ouContentMap;
	}
	
}
