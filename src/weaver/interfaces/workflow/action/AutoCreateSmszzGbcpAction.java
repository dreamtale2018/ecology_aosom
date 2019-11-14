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

import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 国别产品信息变更单结束节点自动创建说明书制作申请表<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateSmszzGbcpAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateSmszzGbcpAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
	String SQR = "";	//申请人
    String SQRXM = "";	//申请人 姓名 
    String YJZZ = "";	//一级组织
    String EJZZ = "";	//二级组织
    String SQRQ = "";	//申请日期
    String LX = "";		//类型
    
    String sql = "";

    try
    {
		String workflowid = request.getWorkflowid();

    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("SQR"));
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		EJZZ = Util.null2String(mainTable.get("RJZZ"));
		LX = Util.null2String(mainTable.get("LX"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        if("1".equals(LX)){
        	// 获取流程明细表 1
    		List<Map<String, String>> detailAList = info.getDetailMap("1");
    		if (detailAList != null && detailAList.size() > 0) {
    			//按照开发员进行分类。
				Map<String, List<Map<String,String>>> detailMap = new HashMap<String, List<Map<String,String>>>();
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					String kfDetailA = Util.null2String(detailAMap.get("KF"));		// 开发
					if(detailMap.containsKey(kfDetailA)){
						detailMap.get(kfDetailA).add(detailAMap);
					}else{
						List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
						detailList.add(detailAMap);
						detailMap.put(kfDetailA, detailList);
					}
				}
				for(Map.Entry<String,List<Map<String,String>>> entry : detailMap.entrySet()){
					String kf = entry.getKey();
					if(!"".equals(kf)){
						SQR = kf;
					}
					sql = "select departmentid from hrmresource where id = '" + SQR + "'";
    				rs.execute(sql);
    				if(rs.next()){
    					EJZZ = Util.null2String(rs.getString("departmentid"));
    				}
    				sql = "select * from hrmdepartment  where id = (select SUPDEPID from hrmdepartment where id = '"+EJZZ+"' )";
    				rs.execute(sql);
    				if(rs.next()){
    					YJZZ = Util.null2String(rs.getString("id"));
    				}else{
    					YJZZ = EJZZ;
    				}
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
						wrti[1].setFieldName("yjzz");//一级组织      
						wrti[1].setFieldValue(YJZZ);//        
						wrti[1].setView(true);//字段是否可见       
						wrti[1].setEdit(true);//字段是否可编辑
						wrti[2] = new WorkflowRequestTableField();         
						wrti[2].setFieldName("ejzz");//二级组织       
						wrti[2].setFieldValue(EJZZ);//        
						wrti[2].setView(true);//字段是否可见       
						wrti[2].setEdit(true);//字段是否可编辑
						wrti[3] = new WorkflowRequestTableField();         
						wrti[3].setFieldName("gbcplclj");//国别产品流程链接       
						wrti[3].setFieldValue(requestid);//        
						wrti[3].setView(true);//字段是否可见       
						wrti[3].setEdit(true);//字段是否可编辑
						wrti[4] = new WorkflowRequestTableField();         
						wrti[4].setFieldName("sqrq");//申请日期       
						wrti[4].setFieldValue(SQRQ);//        
						wrti[4].setView(true);//字段是否可见       
						wrti[4].setEdit(true);//字段是否可编辑
						wrti[5] = new WorkflowRequestTableField();         
						wrti[5].setFieldName("xqly");//需求来源     
						wrti[5].setFieldValue(LX);//        
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
						for (int i = 0; i < detailrows; i++) {
							Map<String, String> mxhhMap = mxhhList.get(i);
							String hhDetailA = Util.null2String(mxhhMap.get("HH"));				//货号
							String fxsDetailA = "";												//分箱数
							sql = "select box_quantity from uf_product where id = '" + hhDetailA + "'";
							rs.execute(sql);
							if(rs.next()){
								fxsDetailA = Util.null2String(rs.getString("box_quantity"));
							}
							String cppmDetailA = Util.null2String(mxhhMap.get("CPPM"));			//产品品名
							String ppDetailA = Util.null2String(WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "PP", mxhhMap.get("PP")));								
																								//品牌
							String qykzgjDetailA = Util.null2String(WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "QYKZGJ", mxhhMap.get("QYKZGJ")));
																								//启用/扩增国别
							String csjrzxxDetailA = Util.null2String(mxhhMap.get("CSJRZXX"));	//测试及认证信息
							
							wrti = new WorkflowRequestTableField[6]; //字段信息             
							wrti[0] = new WorkflowRequestTableField();             
							wrti[0].setFieldName("hh");//货号             
							wrti[0].setFieldValue(hhDetailA);            
							wrti[0].setView(true);//字段是否可见              
							wrti[0].setEdit(true);//字段是否可编辑
							
							wrti[1] = new WorkflowRequestTableField();             
							wrti[1].setFieldName("pm");//品名          
							wrti[1].setFieldValue(cppmDetailA);            
							wrti[1].setView(true);//字段是否可见              
							wrti[1].setEdit(true);//字段是否可编辑
							
							wrti[2] = new WorkflowRequestTableField();             
							wrti[2].setFieldName("gb");//国别           
							wrti[2].setFieldValue(qykzgjDetailA);            
							wrti[2].setView(true);//字段是否可见              
							wrti[2].setEdit(true);//字段是否可编辑
							
							wrti[3] = new WorkflowRequestTableField();             
							wrti[3].setFieldName("csjrzyq");//测试及认证要求         
							wrti[3].setFieldValue(csjrzxxDetailA);            
							wrti[3].setView(true);//字段是否可见              
							wrti[3].setEdit(true);//字段是否可编辑
							
							wrti[4] = new WorkflowRequestTableField();             
							wrti[4].setFieldName("pp");//品牌         
							wrti[4].setFieldValue(ppDetailA);            
							wrti[4].setView(true);//字段是否可见              
							wrti[4].setEdit(true);//字段是否可编辑
							
							wrti[5] = new WorkflowRequestTableField();             
							wrti[5].setFieldName("fxs");//分箱数         
							wrti[5].setFieldValue(fxsDetailA);            
							wrti[5].setView(true);//字段是否可见              
							wrti[5].setEdit(true);//字段是否可编辑
							
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
						wbi.setWorkflowId("1384");//workflowid       
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
						wri.setRequestName("说明书制作申请表-" + SQRXM + "-" + SQRQ);//流程标题        
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