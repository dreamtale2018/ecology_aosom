package weaver.interfaces.workflow.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdateXpkfjdHhsqQr implements Action
{
  private Log logger = LogFactory.getLog(UpdateXpkfjdHhsqQr.class);
  
  private OracleManager oracleManager = new OracleManager();
  
  @Override
  public String execute(RequestInfo request)
  {
	String requestid = request.getRequestid();
    
    String SQR = "";		//申请人
    String HHSQSJ = "";		//货号申请时间
    String CPXXBLC = "";	//产品选型表流程
    String HHSQLCBH = "";	//货号申请流程编号
    String KFY = "";		//开发员
    String TP = "";			//图片
    String PM = "";			//品名
    
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		HHSQSJ = Util.null2String(mainTable.get("SQRQ"));
		CPXXBLC = Util.null2String(mainTable.get("XXLCLJ"));
		HHSQLCBH = Util.null2String(mainTable.get("LCBH"));
		KFY = oracleManager.getRymc(SQR);
		TP = Util.null2String(mainTable.get("CPTP"));
		PM = Util.null2String(mainTable.get("ZWPM"));
    	// 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxidDetailA = Util.null2String(detailAMap.get("id"));//明细ID
				String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"<ROOT>" +
					"<header>" +
						"<userid>"+SQR+"</userid>" +
						"<modeid>623</modeid>" +
						"<id/>" +
					"</header>" +
					"<search>" +
					"<condition/>" +
					"<right>Y</right>" +
					"</search>" +
					"<data id=\"\">" +
						"<maintable>" +
							"<field>" +
								"<filedname>HHSQLCBH</filedname>" +
								"<filedvalue>"+HHSQLCBH+"</filedvalue>" +
							"</field>" +
							"<field>" +
								"<filedname>KFY</filedname>" +
								"<filedvalue>"+KFY+"</filedvalue>" +
							"</field>" +
							"<field>" +
								"<filedname>TP</filedname>" +
								"<filedvalue>"+TP+"</filedvalue>" +
							"</field>" +
							"<field>" +
								"<filedname>PM</filedname>" +
								"<filedvalue>"+PM+"</filedvalue>" +
							"</field>" +
							"<field>" +
								"<filedname>HHSQSJ</filedname>" +
								"<filedvalue>"+HHSQSJ+"</filedvalue>" +
							"</field>" +
							"<field>" +
								"<filedname>CPXXBLC</filedname>" +
								"<filedvalue>"+CPXXBLC+"</filedvalue>" +
							"</field>" +
							"<field>" +
								"<filedname>MXID</filedname>" +
								"<filedvalue>"+mxidDetailA+"</filedvalue>" +
							"</field>" +
							"<field>" +
								"<filedname>HHSQBLC</filedname>" +
								"<filedvalue>"+requestid+"</filedvalue>" +
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
				System.out.println(returncode);
			}
		}
        //this.logger.error("sql：" + sql);
        
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