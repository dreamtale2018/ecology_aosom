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
 * 硬件/耗材/办公用品采购单流程结束自动创建硬件/耗材/办公用品采购单-子表<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateBgypcgzbAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateBgypcgzbAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	String requestid = request.getRequestid();

	RecordSet rs = new RecordSet();
	  
    String SQR = "";		//申请人 
    String SQRXM = "";		//申请人姓名
    String ZJE = "";		//总金额 
    String LB = "";			//类别
    String SQRQ = "";		//申请日期
    String WPSFRTZ = "";	//物品是否入台账
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		ZJE = Util.null2String(mainTable.get("ZJE"));
		LB = Util.null2String(mainTable.get("LB"));
		WPSFRTZ = Util.null2String(mainTable.get("WPSFRTZ"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        // 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			Map<String, List<Map<String,String>>> detailMap = new HashMap<String, List<Map<String,String>>>();
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String dycgDetailA = Util.null2String(detailAMap.get("GYS"));		// 供应商
				if(detailMap.containsKey(dycgDetailA)){
					detailMap.get(dycgDetailA).add(detailAMap);
				}else{
					List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
					detailList.add(detailAMap);
					detailMap.put(dycgDetailA, detailList);
				}
			}
			for(Map.Entry<String,List<Map<String,String>>> entry : detailMap.entrySet()){
				List<Map<String,String>> mxhhList = entry.getValue();
				if (mxhhList != null && mxhhList.size() > 0) {
					//主字段        
					WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[6]; //字段信息        
					wrti[0] = new WorkflowRequestTableField();         
					wrti[0].setFieldName("sqr");//申请人       
					wrti[0].setFieldValue(SQR);//        
					wrti[0].setView(true);//字段是否可见       
					wrti[0].setEdit(true);//字段是否可编辑
					wrti[1] = new WorkflowRequestTableField();         
					wrti[1].setFieldName("sqrq");//申请日期     
					wrti[1].setFieldValue(SQRQ);//        
					wrti[1].setView(true);//字段是否可见       
					wrti[1].setEdit(true);//字段是否可编辑
					wrti[2] = new WorkflowRequestTableField();         
					wrti[2].setFieldName("zje");//总金额   
					wrti[2].setFieldValue(ZJE);//        
					wrti[2].setView(true);//字段是否可见       
					wrti[2].setEdit(true);//字段是否可编辑
					wrti[3] = new WorkflowRequestTableField();         
					wrti[3].setFieldName("lb");//类别   
					wrti[3].setFieldValue(LB);//        
					wrti[3].setView(true);//字段是否可见       
					wrti[3].setEdit(true);//字段是否可编辑
					wrti[4] = new WorkflowRequestTableField();         
					wrti[4].setFieldName("zlclj");//主流程链接   
					wrti[4].setFieldValue(requestid);//        
					wrti[4].setView(true);//字段是否可见       
					wrti[4].setEdit(true);//字段是否可编辑
					wrti[5] = new WorkflowRequestTableField();         
					wrti[5].setFieldName("wpsfrtz");//物品是否入台账  
					wrti[5].setFieldValue(WPSFRTZ);//        
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
					for (int i = 0; i < mxhhList.size(); i++) {
						Map<String, String> mxhhMap = mxhhList.get(i);
						String mxhhDetail = Util.null2String(mxhhMap.get("id"));		// 行号
						String wlbmDetail = Util.null2String(mxhhMap.get("WLBM"));		// 物料编码
						String pmDetail = Util.null2String(mxhhMap.get("PM"));			// 品名
						String xhDetail = Util.null2String(mxhhMap.get("XH"));			// 规格型号
						String bccgslDetail = Util.null2String(mxhhMap.get("BCCGSL"));	// 本次采购数量
						String dwDetail = Util.null2String(mxhhMap.get("DW"));			// 单位
						String djDetail = Util.null2String(mxhhMap.get("DJ"));			// 单价
						String jeDetail = Util.null2String(mxhhMap.get("JE"));			// 金额
						String gysDetail = Util.null2String(mxhhMap.get("GYS"));		// 供应商
						String kykcDetail = Util.null2String(mxhhMap.get("KYKC"));		// 可用库存
						String sjdhslDetail = Util.null2String(mxhhMap.get("SJDHSL"));	// 实际到货数量
						String bzDetail = Util.null2String(mxhhMap.get("BZ"));			// 备注
						
						wrti = new WorkflowRequestTableField[12]; //字段信息             
						wrti[0] = new WorkflowRequestTableField();             
						wrti[0].setFieldName("mxhid");//明细行ID            
						wrti[0].setFieldValue(mxhhDetail);            
						wrti[0].setView(true);//字段是否可见              
						wrti[0].setEdit(true);//字段是否可编辑
						wrti[1] = new WorkflowRequestTableField();             
						wrti[1].setFieldName("wlbm");//物料编码         
						wrti[1].setFieldValue(wlbmDetail);            
						wrti[1].setView(true);//字段是否可见              
						wrti[1].setEdit(true);//字段是否可编辑
						wrti[2] = new WorkflowRequestTableField();             
						wrti[2].setFieldName("pm");//品名         
						wrti[2].setFieldValue(pmDetail);            
						wrti[2].setView(true);//字段是否可见              
						wrti[2].setEdit(true);//字段是否可编辑
						wrti[3] = new WorkflowRequestTableField();             
						wrti[3].setFieldName("xh");//规格型号         
						wrti[3].setFieldValue(xhDetail);            
						wrti[3].setView(true);//字段是否可见              
						wrti[3].setEdit(true);//字段是否可编辑
						wrti[4] = new WorkflowRequestTableField();             
						wrti[4].setFieldName("bccgsl");//本次采购数量       
						wrti[4].setFieldValue(bccgslDetail);            
						wrti[4].setView(true);//字段是否可见              
						wrti[4].setEdit(true);//字段是否可编辑
						wrti[5] = new WorkflowRequestTableField();             
						wrti[5].setFieldName("dw");//单位        
						wrti[5].setFieldValue(dwDetail);            
						wrti[5].setView(true);//字段是否可见              
						wrti[5].setEdit(true);//字段是否可编辑
						wrti[6] = new WorkflowRequestTableField();             
						wrti[6].setFieldName("dj");//单价        
						wrti[6].setFieldValue(djDetail);            
						wrti[6].setView(true);//字段是否可见              
						wrti[6].setEdit(true);//字段是否可编辑
						wrti[7] = new WorkflowRequestTableField();             
						wrti[7].setFieldName("je");//金额        
						wrti[7].setFieldValue(jeDetail);            
						wrti[7].setView(true);//字段是否可见              
						wrti[7].setEdit(true);//字段是否可编辑
						wrti[8] = new WorkflowRequestTableField();             
						wrti[8].setFieldName("gys");//供应商        
						wrti[8].setFieldValue(gysDetail);            
						wrti[8].setView(true);//字段是否可见              
						wrti[8].setEdit(true);//字段是否可编辑
						wrti[9] = new WorkflowRequestTableField();             
						wrti[9].setFieldName("kykc");//可用库存      
						wrti[9].setFieldValue(kykcDetail);            
						wrti[9].setView(true);//字段是否可见              
						wrti[9].setEdit(true);//字段是否可编辑
						wrti[10] = new WorkflowRequestTableField();             
						wrti[10].setFieldName("sjdhsl");//实际到货数量    
						wrti[10].setFieldValue(sjdhslDetail);            
						wrti[10].setView(true);//字段是否可见              
						wrti[10].setEdit(true);//字段是否可编辑
						wrti[11] = new WorkflowRequestTableField();             
						wrti[11].setFieldName("bz");//备注      
						wrti[11].setFieldValue(bzDetail);            
						wrti[11].setView(true);//字段是否可见              
						wrti[11].setEdit(true);//字段是否可编辑
						
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
					wbi.setWorkflowId("7125");//workflowid       
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
					wri.setRequestName("硬件/耗材/办公用品采购单-子表-" + SQRXM + "-" + SQRQ);//流程标题        
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