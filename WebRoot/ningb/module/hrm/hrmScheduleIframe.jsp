<%@ page language="java" import="java.util.*,weaver.general.*" pageEncoding="UTF-8" %>
<%@ include file="/systeminfo/init_wev8.jsp" %>
<html>
<%
	String userid = user.getUID() + "";
	if (!"1".equals(userid)) return;
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
					<div style="width:88%;margin: 0px auto;font-size:18px;">考勤数据相关 (将考勤相关的数据同步到建模中)</div>
					<table class="ViewForm maintable" style="width:88%;margin: 12px auto;">
						<colgroup>
							<col width="20%"></col>
							<col width="80%"></col>
						</colgroup>
						<tbody>
							<tr>
								<td class="fname">打卡记录 (早上数据)：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleAMksrq" name="syncHrmScheduleAMksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleAMksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleAMksrqspan"></span>
									至 
									<input id="syncHrmScheduleAMjsrq" name="syncHrmScheduleAMjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleAMjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleAMjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleAM" name="syncHrmScheduleAM" style="margin-left:8px;" onclick="onSyncHrm(this, '同步打卡记录 (早上数据)', 0);" value="执行"></input>
								</td>
							</tr>
							<tr>
								<td class="fname">打卡记录 (晚上数据)：</td>
								<td class="field fvalue">
									<input id="syncHrmSchedulePMksrq" name="syncHrmSchedulePMksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmSchedulePMksrqspan" _button="buttonId"/>
									<span id="syncHrmSchedulePMksrqspan"></span>
									至 
									<input id="syncHrmSchedulePMjsrq" name="syncHrmSchedulePMjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmSchedulePMjsrqspan" _button="buttonId"/>
									<span id="syncHrmSchedulePMjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmSchedulePM" name="syncHrmSchedulePM" style="margin-left:8px;" onclick="onSyncHrm(this, '同步打卡记录 (晚上数据)', 1);" value="执行"></input>
								</td>
							</tr>
							<tr>
								<td class="fname">打卡记录 (早上和晚上数据)：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleALLksrq" name="syncHrmScheduleALLksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleALLksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleALLksrqspan"></span>
									至 
									<input id="syncHrmScheduleALLjsrq" name="syncHrmScheduleALLjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleALLjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleALLjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleALL" name="syncHrmScheduleALL" style="margin-left:8px;" onclick="onSyncHrm(this, '同步打卡记录 (早上和晚上数据)', 2);" value="执行"></input>
								</td>
							</tr>
							<tr>
								<td class="fname">打卡提醒 (早上)：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleRemindAMksrq" name="syncHrmScheduleRemindAMksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleRemindAMksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleRemindAMksrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleRemindAM" name="syncHrmScheduleRemindAM" style="margin-left:8px;" onclick="onSyncHrm(this, '打卡提醒 (早上)', 3);" value="执行"></input>
								</td>
							</tr>
							<tr>
								<td class="fname">打卡提醒 (晚上)：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleRemindPMksrq" name="syncHrmScheduleRemindPMksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleRemindPMksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleRemindPMksrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleRemindPM" name="syncHrmScheduleRemindPM" style="margin-left:8px;" onclick="onSyncHrm(this, '打卡提醒 (晚上)', 4);" value="执行"></input>
								</td>
							</tr>
							<tr>
								<td class="fname">考勤数据 (流程数据)：</td>
								<td class="field fvalue">
									<input type="button" class="e8_btn_top" id="syncHrmScheduleWorkflow" name="syncHrmScheduleWorkflow" style="margin-left:8px;" onclick="onSyncHrm(this, '考勤数据 (流程数据)', 5);" value="执行"></input>
								</td>
							</tr>
							<tr>
								<td class="fname">考勤数据 (哺乳假数据)：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleLactationksrq" name="syncHrmScheduleLactationksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleLactationksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleLactationksrqspan"></span>
									至 
									<input id="syncHrmScheduleLactationjsrq" name="syncHrmScheduleLactationjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleLactationjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleLactationjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleLactation" name="syncHrmScheduleLactation" style="margin-left:8px;" onclick="onSyncHrm(this, '考勤数据 (哺乳假数据)', 6);" value="执行"></input>
								</td>
							</tr>
						</tbody>
					</table>
					<br />
					<div style="width:88%;margin: 0px auto;font-size:18px;">考勤报表相关 (将建模中相关类型的数据同步到考勤报表中)</div>
					<table class="ViewForm maintable" style="width:88%;margin: 12px auto;">
						<colgroup>
							<col width="20%"></col>
							<col width="80%"></col>
						</colgroup>
						<tbody>
							<tr>
								<td class="fname">打卡：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleReportksrq" name="syncHrmScheduleReportksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleReportksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleReportksrqspan"></span>
									至 
									<input id="syncHrmScheduleReportjsrq" name="syncHrmScheduleReportjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleReportjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleReportjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleReport" name="syncHrmScheduleReport" style="margin-left:8px;" onclick="onSyncHrm(this, '考勤报表相关-打卡', 7);" value="执行"></input>
								</td>
							</tr>
							<tr>
								<td class="fname">流程：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleReportWorkflowksrq" name="syncHrmScheduleReportWorkflowksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleReportWorkflowksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleReportWorkflowksrqspan"></span>
									至 
									<input id="syncHrmScheduleReportWorkflowjsrq" name="syncHrmScheduleReportWorkflowjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleReportWorkflowjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleReportWorkflowjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleReportWorkflow" name="syncHrmScheduleReportWorkflow" style="margin-left:8px;" onclick="onSyncHrm(this, '考勤报表相关-流程', 8);" value="执行"></input>
								</td>
							</tr>
							<tr>
								<td class="fname">哺乳假：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleReportLactationksrq" name="syncHrmScheduleReportLactationksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleReportLactationksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleReportLactationksrqspan"></span>
									至 
									<input id="syncHrmScheduleReportLactationjsrq" name="syncHrmScheduleReportLactationjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleReportLactationjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleReportLactationjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleReportLactation" name="syncHrmScheduleReportLactation" style="margin-left:8px;" onclick="onSyncHrm(this, '考勤报表相关-哺乳假', 9);" value="执行"></input>
								</td>
							</tr>
							<tr>
								<td class="fname">全部：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleReportALLksrq" name="syncHrmScheduleReportALLksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleReportALLksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleReportALLksrqspan"></span>
									至 
									<input id="syncHrmScheduleReportALLjsrq" name="syncHrmScheduleReportALLjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleReportALLjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleReportALLjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleReportALL" name="syncHrmScheduleReportALL" style="margin-left:8px;" onclick="onSyncHrm(this, '考勤报表相关-全部', 10);" value="执行"></input>
								</td>
							</tr>
						</tbody>
					</table>
					<br />
					<div style="width:88%;margin: 0px auto;font-size:18px;">考勤差异相关 (将建模中相关类型的数据同步到考勤报表中, 处理数据不同步的问题)</div>
					<table class="ViewForm maintable" style="width:88%;margin: 12px auto;">
						<colgroup>
							<col width="20%"></col>
							<col width="80%"></col>
						</colgroup>
						<tbody>
							<tr>
								<td class="fname">打卡：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleDiffksrq" name="syncHrmScheduleDiffksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleDiffksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleDiffksrqspan"></span>
									至 
									<input id="syncHrmScheduleDiffjsrq" name="syncHrmScheduleDiffjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleDiffjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleDiffjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleDiff" name="syncHrmScheduleDiff" style="margin-left:8px;" onclick="onSyncHrm(this, '考勤差异相关-打卡', 11);" value="执行"></input>
									<span style="margin-left:5px;color:red;">老 OA 考勤数据可能存在更新的问题处理</span>
								</td>
							</tr>
							<tr>
								<td class="fname">打卡：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleDiffNoSignksrq" name="syncHrmScheduleDiffNoSignksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleDiffNoSignksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleDiffNoSignksrqspan"></span>
									至 
									<input id="syncHrmScheduleDiffNoSignjsrq" name="syncHrmScheduleDiffNoSignjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleDiffNoSignjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleDiffNoSignjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleDiffNoSign" name="syncHrmScheduleDiffNoSign" style="margin-left:8px;" onclick="onSyncHrm(this, '考勤差异相关-打卡', 12);" value="执行"></input>
									<span style="margin-left:5px;color:red;">不存在打卡数据, 但是早上, 晚上的考勤流程 (请假, 出差) 同时存在的问题处理</span>
								</td>
							</tr>
							<tr>
								<td class="fname">流程：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleDiffWorkflowksrq" name="syncHrmScheduleDiffWorkflowksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleDiffWorkflowksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleDiffWorkflowksrqspan"></span>
									至 
									<input id="syncHrmScheduleDiffWorkflowjsrq" name="syncHrmScheduleDiffWorkflowjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleDiffWorkflowjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleDiffWorkflowjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleDiffWorkflow" name="syncHrmScheduleDiffWorkflow" style="margin-left:8px;" onclick="onSyncHrm(this, '考勤差异相关-流程', 13);" value="执行"></input>
									<span style="margin-left:5px;color:red;">用户提交请假, 出差类流程后; 不进行打卡的问题处理</span>
								</td>
							</tr>
							<tr>
								<td class="fname">流程：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleDiffLactationksrq" name="syncHrmScheduleDiffLactationksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleDiffLactationksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleDiffLactationksrqspan"></span>
									至 
									<input id="syncHrmScheduleDiffLactationjsrq" name="syncHrmScheduleDiffLactationjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleDiffLactationjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleDiffLactationjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleDiffLactation" name="syncHrmScheduleDiffLactation" style="margin-left:8px;" onclick="onSyncHrm(this, '考勤差异相关-流程', 14);" value="执行"></input>
									<span style="margin-left:5px;color:red;">用户提交请假, 出差, 销假等流程, 审批时间延时的问题处理</span>
								</td>
							</tr>
							<tr>
								<td class="fname">全部：</td>
								<td class="field fvalue">
									<input id="syncHrmScheduleDiffALLksrq" name="syncHrmScheduleDiffALLksrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleDiffALLksrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleDiffALLksrqspan"></span>
									至 
									<input id="syncHrmScheduleDiffALLjsrq" name="syncHrmScheduleDiffALLjsrq" value=""  type="hidden"  class="wuiDate" _span="syncHrmScheduleDiffALLjsrqspan" _button="buttonId"/>
									<span id="syncHrmScheduleDiffALLjsrqspan"></span>
									<input type="button" class="e8_btn_top" id="syncHrmScheduleDiffALL" name="syncHrmScheduleDiffALL" style="margin-left:8px;" onclick="onSyncHrm(this, '考勤差异相关-全部', 15);" value="执行"></input>
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
function onSyncHrm(obj, message, type) {
	var id = jQuery(obj).attr("id");
	var params;
	if (type == 3 || type == 4) {
		var startDate = handleEmpty(jQuery("#" + id + "ksrq").val());
		if (startDate == "") {
			Dialog.alert(message + ", 时间不能为空.");
			return;
		}
		params = {"type": type, "startDate": startDate};
	} else if (type == 5) {
		params = {"type": type};
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
		url : "/ningb/module/hrm/action/hrmSyncOperation.jsp",
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