package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 设计需求表结束节点更新设计任务列表台账的完成日期和状态<br>
 * 
 * @author ycj
 *
 */
public class UpdateSjxqbJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateSjxqbJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();

    String WCRQ = "";//完成日期
    String sql = "";
    try
    {
		WCRQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		sql = "update uf_SJRWLB set ZT='1',WCRQ='"+ WCRQ +"' where lc = '"+ requestid +"'";
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