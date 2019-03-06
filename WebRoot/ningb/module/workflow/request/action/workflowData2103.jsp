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
	String fyxdl = Util.null2String(request.getParameter("fyxdl"));		// 费用项大类
	String fkdw = Util.null2String(request.getParameter("fkdw"));		// 付款单位
	if (fkdw == null || "".equals(fkdw) || fyxdl == null || "".equals(fyxdl)) {
		out.print(callback(code, message, data));
		return;
	}
	
	String sprje = "";		// 审批人金额
	String shrId = "";		// 审核人ID
	String shrName = "";	// 审核人Name
	String sprId = "";		// 审批人ID
	String sprName = "";	// 审批人Name
	String sql = "select sprje,shr,spr from uf_FYXKMDM where xh = ? and ywst = ?";
	rs.executeQuery(sql, fyxdl, fkdw);
	if (rs.next()) {
		sprje = rs.getString("sprje");
		shrId = rs.getString("shr");
		sprId = rs.getString("spr");
	}
	if(shrId!=null && !"".equals(shrId)){
		JSONObject shrObject = new JSONObject();
		shrObject.put("id",shrId);
		sql = "select lastname from hrmresource where id = ?";
		rs.executeQuery(sql, shrId);
		if (rs.next()) {
			shrName = rs.getString("lastname");
			shrName = getName(shrName);
			shrObject.put("name",shrName);
			data.put("shrObject", shrObject);
		}
	}
	if(sprId!=null && !"".equals(sprId)){
		JSONObject sprObject = new JSONObject();
		sprObject.put("id",sprId);
		sql = "select lastname from hrmresource where id = ?";
		rs.executeQuery(sql, sprId);
		if (rs.next()) {
			sprName = rs.getString("lastname");
			sprName = getName(sprName);
			sprObject.put("name",sprName);
			data.put("sprObject", sprObject);
		}
	}
	data.put("sprje", sprje);
	
	out.print(callback(code, message, data));					// 有返回数据
%>