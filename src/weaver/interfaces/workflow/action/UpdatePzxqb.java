package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdatePzxqb implements Action
{
  private Log logger = LogFactory.getLog(UpdatePzxqb.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    RecordSet rs2 = new RecordSet();
    
    String requestid = request.getRequestid();
    String ID = "";//主表ID 
    String CPGG = "";//产品规格
    String CPSX1 = "";//产品属性1

    String sql = "";
    String sql1 = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	 // 获取主表信息
		  Map<String, String> mainTable = info.getMainMap();
		  ID = Util.null2String(mainTable.get("HH"));
		sql = "select * from uf_product where SEGMENT1 = '"+ ID +"'";
		rs.execute(sql);
        
		if (rs.next()){
			CPGG = Util.null2String(rs.getString("ITEM_SPECIFICATION_CN"));
			CPSX1 = Util.null2String(rs.getString("ITEM_ATTACHED_ATTRIBUTE1"));
        }
        sql1 = "update formtable_main_157 set CPGG='" + CPGG + "',CPSX1='" + CPSX1 + "' where REQUESTID = '"+ requestid +"'";
        rs1.execute(sql1);
        String sqlTp = "select tp from formtable_main_195_dt1 a "
			+ " left join formtable_main_195 b on a.mainid = b.id "
			+ " left join workflow_requestbase c on b.requestId = c.requestid "
			+ " where hh2 = '" + ID + "' "
			+ " and c.currentnodetype = 3 ";
        rs2.execute(sqlTp);
        if (rs2.next()) {
			String cptp = Util.null2String(rs2.getString("tp"));
			if("".equals(cptp)){
				sqlTp = "select tp from formtable_main_104_dt1 a "
					+ " left join formtable_main_104 b on a.mainid = b.id "
					+ " left join workflow_requestbase c on b.requestId = c.requestid "
					+ " where hh2 = '" + ID + "' "
					+ " and c.currentnodetype = 3 ";
				rs2.execute(sqlTp);
				if (rs2.next()) {
					cptp = Util.null2String(rs2.getString("tp"));
				}
			}
			if(!"".equals(cptp)){
				String sqlTp1 = "update formtable_main_157 set cptp = '" + cptp + "' where REQUESTID = '"+ requestid +"'";
				rs2.execute(sqlTp1);
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