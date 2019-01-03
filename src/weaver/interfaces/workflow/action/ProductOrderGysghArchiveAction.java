package weaver.interfaces.workflow.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import weaver.soa.workflow.request.RequestInfo;

import com.weaver.general.Util;
import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.direct.dao.workflow.ProductCodeDao;
import com.weaver.ningb.direct.dao.workflow.impl.ProductCodeDaoImpl;
import com.weaver.ningb.direct.entity.integration.OracleProductOrder;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * 供应商更换单流程结束节点推送报价单数据到oracle<br>
 * 归档, 将数据推送给 Oracle
 * 
 * @author ycj
 *
 */
public class ProductOrderGysghArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductOrderGysghArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	private ProductCodeDao dao = new ProductCodeDaoImpl();
	
	@Override
	public String execute(RequestInfo request) {
		try {
			String workflowid = request.getWorkflowid();
			String requestid = request.getRequestid();
			
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			Map<String, String> mainMap = info.getMainMap();
			String ywst = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "YWST", mainMap.get("YWST")));		// 业务实体
			if(ywst.indexOf("遨森电子商务股份有限公司")!=-1){
				ywst = "CHN";
			}else if(ywst.indexOf("遨森国际发展有限公司")!=-1){
				ywst = "HKI";
			}
			String bjdlx = "标准报价单";																						// 报价单类型
			String bz = null;																								// 币种
			String gys = oracleManager.getGysmc(mainMap.get("GHHGYS"));														// 供应商
			String dd = null;																								// 地点
			String lxr = null;																								// 联系人
			String zt = "Active";																							// 状态
			String sqrq = Util.null2String(mainMap.get("TBRQ"));															// 申请日期
			String cg = oracleManager.getRymc(mainMap.get("CG"));	
			cg = oracleManager.getChineseMsg(cg);																			// 采购
			String yxrqc = null;																							// 有效日期从
			String yxrqz = null;																							// 结束日期至
			//String sqr = Util.null2String(mainMap.get("SQR"));															// 申请人
			String lcbh = Util.null2String(mainMap.get("LCBH"));															// 流程编号
			
			// 货号信息
			List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
			OracleProductOrder po = new OracleProductOrder();
			List<Map<String, String>> detailContentList = new ArrayList<Map<String, String>>();
			Map<String, String> headContentMap = new HashMap<String, String>();
			headContentMap.put("ou_name", ywst);
			headContentMap.put("quotation_type", bjdlx);
			headContentMap.put("vendor_name", gys);
			headContentMap.put("vendor_site", dd);
			headContentMap.put("vendor_contact", lxr);
			headContentMap.put("status", zt);
			headContentMap.put("comments", lcbh);
			headContentMap.put("reply_date", sqrq);
			headContentMap.put("agent_name", cg+",");
			headContentMap.put("start_date", yxrqc);
			headContentMap.put("end_date", yxrqz);
			
			// 获取流程明细表 1
			List<Map<String, String>> detailAList = info.getDetailMap("1");
			if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					
					String hhDetailA = oracleManager.getHhmc(detailAMap.get("HH"));		// 货号名
					String wlmsDetailA = null;											// 物料描述
					String dwDetailA = null;											// 单位
					String jgDetailA = Util.null2String(detailAMap.get("JG"));			// 价格
					String gyswlDetailA = null;											// 供应商物料
					String jqDetailA = Util.null2String(WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "XGCJQ", detailAMap.get("XGCJQ")));		
																						// 交期
					String sfmrgysDetailA = "Y";										// 是否默认供应商
					String zxqdlDetailA = Util.null2String(detailAMap.get("ZXQDL"));	// 最小起订量
					String zdqdlDetailA = Util.null2String(detailAMap.get("ZDQDL"));	// 最大起订量
					String gysfzDetailA = Util.null2String(detailAMap.get("GYSFZ"));	// 供应商附注
					if(bz==null || "".equals(bz)){
						bz = Util.null2String(WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "BZCGJGTZ", detailAMap.get("BZCGJGTZ")));	
																						// 币种
					}
					if(bz.indexOf("RMB")!=-1){
						bz = "CNY";
					}
					
					Map<String, String> detailContentMap = new HashMap<String, String>();
					detailContentMap.put("item_code", hhDetailA);
					detailContentMap.put("item_description", wlmsDetailA);
					detailContentMap.put("uom_name", dwDetailA);
					detailContentMap.put("unit_price", jgDetailA);
					detailContentMap.put("vendor_product_num", gyswlDetailA);
					detailContentMap.put("delivery_num", jqDetailA);
					detailContentMap.put("default_vendor_flag", sfmrgysDetailA);
					detailContentMap.put("min_order_quantity", zxqdlDetailA);
					detailContentMap.put("max_order_quantity", zdqdlDetailA);
					detailContentMap.put("note_to_vendor", gysfzDetailA);
					
					detailContentList.add(detailContentMap);
					
				}
			}
			headContentMap.put("currency_code", bz);
			po.setHeadContentMap(headContentMap);
			po.setDetailContentList(detailContentList);
			
			poList.add(po);
			
			if (poList == null || poList.size() <= 0) {
				request.getRequestManager().setMessage("操作失败 (-2)");
				request.getRequestManager().setMessagecontent("数据推送失败; {流程信息为空}; 如有疑问, 请联系系统管理员.");
			}
			
			OracleResult<String, String> result = oracleManager.pushProductOrder(poList);
			if (result == null || !"0".equals(result.getCode())) {
				request.getRequestManager().setMessage("操作失败 (-3)");
				if (result == null) {
					request.getRequestManager().setMessagecontent("数据推送失败, 请联系系统管理员.");
				} else {
					request.getRequestManager().setMessagecontent(String.format("数据推送失败; {%s}; 如有疑问, 请联系系统管理员.",
							result.getMessage()));
				}
				return Action.FAILURE_AND_CONTINUE;
			} else {
				// 更新 Oracle 生成报价单编号到 OA 流程中
				String tablename = WorkflowUtils.getTablename(workflowid);
				boolean flag = dao.update(tablename, requestid, "BJDBH", result.getResponse());
				if (!flag) {
					request.getRequestManager().setMessage("操作失败 (-4)");
					request.getRequestManager().setMessagecontent("数据更新失败, {Oracle 报价单编号更新失败}; 如有疑问, 请联系系统管理员.");
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
