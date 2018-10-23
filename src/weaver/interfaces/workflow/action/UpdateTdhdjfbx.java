package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateTdhdjfbx implements Action
{
  private Log logger = LogFactory.getLog(UpdateTdhdjfbx.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    RecordSet rs2 = new RecordSet();
    
    String requestid = request.getRequestid();
    String BMID = "";//主表ID 
    String BBID = "";//报备流程ID
    Double SJFY = 0.00;//实际费用
    
    Double JNYYFY = 0.00;//今年已用费用
    Double JNSYFY = 0.00;//今年剩余费用

    String sql = "";
    String sql1 = "";
    String sql2 = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	 // 获取主表信息
		  Map<String, String> mainTable = info.getMainMap();
		  BMID = Util.null2String(mainTable.get("BM"));
		  BBID = Util.null2String(mainTable.get("HDBBLC"));
	        if(Util.getDoubleValue(mainTable.get("SJFY"))==-1.0) {
	        	SJFY = 0.00;
	        }else {
	        	SJFY = Util.getDoubleValue(mainTable.get("SJFY"));
	        }
	        
        sql = "select * from uf_TDHDJF where id =" + BMID;
        rs.execute(sql);
        if (rs.next()){
	        if(Util.getDoubleValue(rs.getString("JNYYFY"))==-1.0) {
	        	JNYYFY = 0.00;
	        }else {
	        	JNYYFY = Util.getDoubleValue(rs.getString("JNYYFY"));
	        }
	        if(Util.getDoubleValue(rs.getString("JNSYFY"))==-1.0) {
	        	JNSYFY = 0.00;
	        }else {
	        	JNSYFY = Util.getDoubleValue(rs.getString("JNSYFY"));
	        }
	        JNYYFY = JNYYFY+SJFY;
	        JNSYFY = JNSYFY-SJFY;
        }
        
        sql1 = "update uf_TDHDJF set JNYYFY="+ JNYYFY +",JNSYFY="+ JNSYFY +" where id="+ BMID;
        rs1.execute(sql1);
        //this.logger.error("sql1：" + sql1);
        
        sql2 = "update uf_TDHDJF_dt1 set SJFY="+ SJFY +",BXLC="+ requestid +" where BBLC="+ BBID;
        rs2.execute(sql2);
        //this.logger.error("sql2：" + sql2);
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