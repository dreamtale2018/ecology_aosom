package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 活动需求-策划单流程结束节点更新官网活动与评估台账<br>
 * 
 * @author ycj
 *
 */
public class UpdateGwtzHdxqJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateGwtzHdxqJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();
    String ZT = "";//状态
    String JSRQ = "";//结束日期
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
		
    	// 获取主表信息
    	Map<String, String> mainTable = info.getMainMap();
    	ZT = Util.null2String(mainTable.get("ZT"));
    	JSRQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    	
    	sql = "update uf_GWHDXP set ZT = '" + ZT + "',LCJSRQ = '"+ JSRQ + 
    			"' where LC = '" + requestid + "'";
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