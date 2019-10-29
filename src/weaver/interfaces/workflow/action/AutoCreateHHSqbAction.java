package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

public class AutoCreateHHSqbAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateHHSqbAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
    String SQR = "";	//申请人 
    String SQRXM = "";	//申请姓名 
    String KFYGH = "";	//开发员工号 
    String YJZZ = "";	//一级组织
    String EJZZ = "";	//二级组织
    String SQRQ = "";	//申请日期
    String CPXXDH = "";	//产品选型单号
    String GJC = "";	//关键词
    String TJLCH = "";	//推荐子流程号
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		SQR = Util.null2String(mainTable.get("KF"));
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		EJZZ = Util.null2String(mainTable.get("EJZZ"));
		CPXXDH = Util.null2String(mainTable.get("CPXXDH"));
		GJC = Util.null2String(mainTable.get("GJC"));
		TJLCH = Util.null2String(mainTable.get("TJLCH"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        sql = "select workcode from hrmresource where id = '" + SQR + "'";
		rs.execute(sql);
		if(rs.next()){
			KFYGH = Util.null2String(rs.getString("workcode"));
		}
        // 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				boolean flag = false;
				Map<String, String> detailAMap = detailAList.get(i);
				String mxidDetailA = Util.null2String(detailAMap.get("id"));			// 明细行号
				String dghmxidDetailA = "";												// 订购会明细ID
				String xdgbDetailA = "";												// 下单国别
				String bsDetailA = Util.null2String(detailAMap.get("BS"));				// 标识
				if(bsDetailA.indexOf(";")!=-1){
					String[] bsArr = bsDetailA.split(";");
					for(String bs : bsArr){
						dghmxidDetailA = bs.split("-")[0];
						xdgbDetailA += bs.split("-")[1] + ";";
					}
				}else{
					dghmxidDetailA = bsDetailA.split("-")[0];
					xdgbDetailA = bsDetailA.split("-")[1];
				}
				String usqrdlDetailA = Util.null2String(detailAMap.get("USQRDL"));		// US确认定量
				String caqrdlDetailA = Util.null2String(detailAMap.get("CAQRDL"));		// CA确认定量
				String ukqrdlDetailA = Util.null2String(detailAMap.get("UKQRDL"));		// UK确认定量
				String deqrdlDetailA = Util.null2String(detailAMap.get("DEQRDL"));		// DE确认定量
				String frqrdlDetailA = Util.null2String(detailAMap.get("FRQRDL"));		// FR确认定量
				String itqrdlDetailA = Util.null2String(detailAMap.get("ITQRDL"));		// IT确认定量
				String esqrdlDetailA = Util.null2String(detailAMap.get("ESQRDL"));		// ES确认定量
				if((StringUtils.isNotBlank(usqrdlDetailA) && !"0".equals(usqrdlDetailA))  
				 || (StringUtils.isNotBlank(caqrdlDetailA) && !"0".equals(caqrdlDetailA))
				 || (StringUtils.isNotBlank(ukqrdlDetailA) && !"0".equals(ukqrdlDetailA))
				 || (StringUtils.isNotBlank(deqrdlDetailA) && !"0".equals(deqrdlDetailA))
				 || (StringUtils.isNotBlank(frqrdlDetailA) && !"0".equals(frqrdlDetailA))
				 || (StringUtils.isNotBlank(itqrdlDetailA) && !"0".equals(itqrdlDetailA))
				 || (StringUtils.isNotBlank(esqrdlDetailA) && !"0".equals(esqrdlDetailA))){
					flag = true;
				}
				String usjjsxDetailA = Util.null2String(detailAMap.get("USJJSX"));		// US季节属性
				String cajjsxDetailA = Util.null2String(detailAMap.get("CAJJSX"));		// CA季节属性
				String ukjjsxDetailA = Util.null2String(detailAMap.get("UKJJSX"));		// UK季节属性
				String dejjsxDetailA = Util.null2String(detailAMap.get("DEJJSX"));		// DE季节属性
				String frjjsxDetailA = Util.null2String(detailAMap.get("FRJJSX"));		// FR季节属性
				String itjjsxDetailA = Util.null2String(detailAMap.get("ITJJSX"));		// IT季节属性
				String esjjsxDetailA = Util.null2String(detailAMap.get("ESJJSX"));		// ES季节属性
				
				String usjrsxDetailA = Util.null2String(detailAMap.get("USJRSX"));		// US节日属性
				String cajrsxDetailA = Util.null2String(detailAMap.get("CAJRSX"));		// CA节日属性
				String ukjrsxDetailA = Util.null2String(detailAMap.get("UKJRSX"));		// UK节日属性
				String dejrsxDetailA = Util.null2String(detailAMap.get("DEJRSX"));		// DE节日属性
				String frjrsxDetailA = Util.null2String(detailAMap.get("FRJRSX"));		// FR节日属性
				String itjrsxDetailA = Util.null2String(detailAMap.get("ITJRSX"));		// IT节日属性
				String esjrsxDetailA = Util.null2String(detailAMap.get("ESJRSX"));		// ES节日属性
				String cpmdDetailA = Util.null2String(detailAMap.get("CPMD"));			// 产品卖点
				String tpDetailA = Util.null2String(detailAMap.get("TP"));				// 产品图片
				if(flag){
					//主字段        
					WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[27]; //字段信息        
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
					wrti[2].setFieldName("rjzz");//二级组织       
					wrti[2].setFieldValue(EJZZ);//        
					wrti[2].setView(true);//字段是否可见       
					wrti[2].setEdit(true);//字段是否可编辑
					wrti[3] = new WorkflowRequestTableField();         
					wrti[3].setFieldName("cptp");//产品图片       
					wrti[3].setFieldValue(tpDetailA);//        
					wrti[3].setView(true);//字段是否可见       
					wrti[3].setEdit(true);//字段是否可编辑
					wrti[4] = new WorkflowRequestTableField();         
					wrti[4].setFieldName("cpmd");//产品卖点       
					wrti[4].setFieldValue(cpmdDetailA);//        
					wrti[4].setView(true);//字段是否可见       
					wrti[4].setEdit(true);//字段是否可编辑
					wrti[5] = new WorkflowRequestTableField();         
					wrti[5].setFieldName("usjjsx");//us季节属性       
					wrti[5].setFieldValue(usjjsxDetailA);//        
					wrti[5].setView(true);//字段是否可见       
					wrti[5].setEdit(true);//字段是否可编辑
					wrti[6] = new WorkflowRequestTableField();         
					wrti[6].setFieldName("cajjsx");//ca季节属性       
					wrti[6].setFieldValue(cajjsxDetailA);//        
					wrti[6].setView(true);//字段是否可见       
					wrti[6].setEdit(true);//字段是否可编辑
					wrti[7] = new WorkflowRequestTableField();         
					wrti[7].setFieldName("ukjjsx");//uk季节属性       
					wrti[7].setFieldValue(ukjjsxDetailA);//        
					wrti[7].setView(true);//字段是否可见       
					wrti[7].setEdit(true);//字段是否可编辑
					wrti[8] = new WorkflowRequestTableField();         
					wrti[8].setFieldName("dejjsx");//de季节属性       
					wrti[8].setFieldValue(dejjsxDetailA);//        
					wrti[8].setView(true);//字段是否可见       
					wrti[8].setEdit(true);//字段是否可编辑
					wrti[9] = new WorkflowRequestTableField();         
					wrti[9].setFieldName("frjjsx");//fr季节属性       
					wrti[9].setFieldValue(frjjsxDetailA);//        
					wrti[9].setView(true);//字段是否可见       
					wrti[9].setEdit(true);//字段是否可编辑
					wrti[10] = new WorkflowRequestTableField();         
					wrti[10].setFieldName("itjjsx");//it季节属性       
					wrti[10].setFieldValue(itjjsxDetailA);//        
					wrti[10].setView(true);//字段是否可见       
					wrti[10].setEdit(true);//字段是否可编辑
					wrti[11] = new WorkflowRequestTableField();         
					wrti[11].setFieldName("esjjsx");//es季节属性       
					wrti[11].setFieldValue(esjjsxDetailA);//        
					wrti[11].setView(true);//字段是否可见       
					wrti[11].setEdit(true);//字段是否可编辑
					wrti[12] = new WorkflowRequestTableField();         
					wrti[12].setFieldName("usjrsx");//us节日属性       
					wrti[12].setFieldValue(usjrsxDetailA);//        
					wrti[12].setView(true);//字段是否可见       
					wrti[12].setEdit(true);//字段是否可编辑
					wrti[13] = new WorkflowRequestTableField();         
					wrti[13].setFieldName("cajrsx");//ca节日属性       
					wrti[13].setFieldValue(cajrsxDetailA);//        
					wrti[13].setView(true);//字段是否可见       
					wrti[13].setEdit(true);//字段是否可编辑
					wrti[14] = new WorkflowRequestTableField();         
					wrti[14].setFieldName("ukjrsx");//uk节日属性       
					wrti[14].setFieldValue(ukjrsxDetailA);//        
					wrti[14].setView(true);//字段是否可见       
					wrti[14].setEdit(true);//字段是否可编辑
					wrti[15] = new WorkflowRequestTableField();         
					wrti[15].setFieldName("dejrsx");//de节日属性       
					wrti[15].setFieldValue(dejrsxDetailA);//        
					wrti[15].setView(true);//字段是否可见       
					wrti[15].setEdit(true);//字段是否可编辑
					wrti[16] = new WorkflowRequestTableField();         
					wrti[16].setFieldName("frjrsx");//fr节日属性       
					wrti[16].setFieldValue(frjrsxDetailA);//        
					wrti[16].setView(true);//字段是否可见       
					wrti[16].setEdit(true);//字段是否可编辑
					wrti[17] = new WorkflowRequestTableField();         
					wrti[17].setFieldName("itjrsx");//it节日属性       
					wrti[17].setFieldValue(itjrsxDetailA);//        
					wrti[17].setView(true);//字段是否可见       
					wrti[17].setEdit(true);//字段是否可编辑
					wrti[18] = new WorkflowRequestTableField();         
					wrti[18].setFieldName("esjrsx");//es节日属性       
					wrti[18].setFieldValue(esjrsxDetailA);//        
					wrti[18].setView(true);//字段是否可见       
					wrti[18].setEdit(true);//字段是否可编辑
					wrti[19] = new WorkflowRequestTableField();         
					wrti[19].setFieldName("xxdh");//选型单号       
					wrti[19].setFieldValue(CPXXDH+"-"+mxidDetailA);//        
					wrti[19].setView(true);//字段是否可见       
					wrti[19].setEdit(true);//字段是否可编辑
					wrti[20] = new WorkflowRequestTableField();         
					wrti[20].setFieldName("gjc");//关键词       
					wrti[20].setFieldValue(GJC);//        
					wrti[20].setView(true);//字段是否可见       
					wrti[20].setEdit(true);//字段是否可编辑
					wrti[21] = new WorkflowRequestTableField();  
					wrti[21].setFieldName("sqrq");//申请日期    
					wrti[21].setFieldValue(SQRQ);//
					wrti[21].setView(true);//字段是否可见       
					wrti[21].setEdit(true);//字段是否可编辑
					wrti[22] = new WorkflowRequestTableField();         
					wrti[22].setFieldName("xxlclj");//选型流程链接      
					wrti[22].setFieldValue(requestid);//      
					wrti[22].setView(true);//字段是否可见       
					wrti[22].setEdit(true);//字段是否可编辑
					wrti[23] = new WorkflowRequestTableField();         
					wrti[23].setFieldName("kfygh");//开发员工号      
					wrti[23].setFieldValue(KFYGH);//      
					wrti[23].setView(true);//字段是否可见       
					wrti[23].setEdit(true);//字段是否可编辑
					wrti[24] = new WorkflowRequestTableField();         
					wrti[24].setFieldName("tjzlch");//推荐子流程号     
					wrti[24].setFieldValue(TJLCH);//      
					wrti[24].setView(true);//字段是否可见       
					wrti[24].setEdit(true);//字段是否可编辑
					wrti[25] = new WorkflowRequestTableField();         
					wrti[25].setFieldName("mxid");//订购会明细ID     
					wrti[25].setFieldValue(dghmxidDetailA);//      
					wrti[25].setView(true);//字段是否可见       
					wrti[25].setEdit(true);//字段是否可编辑
					wrti[26] = new WorkflowRequestTableField();         
					wrti[26].setFieldName("xdgb");//下单国别    
					wrti[26].setFieldValue(xdgbDetailA);//      
					wrti[26].setView(true);//字段是否可见       
					wrti[26].setEdit(true);//字段是否可编辑
					
					WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
					wrtri[0] = new WorkflowRequestTableRecord();        
					wrtri[0].setWorkflowRequestTableFields(wrti);           
					WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
					wmi.setRequestRecords(wrtri);
					//添加明细数据       
					wrtri = new WorkflowRequestTableRecord[1];
					wrti = new WorkflowRequestTableField[7]; //字段信息             
					wrti[0] = new WorkflowRequestTableField();             
					wrti[0].setFieldName("us");//US下单数量             
					wrti[0].setFieldValue(usqrdlDetailA);            
					wrti[0].setView(true);//字段是否可见              
					wrti[0].setEdit(true);//字段是否可编辑
					wrti[1] = new WorkflowRequestTableField();             
					wrti[1].setFieldName("ca");//CA下单数量             
					wrti[1].setFieldValue(caqrdlDetailA);            
					wrti[1].setView(true);//字段是否可见              
					wrti[1].setEdit(true);//字段是否可编辑
					wrti[2] = new WorkflowRequestTableField();             
					wrti[2].setFieldName("uk");//UK下单数量             
					wrti[2].setFieldValue(ukqrdlDetailA);            
					wrti[2].setView(true);//字段是否可见              
					wrti[2].setEdit(true);//字段是否可编辑
					wrti[3] = new WorkflowRequestTableField();             
					wrti[3].setFieldName("de");//DE下单数量             
					wrti[3].setFieldValue(deqrdlDetailA);            
					wrti[3].setView(true);//字段是否可见              
					wrti[3].setEdit(true);//字段是否可编辑
					wrti[4] = new WorkflowRequestTableField();             
					wrti[4].setFieldName("fr");//FR下单数量             
					wrti[4].setFieldValue(frqrdlDetailA);            
					wrti[4].setView(true);//字段是否可见              
					wrti[4].setEdit(true);//字段是否可编辑
					wrti[5] = new WorkflowRequestTableField();             
					wrti[5].setFieldName("it");//IT下单数量             
					wrti[5].setFieldValue(itqrdlDetailA);            
					wrti[5].setView(true);//字段是否可见              
					wrti[5].setEdit(true);//字段是否可编辑
					wrti[6] = new WorkflowRequestTableField();             
					wrti[6].setFieldName("es");//ES下单数量             
					wrti[6].setFieldValue(esqrdlDetailA);            
					wrti[6].setView(true);//字段是否可见              
					wrti[6].setEdit(true);//字段是否可编辑
					wrtri[0] = new WorkflowRequestTableRecord();
					wrtri[0].setWorkflowRequestTableFields(wrti);
					//添加到明细表中        
					WorkflowDetailTableInfo WorkflowDetailTableInfo[] = new WorkflowDetailTableInfo[1];
					//指定明细表的个数，多个明细表指定多个，顺序按照明细的顺序        
					WorkflowDetailTableInfo[0] = new WorkflowDetailTableInfo();        
					WorkflowDetailTableInfo[0].setWorkflowRequestTableRecords(wrtri);
					//添加工作流id        
					WorkflowBaseInfo wbi = new WorkflowBaseInfo();        
					wbi.setWorkflowId("1483");//workflowid       
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
					wri.setRequestName("货号申请表-" + SQRXM + "-" + SQRQ);//流程标题        
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