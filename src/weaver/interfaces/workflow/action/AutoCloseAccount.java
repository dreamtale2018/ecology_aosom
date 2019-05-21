package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 工作交接单流程结束自动关闭OA账号<br>
 * 
 * @author ycj
 *
 */
public class AutoCloseAccount implements Action
{
  private Log logger = LogFactory.getLog(AutoCloseAccount.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();

    String JJR = "";	//交接人
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
    	Map<String, String> mainTable = info.getMainMap();
    	JJR = Util.null2String(mainTable.get("JJR"));
		sql = "update hrmresource set STATUS='5',LOGINID='' where ID = '" + JJR + "'";
		rs.execute(sql);
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