<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*,weaver.general.*" %>
<%@ page language="java" import="net.sf.json.JSONArray,net.sf.json.JSONObject"%>
<%@ page language="java" import="weaver.hrm.*" %>
<%@ page language="java" import="weaver.conn.RecordSet" %>
<%@ page language="java" import="java.text.SimpleDateFormat" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="java.math.RoundingMode" %>
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
	
	private String getFlzj(String id, RecordSet rs){
		String flzj = "";
		String sql = "select field18 from cus_fielddata where scopeid = '1' and id = ?";
	    rs.executeQuery(sql, id);
	    if(rs.next()){
	    	flzj = rs.getString(1);
	    }
	    return flzj;
	}
%>
<%
	String code = "0";								// 返回编码：0.成功   -1.未登录
	String message = "请求成功";						// 返回信息
	
	JSONObject data = new JSONObject();									// 返回的数据
	String sqr = Util.null2String(request.getParameter("sqr"));			// 申请人
	String sqrq = Util.null2String(request.getParameter("sqrq"));		// 申请日期
	String rzrq = Util.null2String(request.getParameter("rzrq"));		// 入职日期
	
	RecordSet rs = new RecordSet();
	String flzjStr = getFlzj(sqr,rs);									// 福利职级
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    double dqkxts = 0d;		//当前可休天数
    double zjts = 0d;		//职级天数
	Date sqrqDate = sdf.parse(sqrq);  
	Date rzrqDate = sdf.parse(rzrq);  
    //计算入职天数。
	long rzsj = sqrqDate.getTime() - rzrqDate.getTime();
    double rzts = Math.floor(rzsj /(24*3600*1000));
    //计算职级天数。
    if(rzts<365){
		zjts = 0.00;
	}else if(rzts>=365 && rzts<1095){
	    zjts = 5.00;	
	}else if(rzts>=1095){
		int flzj = Integer.parseInt(flzjStr);
	    if(flzj<4){
	    	zjts = 5.00;
	    }else if(flzj==4){
	    	zjts = 8.00;
	    }else if(flzj>4){
	    	zjts = 10.00;
	    }	
	}
	//获取年份。
	Calendar cale = Calendar.getInstance();  
    int year = cale.get(Calendar.YEAR); 
    //获取当年6月30日。
    Date halfYear = sdf.parse(String.valueOf(year) + "-06-30");
    DecimalFormat df = new DecimalFormat("0.0");//设置保留1位数
    df.setRoundingMode(RoundingMode.HALF_UP);
    DecimalFormat df1 = new DecimalFormat("0.00");//设置保留2位数
    df1.setRoundingMode(RoundingMode.HALF_UP);
    
    //获取当年12月31日。
	Date nmrqDate = sdf.parse(String.valueOf(year) + "-12-31");
	//计算年末天数。
    long nmsj = nmrqDate.getTime() - rzrqDate.getTime();
    double nmts = Math.floor(nmsj /(24*3600*1000));
    //计算年末月数(保留一位小数)。
    String nmysStr = df.format((float)nmts/30);
    double nmys = Double.parseDouble(nmysStr);
    double nmkxts = Double.parseDouble(df1.format((float)zjts * (nmys - 12) / 12 ));
    
    //获取当年01月01日。
	Date ncrqDate = sdf.parse(String.valueOf(year) + "-01-01");
	//计算年度天数。
    long ndsj = sqrqDate.getTime() - ncrqDate.getTime();
    double ndts = Math.floor(ndsj /(24*3600*1000));
    //计算年度月数(保留一位小数)。
    String ndysStr = df.format((float)ndts/30);
    double ndys = Double.parseDouble(ndysStr);
    
    if(sqrqDate.getTime()<=halfYear.getTime()){
	    if(rzts<365){
	    	dqkxts = 0.00;
	    }else if(rzts>=365 && rzts<730){
		    //计算司龄月数(保留一位小数)。
		    String slysStr = df.format((float)rzts/30);
		    double slys = Double.parseDouble(slysStr);
		    if(nmkxts<zjts){
			    dqkxts = Double.parseDouble(df1.format((float)zjts * (slys - 12) / 12 ));
		    }else{
		    	dqkxts = Double.parseDouble(df1.format((float)zjts * ndys / 12 ));
		    }
	    }else if(rzts>=730 && rzts<1095){
		    dqkxts = Double.parseDouble(df1.format((float)zjts * ndys / 12 ));
	    }else if(rzts>=1095){
	    	dqkxts = zjts;
	    }
    }else{
    	if(rzts<365){
	    	dqkxts = 0.00;
	    }else if(rzts>=365 && rzts<730){
		    if(nmkxts<zjts){
		    	dqkxts = nmkxts;
		    }else{
		    	dqkxts = zjts;
		    }
	    }else if(rzts>=730){
	    	dqkxts = zjts;
	    }
    }
	data.put("dqkxts",dqkxts);
	
	out.print(callback(code, message, data));					// 有返回数据
%>