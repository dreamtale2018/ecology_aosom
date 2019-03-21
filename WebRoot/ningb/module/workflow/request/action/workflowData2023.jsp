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
	
	JSONObject data = new JSONObject();									// 返回的数据
	String gxhth = Util.null2String(request.getParameter("gxhth"));		// 购销合同号
	String hangh = Util.null2String(request.getParameter("hangh"));		// 行号
	RecordSet rs = new RecordSet();
	if (gxhth == null || "".equals(gxhth)) {
		out.print(callback(code, message, data));
		return;
	}
	
	String cghtsplc = "";				// 采购合同审批流程
	String currentnodetype = "";		// 节点类型
	String gchcj = "";					// 工厂回传件
	String sql = "select requestid,currentnodetype,gchcj from (select a.requestid,b.currentnodetype,a.gchcj from " +
				"formtable_main_73 a left join workflow_requestbase b on a.requestid = b.requestid " +
				"where a.sghth = ? order by b.createdate desc,b.createtime desc) where rownum<=1";
	rs.executeQuery(sql, gxhth);
	if (rs.next()) {
		cghtsplc = rs.getString("requestid");
		currentnodetype = rs.getString("currentnodetype");
		gchcj = rs.getString("gchcj");
		sql = "select gxhthcj from formtable_main_229_dt1 where id = ?";
		rs.executeQuery(sql, hangh);
		if (rs.next()) {
			String gxhthcj = rs.getString("gxhthcj");
			if("".equals(gxhthcj)){
				sql = "update formtable_main_229_dt1 set gxhthcj = '" + gchcj + "' where id = '" + hangh + "'";
				rs.execute(sql);
			}
		}
	}
	data.put("cghtsplc", cghtsplc);
	if("3".equals(currentnodetype)){
		data.put("isFinish", true);
	}else{
		data.put("isFinish", false);
	}
	
	out.print(callback(code, message, data));					// 有返回数据
%>