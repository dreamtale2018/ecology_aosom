package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * 硬件领用流程结束自动录入台账信息<br>
 * 
 * @author jq
 *
 */
public class AutoCreateYjlyAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateYjlyAction.class);
  
  private OracleManager oracleManager = new OracleManager();
  
  @Override
  public String execute(RequestInfo request)
  {
	String requestid = request.getRequestid();

	RecordSet rs = new RecordSet();
	  
	String LB = "";			//类别
    String SQR = "";		//申请人
    String SQRQ = "";		//申请日期

    String sql = "";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SQRQ = sdf.format(new Date());
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
        // 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		LB = Util.null2String(mainTable.get("LB"));
		SQR = Util.null2String(mainTable.get("SQR"));

		
		// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxidDetailA = Util.null2String(detailAMap.get("id"));			//明细ID
				String wlbmDetailA = Util.null2String(detailAMap.get("WLBM"));			//物料编码
				String pmDetailA = Util.null2String(detailAMap.get("PM"));				//品名
				String ggDetailA = Util.null2String(detailAMap.get("GG"));			    //规格
				String zcbmDetailA = Util.null2String(detailAMap.get("ZCBM"));	        //资产编码
				String zcxlDetailA = Util.null2String(detailAMap.get("ZCXL"));	        //资产小类
				String syrDetailA = Util.null2String(detailAMap.get("SYR"));			//使用人
				String bmDetailA = Util.null2String(detailAMap.get("BM"));			    //部门
				String lyrqDetailA = Util.null2String(detailAMap.get("LYRQ"));			//领用日期
				String ipDetailA = Util.null2String(detailAMap.get("IP"));				//IP
				String macDetailA = Util.null2String(detailAMap.get("MAC"));			//MAC
				String ztDetailA = Util.null2String(detailAMap.get("ZT"));		 		//状态
				String zcbmzjDetailA = Util.null2String(detailAMap.get("ZCBMZJ"));		//资产编码主机
				String zcbmxsqDetailA = Util.null2String(detailAMap.get("ZCBMXSQ"));	//资产编码显示器
				
				List<String> ZCBMList = new ArrayList<String>();
				ZCBMList.add(zcbmzjDetailA);
				ZCBMList.add(zcbmxsqDetailA);
				 	for(int j=0;j<ZCBMList.size();j++){
				if(!"".equals(ZCBMList.get(j))){
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
					        "<filedname>WLBM</filedname>" +
					        "<filedvalue>"+wlbmDetailA+"</filedvalue>" +
					      "</field>" +
					      "<field>" +
						    "<filedname>PM</filedname>" +
						    "<filedvalue>"+pmDetailA+"</filedvalue>" +
					      "</field>" +
					      "<field>" +
					        "<filedname>GG</filedname>" +
					        "<filedvalue>"+ggDetailA+"</filedvalue>" +
					      "</field>" +
					      "<field>" +
						    "<filedname>ZCXL</filedname>" +
						    "<filedvalue>"+ZCBMList.get(i)+"</filedvalue>" +
					      "</field>" +
					      "<field>" +
					        "<filedname>ZCBM</filedname>" +
					        "<filedvalue>"+zcbmDetailA+"</filedvalue>" +
					      "</field>" +
					      "<field>" +
					        "<filedname>SYR</filedname>" +
					        "<filedvalue>"+syrDetailA+"</filedvalue>" +
					      "</field>" +
					      "<field>" +
					        "<filedname>BM</filedname>" +
					        "<filedvalue>"+bmDetailA+"</filedvalue>" +
					      "</field>" +
					      "<field>" +
					      "<filedname>LYRQ</filedname>" +
					      "<filedvalue>"+SQRQ+"</filedvalue>" +
					      "</field>" +
					      "<field>" +
					      "<filedname>IP</filedname>" +
					      "<filedvalue>"+ipDetailA+"</filedvalue>" +
					      "</field>" +
					      "<field>" +
					      "<filedname>MAC</filedname>" +
					      "<filedvalue>"+macDetailA+"</filedvalue>" +
					      "</field>" +
					      "<field>" +
					      "<filedname>ZT</filedname>" +
					      "<filedvalue>"+ztDetailA+"</filedvalue>" +
					      "</field>" +
					      "<field>" +
					      "<filedname>MXID</filedname>" +
					      "<filedvalue>"+mxidDetailA+"</filedvalue>" +
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