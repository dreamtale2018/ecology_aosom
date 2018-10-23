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

public class UpdateBpjtz implements Action
{
  private Log logger = LogFactory.getLog(UpdateBpjtz.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();
    String TJRQ = "";//提交日期
    String WCRQ = "";//完成日期
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
		
    	// 获取主表信息
    	Map<String, String> mainTable = info.getMainMap();
    	TJRQ = Util.null2String(mainTable.get("TJRQ"));
    	WCRQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String gcfkDetailA = Util.null2String(detailAMap.get("GCFK"));//工厂反馈
				String bzDetailA = Util.null2String(detailAMap.get("BZ"));//备注
				String zgddDetailA = Util.null2String(detailAMap.get("ZGDD"));//装柜地点
				String yjbprqDetailA = Util.null2String(detailAMap.get("YJBPRQ"));//预计补配日期
				String sjbprqDetailA = Util.null2String(detailAMap.get("SJBPRQ"));//实际补配日期
				String cybhDetailA = Util.null2String(detailAMap.get("CYBH"));//出运编号
				sql = "update uf_BPJTZ set ZT='1',GCFK='"+ gcfkDetailA +"',BZ='"+ bzDetailA +"'" +
						",ZGDD='"+ zgddDetailA +"',YJBPRQ='"+ yjbprqDetailA +"'" +
						",SJBPRQ='"+ sjbprqDetailA +"',CYBH='"+ cybhDetailA +"'" +
						",TJRQ='"+ TJRQ + "',WCRQ='"+ WCRQ + "' where LC = '"+ requestid +"'";
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