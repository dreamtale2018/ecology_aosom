package weaver.interfaces.workflow.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

import weaver.conn.RecordSet;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.general.Util;
import com.weaver.ningb.direct.entity.integration.OracleProductOrder;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * 新品排单表数据到oracle<br>
 * 归档, 将数据推送给 Oracle
 * 
 * @author ycj
 *
 */
public class ProductOrderInsertArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductOrderInsertArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	RecordSet rs = new RecordSet();
	@Override
	public String execute(RequestInfo request) {
		try {
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			Map<String, String> mainMap = info.getMainMap();
			String lcbh = Util.null2String(mainMap.get("LCBH"));
			//获取国别台账中的所有国别。
			String sql = "select org_information5,organization_id from uf_gb order by organization_id";
			rs.execute(sql);
			Map<String,String> gbMap = new HashMap<String,String>();
	        while (rs.next()) {
	        	String gbName = Util.null2String(rs.getString(1));
	        	String gbId = Util.null2String(rs.getString(2));
	        	gbMap.put(gbName,gbId);
	        }
	        List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
	        Iterator<Map.Entry<String, String>> iterator = gbMap.entrySet().iterator();
	        while(iterator.hasNext()){
	        	boolean flag = false;
	        	Entry<String, String> entry = iterator.next();
	        	String gbName = (String) entry.getKey();
	        	String gbId = (String) entry.getValue();
	        	OracleProductOrder po = new OracleProductOrder();
				List<Map<String, String>> detailContentList = new ArrayList<Map<String, String>>();
	        	Map<String, String> headContentMap = new HashMap<String, String>();
	        	headContentMap.put("oa_org_id", gbId);
	        	// 获取流程明细表 1
				List<Map<String, String>> detailAList = info.getDetailMap("1");
				if (detailAList != null && detailAList.size() > 0) {
					for (int j = 0; j < detailAList.size(); j++) {
						Map<String, String> detailAMap = detailAList.get(j);
						
						String hhDetailA = Util.null2String(detailAMap.get("HH"));			// 货号
						hhDetailA = oracleManager.getHhmc(hhDetailA);
						String sl1DetailA = Util.null2String(detailAMap.get(gbName+"SL1"));	// 完成数量
						String jq1DetailA = Util.null2String(detailAMap.get(gbName+"JQ1"));	// 交期
						
						Map<String, String> detailContentMap = new HashMap<String, String>();
						if(!"".equals(sl1DetailA) && !"0".equals(sl1DetailA) && !"".equals(jq1DetailA)){
							flag = true;
							detailContentMap.put("oa_quantity", sl1DetailA);
							detailContentMap.put("oa_item_number", hhDetailA);
							detailContentMap.put("oa_target_date", jq1DetailA);
							detailContentMap.put("oa_description", lcbh);
							detailContentList.add(detailContentMap);
						}
					}
					for (int j = 0; j < detailAList.size(); j++) {
						Map<String, String> detailAMap = detailAList.get(j);
						
						String hhDetailA = Util.null2String(detailAMap.get("HH"));			// 货号
						hhDetailA = oracleManager.getHhmc(hhDetailA);
						String sl2DetailA = Util.null2String(detailAMap.get(gbName+"SL2"));	// 完成数量
						String jq2DetailA = Util.null2String(detailAMap.get(gbName+"JQ2"));	// 交期
						
						Map<String, String> detailContentMap = new HashMap<String, String>();
						if(!"".equals(sl2DetailA) && !"0".equals(sl2DetailA) && !"".equals(jq2DetailA)){
							flag = true;
							detailContentMap.put("oa_quantity", sl2DetailA);
							detailContentMap.put("oa_item_number", hhDetailA);
							detailContentMap.put("oa_target_date", jq2DetailA);
							detailContentMap.put("oa_description", lcbh);
							detailContentList.add(detailContentMap);
						}
					}
				}
				if(flag){
					po.setHeadContentMap(headContentMap);
					po.setDetailContentList(detailContentList);
					poList.add(po);
				}
	        }
			
			/*if (poList == null || poList.size() <= 0) {
				request.getRequestManager().setMessage("操作失败 (-2)");
				request.getRequestManager().setMessagecontent("数据推送失败; {流程信息为空}; 如有疑问, 请联系系统管理员.");
				return Action.FAILURE_AND_CONTINUE;
			}*/
	        
	        if (poList == null || poList.size() <= 0) {
	        	return Action.SUCCESS;
			}
			
			OracleResult<String, String> result = oracleManager.updateStatus(poList,"PS","pushOrderInsert");
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
