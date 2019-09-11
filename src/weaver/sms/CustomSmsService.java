package weaver.sms;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;

import weaver.general.BaseBean;

import com.chinamobile.openmas.client.Sms;

/**
 * 自定义短信接口
 * 
 * <p>配合 OpenMas 实现
 * 
 * @author liberal
 * @see OpenMasClient-1.2.jar
 *
 */
public class CustomSmsService extends BaseBean implements SmsService {

	/**
	 * 
	 * @param smsId
	 * @param receiver
	 * 					接收人
	 * @param msg
	 * 					短信内容
	 * 
	 */
	@Override
	public boolean sendSMS(String smsId, String receiver, String msg) {
		String messageId = null;
		int i = 0;
		
		while (StringUtils.isBlank(messageId) && i < 5) {
			messageId = SendSms(new String[]{receiver}, msg);
			writeLog(" --------- addressId：" + getSecurityReceiver(receiver)  + " ----------- ");
			if (i > 0) writeLog(" --------- receiver i：" + i  + " ----------- ");
			i++;
		}
		
		return messageId != null;
	}
	
	/**
	 * 使用 OpenMas 实现发送短信
	 * 
	 * @param destinationAddress
	 * 								发送人地址
	 * @param message
	 * 							发送短信内容
	 * @return
	 */
	private String SendSms(String[] destinationAddresses, String message) {
		// 短信 ID, OpenMAS 上的唯一标识
		String messageId = null; 
		try {
			writeLog(" --------------- send message start --------------- ");
			Sms client = new Sms(SMS_APPLICATION_URL);
			messageId = client.SendMessage(destinationAddresses, message, 
					SMS_APPLICATION_EXTENDCODE, SMS_APPLICATION_ID, SMS_APPLICATION_PASSWORD);
			writeLog(" --------- messageId：" + messageId + " ----------- ");
			writeLog(" --------------- send message  end  --------------- ");
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			writeLog(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			writeLog(e);
		} catch (Exception e) {
			writeLog(e);
		}
		
		return messageId;
	}
	
	
	/**
	 * 对接收人信息做安全处理
	 * 
	 * @param receiver
	 * 					接收人信息
	 * @return 处理后的接收人信息
	 */
	private String getSecurityReceiver(String receiver) {
		if (StringUtils.isBlank(receiver)
				|| receiver.length() < 4) return receiver;
		String security = null;
		try {
			String suffix = "xxxx";
			security = receiver.substring(0, receiver.length() - 4) + suffix;
		} catch (Exception e) {
			writeLog("getSecurityReceiver Failure: ", e);
		}
		return security;
	}
	
	
	/**
	 * OpenMas 应用提供的 WebService 地址
	 */
	//private static final String SMS_APPLICATION_URL = "http://111.1.3.184:9080/OpenMasService?wsdl";
	private static final String SMS_APPLICATION_URL = "http://api.eyun.openmas.net/yunmas_api/SendMessageServlet";
	
	/**
	 * OpenMas 应用ID
	 */
	//private static final String SMS_APPLICATION_ID = "OA";
	private static final String SMS_APPLICATION_ID = "qyo6h3RTkbTHD0gpZ319qQoZdHC2eSLwM8H";
	
	/**
	 * OpenMas 应用密码
	 */
	//private static final String SMS_APPLICATION_PASSWORD = "hxerpmas";
	private static final String SMS_APPLICATION_PASSWORD = "QvXuKtr0hgPWa2S";
	
	/**
	 * 自定义扩展代码 (发送短信时, 标题为发送人地址 + 自定义扩展代码)
	 */
	private static final String SMS_APPLICATION_EXTENDCODE = "3";

}
