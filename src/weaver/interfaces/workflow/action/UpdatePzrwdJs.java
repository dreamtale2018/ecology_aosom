package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.soa.workflow.request.RequestInfo;

public class UpdatePzrwdJs implements Action
{
  private Log logger = LogFactory.getLog(UpdatePzrwdJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();

    String sql = "";
    try
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String pzwcrq = sdf.format(date);
        sql = "update formtable_main_159 set PZZT='2',PZWCRQ='"+ pzwcrq +"' where lc = '"+ requestid +"'";
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