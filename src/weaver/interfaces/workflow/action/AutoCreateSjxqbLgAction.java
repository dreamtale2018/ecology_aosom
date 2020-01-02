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
 * Listing优化需求表结束节点自动创建设计需求表<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateSjxqbLgAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateSjxqbLgAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
    String SQR = "";		//申请人 
    String SQRXM = "";		//申请姓名 
    String YJZZ = "";		//一级组织
    String EJZZ = "";		//二级组织
    String SQRQ = "";		//申请日期
    String YQWCR = "";		//要求完成日
    String SJYQ = "";		//设计要求
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		EJZZ = Util.null2String(mainTable.get("EJZZ"));
		YQWCR = Util.null2String(mainTable.get("YQWCR"));
		SJYQ = Util.null2String(mainTable.get("YHFA"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        
		//主字段        
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[8]; //字段信息        
		wrti[0] = new WorkflowRequestTableField();         
		wrti[0].setFieldName("sqr");//申请人       
		wrti[0].setFieldValue(SQR);//        
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
		wrti[3].setFieldName("sqrq");//申请日期    
		wrti[3].setFieldValue(SQRQ);//
		wrti[3].setView(true);//字段是否可见       
		wrti[3].setEdit(true);//字段是否可编辑
		wrti[4] = new WorkflowRequestTableField();  
		wrti[4].setFieldName("xqlx");//需求类型    
		wrti[4].setFieldValue("1");//
		wrti[4].setView(true);//字段是否可见       
		wrti[4].setEdit(true);//字段是否可编辑
		wrti[5] = new WorkflowRequestTableField();  
		wrti[5].setFieldName("yqwcr");//要求完成日期    
		wrti[5].setFieldValue(YQWCR);//
		wrti[5].setView(true);//字段是否可见       
		wrti[5].setEdit(true);//字段是否可编辑
		wrti[6] = new WorkflowRequestTableField();  
		wrti[6].setFieldName("lgyhxqblc");//Listing优化需求表流程    
		wrti[6].setFieldValue(requestid);//
		wrti[6].setView(true);//字段是否可见       
		wrti[6].setEdit(true);//字段是否可编辑
		wrti[7] = new WorkflowRequestTableField();  
		wrti[7].setFieldName("sjyq");//设计要求   
		wrti[7].setFieldValue(SJYQ);//
		wrti[7].setView(true);//字段是否可见       
		wrti[7].setEdit(true);//字段是否可编辑
		
		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
		wrtri[0] = new WorkflowRequestTableRecord();        
		wrtri[0].setWorkflowRequestTableFields(wrti);           
		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
		wmi.setRequestRecords(wrtri);
		// 优化项目选择图片/A+页面
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String yhxmDetailA = Util.null2String(detailAMap.get("YHXM"));			// 优化项目
				if("1".equals(yhxmDetailA)||"2".equals(yhxmDetailA)){
					detailList.add(detailAMap);
				}
			}
		}
		if(detailList.size()>0){
			//添加明细数据       
			wrtri = new WorkflowRequestTableRecord[detailList.size()];
			for (int i = 0; i < detailList.size(); i++) {
				Map<String, String> detailAMap = detailList.get(i);
				String hhDetailA = Util.null2String(detailAMap.get("HH"));			// 货号
				String cphDetailA = Util.null2String(detailAMap.get("CPH"));		// 产品号
				String cpmcDetailA = Util.null2String(detailAMap.get("CPMC"));		// 产品名称
				String sx1DetailA = Util.null2String(detailAMap.get("SX1"));		// 属性1
				String cpggDetailA = Util.null2String(detailAMap.get("CPGG"));		// 产品规格
				String tpDetailA = Util.null2String(detailAMap.get("TP"));			// 图片
				wrti = new WorkflowRequestTableField[6]; //字段信息             
				wrti[0] = new WorkflowRequestTableField();             
				wrti[0].setFieldName("hh");//货号             
				wrti[0].setFieldValue(hhDetailA);            
				wrti[0].setView(true);//字段是否可见              
				wrti[0].setEdit(true);//字段是否可编辑
				wrti[1] = new WorkflowRequestTableField();             
				wrti[1].setFieldName("cpmc");//产品名称            
				wrti[1].setFieldValue(cpmcDetailA);            
				wrti[1].setView(true);//字段是否可见              
				wrti[1].setEdit(true);//字段是否可编辑
				wrti[2] = new WorkflowRequestTableField();             
				wrti[2].setFieldName("sx1");//属性1            
				wrti[2].setFieldValue(sx1DetailA);            
				wrti[2].setView(true);//字段是否可见              
				wrti[2].setEdit(true);//字段是否可编辑
				wrti[3] = new WorkflowRequestTableField();             
				wrti[3].setFieldName("tp");//图片             
				wrti[3].setFieldValue(tpDetailA);            
				wrti[3].setView(true);//字段是否可见              
				wrti[3].setEdit(true);//字段是否可编辑
				wrti[4] = new WorkflowRequestTableField();             
				wrti[4].setFieldName("cpgg");//产品规格         
				wrti[4].setFieldValue(cpggDetailA);            
				wrti[4].setView(true);//字段是否可见              
				wrti[4].setEdit(true);//字段是否可编辑
				wrti[5] = new WorkflowRequestTableField();             
				wrti[5].setFieldName("cph");//产品号        
				wrti[5].setFieldValue(cphDetailA);            
				wrti[5].setView(true);//字段是否可见              
				wrti[5].setEdit(true);//字段是否可编辑
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
			wbi.setWorkflowId("5123");//workflowid       
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
			wri.setRequestName("设计需求表-" + SQRXM + "-" + SQRQ);//流程标题        
			wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
			wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
			wri.setWorkflowBaseInfo(wbi);        
			WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
			String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(SQR)); 
			this.logger.error("newRequestid:"+newRequestid);
			String submitStr = workflowServiceImpl.submitWorkflowRequest(wri, Integer.parseInt(newRequestid), Integer.parseInt(SQR), "submit", "流程自动提交");
			this.logger.error("submitStr:"+submitStr);
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