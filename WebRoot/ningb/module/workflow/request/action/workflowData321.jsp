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
	String ppname = "";			// 品牌, 多个逗号隔开
	String sql = "select a.segment1,c.org_information5 "
			+ "from uf_product a "
			+ "inner join uf_seasproduct b on a.standard_item_id=b.standard_item_id and b.ou_item_status_code!='STOPPED'"
			+ "inner join uf_gb c on b.org_id = c.organization_id "
			+ "where a.id = ?";
	rs.executeQuery(sql, hh);
	while (rs.next()) {
		if ("".equals(gbname)) {
			String gb = rs.getString("org_information5");
			String sql1 = "select d.selectname,a.hh from formtable_main_196_dt1 a "
					+ "inner join formtable_main_196 b on b.id = a.mainid "
					+ "inner join uf_product c on a.hh = c.segment1 "
					+ "inner join workflow_SelectItem d on d.selectvalue = b." + gb + "pp "
					+ "where d.fieldid = '14199' "
					+ "and c.id = ?";
			rs1.executeQuery(sql1, hh);
			if(rs1.next()){
				ppname = rs1.getString("selectname");
			}
			gbname = gb;
		} else {
			String gb = rs.getString("org_information5");
			String sql1 = "select d.selectname,a.hh from formtable_main_196_dt1 a "
					+ "inner join formtable_main_196 b on b.id = a.mainid "
					+ "inner join uf_product c on a.hh = c.segment1 "
					+ "inner join workflow_SelectItem d on d.selectvalue = b." + gb + "pp "
					+ "where d.fieldid = '14199' "
					+ "and c.id = ?";
			rs1.executeQuery(sql1, hh);
			if(rs1.next()){
				ppname += "," + rs1.getString("selectname");
			}
			gbname += "," + gb;
		}
	}
	data.put("gbname", gbname);
	data.put("ppname", ppname);
	
	out.print(callback(code, message, data));					// 有返回数据
%>