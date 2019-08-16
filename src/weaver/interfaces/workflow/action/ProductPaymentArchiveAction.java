package weaver.interfaces.workflow.action;

import java.util.ArrayList;
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

/**
 * 费用支付台账<br>
 * 定时将数据推送给 Oracle
 * 
 * @author ycj
 *
 */
public class ProductPaymentArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductPaymentArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	
	RecordSet rs = new RecordSet();
	@Override
	public String execute(RequestInfo request) {
		try {
			//获取费用支付台账中的相关信息。
			String sql = "select id,fkzt,bxdh,fkbz,rq,modedatacreatedate,zy,jfkjkmdm,dfkjkmdm,je from uf_fyzftz where zt is null OR zt = '0'";
			rs.execute(sql);
	        while (rs.next()) {
	        	List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
	        	OracleProductOrder po = new OracleProductOrder();
	        	String fkzt = Util.null2String(rs.getString("fkzt"));
	        	if("遨森电子商务股份有限公司".equals(fkzt) || "遨森国际发展有限公司".equals(fkzt) || "深圳遨森电子商务有限公司".equals(fkzt)){
	        		fkzt = "CHN";
	        	}else if("MH STAR UK LTD".equals(fkzt)){
	        		fkzt = "UK";
	        	}
	        	String bxdh = Util.null2String(rs.getString("bxdh"));
	        	String fkbz = Util.null2String(rs.getString("fkbz"));
	        	String createdate = Util.null2String(rs.getString("modedatacreatedate"));
	        	String zy = Util.null2String(rs.getString("zy"));
	        	String jfkjkmdm = Util.null2String(rs.getString("jfkjkmdm"));
	        	String dfkjkmdm = Util.null2String(rs.getString("dfkjkmdm"));
	        	String je = Util.null2String(rs.getString("je"));
	        	Map<String, String> headContentMap = new HashMap<String, String>();
				headContentMap.put("ou_code", fkzt);
				headContentMap.put("oa_num", bxdh);
				headContentMap.put("gl_name", zy);
				headContentMap.put("rf5_name", zy);
				headContentMap.put("currency_code", fkbz);
				if("".equals(createdate)){
					headContentMap.put("period_name", createdate);
					headContentMap.put("accounting_date", createdate);
				}else{
					headContentMap.put("period_name", createdate.substring(0,7));
					headContentMap.put("accounting_date", createdate.substring(0,7));
				}
	        	po.setHeadContentMap(headContentMap);
	        	List<Map<String, String>> detailContentList = new ArrayList<Map<String, String>>();
	        	Map<String, String> detailContentMap = new HashMap<String, String>();
				detailContentMap.put("reference10", zy);
				detailContentMap.put("debit_account_code", jfkjkmdm);
				detailContentMap.put("crebit_account_code", dfkjkmdm);
				detailContentMap.put("account_amt", je);
				detailContentList.add(detailContentMap);
				po.setDetailContentList(detailContentList);
				poList.add(po);
				
				OracleResult<String, String> result = oracleManager.updateStatus(poList,"GL","pushOrderInsert");
				if (result == null || !"0".equals(result.getCode())) {
					if(!"-4".equals(result.getCode())){
						sql = "update uf_fyzftz set zt = '2' where bxdh = '" + bxdh + "'";
						rs.execute(sql);
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
					sql = "update uf_fyzftz set zt = '1' where bxdh = '" + bxdh + "'";
					rs.execute(sql);
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
