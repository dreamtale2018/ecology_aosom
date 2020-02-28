package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 拍照需求表进摄影摄像台账<br>
 * 
 * @author ycj
 *
 */
public class AutosysxLX implements Action
{
  private Log logger = LogFactory.getLog(AutosysxLX.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	    RecordSet rs = new RecordSet();
    String requestid = request.getRequestid();
    String HH = "";					//货号
    String PM = "";					//品名
    String SQR = "";				//申请人 
    String sql = "";
 
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		HH = Util.null2String(mainTable.get("HH"));
		PM = Util.null2String(mainTable.get("CPPM"));
		SQR = Util.null2String(mainTable.get("SQR"));
		sql = "select * from uf_Sysxlx where hh = '" + HH  + "'";
		rs.execute(sql);
		if (!rs.next()){
			String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<ROOT>" +
			  "<header>" +
			    "<userid>"+SQR+"</userid>" +
			    "<modeid>13463</modeid>" +
			    "<id/>" +
			  "</header>" +
			  "<search>" +
			    "<condition/>" +
			    "<right>Y</right>" +
			  "</search>" +
			  "<data id=\"\">" +
			    "<maintable>" +
			      "<field>" +
			        "<filedname>HH</filedname>" +
			        "<filedvalue>"+HH+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
				    "<filedname>CPPM</filedname>" +
				    "<filedvalue>"+PM+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>LC</filedname>" +
			        "<filedvalue>"+requestid+"</filedvalue>" +
			      "</field>" +
			    "</maintable>" +
			  "</data>" +
			"</ROOT>";

			ModeDataServiceImpl mdsi = new ModeDataServiceImpl();
			String returncode = mdsi.saveModeData(xml);
			this.logger.info("returncode:"+returncode);
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