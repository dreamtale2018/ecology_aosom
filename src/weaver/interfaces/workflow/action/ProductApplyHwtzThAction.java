package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.ERPManager;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * 海外投资付款审批流程数据<br>
 * 拒绝, 将数据推送给 ERP
 * 
 * @author liberal
 *
 */
public class ProductApplyHwtzThAction implements Action {
	
	private static final Log logger = LogFactory.getLog(PurchaseRejectAction.class);
	
	
	private ERPManager erpManager = new ERPManager();
	

	@Override
	public String execute(RequestInfo request) {
		try {
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			String lastoperator = request.getLastoperator();
			Map<String, String> mainMap = info.getMainMap();
			
			String contractNo = Util.null2o(mainMap.get("FKTZSBH"));	// 付款通知书编号
			String status = "reject";									// 退回状态
			
			if (StringUtils.isBlank(contractNo)) {
				request.getRequestManager().setMessage("操作失败 (10020002)");
				request.getRequestManager().setMessagecontent("数据保存失败; {付款通知书编号为空}; 如有疑问, 请联系系统管理员.");
				return Action.FAILURE_AND_CONTINUE;
			}
			
			OracleResult<String, String> result = erpManager.updateOAApprovalStatus("PaymentApproval", contractNo, status, lastoperator, "ProductApplyHwtzTg");
			if (result == null || !"0".equals(result.getCode())) {
				request.getRequestManager().setMessage("操作失败 (10020003)");
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
			request.getRequestManager().setMessage("操作失败 (10020001)");
			request.getRequestManager().setMessagecontent("提交异常, 请联系系统管理员.");
			return Action.FAILURE_AND_CONTINUE;
		}
		return Action.SUCCESS;
	}

}
