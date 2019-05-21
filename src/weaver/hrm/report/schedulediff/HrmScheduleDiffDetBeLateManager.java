package weaver.hrm.report.schedulediff;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weaver.common.StringUtil;
import weaver.hrm.User;
import weaver.hrm.report.manager.HrmReportManager;
import weaver.hrm.schedule.domain.HrmSchedule;

/**
 * 渤海保险 考勤情况统计 - 迟到明细 后台管理类
 * @author 方观生
 * @date 2006-12-22
 * 
 * 分部增加签到次数功能，改造查询逻辑 Modified by wcd 2015-05-08
 * 
 * 优化查询效率，支持排班人员
 * @author 王长东
 * @date 2015-11-12
 */
public class HrmScheduleDiffDetBeLateManager extends HrmReportManager {
	
	public HrmScheduleDiffDetBeLateManager() {
		this(null);
	}
	
	public HrmScheduleDiffDetBeLateManager(User user) {
		super(user, SignType.BE_LATE);
	}
	
	public List getScheduleList(User user, Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
		return getScheduleList(map);
	}
	
	protected List<Map<String, Object>> getScheduleList(List<Map<String, Object>> list, Map<String, Map<String, List<HrmSchedule>>> scheduleMap) {
		List<Map<String, Object>> inList = getSignInList();
		List<Map<String, Object>> relList = new ArrayList<Map<String, Object>>();
		if(list != null) {
			for(Map<String, Object> m : list) {
				if(notEquals(StringUtil.parseToInt(String.valueOf(m.get("signId")), 0),StringUtil.parseToInt(String.valueOf(m.get("signType")), 0), StringUtil.vString(m.get("signDate")), StringUtil.vString(m.get("signTime")), inList)) continue;
				
				// 不算迟到：8:00 到 8:05 (不包括 8:05);
				String signTime = StringUtil.vString(m.get("signTime"));
				// 处理时间类似为 8:05 与 8:05:00 比较的问题
				if (signTime != null && !"".equals(signTime) && signTime.length() == 5) signTime += ":00";
				//if (signTime.compareTo("08:05:00") < 0) continue;
				String signDate = StringUtil.vString(m.get("signDate"));
				if (signDate.compareTo("2019-04-19") > 0 && signTime.compareTo("08:30:00") < 0) continue;
				if (signDate.compareTo("2019-04-12") <= 0 && signTime.compareTo("08:05:00") < 0) continue;
				if (signDate.compareTo("2019-04-12") > 0 && signDate.compareTo("2019-04-19") <= 0) continue;
				
				relList.add(m);
			}
		}
		return relList;
	}
	
	private boolean notEquals(int signId,int signType, String signDate, String signTime, List<Map<String, Object>> inList) {
		boolean value = false;
		switch(signType) {
		case 1:
		case 2:
			if(inList != null) {
				for(Map<String, Object> m : inList) {
					if(StringUtil.parseToInt(String.valueOf(m.get("signId")), 0) != signId) continue;
					if(StringUtil.vString(m.get("signDate")).equals(signDate)) {
						if(signType == 2) {
							if(signTime.compareTo(StringUtil.vString(m.get("signStartTime"))) < 0) {
								if(!StringUtil.vString(m.get("signTime")).equals(signTime) && StringUtil.vString(m.get("signTime")).compareTo(StringUtil.vString(m.get("signStartTime"))) < 0) {
									value = true;
									break;
								}
							} else {
								if(!StringUtil.vString(m.get("signTime")).equals(signTime) && StringUtil.vString(m.get("signTime")).compareTo(StringUtil.vString(m.get("signStartTime"))) > 0) {
									value = true;
									break;
								}
							}
						} else {
							if(!StringUtil.vString(m.get("signTime")).equals(signTime)) {
								value = true;
							}
							break;
						}
					}
				}
			}
			break;
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getSignInList() {
		HrmScheduleDiffDetSignInManager inManager = new HrmScheduleDiffDetSignInManager();
		inManager.setPersonList(personList);
		inManager.setIndexMap(indexMap);
		return inManager.getScheduleList(bean);
	}
}