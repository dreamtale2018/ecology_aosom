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
 * 订购会新品确认单-子表结束节点自动创建国别产品信息变更单<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateGbcpDghxpqrdzbAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateGbcpDghxpqrdzbAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
	String SQR = "";									//申请人
    String SQRXM = "";									//申请人 姓名 
    String SQRQ = "";									//申请日期
    String ZHBH = "";									//展会编号
    List<String> dylcList = new ArrayList<String>();	//对应流程
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("ZRR"));
		ZHBH = Util.null2String(mainTable.get("ZHBH"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				
				//开发确认为扩增国别的。
				String[] gb = {"US","CA","UK","DE","FR","IT","ES"};
				for(int j=0; j<gb.length; j++){
					String kfqrztDetailA = Util.null2String(detailAMap.get(gb[j]+"KFQRZT"));																
																// 状态
					if("1".equals(kfqrztDetailA)){
						Map<String, String> detailMap = new HashMap<String, String>();
						detailMap.put("GB", gb[j]);
						detailMap.put("HH", detailAMap.get("SKU"));
						detailMap.put("PM", detailAMap.get("PM"));
						detailMap.put("id", detailAMap.get("id"));
						detailMap.put(gb[j]+"JJSX", Util.null2String(detailAMap.get(gb[j]+"JJSX")));
						detailMap.put(gb[j]+"JRSX", Util.null2String(detailAMap.get(gb[j]+"JRSX")));
						detailMap.put(gb[j]+"KFBZ", Util.null2String(detailAMap.get(gb[j]+"KFBZ")));
						detailList.add(detailMap);
					}
				}
			}
			int detailrows = detailList.size() ;//添加指定条数明细  
			if(detailrows > 0){
				 //主字段        
				WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[5]; //字段信息        
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
				wrti[2].setFieldName("lx");//类型      
				wrti[2].setFieldValue("1");//        
				wrti[2].setView(true);//字段是否可见       
				wrti[2].setEdit(true);//字段是否可编辑
				wrti[3] = new WorkflowRequestTableField();         
				wrti[3].setFieldName("xpqrlc");//订购会新品确认表(子表)  
				wrti[3].setFieldValue(requestid);//        
				wrti[3].setView(true);//字段是否可见       
				wrti[3].setEdit(true);//字段是否可编辑
				wrti[4] = new WorkflowRequestTableField();         
				wrti[4].setFieldName("zhbh");//展会编号 
				wrti[4].setFieldValue(ZHBH);//        
				wrti[4].setView(true);//字段是否可见       
				wrti[4].setEdit(true);//字段是否可编辑
				
				WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
				wrtri[0] = new WorkflowRequestTableRecord();        
				wrtri[0].setWorkflowRequestTableFields(wrti);           
				WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
				wmi.setRequestRecords(wrtri);
				//添加明细数据  
				wrtri = new WorkflowRequestTableRecord[detailrows];
				for (int k = 0; k < detailrows; k++) {
					Map<String, String> detailMap2 = detailList.get(k);
					String idDetail2 = Util.null2String(detailMap2.get("id"));				//明细ID
					String gbDetail2 = Util.null2String(detailMap2.get("GB"));				//国别
					String hhDetail2 = Util.null2String(detailMap2.get("HH"));				//货号
					String pmDetail2 = Util.null2String(detailMap2.get("PM"));				//品名
					sql = "select id from uf_product where segment1 = '" + hhDetail2 + "'";
					rs.execute(sql);
					if (rs.next()){
						hhDetail2 = rs.getString("id");
			        }else {
			        	hhDetail2 = "";
			        }
					//获取启用扩增国别id
					String qykzgjDetail2 = "";
					sql = "select selectvalue from workflow_SelectItem where fieldid = '39683' and selectname = '" + gbDetail2 + "'";
					rs.execute(sql);
					if (rs.next()){
						qykzgjDetail2 = rs.getString("selectvalue");
			        }
					//获取现销售国家
					String xxsgjDetail2 = "";
					sql = "select a.segment1,c.org_information5 "
						+ "from uf_product a "
						+ "inner join uf_seasproduct b on a.standard_item_id=b.standard_item_id and b.ou_item_status_code!='STOPPED'"
						+ "inner join uf_gb c on b.org_id = c.organization_id "
						+ "where a.id = ?";
					rs.executeQuery(sql, hhDetail2);
					while (rs.next()) {
						if ("".equals(xxsgjDetail2)) {
							String gb = rs.getString("org_information5");
							xxsgjDetail2 = gb;
						} else {
							String gb = rs.getString("org_information5");
							xxsgjDetail2 += "," + gb;
						}
					}
					dylcList.add(idDetail2+"-"+gbDetail2);
					wrti = new WorkflowRequestTableField[10]; //字段信息             
					
					wrti[0] = new WorkflowRequestTableField();             
					wrti[0].setFieldName("kf");//开发        
					wrti[0].setFieldValue(SQR);            
					wrti[0].setView(true);//字段是否可见              
					wrti[0].setEdit(true);//字段是否可编辑
					
					wrti[1] = new WorkflowRequestTableField();             
					wrti[1].setFieldName("hh");//货号         
					wrti[1].setFieldValue(hhDetail2);            
					wrti[1].setView(true);//字段是否可见              
					wrti[1].setEdit(true);//字段是否可编辑
					
					wrti[2] = new WorkflowRequestTableField();             
					wrti[2].setFieldName("qykzgj");//启用/扩增国家         
					wrti[2].setFieldValue(qykzgjDetail2);            
					wrti[2].setView(true);//字段是否可见              
					wrti[2].setEdit(true);//字段是否可编辑
					
					wrti[3] = new WorkflowRequestTableField();             
					wrti[3].setFieldName("jjsx");//季节属性        
					wrti[3].setFieldValue(Util.null2String(detailMap2.get(gbDetail2+"JJSX")));            
					wrti[3].setView(true);//字段是否可见              
					wrti[3].setEdit(true);//字段是否可编辑
					
					wrti[4] = new WorkflowRequestTableField();             
					wrti[4].setFieldName("jrsx");//节日属性       
					wrti[4].setFieldValue(Util.null2String(detailMap2.get(gbDetail2+"JRSX")));            
					wrti[4].setView(true);//字段是否可见              
					wrti[4].setEdit(true);//字段是否可编辑
					
					wrti[5] = new WorkflowRequestTableField();             
					wrti[5].setFieldName("bs");//标识       
					wrti[5].setFieldValue(idDetail2+"-"+gbDetail2);            
					wrti[5].setView(true);//字段是否可见              
					wrti[5].setEdit(true);//字段是否可编辑
					
					wrti[6] = new WorkflowRequestTableField();             
					wrti[6].setFieldName("cppm");//产品品名      
					wrti[6].setFieldValue(pmDetail2);            
					wrti[6].setView(true);//字段是否可见              
					wrti[6].setEdit(true);//字段是否可编辑
					
					wrti[7] = new WorkflowRequestTableField();             
					wrti[7].setFieldName("xxsgj");//现销售国家    
					wrti[7].setFieldValue(xxsgjDetail2);            
					wrti[7].setView(true);//字段是否可见              
					wrti[7].setEdit(true);//字段是否可编辑
					
					wrti[8] = new WorkflowRequestTableField();             
					wrti[8].setFieldName("kfbz");//开发备注    
					wrti[8].setFieldValue(Util.null2String(detailMap2.get(gbDetail2+"KFBZ")));            
					wrti[8].setView(true);//字段是否可见              
					wrti[8].setEdit(true);//字段是否可编辑
					
					wrti[9] = new WorkflowRequestTableField();             
					wrti[9].setFieldName("yy");//原因    
					wrti[9].setFieldValue("展会扩增国别");            
					wrti[9].setView(true);//字段是否可见              
					wrti[9].setEdit(true);//字段是否可编辑
					
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
				wbi.setWorkflowId("842");//workflowid       
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
				wri.setRequestName("国别产品信息变更单(货号启用/扩增国别/季节属性变更)-" + SQRXM + "-" + SQRQ);//流程标题        
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