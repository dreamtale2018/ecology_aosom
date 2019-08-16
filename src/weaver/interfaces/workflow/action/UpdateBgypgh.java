package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 办公用品归还登记(入库)流程<br>
 * 流程结束后，数据增加至对应物料编号的 当前库存数量
 *
 * @author ycj
 *
 */
public class UpdateBgypgh implements Action
{
  private Log logger = LogFactory.getLog(UpdateBgypgh.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String WLBM = "";	//物料编码
    String GHSL = "";	//归还数量

    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		WLBM = Util.null2String(mainTable.get("WLBM"));
		GHSL = Util.null2String(mainTable.get("GHSL"));
        sql = "select dqkcsl from uf_kctz where wlbm = '" + WLBM + "'";
        rs.execute(sql);
        if(rs.next()){
        	String dqkcsl = rs.getString("dqkcsl");
        	if(dqkcsl!=null && !"".equals(dqkcsl)){
        		int sjkc = Integer.parseInt(dqkcsl) + Integer.parseInt(GHSL);
        		sql = "update uf_kctz set dqkcsl = '" + sjkc + "' where wlbm = '" + WLBM + "'";
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