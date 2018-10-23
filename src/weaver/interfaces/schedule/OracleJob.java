package weaver.interfaces.schedule;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;

import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;

/**
 * 同步 Oracle ERP 基础数据到 OA 中间表中
 * 目前每四个小时执行一次：0 0 0/4 * * ?
 * 
 * @author liberal
 *
 */
public class OracleJob extends BaseCronJob {

	private static final Log logger = LogFactory.getLog(OracleJob.class);
	
	
	private OracleManager manager = null;;
	
	
	@Override
	public void execute() {
		String task = "OracleJob";
		try {
			logger.info("---------------- " + task + " start ----------------");
			long startTime = System.currentTimeMillis();
			if (manager == null) manager = new OracleManager();
			
			// 设置时间为当前时间前的 4 个小时
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(System.currentTimeMillis()));
			calendar.add(Calendar.HOUR_OF_DAY, -4);
			
			// 如果同步失败, 继续同步, 连续三次同步失败不再进行同步
			for (int i = 0; i < 3; i++) {
				boolean flag = manager.syncQuery(calendar);
				if (flag) {
					logger.info("Successfully");
					break;
				} else {
					logger.info((i + 1) + " Failure");
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
