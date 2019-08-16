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
 * 活动需求-策划单流程更新官网活动与评估台账<br>
 * 
 * @author ycj
 *
 */
public class UpdateGwtzHdxq implements Action
{
  private Log logger = LogFactory.getLog(UpdateGwtzHdxq.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();
    String ZT = "";//状态
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
		
    	// 获取主表信息
    	Map<String, String> mainTable = info.getMainMap();
    	ZT = Util.null2String(mainTable.get("ZT"));
    	
    	sql = "update uf_GWHDXP set ZT = '" + ZT + "' where LC = '" + requestid + "'";
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