package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

public class UpdatePzrwdSys implements Action
{
  private Log logger = LogFactory.getLog(UpdatePzrwdSys.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String requestid = request.getRequestid();
    String YPDD = "";	//样品地点 
    String SFXYPZ = "";	//是否需拍照 
    String YY = "";		//原因
    String PZYQ = "";	//拍照要求
    String SJXQ = "";	//实景需求
    //String BYWCRQ = "";//备样完成日期

    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		YPDD = Util.null2String(mainTable.get("YPDD"));
		SFXYPZ = Util.null2String(mainTable.get("SFXPZ"));
		YY = Util.null2String(mainTable.get("SM"));
		PZYQ = Util.null2String(mainTable.get("HBYQ"));
		SJXQ = Util.null2String(mainTable.get("SJ"));
		//BYWCRQ = Util.null2String(mainTable.get("BYWCRQ"));
        sql = "update formtable_main_159 set PZZT='1',YPDD='"+ YPDD + 
        		"',SFXYPZ='" + SFXYPZ + "',YY='" + YY + "',PZYQ='" + PZYQ + 
        		"',SJXQ='" + SJXQ + "' where lc = '" + requestid + "'";
        rs.execute(sql);
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