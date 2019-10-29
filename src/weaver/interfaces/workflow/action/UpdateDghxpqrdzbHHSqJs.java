package weaver.interfaces.workflow.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 货号申请表结束节点更新对应货号到订购会新品确认单-子表<br>
 * 
 * @author ycj
 *
 */
public class UpdateDghxpqrdzbHHSqJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateDghxpqrdzbHHSqJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String MXID = "";	//明细ID
    String XDGB = "";	//下单国别
    String HH = "";		//货号

    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		MXID = Util.null2String(mainTable.get("MXID"));
		XDGB = Util.null2String(mainTable.get("XDGB"));
		
		// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String hhDetail = Util.null2String(detailAMap.get("HH"));					//货号
				HH += hhDetail + ";";
				if(XDGB.indexOf(";")!=-1){
					String[] xdgbArr = XDGB.split(";");
					for(String xdgbDetail : xdgbArr){
						sql = "update formtable_main_296_dt1 set " + xdgbDetail + "DYHH = '" + HH + 
								"'," + xdgbDetail + "ZT = '已完成' where id = '" + MXID + "'";
						rs.execute(sql);
					}
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