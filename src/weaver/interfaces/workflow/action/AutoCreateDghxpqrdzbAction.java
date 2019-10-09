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
 * 订购会新品确认单结束节点自动创建订购会新品确认单-子表<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateDghxpqrdzbAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateDghxpqrdzbAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
	String SQR = "";	//申请人
    String SQRXM = "";	//申请人 姓名 
    String ZHBH = "";	//展会编号
    String SQRQ = "";	//申请日期
    String SM = "";		//说明
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		ZHBH = Util.null2String(mainTable.get("ZHBH"));
		SM = Util.null2String(mainTable.get("SM"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			//按照工厂和责任人进行分类。
			Map<String, List<Map<String,String>>> detailMap = new HashMap<String, List<Map<String,String>>>();
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String gcDetailA = Util.null2String(detailAMap.get("GC"));		// 工厂
				String zrrDetailA = Util.null2String(detailAMap.get("ZRR"));	// 责任人
				if(detailMap.containsKey(gcDetailA+zrrDetailA)){
					detailMap.get(gcDetailA+zrrDetailA).add(detailAMap);
				}else{
					List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
					detailList.add(detailAMap);
					detailMap.put(gcDetailA+zrrDetailA, detailList);
				}
			}
			for(Map.Entry<String,List<Map<String,String>>> entry : detailMap.entrySet()){
				List<Map<String,String>> mxhhList = entry.getValue();
				if (mxhhList != null && mxhhList.size() > 0) {
					Map<String, String> mxhhMapA = mxhhList.get(0);
					String zbDetail = Util.null2String(mxhhMapA.get("ZB"));				//组别
					String gcDetail = Util.null2String(mxhhMapA.get("GC"));				//工厂
					String zrrDetail = Util.null2String(mxhhMapA.get("ZRR"));			//责任人
    				//主字段        
    				WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[8]; //字段信息        
    				wrti[0] = new WorkflowRequestTableField();         
    				wrti[0].setFieldName("sqr");//申请人       
    				wrti[0].setFieldValue(SQR);//        
    				wrti[0].setView(true);//字段是否可见       
    				wrti[0].setEdit(true);//字段是否可编辑
    				wrti[1] = new WorkflowRequestTableField();         
    				wrti[1].setFieldName("zhbh");//展会编号      
    				wrti[1].setFieldValue(ZHBH);//        
    				wrti[1].setView(true);//字段是否可见       
    				wrti[1].setEdit(true);//字段是否可编辑
    				wrti[2] = new WorkflowRequestTableField();         
    				wrti[2].setFieldName("zb");//组别       
    				wrti[2].setFieldValue(zbDetail);//        
    				wrti[2].setView(true);//字段是否可见       
    				wrti[2].setEdit(true);//字段是否可编辑
    				wrti[3] = new WorkflowRequestTableField();         
    				wrti[3].setFieldName("gc");//工厂     
    				wrti[3].setFieldValue(gcDetail);//        
    				wrti[3].setView(true);//字段是否可见       
    				wrti[3].setEdit(true);//字段是否可编辑
    				wrti[4] = new WorkflowRequestTableField();         
    				wrti[4].setFieldName("sqrq");//申请日期       
    				wrti[4].setFieldValue(SQRQ);//        
    				wrti[4].setView(true);//字段是否可见       
    				wrti[4].setEdit(true);//字段是否可编辑
    				wrti[5] = new WorkflowRequestTableField();         
    				wrti[5].setFieldName("zrr");//责任人     
    				wrti[5].setFieldValue(zrrDetail);//        
    				wrti[5].setView(true);//字段是否可见       
    				wrti[5].setEdit(true);//字段是否可编辑
    				wrti[6] = new WorkflowRequestTableField();         
    				wrti[6].setFieldName("sm");//说明     
    				wrti[6].setFieldValue(SM);//        
    				wrti[6].setView(true);//字段是否可见       
    				wrti[6].setEdit(true);//字段是否可编辑
    				wrti[7] = new WorkflowRequestTableField();         
    				wrti[7].setFieldName("zblclj");//主表流程链接    
    				wrti[7].setFieldValue(requestid);//        
    				wrti[7].setView(true);//字段是否可见       
    				wrti[7].setEdit(true);//字段是否可编辑
    				
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
    					String idDetail = Util.null2String(mxhhMap.get("id"));					//明细ID
    					String skuDetail = Util.null2String(mxhhMap.get("SKU"));				//SKU
    					String pmDetail = Util.null2String(mxhhMap.get("PM"));					//品名
    					String dqyktgbDetail = Util.null2String(mxhhMap.get("DQYKTGB"));		//当前已开通国别
    					String[] gb = {"US","CA","UK","DE","FR","IT","ES"};
    					
    					wrti = new WorkflowRequestTableField[74]; //字段信息             
    					wrti[0] = new WorkflowRequestTableField();             
    					wrti[0].setFieldName("sku");//货号             
    					wrti[0].setFieldValue(skuDetail);            
    					wrti[0].setView(true);//字段是否可见              
    					wrti[0].setEdit(true);//字段是否可编辑
    					
    					wrti[1] = new WorkflowRequestTableField();             
    					wrti[1].setFieldName("pm");//品名          
    					wrti[1].setFieldValue(pmDetail);            
    					wrti[1].setView(true);//字段是否可见              
    					wrti[1].setEdit(true);//字段是否可编辑
    					
    					wrti[2] = new WorkflowRequestTableField();             
    					wrti[2].setFieldName("dqyktgb");//国别           
    					wrti[2].setFieldValue(dqyktgbDetail);            
    					wrti[2].setView(true);//字段是否可见              
    					wrti[2].setEdit(true);//字段是否可编辑
    					
    					wrti[3] = new WorkflowRequestTableField();             
    					wrti[3].setFieldName("mxid");//明细ID           
    					wrti[3].setFieldValue(idDetail);            
    					wrti[3].setView(true);//字段是否可见              
    					wrti[3].setEdit(true);//字段是否可编辑
    					
    					int index = 3;
    					for(int j=0; j<gb.length; j++){
    						index++;
    						wrti[index] = new WorkflowRequestTableField();             
    						wrti[index].setFieldName(gb[j]+"MJ");//卖价         
    						wrti[index].setFieldValue(Util.null2String(mxhhMap.get(gb[j]+"MJ")));            
    						wrti[index].setView(true);//字段是否可见              
    						wrti[index].setEdit(true);//字段是否可编辑
    						index++;
    						wrti[index] = new WorkflowRequestTableField();             
    						wrti[index].setFieldName(gb[j]+"CBSL");//初步数量         
    						wrti[index].setFieldValue(Util.null2String(mxhhMap.get(gb[j]+"CBSL")));            
    						wrti[index].setView(true);//字段是否可见              
    						wrti[index].setEdit(true);//字段是否可编辑
    						index++;
    						wrti[index] = new WorkflowRequestTableField();             
    						wrti[index].setFieldName(gb[j]+"JJSX");//季节属性        
    						wrti[index].setFieldValue(Util.null2String(mxhhMap.get(gb[j]+"JJSX")));            
    						wrti[index].setView(true);//字段是否可见              
    						wrti[index].setEdit(true);//字段是否可编辑
    						index++;
    						wrti[index] = new WorkflowRequestTableField();             
    						wrti[index].setFieldName(gb[j]+"JRSX");//节日属性         
    						wrti[index].setFieldValue(Util.null2String(mxhhMap.get(gb[j]+"JRSX")));            
    						wrti[index].setView(true);//字段是否可见              
    						wrti[index].setEdit(true);//字段是否可编辑
    						index++;
    						wrti[index] = new WorkflowRequestTableField();             
    						wrti[index].setFieldName(gb[j]+"CPZT");//产品状态         
    						wrti[index].setFieldValue(Util.null2String(mxhhMap.get(gb[j]+"CPZT")));            
    						wrti[index].setView(true);//字段是否可见              
    						wrti[index].setEdit(true);//字段是否可编辑
    						index++;
    						wrti[index] = new WorkflowRequestTableField();             
    						wrti[index].setFieldName(gb[j]+"XSBZ");//销售备注        
    						wrti[index].setFieldValue(Util.null2String(mxhhMap.get(gb[j]+"XSBZ")));            
    						wrti[index].setView(true);//字段是否可见              
    						wrti[index].setEdit(true);//字段是否可编辑
    						index++;
    						wrti[index] = new WorkflowRequestTableField();             
    						wrti[index].setFieldName(gb[j]+"KFQRZT");//开发确认         
    						wrti[index].setFieldValue(Util.null2String(mxhhMap.get(gb[j]+"KFQRZT")));            
    						wrti[index].setView(true);//字段是否可见              
    						wrti[index].setEdit(true);//字段是否可编辑
    						index++;
    						wrti[index] = new WorkflowRequestTableField();             
    						wrti[index].setFieldName(gb[j]+"DYHH");//对应货号         
    						wrti[index].setFieldValue(Util.null2String(mxhhMap.get(gb[j]+"DYHH")));            
    						wrti[index].setView(true);//字段是否可见              
    						wrti[index].setEdit(true);//字段是否可编辑
    						index++;
    						wrti[index] = new WorkflowRequestTableField();             
    						wrti[index].setFieldName(gb[j]+"KFBZ");//开发备注         
    						wrti[index].setFieldValue(Util.null2String(mxhhMap.get(gb[j]+"KFBZ")));            
    						wrti[index].setView(true);//字段是否可见              
    						wrti[index].setEdit(true);//字段是否可编辑
    						index++;
    						wrti[index] = new WorkflowRequestTableField();             
    						wrti[index].setFieldName(gb[j]+"TP");//图片         
    						wrti[index].setFieldValue(Util.null2String(mxhhMap.get(gb[j]+"TP")));            
    						wrti[index].setView(true);//字段是否可见              
    						wrti[index].setEdit(true);//字段是否可编辑
    					}
    					
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
    				wbi.setWorkflowId("9624");//workflowid       
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
    				wri.setRequestName("订购会新品确认表(子表)-" + SQRXM + "-" + SQRQ);//流程标题        
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