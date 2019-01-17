package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.weaver.ningb.direct.entity.integration.TaskAssignedReview;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * 产品客户中差评改善任务分配表结束节点触发产品客户中差评改善任务分配-子表<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateZcpgsrwzbAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateZcpgsrwzbAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	String requestid = request.getRequestid();
	 
    String TBRQ = "";//填表日期 
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TBRQ = sdf.format(new Date());
        // 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		Map<String, List<TaskAssignedReview>> map = new HashMap<String, List<TaskAssignedReview>>();
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxhhDetailA = Util.null2String(detailAMap.get("id"));				// 行号
				String cpmcDetailA = Util.null2String(detailAMap.get("CPMC"));				// 产品名称
				String cpzcpplnrDetailA = Util.null2String(detailAMap.get("CPZCPPLNR"));	// 产品中差评评论内容
				String dyskuDetailA = Util.null2String(detailAMap.get("DYSKU"));			// 对应SKU
				String fsrqDetailA = Util.null2String(detailAMap.get("FSRQ"));				// 发生日期
				String gbDetailA = Util.null2String(detailAMap.get("GB"));					// 国别
				String kslxDetailA = Util.null2String(detailAMap.get("KSLX"));				// 客述类型
				String xjDetailA = Util.null2String(detailAMap.get("XJ"));					// 星级
				String zrrDetailA = Util.null2String(detailAMap.get("ZRR"));				// 责任人
				String sffprwDetailA = Util.null2String(detailAMap.get("SFFPRW"));			// 是否分配任务
				//是否分配任务选择“是”后会形成“产品客户中差评改善任务--子表”。
				if("0".equals(sffprwDetailA)){
					TaskAssignedReview taskAssignedReview = new TaskAssignedReview();
					taskAssignedReview.setCpmc(cpmcDetailA);
					taskAssignedReview.setCpzcpplnr(cpzcpplnrDetailA);
					taskAssignedReview.setDysku(dyskuDetailA);
					taskAssignedReview.setFsrq(fsrqDetailA);
					taskAssignedReview.setGb(gbDetailA);
					taskAssignedReview.setKslx(kslxDetailA);
					taskAssignedReview.setMxid(mxhhDetailA);
					taskAssignedReview.setSffprw(sffprwDetailA);
					taskAssignedReview.setXj(xjDetailA);
					taskAssignedReview.setZrr(zrrDetailA);
					//map中包含责任人的时候直接获取责任人list并添加。
					if(map.containsKey(zrrDetailA)){
						map.get(zrrDetailA).add(taskAssignedReview);
					}else{
						List<TaskAssignedReview> taskList = new ArrayList<TaskAssignedReview>();
						taskList.add(taskAssignedReview);
						map.put(zrrDetailA, taskList);
					}
				}
			}
		}
		for(Map.Entry<String,List<TaskAssignedReview>> entry : map.entrySet()){
			String zrr = entry.getKey();				//责任人
			String zrrxm = "";							//责任人姓名
			List<TaskAssignedReview> taskList = entry.getValue();
			//主字段        
			WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[3]; //字段信息        
			wrti[0] = new WorkflowRequestTableField();         
			wrti[0].setFieldName("tbr");//填表人       
			wrti[0].setFieldValue(zrr);//        
			wrti[0].setView(true);//字段是否可见       
			wrti[0].setEdit(true);//字段是否可编辑
			wrti[1] = new WorkflowRequestTableField();         
			wrti[1].setFieldName("tbrq");//填表日期       
			wrti[1].setFieldValue(TBRQ);//        
			wrti[1].setView(true);//字段是否可见       
			wrti[1].setEdit(true);//字段是否可编辑
			wrti[2] = new WorkflowRequestTableField();         
			wrti[2].setFieldName("zblj");//主表流程链接        
			wrti[2].setFieldValue(requestid);//        
			wrti[2].setView(true);//字段是否可见       
			wrti[2].setEdit(true);//字段是否可编辑
			
			WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
			wrtri[0] = new WorkflowRequestTableRecord();        
			wrtri[0].setWorkflowRequestTableFields(wrti);           
			WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
			wmi.setRequestRecords(wrtri);
			int detailrows = taskList.size() ;//添加指定条数明细        
			//添加明细数据       
			wrtri = new WorkflowRequestTableRecord[detailrows];
			//添加指定条数行明细数据        
			for(int i = 0 ; i < detailrows ; i++){
				TaskAssignedReview task = taskList.get(i);
				//每行明细对应的字段             
				wrti = new WorkflowRequestTableField[10]; //字段信息             
				wrti[0] = new WorkflowRequestTableField();             
				wrti[0].setFieldName("zbmxid");//主表明细ID          
				wrti[0].setFieldValue(task.getMxid());            
				wrti[0].setView(true);//字段是否可见              
				wrti[0].setEdit(true);//字段是否可编辑
				
				wrti[1] = new WorkflowRequestTableField();             
				wrti[1].setFieldName("cpmc");//产品名称            
				wrti[1].setFieldValue(task.getCpmc());            
				wrti[1].setView(true);//字段是否可见              
				wrti[1].setEdit(true);//字段是否可编辑
				
				wrti[2] = new WorkflowRequestTableField();             
				wrti[2].setFieldName("cpzcpplnr");//产品中差评评论内容           
				wrti[2].setFieldValue(task.getCpzcpplnr());            
				wrti[2].setView(true);//字段是否可见              
				wrti[2].setEdit(true);//字段是否可编辑
				
				wrti[3] = new WorkflowRequestTableField();             
				wrti[3].setFieldName("dysku");//对应SKU           
				wrti[3].setFieldValue(task.getDysku());            
				wrti[3].setView(true);//字段是否可见              
				wrti[3].setEdit(true);//字段是否可编辑
				
				wrti[4] = new WorkflowRequestTableField();             
				wrti[4].setFieldName("fsrq");//发生日期            
				wrti[4].setFieldValue(task.getFsrq());            
				wrti[4].setView(true);//字段是否可见              
				wrti[4].setEdit(true);//字段是否可编辑
				
				wrti[5] = new WorkflowRequestTableField();             
				wrti[5].setFieldName("gb");//国别            
				wrti[5].setFieldValue(task.getGb());            
				wrti[5].setView(true);//字段是否可见              
				wrti[5].setEdit(true);//字段是否可编辑
				
				wrti[6] = new WorkflowRequestTableField();             
				wrti[6].setFieldName("kslx");//客述类型          
				wrti[6].setFieldValue(task.getKslx());            
				wrti[6].setView(true);//字段是否可见              
				wrti[6].setEdit(true);//字段是否可编辑
				
				wrti[7] = new WorkflowRequestTableField();             
				wrti[7].setFieldName("sffprw");//是否分配任务           
				wrti[7].setFieldValue(task.getSffprw());            
				wrti[7].setView(true);//字段是否可见              
				wrti[7].setEdit(true);//字段是否可编辑
				
				wrti[8] = new WorkflowRequestTableField();             
				wrti[8].setFieldName("xj");//星级            
				wrti[8].setFieldValue(task.getXj());            
				wrti[8].setView(true);//字段是否可见              
				wrti[8].setEdit(true);//字段是否可编辑
				
				wrti[9] = new WorkflowRequestTableField();             
				wrti[9].setFieldName("zrr");//责任人            
				wrti[9].setFieldValue(task.getZrr());            
				wrti[9].setView(true);//字段是否可见              
				wrti[9].setEdit(true);//字段是否可编辑
				
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
			wbi.setWorkflowId("1964");//workflowid       
			WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息            
			wri.setCreatorId(zrr);//创建人id        
			wri.setRequestLevel("0");//0 正常，1重要，2紧急
			sql = "select lastname from hrmresource where id = '" + zrr + "'";
			rs.execute(sql);
			if(rs.next()){
				zrrxm = Util.null2String(rs.getString("lastname"));
				if(zrrxm.indexOf("`~`7")!=-1 && zrrxm.indexOf("`~`8")!=-1){
					zrrxm = zrrxm.split("`~`7")[1].split("`~`8")[0].trim(); 
				}else{
					zrrxm = zrrxm.split("`~`7")[0].split("`~`8")[0].trim();
				}
			}
			wri.setRequestName("产品客户中差评改善任务分配表-子表-" + zrrxm + "-" + TBRQ);//流程标题        
			wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
			wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
			wri.setWorkflowBaseInfo(wbi);        
			WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
			String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(zrr));        
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