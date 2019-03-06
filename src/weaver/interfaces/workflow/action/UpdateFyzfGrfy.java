package weaver.interfaces.workflow.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 个人费用报销单结束节点新建费用支付台账<br>
 * 
 * @author ycj
 *
 */
public class UpdateFyzfGrfy implements Action
{
  private Log logger = LogFactory.getLog(UpdateFyzfGrfy.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    
    String requestid = request.getRequestid();
    String DJM = "个人费用报销单";					//单据名 
    String BXDH = "";							//流程号 
    String FYXDL = "";							//费用项-大类 
    String RQ = "";								//日期 
    String SQR = "";							//申请人
    String YJZZ = "";							//一级组织
    String FKZT = "遨森电子商务股份有限公司";			//付款主体
    String FKBZ = "CNY";						//付款币种

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		BXDH = Util.null2String(mainTable.get("BXDH"));
		RQ = Util.null2String(mainTable.get("BXRQ"));
		SQR = Util.null2String(mainTable.get("SQR"));
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxidDetailA = Util.null2String(detailAMap.get("id"));			//明细ID
				String jeDetailA = Util.null2String(detailAMap.get("BXJE"));			//金额
				String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"<ROOT>" +
				  "<header>" +
				    "<userid>"+SQR+"</userid>" +
				    "<modeid>663</modeid>" +
				    "<id/>" +
				  "</header>" +
				  "<search>" +
				    "<condition/>" +
				    "<right>Y</right>" +
				  "</search>" +
				  "<data id=\"\">" +
				    "<maintable>" +
				      "<field>" +
				        "<filedname>DJM</filedname>" +
				        "<filedvalue>"+DJM+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
					    "<filedname>LCLJ</filedname>" +
					    "<filedvalue>"+requestid+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
				        "<filedname>BXDH</filedname>" +
				        "<filedvalue>"+BXDH+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
				        "<filedname>RQ</filedname>" +
				        "<filedvalue>"+RQ+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
				        "<filedname>SQR</filedname>" +
				        "<filedvalue>"+SQR+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
				        "<filedname>YJZZ</filedname>" +
				        "<filedvalue>"+YJZZ+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
				        "<filedname>FKZT</filedname>" +
				        "<filedvalue>"+FKZT+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
					    "<filedname>FKBZ</filedname>" +
					    "<filedvalue>"+FKBZ+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
					    "<filedname>SKDW</filedname>" +
					    "<filedvalue>"+SQR+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
					    "<filedname>MXID</filedname>" +
					    "<filedvalue>"+mxidDetailA+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
					    "<filedname>ZT</filedname>" +
					    "<filedvalue>0</filedvalue>" +
				      "</field>" +
				    "</maintable>" +
				  "</data>" +
				"</ROOT>";

				xml = xml.replaceAll("&quot;","\"");
				xml = xml.replaceAll("'","''");
				xml = xml.replaceAll("&nbsp;", " ");
				xml = xml.replaceAll("\r", " ");
				xml = xml.replaceAll("\n", " ");
				xml = xml.replaceAll("<br>", ";");
				ModeDataServiceImpl mdsi = new ModeDataServiceImpl();
				String returncode = mdsi.saveModeData(xml);
				this.logger.info(returncode);
			}
		}
    }
    catch (Exception e)
    {
      this.logger.error("Exception e", e);
      request.getRequestManager().setMessageid("1111111113");
      request.getRequestManager().setMessagecontent(e + ",请联系管理员.");
      return Action.FAILURE_AND_CONTINUE;
    }
    return Action.SUCCESS;
      
  }
}