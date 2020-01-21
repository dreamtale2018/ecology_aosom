package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 资金账户管理流程结束自动录入台账信息<br>
 * 
 * @author jq
 *
 */
public class AutoCreateZjzhglAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateZjzhglAction.class);
  
  private OracleManager oracleManager = new OracleManager();
  
  @Override
  public String execute(RequestInfo request)
  {
	String requestid = request.getRequestid();

	RecordSet rs = new RecordSet();
	  
    String SQR = "";		//申请人
    String SQRQ = "";		//申请日期
    String ZH = "";			//账号
    String YWST = "";		//业务实体
    String SQLX = "";		//申请类型
    String ZHLX = "";		//账户类型
    String BZ = "";		    //币种
    String KHSJ = "";		//开户时间
    String LXFS = "";		//联系方式 
    String JGMC = "";		//机构名称

    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
        // 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		SQRQ = Util.null2String(mainTable.get("SQRQ"));
		SQLX = Util.null2String(mainTable.get("SQLX"));	
		ZH = Util.null2String(mainTable.get("ZH"));	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SQRQ = sdf.format(new Date());
		if("0".equals(SQLX)){
			SQRQ += "开户";
		}else if("1".equals(SQLX)){
			SQRQ += "销户";
		}
		YWST = Util.null2String(mainTable.get("YWST"));
		ZHLX = Util.null2String(mainTable.get("ZHLX"));
		BZ = Util.null2String(mainTable.get("BZ"));
		KHSJ = Util.null2String(mainTable.get("KHSJ"));
		LXFS = Util.null2String(mainTable.get("LXFS"));
		JGMC = Util.null2String(mainTable.get("JGMC"));
					
			        sql = "select * from uf_Hnwzjzhgl where ZH = '" + ZH + "'";
			        rs.execute(sql);
			        if(rs.next()){
			        		sql = "update uf_Hnwzjzhgl set zt = '" + SQLX + "',rq = '" + SQRQ + "' where zh = '" + ZH + "'";
			                rs.execute(sql);
			        	
			        }else{
						String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
						"<ROOT>" +
						  "<header>" +
						    "<userid>"+SQR+"</userid>" +
						    "<modeid>10963</modeid>" +
						    "<id/>" +
						  "</header>" +
						  "<search>" +
						    "<condition/>" +
						    "<right>Y</right>" +
						  "</search>" +
						  "<data id=\"\">" +
						    "<maintable>" +
						      "<field>" +
						        "<filedname>YWST</filedname>" +
						        "<filedvalue>"+YWST+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
							    "<filedname>ZHLX</filedname>" +
							    "<filedvalue>"+ZHLX+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
						        "<filedname>JGMC</filedname>" +
						        "<filedvalue>"+JGMC+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
							    "<filedname>ZH</filedname>" +
							    "<filedvalue>"+ZH+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
						        "<filedname>BZ</filedname>" +
						        "<filedvalue>"+BZ+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
						        "<filedname>KHSJ</filedname>" +
						        "<filedvalue>"+KHSJ+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
						        "<filedname>LXFS</filedname>" +
						        "<filedvalue>"+LXFS+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
							      "<filedname>RQ</filedname>" +
							      "<filedvalue>"+SQRQ+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
							      "<filedname>ZT</filedname>" +
							      "<filedvalue>"+SQLX+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
							      "<filedname>LC</filedname>" +
							      "<filedvalue>"+requestid+"</filedvalue>" +
						      "</field>" +
						    "</maintable>" +
						  "</data>" +
						"</ROOT>";
	
						
						xml = oracleManager.transXml(xml);
						ModeDataServiceImpl mdsi = new ModeDataServiceImpl();
						String returncode = mdsi.saveModeData(xml);
						this.logger.info("returncode:"+returncode);
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