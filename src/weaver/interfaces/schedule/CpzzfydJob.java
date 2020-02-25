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
 * 同步 产品制作费用单数据到 oracle中
 * 
 * @author liberal
 *
 */
public class CpzzfydJob extends BaseCronJob {

	private static final Log logger = LogFactory.getLog(CpzzfydJob.class);
	
	
	private OracleManager manager = null;
	
	RecordSet rs = new RecordSet();
	RecordSet rs1 = new RecordSet();
	RecordSet rs2 = new RecordSet();
	@Override
	public void execute() {
		String task = "CpzzfydJob";
		try {
			logger.info("---------------- " + task + " start ----------------");
			long startTime = System.currentTimeMillis();
			if (manager == null) manager = new OracleManager();
			
			//获取费用支付台账中的相关信息。
			String sql = "select * from uf_gyskk where tszt is null or tszt = '1'";
			rs.execute(sql);
	        while (rs.next()) {
	        	String gys = Util.null2String(rs.getString("gys"));
	        	List<OracleProductOrder> poList = new ArrayList<OracleProductOrder>();
	        	OracleProductOrder po = new OracleProductOrder();
	        	String fyje = Util.null2String(rs.getString("fyje"));
	        	if(fyje.indexOf(",")!=-1){
	        		fyje = fyje.replace(",", "");
	        	}
	        	String kklc = Util.null2String(rs.getString("kklc"));
		        if(!"0".equals(fyje)){
		        	String gysId = "";
		        	String gysName = "";
		        	String sql1 = "select vendor_id,vendor_name from uf_vendor where id = '" + gys + "'";
		        	rs1.execute(sql1);
		        	if (rs1.next()) {
		        		gysId = Util.null2String(rs1.getString("vendor_id"));
		        		gysName = Util.null2String(rs1.getString("vendor_name"));
		        	}
		        	String lcbh = Util.null2String(rs.getString("lcbh"));
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


		        	Map<String, String> headContentMap = new HashMap<String, String>();
					headContentMap.put("oa_num", lcbh);
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
					detailContentMap.put("confirm_amount", fyje);
					detailContentMap.put("po_number", "");
					detailContentMap.put("buyer_code", cgyGh);
					detailContentList.add(detailContentMap);
					po.setDetailContentList(detailContentList);
					poList.add(po);
					
					OracleResult<String, String> result = manager.updateStatus(poList,"CLAIM","cpzzfydJob");
					Pattern pattern = Pattern.compile("QX[0-9]{10}");
					Matcher isNum = pattern.matcher(result.getResponse());
					if (result == null || (!"0".equals(result.getCode()) && !isNum.matches())) {
						if(!"-4".equals(result.getCode())){
							sql1 = "update uf_gyskk set tszt = '2' where kklc = '" + kklc + "'";
							rs1.execute(sql1);
						}
						if (result == null) {
							logger.error("数据推送失败, 请联系系统管理员.");
						} else {
							logger.error(String.format("数据推送失败; {%s}; 如有疑问, 请联系系统管理员.",
									result.getMessage()));
						}
					}else{
						sql1 = "update uf_gyskk set ORASPDH = '" + result.getResponse() + "',tszt = '0' where kklc = '" + kklc + "'";
						rs1.execute(sql1);
					}
	        	}else{
	        		String sql1 = "update uf_gyskk set tszt = '2' where kklc = '" + kklc + "'";
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
}
