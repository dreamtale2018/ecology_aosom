package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 国别产品信息变更单结束节点自动创建认证需求表<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateRzxqbGbcpAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateRzxqbGbcpAction.class);
  
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
    String LX = "";		//类型
    
    String sql = "";

    try
    {
		String workflowid = request.getWorkflowid();

    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		KFY = Util.null2String(mainTable.get("SQR"));
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		EJZZ = Util.null2String(mainTable.get("RJZZ"));
		LX = Util.null2String(mainTable.get("LX"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        if("1".equals(LX)){
        	// 获取流程明细表 1
    		List<Map<String, String>> detailAList = info.getDetailMap("1");
    		if (detailAList != null && detailAList.size() > 0) {
    			List<Map<String, String>> createAList = new ArrayList<Map<String, String>>();
    			//如果认证审批意见不为空则为明细数据。
    			for (int i = 0; i < detailAList.size(); i++) {
    				Map<String, String> detailAMap = detailAList.get(i);
    				String csjrzxxDetailA = Util.null2String(detailAMap.get("CSJRZXX")); //测试及认证信息
    				if(StringUtils.isNotBlank(csjrzxxDetailA)){
    					createAList.add(detailAMap);
    				}
    			}
    			if(createAList != null && createAList.size()>0){
    				//按照开发员进行分类。
    				Map<String, List<Map<String,String>>> detailMap = new HashMap<String, List<Map<String,String>>>();
    				for (int i = 0; i < createAList.size(); i++) {
    					Map<String, String> createAMap = createAList.get(i);
    					String kfDetailA = Util.null2String(createAMap.get("KF"));		// 开发
    					if(detailMap.containsKey(kfDetailA)){
    						detailMap.get(kfDetailA).add(createAMap);
    					}else{
    						List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
    						detailList.add(createAMap);
    						detailMap.put(kfDetailA, detailList);
    					}
    				}
    				for(Map.Entry<String,List<Map<String,String>>> entry : detailMap.entrySet()){
    					String kf = entry.getKey();
    					if(!"".equals(kf)){
    						KFY = kf;
    					}
    					sql = "select departmentid from hrmresource where id = '" + KFY + "'";
        				rs.execute(sql);
        				if(rs.next()){
        					EJZZ = Util.null2String(rs.getString("departmentid"));
        				}
        				sql = "select * from hrmdepartment  where id = (select SUPDEPID from hrmdepartment where id = '"+EJZZ+"' )";
        				rs.execute(sql);
        				if(rs.next()){
        					YJZZ = Util.null2String(rs.getString("id"));
        				}else{
        					YJZZ = EJZZ;
        				}
    					List<Map<String,String>> mxhhList = entry.getValue();
    					if (mxhhList != null && mxhhList.size() > 0) {
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
		    				wrti[3].setFieldName("gbcplclj");//国别产品流程链接       
		    				wrti[3].setFieldValue(requestid);//        
		    				wrti[3].setView(true);//字段是否可见       
		    				wrti[3].setEdit(true);//字段是否可编辑
		    				wrti[4] = new WorkflowRequestTableField();         
		    				wrti[4].setFieldName("sqrq");//申请日期       
		    				wrti[4].setFieldValue(SQRQ);//        
		    				wrti[4].setView(true);//字段是否可见       
		    				wrti[4].setEdit(true);//字段是否可编辑
		    				wrti[5] = new WorkflowRequestTableField();         
		    				wrti[5].setFieldName("xqly");//需求来源     
		    				wrti[5].setFieldValue(LX);//        
		    				wrti[5].setView(true);//字段是否可见       
		    				wrti[5].setEdit(true);//字段是否可编辑
		    				
		    				WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
		    				wrtri[0] = new WorkflowRequestTableRecord();        
		    				wrtri[0].setWorkflowRequestTableFields(wrti);           
		    				WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
		    				wmi.setRequestRecords(wrtri);
		    				
		    				int detailrows = mxhhList.size() ;//添加指定条数明细  
		    				//添加明细数据  
		    				wrtri = new WorkflowRequestTableRecord[detailrows];
		    				for (int i = 0; i < detailrows; i++) {
								Map<String, String> mxhhMap = mxhhList.get(i);
		    					String hhDetail = Util.null2String(mxhhMap.get("HH"));					//货号
		    					sql = "select segment1 from uf_product where id = '" + hhDetail + "'";
		        				rs.execute(sql);
		        				if(rs.next()){
		        					hhDetail = Util.null2String(rs.getString("segment1"));
		        				}
		    					String cppmDetail = Util.null2String(mxhhMap.get("CPPM"));				//产品品名
		    					String qykzgjDetail = Util.null2String(WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "QYKZGJ", mxhhMap.get("QYKZGJ")));		
		    																							//启用/扩增国别
		    					String csjrzxxDetail = Util.null2String(mxhhMap.get("CSJRZXX"));		//测试及认证信息
		    					
		    					wrti = new WorkflowRequestTableField[4]; //字段信息             
		    					wrti[0] = new WorkflowRequestTableField();             
		    					wrti[0].setFieldName("hh");//货号             
		    					wrti[0].setFieldValue(hhDetail);            
		    					wrti[0].setView(true);//字段是否可见              
		    					wrti[0].setEdit(true);//字段是否可编辑
		    					
		    					wrti[1] = new WorkflowRequestTableField();             
		    					wrti[1].setFieldName("pm");//品名          
		    					wrti[1].setFieldValue(cppmDetail);            
		    					wrti[1].setView(true);//字段是否可见              
		    					wrti[1].setEdit(true);//字段是否可编辑
		    					
		    					wrti[2] = new WorkflowRequestTableField();             
		    					wrti[2].setFieldName("gb");//国别           
		    					wrti[2].setFieldValue(qykzgjDetail);            
		    					wrti[2].setView(true);//字段是否可见              
		    					wrti[2].setEdit(true);//字段是否可编辑
		    					
		    					wrti[3] = new WorkflowRequestTableField();             
		    					wrti[3].setFieldName("csjrzyq");//测试及认证要求         
		    					wrti[3].setFieldValue(csjrzxxDetail);            
		    					wrti[3].setView(true);//字段是否可见              
		    					wrti[3].setEdit(true);//字段是否可编辑
		    					
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
		    				wbi.setWorkflowId("1323");//workflowid       
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