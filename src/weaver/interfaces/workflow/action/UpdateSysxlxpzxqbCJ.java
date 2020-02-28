package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 拍照需求表更新摄影摄像类型台账<br>
 * 
 * @author jq
 * 
 *
 */
public class UpdateSysxlxpzxqbCJ implements Action
{
  private Log logger = LogFactory.getLog(UpdateSysxlxpzxqbCJ.class);
  
  OracleManager oracleManager = new OracleManager();
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    String requestid = request.getRequestid();

    String SFXPZ = "";	//是否需拍照
    String BDWCSJ = "";	//白底完成时间
    String sql = "";
    try
    {
    	
    		ActionInfo info = ActionUtils.getActionInfo(request);
        
        	
        	// 获取主表信息
    		Map<String, String> mainTable = info.getMainMap();
    		SFXPZ = Util.null2String(mainTable.get("BD"));
    		BDWCSJ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    		sql = "update uf_Sysxlx  set BD= '" + SFXPZ  + "', BDWCSJ = '" + BDWCSJ + "' where lc = '" + requestid + "'";
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
