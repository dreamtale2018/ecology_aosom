package weaver.interfaces.workflow.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;

import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * 退税通流程<br>
 * 归档, 将数据推送给 Oracle
 * 
 * @author liberal
 *
 */
public class ProductErrorArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductErrorArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	@Override
	public String execute(RequestInfo request) {
		try {
			String workflowid = request.getWorkflowid();
			
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			
			// 更新行信息
			List<Map<String, String>> electronicLineStatesList = new ArrayList<Map<String, String>>();
			// 更新临时表信息
			List<Map<String, String>> electronicIsCreateSoList = new ArrayList<Map<String, String>>();
			// 获取流程明细表 1
			List<Map<String, String>> detailAList = info.getDetailMap("1");
			if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					
					String cwlxDetailA = Util.null2String(detailAMap.get("CWLX"));			// 错误类型
					String bgdhDetailA = Util.null2String(detailAMap.get("BGDH"));			// 报关单号
					String ftdhDetailA = "";												// 分提单号
					if(cwlxDetailA.indexOf("报关单号错误")!=-1){
						ftdhDetailA = Util.null2String(detailAMap.get("FTDHSD"));
					}else{
						ftdhDetailA = Util.null2String(detailAMap.get("FTDH"));
					}
					String bgyclDetailA = WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "BGYCL", detailAMap.get("BGYCL"));
																							// 报关员处理
					Map<String, String> electronicLineStatesMap = new HashMap<String, String>();
					Map<String, String> electronicIsCreateSoMap = new HashMap<String, String>();
					if("OK".equals(bgyclDetailA)){
						if(cwlxDetailA.indexOf("行数不一致")!=-1){
							electronicLineStatesMap.put("CUSTOMS_NO", bgdhDetailA);
							electronicLineStatesMap.put("SUB_BL_NO", ftdhDetailA);
							electronicIsCreateSoMap.put("SUB_BL_NO", ftdhDetailA);
							electronicLineStatesList.add(electronicLineStatesMap);
							electronicIsCreateSoList.add(electronicIsCreateSoMap);
						}else{
							electronicLineStatesMap.put("CUSTOMS_NO", bgdhDetailA);
							electronicLineStatesMap.put("SUB_BL_NO", ftdhDetailA);
							electronicLineStatesList.add(electronicLineStatesMap);
						}
					}
				}
			}
			
			OracleResult<String, String> result1 = oracleManager.pushElectronicLineStates(electronicLineStatesList);
			OracleResult<String, String> result2 = oracleManager.pushElectronicIsCreateSo(electronicIsCreateSoList);
			if (result1 == null || !"0".equals(result1.getCode()) || result2 == null || !"0".equals(result2.getCode()) ) {
				request.getRequestManager().setMessage("操作失败 (-3)");
				if (result1 == null || result2 == null) {
					request.getRequestManager().setMessagecontent("数据推送失败, 请联系系统管理员.");
				} else {
					String electronicLineStates = "";
					String electronicIsCreateSo = "";
					electronicLineStates += getSUB_BL_NO(result1,"UpdateElectronicLineStates");
					electronicIsCreateSo += getSUB_BL_NO(result2,"UpdateElectronicIsCreateSo");
					if(!"".equals(electronicLineStates)){
						electronicLineStates = "更新行信息错误：" + electronicLineStates;
					}
					if(!"".equals(electronicIsCreateSo)){
						electronicIsCreateSo = "更新临时表信息错误：" + electronicIsCreateSo;
					}
					
					request.getRequestManager().setMessagecontent(String.format("数据推送失败; {%s}; 如有疑问, 请联系系统管理员.",
							electronicLineStates+electronicIsCreateSo));
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
	
	public String getSUB_BL_NO(OracleResult<String, String> result,String str){
		String SUB_BL_NO = "";
		if("-4".equals(result.getCode())){
			JSONObject jsonObject = JSONObject.fromObject(result.getMessage());
			JSONArray jsonArray = jsonObject.getJSONArray(str);
			for(int i=0;i<jsonArray.size();i++){
				if("False".equals(jsonArray.getJSONObject(i).get("states"))){
					SUB_BL_NO += jsonArray.getJSONObject(i).get("SUB_BL_NO") + ";";
				}
			}
		}
		return SUB_BL_NO;
	}
}
