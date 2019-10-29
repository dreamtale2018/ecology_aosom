package weaver.interfaces.workflow.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 订购会新品确认子表流程<br>
 * 到达分配节点,创建订购会管理台账
 *
 * @author ycj
 *
 */
public class UpdateXpqrzbFp implements Action
{
  private Log logger = LogFactory.getLog(UpdateXpqrzbFp.class);
  
  @Override
  public String execute(RequestInfo request)
  {
		RecordSet rs = new RecordSet();

	    String requestid = request.getRequestid();
	
	    String SQR = "";	//申请人
	    String SQRQ = "";	//申请日期
	    String ZB = "";		//组别
	    String ZHBH = "";	//展会编号
	    String GC = "";		//工厂
	    String ZRR = "";	//责任人
	    String SM = "";		//说明
	    
	    String sql = "";
	    
	    try
	    {
	    	ActionInfo info = ActionUtils.getActionInfo(request);
	    	
	    	// 获取主表信息
			Map<String, String> mainTable = info.getMainMap();
			SQR = Util.null2String(mainTable.get("SQR"));
			SQRQ = Util.null2String(mainTable.get("SQRQ"));
			ZB = Util.null2String(mainTable.get("ZB"));
			ZHBH = Util.null2String(mainTable.get("ZHBH"));
			GC = Util.null2String(mainTable.get("GC"));
			ZRR = Util.null2String(mainTable.get("ZRR"));
			SM = Util.null2String(mainTable.get("SM"));
			
	    	// 获取明细表1信息
		    List<Map<String, String>> detailAList = info.getDetailMap("1");
		    if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					String mxidDetailA = Util.null2String(detailAMap.get("id"));	//明细ID
					String skuDetailA = Util.null2String(detailAMap.get("SKU"));	//SKU
					String pmDetailA = Util.null2String(detailAMap.get("PM"));		//品名
					String[] gb = {"US","CA","UK","DE","FR","IT","ES"};
					for(int j=0; j<gb.length; j++){
						String gbDetailA = gb[j];														// 国别																
						String gjDetailA = "";															// 国家
						sql = "select selectvalue from workflow_SelectItem where fieldid = '39683' and selectname = '" + gbDetailA + "'";
						rs.execute(sql);
						if (rs.next()){
							gjDetailA = rs.getString("selectvalue");
				        }
						String mjDetailA = Util.null2String(detailAMap.get(gb[j]+"MJ"));				// 卖价														
						String cbslDetailA = Util.null2String(detailAMap.get(gb[j]+"CBSL"));			// 初步数量														
						String jjsxDetailA = Util.null2String(detailAMap.get(gb[j]+"JJSX"));			// 季节属性														
						String jrsxDetailA = Util.null2String(detailAMap.get(gb[j]+"JRSX"));			// 节日属性														
						String cpztDetailA = Util.null2String(detailAMap.get(gb[j]+"CPZT"));			// 产品状态														
						String xsbzDetailA = Util.null2String(detailAMap.get(gb[j]+"XSBZ"));			// 销售备注														
						String kfqrztDetailA = Util.null2String(detailAMap.get(gb[j]+"KFQRZT"));		// 开发确认														
						String dyhhDetailA = Util.null2String(detailAMap.get(gb[j]+"DYHH"));			// 对应货号														
						
						if(!"".equals(cpztDetailA)){
							String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
							"<ROOT>" +
								"<header>" +
									"<userid>"+SQR+"</userid>" +
									"<modeid>4443</modeid>" +
									"<id/>" +
								"</header>" +
								"<search>" +
								"<condition/>" +
								"<right>Y</right>" +
								"</search>" +
								"<data id=\"\">" +
									"<maintable>" +
										"<field>" +
											"<filedname>SQR</filedname>" +
											"<filedvalue>"+SQR+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>SQRQ</filedname>" +
											"<filedvalue>"+SQRQ+"</filedvalue>" +
										"</field>" +
										"<field>" +
										    "<filedname>ZB</filedname>" +
										    "<filedvalue>"+ZB+"</filedvalue>" +
									    "</field>" +
									    "<field>" +
										    "<filedname>ZHBH</filedname>" +
										    "<filedvalue>"+ZHBH+"</filedvalue>" +
									    "</field>" +
										"<field>" +
											"<filedname>GC</filedname>" +
											"<filedvalue>"+GC+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>SM</filedname>" +
											"<filedvalue>"+SM+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>ZRR</filedname>" +
											"<filedvalue>"+ZRR+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>SKU</filedname>" +
											"<filedvalue>"+skuDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>PM</filedname>" +
											"<filedvalue>"+pmDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>GB</filedname>" +
											"<filedvalue>"+gjDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>MJ</filedname>" +
											"<filedvalue>"+mjDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>CBSL</filedname>" +
											"<filedvalue>"+cbslDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>JJSX</filedname>" +
											"<filedvalue>"+jjsxDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>JRSX</filedname>" +
											"<filedvalue>"+jrsxDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>CPZT</filedname>" +
											"<filedvalue>"+cpztDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>XSBZ</filedname>" +
											"<filedvalue>"+xsbzDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>KFQR</filedname>" +
											"<filedvalue>"+kfqrztDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>DYHH</filedname>" +
											"<filedvalue>"+dyhhDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>LC</filedname>" +
											"<filedvalue>"+requestid+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>ZT</filedname>" +
											"<filedvalue>0</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>MXID</filedname>" +
											"<filedvalue>"+mxidDetailA+"</filedvalue>" +
										"</field>" +
										"<field>" +
											"<filedname>BS</filedname>" +
											"<filedvalue>"+mxidDetailA+"-"+gbDetailA+"</filedvalue>" +
										"</field>" +
									"</maintable>" +
								"</data>" +
							"</ROOT>";
							
							if (!StringUtils.isBlank(xml)) {
								xml = xml.replaceAll("&nbsp;", " ");
								xml = xml.replaceAll("\r", " ");
								xml = xml.replaceAll("\n", " ");
								xml = xml.replaceAll("<br>", ";");
								xml = xml.replaceAll("<br/>", ";");
								xml = xml.replaceAll("&quot;", "\"");
								xml = xml.replaceAll("'", "''");
							}
							
							ModeDataServiceImpl mdsi = new ModeDataServiceImpl();
							String returncode = mdsi.saveModeData(xml);
							System.out.println(returncode);
						}
					}
				}
		    }
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