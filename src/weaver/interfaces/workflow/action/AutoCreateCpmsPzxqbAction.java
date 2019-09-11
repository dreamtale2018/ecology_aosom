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
import weaver.workflow.webservices.WorkflowDetailTableInfo;
import weaver.workflow.webservices.WorkflowMainTableInfo;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowRequestTableField;
import weaver.workflow.webservices.WorkflowRequestTableRecord;
import weaver.workflow.webservices.WorkflowServiceImpl;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 拍照需求表结束节点自动创建产品描述/包装尺寸变更申请单 <br>
 * 
 * @author ycj
 *
 */
public class AutoCreateCpmsPzxqbAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateCpmsPzxqbAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
	String KFY = "";	//开发员
    String KFYXM = "";	//开发员 姓名 
    String YJZZ = "";	//一级组织
    String EJZZ = "";	//二级组织
    String SQRQ = "";	//申请日期
    String SFBG = "";	//是否变更产品规格/产品属性内容
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		KFY = Util.null2String(mainTable.get("KF"));
		SFBG = Util.null2String(mainTable.get("SFBG"));
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		EJZZ = Util.null2String(mainTable.get("EJZZ"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        if("0".equals(SFBG)){
        	//主字段        
    		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[5]; //字段信息        
    		wrti[0] = new WorkflowRequestTableField();         
    		wrti[0].setFieldName("sqr");//申请人       
    		wrti[0].setFieldValue(KFY);//        
    		wrti[0].setView(true);//字段是否可见       
    		wrti[0].setEdit(true);//字段是否可编辑
    		wrti[1] = new WorkflowRequestTableField();         
    		wrti[1].setFieldName("yjzz");//一级组织      
    		wrti[1].setFieldValue(YJZZ);//        
    		wrti[1].setView(true);//字段是否可见       
    		wrti[1].setEdit(true);//字段是否可编辑
    		wrti[2] = new WorkflowRequestTableField();         
    		wrti[2].setFieldName("rjzz");//二级组织       
    		wrti[2].setFieldValue(EJZZ);//        
    		wrti[2].setView(true);//字段是否可见       
    		wrti[2].setEdit(true);//字段是否可编辑
    		wrti[3] = new WorkflowRequestTableField();         
    		wrti[3].setFieldName("pzxqblc");//国别产品流程链接       
    		wrti[3].setFieldValue(requestid);//        
    		wrti[3].setView(true);//字段是否可见       
    		wrti[3].setEdit(true);//字段是否可编辑
    		wrti[4] = new WorkflowRequestTableField();         
    		wrti[4].setFieldName("sqrq");//申请日期       
    		wrti[4].setFieldValue(SQRQ);//        
    		wrti[4].setView(true);//字段是否可见       
    		wrti[4].setEdit(true);//字段是否可编辑
    		
    		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
    		wrtri[0] = new WorkflowRequestTableRecord();        
    		wrtri[0].setWorkflowRequestTableFields(wrti);           
    		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
    		wmi.setRequestRecords(wrtri);
    		
    		//添加明细数据  
    		wrtri = new WorkflowRequestTableRecord[1];
    		String hhDetailA = Util.null2String(mainTable.get("HH"));				//货号
    		sql = "select id from uf_product where segment1 = '" + hhDetailA + "'";
    		rs.execute(sql);
    		if(rs.next()){
    			hhDetailA = Util.null2String(rs.getString("id"));
    		}
    		String cppmDetailA = Util.null2String(mainTable.get("CPPM"));			//产品品名
    		String gysDetailA = Util.null2String(mainTable.get("GYS"));				//供应商
    		
    		wrti = new WorkflowRequestTableField[3]; //字段信息             
    		wrti[0] = new WorkflowRequestTableField();             
    		wrti[0].setFieldName("hh2");//货号             
    		wrti[0].setFieldValue(hhDetailA);            
    		wrti[0].setView(true);//字段是否可见              
    		wrti[0].setEdit(true);//字段是否可编辑
    		
    		wrti[1] = new WorkflowRequestTableField();             
    		wrti[1].setFieldName("pm");//品名          
    		wrti[1].setFieldValue(cppmDetailA);            
    		wrti[1].setView(true);//字段是否可见              
    		wrti[1].setEdit(true);//字段是否可编辑
    		
    		wrti[2] = new WorkflowRequestTableField();             
    		wrti[2].setFieldName("gys");//供应商           
    		wrti[2].setFieldValue(gysDetailA);            
    		wrti[2].setView(true);//字段是否可见              
    		wrti[2].setEdit(true);//字段是否可编辑
    		
    		wrtri[0] = new WorkflowRequestTableRecord();
    		wrtri[0].setWorkflowRequestTableFields(wrti);
    		//添加到明细表中        
    		WorkflowDetailTableInfo WorkflowDetailTableInfo[] = new WorkflowDetailTableInfo[1];
    		//指定明细表的个数，多个明细表指定多个，顺序按照明细的顺序        
    		WorkflowDetailTableInfo[0] = new WorkflowDetailTableInfo();        
    		WorkflowDetailTableInfo[0].setWorkflowRequestTableRecords(wrtri);
    		//添加工作流id        
    		WorkflowBaseInfo wbi = new WorkflowBaseInfo();        
    		wbi.setWorkflowId("1303");//workflowid       
    		WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息            
    		wri.setCreatorId(KFY);//创建人id        
    		wri.setRequestLevel("0");//0 正常，1重要，2紧急
    		sql = "select lastname from hrmresource where id = '" + KFY + "'";
    		rs.execute(sql);
    		if(rs.next()){
    			KFYXM = Util.null2String(rs.getString("lastname"));
    			if(KFYXM.indexOf("`~`7")!=-1 && KFYXM.indexOf("`~`8")!=-1){
    				KFYXM = KFYXM.split("`~`7")[1].split("`~`8")[0].trim(); 
    			}else{
    				KFYXM = KFYXM.split("`~`7")[0].split("`~`8")[0].trim();
    			}
    		}
    		wri.setRequestName("产品描述/包装尺寸变更申请单-" + KFYXM + "-" + SQRQ);//流程标题        
    		wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
    		wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
    		wri.setWorkflowBaseInfo(wbi);        
    		WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
    		String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(KFY));        
    		this.logger.error("newRequestid:"+newRequestid);
    		//this.logger.error("sql：" + sql);
        }
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