<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@page import="weaver.general.*" %>
<%@page import="weaver.conn.RecordSetDataSource" %>
<%
	//供应商对账
	int requestid = Util.getIntValue(request.getParameter("requestid"));
	int workflowid = Util.getIntValue(request.getParameter("workflowid"));
	int formid = Util.getIntValue(request.getParameter("formid"));
	int isbill = Util.getIntValue(request.getParameter("isbill"));
	int nodeid = Util.getIntValue(request.getParameter("nodeid"));
%>


<script type="text/javascript">

	var ejzz= "field8993";  //二级组织
	
	var yjzz = "field8992"; //要获取的一级组织ID
	
	var yjzzSpan = "field8992span"; //一级组织 span ID（名称）
	
	
	
	$(function(){
	
		if(<%=nodeid%>+'' !='3874') return;
											
		var departId = $("#"+ejzz).val();
			
		var url_getDeptName = "/workflow/request/getDepartById.jsp?departId="+departId;
						
		$.ajax({			
			type:"post",
			url:url_getDeptName,
			//async:true, 
			success:function(data){
				if(!data){
					
				}else{							
					var json = eval("("+data+")");					
					var a_s = '<a href="" target="_blank" disabled="true">'+json.deptName+'</a>'					
					$("#"+yjzzSpan).html(a_s);					
					$("#"+yjzz).val(json.deptId)
				}
				
			}			
		});
		
	})
	
	
	
</script>