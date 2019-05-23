package weaver.interfaces.workflow.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 采购合同延期索赔流程<br>
 * 到达财务节点, 更新索赔台账状态为财务处理中
 * 
 * @author ycj
 *
 */
public class UpdateCghtyqspCw implements Action
{
  private Log logger = LogFactory.getLog(UpdateCghtyqspCw.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();

    String sql = "";
    try
    {
        sql = "update uf_SPGLTZ set ZT='1' where lc = '" + requestid + "'";
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