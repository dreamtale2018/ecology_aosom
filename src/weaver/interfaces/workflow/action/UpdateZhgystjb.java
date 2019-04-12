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
 * 展会供应商推荐子表结束节点更新反馈信息到主表<br>
 * 
 * @author ycj
 *
 */
public class UpdateZhgystjb implements Action
{
  private Log logger = LogFactory.getLog(UpdateZhgystjb.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    
    String requestid = request.getRequestid();
    String ID = "";		//子流程ID
    String KFFK = "";	//开发反馈 
    String KFBZ = "";	//开发备注
    String FKSJ = "";	//反馈时间 
    String LCBH = "";	//流程编号 

    String sql = "";
    String sql1 = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	 // 获取主表信息
		  Map<String, String> mainTable = info.getMainMap();
		  KFFK = Util.null2String(mainTable.get("KFFK"));
		  KFBZ = Util.null2String(mainTable.get("KFBZ"));
		  FKSJ = Util.null2String(mainTable.get("FKSJ"));
		  LCBH = Util.null2String(mainTable.get("LCBH"));
		sql = "select * from formtable_main_245 where requestid = '"+ requestid +"'";
		rs.execute(sql);
        if (rs.next()){
        	ID = rs.getString("ZLCID");
        }
        sql1 = "update formtable_main_244_dt1 set KFFK='" + KFFK + "',KFBZ='" + KFBZ + 
        		"',FKSJ='" + FKSJ + "',ZLCID='" + LCBH + "' where ID = '"+ ID +"'";
        rs1.execute(sql1);
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