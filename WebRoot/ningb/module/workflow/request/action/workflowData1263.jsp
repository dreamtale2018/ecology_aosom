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
	String hth = Util.null2String(request.getParameter("hth"));	// 合同号
	String gys = Util.null2String(request.getParameter("gys"));	// 供应商
	if (gys == null || "".equals(gys) || hth == null || "".equals(hth)) {
		out.print(callback(code, message, data));
		return;
	}
	
	String hhIDList = "";		// 货号ID, 多个逗号隔开
	String hhNameList = "";		// 货号名称, 多个逗号隔开
	String pmList = "";			// 产品品名, 多个逗号隔开
	String pztzIDList = "";		// 拍照台账ID, 多个逗号隔开
	String pzxqbIDList = "";	// 拍照需求表ID, 多个逗号隔开
	String pzddList = "";		// 样品地点, 多个逗号隔开
	String sql = "select b.id,b.segment1,b.item_name_cn,a.id,a.lc,a.ypdd "
			+ "from formtable_main_159 a "
			+ "join uf_product b on b.segment1 = a.hh "
			+ "join uf_vendor c on c.vendor_name = a.gys "
			+ "where a.pzzt = '0' and a.gxhth = ? and c.id = ?";
	rs.executeQuery(sql, hth, gys);
	while (rs.next()) {
		if ("".equals(hhIDList)) {
			hhIDList = rs.getString(1);
			hhNameList = rs.getString(2);
			pmList = rs.getString(3).replaceAll(",","，");
			pztzIDList = rs.getString(4);
			pzxqbIDList = rs.getString(5);
			pzddList = rs.getString(6);
		} else {
			hhIDList += "," + rs.getString(1);
			hhNameList += "," + rs.getString(2);
			pmList += "," + rs.getString(3).replaceAll(",","，");
			pztzIDList += "," + rs.getString(4);
			pzxqbIDList += "," + rs.getString(5);
			pzddList += "," + rs.getString(6);
		}
	}
	data.put("hhIDList", hhIDList);
	data.put("hhNameList", hhNameList);
	data.put("pmList", pmList);
	data.put("pztzIDList", pztzIDList);
	data.put("pzxqbIDList", pzxqbIDList);
	data.put("pzddList", pzddList);
	
	out.print(callback(code, message, data));					// 有返回数据
%>