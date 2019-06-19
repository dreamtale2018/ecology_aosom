package weaver.hrm.report.schedulediff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weaver.common.DateUtil;
import weaver.common.StringUtil;
import weaver.hrm.User;
import weaver.hrm.attendance.domain.HrmAttFlowType;
import weaver.hrm.attendance.domain.HrmLeaveTypeColor;
import weaver.hrm.attendance.manager.HrmLeaveTypeColorManager;
import weaver.hrm.report.domain.HrmReport;
import weaver.hrm.report.manager.HrmReportManager;
import weaver.hrm.schedule.domain.HrmScheduleSetDetail;

/**
 * 渤海保险 考勤情况统计 后台管理类
 * @author 方观生
 * @date 2006-12-22
 * 
 * 优化查询效率，支持排班人员
 * @author 王长东
 * @date 2015-11-11
 */
public class HrmScheduleDiffManager extends HrmReportManager {
	
	private HrmReportManager reportManager = null;

	public HrmScheduleDiffManager() {
		this(null);
	}
	
	public HrmScheduleDiffManager(User user) {
		super(user, SignType.DIFF_REPORT);
	}
	
	public int getSubCompanyId() {
		return bean.getSubCompanyId();
	}

	/**
	 * 计算合计的工作天数
	 */
	public int getTotalWorkingDays(String fromDate, String toDate) {
		if (StringUtil.isNull(fromDate, toDate) || fromDate.compareTo(toDate) > 0) return 0;
		Map<String, Boolean> workDayMap = manager.isWorkday(fromDate, toDate, manager.getSubCompanyId());
		boolean hasReachToDate = false;
		int totalWorkingDays = 0;
		for (String currentDate = fromDate; !hasReachToDate;) {
			if (currentDate.equals(toDate)) hasReachToDate = true;

			Boolean isWorkDay = workDayMap.get(currentDate);
			if (isWorkDay != null && isWorkDay.booleanValue()) totalWorkingDays++;
			
			currentDate = DateUtil.addDate(currentDate, 1);
		}
		return totalWorkingDays;
	}

	protected void updateData() {
		// 更新迟到（A、B）的数据
		updateDataOfBeLate();
		// 更新早退（A、B）的数据
		updateDataOfLeaveEarly();
		// 更新旷工的数据
		updateDataOfAbsentFromWork();
		// 更新漏签的数据
		updateDataOfNoSign();
		// 更新请假（病假、事假、其他假别、备注）的数据
		updateDataForAttFlow(leaveMap, HrmAttFlowType.LEAVE);
		// 更新出差的数据
		updateDataForAttFlow(evectionMap, HrmAttFlowType.EVECTION);
		// 更新公出的数据
		updateDataForAttFlow(outMap, HrmAttFlowType.OUT);
		// 更新加班的数据
		updateDataForAttFlow(overTimeMap, HrmAttFlowType.OVERTIME);
	}

