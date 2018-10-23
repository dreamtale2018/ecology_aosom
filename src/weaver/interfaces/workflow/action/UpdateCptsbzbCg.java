package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdateCptsbzbCg implements Action
{
  private Log logger = LogFactory.getLog(UpdateCptsbzbCg.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();
    String GJ = "";//改进
    String WTLX = "";//问题类型

    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		GJ = Util.null2String(mainTable.get("GJ"));
		WTLX = Util.null2String(mainTable.get("WTLX"));
		if("1".equals(WTLX) || "2".equals(WTLX)){
	        sql = "update uf_CPZLGSGZB set GJZT='2',GJ='"+ GJ +"' where lc = '"+ requestid +"'";
	        rs.execute(sql);
		}
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