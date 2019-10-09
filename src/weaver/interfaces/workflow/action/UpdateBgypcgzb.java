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
 * 硬件/耗材/办公用品采购单-子表流程<br>
 * 到达结束节点, 更新硬件/耗材/办公用品采购单实际到货数量和备注
 * 子流程结束后，数据增加至对应物料编号的 当前库存数量
 *
 * @author ycj
 *
 */
public class UpdateBgypcgzb implements Action
{
  private Log logger = LogFactory.getLog(UpdateBgypcgzb.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String SQR = "";		//申请人 
    String LB = "";			//类别
    String WPSFRTZ = "";	//物品是否入台账
    
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
        // 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		LB = Util.null2String(mainTable.get("LB"));
		WPSFRTZ = Util.null2String(mainTable.get("WPSFRTZ"));
    	
		//判断物品是否入台账
		if("0".equals(WPSFRTZ)){
	        // 获取明细表1信息
		    List<Map<String, String>> detailAList = info.getDetailMap("1");
		    if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					String wlbmDetailA = Util.null2String(detailAMap.get("WLBM"));
					String sjdhslDetailA = Util.null2String(detailAMap.get("SJDHSL"));
					String mxidDetailA = Util.null2String(detailAMap.get("MXHID"));
					String bzDetailA = Util.null2String(detailAMap.get("BZ"));
					//更新主表中实际到货数量和备注
			        sql = "update formtable_main_288_dt1 set SJDHSL='" + sjdhslDetailA + "',BZ='" + bzDetailA + 
	        				"' where id = '" + mxidDetailA + "'";
			        rs.execute(sql);
			        sql = "select dqkcsl from uf_kctz where wlbm = '" + wlbmDetailA + "'";
			        rs.execute(sql);
			        if(rs.next()){
			        	String dqkcsl = rs.getString("dqkcsl");
			        	if(dqkcsl!=null && !"".equals(dqkcsl)){
			        		int sjkc = Integer.parseInt(dqkcsl) + Integer.parseInt(sjdhslDetailA);
			        		sql = "update uf_kctz set dqkcsl = '" + sjkc + "' where wlbm = '" + wlbmDetailA + "'";
			                rs.execute(sql);
			        	}
			        }else{
			        	String pmDetailA = Util.null2String(detailAMap.get("PM"));
						String xhDetailA = Util.null2String(detailAMap.get("XH"));
						String dwDetailA = Util.null2String(detailAMap.get("DW"));
						String lx = "";
						sql = "select lx from uf_wlwhb where wlbm = '" + wlbmDetailA + "'";
						rs.execute(sql);
						if (rs.next()){
							lx = rs.getString("lx");
				        }
						String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
						"<ROOT>" +
						  "<header>" +
						    "<userid>"+SQR+"</userid>" +
						    "<modeid>944</modeid>" +
						    "<id/>" +
						  "</header>" +
						  "<search>" +
						    "<condition/>" +
						    "<right>Y</right>" +
						  "</search>" +
						  "<data id=\"\">" +
						    "<maintable>" +
						      "<field>" +
						        "<filedname>LB</filedname>" +
						        "<filedvalue>"+LB+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
							    "<filedname>WLBM</filedname>" +
							    "<filedvalue>"+wlbmDetailA+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
						        "<filedname>DQKCSL</filedname>" +
						        "<filedvalue>"+sjdhslDetailA+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
							    "<filedname>PM</filedname>" +
							    "<filedvalue>"+pmDetailA+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
						        "<filedname>XH</filedname>" +
						        "<filedvalue>"+xhDetailA+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
						        "<filedname>DW</filedname>" +
						        "<filedvalue>"+dwDetailA+"</filedvalue>" +
						      "</field>" +
						      "<field>" +
						        "<filedname>LX</filedname>" +
						        "<filedvalue>"+lx+"</filedvalue>" +
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
						xml = xml.replaceAll("&", "&amp;");
						ModeDataServiceImpl mdsi = new ModeDataServiceImpl();
						String returncode = mdsi.saveModeData(xml);
						this.logger.info("returncode:"+returncode);
			        }
				}
		    }
	        //this.logger.error("sql：" + sql);
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