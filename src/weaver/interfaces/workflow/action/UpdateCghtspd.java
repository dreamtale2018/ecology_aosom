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

public class UpdateCghtspd implements Action
{
  private Log logger = LogFactory.getLog(UpdateCghtspd.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    
    String requestid = request.getRequestid();
    String workflowid = request.getWorkflowid();
    String formid = "";
    String tablename = "";
    String mainid = "";//主流程ID

    String sql = "";
    String sql1 = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
		sql = "select * from workflow_base where id = '" + workflowid + "'";
	    rs.execute(sql);
	    if (rs.next()) {
	      formid = Util.null2String(rs.getString("formid"));
	    }

	    this.logger.error("formid：" + formid);

	    sql = "select * from workflow_bill where id = '" + 
	    formid + "' ";
	    rs.execute(sql);
	    if (rs.next()) {
	      tablename = Util.null2String(rs.getString("tablename"));
	    }

	    this.logger.error("tablename：" + tablename);
	    // 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String hhDetailA = detailAMap.get("HH");	
				sql = "select thl from uf_returnrate where hh = '" + hhDetailA + "'";
				rs.execute(sql);
				if (rs.next()) {
					String thlStr = Util.null2String(rs.getString("thl"));
					sql1 = "select id from " + tablename + " where requestid = '" + requestid + "'";
					rs1.execute(sql1);
					if (rs1.next()) {
						mainid = Util.null2String(rs1.getString("id"));
						sql1 = "update " + tablename + "_dt1 set THL = '"+thlStr+"' " +
						"where mainid = '" + mainid + "' and hh = '" + hhDetailA + "'"; 
						rs1.execute(sql1);
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