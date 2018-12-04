package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateYprkglWb implements Action
{
  private Log logger = LogFactory.getLog(UpdateYprkglWb.class);
  
  @Override
  public String execute(RequestInfo request)
  {
    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    RecordSet rs2 = new RecordSet();
    RecordSet rs3 = new RecordSet();
    
    String requestid = request.getRequestid();
    String sql = "";
    String sql1 = "";
    String sql2 = "";
    String sql3 = "";
    String TJLXR = ""; 	//退件联系人
    String TJLXFS = ""; //退件联系方式
    String TJDZ = ""; 	//退件地址
    try
    {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String RKRQ = sdf.format(date);
		sql = "update uf_ypcrktz set RKRQ='" + RKRQ + "' where rklc = '"+ requestid +"'";
		rs.execute(sql);
		
		ActionInfo info = ActionUtils.getActionInfo(request);
		
		// 获取主表信息
		Map<String, String> mainTable = info.getMainMap();
		TJLXR = Util.null2String(mainTable.get("TJLXR"));
		TJLXFS = Util.null2String(mainTable.get("TJLXFS"));
		TJDZ = Util.null2String(mainTable.get("TJDZ"));
    	// 获取流程明细表 1
		List<Map<String, String>> detailAList = info.getDetailMap("1");
		if (detailAList != null && detailAList.size() > 0) {
			for (int i = 0; i < detailAList.size(); i++) {
				Map<String, String> detailAMap = detailAList.get(i);
				String pztzIDDetailA = Util.null2String(detailAMap.get("PZTZID"));	//拍照台账ID
				String pzxqbIDDetailA = Util.null2String(detailAMap.get("PZXQBID"));//拍照需求表流程ID
				if(!pzxqbIDDetailA.equals("")){
					sql = "update formtable_main_157 set BYWCRQ='"+ RKRQ +"',PZLXR='"+ TJLXR + "',RKDLC='"+ requestid +
							"',DH='"+ TJLXFS +"',DZ='"+ TJDZ +"' where requestid = '"+ pzxqbIDDetailA +"'";
					rs.execute(sql);
				}
				if (!pztzIDDetailA.equals("")){
					sql = "update formtable_main_159 set YPZT='0',BYWCRQ='"+ RKRQ +"' where id = '"+ pztzIDDetailA +"'";
					rs.execute(sql);
				}else{
					String hhDetailA = Util.null2String(detailAMap.get("HH"));//明细表中的货号,此货号为ID
					sql1 = "select segment1 from uf_product where id = '"+ hhDetailA +"'";
					rs1.execute(sql1);
					if (rs1.next()){
						hhDetailA = rs1.getString("segment1");
					}
					if(hhDetailA!=null && !"".equals(hhDetailA)){
						sql2 = "select id from formtable_main_159 where hh = '"+ hhDetailA +"' " +
						"order by modedatacreatedate desc,modedatacreatetime desc";
						rs2.execute(sql2);
						if (rs2.next()){
							String id = rs2.getString("id");
							sql3 = "update formtable_main_159 set YPZT='0',BYWCRQ='"+ RKRQ +"' where id = '"+ id +"'";
							rs3.execute(sql3);
						}
					}
				}
			}
		}
	}
        //this.logger.error("sql：" + sql);
        
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