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

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 产品认证制作申请表售后保障主管确认节点自动创建产品证书制作费用单<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateCprzzzsqbAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateCprzzzsqbAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
	String SQR = "";	//申请人
    String SQRXM = "";	//申请人 姓名 
    String YJZZ = "";	//一级组织
    String EJZZ = "";	//二级组织
    String SQRQ = "";	//申请日期
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		EJZZ = Util.null2String(mainTable.get("EJZZ"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			//按照工厂名称进行分类。
			Map<String, List<Map<String,String>>> detailMap = new HashMap<String, List<Map<String,String>>>();
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String fygsDetailA = Util.null2String(detailAMap.get("FYGS"));
				String gcmcDetailA = Util.null2String(detailAMap.get("GCMC"));
				//费用为归属工厂承担或共同承担时。
				if("0".equals(fygsDetailA) || "1".equals(fygsDetailA)){
					if(detailMap.containsKey(gcmcDetailA)){
						detailMap.get(gcmcDetailA).add(detailAMap);
					}else{
						List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
						detailList.add(detailAMap);
						detailMap.put(gcmcDetailA, detailList);
					}
				}
			}
			for(Map.Entry<String,List<Map<String,String>>> entry : detailMap.entrySet()){
				List<Map<String,String>> mxhhList = entry.getValue();
				if (mxhhList != null && mxhhList.size() > 0) {
					Map<String, String> mxhhMapA = mxhhList.get(0);
					String gysDetail = Util.null2String(mxhhMapA.get("GCMC"));			//供应商
					String cgyDetail = Util.null2String(mxhhMapA.get("CGY"));			//采购员
    				//主字段        
    				WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[7]; //字段信息        
    				wrti[0] = new WorkflowRequestTableField();         
    				wrti[0].setFieldName("zdr");//制单人       
    				wrti[0].setFieldValue(SQR);//        
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
    				wrti[3].setFieldName("zdrq");//制单日期       
    				wrti[3].setFieldValue(SQRQ);//        
    				wrti[3].setView(true);//字段是否可见       
    				wrti[3].setEdit(true);//字段是否可编辑
    				wrti[4] = new WorkflowRequestTableField();         
    				wrti[4].setFieldName("gys");//供应商    
    				wrti[4].setFieldValue(gysDetail);//        
    				wrti[4].setView(true);//字段是否可见       
    				wrti[4].setEdit(true);//字段是否可编辑
    				wrti[5] = new WorkflowRequestTableField();         
    				wrti[5].setFieldName("cgy");//采购员   
    				wrti[5].setFieldValue(cgyDetail);//        
    				wrti[5].setView(true);//字段是否可见       
    				wrti[5].setEdit(true);//字段是否可编辑
    				wrti[6] = new WorkflowRequestTableField();         
    				wrti[6].setFieldName("cprzzzsqblc");//产品认证制作申请表流程    
    				wrti[6].setFieldValue(requestid);//        
    				wrti[6].setView(true);//字段是否可见       
    				wrti[6].setEdit(true);//字段是否可编辑
    				
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
						String hhDetail = Util.null2String(mxhhMap.get("HH"));				//货号
						String pmDetail = Util.null2String(mxhhMap.get("PM"));				//品名
    					
    					wrti = new WorkflowRequestTableField[2]; //字段信息             
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
    				wbi.setWorkflowId("19124");//workflowid       
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
    				wri.setRequestName("产品证书制作费用单-" + SQRXM + "-" + SQRQ);//流程标题        
    				wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
    				wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
    				wri.setWorkflowBaseInfo(wbi);        
    				WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
    				String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(SQR));        
    				this.logger.error("newRequestid:"+newRequestid);
    				//this.logger.error("sql：" + sql);
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