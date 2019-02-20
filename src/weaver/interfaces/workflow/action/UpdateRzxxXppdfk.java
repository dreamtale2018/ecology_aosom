package weaver.interfaces.workflow.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 新品排单反馈表更新认证信息台账<br>
 * 
 * @author ycj
 *
 */
public class UpdateRzxxXppdfk implements Action
{
  private Log logger = LogFactory.getLog(UpdateRzxxXppdfk.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    
	  OracleManager oracleManager = new OracleManager();
	  RecordSet rs = new RecordSet();
	    
	  String HTJQ = "";		//合同交期
    
	  String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		HTJQ = Util.null2String(mainTable.get("JQ"));			
		
    	// 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String hhDetailA = oracleManager.getHhmc(detailAMap.get("HH3"));//货号
				sql = "update uf_RZXXB set HTJQ = '" + HTJQ + "' where HH = '" + hhDetailA + "'";
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