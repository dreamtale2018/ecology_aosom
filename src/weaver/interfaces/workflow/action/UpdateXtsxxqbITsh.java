package weaver.interfaces.workflow.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.soa.workflow.request.RequestInfo;
/**
 * IT系统实现需求单IT管理审核节点更新IT系统实现需求查询台帐<br>
 * 
 * @author ycj
 *
 */
public class UpdateXtsxxqbITsh implements Action
{
  private Log logger = LogFactory.getLog(UpdateXtsxxqbITsh.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();

    String sql = "";
    try
    {
		sql = "update uf_ITXTXQSXTZ set ZT='2' where LCLJ = '" + requestid + "'";
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