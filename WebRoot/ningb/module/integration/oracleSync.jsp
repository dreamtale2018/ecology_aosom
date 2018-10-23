<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.servicefiles.DataSourceXML"%>
<%@ page import="java.util.*" %>
<%@ page import="weaver.general.Util" %>
<%@page import="weaver.formmode.data.FieldInfo"%>
<%@ page import="weaver.formmode.virtualform.VirtualFormHandler"%>
<%@page import="weaver.formmode.service.CommonConstant"%>
<%@page import="weaver.formmode.customjavacode.CustomJavaCodeRun"%>
<%@page import="com.weaver.formmodel.util.StringHelper"%>
<%@page import="weaver.interfaces.workflow.browser.Browser"%>
<%@page import="weaver.interfaces.workflow.browser.BrowserBean"%>
<%@page import="weaver.conn.RecordSetDataSource"%>
<%@page import="weaver.formmode.service.SelectItemPageService"%>
<%@page import="weaver.common.util.string.StringUtil"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="rsm" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="WorkflowComInfo" class="weaver.workflow.workflow.WorkflowComInfo" scope="page" />
<jsp:useBean id="BrowserComInfo" class="weaver.workflow.field.BrowserComInfo" scope="page"/>
<jsp:useBean id="customSearchService" class="weaver.formmode.service.CustomSearchService" scope="page" />
<jsp:useBean id="ModeShareManager" class="weaver.formmode.view.ModeShareManager" scope="page" />
<jsp:useBean id="ModeRightInfo" class="weaver.formmode.setup.ModeRightInfo" scope="page" />
<jsp:useBean id="FormModeTransMethod" class="weaver.formmode.search.FormModeTransMethod" scope="page" />
<jsp:useBean id="FormModeRightInfo" class="weaver.formmode.search.FormModeRightInfo" scope="page" />
<jsp:useBean id="CustomSearchService" class="weaver.formmode.service.CustomSearchService" scope="page" />
<jsp:useBean id="CustomTreeUtil" class="weaver.formmode.tree.CustomTreeUtil" scope="page" />

<%@ include file="/systeminfo/init_wev8.jsp" %>
<%
	String url = "oracleSyncIframe.jsp";
%>
<!DOCTYPE html>
<head>
<script src="/js/tabs/jquery.tabs.extend_wev8.js"></script>
<link type="text/css" href="/js/tabs/css/e8tabs1_wev8.css" rel="stylesheet" />
<link rel="stylesheet" href="/css/ecology8/request/searchInput_wev8.css" type="text/css" />
<script type="text/javascript" src="/js/ecology8/request/searchInput_wev8.js"></script>

<link rel="stylesheet" href="/css/ecology8/request/seachBody_wev8.css" type="text/css" />
<link rel="stylesheet" href="/css/ecology8/request/hoverBtn_wev8.css" type="text/css" />
<script type="text/javascript" src="/js/ecology8/request/hoverBtn_wev8.js"></script>
<script type="text/javascript" src="/js/ecology8/request/titleCommon_wev8.js"></script>

<script type="text/javascript" src="/wui/theme/ecology8/jquery/js/zDrag_wev8.js"></script>
<script type="text/javascript" src="/wui/theme/ecology8/jquery/js/zDialog_wev8.js"></script>
<LINK href="/wui/theme/ecology8/jquery/js/e8_zDialog_btn_wev8.css" type=text/css rel=STYLESHEET>

<script type="text/javascript">
$(function(){
    $('.e8_box').Tabs({
        getLine:1,
        iframe:"tabcontentframe",
        mouldID:"mnav0_wev8.png",
        staticOnLoad:true,
        objName:"Oracle 基础数据同步"
    });
    
    // 树形打开此页面，给图标加上展开功/关闭左侧树的功能
    if(parent.location.href.indexOf("/formmode/tree/ViewCustomTree.jsp")!=-1){
    	var logo = jQuery("#e8_tablogo");
   		if(typeof(parent.expandOrCollapse)=="function"){
	    	logo.bind("click",function(){
	    			parent.expandOrCollapse();
	    	});
	    	logo.css("cursor","pointer");
   		}
    }
}); 
</script>
</head>
<body scroll="no">
	<div class="e8_box demo2">
		<div class="e8_boxhead">
			<div class="div_e8_xtree" id="div_e8_xtree"></div>
	        <div class="e8_tablogo" id="e8_tablogo"></div>
			<div class="e8_ultab">
				<div class="e8_navtab" id="e8_navtab">
					<span id="objName" title="Oracle 基础数据同步"></span>
				</div>
				<div>	
					<ul class="tab_menu">
						<li>
							<a href="<%= url %>" target="tabcontentframe" class="a_tabcontentframe"></a>
						</li>
					</ul>
					<div id="rightBox" class="e8_rightBox">
					</div>
				</div>
			</div>
		</div>	 
	    <div class="tab_box">
	        <div>
	            <iframe src="<%= url %>" id="tabcontentframe" name="tabcontentframe" class="flowFrame" frameborder="0" height="100%" width="100%;" onload="update()"></iframe>
	        </div>
	    </div>
	</div>
<script language="javascript">
$(function(){
	$('.a_tabcontentframe').hover(function(){
		$('.a_tabcontentframe').attr('title', "<%= StringUtil.Html2Text("Oracle 基础数据同步").replaceAll("\r\n|\r|\n|\n\r", "")%>");
	});
});
</script>     
</body>
</html>