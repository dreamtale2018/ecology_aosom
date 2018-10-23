package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateLyfy implements Action {
	
	private Log logger = LogFactory.getLog(UpdateLyfy.class);
	

	@Override
	public String execute(RequestInfo request) {
	    String requestid = request.getRequestid();
	    String workflowid = request.getWorkflowid();
	    String formid = "";
	    String tablename = "";
	    
	    RecordSet rs = new RecordSet();
	    RecordSet rs1 = new RecordSet();
	    String id2 = "";    
	    String SQR = "";
	    Double CCHF = 0.00;//此次花费
	    String CXKSRJ = "";//出行开始日期
	    String CXJSRJ = "";//出行结束日期
	    String LYDD = "";//旅游地点
	    String CXRY = "";//出行人员

	    Double BNYY = 0.00;//本年已用
	    Double SYKY = 0.00;//剩余未用
	    
	    String JTMX = "";
	    String sql = "";
	    String sql1 = "";
	    try
	    {
//	      sql = "select * from workflow_base where id = '" + workflowid + "'";
//	      rs.execute(sql);
//	      if (rs.next()) {
//	        formid = Util.null2String(rs.getString("formid"));
//	      }
//	      this.logger.error("formid：" + formid);
//	      
//	      sql = "select * from workflow_bill where id = '" + 
//	        formid + "' "; 
//	      rs.execute(sql);
//	      if (rs.next()) {
//	        tablename = Util.null2String(rs.getString("tablename"));
//	      }
//	      this.logger.error("tablename：" + tablename);
//		  this.logger.error("select * from " + tablename + " where requestid =" + requestid);
//	      
//	      sql = "select * from " + tablename + " where requestid =" + requestid;
//	      rs.execute(sql);
//	      if (rs.next())
//	      {
//	        SQR = Util.null2String(rs.getString("SQR"));
//	        if(Util.getDoubleValue(rs.getString("CCHF"))==-1.0) {
//	        	CCHF = 0.00;
//	        }else {
//	        	CCHF = Util.getDoubleValue(rs.getString("CCHF"));
//	        }
//	        CXKSRJ = Util.null2String(rs.getString("CXKSRJ"));
//	        CXJSRJ = Util.null2String(rs.getString("CXJSRJ"));
//	        LYDD = Util.null2String(rs.getString("LYDD"));
//	        CXRY = Util.null2String(rs.getString("CXRY"));
//	        JTMX = Util.null2String(rs.getString("JTMX"));
//	        
//	        sql = "select * from uf_grlyfytz where SQR =" + SQR;
//	        rs.execute(sql);
//	        if (rs.next()) {
//		    if(Util.getDoubleValue(rs.getString("BNYY"))==-1.0) {
//		    		BNYY = 0.00;
//		    	}else {
//	        		BNYY = Util.getDoubleValue(rs.getString("BNYY"));
//		    	}
//		    if(Util.getDoubleValue(rs.getString("SYKY"))==-1.0) {
//		    		SYKY = 0.00;
//	        	}else {
//	        		SYKY = Util.getDoubleValue(rs.getString("SYKY"));
//	        	}
//	          BNYY = BNYY+CCHF;
//	          SYKY = SYKY-CCHF;
//	        }
//	        sql1 = "update uf_grlyfytz set BNYY='" + BNYY +"',SYKY='" + SYKY + "' where id='" + id2 + "'";
//	        rs1.execute(sql1);
//	        sql1 = "insert into uf_grlyfytz_dt1(mainid,CXKSRQ,CXJSRQ,LYDD,CXRY,CCHF,LC) values "
//			     +"('" + id2 + "','" + CXKSRJ + "','" + CXJSRJ + "','" + LYDD + "','" + CXRY +"','" +  CCHF +"','" + requestid + "')";
//	        rs1.execute(sql1);
//	      }
	      
	      ActionInfo info = ActionUtils.getActionInfo(request);
			
		  // 获取主表信息
		  Map<String, String> mainTable = info.getMainMap();
		  SQR = Util.null2String(mainTable.get("SQR"));
	        if(Util.getDoubleValue(mainTable.get("CCHF"))==-1.0) {
	        	CCHF = 0.00;
	        }else {
	        	CCHF = Util.getDoubleValue(mainTable.get("CCHF"));
	        }
	        CXKSRJ = Util.null2String(mainTable.get("CXKSRJ"));
	        CXJSRJ = Util.null2String(mainTable.get("CXJSRJ"));
	        LYDD = Util.null2String(mainTable.get("LYDD"));
	        CXRY = Util.null2String(mainTable.get("CXRY"));
	        JTMX = Util.null2String(mainTable.get("JTMX"));
	        
	        sql = "select * from uf_grlyfytz where SQR =" + SQR;
	        rs.execute(sql);
	        if (rs.next()) {
		    if(Util.getDoubleValue(rs.getString("BNYY"))==-1.0) {
		    		BNYY = 0.00;
		    	}else {
	        		BNYY = Util.getDoubleValue(rs.getString("BNYY"));
		    	}
		    if(Util.getDoubleValue(rs.getString("SYKY"))==-1.0) {
		    		SYKY = 0.00;
	        	}else {
	        		SYKY = Util.getDoubleValue(rs.getString("SYKY"));
	        	}
	          BNYY = BNYY+CCHF;
	          SYKY = SYKY-CCHF;
	        }
	        
	        sql1 = "update uf_grlyfytz set BNYY='" + BNYY +"',SYKY='" + SYKY + "' where id='" + id2 + "'";
	        logger.error(sql1);
	        rs1.execute(sql1);
	        sql1 = "insert into uf_grlyfytz_dt1(mainid,CXKSRQ,CXJSRQ,LYDD,CXRY,CCHF,LC) values "
	        		+"('" + id2 + "','" + CXKSRJ + "','" + CXJSRJ + "','" + LYDD + "','" + CXRY +"','" +  CCHF +"','" + requestid + "')";
	        logger.error(sql1);
	        rs1.execute(sql1);
	    } catch (Exception e) {
	    	this.logger.error("Exception e", e);
	    	request.getRequestManager().setMessageid("1111111112");
	    	request.getRequestManager().setMessagecontent(e + ",请联系管理员.");
	    	return Action.FAILURE_AND_CONTINUE;
	    }
	    return Action.SUCCESS;
	}

}
