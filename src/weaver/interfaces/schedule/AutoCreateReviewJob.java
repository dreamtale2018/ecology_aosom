package weaver.interfaces.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;

import weaver.workflow.webservices.WorkflowBaseInfo;
import weaver.workflow.webservices.WorkflowMainTableInfo;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowRequestTableField;
import weaver.workflow.webservices.WorkflowRequestTableRecord;
import weaver.workflow.webservices.WorkflowServiceImpl;

import com.weaver.ningb.logging.LogFactory;

/**
 * 自动创建流程
 * 目前每天11点执行一次：0 0 11 * * ?
 * 
 * @author liberal
 *
 */
public class AutoCreateReviewJob extends BaseCronJob {

	private static final Log logger = LogFactory.getLog(AutoCreateReviewJob.class);
	
	@Override
	public void execute() {
		try
	    {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String SQRQ = sdf.format(new Date());
			//主字段        
			WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[1]; //字段信息        
			wrti[0] = new WorkflowRequestTableField();         
			wrti[0].setFieldName("sqr");//申请人       
			wrti[0].setFieldValue("1");//        
			wrti[0].setView(true);//字段是否可见       
			wrti[0].setEdit(true);//字段是否可编辑
			
			WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
			wrtri[0] = new WorkflowRequestTableRecord();        
			wrtri[0].setWorkflowRequestTableFields(wrti);           
			WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
			wmi.setRequestRecords(wrtri);
			/*//添加明细数据       
			wrtri = new WorkflowRequestTableRecord[1];
			wrti = new WorkflowRequestTableField[7]; //字段信息             
			wrti[0] = new WorkflowRequestTableField();             
			wrti[0].setFieldName("us");//US下单数量             
			wrti[0].setFieldValue(usqrdlDetailA);            
			wrti[0].setView(true);//字段是否可见              
			wrti[0].setEdit(true);//字段是否可编辑
			wrti[1] = new WorkflowRequestTableField();             
			wrti[1].setFieldName("ca");//CA下单数量             
			wrti[1].setFieldValue(caqrdlDetailA);            
			wrti[1].setView(true);//字段是否可见              
			wrti[1].setEdit(true);//字段是否可编辑
			wrti[2] = new WorkflowRequestTableField();             
			wrti[2].setFieldName("uk");//UK下单数量             
			wrti[2].setFieldValue(ukqrdlDetailA);            
			wrti[2].setView(true);//字段是否可见              
			wrti[2].setEdit(true);//字段是否可编辑
			wrti[3] = new WorkflowRequestTableField();             
			wrti[3].setFieldName("de");//DE下单数量             
			wrti[3].setFieldValue(deqrdlDetailA);            
			wrti[3].setView(true);//字段是否可见              
			wrti[3].setEdit(true);//字段是否可编辑
			wrti[4] = new WorkflowRequestTableField();             
			wrti[4].setFieldName("fr");//FR下单数量             
			wrti[4].setFieldValue(frqrdlDetailA);            
			wrti[4].setView(true);//字段是否可见              
			wrti[4].setEdit(true);//字段是否可编辑
			wrti[5] = new WorkflowRequestTableField();             
			wrti[5].setFieldName("it");//IT下单数量             
			wrti[5].setFieldValue(itqrdlDetailA);            
			wrti[5].setView(true);//字段是否可见              
			wrti[5].setEdit(true);//字段是否可编辑
			wrti[6] = new WorkflowRequestTableField();             
			wrti[6].setFieldName("es");//ES下单数量             
			wrti[6].setFieldValue(esqrdlDetailA);            
			wrti[6].setView(true);//字段是否可见              
			wrti[6].setEdit(true);//字段是否可编辑
			wrtri[0] = new WorkflowRequestTableRecord();
			wrtri[0].setWorkflowRequestTableFields(wrti);
			//添加到明细表中        
			WorkflowDetailTableInfo WorkflowDetailTableInfo[] = new WorkflowDetailTableInfo[1];
			//指定明细表的个数，多个明细表指定多个，顺序按照明细的顺序        
			WorkflowDetailTableInfo[0] = new WorkflowDetailTableInfo();        
			WorkflowDetailTableInfo[0].setWorkflowRequestTableRecords(wrtri);*/
			//添加工作流id        
			WorkflowBaseInfo wbi = new WorkflowBaseInfo();        
			wbi.setWorkflowId("862");//workflowid       
			WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息            
			wri.setCreatorId("1");//创建人id        
			wri.setRequestLevel("0");//0 正常，1重要，2紧急
			wri.setRequestName("货号申请表-系统管理员-" + SQRQ);//流程标题        
			wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
			//wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
			wri.setWorkflowBaseInfo(wbi);        
			WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
			String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, 1);        
			logger.error("newRequestid:"+newRequestid);
			//this.logger.error("sql：" + sql);
	}
	catch (Exception e)
    {
      logger.error("Exception e", e);
    }
	}
}
