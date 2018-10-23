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

public class AutoCreateComplainAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateComplainAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	RecordSet rs1 = new RecordSet();
	  
    String FQR = "";//发起人 
    String SQRXM = "";//申请人姓名
    String CGHTH = "";//采购合同号 
    String GYS = "";//供应商
    String SQRQ = "";//申请日期
    String YJZZ = "";//一级组织
    String EJZZ = "";//二级组织
    
    String sql = "";
    String sql1 = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		FQR = Util.null2String(mainTable.get("FQR"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        // 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		List<Map<String, String>> hhList = new ArrayList<Map<String, String>>();
		boolean flag = false;
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String mxhhDetailA = Util.null2String(detailAMap.get("id"));		// 行号
				String hhDetailA = Util.null2String(detailAMap.get("HH"));			// 货号
				String pmDetailA = Util.null2String(detailAMap.get("PM"));			// 品名
				String dycgDetailA = Util.null2String(detailAMap.get("DYCG"));		// 对应采购
				if(map.containsKey(dycgDetailA)){
					map.get(dycgDetailA).add(mxhhDetailA);
				}else{
					List<String> mxhhList = new ArrayList<String>();
					mxhhList.add(mxhhDetailA);
					map.put(dycgDetailA, mxhhList);
				}
			}
		}
		for(Map.Entry<String,List<String>> entry : map.entrySet()){
			String dycg = entry.getKey();
			List<String> mxhhList = entry.getValue();
			//主字段        
			WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[1]; //字段信息        
			wrti[0] = new WorkflowRequestTableField();         
			wrti[0].setFieldName("fqr");//发起人        
			wrti[0].setFieldValue(FQR);//        
			wrti[0].setView(true);//字段是否可见       
			wrti[0].setEdit(true);//字段是否可编辑
			
			WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
			wrtri[0] = new WorkflowRequestTableRecord();        
			wrtri[0].setWorkflowRequestTableFields(wrti);           
			WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
			wmi.setRequestRecords(wrtri);
			int detailrows = mxhhList.size() ;//添加指定条数明细        
			//添加明细数据       
			wrtri = new WorkflowRequestTableRecord[detailrows];
			//添加指定条数行明细数据        
			for(int i = 0 ; i < detailrows ; i++){
				String mxhh = mxhhList.get(i);
				String hhDetail = "";
				sql1 = "select hh from formtable_main_98_dt1 where id='" + mxhh + "'";
				rs1.execute(sql1);
				if(rs1.next()){
					hhDetail = rs1.getString("hh");
				}
				//每行明细对应的字段             
				wrti = new WorkflowRequestTableField[2]; //字段信息             
				wrti[0] = new WorkflowRequestTableField();             
				wrti[0].setFieldName("dycg");//对应采购             
				wrti[0].setFieldValue(dycg);            
				wrti[0].setView(true);//字段是否可见              
				wrti[0].setEdit(true);//字段是否可编辑
				
				wrti[1] = new WorkflowRequestTableField();             
				wrti[1].setFieldName("hh");//货号            
				wrti[1].setFieldValue(hhDetail);            
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
			wbi.setWorkflowId("361");//workflowid       
			WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息            
			wri.setCreatorId(FQR);//创建人id        
			wri.setRequestLevel("0");//0 正常，1重要，2紧急
			sql = "select lastname from hrmresource where id = '" + FQR + "'";
			rs.execute(sql);
			if(rs.next()){
				SQRXM = Util.null2String(rs.getString("lastname"));
				if(SQRXM.indexOf("`~`7")!=-1 && SQRXM.indexOf("`~`8")!=-1){
					SQRXM = SQRXM.split("`~`7")[1].split("`~`8")[0].trim(); 
				}else{
					SQRXM = SQRXM.split("`~`7")[0].split("`~`8")[0].trim();
				}
			}
			wri.setRequestName("产品投诉表-" + SQRXM + "-" + SQRQ);//流程标题        
			wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
			wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
			wri.setWorkflowBaseInfo(wbi);        
			WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
			String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(FQR));        
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