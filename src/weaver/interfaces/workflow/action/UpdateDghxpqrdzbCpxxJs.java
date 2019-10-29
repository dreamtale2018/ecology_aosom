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
 * 产品选型表结束节点更新状态到订购会新品确认单-子表<br>
 * 
 * @author ycj
 *
 */
public class UpdateDghxpqrdzbCpxxJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateDghxpqrdzbCpxxJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String bsDetailA = Util.null2String(detailAMap.get("BS"));					//标识
				String idDetailA = "";														//明细ID
				String gbDetailA = "";	
				String[] bsArr = bsDetailA.split(";");
				for(int j=0;j<bsArr.length;j++){
					String bsStr = bsArr[j];
					if(bsStr.indexOf("-")!=-1){
						idDetailA = bsStr.split("-")[0];
						gbDetailA = bsStr.split("-")[1];
					}
					sql = "update formtable_main_296_dt1 set " + gbDetailA + "ZT = '已完成' where id = '" + idDetailA + "'";
					rs.execute(sql);
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