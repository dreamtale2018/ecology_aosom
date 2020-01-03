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
 * 合同尾数清理表-子表流程<br>
 * 到达结束节点, 更新合同尾数清理表状态
 * 子流程结束后，合同尾数清理表更改状态为已完成
 *
 * @author 
 *
 */
public class UpdateHtwsqld implements Action
{
  private Log logger = LogFactory.getLog(UpdateBgypcgzb.class);
  
  @Override
  public String execute(RequestInfo request)
  {
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

					String mxidDetailA = Util.null2String(detailAMap.get("MXHID"));

					//更新主表中状态为已完成
			        sql = "update formtable_main_320_dt1 set MXHID='已完成',BZ='' where id = '" + mxidDetailA + "'";
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