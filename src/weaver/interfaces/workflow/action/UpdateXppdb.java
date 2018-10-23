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

public class UpdateXppdb implements Action
{
  private Log logger = LogFactory.getLog(UpdateXppdb.class);
  
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
				String hhDetailA = Util.null2String(detailAMap.get("HH"));		// 货号
				sql = "select cptp from formtable_main_196_dt1 a "
					+ " left join formtable_main_196 b on a.mainid = b.id "
					+ " left join uf_product c on c.segment1 = a.hh "
					+ " where c.id = '" + hhDetailA + "'";
				rs.execute(sql);
				if (rs.next()){
					String cptp = Util.null2String(rs.getString("cptp"));
					if(!"".equals(cptp)){
						String sqlTp = "update formtable_main_195_dt1 set tp = '" + cptp + "' where hh = '"+ hhDetailA +"'";
						rs.execute(sqlTp);
					}
				}
			}
		}
        
        
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