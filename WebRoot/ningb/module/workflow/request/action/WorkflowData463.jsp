<%@ page language="java" import="java.util.*,weaver.general.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="net.sf.json.JSONArray,net.sf.json.JSONObject"%>
<%@ page language="java" import="weaver.hrm.*" %>
<%@ page language="java" import="weaver.conn.RecordSet" %>
<%@ page language="java" import="java.sql.Timestamp" %>
<%@ page language="java" import="java.text.SimpleDateFormat" %>
<%@ page language="java" import="java.text.ParseException" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
<jsp:useBean id="basebean" class="weaver.general.BaseBean" scope="page" />
<%
	JSONObject message = new JSONObject();		// 返回的信息
	// 验证用户是否登录
	User user = HrmUserVarify.getUser(request, response);
	if (user == null) {
		message.put("success", "false");
		message.put("error", "not login!");
		
		JSONObject data = new JSONObject();			// 返回的数据
	
		JSONObject result = new JSONObject();
		result.put("message", message);
		
		out.print(result.toString());
		return;
	}

	
	int workflowid = Util.getIntValue(request.getParameter("workflowid"));			// 流程id
	int requestid = Util.getIntValue(request.getParameter("requestid"));			// 请求id
	int userid = user.getUID();														// 用户id
	
	
	// 获取流程自定义表
	//String tablename = WorkflowUtil.getTablename(workflowid + "");
	String tablename = "formtable_main_113";

	
	JSONObject data = new JSONObject();
	// 获取拍照需求表货号
	//String hh = "";
	//String sql = "select hh from " + tablename
	//			+ " where requestid = '" + requestid + "'";
	//rs.execute(sql);
	//basebean.writeLog("select HH sql: " + sql);
	//if (rs.next()) hh = rs.getString("hh");
	String hh = request.getParameter("hh");
	if (!"".equals(hh)) {
		// 从新品排单表中获取产品图片
		String sqlTp = "select tp from formtable_main_104_dt1 a "
						+ " left join formtable_main_104 b on a.mainid = b.id "
						+ " left join workflow_requestbase c on b.requestId = c.requestid "
						+ " where hh2 = '" + hh + "' "
						+ " and c.currentnodetype = 3 ";
		rs.execute(sqlTp);
		if (rs.next()) {
			String cptp = Util.null2String(rs.getString("tp"));
			
			data.put("cptp", cptp);
			//String sqlTp1 = "update " + tablename + " set cptp = '" + cptp + "'"
			// 				+ " where requestid = '" + requestid + "'";
			//rs.execute(sqlTp1);
			//basebean.writeLog("update sqlTp1 sql: " + sqlTp1);
		}
		
	}
	
	
	message.put("success", "true");
	
	JSONObject result = new JSONObject();
	result.put("message", message);
	result.put("data", data);
	
	out.print(result.toString());
%>