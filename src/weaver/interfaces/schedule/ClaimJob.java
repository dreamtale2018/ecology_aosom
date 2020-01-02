package weaver.interfaces.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;

import weaver.conn.RecordSet;

import com.weaver.general.Util;
import com.weaver.ningb.direct.entity.integration.OracleProductOrder;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;

/**
 * 同步 索赔管理台账 数据到 oracle中
 * 
 * @author liberal
 *
 */
public class ClaimJob extends BaseCronJob {

	private static final Log logger = LogFactory.getLog(ClaimJob.class);
	
	
	private OracleManager manager = null;
	
	RecordSet rs = new RecordSet();
	RecordSet rs1 = new RecordSet();
	RecordSet rs2 = new RecordSet();
	@Override
	public void execute() {
		String task = "ClaimJob";
		try {
			logger.info("---------------- " + task + " start ----------------");
			long startTime = System.currentTimeMillis();
			if (manager == null) manager = new OracleManager();
			
			//获取费用支付台账中的相关信息。
			String sql = "select * from uf_spgltz where zt = '1' and spdh is not null and (tszt is null OR tszt = '0')";
			rs.execute(sql);
	        while (rs.next()) {
	        	String gys = Util.null2String(rs.getString("gys"));
	        	List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
	        	OracleProductOrder po = new OracleProductOrder();
	        	String sjsp = Util.null2String(rs.getString("sjsp"));
	        	if(sjsp.indexOf(",")!=-1){
	        		sjsp = sjsp.replace(",", "");
	        	}
	        	String spdh = Util.null2String(rs.getString("spdh"));
	        	if(!"0".equals(sjsp)){
	        		String gysId = "";
		        	String gysName = "";
		        	String sql1 = "select vendor_id,vendor_name from uf_vendor where id = '" + gys + "'";
					rs1.execute(sql1);
			        if (rs1.next()) {
			        	gysId = Util.null2String(rs1.getString("vendor_id"));
			        	gysName = Util.null2String(rs1.getString("vendor_name"));
			        }
		        	String zdr = Util.null2String(rs.getString("zdr"));
		        	String zdrGh = "";
		        	String zdrName = "";
		        	String sql2 = "select workcode,lastname from hrmresource where id = '" + zdr + "'";
					rs2.execute(sql2);
			        if (rs2.next()) {
			        	zdrGh = Util.null2String(rs2.getString("workcode"));
			        	zdrName = Util.null2String(rs2.getString("lastname"));
			        	zdrName = manager.getChineseMsg(zdrName);
			        }
		        	String zdrq = Util.null2String(rs.getString("zdrq"));
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
		        	String ywst = Util.null2String(rs.getString("ywst"));
		        	if("0".equals(ywst)){
		        		ywst = "81";
		        	}else if("1".equals(ywst)){
		        		ywst = "221";
		        	}else if("2".equals(ywst)){
		        		ywst = "261";
		        	}
		        	Map<String, String> headContentMap = new HashMap<String, String>();
					headContentMap.put("oa_num", spdh);
					headContentMap.put("org_id", ywst);
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
					
					OracleResult<String, String> result = manager.updateStatus(poList,"CLAIM","pushOrderClaim");
					Pattern pattern = Pattern.compile("QX[0-9]{10}");
					Matcher isNum = pattern.matcher(result.getResponse());
					if (result == null || (!"0".equals(result.getCode()) && !isNum.matches())) {
						if(!"-4".equals(result.getCode())){
							sql1 = "update uf_spgltz set tszt = '2' where spdh = '" + spdh + "'";
							rs1.execute(sql1);
						}
						if (result == null) {
							logger.error("数据推送失败, 请联系系统管理员.");
						} else {
							logger.error(String.format("数据推送失败; {%s}; 如有疑问, 请联系系统管理员.",
									result.getMessage()));
						}
					}else{
						sql1 = "update uf_spgltz set ORASPDH = '" + result.getResponse() + "',tszt = '1' where spdh = '" + spdh + "'";
						rs1.execute(sql1);
					}
	        	}else{
	        		String sql1 = "update uf_spgltz set tszt = '2' where spdh = '" + spdh + "'";
					rs1.execute(sql1);
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
