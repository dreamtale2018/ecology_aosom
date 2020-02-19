package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.webservices.WorkflowBaseInfo;
import weaver.workflow.webservices.WorkflowMainTableInfo;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowRequestTableField;
import weaver.workflow.webservices.WorkflowRequestTableRecord;
import weaver.workflow.webservices.WorkflowServiceImpl;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 自动创建提醒功能<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateTXAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateTXAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String SQR = "522";
	String SQRXM = "";
    String SQRQ = "";	//申请日期
	
    String sql = "";

    try
    {
		String workflowid = request.getWorkflowid();

    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
		//主字段        
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[1]; //字段信息        
		wrti[0] = new WorkflowRequestTableField();         
		wrti[0].setFieldName("mutiresource");//申请人       
		wrti[0].setFieldValue(SQR);//        
		wrti[0].setView(true);//字段是否可见       
		wrti[0].setEdit(true);//字段是否可编辑
		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
		wrtri[0] = new WorkflowRequestTableRecord();        
		wrtri[0].setWorkflowRequestTableFields(wrti);           
		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
		wmi.setRequestRecords(wrtri);
		
		//添加工作流id        
		WorkflowBaseInfo wbi = new WorkflowBaseInfo();        
		wbi.setWorkflowId("1");//workflowid       
		WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息            
		wri.setCreatorId(SQR);//创建人id        
		wri.setRequestLevel("0");//0 正常，1重要，2紧急
		sql = "select lastname from hrmresource where id = '" + SQR + "'";
		rs.execute(sql);
		if(rs.next()){
			SQRXM = Util.null2String(rs.getString("lastname"));
			if(SQRXM.indexOf("`~`7")!=-1 && SQRXM.indexOf("`~`8")!=-1){
				SQRXM = SQRXM.split("`~`7")[1].split("`~`8")[0].trim(); 
			}else{
				SQRXM = SQRXM.split("`~`7")[0].split("`~`8")[0].trim();
			}
		}
		wri.setRequestName("tixing-" + SQRXM + "-" + SQRQ);//流程标题        
		wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
		wri.setWorkflowBaseInfo(wbi);        
		WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
		String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(SQR)); 
		this.logger.error("newRequestid:"+newRequestid);
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