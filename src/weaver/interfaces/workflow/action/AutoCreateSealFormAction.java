package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

public class AutoCreateSealFormAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateSealFormAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String CG = "";//采购
    String SQR = "";//申请人
    String SQRXM = "";//申请人姓名
    String YJZZ = "";//一级组织 
    String EJZZ = "";//二级组织
    String GYS = "";// 供应商
    String SQRQ = "";//申请日期

    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		CG = Util.null2String(mainTable.get("CG"));
		SQR = Util.null2String(mainTable.get("SQR"));
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		EJZZ = Util.null2String(mainTable.get("EJZZ"));
		GYS = Util.null2String(mainTable.get("GYS"));
        SQRQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String xlpDetailA = Util.null2String(detailAMap.get("XLP"));		// 新老品
				String fyDetailA = Util.null2String(detailAMap.get("FY"));			// 封样
				String hhDetailA = Util.null2String(detailAMap.get("HH"));			// 货号
				String cppmDetailA = Util.null2String(detailAMap.get("CPPM"));		// 产品品名
				String tpDetailA = Util.null2String(detailAMap.get("TP"));			// 图片
				String cpggDetailA = Util.null2String(detailAMap.get("CPGG"));		// 产品规格
				String cpsx1DetailA = Util.null2String(detailAMap.get("CPSX1"));	// 产品属性1
				String cpsx2DetailA = Util.null2String(detailAMap.get("CPSX2"));	// 产品属性2
				if("0".equals(fyDetailA)){
					//主字段        
					WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[16]; //字段信息        
					wrti[0] = new WorkflowRequestTableField();         
					wrti[0].setFieldName("cg");//采购        
					wrti[0].setFieldValue(CG);//        
					wrti[0].setView(true);//字段是否可见       
					wrti[0].setEdit(true);//字段是否可编辑
					
					wrti[1] = new WorkflowRequestTableField();         
					wrti[1].setFieldName("fqr");//发起人        
					wrti[1].setFieldValue(SQR);//        
					wrti[1].setView(true);//字段是否可见       
					wrti[1].setEdit(true);//字段是否可编辑
					
					wrti[2] = new WorkflowRequestTableField();         
					wrti[2].setFieldName("byzrr");//备样责任人       
					wrti[2].setFieldValue(SQR);//        
					wrti[2].setView(true);//字段是否可见       
					wrti[2].setEdit(true);//字段是否可编辑
					
					wrti[3] = new WorkflowRequestTableField();         
					wrti[3].setFieldName("cqyqrzrr");//产前样确认责任人       
					wrti[3].setFieldValue(SQR);//        
					wrti[3].setView(true);//字段是否可见       
					wrti[3].setEdit(true);//字段是否可编辑
					
					wrti[4] = new WorkflowRequestTableField();         
					wrti[4].setFieldName("fyzrr");//封样责任人      
					wrti[4].setFieldValue(SQR);//        
					wrti[4].setView(true);//字段是否可见       
					wrti[4].setEdit(true);//字段是否可编辑
					
					wrti[5] = new WorkflowRequestTableField();         
					wrti[5].setFieldName("yjzz");//一级组织       
					wrti[5].setFieldValue(YJZZ);//        
					wrti[5].setView(true);//字段是否可见       
					wrti[5].setEdit(true);//字段是否可编辑
					
					wrti[6] = new WorkflowRequestTableField();         
					wrti[6].setFieldName("ejzz");//二级组织       
					wrti[6].setFieldValue(EJZZ);//        
					wrti[6].setView(true);//字段是否可见       
					wrti[6].setEdit(true);//字段是否可编辑
					
					wrti[7] = new WorkflowRequestTableField();         
					wrti[7].setFieldName("gys");//供应商     
					wrti[7].setFieldValue(GYS);//        
					wrti[7].setView(true);//字段是否可见       
					wrti[7].setEdit(true);//字段是否可编辑
					
					wrti[8] = new WorkflowRequestTableField();         
					wrti[8].setFieldName("xlp");//新老品    
					wrti[8].setFieldValue(xlpDetailA);//        
					wrti[8].setView(true);//字段是否可见       
					wrti[8].setEdit(true);//字段是否可编辑
					
					wrti[9] = new WorkflowRequestTableField();         
					wrti[9].setFieldName("sfxyfy");//是否需封样     
					wrti[9].setFieldValue(fyDetailA);//        
					wrti[9].setView(true);//字段是否可见       
					wrti[9].setEdit(true);//字段是否可编辑
					
					wrti[10] = new WorkflowRequestTableField();         
					wrti[10].setFieldName("hh");//货号     
					wrti[10].setFieldValue(hhDetailA);//        
					wrti[10].setView(true);//字段是否可见       
					wrti[10].setEdit(true);//字段是否可编辑
					
					wrti[11] = new WorkflowRequestTableField();         
					wrti[11].setFieldName("cppm");//产品品名     
					wrti[11].setFieldValue(cppmDetailA);//        
					wrti[11].setView(true);//字段是否可见       
					wrti[11].setEdit(true);//字段是否可编辑
					
					wrti[12] = new WorkflowRequestTableField();         
					wrti[12].setFieldName("cptp");//产品图片    
					wrti[12].setFieldValue(tpDetailA);//        
					wrti[12].setView(true);//字段是否可见       
					wrti[12].setEdit(true);//字段是否可编辑
					
					wrti[13] = new WorkflowRequestTableField();         
					wrti[13].setFieldName("cpgg");//产品规格     
					wrti[13].setFieldValue(cpggDetailA);//        
					wrti[13].setView(true);//字段是否可见       
					wrti[13].setEdit(true);//字段是否可编辑
					
					wrti[14] = new WorkflowRequestTableField();         
					wrti[14].setFieldName("cpsx1");//产品属性1     
					wrti[14].setFieldValue(cpsx1DetailA);//        
					wrti[14].setView(true);//字段是否可见       
					wrti[14].setEdit(true);//字段是否可编辑
					
					wrti[15] = new WorkflowRequestTableField();         
					wrti[15].setFieldName("cpsx2");//产品属性2     
					wrti[15].setFieldValue(cpsx2DetailA);//        
					wrti[15].setView(true);//字段是否可见       
					wrti[15].setEdit(true);//字段是否可编辑
					
					WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
					wrtri[0] = new WorkflowRequestTableRecord();        
					wrtri[0].setWorkflowRequestTableFields(wrti);           
					WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
					wmi.setRequestRecords(wrtri);
					//添加工作流id        
					WorkflowBaseInfo wbi = new WorkflowBaseInfo();        
					wbi.setWorkflowId("1003");//workflowid       
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
					wri.setRequestName(SQRXM + "-产前样封样单-" + SQRQ);//流程标题        
					wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
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