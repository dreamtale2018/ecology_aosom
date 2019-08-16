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
 * 硬件/耗材/办公用品采购单-子表流程<br>
 * 到达结束节点, 更新硬件/耗材/办公用品采购单实际到货数量和备注
 * 子流程结束后，数据增加至对应物料编号的 当前库存数量
 *
 * @author ycj
 *
 */
public class UpdateBgypcgzb implements Action
{
  private Log logger = LogFactory.getLog(UpdateBgypcgzb.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String WLBM = "";	//物料编码
    String SJDHSL = "";	//实际到货数量
    String BZ = "";		//备注
    String MXID = "";	//明细id

    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		WLBM = Util.null2String(mainTable.get("WLBM"));
		SJDHSL = Util.null2String(mainTable.get("SJDHSL"));
		MXID = Util.null2String(mainTable.get("MXHID"));
		BZ = Util.null2String(mainTable.get("BZ"));
        sql = "update formtable_main_276_dt1 set SJDHSL='" + SJDHSL + "',BZ='" + BZ + 
        		"' where id = '" + MXID + "'";
        rs.execute(sql);
        sql = "select dqkcsl from uf_kctz where wlbm = '" + WLBM + "'";
        rs.execute(sql);
        if(rs.next()){
        	String dqkcsl = rs.getString("dqkcsl");
        	if(dqkcsl!=null && !"".equals(dqkcsl)){
        		int sjkc = Integer.parseInt(dqkcsl) + Integer.parseInt(SJDHSL);
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