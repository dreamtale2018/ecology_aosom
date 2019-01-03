package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;
/**
 * 说明书制作流程结束节点更新新品排单反馈表和供应商更换反馈表<br>
 * 
 * @author ycj
 *
 */
public class UpdateSmszzlc implements Action
{
  private Log logger = LogFactory.getLog(UpdateSmszzlc.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    
	RecordSet rs = new RecordSet();
	    
	String sql = "";
	String XPPDFKLC = "";		//新品排单反馈流程
	String XPPDFKLCMAIN = "";	//新品排单反馈流程MAIN
	String GYSGHLC = "";		//供应商更换反馈流程
	String GYSGHLCMAIN = "";	//供应商更换反馈流程MAIN
	String WCRQ = "";		//完成日期
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		XPPDFKLC = Util.null2String(mainTable.get("XPPDFKLC"));
		GYSGHLC = Util.null2String(mainTable.get("GYSGHLC"));
		WCRQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String sfxyzzDetailA = Util.null2String(detailAMap.get("SFXYZZ"));	//是否需要制作
				String hhDetailA = Util.null2String(detailAMap.get("HH"));			//货号
				if(!"".equals(XPPDFKLC)){
					sql = "select id from formtable_main_105 where requestid = '" + XPPDFKLC + "'";
					rs.execute(sql);
					if(rs.next()){
						XPPDFKLCMAIN = Util.null2String(rs.getString("id"));
						sql = "update formtable_main_105_dt1 set sfxyzz = '" + sfxyzzDetailA + "',smslczt = '4'," +
						"smsqrrq = '" + WCRQ + "' where mainid = '" + XPPDFKLCMAIN + "' and HH3 = '" + hhDetailA + "'";
						rs.execute(sql);
					}
				}
				if(!"".equals(GYSGHLC)){
					sql = "select id from formtable_main_218 where requestid = '" + GYSGHLC + "'";
					rs.execute(sql);
					if(rs.next()){
						GYSGHLCMAIN = Util.null2String(rs.getString("id"));
						sql = "update formtable_main_218_dt1 set sfxyzz = '" + sfxyzzDetailA + "',smszt = '4'," +
						"smsqrrq = '" + WCRQ + "' where mainid = '" + GYSGHLCMAIN + "' and HH = '" + hhDetailA + "'";
						rs.execute(sql);
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