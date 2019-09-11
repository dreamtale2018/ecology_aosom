package weaver.interfaces.workflow.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import weaver.general.MD5;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.direct.soa.hrm.HrmManager;
import com.weaver.ningb.direct.soa.hrm.HrmManager.HrmData;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * 海外人员录用审批流程<br>
 * 归档后, 更新人员自定义信息并初始化人员登录信息
 * 
 * @author liberal
 *
 */
public class HrmHwfgsArchiveAction implements Action {

	private static final Log logger = LogFactory.getLog(HrmHwfgsArchiveAction.class);
	
	@Override
	public String execute(RequestInfo request) {
		try {
			HrmManager hrmManager = new HrmManager();
			
			ActionInfo info = ActionUtils.getActionInfo(request);
			
			// 获取主表信息
			Map<String, String> mainTable = info.getMainMap();
			String yggh = mainTable.get("YGGH");		// 员工工号
			String mpcwzw = mainTable.get("MPCWZW");	// 名片称谓 (中文)
			String mpcwyw = mainTable.get("MPCWYW");	// 名片称谓 (英文)
			String jjlxrxm = mainTable.get("JJLXRXM");	// 紧急联系人姓名
			String jjlxrsjh = mainTable.get("LXFS");
														// 紧急联系人手机号
			String bysj = mainTable.get("BYSJ");		// 毕业时间
			String ysxl = mainTable.get("YSXL");		// 原始学历
			String xx = mainTable.get("BYXX");			// 学校
			String zy = mainTable.get("ZY");			// 专业
			String sfzdz = mainTable.get("XJZD");		// 身份证地址
			String htqdcs = mainTable.get("HTQDCS");	// 合同签订次数
			String ldhtqx = mainTable.get("LDHTQX");	// 劳动合同期限
			
			
			// 整理人员基本信息
			Map<String, String> baseMap = new HashMap<String, String>();
			baseMap.put("loginid", yggh);
			baseMap.put("password", new MD5().getMD5ofStr("123456"));		// 设置人员默认密码 123456
			baseMap.put("seclevel", aqjb);
			
			// 整理人员自定义信息
			Map<HrmData, Map<String, String>> customMap = new HashMap<HrmData, Map<String, String>>();
			
			// 人员自定义个人信息
			Map<String, String> customPersonMap = new HashMap<String, String>();
			customPersonMap.put("field0", mpcwzw);
			customPersonMap.put("field1", mpcwyw);
			customPersonMap.put("field5", jjlxrxm);
			customPersonMap.put("field7", jjlxrsjh);
			customPersonMap.put("field9", bysj);
			customPersonMap.put("field10", ysxl);
			customPersonMap.put("field11", xx);
			customPersonMap.put("field12", zy);
			customPersonMap.put("field13", sfzdz);
			
			customMap.put(HrmData.PERSON, customPersonMap);
			
			
			// 人员自定义工作信息
			Map<String, String> customWorkMap = new HashMap<String, String>();
			customWorkMap.put("field20", htqdcs);
			customWorkMap.put("field21", ldhtqx);
			customWorkMap.put("field22", ldhtqx);
			
			customMap.put(HrmData.WORK, customWorkMap);
			
			boolean flag = hrmManager.updateData(yggh, baseMap, customMap);
			// TODO 如果保存失败, 输出失败工号到日志不进行流程提交失败操作; 避免系统重复创建账号问题
			if (!flag) {
				logger.error(String.format("HrmArchiveAction updateData Failure (workcode = %s).", 
						yggh));
			}
		} catch (Exception e) {
			logger.error("HrmArchiveAction Failure: ", e);
			request.getRequestManager().setMessage("操作失败 (10010001)");
			request.getRequestManager().setMessagecontent("提交异常, 请联系系统管理员.");
			return Action.FAILURE_AND_CONTINUE;
		}
		return Action.SUCCESS;
	}
	
}
