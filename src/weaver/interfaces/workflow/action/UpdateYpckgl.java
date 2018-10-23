package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateYpckgl implements Action
{
  private Log logger = LogFactory.getLog(UpdateYpckgl.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();
    String SQR = "";//申请人
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
		
    	// 获取主表信息
    	Map<String, String> mainTable = info.getMainMap();
    	SQR = Util.null2String(mainTable.get("SQR"));
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String hhDetailA = Util.null2String(detailAMap.get("HH"));//明细表中的货号，对应样品出入库台账的ID
				String ytDetailA = Util.null2String(detailAMap.get("YT"));//用途
				String sfxghDetailA = Util.null2String(detailAMap.get("SFXGH"));//是否需归还
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String CKRQ = sdf.format(date);
				sql = "update uf_ypcrktz set ZT='1',CKSQR='"+ SQR +"',CKRQ='"+ CKRQ +"'" +
						",CKYT='"+ ytDetailA +"',SFXGH='"+ sfxghDetailA +"'" +
						",CKLC='"+ requestid + "',KW='' where id = '"+ hhDetailA +"'";
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