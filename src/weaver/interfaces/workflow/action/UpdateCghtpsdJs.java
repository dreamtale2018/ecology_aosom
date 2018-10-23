package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdateCghtpsdJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateCghtpsdJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    
    String requestid = request.getRequestid();

    String WCRQ = "";//完成日期
    String sql = "";
    String sql1 = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
		sql = "update uf_HTPSTZ set PSDZT='1' where LC = '"+ requestid +"'";
		rs.execute(sql);
    	
    	// 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String psxmDetailA = Util.null2String(detailAMap.get("PSXM"));//评审项目
				if("5".equals(psxmDetailA)){
					WCRQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					sql1 = "update uf_CPZLGSGZB set GJZT='1',WCRQ='"+ WCRQ +"' where lc = '"+ requestid +"'";
					rs1.execute(sql1);
				}
			}
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