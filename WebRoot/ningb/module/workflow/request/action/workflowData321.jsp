<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*,weaver.general.*" %>
<%@ page language="java" import="net.sf.json.JSONArray,net.sf.json.JSONObject"%>
<%@ page language="java" import="weaver.hrm.*" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
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
	String hh = Util.null2String(request.getParameter("hh"));	// 货号
	if (hh == null || "".equals(hh)) {
		out.print(callback(code, message, data));
		return;
	}
	
	String gbname = "";		// 国别, 多个逗号隔开
	String sql = "select a.segment1,b.org_information5 "
			+ "from uf_product a "
			+ "join uf_gb b on a.org_id = b.organization_id "
			+ "where a.segment1 = ?";
	rs.executeQuery(sql, hh);
	while (rs.next()) {
		if ("".equals(gbname)) {
			gbname = rs.getString("org_information5");
		} else {
			gbname += "," + rs.getString("org_information5");
		}
	}
	data.put("gbname", gbname);
	
	out.print(callback(code, message, data));					// 有返回数据
%>