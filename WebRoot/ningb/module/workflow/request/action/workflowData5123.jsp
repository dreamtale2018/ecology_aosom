<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*,weaver.general.*" %>
<%@ page language="java" import="net.sf.json.JSONArray,net.sf.json.JSONObject"%>
<%@ page language="java" import="weaver.hrm.*" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
<jsp:useBean id="rs1" class="weaver.conn.RecordSet" scope="page"/>
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
	
	String gbname = "";			// 国别, 多个逗号隔开
	String bjid = "";			// 编辑id, 多个逗号隔开
	String bjname = "";			// 编辑name, 多个逗号隔开
	String sql = "select a.segment1,c.org_information5 "
			+ "from uf_product a "
			+ "inner join uf_seasproduct b on a.standard_item_id=b.standard_item_id and b.ou_item_status_code!='STOPPED'"
			+ "inner join uf_gb c on b.org_id = c.organization_id "
			+ "where a.id = ?";
	rs.executeQuery(sql, hh);
	while (rs.next()) {
		if ("".equals(gbname)) {
			String gb = rs.getString("org_information5");
			String sql1 = "select b.lastname,a.gbbj from uf_CPXXCLR a "
					+ "inner join hrmresource b on b.id = a.gbbj  "
					+ "where a.gbjc = ? ";
			rs1.executeQuery(sql1, gb);
			if(rs1.next()){
				bjid = rs1.getString("gbbj");
				bjname = rs1.getString("lastname");
			}
			gbname = gb;
		} else {
			String gb = rs.getString("org_information5");
			String sql1 = "select b.lastname,a.gbbj from uf_CPXXCLR a "
					+ "inner join hrmresource b on b.id = a.gbbj  "
					+ "where a.gbjc = ? ";
			rs1.executeQuery(sql1, gb);
			if(rs1.next()){
				bjid += "," + rs1.getString("gbbj");
				bjname += "," + rs1.getString("lastname");
			}
			gbname += "," + gb;
		}
	}
	data.put("gbname", gbname);
	data.put("bjid", bjid);
	data.put("bjname", bjname);
	
	out.print(callback(code, message, data));					// 有返回数据
%>