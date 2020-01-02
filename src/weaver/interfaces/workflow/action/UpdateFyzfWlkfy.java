package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 往来款项支付审批单结束节点新建费用支付台账<br>
 * 
 * @author ycj
 *
 */
public class UpdateFyzfWlkfy implements Action
{
  private OracleManager oracleManager = new OracleManager();
  private Log logger = LogFactory.getLog(UpdateFyzfWlkfy.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    String requestid = request.getRequestid();
    String workflowid = request.getWorkflowid();
    String DJM = "往来款项支付单";					//单据名
    String BXDH = "";							//流程号 
    String RQ = "";								//日期 
    String SQR = "";							//申请人
    String SQRXM = "";							//申请人姓名
    String JE = "";								//金额
    String YJZZ = "";							//一级组织
    String YJZZMC = "";							//一级组织名称
    String SKDW = "";							//收款单位/人
    String FKZT = "遨森电子商务股份有限公司";			//付款主体
    String FKBZ = "CNY";						//付款币种
    String FYXDL = "";							//费用项大类
	String JFKM = "";							//借方科目
	String JFKMDM = "";							//借方科目代码
	String DFKM = "";							//贷方科目
	String DFKMDM = "";							//贷方科目代码
    RecordSet rs = new RecordSet();
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		BXDH = Util.null2String(mainTable.get("SQDH"));
		RQ = Util.null2String(mainTable.get("SQRQ"));
		SQR = Util.null2String(mainTable.get("JBR"));
		SQRXM = oracleManager.getRymc(SQR);
		SQRXM = oracleManager.getChineseMsg(SQRXM);
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		SKDW = Util.null2String(mainTable.get("SKDW"));
		FKZT = WorkflowUtils.getFieldSelectName(workflowid, "FKDW", mainTable.get("FKDW"));
		FKBZ = WorkflowUtils.getFieldSelectName(workflowid, "BZ", mainTable.get("BZ"));
		if("人民币".equals(FKBZ)){
			FKBZ = "CNY";
		}else if("美元".equals(FKBZ)){
			FKBZ = "USD";
		}
		JE = Util.null2String(mainTable.get("HJJE"));										//金额
		FYXDL = Util.null2String(mainTable.get("FYXDL"));									//费用项大类
		sql = "select fyxdl from uf_FYXKMDM where xh = '" + FYXDL + "'";
		rs.execute(sql);
		if (rs.next()){
			FYXDL = rs.getString("fyxdl");
        }
		JFKM = Util.null2String(mainTable.get("JFKM"));										//借方科目
		sql = "select kmmc from uf_KJKMDM where sfyx = '0' and xh = '" + JFKM + "'";
		rs.execute(sql);
		if (rs.next()){
			JFKM = rs.getString("kmmc");
        }
		JFKMDM = Util.null2String(mainTable.get("JFKMDM"));									//借方科目代码
		DFKM = Util.null2String(mainTable.get("DFKM"));										//贷方科目
		DFKMDM = Util.null2String(mainTable.get("DFKMDM"));									//贷方科目代码
		String fkstdm = "";																	//付款实体代码
		sql = "select gbkmdm from uf_BMKMDM where sfyx = '0' and gb = '" + FKZT + "'";
		rs.execute(sql);
		if (rs.next()){
			fkstdm = rs.getString("gbkmdm");
        }
		if(("101".equals(fkstdm)||"111".equals(fkstdm))&&"USD".equals(FKBZ)){
			DFKMDM = "10020207";
		}
		YJZZMC = oracleManager.getBmmc(YJZZ);
		YJZZMC = oracleManager.getChineseMsg(YJZZMC);
		String bmdm = "";																	//部门代码
		sql = "select bmkmdm from uf_BMKMDM where sfyx = '0' and gb = '" + FKZT + "' and bm = '" + YJZZMC + "'";
		rs.execute(sql);
		if (rs.next()){
			bmdm = rs.getString("bmkmdm");
        }
		String JFKJKMKM = new StringBuilder().append(fkstdm).append(".").append(bmdm).append(".")
									.append(JFKMDM).append(".0.0.0").toString();			//借方会计科目代码
		String DFKJKMKM = new StringBuilder().append(fkstdm).append(".0.").append(DFKMDM)
									.append(".0.0.0").toString();							//贷方会计科目代码
		
		String zyDetailA = new StringBuilder().append(SQRXM).append("-")
							.append(FYXDL).append(" 收款单位：")
							.append(SKDW).append(" 流程号：").append(BXDH).toString();		//摘要
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
		"<ROOT>" +
		  "<header>" +
		    "<userid>"+SQR+"</userid>" +
		    "<modeid>783</modeid>" +
		    "<id/>" +
		  "</header>" +
		  "<search>" +
		    "<condition/>" +
		    "<right>Y</right>" +
		  "</search>" +
		  "<data id=\"\">" +
		    "<maintable>" +
		      "<field>" +
		        "<filedname>DJM</filedname>" +
		        "<filedvalue>"+DJM+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
			    "<filedname>LCLJ</filedname>" +
			    "<filedvalue>"+requestid+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
		        "<filedname>BXDH</filedname>" +
		        "<filedvalue>"+BXDH+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
			    "<filedname>FYXDL</filedname>" +
			    "<filedvalue>"+FYXDL+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
		        "<filedname>RQ</filedname>" +
		        "<filedvalue>"+RQ+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
		        "<filedname>SQR</filedname>" +
		        "<filedvalue>"+SQR+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
		        "<filedname>YJZZ</filedname>" +
		        "<filedvalue>"+YJZZ+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
		        "<filedname>FKZT</filedname>" +
		        "<filedvalue>"+FKZT+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
			    "<filedname>FKBZ</filedname>" +
			    "<filedvalue>"+FKBZ+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
			    "<filedname>SKDW</filedname>" +
			    "<filedvalue>"+SKDW+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
			    "<filedname>JE</filedname>" +
			    "<filedvalue>"+JE+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
		        "<filedname>JFKM</filedname>" +
		        "<filedvalue>"+JFKM+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
		        "<filedname>DFKM</filedname>" +
		        "<filedvalue>"+DFKM+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
		        "<filedname>JFKJKMDM</filedname>" +
		        "<filedvalue>"+JFKJKMKM+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
		        "<filedname>DFKJKMDM</filedname>" +
		        "<filedvalue>"+DFKJKMKM+"</filedvalue>" +
		      "</field>" +
		      "<field>" +
		        "<filedname>ZY</filedname>" +
		        "<filedvalue>"+zyDetailA+"</filedvalue>" +
		      "</field>" +
		    "</maintable>" +
		  "</data>" +
		"</ROOT>";

		xml = xml.replaceAll("&quot;","\"");
		xml = xml.replaceAll("'","''");
		xml = xml.replaceAll("&nbsp;", " ");
		xml = xml.replaceAll("\r", " ");
		xml = xml.replaceAll("\n", " ");
		xml = xml.replaceAll("<br>", ";");
		xml = xml.replaceAll("&", "&amp;");
		ModeDataServiceImpl mdsi = new ModeDataServiceImpl();
		String returncode = mdsi.saveModeData(xml);
		this.logger.info("returncode:"+returncode);
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