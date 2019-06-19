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
/**
 * 产品终止流程 03产品资料提交后更新产品投诉子表-问题产品台帐台帐<br>
 * 
 * @author ycj
 *
 */
public class UpdateWtcptzCpzz implements Action
{
  private Log logger = LogFactory.getLog(UpdateWtcptzCpzz.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	String requestid = request.getRequestid();
    
	RecordSet rs = new RecordSet();
    
    String sql = "";
    String RQ = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
        
    	// 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
	        RQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String hhDetailA = Util.null2String(detailAMap.get("HH"));			//货号
				String gbDetailA = Util.null2String(detailAMap.get("XZZGJ"));		//国别
				String gbxsyjDetailA = Util.null2String(detailAMap.get("GBXSYJ"));	//国别销售意见
				if("0".equals(gbxsyjDetailA)){
					sql = "update uf_CPTSTZ set CPZLZT = '2'," + 
					"RQ = '" + RQ + "',ZZLC = '" + requestid + 
					"' where HH = '" + hhDetailA + "' and GB = '" + gbDetailA + "'";
					rs.execute(sql);
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