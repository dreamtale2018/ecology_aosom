<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.*" %>
<%@page import="weaver.general.*" %>
<%
	int requestid = Util.getIntValue(request.getParameter("requestid"));		// 请求id
	int workflowid = Util.getIntValue(request.getParameter("workflowid"));		// 流程id
	int formid = Util.getIntValue(request.getParameter("formid"));				// 表单id
	int isbill = Util.getIntValue(request.getParameter("isbill"));				// 表单类型，1单据，0表单
	int nodeid = Util.getIntValue(request.getParameter("nodeid"));				// 流程的节点id
%>
<script type="text/javascript">
var 
	cptp = "field11630";		// 产品图片
		

jQuery(document).ready(function() {
	jQuery("#field12225").bind("change",function(){
		getPicture();
	});
	getPicture();
})

/**
 * 获取产品图片
 */
function getPicture() {
    var HH = jQuery("#field12225").val();
	var params = {"workflowid": "<%= workflowid %>", "requestid": "<%= requestid %>", "hh": HH};
	
	jQuery.ajax({
		type : "post",
		url : "/ningb/module/workflow/request/action/WorkflowData463.jsp",
		data : params,
		dataType : "json",
		success : function(result) {
			if (result == undefined || result == null || result == "") return;
			
			if (result.message.success == true || result.message.success == "true") {
			
				try {
					var data = result.data;
					if (data == undefined || data == null || data == "" ||
						data.cptp == undefined || data.cptp == null || data.cptp == "") return;
						//if(location.href.indexOf('#reloaded')==-1){
						//    location.href=location.href+"#reloaded";
						//    location.reload();
					    //}
					
					syncPicture(data);
				} catch (e) {}
			
			}
		
		}

	})
}

function syncPicture(data) {
	if (data == undefined || data == null || data == "") return;
	setTextValueAndText(cptp, data.cptp);
}

function setTextValue(fieldId, fieldValue) {
	try {
		jQuery("#" + fieldId).val(fieldValue);
	} catch (e) {}
}

function setTextValueAndText(fieldId, fieldValue) {
	try {
		jQuery("#" + fieldId).val(fieldValue);
		jQuery("#" + fieldId + "span").text(fieldValue);
	} catch (e) {}
}
</script>