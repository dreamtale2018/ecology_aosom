package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdateGcspdCpwt implements Action
{
  private Log logger = LogFactory.getLog(UpdateGcspdCpwt.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();

    String WCRQ = "";	//完成日期
    String SJSP = "";	//实际索赔
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
    	Map<String, String> mainTable = info.getMainMap();
    	SJSP = Util.null2String(mainTable.get("SJSPJE"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		WCRQ = sdf.format(new Date());
		sql = "update uf_SPGLTZ set ZT='1',WCRQ='"+ WCRQ + "',SJSP='"+ SJSP + 
				"' where LC = '" + requestid + "'";
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