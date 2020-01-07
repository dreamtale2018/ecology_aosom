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
 * 合同尾数清理表流程结束自动创建采购合同取消申请单<br>
 * 
 * @author jq
 *
 */
public class AutoCreateCghtqxHtwsqlAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateCghtqxHtwsqlAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	String requestid = request.getRequestid();

	RecordSet rs = new RecordSet();
	  
    String BBRQ = "";		//报表日期
    String SQRXM = "";		//申请人姓名
    String GYS = "";		//供应商
    String GDY = "";		//跟单员
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BBRQ = sdf.format(new Date());
        // 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			Map<String, List<Map<String,String>>> detailMap = new HashMap<String, List<Map<String,String>>>();
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String gysDetailA = Util.null2String(detailAMap.get("GYS"));			// 供应商
				String sfcflcDetailA = Util.null2String(detailAMap.get("CFCGHTQXLC"));	// 是否触发流程
				if("0".equals(sfcflcDetailA)){
					if(detailMap.containsKey(gysDetailA)){
						detailMap.get(gysDetailA).add(detailAMap);
					}else{
						List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
						detailList.add(detailAMap);
						detailMap.put(gysDetailA, detailList);
					}
				}
			}
			for(Map.Entry<String,List<Map<String,String>>> entry : detailMap.entrySet()){
				List<Map<String,String>> mxhhList = entry.getValue();
				if (mxhhList != null && mxhhList.size() > 0) {
					Map<String, String> mxMap = mxhhList.get(0);
					GYS = Util.null2String(mxMap.get("GYS"));							// 供应商
					GDY = Util.null2String(mxMap.get("GDY"));	   				     	// 跟单员
					//主字段        
					WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[5]; //字段信息        
					wrti[0] = new WorkflowRequestTableField();         
					wrti[0].setFieldName("sqr");//申请人       
					wrti[0].setFieldValue(GDY);//        
					wrti[0].setView(true);//字段是否可见       
					wrti[0].setEdit(true);//字段是否可编辑
					wrti[1] = new WorkflowRequestTableField();         
					wrti[1].setFieldName("bbrq");//报表日期     
					wrti[1].setFieldValue(BBRQ);//        
					wrti[1].setView(true);//字段是否可见       
					wrti[1].setEdit(true);//字段是否可编辑 
					wrti[2] = new WorkflowRequestTableField();         
					wrti[2].setFieldName("htwsqlblc");//合同尾数清理表流程   
					wrti[2].setFieldValue(requestid);//        
					wrti[2].setView(true);//字段是否可见       
					wrti[2].setEdit(true);//字段是否可编辑
					wrti[3] = new WorkflowRequestTableField();         
					wrti[3].setFieldName("gys");//供应商  
					wrti[3].setFieldValue(GYS);//        
					wrti[3].setView(true);//字段是否可见       
					wrti[3].setEdit(true);//字段是否可编辑
					wrti[4] = new WorkflowRequestTableField();         
					wrti[4].setFieldName("gd");//跟单员 
					wrti[4].setFieldValue(GDY);//        
					wrti[4].setView(true);//字段是否可见       
					wrti[4].setEdit(true);//字段是否可编辑
					
					WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
					wrtri[0] = new WorkflowRequestTableRecord();        
					wrtri[0].setWorkflowRequestTableFields(wrti);           
					WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
					wmi.setRequestRecords(wrtri);
					int detailrows = mxhhList.size() ;//添加指定条数明细      
					List<String> hanghList = new ArrayList<String>();
					//添加明细数据       
					wrtri = new WorkflowRequestTableRecord[detailrows];
					for (int i = 0; i < mxhhList.size(); i++) {
						Map<String, String> mxhhMap = mxhhList.get(i);
						String mxhhDetail = Util.null2String(mxhhMap.get("id"));		// 行号
						hanghList.add(mxhhDetail);
						String hhDetail = Util.null2String(mxhhMap.get("HH"));		    // 货号
						String cppmDetail = Util.null2String(mxhhMap.get("CPPM"));		// 产品品名
						String gxhthdetail = Util.null2String(mxhhMap.get("GXHTH"));	// 购销合同号
						String hh1Detail = Util.null2String(mxhhMap.get("HH1"));		// 行号1
						String gbDetail = Util.null2String(mxhhMap.get("GB"));			// 国别
						String kcslDetail = Util.null2String(mxhhMap.get("KCSL"));		// 库存数量
						String chbzDetail = Util.null2String(mxhhMap.get("CHBZ"));		// 采购币种
						String cgdjDetail = Util.null2String(mxhhMap.get("CGDJ"));		// 采购单价
						String cgjeDetail = Util.null2String(mxhhMap.get("CGJE"));	    // 采购金额
						String jhjhrqDetail = Util.null2String(mxhhMap.get("JHJHRQ"));	// 计划交货日期
						
						wrti = new WorkflowRequestTableField[11]; //字段信息             
						wrti[0] = new WorkflowRequestTableField();             
						wrti[0].setFieldName("mxhid");//明细行ID            
						wrti[0].setFieldValue(mxhhDetail);            
						wrti[0].setView(true);//字段是否可见              
						wrti[0].setEdit(true);//字段是否可编辑
						wrti[1] = new WorkflowRequestTableField();             
						wrti[1].setFieldName("hh");//货号         
						wrti[1].setFieldValue(hhDetail);            
						wrti[1].setView(true);//字段是否可见              
						wrti[1].setEdit(true);//字段是否可编辑
						wrti[2] = new WorkflowRequestTableField();             
						wrti[2].setFieldName("cppm");//产品品名       
						wrti[2].setFieldValue(cppmDetail);            
						wrti[2].setView(true);//字段是否可见              
						wrti[2].setEdit(true);//字段是否可编辑
						wrti[3] = new WorkflowRequestTableField();             
						wrti[3].setFieldName("jhjhrq");//计划交货日期   
						wrti[3].setFieldValue(jhjhrqDetail);            
						wrti[3].setView(true);//字段是否可见              
						wrti[3].setEdit(true);//字段是否可编辑
						wrti[4] = new WorkflowRequestTableField();             
						wrti[4].setFieldName("gxhth");// 购销合同号       
						wrti[4].setFieldValue(gxhthdetail);            
						wrti[4].setView(true);//字段是否可见              
						wrti[4].setEdit(true);//字段是否可编辑
						wrti[5] = new WorkflowRequestTableField();             
						wrti[5].setFieldName("hh1");// 行号1        
						wrti[5].setFieldValue(hh1Detail);            
						wrti[5].setView(true);//字段是否可见              
						wrti[5].setEdit(true);//字段是否可编辑
						wrti[6] = new WorkflowRequestTableField();             
						wrti[6].setFieldName("gb");//国别     
						wrti[6].setFieldValue(gbDetail);            
						wrti[6].setView(true);//字段是否可见              
						wrti[6].setEdit(true);//字段是否可编辑
						wrti[7] = new WorkflowRequestTableField();             
						wrti[7].setFieldName("kcsl");//库存数量      
						wrti[7].setFieldValue(kcslDetail);            
						wrti[7].setView(true);//字段是否可见              
						wrti[7].setEdit(true);//字段是否可编辑
						wrti[8] = new WorkflowRequestTableField();             
						wrti[8].setFieldName("chbz");//采购币种      
						wrti[8].setFieldValue(chbzDetail);            
						wrti[8].setView(true);//字段是否可见              
						wrti[8].setEdit(true);//字段是否可编辑
						wrti[9] = new WorkflowRequestTableField();             
						wrti[9].setFieldName("cgdj");//采购单价     
						wrti[9].setFieldValue(cgdjDetail);            
						wrti[9].setView(true);//字段是否可见              
						wrti[9].setEdit(true);//字段是否可编辑
						wrti[10] = new WorkflowRequestTableField();             
						wrti[10].setFieldName("cgje");//采购金额    
						wrti[10].setFieldValue(cgjeDetail);            
						wrti[10].setView(true);//字段是否可见              
						wrti[10].setEdit(true);//字段是否可编辑
						
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
					wbi.setWorkflowId("1943");//workflowid       
					WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息            
					wri.setCreatorId(GDY);//创建人id        
					wri.setRequestLevel("0");//0 正常，1重要，2紧急
					sql = "select lastname from hrmresource where id = '" + GDY + "'";
					rs.execute(sql);
					if(rs.next()){
						SQRXM = Util.null2String(rs.getString("lastname"));
						if(SQRXM.indexOf("`~`7")!=-1 && SQRXM.indexOf("`~`8")!=-1){
							SQRXM = SQRXM.split("`~`7")[1].split("`~`8")[0].trim(); 
						}else{
							SQRXM = SQRXM.split("`~`7")[0].split("`~`8")[0].trim();
						}
					}
					wri.setRequestName("采购合同取消申请单-" + SQRXM + "-" + BBRQ);//流程标题        
					wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
					wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
					wri.setWorkflowBaseInfo(wbi);        
					WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
					String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(GDY));
					this.logger.error("newRequestid:"+newRequestid);
					for(String hangh : hanghList){
						sql = "update formtable_main_320_dt1 set LC = '" + newRequestid + "' where id = '" + hangh + "'";
						rs.execute(sql);
					}
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