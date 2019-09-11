package weaver.interfaces.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import weaver.conn.RecordSet;

import com.weaver.general.Util;
import com.weaver.ningb.direct.entity.integration.OracleProductOrder;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;

/**
 * 同步 费用支付台账 数据到 oracle中
 * 
 * @author liberal
 *
 */
public class PaymentJob extends BaseCronJob {

	private static final Log logger = LogFactory.getLog(PaymentJob.class);
	
	
	private OracleManager manager = null;;
	
	RecordSet rs = new RecordSet();
	@Override
	public void execute() {
		String task = "PaymentJob";
		try {
			logger.info("---------------- " + task + " start ----------------");
			long startTime = System.currentTimeMillis();
			if (manager == null) manager = new OracleManager();
			
			//获取费用支付台账中的相关信息。
			String sql = "select id,fkzt,bxdh,fkbz,rq,modedatacreatedate,zy,jfkjkmdm,dfkjkmdm,je from uf_fyzftz where zt is null OR zt = '0'";
			rs.execute(sql);
	        while (rs.next()) {
	        	List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
	        	OracleProductOrder po = new OracleProductOrder();
	        	String fkzt = Util.null2String(rs.getString("fkzt"));
	        	if(!"MH STAR UK LTD".equals(fkzt)){
	        		if("遨森电子商务股份有限公司".equals(fkzt) || "深圳遨森电子商务有限公司".equals(fkzt)){
		        		fkzt = "CHN";
		        	}else if("遨森国际发展有限公司".equals(fkzt)){
		        		fkzt = "HKI";
		        	}else if("宁波遨森网络科技有限公司".equals(fkzt)){
		        		fkzt = "NIT";
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
						headContentMap.put("accounting_date", createdate);
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
					
					OracleResult<String, String> result = manager.updateStatus(poList,"GL","pushOrderInsert");
					if (result == null || !"0".equals(result.getCode())) {
						if("-4".equals(result.getCode())){
							sql = "update uf_fyzftz set zt = '2' where bxdh = '" + bxdh + "'";
							rs.execute(sql);
						}
						if (result == null) {
							logger.error("数据推送失败, 请联系系统管理员.");
						} else {
							logger.error(String.format("数据推送失败; {%s}; 如有疑问, 请联系系统管理员.",
									result.getMessage()));
						}
					}else{
						sql = "update uf_fyzftz set zt = '1' where bxdh = '" + bxdh + "'";
						rs.execute(sql);
					}
	        	}
	        }
			
			long endTime = System.currentTimeMillis();
			logger.info("times = " + (endTime - startTime) / 1000 + " s");
			logger.info("---------------- " + task + " end ----------------");
		} finally {
			manager = null;
		}
	}
	
//	public static void main(String[] args) {
//		try {
//			OAServiceContract proxy = new OAServiceContractProxy();
//			
//			GetProductInfo productInfo = new GetProductInfo();
//			productInfo.setProductID("01-0337");
//			productInfo.setOperater("121");
//			Calendar calen = Calendar.getInstance();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			calen.setTime(sdf.parse("2017-01-28 00:51:39"));
//			productInfo.setLastupdate_datetime(calen);
//			
//			String response = proxy.getProduct(productInfo, "OAAdmin", "OA123456@");
//		} catch (RemoteException e) {
//		} catch (ParseException e) {
//		}
		
//		try {
//			OAServiceContract proxy = new OAServiceContractProxy();
//			GetProductInfo info = new GetProductInfo();
//			info.setProductID("01-0337");
//			info.setOperater("121");
//			Calendar calen = Calendar.getInstance();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			calen.setTime(sdf.parse("2017-01-28 00:51:39"));
//			info.setLastupdate_datetime(calen);
//			String response = proxy.getProduct(info, "OAAdmin", "OA123456@");
//			System.out.println(response);
//			String aa = "<?xml version = \"1.0\" encoding = \"UTF-8\" standalone='yes'?>";
//			System.out.println(aa.indexOf("<?xml version"));
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}
	
}
