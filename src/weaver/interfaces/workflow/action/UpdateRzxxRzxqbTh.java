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
/**
 * 认证需求表审核节点退回更新认证信息台账<br>
 * 
 * @author ycj
 *
 */
public class UpdateRzxxRzxqbTh implements Action
{
  private Log logger = LogFactory.getLog(UpdateRzxxRzxqbTh.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    
	RecordSet rs = new RecordSet();
	
	String requestid = request.getRequestid();
    
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxidDetailA = Util.null2String(detailAMap.get("id"));		//明细ID
				sql = "update uf_RZXXB set tjrq = '',zt = '0'" +
				" where rzxqblc = '" + requestid + "' and mxid = '" + mxidDetailA + "'";
		rs.execute(sql);
			}
		}	
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