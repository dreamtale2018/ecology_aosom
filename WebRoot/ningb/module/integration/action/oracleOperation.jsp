<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*,weaver.general.*" %>
<%@ page language="java" import="net.sf.json.JSONArray,net.sf.json.JSONObject" %>
<%@ page language="java" import="weaver.hrm.*" %>
<%@ page language="java" import="java.text.SimpleDateFormat,java.text.ParseException" %>
<%@ page language="java" import="com.weaver.ningb.direct.manager.integration.*" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
<jsp:useBean id="baseBean" class="weaver.general.BaseBean" scope="page" />
<%!
	/**
	 * 创建返回信息
	 * 
	 * @param code
	 * 					返回编码
	 * @param message
	 * 					返回信息
	 * @return 返回信息, JSON 字符串
	 */
	public String callback(String code, String message) {
		return callback(code, message, null);
	}

	/**
	 * 创建返回信息
	 * 
	 * @param code
	 * 					返回编码
	 * @param message
	 * 					返回信息
	 * @param data
	 					返回数据
	 * @return 返回信息, JSON 字符串
	 */
	public String callback(String code, String message, Object data) {
		JSONObject result = new JSONObject();
		result.put("code", code);
		result.put("message", message);
		if (data != null) result.put("data", data);
		return result.toString();
	}
%>
<%!
	public Date getDateStr(String dateStr) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateStr);
		} catch (ParseException e) {}
		return date;
	}
%>
<%
	String code = "0";								// 返回编码：0.成功   -1.未登录
	String message = "请求成功";						// 返回信息

	User user = HrmUserVarify.getUser(request, response);
	if (user == null) {
		code = "-1";
		message = "not login!";
		out.print(callback(code, message));
		return;
	}
	
	
	String type = Util.null2String(request.getParameter("type"));
	String startDate = Util.null2String(request.getParameter("startDate"));
	String endDate = Util.null2String(request.getParameter("endDate"));
	
	// 同步信息
	OracleManager manager = new OracleManager();
	boolean flag = true;
	if (type == null || "".equals(type)) {
	} else if ("0".equals(type)) {
		Calendar calendar = Calendar.getInstance();
		// 设置自定义事件
		if (startDate != null && !"".equals(startDate)) {
			calendar.setTime(getDateStr(startDate));
			
		// 设置时间为当前时间前的 4 个小时
		} else {
			calendar.setTime(new Date(System.currentTimeMillis()));
			calendar.add(Calendar.HOUR_OF_DAY, -4);
			//calendar.add(Calendar.DAY_OF_YEAR, -20);
		}
		flag = manager.syncQuery(calendar);
	}
	JSONObject data = new JSONObject();
	data.put("flag", flag + "");

	out.print(callback(code, message, data));					// 有返回数据
%>