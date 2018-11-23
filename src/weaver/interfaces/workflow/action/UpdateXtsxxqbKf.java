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
 * IT系统实现需求单开发节点更新IT系统实现需求查询台帐<br>
 * 
 * @author ycj
 *
 */
public class UpdateXtsxxqbKf implements Action
{
  private Log logger = LogFactory.getLog(UpdateXtsxxqbKf.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();

    String KFY = "";	//开发员
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
    	Map<String, String> mainTable = info.getMainMap();
    	KFY = Util.null2String(mainTable.get("KFY"));
		sql = "update uf_ITXTXQSXTZ set ZT='3',KFY = '" + KFY + "' where LCLJ = '" + requestid + "'";
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