<!-- script代码，如果需要引用js文件，请使用与HTML中相同的方式。 -->
<script type="text/javascript">
/*
 *  TODO
 *  请在此处编写javascript代码
 */
var 
	hh = "field10587",				// 货号
	gb = "field9687";				// 国别
	var brower = browerInfo();		// 浏览器信息
jQuery(document).ready(function() {
	bindEvent();
})

// 初始化页面已存在的明细行
function bindEvent() {
	var index = 0;
	if (jQuery("#indexnum0").length > 0) index = jQuery("#indexnum0").val() * 1.0;
	
	for (var i = 0; i < index; i++) {
		if (brower.name == "firefox" || brower.name == "chrome" 
			|| (brower.name == "msie" && brower.version > 8)) {
			
			// 监听货号值改变
			jQuery("#" + hh + "_" + i).bindPropertyChange(function(){
				var 
					$self = jQuery("#" + hh + "_" + i),
					innum = $self.attr("id").split(hh)[1];
				getGbName($self, innum);
			})
			
		// 处理其他类型浏览器兼容问题
		} else {
		
			// 监听货号值改变
			jQuery("#" + hh + "_" + i).bind("propertychange",function(){
				var 
					$self = jQuery("#" + hh + "_" + i),
					innum = $self.attr("id").split(hh)[1];
				getGbName($self, innum);
			})
		}
	}
}

// 处理明细单行新增
function _customAddFun0() {
	var index = 0;
	if (jQuery("#indexnum0").length > 0) index = jQuery("#indexnum0").val() * 1.0 - 1;
	
	if (brower.name == "firefox" || brower.name == "chrome" 
		|| (brower.name == "msie" && brower.version > 8)) {
		
		// 监听货号值改变
		jQuery("#" + hh + "_" + index).bindPropertyChange(function(){
			var 
				$self = jQuery("#" + hh + "_" + index),
				innum = $self.attr("id").split(hh)[1];
			getGbName($self, innum);
		})
		
	// 处理其他类型浏览器兼容问题
	} else {
	
		// 监听货号值改变
		jQuery("#" + hh + "_" + index).bind("propertychange",function(){
			var 
				$self = jQuery("#" + hh + "_" + index),
				innum = $self.attr("id").split(hh)[1];
			getGbName($self, innum);
		})
	}
}

function getGbName(obj, index) {
	var hhVal = handleEmpty(jQuery("#" + hh + index).val());
	var gbId = gb + index;
	
	// 清空信息
	if (hhVal == "") {
		clearValue(gbId);
		return;
	}
	
	// 根据货号获取国别信息
	var params = {"hh": hhVal};
	jQuery.ajax({
		type : "post",
		url : "/ningb/module/workflow/request/action/workflowData321.jsp",
		data : params,
		dataType : "json",
		success : function(result) {
			if (handleEmpty(result) == "") {
				clearValue(gbId);
				return;
			}
			
			if (result.code == 0 || result.code == "0") {
				try {
					var data = result.data;
					if (handleEmpty(data) == "") {
						clearValue(gbId);
						return;
					}
					
					setValue(gbId, data.gbname);
				} catch (e) {}
			
			}
		
		}

	})
}
	
function setValue(fieldId, fieldValue) {
	jQuery("#" + fieldId).val(fieldValue);
	jQuery("#" + fieldId + "span").html(fieldValue);
}

function clearValue(fieldId) {
	jQuery("#" + fieldId).val("");
	jQuery("#" + fieldId + "span").html("");
}

function handleEmpty(val) {
	if (val == undefined || val == null) {
		return "";
	} else {
		return val;
	}
}

function browerInfo() {
	var brower = {name: "unknown", version: 0},
		userAgent = window.navigator.userAgent.toLowerCase();
		
	if (/(msie|firefox|chrome)\D+(\d[\d.]*)/.test(userAgent)) {
		brower.name = RegExp.$1;
		brower.version = RegExp.$2;
		
	// safari
	} else if (/version\D+(\d[\d.]*).*safari/.test(userAgent)) {
	}
	
	return brower;
}
</script>
