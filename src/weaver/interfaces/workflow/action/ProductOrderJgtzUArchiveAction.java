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
 * 供应商更换单流程结束节点更新报价单数据到oracle<br>
 * 归档, 将数据推送给 Oracle
 * 
 * @author ycj
 *
 */
public class ProductOrderJgtzUArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductOrderJgtzUArchiveAction.class);

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
			}else if(ywst.indexOf("宁波遨森网络科技有限公司")!=-1){
				ywst = "NWK";
			}
			
			// 货号信息
			List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
			
			// 获取流程明细表 1
			List<Map<String, String>> detailAList = info.getDetailMap("1");
			if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					OracleProductOrder po = new OracleProductOrder();
					List<Map<String, String>> detailContentList = new ArrayList<Map<String, String>>();
					Map<String, String> headContentMap = new HashMap<String, String>();
					headContentMap.put("ou_name", ywst);
					Map<String, String> detailAMap = detailAList.get(i);
					
					String hhDetailA = oracleManager.getHhmc(detailAMap.get("HH"));		// 货号名
					String jgDetailA = Util.null2String(detailAMap.get("BGHCGJG"));		// 价格
					String ybjdbhDetailA = Util.null2String(detailAMap.get("YBJDBH"));	// 原报价单编号
					String jqDetailA = null;											// 交期
					String bz = null;													// 币种
					String sfmrgysDetailA = "Y";										// 是否默认供应商
					String zxqdlDetailA = null;											// 最小起订量
					String zdqdlDetailA = null;											// 最大起订量
					bz = Util.null2String(WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "BGHBZ", detailAMap.get("BGHBZ")));	
					if(bz.indexOf("RMB")!=-1){
						bz = "CNY";
					}
					
					Map<String, String> detailContentMap = new HashMap<String, String>();
					detailContentMap.put("item_code", hhDetailA);
					detailContentMap.put("unit_price", jgDetailA);
					detailContentMap.put("delivery_num", jqDetailA);
					detailContentMap.put("default_vendor_flag", sfmrgysDetailA);
					detailContentMap.put("min_order_quantity", zxqdlDetailA);
					detailContentMap.put("max_order_quantity", zdqdlDetailA);
					
					detailContentList.add(detailContentMap);
					
					headContentMap.put("currency_code", bz);
					headContentMap.put("quotation_doc_num", ybjdbhDetailA);
					po.setHeadContentMap(headContentMap);
					po.setDetailContentList(detailContentList);
					poList.add(po);
				}
			}
			
			
			if (poList == null || poList.size() <= 0) {
				request.getRequestManager().setMessage("操作失败 (-2)");
				request.getRequestManager().setMessagecontent("数据推送失败; {流程信息为空}; 如有疑问, 请联系系统管理员.");
			}
			
			OracleResult<String, String> result = oracleManager.updateProductOrder(poList);
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
