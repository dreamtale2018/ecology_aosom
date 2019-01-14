package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdateCptsbzbZrr implements Action
{
  private Log logger = LogFactory.getLog(UpdateCptsbzbZrr.class);
  
  @Override
  public String execute(RequestInfo request)
  {
//    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();
    String FQR = "";//发起人
    String YJZZ = "";//一级组织
    String EJZZ = "";//二级组织
    String FQRQ = "";//发起日期
    String LYLX = "";//来源类型
    String WTLX = "";//问题类型
    String GB = "";//国别
    String HH = "";//货号
    String PM = "";//品名
    String CPH = "";//产品号
    String THL = "";//退货率
    String HTH = "";//合同号
    String GYS = "";//供应商
    String WT = "";//问题
    String ZRR = "";//责任人
    String CG = "";//采购

//    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		FQR = Util.null2String(mainTable.get("FQR"));
		FQRQ = Util.null2String(mainTable.get("FQRQ"));
		LYLX = Util.null2String(mainTable.get("LYLX"));
		//由于投诉子表来源类型是下拉框需要转换。
		if("0".equals(LYLX)){
			LYLX = "1";
		}else if("1".equals(LYLX)){
			LYLX = "2";
		}else if("2".equals(LYLX)){
			LYLX = "3";
		}
		WTLX = Util.null2String(mainTable.get("WTLX"));
		GB = Util.null2String(mainTable.get("GB"));
		HH = Util.null2String(mainTable.get("HH"));
		PM = Util.null2String(mainTable.get("PM"));
		CPH = Util.null2String(mainTable.get("CPH"));
		THL = Util.null2String(mainTable.get("THL"));
		HTH = Util.null2String(mainTable.get("HTH"));
		GYS = Util.null2String(mainTable.get("GYS"));
		WT = Util.null2String(mainTable.get("WT"));
		ZRR = Util.null2String(mainTable.get("ZRR"));
		CG = Util.null2String(mainTable.get("DYCG"));
		EJZZ = Util.null2String(mainTable.get("EJZZ"));
		if("1".equals(WTLX) || "2".equals(WTLX)){
//			sql = "insert into uf_CPZLGSGZB (SQR,SQRQ,WTLY,WTLX,GB,HH,PM,CPH,THL,HTH,GYS,WTMS,ZRR,CG,LC,GJZT) values " + 
//			"('" + FQR + "','" + FQRQ + "','" + LYLX + "','" + WTLX + "','" + GB + "','" + HH + "','" + PM + "','" + CPH + 
//			"','"+ THL + "','" + HTH + "','" + GYS + "','" + WT + "','" + ZRR + "','" + CG + "','" + requestid + "','0')";
//			rs.execute(sql);
			RecordSet rs = new RecordSet();
			String sql = " select * from hrmdepartment  where id = (select SUPDEPID from hrmdepartment where id = '"+EJZZ+"' )";

			rs.executeSql(sql);

			if(rs.next()){
				YJZZ = rs.getString("id");
				
			}else{		
				rs.executeSql("select * from hrmdepartment where id="+EJZZ);
				if(rs.next()){
					YJZZ = rs.getString("id");
				}
				
			}
			String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<ROOT>" +
			  "<header>" +
			    "<userid>"+FQR+"</userid>" +
			    "<modeid>386</modeid>" +
			    "<id/>" +
			  "</header>" +
			  "<search>" +
			    "<condition/>" +
			    "<right>Y</right>" +
			  "</search>" +
			  "<data id=\"\">" +
			    "<maintable>" +
			      "<field>" +
			        "<filedname>SQR</filedname>" +
			        "<filedvalue>"+FQR+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
				    "<filedname>YJZZ</filedname>" +
				    "<filedvalue>"+YJZZ+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
				    "<filedname>EJZZ</filedname>" +
				    "<filedvalue>"+EJZZ+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>SQRQ</filedname>" +
			        "<filedvalue>"+FQRQ+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>WTLY</filedname>" +
			        "<filedvalue>"+LYLX+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>WTLX</filedname>" +
			        "<filedvalue>"+WTLX+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>GB</filedname>" +
			        "<filedvalue>"+GB+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>HH</filedname>" +
			        "<filedvalue>"+HH+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>PM</filedname>" +
			        "<filedvalue>"+PM+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>CPH</filedname>" +
			        "<filedvalue>"+CPH+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>THL</filedname>" +
			        "<filedvalue>"+THL+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>HTH</filedname>" +
			        "<filedvalue>"+HTH+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>GYS</filedname>" +
			        "<filedvalue>"+GYS+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>WTMS</filedname>" +
			        "<filedvalue>"+WT+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>ZRR</filedname>" +
			        "<filedvalue>"+ZRR+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>CG</filedname>" +
			        "<filedvalue>"+CG+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>LC</filedname>" +
			        "<filedvalue>"+requestid+"</filedvalue>" +
			      "</field>" +
			      "<field>" +
			        "<filedname>GJZT</filedname>" +
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