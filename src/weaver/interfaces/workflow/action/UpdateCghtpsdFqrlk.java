package weaver.interfaces.workflow.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdateCghtpsdFqrlk implements Action
{
  private Log logger = LogFactory.getLog(UpdateCghtpsdFqrlk.class);
  
  @Override
  public String execute(RequestInfo request)
  {
//    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();

    String FQR = "";//发起人
    String YJZZ = "";//一级组织
    String EJZZ = "";//二级组织
    String CGHTH = "";//采购合同号
    String GYS = "";//供应商
    String PSRQ = "";//发起日期
    
//    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		FQR = Util.null2String(mainTable.get("FQR"));
		CGHTH = Util.null2String(mainTable.get("CGHTH"));
		GYS = Util.null2String(mainTable.get("GYS"));
		PSRQ = Util.null2String(mainTable.get("PSRQ"));
		EJZZ = Util.null2String(mainTable.get("EJZZ"));
		
    	// 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxidDetailA = Util.null2String(detailAMap.get("id"));//明细ID
				String psxmDetailA = Util.null2String(detailAMap.get("PSXM"));//评审项目
				String hhDetailA = Util.null2String(detailAMap.get("HH"));//货号
				String cppmDetailA = Util.null2String(detailAMap.get("CPPM"));//品名
				String thlDetailA = Util.null2String(detailAMap.get("THL"));//退货率
				String zrrDetailA = Util.null2String(detailAMap.get("ZRR"));//责任人
				String wtlyDetailA = Util.null2String(detailAMap.get("WTLY"));//问题来源
				String wtlxDetailA = Util.null2String(detailAMap.get("WTLX"));//问题类型
				if("5".equals(psxmDetailA)){
//					sql = "insert into uf_CPZLGSGZB (SQR,HTH,GYS,SQRQ,HH,PM,THL,ZRR,CG,WTLY,WTLX,LC,GJZT) values " + 
//					"('" + FQR + "','" + CGHTH + "','" + GYS + "','" + PSRQ + "','" + hhDetailA + "','" + cppmDetailA + "','"+ thlDetailA + 
//					"','" + zrrDetailA + "','" + FQR + "','" + wtlyDetailA + "','" + wtlxDetailA + "','" + requestid + "','0')";
//					rs.execute(sql);
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
									"<filedname>HTH</filedname>" +
									"<filedvalue>"+CGHTH+"</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>GYS</filedname>" +
									"<filedvalue>"+GYS+"</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>SQRQ</filedname>" +
									"<filedvalue>"+PSRQ+"</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>HH</filedname>" +
									"<filedvalue>"+hhDetailA+"</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>PM</filedname>" +
									"<filedvalue>"+cppmDetailA+"</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>THL</filedname>" +
									"<filedvalue>"+thlDetailA+"</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>ZRR</filedname>" +
									"<filedvalue>"+zrrDetailA+"</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>CG</filedname>" +
									"<filedvalue>"+FQR+"</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>WTLY</filedname>" +
									"<filedvalue>"+wtlyDetailA+"</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>WTLX</filedname>" +
									"<filedvalue>"+wtlxDetailA+"</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>LC</filedname>" +
									"<filedvalue>"+requestid+"</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>GJZT</filedname>" +
									"<filedvalue>0</filedvalue>" +
								"</field>" +
								"<field>" +
									"<filedname>MXID</filedname>" +
									"<filedvalue>"+mxidDetailA+"</filedvalue>" +
								"</field>" +
							"</maintable>" +
						"</data>" +
					"</ROOT>";
					
					ModeDataServiceImpl mdsi = new ModeDataServiceImpl();
					String returncode = mdsi.saveModeData(xml);
					System.out.println(returncode);
				}
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