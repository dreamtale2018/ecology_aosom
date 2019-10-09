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
 * 产品选型表流程结束节点更新订购会新品台账状态<br>
 * 
 * @author ycj
 *
 */
public class UpdateDghxptzCpxxJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateDghxptzCpxxJs.class);
  
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
				String[] bsArr = bsDetailA.split(";");
				for(int j=0;j<bsArr.length;j++){
					String bsStr = bsArr[j];
					sql = "update formtable_main_286 set ZT='2' where bs = '"+ bsStr +"'";
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