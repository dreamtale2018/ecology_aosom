package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
 * 内部员工推荐表结束节点自动创建调动申请单<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateDdsqdAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateDdsqdAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
	String DGR = "";	//员工姓名
    String DCYJZZ = "";	//现部门
    String DCEJZZ = "";	//现分组织
    String GWMC = "";	//岗位名称
    String YGZ = "";	//现工资(元)
    String DRYJZZ = "";	//接收部门
    String DREJZZ = "";	//接收分组织
    String DRGW = "";	//接收岗位
    String DCSYB = "";	//调出事业部
    String SQR = "";	//人事专员
    String SQRXM = "";	//人事专员姓名
    String SQRQ = "";	//申请日期
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		DGR = Util.null2String(mainTable.get("YGXM"));
		DCYJZZ = Util.null2String(mainTable.get("YJZZ"));
		DCEJZZ = Util.null2String(mainTable.get("EJZZ"));
		GWMC = Util.null2String(mainTable.get("GWMC"));
		YGZ = Util.null2String(mainTable.get("XGZ"));
		DRYJZZ = Util.null2String(mainTable.get("JSBM"));
		DREJZZ = Util.null2String(mainTable.get("JSFZZ"));
		DRGW = Util.null2String(mainTable.get("JSGW"));
		SQR = Util.null2String(mainTable.get("RSZY"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        
		//主字段        
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[11]; //字段信息        
		wrti[0] = new WorkflowRequestTableField();         
		wrti[0].setFieldName("ddry");//调岗人      
		wrti[0].setFieldValue(DGR);//        
		wrti[0].setView(true);//字段是否可见       
		wrti[0].setEdit(true);//字段是否可编辑
		wrti[1] = new WorkflowRequestTableField();         
		wrti[1].setFieldName("dcyjzz");//调出一级组织      
		wrti[1].setFieldValue(DCYJZZ);//        
		wrti[1].setView(true);//字段是否可见       
		wrti[1].setEdit(true);//字段是否可编辑
		wrti[2] = new WorkflowRequestTableField();         
		wrti[2].setFieldName("dcejzz");//调出二级组织      
		wrti[2].setFieldValue(DCEJZZ);//        
		wrti[2].setView(true);//字段是否可见       
		wrti[2].setEdit(true);//字段是否可编辑
		wrti[3] = new WorkflowRequestTableField();         
		wrti[3].setFieldName("gwmc");//岗位名称      
		wrti[3].setFieldValue(GWMC);//        
		wrti[3].setView(true);//字段是否可见       
		wrti[3].setEdit(true);//字段是否可编辑
		wrti[4] = new WorkflowRequestTableField();         
		wrti[4].setFieldName("ygz");//原工资     
		wrti[4].setFieldValue(YGZ);//        
		wrti[4].setView(true);//字段是否可见       
		wrti[4].setEdit(true);//字段是否可编辑
		wrti[5] = new WorkflowRequestTableField();         
		wrti[5].setFieldName("dryjzz");//调入一级组织       
		wrti[5].setFieldValue(DRYJZZ);//        
		wrti[5].setView(true);//字段是否可见       
		wrti[5].setEdit(true);//字段是否可编辑
		wrti[6] = new WorkflowRequestTableField();         
		wrti[6].setFieldName("drejzz");//调入二级组织   
		wrti[6].setFieldValue(DREJZZ);//        
		wrti[6].setView(true);//字段是否可见       
		wrti[6].setEdit(true);//字段是否可编辑
		wrti[7] = new WorkflowRequestTableField();         
		wrti[7].setFieldName("drgw");//调入岗位   
		wrti[7].setFieldValue(DRGW);//        
		wrti[7].setView(true);//字段是否可见       
		wrti[7].setEdit(true);//字段是否可编辑
		wrti[8] = new WorkflowRequestTableField();         
		wrti[8].setFieldName("dcsyb");//调出事业部   
		wrti[8].setFieldValue(DCSYB);//        
		wrti[8].setView(true);//字段是否可见       
		wrti[8].setEdit(true);//字段是否可编辑
		wrti[9] = new WorkflowRequestTableField();         
		wrti[9].setFieldName("tbr");//申请人  
		wrti[9].setFieldValue(SQR);//        
		wrti[9].setView(true);//字段是否可见       
		wrti[9].setEdit(true);//字段是否可编辑
		wrti[10] = new WorkflowRequestTableField();         
		wrti[10].setFieldName("ygtjlc");//员工推荐流程  
		wrti[10].setFieldValue(requestid);//        
		wrti[10].setView(true);//字段是否可见       
		wrti[10].setEdit(true);//字段是否可编辑
		
		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
		wrtri[0] = new WorkflowRequestTableRecord();        
		wrtri[0].setWorkflowRequestTableFields(wrti);           
		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
		wmi.setRequestRecords(wrtri);
		
		//添加工作流id        
		WorkflowBaseInfo wbi = new WorkflowBaseInfo();        
		wbi.setWorkflowId("1603");//workflowid       
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
		wri.setRequestName("调动申请单-" + SQRXM + "-" + SQRQ);//流程标题        
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