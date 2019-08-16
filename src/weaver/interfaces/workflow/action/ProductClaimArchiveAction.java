package weaver.interfaces.workflow.action;

import java.util.ArrayList;
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

/**
 * 索赔管理台账<br>
 * 定时将数据推送给 Oracle
 * 
 * @author ycj
 *
 */
public class ProductClaimArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductClaimArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	RecordSet rs = new RecordSet();
	RecordSet rs1 = new RecordSet();
	RecordSet rs2 = new RecordSet();
	@Override
	public String execute(RequestInfo request) {
		try {
			//获取费用支付台账中的相关信息。
			String sql = "select * from uf_spgltz where zt = '1' and (tszt is null OR tszt = '0')";
			rs.execute(sql);
	        while (rs.next()) {
	        	List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
	        	OracleProductOrder po = new OracleProductOrder();
	        	String gys = Util.null2String(rs.getString("gys"));
	        	String gysId = "";
	        	String gysName = "";
	        	String sql1 = "select vendor_id,vendor_name from uf_vendor where id = '" + gys + "'";
				rs1.execute(sql1);
		        if (rs1.next()) {
		        	gysId = Util.null2String(rs1.getString("vendor_id"));
		        	gysName = Util.null2String(rs1.getString("vendor_name"));
		        }
	        	String spdh = Util.null2String(rs.getString("spdh"));
	        	String zdr = Util.null2String(rs.getString("zdr"));
	        	String zdrGh = "";
	        	String zdrName = "";
	        	String sql2 = "select workcode,lastname from hrmresource where id = '" + zdr + "'";
				rs2.execute(sql2);
		        if (rs2.next()) {
		        	zdrGh = Util.null2String(rs2.getString("workcode"));
		        	zdrName = Util.null2String(rs2.getString("lastname"));
		        	zdrName = oracleManager.getChineseMsg(zdrName);
		        }
	        	String zdrq = Util.null2String(rs.getString("zdrq"));
	        	String sjsp = Util.null2String(rs.getString("sjsp"));
	        	String cgy = Util.null2String(rs.getString("cgy"));
	        	String cgyGh = "";
	        	sql2 = "select workcode from hrmresource where id = '" + cgy + "'";
				rs2.execute(sql2);
		        if (rs2.next()) {
		        	cgyGh = Util.null2String(rs2.getString("workcode"));
		        }
	        	String bz = Util.null2String(rs.getString("bz"));
	        	if("0".equals(bz)){
	        		bz = "CNY";
	        	}else if("1".equals(bz)){
	        		bz = "USD";
	        	}else{
	        		bz = "";
	        	}
	        	Map<String, String> headContentMap = new HashMap<String, String>();
				headContentMap.put("oa_num", spdh);
				headContentMap.put("vendor_id", gysId);
				headContentMap.put("vendor_name", gysName);
				headContentMap.put("create_code", zdrGh);
				headContentMap.put("create_name", zdrName);
				headContentMap.put("create_date", zdrq);
				headContentMap.put("deduction_type", "索赔单");
				headContentMap.put("comments", "");
	        	po.setHeadContentMap(headContentMap);
	        	List<Map<String, String>> detailContentList = new ArrayList<Map<String, String>>();
	        	Map<String, String> detailContentMap = new HashMap<String, String>();
				detailContentMap.put("currency_code", bz);
				detailContentMap.put("confirm_amount", sjsp);
				detailContentMap.put("po_number", "");
				detailContentMap.put("buyer_code", cgyGh);
				detailContentList.add(detailContentMap);
				po.setDetailContentList(detailContentList);
				poList.add(po);
				
				OracleResult<String, String> result = oracleManager.updateStatus(poList,"CLAIM","pushOrderClaim");
				Pattern pattern = Pattern.compile("QX[0-9]{10}");
				Matcher isNum = pattern.matcher(result.getResponse());
				if (result == null || (!"0".equals(result.getCode()) && !isNum.matches())) {
					if(!"-4".equals(result.getCode())){
						sql1 = "update uf_spgltz set tszt = '2' where spdh = '" + spdh + "'";
						rs1.execute(sql1);
					}
					request.getRequestManager().setMessage("操作失败 (-3)");
					if (result == null) {
						request.getRequestManager().setMessagecontent("数据推送失败, 请联系系统管理员.");
					} else {
						request.getRequestManager().setMessagecontent(String.format("数据推送失败; {%s}; 如有疑问, 请联系系统管理员.",
								result.getMessage()));
					}
					return Action.FAILURE_AND_CONTINUE;
				}else{
					sql1 = "update uf_spgltz set ORASPDH = '" + result.getResponse() + "',tszt = '1' where spdh = '" + spdh + "'";
					rs1.execute(sql1);
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
