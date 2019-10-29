package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * 订购会新品确认单-子表结束节点自动创建产品选型表<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateCpxxDghxpqrdzbAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateCpxxDghxpqrdzbAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
	String SQR = "";							//申请人
    String SQRXM = "";							//申请人 姓名 
    String ZHBH = "";							//展会编号
    String GC = "";								//工厂
    String SQRQ = "";							//申请日期
    Set<String> gbSet = new HashSet<String>(); 	//国别
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("ZRR"));
		ZHBH = Util.null2String(mainTable.get("ZHBH"));
		GC = Util.null2String(mainTable.get("GC"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			//按照品名进行分类。
			Map<String, List<Map<String,String>>> detailMap = new HashMap<String, List<Map<String,String>>>();
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
			    String BS = "";													// 标识
				String idDetailA = Util.null2String(detailAMap.get("id"));		// 明细id
				String pmDetailA = Util.null2String(detailAMap.get("PM"));		// 品名
				boolean flag = false;											// 是否包含市场新增
				//开发确认为市场新增的。
				String[] gb = {"US","CA","UK","DE","FR","IT","ES"};
				for(int j=0; j<gb.length; j++){
					String kfqrztDetailA = Util.null2String(detailAMap.get(gb[j]+"KFQRZT"));																
																				// 状态
					if("3".equals(kfqrztDetailA)){
						BS += idDetailA + "-" + gb[j] + ";";
						gbSet.add(gb[j]);
						flag = true;
					}
				}
				detailAMap.put("BS", BS);
				if(flag){
					if(detailMap.containsKey(pmDetailA)){
						detailMap.get(pmDetailA).add(detailAMap);
					}else{
						List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
						detailList.add(detailAMap);
						detailMap.put(pmDetailA, detailList);
					}
				}
			}
			for(Map.Entry<String,List<Map<String,String>>> entry : detailMap.entrySet()){
				List<Map<String,String>> mxhhList = entry.getValue();
				if (mxhhList != null && mxhhList.size() > 0) {
					//主字段        
					WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[5+gbSet.size()]; //字段信息        
					wrti[0] = new WorkflowRequestTableField();         
					wrti[0].setFieldName("kf");//开发       
					wrti[0].setFieldValue(SQR);//        
					wrti[0].setView(true);//字段是否可见       
					wrti[0].setEdit(true);//字段是否可编辑
					wrti[1] = new WorkflowRequestTableField();         
					wrti[1].setFieldName("sqrq");//申请日期      
					wrti[1].setFieldValue(SQRQ);//        
					wrti[1].setView(true);//字段是否可见       
					wrti[1].setEdit(true);//字段是否可编辑
					wrti[2] = new WorkflowRequestTableField();         
					wrti[2].setFieldName("tjll");//推荐来源       
					wrti[2].setFieldValue("7");//        
					wrti[2].setView(true);//字段是否可见       
					wrti[2].setEdit(true);//字段是否可编辑
					wrti[3] = new WorkflowRequestTableField();         
					wrti[3].setFieldName("gjc");//关键词    
					wrti[3].setFieldValue(GC+ZHBH);//        
					wrti[3].setView(true);//字段是否可见       
					wrti[3].setEdit(true);//字段是否可编辑
					wrti[4] = new WorkflowRequestTableField();         
					wrti[4].setFieldName("xpqrlc");//订购会新品确认表(子表)  
					wrti[4].setFieldValue(requestid);//        
					wrti[4].setView(true);//字段是否可见       
					wrti[4].setEdit(true);//字段是否可编辑
					Object[] gbArr = (Object[]) gbSet.toArray();
					for(int i=0;i<gbArr.length;i++){
						wrti[i+5] = new WorkflowRequestTableField();         
						wrti[i+5].setFieldName(gbArr[i].toString());//推荐国别  
						wrti[i+5].setFieldValue("1");//        
						wrti[i+5].setView(true);//字段是否可见       
						wrti[i+5].setEdit(true);//字段是否可编辑
					}
					
					WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
					wrtri[0] = new WorkflowRequestTableRecord();        
					wrtri[0].setWorkflowRequestTableFields(wrti);           
					WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
					wmi.setRequestRecords(wrtri);
					
					int detailrows = mxhhList.size() ;//添加指定条数明细  
					//添加明细数据  
				    List<String> dylcList = new ArrayList<String>();							//对应流程
					wrtri = new WorkflowRequestTableRecord[detailrows];
					for (int k = 0; k < detailrows; k++) {
						Map<String, String> mxhhMap = mxhhList.get(k);
						String skuDetailA = Util.null2String(mxhhMap.get("SKU"));				//SKU
						String pmDetailA = Util.null2String(mxhhMap.get("PM"));					//品名
						String bsDetailA = Util.null2String(mxhhMap.get("BS"));					//标识
						String[] bsArr = bsDetailA.split(";");
						for(String bs : bsArr){
							dylcList.add(bs);
						}
						wrti = new WorkflowRequestTableField[2]; //字段信息             
						
						wrti[0] = new WorkflowRequestTableField();             
						wrti[0].setFieldName("cpjms");//产品及描述        
						wrti[0].setFieldValue(skuDetailA+pmDetailA);            
						wrti[0].setView(true);//字段是否可见              
						wrti[0].setEdit(true);//字段是否可编辑
						
						wrti[1] = new WorkflowRequestTableField();         
						wrti[1].setFieldName("bs");//标识  
						wrti[1].setFieldValue(bsDetailA);//        
						wrti[1].setView(true);//字段是否可见       
						wrti[1].setEdit(true);//字段是否可编辑
						
						wrtri[k] = new WorkflowRequestTableRecord();
						wrtri[k].setWorkflowRequestTableFields(wrti);
					}
					//添加到明细表中        
					WorkflowDetailTableInfo WorkflowDetailTableInfo[] = new WorkflowDetailTableInfo[1];
					//指定明细表的个数，多个明细表指定多个，顺序按照明细的顺序        
					WorkflowDetailTableInfo[0] = new WorkflowDetailTableInfo();        
					WorkflowDetailTableInfo[0].setWorkflowRequestTableRecords(wrtri);
					//添加工作流id        
					WorkflowBaseInfo wbi = new WorkflowBaseInfo();        
					wbi.setWorkflowId("2163");//workflowid       
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
					wri.setRequestName("产品选型表-" + SQRXM + "-" + SQRQ);//流程标题        
					wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
					wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
					wri.setWorkflowBaseInfo(wbi);        
					WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
					String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(SQR));
					for(int l=0;l<dylcList.size();l++){
						String dylc = Util.null2String(dylcList.get(l));
						if(!"".equals(dylc) && dylc.indexOf("-")!=-1){
							String[] dylcArr =  dylc.split("-");
							sql = "update formtable_main_296_dt1 set " + dylcArr[1] + "DYLC = '" + newRequestid + 
									"' where id = '" + dylcArr[0] + "'";
					        rs.execute(sql); 
						}
					}
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