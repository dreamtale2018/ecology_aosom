package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.soa.workflow.request.RequestInfo;
/**
 * IT系统实现需求单结束节点更新IT系统实现需求查询台帐<br>
 * 
 * @author ycj
 *
 */
public class UpdateXtsxxqbJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateXtsxxqbJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();

    String WCRQ = "";	//完成日期
    String sql = "";
    try
    {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		WCRQ = sdf.format(new Date());
		sql = "update uf_ITXTXQSXTZ set ZT='5',WCRQ = '"+ WCRQ + "'  where LCLJ = '" + requestid + "'";
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