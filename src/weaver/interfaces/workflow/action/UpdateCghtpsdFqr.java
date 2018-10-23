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

public class UpdateCghtpsdFqr implements Action
{
  private Log logger = LogFactory.getLog(UpdateCghtpsdFqr.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    
    String requestid = request.getRequestid();

    String sql = "";
    String sql1 = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxidDetailA = Util.null2String(detailAMap.get("id"));//明细ID
				String fqrbzDetailA = Util.null2String(detailAMap.get("FQRBZ"));
				fqrbzDetailA = fqrbzDetailA.replace("&nbsp;"," ");
				fqrbzDetailA = fqrbzDetailA.replace("&quot;","\"");
				fqrbzDetailA = fqrbzDetailA.replace("'","''");
				String hhDetailA = Util.null2String(detailAMap.get("HH"));
				String mxid = "";
				sql1 = "select mxid from uf_HTPSTZ where LC = '" + requestid + "' and mxid='"+ mxidDetailA +"'";
				rs1.execute(sql1);
				if (rs1.next()){
					mxid = rs1.getString("mxid");
		        }
				if(mxid==null || "".equals(mxid)){
					sql = "update uf_HTPSTZ set FQRBZ='"+ fqrbzDetailA + "' where LC = '" + requestid + 
					"' and HH='"+ hhDetailA +"'";
				}else{
					sql = "update uf_HTPSTZ set FQRBZ='"+ fqrbzDetailA + "' where LC = '" + requestid + 
					"' and mxid='"+ mxidDetailA +"'";
				}
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