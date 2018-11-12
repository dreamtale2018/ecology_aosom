package weaver.interfaces.workflow.action;

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
 * 产品投诉表责任人改善节点自动创建工厂索赔单(问题产品)<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateGcspdAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateGcspdAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
	String ZDR = "";	//制单人
	String ZDRXM = "";	//制单人姓名
    String ZDRQ = "";	//制单日期 
    String SFHWSP = "";	//是否海外索赔
    String HWSPJE = "";	//海外索赔金额
    String TYSPJE = "";	//同意索赔金额
    String BBHW = "";	//币别海外
    String SPLY = "";	//索赔来源
    String ZT = "";		//状态
    String ZTHW = "";	//状态海外
    String GB = "";		//国别
    String YWST = "";	//业务实体
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		ZDR = Util.null2String(mainTable.get("FQR"));
		ZDRQ = Util.null2String(mainTable.get("FQRQ"));
		SFHWSP = Util.null2String(mainTable.get("SFHWSP"));
		HWSPJE = Util.null2String(mainTable.get("HWSPJE"));
		TYSPJE = Util.null2String(mainTable.get("TYSPJE"));
		BBHW = Util.null2String(mainTable.get("BB"));
		SPLY = Util.null2String(mainTable.get("SPLY"));
		ZT = Util.null2String(mainTable.get("ZTGNSP"));
		ZTHW = Util.null2String(mainTable.get("ZTHWSP"));
		YWST = Util.null2String(mainTable.get("YWST"));
		GB = Util.null2String(mainTable.get("GN"));
		//国别信息选择框转换成文本。
		if("0".equals(GB)){
			GB = "US";
		}else if("1".equals(GB)){
			GB = "CA";
		}else if("2".equals(GB)){
			GB = "UK";
		}else if("3".equals(GB)){
			GB = "DE";
		}else if("4".equals(GB)){
			GB = "FR";
		}else if("5".equals(GB)){
			GB = "IT";
		}else if("6".equals(GB)){
			GB = "ES";
		}
		
		//主字段        
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[11]; //字段信息        
		wrti[0] = new WorkflowRequestTableField();         
		wrti[0].setFieldName("zdr");//制单人       
		wrti[0].setFieldValue(ZDR);//        
		wrti[0].setView(true);//字段是否可见       
		wrti[0].setEdit(true);//字段是否可编辑
		wrti[1] = new WorkflowRequestTableField();         
		wrti[1].setFieldName("zdrq");//制单日期     
		wrti[1].setFieldValue(ZDRQ);//        
		wrti[1].setView(true);//字段是否可见       
		wrti[1].setEdit(true);//字段是否可编辑
		wrti[2] = new WorkflowRequestTableField();         
		wrti[2].setFieldName("sfhwsp");//是否海外索赔       
		wrti[2].setFieldValue(SFHWSP);//        
		wrti[2].setView(true);//字段是否可见       
		wrti[2].setEdit(true);//字段是否可编辑
		wrti[3] = new WorkflowRequestTableField();         
		wrti[3].setFieldName("hwspje");//海外索赔金额       
		wrti[3].setFieldValue(HWSPJE);//        
		wrti[3].setView(true);//字段是否可见       
		wrti[3].setEdit(true);//字段是否可编辑
		wrti[4] = new WorkflowRequestTableField();         
		wrti[4].setFieldName("tyspje");//同意索赔金额      
		wrti[4].setFieldValue(TYSPJE);//        
		wrti[4].setView(true);//字段是否可见       
		wrti[4].setEdit(true);//字段是否可编辑
		wrti[5] = new WorkflowRequestTableField();         
		wrti[5].setFieldName("bbhw");//币别海外     
		wrti[5].setFieldValue(BBHW);//        
		wrti[5].setView(true);//字段是否可见       
		wrti[5].setEdit(true);//字段是否可编辑
		wrti[6] = new WorkflowRequestTableField();         
		wrti[6].setFieldName("sply");//索赔来源       
		wrti[6].setFieldValue(SPLY);//        
		wrti[6].setView(true);//字段是否可见       
		wrti[6].setEdit(true);//字段是否可编辑
		wrti[7] = new WorkflowRequestTableField();         
		wrti[7].setFieldName("zt");//状态     
		wrti[7].setFieldValue(ZT);//        
		wrti[7].setView(true);//字段是否可见       
		wrti[7].setEdit(true);//字段是否可编辑
		wrti[8] = new WorkflowRequestTableField();         
		wrti[8].setFieldName("zthw");//状态海外      
		wrti[8].setFieldValue(ZTHW);//        
		wrti[8].setView(true);//字段是否可见       
		wrti[8].setEdit(true);//字段是否可编辑
		wrti[9] = new WorkflowRequestTableField();         
		wrti[9].setFieldName("hwtslc");//海外投诉流程     
		wrti[9].setFieldValue(requestid);//        
		wrti[9].setView(true);//字段是否可见       
		wrti[9].setEdit(true);//字段是否可编辑
		wrti[10] = new WorkflowRequestTableField();         
		wrti[10].setFieldName("ywst");//业务实体     
		wrti[10].setFieldValue(YWST);//        
		wrti[10].setView(true);//字段是否可见       
		wrti[10].setEdit(true);//字段是否可编辑
		
		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
		wrtri[0] = new WorkflowRequestTableRecord();        
		wrtri[0].setWorkflowRequestTableFields(wrti);           
		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
		wmi.setRequestRecords(wrtri);
        // 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			int detailrows = detailAList.size() ;//添加指定条数明细  
			//添加明细数据  
			wrtri = new WorkflowRequestTableRecord[detailrows];
			for (int i = 0; i < detailrows; i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String hhDetailA = Util.null2String(detailAMap.get("HH"));		//货号
				String pmDetailA = Util.null2String(detailAMap.get("PM"));		//品名
				String hthDetailA = Util.null2String(detailAMap.get("HTH"));	//合同号
				String ckfphDetailA = Util.null2String(detailAMap.get("CKFPH"));//出口发票号
				String slDetailA = Util.null2String(detailAMap.get("SL"));		//数量
				String rkcbDetailA = Util.null2String(detailAMap.get("RKCB"));	//入库成本
				String rujeDetailA = Util.null2String(detailAMap.get("RUJE"));	//入库金额
				String wtDetailA = Util.null2String(detailAMap.get("WT"));		//问题备注
				wrti = new WorkflowRequestTableField[10]; //字段信息             
				wrti[0] = new WorkflowRequestTableField();             
				wrti[0].setFieldName("hh");//货号             
				wrti[0].setFieldValue(hhDetailA);            
				wrti[0].setView(true);//字段是否可见              
				wrti[0].setEdit(true);//字段是否可编辑
				wrti[1] = new WorkflowRequestTableField();             
				wrti[1].setFieldName("pm");//品名          
				wrti[1].setFieldValue(pmDetailA);            
				wrti[1].setView(true);//字段是否可见              
				wrti[1].setEdit(true);//字段是否可编辑
				wrti[2] = new WorkflowRequestTableField();             
				wrti[2].setFieldName("hth");//合同号           
				wrti[2].setFieldValue(hthDetailA);            
				wrti[2].setView(true);//字段是否可见              
				wrti[2].setEdit(true);//字段是否可编辑
				wrti[3] = new WorkflowRequestTableField();             
				wrti[3].setFieldName("ckfph");//出口发票号             
				wrti[3].setFieldValue(ckfphDetailA);            
				wrti[3].setView(true);//字段是否可见              
				wrti[3].setEdit(true);//字段是否可编辑
				wrti[4] = new WorkflowRequestTableField();             
				wrti[4].setFieldName("sl");//数量         
				wrti[4].setFieldValue(slDetailA);            
				wrti[4].setView(true);//字段是否可见              
				wrti[4].setEdit(true);//字段是否可编辑
				wrti[5] = new WorkflowRequestTableField();             
				wrti[5].setFieldName("rkcb");//入库成本          
				wrti[5].setFieldValue(rkcbDetailA);            
				wrti[5].setView(true);//字段是否可见              
				wrti[5].setEdit(true);//字段是否可编辑
				wrti[6] = new WorkflowRequestTableField();             
				wrti[6].setFieldName("rkje");//入库金额             
				wrti[6].setFieldValue(rujeDetailA);            
				wrti[6].setView(true);//字段是否可见              
				wrti[6].setEdit(true);//字段是否可编辑
				wrti[7] = new WorkflowRequestTableField();             
				wrti[7].setFieldName("wtbz");//问题备注         
				wrti[7].setFieldValue(wtDetailA);            
				wrti[7].setView(true);//字段是否可见              
				wrti[7].setEdit(true);//字段是否可编辑
				wrti[8] = new WorkflowRequestTableField();             
				wrti[8].setFieldName("gb");//国别       
				wrti[8].setFieldValue(GB);            
				wrti[8].setView(true);//字段是否可见              
				wrti[8].setEdit(true);//字段是否可编辑
				wrti[9] = new WorkflowRequestTableField();             
				wrti[9].setFieldName("gwch");//海外存货       
				wrti[9].setFieldValue(slDetailA);            
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
			wbi.setWorkflowId("1323");//workflowid       
			WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息            
			wri.setCreatorId(ZDR);//创建人id        
			wri.setRequestLevel("0");//0 正常，1重要，2紧急
			sql = "select lastname from hrmresource where id = '" + ZDR + "'";
			rs.execute(sql);
			if(rs.next()){
				ZDRXM = Util.null2String(rs.getString("lastname"));
				if(ZDRXM.indexOf("`~`7")!=-1 && ZDRXM.indexOf("`~`8")!=-1){
					ZDRXM = ZDRXM.split("`~`7")[1].split("`~`8")[0].trim(); 
				}else{
					ZDRXM = ZDRXM.split("`~`7")[0].split("`~`8")[0].trim();
				}
			}
			wri.setRequestName("工厂索赔单(问题产品)-" + ZDRXM + "-" + ZDRQ);//流程标题        
			wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
			wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
			wri.setWorkflowBaseInfo(wbi);        
			WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
			String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(ZDR));        
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