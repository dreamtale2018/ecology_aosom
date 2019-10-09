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
 * 货号申请表流程结束节点更新订购会新品台账状态和对应货号<br>
 * 
 * @author ycj
 *
 */
public class UpdateDghxptzHHSqJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateDghxptzHHSqJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String MXID = "";//明细ID
    
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		MXID = Util.null2String(mainTable.get("MXID"));
		
		// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String bsDetailA = Util.null2String(detailAMap.get("BS"));					//标识
				sql = "update formtable_main_286 set ZT='2' where bs = '"+ bsDetailA +"'";
				rs.execute(sql);
				//更新订购会新品台账对应货号
				String hhDetail = Util.null2String(detailAMap.get("HH"));					//货号
				sql = "select id from uf_product where segment1 = '" + hhDetail + "'";
				rs.execute(sql);
				if (rs.next()){
					hhDetail = rs.getString("id");
		        }
				int gb = 0;
				String xdgbDetail = Util.null2String(detailAMap.get("XDGB"));				//下单国别
				String[] gbArr = {"US","CA","UK","DE","FR","IT","ES"};
				for(int j=0; j<gbArr.length; j++){
					if(gbArr[j].equals(xdgbDetail)){
						gb = j;
					}
				}
		    	sql = "update formtable_main_286 set DYHH = '" + hhDetail +
		    			"' where mxid = '" + MXID + "' and gb = '" + gb + "'";
		    	rs.execute(sql);
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