package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * 采购合同取消申请单<br>
 * 结束节点将数据推送给 Oracle
 * 
 * @author ycj
 *
 */
public class ProductOrderCancelArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductOrderCancelArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	RecordSet rs = new RecordSet();
	@Override
	public String execute(RequestInfo request) {
		try {
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			Map<String, String> mainMap = info.getMainMap();
			String kkje = Util.null2String(mainMap.get("KKJE")); 							// 扣款金额
			if(!"0".equals(kkje)){
				String htqxdh = Util.null2String(mainMap.get("HTQXDH")); 					// 合同取消单号
				String ywst = Util.null2String(mainMap.get("YWST")); 						// 业务实体
				String bz = "";																// 币种
				String sql = "select bz from uf_YWSTDYBZ where id = '" + ywst + "'";
				rs.execute(sql);
				if(rs.next()){
					bz = rs.getString("bz");
				}
				String gys = Util.null2String(mainMap.get("GYS")); 							// 供应商
				String gysId = "";
	        	String gysName = "";
	        	sql = "select vendor_id,vendor_name from uf_vendor where id = '" + gys + "'";
	        	rs.execute(sql);
		        if (rs.next()) {
		        	gysId = Util.null2String(rs.getString("vendor_id"));
		        	gysName = Util.null2String(rs.getString("vendor_name"));
		        }
				String zdr = Util.null2String(mainMap.get("SQR")); 							// 制单人
				String zdrGh = "";
	        	String zdrName = "";
	        	sql = "select workcode,lastname from hrmresource where id = '" + zdr + "'";
				rs.execute(sql);
		        if (rs.next()) {
		        	zdrGh = Util.null2String(rs.getString("workcode"));
		        	zdrName = Util.null2String(rs.getString("lastname"));
		        	zdrName = oracleManager.getChineseMsg(zdrName);
		        }
		        String zdrq = new SimpleDateFormat("yyyy-MM-dd").format(new Date());					// 制单日期
				
				List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
				
				OracleProductOrder po = new OracleProductOrder();
				Map<String, String> headContentMap = new HashMap<String, String>();
				headContentMap.put("oa_num", htqxdh);
				headContentMap.put("vendor_id", gysId);
				headContentMap.put("vendor_name", gysName);
				headContentMap.put("create_code", zdrGh);
				headContentMap.put("create_name", zdrName);
				headContentMap.put("create_date", zdrq);
				headContentMap.put("deduction_type", "合同行关闭");
				headContentMap.put("comments", "");
	        	po.setHeadContentMap(headContentMap);
	        	List<Map<String, String>> detailContentList = new ArrayList<Map<String, String>>();
	        	Map<String, String> detailContentMap = new HashMap<String, String>();
				detailContentMap.put("currency_code", bz);
				detailContentMap.put("confirm_amount", kkje);
				detailContentMap.put("po_number", "");
				detailContentMap.put("buyer_code", zdrGh);
				detailContentList.add(detailContentMap);
				po.setDetailContentList(detailContentList);
				poList.add(po);
				
				OracleResult<String, String> result = oracleManager.updateStatus(poList,"CLAIM","pushOrderClaim");
				Pattern pattern = Pattern.compile("QX[0-9]{10}");
				Matcher isNum = pattern.matcher(result.getResponse());
				if (result == null || (!"0".equals(result.getCode()) && !isNum.matches())) {
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
