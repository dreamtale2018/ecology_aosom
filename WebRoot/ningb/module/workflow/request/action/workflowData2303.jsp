<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*,weaver.general.*" %>
<%@ page language="java" import="net.sf.json.JSONArray,net.sf.json.JSONObject"%>
<%@ page language="java" import="weaver.hrm.*" %>
<%@ page language="java" import="weaver.conn.RecordSet" %>
<jsp:useBean id="basebean" class="weaver.general.BaseBean" scope="page" />
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
<%
	String code = "0";								// 返回编码：0.成功   -1.未登录
	String message = "请求成功";						// 返回信息

	// 验证用户是否登录
	User user = HrmUserVarify.getUser(request, response);
	if (user == null) {
		code = "-1";
		message = "not login!";
		out.print(callback(code, message));
		return;
	}
	
	JSONObject data = new JSONObject();							// 返回的数据
	RecordSet rs = new RecordSet();
	
	String userid = "";												// 用户ID
	String username = user.getUsername();						// 用户姓名
	String sql = "select id from hrmresource where lastname like '%" + username + "%'";
	rs.executeQuery(sql);
	if (rs.next()) {
		userid = rs.getString("id");
	}
	data.put("userid",userid);
	
	out.print(callback(code, message, data));					// 有返回数据
%>