package weaver.interfaces.workflow.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 订购会新品确认子表流程<br>
 * 到达结束节点,更新订购会新品确认主表,并更新订购会管理台账状态
 *
 * @author ycj
 *
 */
public class UpdateXpqrzbJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateXpqrzbJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
	  RecordSet rs = new RecordSet();
	    
	    String requestid = request.getRequestid();

	    String sql = "";
	    try
	    {
	    	ActionInfo info = ActionUtils.getActionInfo(request);
	    	
	    	//更新订购会管理台账状态
	    	sql = "update formtable_main_297 set ZT='1' where lc = '"+ requestid +"'";
	    	rs.execute(sql);
	    	// 获取流程明细表 1
			List<Map<String, String>> detailAList = info.getDetailMap("1");
			if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					String idDetailA = Util.null2String(detailAMap.get("id"));					//明细ID
					String mxidDetailA = Util.null2String(detailAMap.get("MXID"));				//主表明细ID
					String[] gb = {"US","CA","UK","DE","FR","IT","ES"};
					for(int j=0; j<gb.length; j++){
						String gbDyhh = Util.null2String(detailAMap.get(gb[j]+"DYHH"));			//对应货号
						String gbKfqr = Util.null2String(detailAMap.get(gb[j]+"KFQRZT"));		//开发确认
						String gbKfbz = Util.null2String(detailAMap.get(gb[j]+"KFBZ"));			//开发备注
						String gbDylc = Util.null2String(detailAMap.get(gb[j]+"DYLC"));			//对应流程
						String gbZt = Util.null2String(detailAMap.get(gb[j]+"ZT"));				//状态
						//更新订购会新品确认主表流程
						sql = "update formtable_main_295_dt1 set " + gb[j] + "DYHH = '" + gbDyhh +
						      "'," + gb[j] + "KFBZ = '" + gbKfbz + "'," + gb[j] + "DYLC = '" + gbDylc +
						      "'," + gb[j] + "ZT = '" + gbZt + "' where id = '" + mxidDetailA + "'";
				    	rs.execute(sql);
				    	//更新订购会管理台账开发备注
				    	sql = "update formtable_main_297 set KFBZ = '" + gbKfbz + "',KFQR = '" + gbKfqr +
				    			"' where mxid = '" + idDetailA + "' and gb = '" + j + "'";
				    	rs.execute(sql);
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