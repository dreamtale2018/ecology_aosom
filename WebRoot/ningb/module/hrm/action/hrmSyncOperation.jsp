<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*,weaver.general.*" %>
<%@ page language="java" import="net.sf.json.JSONArray,net.sf.json.JSONObject" %>
<%@ page language="java" import="weaver.hrm.*" %>
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
	HrmScheduleManager hrmManager = new HrmScheduleManager();
	HrmScheduleDiffManager hrmDiffManager = new HrmScheduleDiffManager();
	boolean flag = true;
	if (type == null || "".equals(type)) {
	} else if ("0".equals(type)) {
		flag = hrmManager.syncAttendanceSignToFormmode(0, startDate, endDate);
	} else if ("1".equals(type)) {
		flag = hrmManager.syncAttendanceSignToFormmode(1, startDate, endDate);
	} else if ("2".equals(type)) {
		flag = hrmManager.syncAttendanceSignAllToFormmode(startDate, endDate);
	} else if ("3".equals(type)) {
		flag = hrmManager.sendAttendanceSign(0, startDate);
	} else if ("4".equals(type)) {
		flag = hrmManager.sendAttendanceSign(1, startDate);
	} else if ("5".equals(type)) {
		flag = hrmManager.syncAttendanceWorkflowToFormmode();
	} else if ("6".equals(type)) {
		flag = hrmManager.syncAttendanceLactationToFormmode(startDate, endDate);
	} else if ("7".equals(type)) {
		List<Integer> signTypes = new ArrayList<Integer>(1);
		signTypes.add(1);
		flag = hrmManager.syncAttendanceToReport(signTypes, startDate, endDate);
	} else if ("8".equals(type)) {
		List<Integer> signTypes = new ArrayList<Integer>(1);
		signTypes.add(2);
		signTypes.add(3);
		signTypes.add(4);
		signTypes.add(5);
		flag = hrmManager.syncAttendanceToReport(signTypes, startDate, endDate);
	} else if ("9".equals(type)) {
		List<Integer> signTypes = new ArrayList<Integer>(1);
		signTypes.add(6);
		flag = hrmManager.syncAttendanceToReport(signTypes, startDate, endDate);
	} else if ("10".equals(type)) {
		flag = hrmManager.syncAttendanceToReport(startDate, endDate);
	} else if ("11".equals(type)) {
		flag = hrmDiffManager.diffAttendanceSign(startDate, endDate);
	} else if ("12".equals(type)) {
		flag = hrmDiffManager.diffAttendanceSignByNoSign(startDate, endDate);
	} else if ("13".equals(type)) {
		flag = hrmDiffManager.diffAttendanceSignByWorkflow(startDate, endDate);
	} else if ("14".equals(type)) {
		flag = hrmDiffManager.diffAttendanceWorkflowToReport(startDate, endDate);
	} else if ("15".equals(type)) {
		flag = hrmDiffManager.diffAttendance(startDate, endDate);
	}
	JSONObject data = new JSONObject();
	data.put("flag", flag + "");

	out.print(callback(code, message, data));					// 有返回数据
%>