<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.*" %>
<%@page import="weaver.general.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<jsp:useBean id="requestCI" class="weaver.workflow.request.RequestComInfo"/>

<%
	//新品排单表
	int requestid = Util.getIntValue(request.getParameter("requestid"));
	int workflowid = Util.getIntValue(request.getParameter("workflowid"));
	int formid = Util.getIntValue(request.getParameter("formid"));
	int isbill = Util.getIntValue(request.getParameter("isbill"));
	int nodeid = Util.getIntValue(request.getParameter("nodeid"));
	int ismonitor = Util.getIntValue(request.getParameter("ismonitor"));
	String nodeType="0";
	if(requestid!=-1){
		nodeType=requestCI.getRequestCurrentNodeType(requestid+"");
	}
%>

<% if("0".equals(nodeType)) { %>
<script type="text/javascript">
var hh_mx1 = "field10052_";//货号_明细1
var hh_mx2 = "field10064_";//货号_明细2

function _customAddFun0(){
	
	var indexnum0 = jQuery("#indexnum0").val() * 1.0 - 1;
	jQuery("#"+hh_mx1+indexnum0).bindPropertyChange(function(){
		setHH(indexnum0);//明细1货号赋值给明细2货号
	});

}

jQuery("#div0button").find(".addbtn_p").mousedown(function(){      
	addRow1(1);
});

jQuery("#div0button").find(".delbtn_p").mousedown(function(){
	if(jQuery('#indexnum0').val()>0){
		var delnum = 0;
		jQuery("input[type='checkbox'][name='check_node_0']").each(function(){
			if(jQuery(this).attr('checked')) {
				delnum++;
				var num = jQuery(this).val();
				jQuery("input[type='checkbox'][name='check_node_1']").each(function(){
					if(jQuery(this).val() == num){
						jQuery(this).attr({'checked':'checked'});
					}
				});
			}
		});
		
		if(Number(delnum)>0){
			var isTrue = isdel();
			if(isTrue){
				deleteRow0(0,true);
				deleteRow1(1,true);
			}
		}	
	}
});

jQuery(document).ready(function(){
	var indexnum0 = jQuery("#indexnum0").val() * 1.0;
	for(var index=0; index<indexnum0; index++){
		jQuery("#"+hh_mx1+index).bindPropertyChange(function(obj){
			var num = obj.id.split("_")[1];
			setHH(num);//明细1货号赋值给明细2货号
		});
	}
});

//明细1货号赋值给明细2货号
function setHH(num){
	var hh_mx1_v = jQuery("#"+hh_mx1+num).val();
	setFMVal(hh_mx2+num,hh_mx1_v,hh_mx1_v);
}

/**
 *	赋值方法
 *	id:fieldid
 *	v:value
 *	h:html
 */
function setFMVal(id,v,h){
	var ismandStr = '<IMG src="/'+"images"+"/"+'BacoError_wev8.gif" align="absMiddle">';
	var x= jQuery('#'+id);
	if(x.length > 0){	
		x.attr({'value':v});
		if(x.attr('type') == 'hidden' || document.getElementById(id).style.display == 'none'){
		   
			jQuery('#'+id+'span').html('');
			if(arguments.length>2){
				jQuery('#'+id+'span').html(h);
			}else{
				jQuery('#'+id+'span').html(v);
			}
				
		}else{
			var viewtype = x.attr('viewtype');
			if(viewtype == 1 && (!v || v == '')){
				jQuery('#'+id+'span').html(ismandStr);
			}else{
				jQuery('#'+id+'span').html('');
			}
		}
	}
}
</script>
<% } %>