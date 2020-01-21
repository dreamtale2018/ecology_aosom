package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 海外投资付款审批流程到达02节点根据合同号获取采购合同审批单链接<br>
 * 
 * @author ycj
 *
 */
public class UpdateHwtzfksp implements Action
{
  private Log logger = LogFactory.getLog(UpdateHwtzfksp.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();
    String HTH = "";	//合同号
    String CGHTLC = "";	//采购合同流程
    String SCFKLC = "";	//上次付款流程
    
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
    	Map<String, String> mainTable = info.getMainMap();
    	HTH = Util.null2String(mainTable.get("HTH"));
    	//获取合同号对应的付款审批流程
    	sql = "select * from formtable_main_280 where HTH = '" + HTH + "'";
    	rs.execute(sql);
    	while(rs.next()){
			String id = Util.null2String(rs.getString("requestid"));
    		if(!requestid.equals(id)){
    			SCFKLC += id + ",";
    		}
    	}
    	sql = "update formtable_main_280 set SCFKLC = '" + SCFKLC + "' where requestid = '" + requestid + "'";
    	rs.execute(sql);
    	String[] hthArr = HTH.split(",");
    	for(int i=0;i<hthArr.length;i++){
    		if(!"".equals(hthArr[i])){
    			//获取合同号对应的采购合同审批单
    			sql = "select * from formtable_main_265 where CGHTH = '" + hthArr[i] + "'";
    			rs.execute(sql);
    			if(rs.next()){
    				if(i==hthArr.length-1){
    					CGHTLC += Util.null2String(rs.getString("requestid"));
    				}else{
    					CGHTLC += Util.null2String(rs.getString("requestid")) + ",";
    				}
    			}
    		}
    	}
    	sql = "update formtable_main_280 set CGHTLC = '" + CGHTLC + "' where requestid = '" + requestid + "'";
    	rs.execute(sql);
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