package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
 * 采购合同审批流程<br>
 * 归档, 自动创建采购合同评审单
 * 
 * @author ycj
 *
 */
public class AutoCreateReviewAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateReviewAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    RecordSet rs2 = new RecordSet();
    RecordSet rs3 = new RecordSet();
    RecordSet rs4 = new RecordSet();
    
    String SQR = "";//申请人 
    String SQRXM = "";//申请人姓名
    String CGHTH = "";//采购合同号 
    String GYS = "";//供应商
    String SQRQ = "";//申请日期
    String YJZZ = "";//一级组织
    String EJZZ = "";//二级组织

    String sql = "";
    String sql1 = "";
    String sql2 = "";
    String sql3 = "";
    String sql4 = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		CGHTH = Util.null2String(mainTable.get("SGHTH"));
		GYS = Util.null2String(mainTable.get("GYS"));
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		EJZZ = Util.null2String(mainTable.get("EJZZ"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        if(GYS!=null && !"".equals(GYS)){
        	sql1 = "select id from uf_vendor where vendor_name = '" + GYS + "'";
	        rs1.execute(sql1);
	        if (rs1.next()) {
	        	GYS = Util.null2String(rs1.getString("id"));
	        }
        }
        // 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		List<Map<String, String>> hhList = new ArrayList<Map<String, String>>();
		boolean flag = false;
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String hhDetailA = Util.null2String(detailAMap.get("HH"));			// 货号
				String pmDetailA = Util.null2String(detailAMap.get("PM"));			// 品名
				String thlDetailA = "";												// 退货率
				String sqrqDetailA = "";											// 申请日期
				sql3 = "select thl from uf_returnrate where hh = '" + hhDetailA + "'";
		        rs3.execute(sql3);
		        if (rs3.next()) {
		        	thlDetailA = Util.null2String(rs3.getString("thl"));
		        }
				Double thl = 0d;
				if(!"".equals(thlDetailA)){
					thl = Double.parseDouble(thlDetailA.split("%")[0]);
				}
	            if(hhDetailA!=null && !"".equals(hhDetailA)){
		        	sql2 = "select id from uf_product where segment1 = '" + hhDetailA + "'";
			        rs2.execute(sql2);
			        if (rs2.next()) {
			        	hhDetailA = Util.null2String(rs2.getString("id"));
			        }
		        }
	            //获取产品质量改善台账中的申请日期并且与当前日期比较是否九个月之内。
	            boolean nineMonth = true;
	            sql4 = "select sqrq from uf_CPZLGSGZB where hh = '" + hhDetailA + "'";
		        rs4.execute(sql4);
		        if (rs4.next()) {
		        	sqrqDetailA = Util.null2String(rs4.getString("sqrq"));
		        	if("".equals(sqrqDetailA)){
		        		nineMonth = false;
		        	}else{
		        		 Calendar calendar = Calendar.getInstance();
		                 calendar.setTime(new Date());
		                 calendar.add(Calendar.MONTH, -9);
		                 Long sqrqTime = sdf.parse(sqrqDetailA).getTime();
		                 if(calendar.getTimeInMillis()>sqrqTime){
		                	 nineMonth = false;
		                 }
		        	}
		        }else{
		        	nineMonth = false;
		        }
	            if(thl<7.00 || nineMonth){
	            	continue;
	            }else{
	            	Map<String, String> hhMap = new HashMap<String, String>();
	            	hhMap.put("hh", hhDetailA);
	            	hhMap.put("pm", pmDetailA);
	            	hhMap.put("thl", thlDetailA);
	            	hhList.add(hhMap);
	            	flag = true;
	            }
			}
		}
		if(flag){
			//主字段        
			WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[6]; //字段信息        
			wrti[0] = new WorkflowRequestTableField();         
			wrti[0].setFieldName("fqr");//发起人        
			wrti[0].setFieldValue(SQR);//        
			wrti[0].setView(true);//字段是否可见       
			wrti[0].setEdit(true);//字段是否可编辑
			
			wrti[1] = new WorkflowRequestTableField();         
			wrti[1].setFieldName("yjzz");//一级组织        
			wrti[1].setFieldValue(YJZZ);//        
			wrti[1].setView(true);//字段是否可见       
			wrti[1].setEdit(true);//字段是否可编辑
			
			wrti[2] = new WorkflowRequestTableField();         
			wrti[2].setFieldName("cghth");//采购合同号        
			wrti[2].setFieldValue(CGHTH);//        
			wrti[2].setView(true);//字段是否可见       
			wrti[2].setEdit(true);//字段是否可编辑
			
			wrti[3] = new WorkflowRequestTableField();         
			wrti[3].setFieldName("gys");//供应商        
			wrti[3].setFieldValue(GYS);//        
			wrti[3].setView(true);//字段是否可见       
			wrti[3].setEdit(true);//字段是否可编辑
			
			wrti[4] = new WorkflowRequestTableField();         
			wrti[4].setFieldName("psrq");//评审日期       
			wrti[4].setFieldValue(SQRQ);//        
			wrti[4].setView(true);//字段是否可见       
			wrti[4].setEdit(true);//字段是否可编辑
			
			wrti[5] = new WorkflowRequestTableField();         
			wrti[5].setFieldName("ejzz");//二级组织       
			wrti[5].setFieldValue(EJZZ);//        
			wrti[5].setView(true);//字段是否可见       
			wrti[5].setEdit(true);//字段是否可编辑
			
			WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
			wrtri[0] = new WorkflowRequestTableRecord();        
			wrtri[0].setWorkflowRequestTableFields(wrti);           
			WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
			wmi.setRequestRecords(wrtri);
			int detailrows = hhList.size() ;//添加指定条数明细        
			//添加明细数据       
			wrtri = new WorkflowRequestTableRecord[detailrows];
			//添加指定条数行明细数据        
			for(int i = 0 ; i < detailrows ; i++){
				Map<String, String> hhMap = hhList.get(i);
				String hh = hhMap.get("hh");
				String cppm = hhMap.get("pm");
				String thl = hhMap.get("thl");
				//每行明细对应的字段             
				wrti = new WorkflowRequestTableField[4]; //字段信息             
				wrti[0] = new WorkflowRequestTableField();             
				wrti[0].setFieldName("hh");//货号             
				wrti[0].setFieldValue(hh);            
				wrti[0].setView(true);//字段是否可见              
				wrti[0].setEdit(true);//字段是否可编辑
				
				wrti[1] = new WorkflowRequestTableField();             
				wrti[1].setFieldName("cppm");//产品品名            
				wrti[1].setFieldValue(cppm);            
				wrti[1].setView(true);//字段是否可见              
				wrti[1].setEdit(true);//字段是否可编辑
				
				wrti[2] = new WorkflowRequestTableField();             
				wrti[2].setFieldName("thl");//退货率            
				wrti[2].setFieldValue(thl);            
				wrti[2].setView(true);//字段是否可见              
				wrti[2].setEdit(true);//字段是否可编辑
				
				wrti[3] = new WorkflowRequestTableField();             
				wrti[3].setFieldName("psxm");//评审项目           
				wrti[3].setFieldValue("5");            
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
			wbi.setWorkflowId("682");//workflowid       
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
			wri.setRequestName("采购合同评审单-" + SQRXM + "-" + SQRQ);//流程标题        
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