	private void updateDataOfBeLate() {
		setReportManager(new HrmScheduleDiffDetBeLateManager());
		List<Map<String, Object>> list = getReportList();
		for (Map<String, Object> map : list) {
			String resourceId = StringUtil.vString(map.get("resourceId"));
			int index = StringUtil.parseToInt(indexMap.get(resourceId), -1);
			if (index >= 0) {
				Map<String, Object> scheduleMap = personList.get(index);
				resetValue(scheduleMap, "beLate");
				// 迟到：A：09点以前迟到，B：09点后迟到(包括09点)；
				if (StringUtil.vString(map.get("signTime")).compareTo("09:00:00") < 0) {
					resetValue(scheduleMap, "beLateA");
				} else {
					resetValue(scheduleMap, "beLateB");
				}
				String signTime = StringUtil.vString(map.get("signTime"));
				// 处理时间类似为 8:05 与 8:05:00 比较的问题
				if (signTime != null && !"".equals(signTime) && signTime.length() == 5) signTime += ":00";
				/*// 不算迟到：8:00 到 8:05 (不包括 8:05); 大于 8:05 才算迟到
				if (signTime.compareTo("08:05:00") >= 0) {
					resetValue(scheduleMap, "beLateCusA");
				}
				// 迟到 (20以内) 8:05 到 8:20
				if (signTime.compareTo("08:05:00") >= 0
						&& signTime.compareTo("08:20:00") <= 0) {
					resetValue(scheduleMap, "beLateCusB");
				} 
				// 迟到 (超过 20) 8:20 以后
				if (signTime.compareTo("08:20:00") > 0) {
					resetValue(scheduleMap, "beLateCusC");
				}*/
				String signDate = StringUtil.vString(map.get("signDate"));
				// 4月15号以后迟到 (超过 30) 8:30 以后
				if (signDate.compareTo("2019-04-19") > 0 && signDate.compareTo("2019-05-31") <= 0 && signTime.compareTo("08:30:00") > 0) {
					resetValue(scheduleMap, "beLateCusA");
				}
				// 4月15号以前不算迟到：8:00 到 8:05 (不包括 8:05); 大于 8:05 才算迟到
				if ((signDate.compareTo("2019-04-12") <= 0 || signDate.compareTo("2019-05-31") > 0) && signTime.compareTo("08:05:00") >= 0) {
					resetValue(scheduleMap, "beLateCusA");
				}
				// 4月15号以前迟到 (20以内) 8:05 到 8:20
				if ((signDate.compareTo("2019-04-12") <= 0 || signDate.compareTo("2019-05-31") > 0) && signTime.compareTo("08:05:00") >= 0
						&& signTime.compareTo("08:20:00") <= 0) {
					resetValue(scheduleMap, "beLateCusB");
				} 
				// 4月15号以前迟到 (超过 20) 8:20 以后
				if ((signDate.compareTo("2019-04-12") <= 0 || signDate.compareTo("2019-05-31") > 0) && signTime.compareTo("08:20:00") > 0) {
					resetValue(scheduleMap, "beLateCusC");
				}
			}
		}
	}
	
	private void resetValue(Map<String, Object> map, String key) {
		resetValue(map, key, 1);
	}
	
	private void resetValue(Map<String, Object> map, String key, int num) {
		int value = StringUtil.parseToInt((String)map.get(key), 0);
		value += num;
		map.put(key, String.valueOf(value));
	}
	
	private void resetValue(Map<String, Object> map, String key, double num) {
		double value = StringUtil.parseToDouble((String)map.get(key), 0);
		value += num;
		map.put(key, String.valueOf(StringUtil.round(value)));
	}
	
