package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
 * 货号申请表结束节点自动创建认证需求表<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateRzxqbAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateRzxqbAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
	String KFY = "";	//开发员 
    String KFYXM = "";	//开发员姓名 
    String YJZZ = "";	//一级组织
    String EJZZ = "";	//二级组织
    String SQRQ = "";	//申请日期
    String PM = "";		//品名
    String CPTP = "";	//图片
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		KFY = Util.null2String(mainTable.get("SQR"));
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		EJZZ = Util.null2String(mainTable.get("EJZZ"));
		PM = Util.null2String(mainTable.get("ZWPM"));
		CPTP = Util.null2String(mainTable.get("CPTP"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        // 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			List<Map<String, String>> createAList = new ArrayList<Map<String, String>>();
			//如果认证审批意见不为空则为明细数据。
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String rzspyjDetailA = Util.null2String(detailAMap.get("RZSPYJ"));	//认证审批意见
				if(StringUtils.isNotBlank(rzspyjDetailA)){
					createAList.add(detailAMap);
				}
			}
			if(createAList != null && createAList.size()>0){
				//主字段        
				WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[6]; //字段信息        
				wrti[0] = new WorkflowRequestTableField();         
				wrti[0].setFieldName("kfy");//申请人       
				wrti[0].setFieldValue(KFY);//        
				wrti[0].setView(true);//字段是否可见       
				wrti[0].setEdit(true);//字段是否可编辑
				wrti[1] = new WorkflowRequestTableField();         
				wrti[1].setFieldName("yjzz");//一级组织      
				wrti[1].setFieldValue(YJZZ);//        
				wrti[1].setView(true);//字段是否可见       
				wrti[1].setEdit(true);//字段是否可编辑
				wrti[2] = new WorkflowRequestTableField();         
				wrti[2].setFieldName("ejzz");//二级组织       
				wrti[2].setFieldValue(EJZZ);//        
				wrti[2].setView(true);//字段是否可见       
				wrti[2].setEdit(true);//字段是否可编辑
				wrti[3] = new WorkflowRequestTableField();         
				wrti[3].setFieldName("hhsqlclj");//货号申请流程链接       
				wrti[3].setFieldValue(requestid);//        
				wrti[3].setView(true);//字段是否可见       
				wrti[3].setEdit(true);//字段是否可编辑
				wrti[4] = new WorkflowRequestTableField();         
				wrti[4].setFieldName("sqrq");//申请日期       
				wrti[4].setFieldValue(SQRQ);//        
				wrti[4].setView(true);//字段是否可见       
				wrti[4].setEdit(true);//字段是否可编辑
				wrti[5] = new WorkflowRequestTableField();         
				wrti[5].setFieldName("cptp");//申请日期       
				wrti[5].setFieldValue(CPTP);//        
				wrti[5].setView(true);//字段是否可见       
				wrti[5].setEdit(true);//字段是否可编辑
				
				WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
				wrtri[0] = new WorkflowRequestTableRecord();        
				wrtri[0].setWorkflowRequestTableFields(wrti);           
				WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
				wmi.setRequestRecords(wrtri);
				
				int detailrows = createAList.size() ;//添加指定条数明细  
				//添加明细数据  
				wrtri = new WorkflowRequestTableRecord[detailrows];
				for (int i = 0; i < detailrows; i++) {
					Map<String, String> createAMap = createAList.get(i);
					String hhDetailA = Util.null2String(createAMap.get("HH"));			//货号
					String gcxhDetailA = Util.null2String(createAMap.get("GCXH"));		//工厂型号
					String gbDetailA = Util.null2String(createAMap.get("GB"));			//国别
					String rzspyjDetailA = Util.null2String(createAMap.get("RZSPYJ"));	//测试及认证要求
					wrti = new WorkflowRequestTableField[5]; //字段信息             
					wrti[0] = new WorkflowRequestTableField();             
					wrti[0].setFieldName("hh");//货号             
					wrti[0].setFieldValue(hhDetailA);            
					wrti[0].setView(true);//字段是否可见              
					wrti[0].setEdit(true);//字段是否可编辑
					
					wrti[1] = new WorkflowRequestTableField();             
					wrti[1].setFieldName("pm");//品名          
					wrti[1].setFieldValue(PM);            
					wrti[1].setView(true);//字段是否可见              
					wrti[1].setEdit(true);//字段是否可编辑
					
					wrti[2] = new WorkflowRequestTableField();             
					wrti[2].setFieldName("gcxh");//工厂型号           
					wrti[2].setFieldValue(gcxhDetailA);            
					wrti[2].setView(true);//字段是否可见              
					wrti[2].setEdit(true);//字段是否可编辑
					
					wrti[3] = new WorkflowRequestTableField();             
					wrti[3].setFieldName("gb");//国别           
					wrti[3].setFieldValue(gbDetailA);            
					wrti[3].setView(true);//字段是否可见              
					wrti[3].setEdit(true);//字段是否可编辑
					
					wrti[4] = new WorkflowRequestTableField();             
					wrti[4].setFieldName("csjrzyq");//测试及认证要求         
					wrti[4].setFieldValue(rzspyjDetailA);            
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
				wbi.setWorkflowId("1743");//workflowid       
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
				wri.setRequestName("认证需求表-" + KFYXM + "-" + SQRQ);//流程标题        
				wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
				wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
				wri.setWorkflowBaseInfo(wbi);        
				WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
				String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(KFY));        
				this.logger.error("newRequestid:"+newRequestid);
				//this.logger.error("sql：" + sql);
			}
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