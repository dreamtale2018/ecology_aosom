package weaver.interfaces.workflow.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdateXpkfjdHhsqJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateXpkfjdHhsqJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	String requestid = request.getRequestid();
    
	RecordSet rs = new RecordSet();
    
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
        
    	// 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxidDetailA = Util.null2String(detailAMap.get("id"));//明细ID
				String hhDetailA = Util.null2String(detailAMap.get("HH"));//货号
				sql = "update uf_XPKFRWGZB set HH = '" + hhDetailA + 
						"' where HHSQBLC = '" + requestid + "' and MXID = '" + mxidDetailA + "'";
				rs.execute(sql);
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