package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdateCghtpsdFk implements Action
{
  private Log logger = LogFactory.getLog(UpdateCghtpsdFk.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    
    String requestid = request.getRequestid();

    String TJRQ = "";//提交日期
    String sql = "";
    String sql1 = "";
    try
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	TJRQ = sdf.format(new Date());
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxidDetailA = Util.null2String(detailAMap.get("id"));//明细ID
				String zrrDetailA = Util.null2String(detailAMap.get("ZRR"));
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
					sql = "update uf_HTPSTZ set ZRRCLZT='0',PSDZT='0',TJRQ='" + TJRQ + "',ZRR='"+ zrrDetailA + 
					"',FQRBZ='"+ fqrbzDetailA + "' where LC = '" + requestid + "' and HH='"+ hhDetailA +"'";
				}else{
					sql = "update uf_HTPSTZ set ZRRCLZT='0',PSDZT='0',TJRQ='" + TJRQ + "',ZRR='"+ zrrDetailA + 
					"',FQRBZ='"+ fqrbzDetailA + "' where LC = '" + requestid + "' and mxid='"+ mxidDetailA +"'";
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