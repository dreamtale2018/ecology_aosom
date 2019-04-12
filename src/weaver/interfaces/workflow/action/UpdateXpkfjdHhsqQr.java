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
/**
 * 货号申请表确认节点新建新品开发进度台帐<br>
 * 
 * @author ycj
 *
 */
public class UpdateXpkfjdHhsqQr implements Action
{
  private Log logger = LogFactory.getLog(UpdateXpkfjdHhsqQr.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	String requestid = request.getRequestid();
    
    String SQR = "";		//申请人
    String HHSQSJ = "";		//货号申请时间
    String CPXXBLC = "";	//产品选型表流程
    String HHSQLCBH = "";	//货号申请流程编号
    String TP = "";			//图片
    String PM = "";			//品名
    String TJLY = "";		//推荐来源
    String XXDH = "";		//选型单号
    String TJZLCH = "";		//推荐子流程号
    
    RecordSet rs = new RecordSet();
    
    String sql = "";
    
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		HHSQSJ = Util.null2String(mainTable.get("SQRQ"));
		CPXXBLC = Util.null2String(mainTable.get("XXLCLJ"));
		HHSQLCBH = Util.null2String(mainTable.get("LCBH"));
		TP = Util.null2String(mainTable.get("CPTP"));
		PM = Util.null2String(mainTable.get("ZWPM"));
		XXDH = Util.null2String(mainTable.get("XXDH"));
		TJZLCH = Util.null2String(mainTable.get("TJZLCH"));
		sql = "select TJLL from formtable_main_103 where requestid = '" + CPXXBLC + "'";
		rs.execute(sql);
		if(rs.next()){
			TJLY = rs.getString("TJLL");
		}
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
								"<filedname>KF</filedname>" +
								"<filedvalue>"+SQR+"</filedvalue>" +
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
								"<filedname>TJLY</filedname>" +
								"<filedvalue>"+TJLY+"</filedvalue>" +
							"</field>" +
							"<field>" +
								"<filedname>XXDH</filedname>" +
								"<filedvalue>"+XXDH+"</filedvalue>" +
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
								"<filedname>TJZLCH</filedname>" +
								"<filedvalue>"+TJZLCH+"</filedvalue>" +
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