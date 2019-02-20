package weaver.interfaces.workflow.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 客服/编辑问题解答表流程<br>
 * 归档, 更新产品常见问答台账状态为已确认
 * 
 * @author ycj
 *
 */
public class UpdateCpcjwttz implements Action
{
  private Log logger = LogFactory.getLog(UpdateCpcjwttz.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();

    String sql = "";
    try
    {
        sql = "update uf_CPCJWD set ZT='1' where WTJDBD = '" + requestid + "'";
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