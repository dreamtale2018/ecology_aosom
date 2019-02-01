package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 预付款申请流程<br>
 * 归档, 将数据推送给 Oracle
 * 
 * @author ycj
 *
 */
public class ProductApplyThArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductApplyThArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	@Override
	public String execute(RequestInfo request) {
	    RecordSet rs = new RecordSet();
	    
	    String SQR = "";
		String SPSJ = "";			//审批时间
		try {
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			Map<String, String> mainMap = info.getMainMap();
			SPSJ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String ywst = Util.null2String(mainMap.get("YWST"));															// 业务实体
			if(ywst.indexOf("遨森电子商务股份有限公司")!=-1 || ywst.indexOf("Aosom E-Commerce Inc")!=-1){
				ywst = "CHN";
			}else if(ywst.indexOf("遨森国际发展有限公司")!=-1 || ywst.indexOf("AOSOM INTERNATIONAL DEVELOPMENT CO.,LIMITED")!=-1){
				ywst = "HKI";
			}
			String bz = Util.null2String(mainMap.get("BZ"));																// 币种
			String lcbh = Util.null2String(mainMap.get("LCBH"));															// 流程编号
			String fklx = Util.null2String(mainMap.get("FKLX"));															// 付款类型
			String sqr = Util.null2String(mainMap.get("SQR"));																// 申请人
			if(!"".equals(sqr)){
	        	String sql = "select loginid from hrmresource where id = '" + sqr + "'";
		        rs.execute(sql);
		        if (rs.next()) {
		        	SQR = Util.null2String(rs.getString("loginid"));
		        }
	        }
			
			// 预付款申请信息
			List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
			OracleProductOrder po = new OracleProductOrder();
			List<Map<String, String>> detailContentList = new ArrayList<Map<String, String>>();
			Map<String, String> headContentMap = new HashMap<String, String>();
			headContentMap.put("ou_name", ywst);
			headContentMap.put("oa_payment_doc_num", lcbh);
			headContentMap.put("currency_code", bz);
			headContentMap.put("payment_doc_type", fklx);
			headContentMap.put("approved_user_name", SQR);
			headContentMap.put("approved_date", SPSJ);
			headContentMap.put("status_name", "WITHDRAW");
			
			// 获取流程明细表 1
			List<Map<String, String>> detailAList = info.getDetailMap("1");
			if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					
					String qkbhDetailA = Util.null2String(detailAMap.get("QKBH"));			// 价格
					String bzDetailA = Util.null2String(detailAMap.get("BZ"));				// 备注
					
					Map<String, String> detailContentMap = new HashMap<String, String>();
					detailContentMap.put("payment_doc_num", qkbhDetailA);
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
			}
			
			OracleResult<String, String> result = oracleManager.pushProductApply(poList);
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
