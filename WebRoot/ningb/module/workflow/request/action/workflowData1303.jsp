<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*,weaver.general.*" %>
<%@ page language="java" import="net.sf.json.JSONArray,net.sf.json.JSONObject"%>
<%@ page language="java" import="weaver.hrm.*" %>
<%@ page language="java" import="java.util.regex.Pattern" %>
<%@ page language="java" import="java.util.regex.Matcher" %>
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
	String remarks = Util.null2String(request.getParameter("remarks"));	// 备注
	String gbremark = remarks.split("官网")[0];//国别编辑
	String gwremark = remarks.split("官网")[1];//官网编辑
	RecordSet rs = new RecordSet();
	JSONArray gbArray = getJSONArray(gbremark, rs);
	JSONArray gwArray = getJSONArray(gwremark, rs);
	if (hh == null || "".equals(hh)) {
		out.print(callback(code, message, data));
		return;
	}
	
	String gbname = "";		// 国别, 多个逗号隔开
	String sql = "select a.segment1,c.org_information5 "
			+ "from uf_product a "
			+ "inner join uf_seasproduct b on a.standard_item_id=b.standard_item_id and b.ou_item_status_code!='STOPPED'"
			+ "inner join uf_gb c on b.org_id = c.organization_id "
			+ "where a.id = ?";
	rs.executeQuery(sql, hh);
	while (rs.next()) {
		if ("".equals(gbname)) {
			gbname = rs.getString("org_information5");
		} else {
			gbname += "," + rs.getString("org_information5");
		}
	}
	data.put("gbname", gbname);
	data.put("gbArray", gbArray);
	data.put("gwArray", gwArray);
	
	out.print(callback(code, message, data));					// 有返回数据
%>