	private void setReportManager(HrmReportManager manager) {
		manager.setUser(user);
		manager.setPersonList(personList);
		manager.setIndexMap(indexMap);
		this.reportManager = manager;
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getReportList() {
		return reportManager == null ? new ArrayList<Map<String, Object>>() : reportManager.getScheduleList(bean);
	}

	private void updateDataOfLeaveEarly() {
		setReportManager(new HrmScheduleDiffDetLeaveEarlyManager());
		List<Map<String, Object>> list = getReportList();
		for (Map<String, Object> map : list) {
			String resourceId = StringUtil.vString(map.get("resourceId"));
			int index = StringUtil.parseToInt(indexMap.get(resourceId), -1);
			if (index >= 0) {
				Map<String, Object> scheduleMap = personList.get(index);
				resetValue(scheduleMap, "leaveEarly");
				// 早退：A：17点以后早退，B：17点前早退（包括17点）；
				if (StringUtil.vString(map.get("signTime")).compareTo("17:00:00") > 0) {
					resetValue(scheduleMap, "leaveEarlyA");
				} else {
					resetValue(scheduleMap, "leaveEarlyB");
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void updateDataOfAbsentFromWork() {
		setReportManager(new HrmScheduleDiffDetAbsentFromWorkManager());
		List<Map<String, Object>> list = getReportList();
		for (Map<String, Object> map : list) {
			String resourceId = StringUtil.vString(map.get("resourceId"));
			int index = StringUtil.parseToInt(indexMap.get(resourceId), -1);
			if (index >= 0) resetValue(personList.get(index), "absentFromWork");
		}
	}

	private void updateDataOfNoSign() {
		setReportManager(new HrmScheduleDiffDetNoSignManager());
		List<Map<String, Object>> list = getReportList();
		for (Map<String, Object> map : list) {
			String resourceId = StringUtil.vString(map.get("resourceId"));
			int index = StringUtil.parseToInt(indexMap.get(resourceId), -1);
			if (index >= 0) resetValue(personList.get(index), "noSign");
		}
	}
	
	private HrmScheduleSetDetail getDetailBean(List<HrmScheduleSetDetail> detailList, String date) {
		HrmScheduleSetDetail detailBean = null;
		for(HrmScheduleSetDetail bean : detailList) {
			if(bean.getField003().equals(date)) {
				detailBean = bean;
				break;
			}
		}
		return detailBean;
	}
	
	@SuppressWarnings("unchecked")
	private void updateDataForAttFlow(Map<String, List<HrmReport>> map, HrmAttFlowType aType) {
		String fromDate = bean.getFromDate();
		String toDate = bean.getToDate();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String resourceId = StringUtil.vString(entry.getKey());
			List<HrmReport> list = (List<HrmReport>)entry.getValue();
			int index = StringUtil.parseToInt((String) indexMap.get(resourceId), -1);
			if (index < 0 || list == null) continue;
			
			Map<String, Object> scheduleMap = personList.get(index);
			List<HrmScheduleSetDetail> detailList = (List<HrmScheduleSetDetail>)scheduleMap.get("detailList");
			if(detailList == null) continue;
			HrmScheduleSetDetail detailBean = null;
			String _fromDate = "", _fromTime = "", _toDate = "", _toTime = "";
			for(HrmReport rBean : list) {
				_fromDate = rBean.getFromDate();
				_fromTime = rBean.getFromTime();
				_toDate = rBean.getToDate();
				_toTime = rBean.getToTime();
				if (_fromDate.compareTo(fromDate) < 0) {
					_fromDate = fromDate;
					_fromTime = "00:00";
				}
				if (_toDate.compareTo(toDate) > 0) {
					String tempTime = "";
					if((detailBean = getDetailBean(detailList, _fromDate)) != null) {
						List<String[]> tList = manager.timeToList(detailBean.getWorkTime());
						for(String[] times : tList) {
							if(times[0].compareTo(times[1]) > 0) {
								tempTime = _toTime.compareTo(times[1]) > 0 ? times[1] : _toTime;
								break;
							}
						}
					}
					_toDate = DateUtil.addDate(toDate, 1);
					_toTime = StringUtil.isNull(tempTime) ? "00:00" : tempTime;
				}
				putAttFlowValue(aType, rBean, scheduleMap, _fromDate, _fromTime, _toDate, _toTime);
			}
		}
	}
	
	private void putAttFlowValue(HrmAttFlowType aType, HrmReport rBean, Map<String, Object> map, String _fromDate, String _fromTime, String _toDate, String _toTime) {
		String tempKey = aType.getName();
		String curValue = "";
		int subCompanyId = bean.getSubCompanyId();
		User user = manager.getUser(rBean.getResourceId());
		switch(aType.getType()) {
		case 3:
			curValue = manager.getTotalRestHours(_fromDate, _fromTime, _toDate, _toTime, subCompanyId, user);
			tempKey += StringUtil.vString(rBean.getOtype());
			break;
		case 0:
			tempKey += StringUtil.vString(rBean.getNewLeaveType());
		default:
			if(aType.getType() == 0){
				leaveTypeColorManager = leaveTypeColorManager == null? new HrmLeaveTypeColorManager():leaveTypeColorManager;  
				HrmLeaveTypeColor leaveTypeColor = (HrmLeaveTypeColor)leaveTypeColorManager.get(leaveTypeColorManager.getMapParam("field004:"+rBean.getNewLeaveType()));
				leaveTypeColor = leaveTypeColor == null? new HrmLeaveTypeColor():leaveTypeColor;  
				manager.setIsCalWorkDay(leaveTypeColor.getIsCalWorkDay());
				manager.setRelateweekday(leaveTypeColor.getRelateweekday());
			}else{
				manager.setIsCalWorkDay(1);
				manager.setRelateweekday(2);
				manager.setScheduleUnit(0);
			}
			curValue = manager.getTotalWorkDays(_fromDate, _fromTime, _toDate, _toTime, subCompanyId, user);
			break;
		}
		resetValue(map, tempKey, StringUtil.parseToDouble(curValue, 0));
	}
}
