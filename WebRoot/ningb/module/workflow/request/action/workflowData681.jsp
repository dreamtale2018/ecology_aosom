<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*,weaver.general.*" %>
<%@ page language="java" import="net.sf.json.JSONArray,net.sf.json.JSONObject"%>
<%@ page language="java" import="weaver.hrm.*" %>
<%@ page language="java" import="org.apache.commons.logging.Log" %>
<%@ page language="java" import="com.weaver.ningb.logging.LogFactory" %>
<%@ page language="java" import="weaver.interfaces.workflow.action.ProductOrderGysghArchiveAction" %>
<%@ page language="java" import="weaver.conn.RecordSet" %>
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
	
	public String getName(String personId){
		String personName = "";
		if("".equals(personId)){
			personName = "";
		}else{
			RecordSet rs1 = new RecordSet();
			String sql = "select lastname from hrmresource where id = ?";
			rs1.executeQuery(sql, personId);
			if (rs1.next()) {
				personName = rs1.getString("lastname");
			}
			if(personName.indexOf("`~`7")!=-1 && personName.indexOf("`~`8")!=-1){
				personName = personName.split("`~`7")[1].split("`~`8")[0].trim(); 
			}else{
				personName = personName.split("`~`7")[0].split("`~`8")[0].trim();
			}
		}
		return personName;
	}
	
	private static final Log logger = LogFactory.getLog(ProductOrderGysghArchiveAction.class);
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
	String tjlb = Util.null2String(request.getParameter("tjlb"));		// 推荐类别
	if (tjlb == null || "".equals(tjlb)) {
		out.print(callback(code, message, data));
		return;
	}
	
	String sql = "select gb,zz,hwkf,cs,zg from uf_CPXXCLR where zb = ?";
	rs.executeQuery(sql, tjlb);
	JSONArray shrArray = new JSONArray();
	while (rs.next()) {
		String gb = rs.getString("gb");
		String zzId = rs.getString("zz");
		String hwId = rs.getString("hwkf");
		String csId = rs.getString("cs");
		String zgId = rs.getString("zg");
		JSONObject shrObject = new JSONObject();
		JSONObject zzObject = new JSONObject();
		JSONObject hwObject = new JSONObject();
		JSONObject csObject = new JSONObject();
		JSONObject zgObject = new JSONObject();
		String zzName = getName(zzId);
		String hwName = getName(hwId);
		String csName = getName(csId);
		String zgName = getName(zgId);
		shrObject.put("gb",gb);
		zzObject.put("id",zzId);
		zzObject.put("name",zzName);
		hwObject.put("id",hwId);
		hwObject.put("name",hwName);
		csObject.put("id",csId);
		csObject.put("name",csName);
		zgObject.put("id",zgId);
		zgObject.put("name",zgName);
		shrObject.put("zzObject",zzObject);
		shrObject.put("hwObject",hwObject);
		shrObject.put("csObject",csObject);
		shrObject.put("zgObject",zgObject);
		shrArray.add(shrObject);
	}
	data.put("shrArray", shrArray);
	
	out.print(callback(code, message, data));					// 有返回数据
%>