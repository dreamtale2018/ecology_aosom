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
	
	public String getName(String personName){
		if(personName.indexOf("`~`7")!=-1 && personName.indexOf("`~`8")!=-1){
			personName = personName.split("`~`7")[1].split("`~`8")[0].trim(); 
		}else{
			personName = personName.split("`~`7")[0].split("`~`8")[0].trim();
		}
		return personName;
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
	String cpdl = Util.null2String(request.getParameter("cpdl"));		// 产品大类
	String cpzl = Util.null2String(request.getParameter("cpzl"));		// 产品中类
	if (cpdl == null || "".equals(cpdl) || cpzl == null || "".equals(cpzl)) {
		out.print(callback(code, message, data));
		return;
	}
	
	String sjId = "";		// 设计ID
	String sjName = "";		// 设计Name
	String bjyywzId = "";	// 编辑英语文字ID
	String bjyywzName = "";	// 编辑英语文字Name
	String sql = "select sj,bjsjty from uf_YXBLCRY where cpdl = ? and cpzl = ?";
	rs.executeQuery(sql, cpdl, cpzl);
	if (rs.next()) {
		sjId = rs.getString("sj");
		bjyywzId = rs.getString("bjsjty");
	}
	sql = "select lastname from hrmresource where id = ?";
	if(sjId!=null && !"".equals(sjId)){
		JSONObject sjObject = new JSONObject();
		sjObject.put("id",sjId);
		rs.executeQuery(sql, sjId);
		if (rs.next()) {
			sjName = rs.getString("lastname");
			sjName = getName(sjName);
			sjObject.put("name",sjName);
			data.put("sjObject", sjObject);
		}
	}
	if(bjyywzId!=null && !"".equals(bjyywzId)){
		JSONObject bjyywzObject = new JSONObject();
		bjyywzObject.put("id",bjyywzId);
		rs.executeQuery(sql, bjyywzId);
		if (rs.next()) {
			bjyywzName = rs.getString("lastname");
			bjyywzName = getName(bjyywzName);
			bjyywzObject.put("name",bjyywzName);
			data.put("bjyywzObject", bjyywzObject);
		}
	}
	
	out.print(callback(code, message, data));					// 有返回数据
%>