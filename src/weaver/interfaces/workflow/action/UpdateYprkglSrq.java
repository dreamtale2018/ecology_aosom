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

public class UpdateYprkglSrq implements Action
{
  private Log logger = LogFactory.getLog(UpdateYprkglSrq.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs1 = new RecordSet();
    RecordSet rs2 = new RecordSet();
    RecordSet rs3 = new RecordSet();
    
    String sql1 = "";
    String sql2 = "";
    String sql3 = "";
    try
    {
		ActionInfo info = ActionUtils.getActionInfo(request);
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String hhDetailA = Util.null2String(detailAMap.get("HH"));//明细表中的货号,此货号为ID
				sql1 = "select segment1 from uf_product where id = '"+ hhDetailA +"'";
				rs1.execute(sql1);
				if (rs1.next()){
					hhDetailA = rs1.getString("segment1");
		        }
				if(hhDetailA!=null && !"".equals(hhDetailA)){
					sql2 = "select id from formtable_main_159 where hh = '"+ hhDetailA +"' " +
							"order by modedatacreatedate desc,modedatacreatetime desc";
					rs2.execute(sql2);
					if (rs2.next()){
						String id = rs2.getString("id");
						sql3 = "update formtable_main_159 set YPZT='1' where id = '"+ id +"'";
						rs3.execute(sql3);
			        }
				}
			}
		}
	}
        //this.logger.error("sql：" + sql);
        
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