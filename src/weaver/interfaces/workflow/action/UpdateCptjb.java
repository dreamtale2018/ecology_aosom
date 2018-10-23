package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdateCptjb implements Action
{
  private Log logger = LogFactory.getLog(UpdateCptjb.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    
    String requestid = request.getRequestid();
    String ID = "";//子流程ID
    int KFFK = 0;//开发反馈 
    String BZ = "";//备注 
    String TJH = "";//推荐号 

    String sql = "";
    String sql1 = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	 // 获取主表信息
		  Map<String, String> mainTable = info.getMainMap();
		  KFFK = Integer.parseInt(mainTable.get("KFFK"));
		  BZ = Util.null2String(mainTable.get("BZ"));
		  TJH = Util.null2String(mainTable.get("TJH"));
		  BZ = BZ.replace("&nbsp;"," ");
		  BZ = BZ.replace("&quot;","\"");
		  BZ = BZ.replace("'","''");
		sql = "select * from formtable_main_111 where requestid = '"+ requestid +"'";
		rs.execute(sql);
        if (rs.next()){
        	ID = rs.getString("ZLCID");
        }
        sql1 = "update formtable_main_96_dt1 set FKJG='" + KFFK + "',KFFK='" + BZ + "'," +
        		"TJH='" + TJH + "' where ID = '"+ ID +"'";
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