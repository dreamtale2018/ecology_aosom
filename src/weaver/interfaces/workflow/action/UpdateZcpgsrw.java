package weaver.interfaces.workflow.action;

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
 * 产品客户中差评改善任务分配-子表结束节点更新相关内容到产品客户中差评改善任务分配表<br>
 * 
 * @author ycj
 *
 */
public class UpdateZcpgsrw implements Action
{
  private Log logger = LogFactory.getLog(UpdateZcpgsrw.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    
    String ZBLJ = "";	//主表流程链接
    String sql = "";
    try
    {
    	ActionInfo info = ActionUtils.getActionInfo(request);
    	
    	// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		ZBLJ = Util.null2String(mainTable.get("ZBLJ"));
		
    	// 获取明细表1信息
	    List<Map<String, String>> detailAList = info.getDetailMap("1");
	    if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String zbmxidDetailA = Util.null2String(detailAMap.get("ZBMXID"));	//主表明细ID
				String gsfaDetailA = Util.null2String(detailAMap.get("GSFA"));		//改善方案
				String gsfafjDetailA = Util.null2String(detailAMap.get("GSFAFJ"));	//改善方案附件
				String wcrqDetailA = Util.null2String(detailAMap.get("WCRQ"));		//完成日期
				String gsjgDetailA = Util.null2String(detailAMap.get("GSJG"));		//改善结果
				gsfaDetailA = gsfaDetailA.replaceAll("&nbsp;", " ");
				gsfaDetailA = gsfaDetailA.replaceAll("\r", " ");
				gsfaDetailA = gsfaDetailA.replaceAll("\n", " ");
				gsfaDetailA = gsfaDetailA.replaceAll("<br>", " ");
				gsfaDetailA = gsfaDetailA.replaceAll("&quot;", "\"");
				gsfaDetailA = gsfaDetailA.replaceAll("'", "''");
				String mainid = "";
				sql = "select id from formtable_main_207 where requestid = '" + ZBLJ + "'";
				rs.execute(sql);
				if (rs.next()){
					mainid = rs.getString("id");
		        }
				if(mainid!=null && !"".equals(mainid)){
					sql = "update formtable_main_207_dt1 set GSFA='"+ gsfaDetailA + "',GSFAFJ='" + gsfafjDetailA +
					"',WCRQ='"+ wcrqDetailA + "',GSJG='" + gsjgDetailA + "' where mainid = '" + mainid + "' and id='"+ zbmxidDetailA +"'";
					rs.execute(sql);
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