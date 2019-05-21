package weaver.interfaces.workflow.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;

public class DinnerOperationDB
{
  RecordSet rs = new RecordSet();
  private Log log = LogFactory.getLog(DinnerOperationDB.class.getName());

  public JSONArray GetGysList() {
    String json = "";
    JSONArray ja = new JSONArray();
    this.rs.executeSql("select id,gysmc from uf_gysxxb order by id ");
    while (this.rs.next()) {
      JSONObject jo = new JSONObject();
      jo.put("id", this.rs.getString(1));
      jo.put("gysmc", this.rs.getString(2));
      ja.add(jo);
    }
    return ja;
  }

  public JSONArray GetDcrqList(String ksrq, String jsrq) throws ParseException {
    String json = "";
    JSONArray ja = new JSONArray();
    this.rs.executeSql("select distinct syrq  from uf_gystcxxb where syrq>='" + ksrq + "' and syrq<='" + jsrq + 
      "' and  zt='y' order by syrq ");

    while (this.rs.next()) {
      JSONObject jo = new JSONObject();
      jo.put("syrq", this.rs.getString(1));
      jo.put("lb", getWeekOfDate(this.rs.getString(1)));
      ja.add(jo);
    }

    return ja;
  }

  public JSONArray GetDinnerByGysList(String syrq, String gysid) throws ParseException {
    String json = "";
    JSONArray ja = new JSONArray();
    this.rs.executeSql(
      "select id,tcmc,dj from uf_gystcxxb  where gysId= " + gysid + " and syrq ='" + syrq + "'  and zt='y'  order by tcbh ");

    while (this.rs.next()) {
      JSONObject jo = new JSONObject();
      jo.put("id", this.rs.getString(1));
      jo.put("tcmc", this.rs.getString(2));
      jo.put("dj", this.rs.getString(3));
      ja.add(jo);
    }

    return ja;
  }

  public JSONArray GetRpByDeptList(String syrq, String deptid) throws ParseException {
    String json = "";
    JSONArray ja = new JSONArray();
//    this.rs.executeSql(" select b.id,b.gysmc, sum(a.sl) sl from  uf_ygdcmxb a,uf_gysxxb b,HrmResource c  where  a.gysid=b.id and a.dcr=c.id  and dcrq='" + 
//      syrq + "' and c.departmentid= " + deptid + 
//      " and zt=0 group by b.gysmc,b.id");
    this.rs.executeSql(" select b.id,b.gysmc, sum(a.sl) sl from  uf_ygdcmxb a,uf_gysxxb b where a.gysid=b.id and dcrq='" + 
    	      syrq + "' and a.sdbm= " + deptid + 
    	      " and zt=0 group by b.gysmc,b.id");

    while (this.rs.next()) {
      JSONObject jo = new JSONObject();
      jo.put("id", this.rs.getString(1));
      jo.put("gysmc", this.rs.getString(2));
      jo.put("sl", this.rs.getString(3));
      ja.add(jo);
    }

    return ja;
  }

  public JSONArray GetRpmxByDeptList(String syrq, String deptid, String gysid) throws ParseException {
    String json = "";
    JSONArray ja = new JSONArray();
//    this.rs.executeSql(
//      " select  c.lastname dcr,a.tcmc,a.sl,case when kw=0 then '不辣' else  '辣' end kw ,b.gysmc ,a.dj  from  uf_ygdcmxb a,uf_gysxxb b,HrmResource c  where  a.gysid=b.id and a.dcr=c.id  and dcrq='" + 
//      syrq + "' and c.departmentid=" + deptid + " and a.gysid=" + gysid + " and zt=0");
    this.rs.executeSql(
    		" select  c.lastname dcr,a.tcmc,a.sl,case when kw=0 then '不辣' else  '辣' end kw ,b.gysmc ,a.dj  from  uf_ygdcmxb a,uf_gysxxb b,HrmResource c  where  a.gysid=b.id and a.dcr=c.id  and dcrq='" + 
    		syrq + "' and a.sdbm=" + deptid + " and a.gysid=" + gysid + " and zt=0");

    while (this.rs.next()) {
      JSONObject jo = new JSONObject();
      jo.put("dcr", this.rs.getString(1));
      jo.put("tcmc", this.rs.getString(2));
      jo.put("sl", this.rs.getString(3));
      jo.put("kw", this.rs.getString(4));
      jo.put("gysmc", this.rs.getString(5));
      jo.put("dj", this.rs.getString(6));
      ja.add(jo);
    }

    return ja;
  }

  public static String getWeekOfDate(String syrq) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date dt = sdf.parse(syrq);

    String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
    Calendar cal = new GregorianCalendar();
    cal.setTime(dt);
    int w = cal.get(7) - 1;
    if (w < 0)
      w = 0;
    return weekDays[w];
  }

  public boolean IsHasOrder(String syrq, String dcr) throws ParseException {
    boolean flag = false;
    this.rs.executeSql("select * from uf_ygdcmxb where dcrq='" + syrq + "' and dcr =" + dcr);
    if (this.rs.getCounts() > 0) {
      flag = true;
    }

    return flag;
  }
}