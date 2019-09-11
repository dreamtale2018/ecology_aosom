package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
 * 拍照需求表结束节点自动创建设计需求表<br>
 * 
 * @author ycj
 *
 */
public class AutoCreateSjxqbAction implements Action
{
  private Log logger = LogFactory.getLog(AutoCreateSjxqbAction.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	RecordSet rs = new RecordSet();
	  
	String requestid = request.getRequestid();
    String SQR = "";		//申请人 
    String SQRXM = "";		//申请姓名 
    String SJS = "";		//设计师 
    String SJSYC = "";		//设计师隐藏 
    String SJYQ = "";		//设计要求
    String SJYQFJ = "";		//设计要求附件
    String HH = "";			//货号
    String YJZZ = "";		//一级组织
    String EJZZ = "";		//二级组织
    String SQRQ = "";		//申请日期
    String CPPM = "";		//产品名称
    String XDGB = "";		//下单国别
    String YQWCR = "";		//要求完成日
    String SYWCR = "";		//摄影完成日
    String SX1 = "";		//属性1
    String TP = "";			//图片
    String STSJBJ = "";		//首图设计-背景
    String STSJDJ = "";		//首图设计-道具
    String STSJRW = "";		//首图设计-人物
    String BDJX = "";		//白底精修
    String TPCLGNT = "";	//图片处理-功能图
    String TPCLCCZZ = "";	//图片处理-尺寸制作
    String TPCLXT = "";		//图片处理-修图
    String QT = "";			//其它
    String BJ = "";			//编辑
    String CPGG = "";		//产品规格
    
    String sql = "";

    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
   	 	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		BJ = Util.null2String(mainTable.get("BJ"));
		SJS = Util.null2String(mainTable.get("SJS"));
		SJSYC = Util.null2String(mainTable.get("SJSYC"));
		SJYQ = Util.null2String(mainTable.get("SJYQ"));
		SJYQFJ = Util.null2String(mainTable.get("SJYQFJ"));
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		EJZZ = Util.null2String(mainTable.get("EJZZ"));
		HH = Util.null2String(mainTable.get("HH"));
		sql = "select id from uf_product where segment1 = '" + HH + "'";
		rs.execute(sql);
		if(rs.next()){
			HH = Util.null2String(rs.getString("id"));
		}
		sql = "select receivedate from workflow_currentoperator where requestid = '" + requestid + "' and nodeid = '2697'";
		rs.execute(sql);
		if(rs.next()){
			SYWCR = Util.null2String(rs.getString("receivedate"));
		}
		if(!"".equals(BJ)){
			SQR = BJ;
		}else{
			SQR = Util.null2String(mainTable.get("SQR"));
		}
		CPPM = Util.null2String(mainTable.get("CPPM"));
		SX1 = Util.null2String(mainTable.get("CPSX1"));
		TP = Util.null2String(mainTable.get("CPSX1"));
		STSJBJ = Util.null2String(mainTable.get("STSJBJ"));
		STSJDJ = Util.null2String(mainTable.get("STSJDJ"));
		STSJRW = Util.null2String(mainTable.get("STSJRW"));
		BDJX = Util.null2String(mainTable.get("BDJX"));
		TPCLGNT = Util.null2String(mainTable.get("TPCLGNT"));
		TPCLCCZZ = Util.null2String(mainTable.get("TPCLCCZZ"));
		TPCLXT = Util.null2String(mainTable.get("TPCLXT"));
		QT = Util.null2String(mainTable.get("QT"));
		CPGG = Util.null2String(mainTable.get("CPGG"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQRQ = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        YQWCR = sdf.format(calendar.getTime());
        
		//主字段        
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[12]; //字段信息        
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
		wrti[3].setFieldName("sqrq");//申请日期    
		wrti[3].setFieldValue(SQRQ);//
		wrti[3].setView(true);//字段是否可见       
		wrti[3].setEdit(true);//字段是否可编辑
		wrti[4] = new WorkflowRequestTableField();  
		wrti[4].setFieldName("xqlx");//需求类型    
		wrti[4].setFieldValue("0");//
		wrti[4].setView(true);//字段是否可见       
		wrti[4].setEdit(true);//字段是否可编辑
		wrti[5] = new WorkflowRequestTableField();  
		wrti[5].setFieldName("yqwcr");//要求完成日期    
		wrti[5].setFieldValue(YQWCR);//
		wrti[5].setView(true);//字段是否可见       
		wrti[5].setEdit(true);//字段是否可编辑
		wrti[6] = new WorkflowRequestTableField();  
		wrti[6].setFieldName("pzxqblc");//拍照需求表流程    
		wrti[6].setFieldValue(requestid);//
		wrti[6].setView(true);//字段是否可见       
		wrti[6].setEdit(true);//字段是否可编辑
		wrti[7] = new WorkflowRequestTableField();  
		wrti[7].setFieldName("sywcr");//摄影完成日    
		wrti[7].setFieldValue(SYWCR);//
		wrti[7].setView(true);//字段是否可见       
		wrti[7].setEdit(true);//字段是否可编辑
		wrti[8] = new WorkflowRequestTableField();  
		wrti[8].setFieldName("sjs");//设计师    
		wrti[8].setFieldValue(SJS);//
		wrti[8].setView(true);//字段是否可见       
		wrti[8].setEdit(true);//字段是否可编辑
		wrti[9] = new WorkflowRequestTableField();  
		wrti[9].setFieldName("sjyq");//设计要求    
		wrti[9].setFieldValue(SJYQ);//
		wrti[9].setView(true);//字段是否可见       
		wrti[9].setEdit(true);//字段是否可编辑
		wrti[10] = new WorkflowRequestTableField();  
		wrti[10].setFieldName("sjyqfj");//设计要求附件    
		wrti[10].setFieldValue(SJYQFJ);//
		wrti[10].setView(true);//字段是否可见       
		wrti[10].setEdit(true);//字段是否可编辑
		wrti[11] = new WorkflowRequestTableField();  
		wrti[11].setFieldName("sjsyc");//设计师隐藏   
		wrti[11].setFieldValue(SJSYC);//
		wrti[11].setView(true);//字段是否可见       
		wrti[11].setEdit(true);//字段是否可编辑
		
		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据        
		wrtri[0] = new WorkflowRequestTableRecord();        
		wrtri[0].setWorkflowRequestTableFields(wrti);           
		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();        
		wmi.setRequestRecords(wrtri);   
		//添加明细数据       
		wrtri = new WorkflowRequestTableRecord[1];
		wrti = new WorkflowRequestTableField[14]; //字段信息             
		wrti[0] = new WorkflowRequestTableField();             
		wrti[0].setFieldName("hh");//货号             
		wrti[0].setFieldValue(HH);            
		wrti[0].setView(true);//字段是否可见              
		wrti[0].setEdit(true);//字段是否可编辑
		wrti[1] = new WorkflowRequestTableField();             
		wrti[1].setFieldName("cpmc");//产品名称            
		wrti[1].setFieldValue(CPPM);            
		wrti[1].setView(true);//字段是否可见              
		wrti[1].setEdit(true);//字段是否可编辑
		wrti[2] = new WorkflowRequestTableField();             
		wrti[2].setFieldName("xdgb");//下单国别             
		wrti[2].setFieldValue(XDGB);            
		wrti[2].setView(true);//字段是否可见              
		wrti[2].setEdit(true);//字段是否可编辑
		wrti[3] = new WorkflowRequestTableField();             
		wrti[3].setFieldName("sx1");//属性1            
		wrti[3].setFieldValue(SX1);            
		wrti[3].setView(true);//字段是否可见              
		wrti[3].setEdit(true);//字段是否可编辑
		wrti[4] = new WorkflowRequestTableField();             
		wrti[4].setFieldName("tp");//图片             
		wrti[4].setFieldValue(TP);            
		wrti[4].setView(true);//字段是否可见              
		wrti[4].setEdit(true);//字段是否可编辑
		wrti[5] = new WorkflowRequestTableField();             
		wrti[5].setFieldName("stsjbj");//首图设计-背景             
		wrti[5].setFieldValue(STSJBJ);            
		wrti[5].setView(true);//字段是否可见              
		wrti[5].setEdit(true);//字段是否可编辑
		wrti[6] = new WorkflowRequestTableField();             
		wrti[6].setFieldName("stsjdj");//首图设计-道具         
		wrti[6].setFieldValue(STSJDJ);            
		wrti[6].setView(true);//字段是否可见              
		wrti[6].setEdit(true);//字段是否可编辑
		wrti[7] = new WorkflowRequestTableField();             
		wrti[7].setFieldName("stsjrw");//首图设计-人物         
		wrti[7].setFieldValue(STSJRW);            
		wrti[7].setView(true);//字段是否可见              
		wrti[7].setEdit(true);//字段是否可编辑
		wrti[8] = new WorkflowRequestTableField();             
		wrti[8].setFieldName("bdjx");//白底精修         
		wrti[8].setFieldValue(BDJX);            
		wrti[8].setView(true);//字段是否可见              
		wrti[8].setEdit(true);//字段是否可编辑
		wrti[9] = new WorkflowRequestTableField();             
		wrti[9].setFieldName("tpclgnt");//图片处理-功能图       
		wrti[9].setFieldValue(TPCLGNT);            
		wrti[9].setView(true);//字段是否可见              
		wrti[9].setEdit(true);//字段是否可编辑
		wrti[10] = new WorkflowRequestTableField();             
		wrti[10].setFieldName("tpclcczz");//图片处理-尺寸制作         
		wrti[10].setFieldValue(TPCLCCZZ);            
		wrti[10].setView(true);//字段是否可见              
		wrti[10].setEdit(true);//字段是否可编辑
		wrti[11] = new WorkflowRequestTableField();             
		wrti[11].setFieldName("tpclxt");//图片处理-修图       
		wrti[11].setFieldValue(TPCLXT);            
		wrti[11].setView(true);//字段是否可见              
		wrti[11].setEdit(true);//字段是否可编辑
		wrti[12] = new WorkflowRequestTableField();             
		wrti[12].setFieldName("qt");//其他         
		wrti[12].setFieldValue(QT);            
		wrti[12].setView(true);//字段是否可见              
		wrti[12].setEdit(true);//字段是否可编辑
		wrti[13] = new WorkflowRequestTableField();             
		wrti[13].setFieldName("cpgg");//产品规格         
		wrti[13].setFieldValue(CPGG);            
		wrti[13].setView(true);//字段是否可见              
		wrti[13].setEdit(true);//字段是否可编辑
		wrtri[0] = new WorkflowRequestTableRecord();
		wrtri[0].setWorkflowRequestTableFields(wrti);
		//添加到明细表中        
		WorkflowDetailTableInfo WorkflowDetailTableInfo[] = new WorkflowDetailTableInfo[1];
		//指定明细表的个数，多个明细表指定多个，顺序按照明细的顺序        
		WorkflowDetailTableInfo[0] = new WorkflowDetailTableInfo();        
		WorkflowDetailTableInfo[0].setWorkflowRequestTableRecords(wrtri);
		//添加工作流id        
		WorkflowBaseInfo wbi = new WorkflowBaseInfo();        
		wbi.setWorkflowId("5123");//workflowid       
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
		wri.setRequestName("设计需求表-" + SQRXM + "-" + SQRQ);//流程标题        
		wri.setWorkflowMainTableInfo(wmi);//添加主字段数据 
		wri.setWorkflowDetailTableInfos(WorkflowDetailTableInfo);//添加明细数据
		wri.setWorkflowBaseInfo(wbi);        
		WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
		String newRequestid = workflowServiceImpl.doCreateWorkflowRequest(wri, Integer.parseInt(SQR));        
		this.logger.error("newRequestid:"+newRequestid);
		//this.logger.error("sql：" + sql);
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