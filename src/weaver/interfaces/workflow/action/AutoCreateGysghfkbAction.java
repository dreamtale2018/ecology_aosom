package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * 供应商更换表结束自动创建供应商更换反馈表<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateGysghfkbAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateGysghfkbAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
	String SQR = "";	//申请人
	String SQRXM = "";	//申请姓名
	String GHHGYS = "";	//更换后供应商
    String SQRQ = "";	//申请日期 
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		GHHGYS = Util.null2String(mainTable.get("GHHGYS"));
		SQRQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		//主字段        
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[4]; //字段信息        
		wrti[0] = new WorkflowRequestTableField();         
		wrti[0].setFieldName("cg");//采购     
		wrti[0].setFieldValue(SQR);//        
		wrti[0].setView(true);//字段是否可见       
		wrti[0].setEdit(true);//字段是否可编辑
		wrti[1] = new WorkflowRequestTableField();         
		wrti[1].setFieldName("sqrq");//申请日期     
		wrti[1].setFieldValue(SQRQ);//        
		wrti[1].setView(true);//字段是否可见       
		wrti[1].setEdit(true);//字段是否可编辑
		wrti[2] = new WorkflowRequestTableField();         
		wrti[2].setFieldName("gys");//供应商   
		wrti[2].setFieldValue(GHHGYS);//        
		wrti[2].setView(true);//字段是否可见       
		wrti[2].setEdit(true);//字段是否可编辑
		wrti[3] = new WorkflowRequestTableField();         
		wrti[3].setFieldName("gysghlc");//供应商更换流程   
		wrti[3].setFieldValue(requestid);//        
		wrti[3].setView(true);//字段是否可见       
		wrti[3].setEdit(true);//字段是否可编辑
		
		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
		wrtri[0] = new WorkflowRequestTableRecord();        
		wrtri[0].setWorkflowRequestTableFields(wrti);           
		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
		wmi.setRequestRecords(wrtri);
        // 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			List<Map<String, String>> detailList  = new ArrayList<Map<String, String>>();
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String ghyyDetailA = Util.null2String(detailAMap.get("GHYY"));		//更换原因
				if(!"15".equals(ghyyDetailA)){
					detailList.add(detailAList.get(i));
				}
			}
			int detailrows = detailList.size() ;//添加指定条数明细  
			//添加明细数据  
			wrtri = new WorkflowRequestTableRecord[detailrows];
			for (int i = 0; i < detailrows; i++) {
				Map<String, String> detailMap = detailList.get(i);
				String hhDetail = Util.null2String(detailMap.get("HH"));		//货号
				String pmDetail = Util.null2String(detailMap.get("CPMC"));		//品名
				String gbDetail = Util.null2String(detailMap.get("XYGB"));		//国别
				String ggDetail = Util.null2String(detailMap.get("GG"));		//规格
				String sx1Detail = Util.null2String(detailMap.get("SX1"));		//属性1
				wrti = new WorkflowRequestTableField[5]; //字段信息             
				wrti[0] = new WorkflowRequestTableField();             
				wrti[0].setFieldName("hh");//货号             
				wrti[0].setFieldValue(hhDetail);            
				wrti[0].setView(true);//字段是否可见              
				wrti[0].setEdit(true);//字段是否可编辑
				wrti[1] = new WorkflowRequestTableField();             
				wrti[1].setFieldName("pm");//品名          
				wrti[1].setFieldValue(pmDetail);            
				wrti[1].setView(true);//字段是否可见              
				wrti[1].setEdit(true);//字段是否可编辑
				wrti[2] = new WorkflowRequestTableField();             
				wrti[2].setFieldName("gb");//国别          
				wrti[2].setFieldValue(gbDetail);            
				wrti[2].setView(true);//字段是否可见              
				wrti[2].setEdit(true);//字段是否可编辑
				wrti[3] = new WorkflowRequestTableField();             
				wrti[3].setFieldName("gg");//规格         
				wrti[3].setFieldValue(ggDetail);            
				wrti[3].setView(true);//字段是否可见              
				wrti[3].setEdit(true);//字段是否可编辑
				wrti[4] = new WorkflowRequestTableField();             
				wrti[4].setFieldName("sx1");//属性1        
				wrti[4].setFieldValue(sx1Detail);            
				wrti[4].setView(true);//字段是否可见              
				wrti[4].setEdit(true);//字段是否可编辑
				
				wrtri[i] = new WorkflowRequestTableRecord();
				wrtri[i].setWorkflowRequestTableFields(wrti);
			}
			//添加到明细表中        
			WorkflowDetailTableInfo WorkflowDetailTableInfo[] = new WorkflowDetailTableInfo[1];
			//指定明细表的个数，多个明细表指定多个，顺序按照明细的顺序        
			WorkflowDetailTableInfo[0] = new WorkflowDetailTableInfo();        
			WorkflowDetailTableInfo[0].setWorkflowRequestTableRecords(wrtri);
			//添加工作流id        
			WorkflowBaseInfo wbi = new WorkflowBaseInfo();        
			wbi.setWorkflowId("1823");//workflowid       
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
			wri.setRequestName("供应商更换反馈表-" + SQRXM + "-" + SQRQ);//流程标题        
			wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
			wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
			wri.setWorkflowBaseInfo(wbi);        
			WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
			String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(SQR));        
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