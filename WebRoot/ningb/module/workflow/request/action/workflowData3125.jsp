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
	
	JSONObject data = new JSONObject();									// 返回的数据
	String wlbm = Util.null2String(request.getParameter("wlbm"));		// 物料编码
	String sl = Util.null2String(request.getParameter("sl"));			// 数量
	if (wlbm == null || "".equals(wlbm) || sl == null || "".equals(sl) ) {
		out.print(callback(code, message, data));
		return;
	}
	
	String dqkcsl = "";		// 当前可用库存
	String flag = "";		// 是否报错
	String sql = "select dqkcsl from uf_KCTZ where wlbm = ?";
	rs.executeQuery(sql, wlbm);
	if (rs.next()) {
		dqkcsl = rs.getString("dqkcsl");
		if(Integer.parseInt(dqkcsl)<Integer.parseInt(sl)){
			flag = "物料"+wlbm+"数量不足！"; 
		}else{
			flag = "true"; 
		}
	}else{
		flag = "库存中无"+wlbm+"库存信息！"; 
	}
	data.put("flag", flag);
	
	out.print(callback(code, message, data));					// 有返回数据
%>