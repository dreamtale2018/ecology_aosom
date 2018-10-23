package weaver.interfaces.workflow.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestManager;

public class UpdateBGHttz
  implements Action
{
  private Log logger = LogFactory.getLog(UpdateBGHttz.class);

  public String execute(RequestInfo request)
  {
    String requestid = request.getRequestid();
    String workflowid = request.getWorkflowid();
    String formid = "";
    String tablename = "";

    RecordSet rs = new RecordSet();
    RecordSet rs1 = new RecordSet();
    String billid = "";
    String id1 = "";
    String id2 = "";

    String yhth = "";
    String ljyf = "";
    String sywf = "";
    String htbgje = "";
    String sql = "";
    String sql1 = "";
    try
    {
      sql = "select * from workflow_base where id = '" + workflowid + "'";
      rs.execute(sql);
      if (rs.next()) {
        formid = Util.null2String(rs.getString("formid"));
      }

      this.logger.error("formid：" + formid);

      sql = "select * from workflow_bill where id = '" + 
        formid + "' ";
      rs.execute(sql);
      if (rs.next()) {
        tablename = Util.null2String(rs.getString("tablename"));
      }

      this.logger.error("tablename：" + tablename);

      sql = "select * from " + tablename + " where requestid =" + requestid;
      rs.execute(sql);
      if (rs.next()) {
        id1 = Util.null2String(rs.getString("id"));
        yhth = Util.null2String(rs.getString("yhth"));
        ljyf = Util.null2String(rs.getString("ljyf"));
        sywf = Util.null2String(rs.getString("sywf"));
        htbgje = Util.null2String(rs.getString("bghhtje"));
        sql = "select * from uf_httz where hth =" + yhth;
        rs.execute(sql);
        if (rs.next()) {
          id2 = Util.null2String(rs.getString("id"));
        }
        sql1 = "insert into uf_httz_dt2(mainid,htbglc,bgje) values('" + id2 + "','" + id1 + "','" + htbgje + "')";
        rs1.execute(sql1);
        sql1 = "insert into uf_httz(htje,ljyfje,sywfje) values('" + htbgje + "','" + ljyf + "','" + sywf + "') where hth=" + yhth;
        rs1.execute(sql1);
      }
    }
    catch (Exception e) {
      this.logger.error("Exception e", e);
      request.getRequestManager().setMessageid("1111111111");
      request.getRequestManager().setMessagecontent(e + ",请联系管理员.");
      return "0";
    }

    return "1";
  }
}