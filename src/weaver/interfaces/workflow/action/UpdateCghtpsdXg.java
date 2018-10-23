package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateCghtpsdXg implements Action
{
  private Log logger = LogFactory.getLog(UpdateCghtpsdXg.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    RecordSet rs2 = new RecordSet();
    
    String requestid = request.getRequestid();
    String workflowid = request.getWorkflowid();

    String GJ = "";//改进
    String sql = "";
    String sql1 = "";
    String sql2 = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxidDetailA = Util.null2String(detailAMap.get("id"));//明细ID
				String psxmDetailA = Util.null2String(detailAMap.get("PSXM"));//评审项目
				String qtDetailA = Util.null2String(detailAMap.get("QT"));
				String hhDetailA = Util.null2String(detailAMap.get("HH"));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String zrrclrq = sdf.format(new Date());
				String mxid = "";
				sql2 = "select mxid from uf_HTPSTZ where LC = '" + requestid + "' and mxid='"+ mxidDetailA +"'";
				rs2.execute(sql2);
				if (rs2.next()){
					mxid = rs2.getString("mxid");
		        }
				if(mxid==null || "".equals(mxid)){
					sql = "update uf_HTPSTZ set ZRRCLZT='1',ZRRCLRQ='" + zrrclrq + "',QT='" + qtDetailA +
					"',PSXM='"+ psxmDetailA + "' where LC = '"+ requestid +"' and HH='"+ hhDetailA +"'";
				}else{
					sql = "update uf_HTPSTZ set ZRRCLZT='1',ZRRCLRQ='" + zrrclrq + "',QT='" + qtDetailA +
					"',PSXM='"+ psxmDetailA + "' where LC = '"+ requestid +"' and mxid='"+ mxidDetailA +"'";
				}
				rs.execute(sql);
				
				if("5".equals(psxmDetailA)){
					String gzjdsmDetailA = WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "GZJDSM", detailAMap.get("GZJDSM"));
																			//工作进度说明
					GJ = gzjdsmDetailA + qtDetailA;
					sql1 = "update uf_CPZLGSGZB set GJZT='2',GJ='"+ GJ +"' where lc = '"+ requestid +"' and mxid = '"+ mxidDetailA +"'";
					rs1.execute(sql1);
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