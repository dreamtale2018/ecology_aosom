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
 * 硬件/耗材/办公用品领用单流程<br>
 * 流程结束后扣减库存台帐：当前库存数量
 *
 * @author ycj
 *
 */
public class UpdateBgyplyJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateBgyplyJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String wlbmDetailA = Util.null2String(detailAMap.get("WLBM"));	//物料编码
				String slDetailA = Util.null2String(detailAMap.get("SL"));		//数量
				sql = "select dqkcsl from uf_kctz where wlbm = '" + wlbmDetailA + "'";
		        rs.execute(sql);
		        if(rs.next()){
		        	String dqkcsl = rs.getString("dqkcsl");
		        	if(dqkcsl!=null && !"".equals(dqkcsl)){
		        		int sjkc = Integer.parseInt(dqkcsl) - Integer.parseInt(slDetailA);
		        		sql = "update uf_kctz set dqkcsl = '" + sjkc + "' where wlbm = '" + wlbmDetailA + "'";
		                rs.execute(sql);
		        	}
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