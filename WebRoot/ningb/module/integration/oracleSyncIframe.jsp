<%@ page language="java" import="java.util.*,weaver.general.*" pageEncoding="UTF-8" %>
<%@ include file="/systeminfo/init_wev8.jsp" %>
<html>
<%
	String userid = user.getUID() + "";
	if (!"1".equals(userid) && !"75".equals(userid) && !"511".equals(userid)) return;
%>
<head>
<meta>
<title></title>
<link href="/css/Weaver_wev8.css" type="text/css" rel="stylesheet">
<link href="/css/crmcss/lanlv_wev8.css" type="text/css" rel="stylesheet">
<link href="/workflow/exceldesign/css/excelHtml_wev8.css" type="text/css" rel="stylesheet">
<link href="/wui/theme/ecology8/templates/default/css/default2_wev8.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/js/weaver_wev8.js"></script>
</head>
<body>
	<div id="loading" style="display:none;">
		<span><img src="/images/loading2_wev8.gif" align="absmiddle"></span>
		<span id="loading-msg">请求执行中，请稍候...</span>
	</div>
	<div id="sync" style="margin:0 auto; width: 100%;">
		<table class="Shadow table">
			<tr>
				<td valign="top">
					<br />
					<div style="width:88%;margin: 0px auto;font-size:18px;">基础数据同步 (将 Oracle 数据同步到建模中)</div>
					<table class="ViewForm maintable" style="width:88%;margin: 12px auto;">
						<colgroup>
							<col width="20%"></col>
							<col width="80%"></col>
						</colgroup>
						<tbody>
							<tr>
								<td class="fname">基础数据：</td>
								<td class="field fvalue">
									<input id="syncOracleksrq" name="syncOracleksrq" value=""  type="hidden"  class="wuiDate" _span="syncOraclespan" _button="buttonId"/>
									<span id="syncOraclespan"></span>
									<input type="button" class="e8_btn_top" id="syncOracle" name="syncOracle" style="margin-left:8px;" onclick="onSyncOracle(this, '同步基础数据', 0);" value="执行"></input>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</table>
	</div>
<script type="text/javascript">
var 
	sync = "sync",
	syncloading = "loading";
function onSyncOracle(obj, message, type) {
	var id = jQuery(obj).attr("id");
	var params;
	if (type == -1) {
		params = {"type": type};
	} else if (type == 0 || type == 3 || type == 4) {
		var startDate = handleEmpty(jQuery("#" + id + "ksrq").val());
		params = {"type": type, "startDate": startDate};
	} else {
		var 
			startDate = handleEmpty(jQuery("#" + id + "ksrq").val()),
			endDate = handleEmpty(jQuery("#" + id + "jsrq").val());
		if (startDate == "" && endDate == "") {
			Dialog.alert(message + ", 开始时间和结束时间不能同时为空.");
			return;
		}
		if (startDate != "" && endDate != "" && checkDate(startDate, endDate)) {
			Dialog.alert(message + ", 开始时间不能大于结束时间.");
			return;
		}
		params = {"type": type, "startDate": startDate, "endDate": endDate};
	}
	
	jQuery("#" + sync).css("display", "none");
	jQuery("#" + syncloading).css("display", "");
	
	jQuery.ajax({
		type : "post",
		url : "/ningb/module/integration/action/oracleOperation.jsp",
		data : params,
		dataType : "json",
		error: function(error) {
			Dialog.alert(message + " 异常.");
			jQuery("#" + sync).css("display", "");
			jQuery("#" + syncloading).css("display", "none");
		},
		success: function(result) {
			if (handleEmpty(result) == "") {
				Dialog.alert(message + " 失败.");
				jQuery("#" + sync).css("display", "");
				jQuery("#" + syncloading).css("display", "none");
				return;
			}
			
			if (result.code == 0 || result.code == "0") {
			
				try {
					var data = handleEmpty(result.data);
					if (data == "") return;
					if (data.flag == true || data.flag == "true") {
						Dialog.alert(message + " 成功.");
					} else {
						Dialog.alert(message + " 失败.");
					}
					jQuery("#" + sync).css("display", "");
					jQuery("#" + syncloading).css("display", "none");
				} catch (e) {}
			
			}
		
		}

	})
}

function checkDate(startDate, endDate) {
	var startDateMillis = getDateToMillis(startDate, "00:00");
	var endDateMillis = getDateToMillis(endDate, "00:00");
	if (startDateMillis > endDateMillis) {
		return true;
	} else {
		return false;
	}
}

// 对时间格式进行拼接解析后反馈毫秒
function getDateToMillis(date, time) {
	var strs = date.split("-");
	var strDate = strs[1] + "-" + strs[2] + "-" + strs[0];
	var testDate = new Date(Date.parse(date.replace(/-/g, "/")));
	return testDate.getTime();
}

function handleEmpty(val) {
	if (val == undefined || val == null) {
		return "";
	} else {
		return val;
	}
}
</script>

<script language="javascript" src="/js/datetime_wev8.js"></script>
<script language="javascript" src="/js/selectDateTime_wev8.js"></script>
<script language="javascript" src="/js/JSDateTime/WdatePicker_wev8.js"></script>
</body>
</html>