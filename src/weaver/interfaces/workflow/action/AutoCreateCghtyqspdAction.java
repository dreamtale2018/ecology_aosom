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
 * 采购合同取消申请流程结束自动创建采购合同延期索赔单<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateCghtyqspdAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateCghtyqspdAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
	String SQR = "";	//申请人
	String SQRXM = "";	//申请姓名
	String GYS = "";	//供应商
    String SQRQ = "";	//申请日期 
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		GYS = Util.null2String(mainTable.get("GYS"));
		SQRQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		//主字段        
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[4]; //字段信息        
		wrti[0] = new WorkflowRequestTableField();         
		wrti[0].setFieldName("cgy");//采购员     
		wrti[0].setFieldValue(SQR);//        
		wrti[0].setView(true);//字段是否可见       
		wrti[0].setEdit(true);//字段是否可编辑
		wrti[1] = new WorkflowRequestTableField();         
		wrti[1].setFieldName("zdrq");//制单日期     
		wrti[1].setFieldValue(SQRQ);//        
		wrti[1].setView(true);//字段是否可见       
		wrti[1].setEdit(true);//字段是否可编辑
		wrti[2] = new WorkflowRequestTableField();         
		wrti[2].setFieldName("gys");//供应商   
		wrti[2].setFieldValue(GYS);//        
		wrti[2].setView(true);//字段是否可见       
		wrti[2].setEdit(true);//字段是否可编辑
		wrti[3] = new WorkflowRequestTableField();         
		wrti[3].setFieldName("cghtqxlc");//采购合同取消流程   
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
				String qxyyDetailA = Util.null2String(detailAMap.get("QXYY"));			//取消原因
				if("0".equals(qxyyDetailA)){
					detailList.add(detailAList.get(i));
				}
			}
			int detailrows = detailList.size() ;//添加指定条数明细  
			if(detailrows > 0){
				//添加明细数据  
				wrtri = new WorkflowRequestTableRecord[detailrows];
				for (int i = 0; i < detailrows; i++) {
					Map<String, String> detailMap = detailList.get(i);
					String gxhthDetail = Util.null2String(detailMap.get("GXHTH"));		//购销合同号
					String hanghDetail = Util.null2String(detailMap.get("HANGH"));		//行号
					String hhwbDetail = Util.null2String(detailMap.get("HHWB"));		//货号
					String cppmDetail = Util.null2String(detailMap.get("CPPM"));		//产品品名
					String jhjhrDetail = Util.null2String(detailMap.get("JHJHR"));		//计划交货日
					String qxslDetail = Util.null2String(detailMap.get("QXSL"));		//取消数量
					String cgdjDetail = Util.null2String(detailMap.get("CGDJ"));		//采购单价
					String bzDetail = Util.null2String(detailMap.get("BZ"));			//币种
					String qxjeDetail = Util.null2String(detailMap.get("QXJE"));		//取消金额
					wrti = new WorkflowRequestTableField[10]; //字段信息             
					wrti[0] = new WorkflowRequestTableField();             
					wrti[0].setFieldName("gxhth");//购销合同号            
					wrti[0].setFieldValue(gxhthDetail);            
					wrti[0].setView(true);//字段是否可见              
					wrti[0].setEdit(true);//字段是否可编辑
					wrti[1] = new WorkflowRequestTableField();             
					wrti[1].setFieldName("hangh");//行号         
					wrti[1].setFieldValue(hanghDetail);            
					wrti[1].setView(true);//字段是否可见              
					wrti[1].setEdit(true);//字段是否可编辑
					wrti[2] = new WorkflowRequestTableField();             
					wrti[2].setFieldName("hh");//货号          
					wrti[2].setFieldValue(hhwbDetail);            
					wrti[2].setView(true);//字段是否可见              
					wrti[2].setEdit(true);//字段是否可编辑
					wrti[3] = new WorkflowRequestTableField();             
					wrti[3].setFieldName("cppm");//产品品名         
					wrti[3].setFieldValue(cppmDetail);            
					wrti[3].setView(true);//字段是否可见              
					wrti[3].setEdit(true);//字段是否可编辑
					wrti[4] = new WorkflowRequestTableField();             
					wrti[4].setFieldName("jhjhrq");//计划交货日期        
					wrti[4].setFieldValue(jhjhrDetail);            
					wrti[4].setView(true);//字段是否可见              
					wrti[4].setEdit(true);//字段是否可编辑
					wrti[5] = new WorkflowRequestTableField();             
					wrti[5].setFieldName("yqsl");//延期数量        
					wrti[5].setFieldValue(qxslDetail);            
					wrti[5].setView(true);//字段是否可见              
					wrti[5].setEdit(true);//字段是否可编辑
					wrti[6] = new WorkflowRequestTableField();             
					wrti[6].setFieldName("dj");//单价        
					wrti[6].setFieldValue(cgdjDetail);            
					wrti[6].setView(true);//字段是否可见              
					wrti[6].setEdit(true);//字段是否可编辑
					wrti[7] = new WorkflowRequestTableField();             
					wrti[7].setFieldName("bz");//币种        
					wrti[7].setFieldValue(bzDetail);            
					wrti[7].setView(true);//字段是否可见              
					wrti[7].setEdit(true);//字段是否可编辑
					wrti[8] = new WorkflowRequestTableField();             
					wrti[8].setFieldName("yqje");//延期金额        
					wrti[8].setFieldValue(qxjeDetail);            
					wrti[8].setView(true);//字段是否可见              
					wrti[8].setEdit(true);//字段是否可编辑
					wrti[9] = new WorkflowRequestTableField();             
					wrti[9].setFieldName("zt");//状态       
					wrti[9].setFieldValue("0");            
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
				wbi.setWorkflowId("2423");//workflowid       
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
				wri.setRequestName("采购合同延期索赔单-" + SQRXM + "-" + SQRQ);//流程标题        
				wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
				wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
				wri.setWorkflowBaseInfo(wbi);        
				WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
				String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(SQR));
				sql = "update formtable_main_147 set yqsplc = '" + newRequestid + "' where requestid = '" + requestid + "'";
				rs.execute(sql);
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