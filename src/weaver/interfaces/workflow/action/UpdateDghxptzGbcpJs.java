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
 * 扩增国别表流程结束节点更新订购会新品台账状态和对应货号<br>
 * 
 * @author ycj
 *
 */
public class UpdateDghxptzGbcpJs implements Action
{
  private Log logger = LogFactory.getLog(UpdateDghxptzGbcpJs.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String JSRQ = "";	//结束日期
    
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
		JSRQ = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    	
		// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String bsDetailA = Util.null2String(detailAMap.get("BS"));					//标识
				sql = "update formtable_main_297 set ZT='2' where bs = '" + bsDetailA + "'";
				rs.execute(sql);
				//更新订购会新品台账对应货号
				String hhDetail = Util.null2String(detailAMap.get("HH"));					//货号
				sql = "select segment1 from uf_product where id = '" + hhDetail + "'";
				rs.execute(sql);
				if(rs.next()){
					hhDetail = Util.null2String(rs.getString("segment1"));
				}
				String idDetail = "";														//明细ID
				String gbDetail = "";														//国别
				if(bsDetailA.indexOf("-")!=-1){
					idDetail = bsDetailA.split("-")[0];
					gbDetail = bsDetailA.split("-")[1];
				}
				int gb = 0;
				String[] gbArr = {"US","CA","UK","DE","FR","IT","ES"};
				for(int j=0; j<gbArr.length; j++){
					if(gbArr[j].equals(gbDetail)){
						gb = j;
					}
				}
		    	sql = "update formtable_main_297 set DYHH = '" + hhDetail + "',JSRQ = '" + JSRQ + 
		    			"' where mxid = '" + idDetail + "' and gb = '" + gb + "'";
		    	rs.execute(sql);
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