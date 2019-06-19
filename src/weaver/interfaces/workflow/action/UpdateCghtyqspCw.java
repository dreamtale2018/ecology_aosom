package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

import weaver.conn.RecordSet;
import weaver.general.Util;
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
    String BZ = "";			//币种
    String SPJE = "";		//索赔金额
    String SJSPJE = "";		//实际索赔金额


    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		BZ = Util.null2String(mainTable.get("BZ"));
	    SPJE = Util.null2String(mainTable.get("SPJE"));
	    SJSPJE = Util.null2String(mainTable.get("SJSPJE"));
        sql = "update uf_SPGLTZ set ZT='2',BZ = '" + BZ + "',SPJE = '" + SPJE + 
              "',SJSP = '" + SJSPJE + "' where lc = '" + requestid + "'";
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