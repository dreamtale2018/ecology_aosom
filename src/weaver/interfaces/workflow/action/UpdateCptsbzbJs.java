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

public class UpdateCptsbzbJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateCptsbzbJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();
    String WCRQ = "";//完成日期
    String WTLX = "";//问题类型

    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
    	WTLX = Util.null2String(mainTable.get("WTLX"));
		WCRQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if("1".equals(WTLX) || "2".equals(WTLX)){
	        sql = "update uf_CPZLGSGZB set GJZT='1',WCRQ='"+ WCRQ +"' where lc = '"+ requestid +"'";
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