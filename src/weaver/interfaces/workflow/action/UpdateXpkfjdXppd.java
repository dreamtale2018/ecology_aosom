package weaver.interfaces.workflow.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdateXpkfjdXppd implements Action
{
  private OracleManager oracleManager = new OracleManager();
  private Log logger = LogFactory.getLog(UpdateXpkfjdXppd.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	String requestid = request.getRequestid();
	RecordSet rs = new RecordSet();
	
	String PDSJ = "";
    
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		PDSJ = Util.null2String(mainTable.get("SQRQ"));
    	
    	// 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String hhDetailA = oracleManager.getHhmc(detailAMap.get("HH"));//货号
				String usDetailA = Util.null2String(detailAMap.get("USSL1"));//US数量
				String caDetailA = Util.null2String(detailAMap.get("CASL1"));//CA数量
				String ukDetailA = Util.null2String(detailAMap.get("UKSL1"));//UK数量
				String deDetailA = Util.null2String(detailAMap.get("DESL1"));//DE数量
				String frDetailA = Util.null2String(detailAMap.get("FRSL1"));//FR数量
				String itDetailA = Util.null2String(detailAMap.get("ITSL1"));//IT数量
				String esDetailA = Util.null2String(detailAMap.get("ESSL1"));//ES数量
				sql = "update uf_XPKFRWGZB set USA = '" + usDetailA + "',CA = '" + caDetailA + 
						"',UK = '" + ukDetailA + "',DE = '" + deDetailA + 
						"',FR = '" + frDetailA + "',IT = '" + itDetailA + 
						"',ES = '" + esDetailA + "',XPPDBLC = '" + requestid + 
						"',PDSJ = '" + PDSJ + "' where HH = '" + hhDetailA + "'";
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