package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
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
 * 到达结束节点, 更新索赔台账状态为已完成
 * 
 * @author ycj
 *
 */
public class UpdateCghtyqspJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateCghtyqspJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();
    String WCRQ = "";//完成日期
    String SJSPJE = "";//实际索赔

    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SJSPJE = Util.null2String(mainTable.get("SJSPJE"));
		WCRQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        sql = "update uf_SPGLTZ set GJZT='2',WCRQ='" + WCRQ +
        		"',SJSP='" + SJSPJE + "' where lc = '"+ requestid + "'";
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