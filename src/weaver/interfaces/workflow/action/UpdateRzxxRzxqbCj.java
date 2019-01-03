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
 * 认证需求表创建节点新建认证信息台账<br>
 * 
 * @author ycj
 *
 */
public class UpdateRzxxRzxqbCj implements Action
{
  private Log logger = LogFactory.getLog(UpdateRzxxRzxqbCj.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    
    String requestid = request.getRequestid();
    String KFY = "";	//开发员 

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		KFY = Util.null2String(mainTable.get("KFY"));
		
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxidDetailA = Util.null2String(detailAMap.get("id"));			//明细ID
				String hhDetailA = Util.null2String(detailAMap.get("HH"));				//货号
				String pmDetailA = Util.null2String(detailAMap.get("PM"));				//品名
				String gcxhDetailA = Util.null2String(detailAMap.get("GCXH"));			//工厂型号
				String gbDetailA = Util.null2String(detailAMap.get("GB"));				//国别
				String csjrzyqDetailA = Util.null2String(detailAMap.get("CSJRZYQ"));	//测试及认证要求
				String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"<ROOT>" +
				  "<header>" +
				    "<userid>"+KFY+"</userid>" +
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
				        "<filedname>KFY</filedname>" +
				        "<filedvalue>"+KFY+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
					      "<filedname>RZXQBLC</filedname>" +
					      "<filedvalue>"+requestid+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
				        "<filedname>HH</filedname>" +
				        "<filedvalue>"+hhDetailA+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
				        "<filedname>PM</filedname>" +
				        "<filedvalue>"+pmDetailA+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
				        "<filedname>GCXH</filedname>" +
				        "<filedvalue>"+gcxhDetailA+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
				        "<filedname>GB</filedname>" +
				        "<filedvalue>"+gbDetailA+"</filedvalue>" +
				      "</field>" +
				      "<field>" +
				        "<filedname>CSJRZYQ</filedname>" +
				        "<filedvalue>"+csjrzyqDetailA+"</filedvalue>" +
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
		        //this.logger.error("sql：" + sql);
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