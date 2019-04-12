package weaver.interfaces.workflow.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 团队活动费用报销单结束节点新建费用支付台账<br>
 * 
 * @author ycj
 *
 */
public class UpdateFyzfTdfy implements Action
{
  private OracleManager oracleManager = new OracleManager();
  private Log logger = LogFactory.getLog(UpdateFyzfTdfy.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    String requestid = request.getRequestid();
    String DJM = "团队活动费用报销单";				//单据名 
    String BXDH = "";							//流程号 
    String RQ = "";								//日期 
    String SQR = "";							//申请人
    String SQRXM = "";							//申请人姓名
    String JE = "";								//金额
    String YJZZ = "";							//一级组织
    String YJZZMC = "";							//一级组织名称
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
		BXDH = Util.null2String(mainTable.get("BXDH"));
		RQ = Util.null2String(mainTable.get("FQRQ"));
		SQR = Util.null2String(mainTable.get("SQR"));
		SQRXM = oracleManager.getRymc(SQR);
		SQRXM = oracleManager.getChineseMsg(SQRXM);
		YJZZ = Util.null2String(mainTable.get("YJZZ"));
		JE = Util.null2String(mainTable.get("SJFY"));										//金额
		FYXDL = Util.null2String(mainTable.get("FYXDL"));									//费用项大类		
		JFKM = Util.null2String(mainTable.get("JFKM"));										//借方科目
		JFKMDM = Util.null2String(mainTable.get("JFKMDM"));									//借方科目代码
		DFKM = Util.null2String(mainTable.get("DFKM"));										//贷方科目
		DFKMDM = Util.null2String(mainTable.get("DFKMDM"));									//贷方科目代码
		String fkstdm = "";																	//付款实体代码
		sql = "select kmdm from uf_BMKMDM where sfyx = '0' and xm1 = '业务实体' and xm2 = '" + FKZT + "'";
		rs.execute(sql);
		if (rs.next()){
			fkstdm = rs.getString("kmdm");
        }
		YJZZMC = oracleManager.getBmmc(YJZZ);
		YJZZMC = oracleManager.getChineseMsg(YJZZMC);
		String bmdm = "";																	//部门代码
		sql = "select kmdm from uf_BMKMDM where sfyx = '0' and xm1 = '部门' and xm2 = '" + YJZZMC + "'";
		rs.execute(sql);
		if (rs.next()){
			bmdm = rs.getString("kmdm");
        }
		String JFKJKMKM = new StringBuilder().append(fkstdm).append(".").append(bmdm).append(".")
									.append(JFKMDM).append(".0.0.0").toString();			//借方会计科目代码
		String DFKJKMKM = new StringBuilder().append(fkstdm).append(".0.").append(DFKMDM)
									.append(".0.0.0").toString();							//贷方会计科目代码
		
		String zyDetailA = new StringBuilder().append(SQRXM).append("-")
							.append(FYXDL).append(" 收款单位：")
							.append(SQRXM).append(" 流程号：").append(BXDH).toString();		//摘要
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
			    "<filedvalue>"+SQRXM+"</filedvalue>" +
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