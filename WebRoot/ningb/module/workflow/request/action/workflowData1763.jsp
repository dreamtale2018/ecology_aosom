<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*,weaver.general.*" %>
<%@ page language="java" import="net.sf.json.JSONArray,net.sf.json.JSONObject"%>
<%@ page language="java" import="weaver.hrm.*" %>
<%@ page language="java" import="java.util.regex.Pattern" %>
<%@ page language="java" import="java.util.regex.Matcher" %>
<%@ page language="java" import="weaver.conn.RecordSet" %>
<%@ page language="java" import="org.apache.commons.lang.StringUtils" %>
<%@ page language="java" import="org.apache.commons.logging.Log" %>
<%@ page language="java" import="org.apache.commons.logging.LogFactory" %>
<%@ page language="java" import="weaver.interfaces.workflow.action.AutoCreateRzxqbAction" %>
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
	
	private Log logger = LogFactory.getLog(AutoCreateRzxqbAction.class);
	
	private JSONArray getJSONArray(String remark, RecordSet rs){
		Pattern pattern = Pattern.compile("[a-zA-Z]{2,3}\\—([\u4e00-\u9fa5]{2,5}[、]?)+");
	    Matcher matcher = pattern.matcher(remark);
	    JSONArray jsonArray = new JSONArray();
	 	while(matcher.find()){
	    	JSONObject jsonObj = new JSONObject();
	    	String gb = matcher.group().split("—")[0];
	    	String name = matcher.group().split("—")[1];
	        jsonObj.put("gb", gb);
	        jsonObj.put("name", name);
	        String sql = "select id from hrmresource where lastname like ?";
	        String id = "";		// 人员ID, 多个、隔开
	    	if(name.indexOf("、")!=-1){
	    		String[] nameList = name.split("、");
	    	    for(int i=0; i<nameList.length; i++){
					rs.executeQuery(sql, "%"+nameList[i]+"%");
					if(rs.next()){
						if(i==nameList.length-1){
							id += rs.getString(1);
						}else{
							id += rs.getString(1) + "、";
						}
					}
	    	    }
	    	    jsonObj.put("id", id);
	    	}else{
	    	    rs.executeQuery(sql, "%"+name+"%");
	    	    if(rs.next()){
					id = rs.getString(1);
				}
		        jsonObj.put("id", id);
	    	}
	    	jsonArray.add(jsonObj);
	    }
	    return jsonArray;
	}
	
	private JSONObject getPersonID(String name, RecordSet rs){
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", name);
		String sql = "select id from hrmresource where lastname like ?";
	    rs.executeQuery(sql, "%"+name+"%");
	    if(rs.next()){
	    	String id = rs.getString(1);
	    	jsonObj.put("id", id);
	    }
	    return jsonObj;
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
	String hh = Util.null2String(request.getParameter("hh"));			// 货号
	String gb = Util.null2String(request.getParameter("gb"));			// 国别
	String kslx = Util.null2String(request.getParameter("kslx"));		// 客述类型
	String remarks = Util.null2String(request.getParameter("remarks"));	// 备注
	String bzremark = remarks.split("客服")[0].substring(3);				// 包装
	String kfremark = remarks.split("客服")[1];							// 客服
	RecordSet rs = new RecordSet();
	JSONArray kfArray = getJSONArray(kfremark, rs);
	if (("1".equals(kslx) && StringUtils.isBlank(hh)) || ("2".equals(kslx) && StringUtils.isBlank(gb))) {
		out.print(callback(code, message, data));
		return;
	}
	
	if("0".equals(kslx)){
		JSONObject bzObject = new JSONObject();
		Pattern pattern = Pattern.compile("([\u4e00-\u9fa5]{2,5}[、]?)+");
	    Matcher matcher = pattern.matcher(bzremark);
		if(matcher.find()){
			String bz = matcher.group();
			bzObject = getPersonID(bz, rs);
		}
		data.put("bzObject", bzObject);
	}else if ("1".equals(kslx)){
		String sql = "select b.id,b.lastname "
			+ "from uf_product a "
			+ "inner join hrmresource b on a.new_develop_user_code=b.loginid "
			+ "where a.segment1 = ?";
		rs.executeQuery(sql, hh);
		JSONObject kfyObject = new JSONObject();
		if (rs.next()) {
			String personId = rs.getString("id");
			String personName = rs.getString("lastname");
			if(personName.indexOf("`~`7")!=-1 && personName.indexOf("`~`8")!=-1){
				personName = personName.split("`~`7")[1].split("`~`8")[0].trim(); 
			}else{
				personName = personName.split("`~`7")[0].split("`~`8")[0].trim();
			}
			kfyObject.put("id", personId);
			kfyObject.put("name", personName);
		}
		data.put("kfyObject", kfyObject);
	}else if ("2".equals(kslx)){
		data.put("kfArray", kfArray);
	}
	
	out.print(callback(code, message, data));					// 有返回数据
%>