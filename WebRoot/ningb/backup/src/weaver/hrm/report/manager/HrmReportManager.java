package weaver.hrm.report.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weaver.common.DateUtil;
import weaver.common.StringUtil;
import weaver.framework.BaseManager;
import weaver.hrm.User;
import weaver.hrm.common.Constants;
import weaver.hrm.report.dao.HrmReportDao;
import weaver.hrm.report.domain.HrmReport;
import weaver.hrm.schedule.domain.HrmSchedule;
import weaver.hrm.schedule.domain.HrmScheduleDate;
import weaver.hrm.schedule.domain.HrmScheduleSetDetail;
import weaver.hrm.attendance.domain.HrmScheduleApplication;
import weaver.hrm.schedule.manager.HrmScheduleManager;
import weaver.hrm.attendance.manager.HrmLeaveTypeColorManager;
import weaver.hrm.attendance.manager.HrmScheduleApplicationManager;
import weaver.systeminfo.SystemEnv;
/**
 * HrmReportManager
 * <p>
 * Generated from 长东设计 www.mfstyle.cn
 * </p>
 * 
 * @author wcd
 * @version 1.0 2015-11-12
 */
public abstract class HrmReportManager extends BaseManager<HrmReport> {

	protected boolean sortForResult;
	
	protected List<Map<String, Object>> personList;
	
	protected Map<String, String> indexMap;
	
	protected Map<String, List<HrmReport>> leaveMap;
	
	protected Map<String, List<HrmReport>> evectionMap;
	
	protected Map<String, List<HrmReport>> outMap;
	
	protected Map<String, List<HrmReport>> otherMap;
	
	protected Map<String, List<HrmReport>> overTimeMap;
	
	protected HrmReport bean;
	
	private boolean needInit = true;
	
	private HrmReportDao dao = null;
	
	protected SignType type = null;
	
	private static final String BEGIN_SIGN_TIME = "00:00";
	
	private static final String END_SIGN_TIME = "23:59";
	
	private int lanId;
	
	private String showInfo;
	
	protected long time = 0L;
	
	protected HrmScheduleManager manager = null;
	
	private boolean isDiffReport = false;
	
	protected Map<String, Map<String, HrmSchedule>> pSchedules = null;
	
	private String currentDate = "";
	
	private String nextDate = "";

	private int scheduleUnit = 0 ;//考勤流程的最小单位（当前只作为请假流程单位）默认0是无限制

	protected HrmLeaveTypeColorManager leaveTypeColorManager = null;//根据请假类型获取请假天数是否过滤非工作日
	
	public enum SignType {
		SIGN_IN("signIn"), 
		SIGN_OUT("signOut"), 
		BE_LATE("beLate"), 
		LEAVE_EARLY("leaveEarly"), 
		NO_SIGN("noSign"), 
		ABSENT("absent"), 
		DIFF_REPORT("diffReport");
		
		public String name;
		
		private SignType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
	}
	
	public enum NoSignType {
		SING_IN, SING_OUT, SING_TIME
	}
	
	public HrmReportManager(User user, SignType type) {
		this.setSignType(type);
		this.setUser(user);
		this.setSortForResult(true);
		this.bean = new HrmReport();
		this.dao = new HrmReportDao();
		this.setDao(dao);
		this.personList = new ArrayList<Map<String, Object>>();
		this.indexMap = new HashMap<String, String>();
		this.lanId = user == null ? 7 : user.getLanguage();
		this.showInfo = SystemEnv.getHtmlLabelNames("367,87", lanId);
		this.scheduleUnit = getUnit();
		this.leaveTypeColorManager = new HrmLeaveTypeColorManager();
	}
	
	protected void setSignType(SignType type) {
		this.type = type;
		this.init();
	}
	
	private void init() {
		this.time = System.currentTimeMillis();
		this.currentDate = DateUtil.getCurrentDate();
		this.nextDate = DateUtil.addDate(currentDate, 1);
		this.pSchedules = new HashMap<String, Map<String, HrmSchedule>>();
		switch(type){
		case SIGN_IN:
			break;
		case SIGN_OUT:
			break;
		case BE_LATE:
			break;
		case LEAVE_EARLY:
			break;
		case NO_SIGN:
			break;
		case ABSENT:
			break;
		case DIFF_REPORT:
			this.isDiffReport = true;
			break;
		}
	}
	
	public void setUser(User user) {
		super.setUser(user);
		this.manager = new HrmScheduleManager(user);
	}
	
	public void setSortForResult(boolean para) {
		this.sortForResult = para;
	}

	public void setIndexMap(Map<String, String> indexMap) {
		this.indexMap = indexMap;
	}
	
	public Map<String, String> getIndexMap() {
		return this.indexMap;
	}

	public void setPersonList(List<Map<String, Object>> personList) {
		this.personList = personList;
	}
	
	public List<Map<String, Object>> getPersonList() {
		return this.personList;
	}
	
	public void setBean(HrmReport bean) {
		this.bean = bean;
	}
	
	public HrmReport getBean() {
		return this.bean;
	}
	
	protected boolean checkParam(String fromDate, String toDate) {
		return StringUtil.isNull(fromDate, toDate)
			|| fromDate.compareTo(toDate) > 0
			|| this.personList == null
			|| this.personList.size() == 0
			|| this.indexMap == null
			|| this.indexMap.isEmpty();
	}
	
	private void initBean(String fromDate, String toDate, int subCompanyId, int departmentId, String resourceId) {
		bean.setFromDate(StringUtil.vString(fromDate));
		bean.setToDate(StringUtil.vString(toDate));
		bean.setSubCompanyId(subCompanyId);
		bean.setDepartmentId(departmentId);
		bean.setResId(StringUtil.vString(resourceId));
		
		if(needInit) initReport();
	}
	
	@SuppressWarnings("unchecked")
	private void initReport() {
		Map<String, Object> map = manager.getDatePersonInfo(bean.getFromDate(), bean.getToDate(), bean.getSubCompanyId(), bean.getDepartmentId(), bean.getResId());
		this.personList = (List<Map<String, Object>>)map.get("personList");
		this.indexMap = (Map<String, String>)map.get("indexMap");
		this.leaveMap = initMap((Map<String, List<HrmReport>>)map.get("leaveMap"));
		this.evectionMap = initMap((Map<String, List<HrmReport>>)map.get("evectionMap"));
		this.outMap = initMap((Map<String, List<HrmReport>>)map.get("outMap"));
		this.otherMap = initMap((Map<String, List<HrmReport>>)map.get("otherMap"));
		if(isDiffReport) {
			this.overTimeMap = manager.getOverTimeMap(bean.getFromDate(), bean.getToDate(), bean.getSubCompanyId(), bean.getDepartmentId(), bean.getResId());
		}
		this.bean.setSubCompanyId(StringUtil.parseToInt((String)map.get("subCompanyId")));
	}
	
	private List<HrmSchedule> getScheduleSignList() {
		return dao.getScheduleSignList(bean, getSignType());
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, HrmScheduleDate> getPersonDateMap(String resourceId) {
		Map<String, HrmScheduleDate> dateMap = null;
		int index = indexMap == null ? -1 : StringUtil.parseToInt(indexMap.get(resourceId));
		if(index >= 0) {
			Map<String, Object> pMap = personList == null ? null : personList.get(index);
			dateMap = pMap == null ? null : (Map<String, HrmScheduleDate>)pMap.get("dateMap");
		}
		return dateMap;
	}
	
	private Map<String, Object> toMap(HrmSchedule sBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("subCompanyId", String.valueOf(sBean.getSubCompanyId()));
		map.put("departmentId", String.valueOf(sBean.getDepartmentId()));
		map.put("departmentName", sBean.getDepartmentName());					
		map.put("resourceId", String.valueOf(sBean.getResourceId()));
		map.put("resourceName", sBean.getResourceName());						
		map.put("statusName", getStatusName(sBean.getStatus()));
		map.put("status", sBean.getStatus());
		map.put("currentDate", sBean.getSignDate());	
		map.put("signId", String.valueOf(sBean.getId()));
		map.put("signDate", sBean.getSignDate());
		map.put("oldSignDate", sBean.getOldSignDate());
		map.put("signTime", sBean.getSignTime());					
		map.put("clientAddress", sBean.getClientAddress());
		map.put("scheduleName", sBean.get$ScheduleName());
		map.put("signType", sBean.get$SignType());
		map.put("signStartTime", sBean.get$SignStartTime());
		map.put("_addr", sBean.getAddr());
		map.put("longitude", sBean.getLongitude());
		map.put("latitude", sBean.getLatitude());
		map.put("mfer", type.getName());
		String addr = "";
		String addrDetail = "";
		if(StringUtil.isNull(sBean.getSignFrom()) || sBean.getSignFrom().equalsIgnoreCase("pc")) {
			addrDetail = "";
		} else {
			if(StringUtil.isNull(sBean.getLongitude(),sBean.getLatitude())) {
				addrDetail = "";
			} else {
				addrDetail = StringUtil.vString(sBean.getAddr(), showInfo);
			}
		}
		if(StringUtil.isNotNull(addrDetail)) {
			addr="<a href=\"javascript:void(0);\" onclick=\"openMap('"+sBean.getLongitude()+"','"+sBean.getLatitude()+"','"+Constants.WEB_MAP_KEY+"','"+(addrDetail.equals(showInfo)?"null":addrDetail)+"');\">"+addrDetail+"</a>";
		}
		map.put("addr", addr);
		map.put("addrDetail", addrDetail.equals(showInfo) ? (sBean.getLongitude()+","+sBean.getLatitude()) : addrDetail);
		return map;
	}
	
	private String getStatusName(String status) {
		String statusName = "";
		if (StringUtil.isNotNull(status)) {
			if (status.equals("0")) {
				statusName = SystemEnv.getHtmlLabelName(15710, lanId);
			} else if (status.equals("1")) {
				statusName = SystemEnv.getHtmlLabelName(15711, lanId);
			} else if (status.equals("2")) {
				statusName = SystemEnv.getHtmlLabelName(480, lanId);
			} else if (status.equals("3")) {
				statusName = SystemEnv.getHtmlLabelName(15844, lanId);
			} else if (status.equals("4")) {
				statusName = SystemEnv.getHtmlLabelName(6094, lanId);
			} else if (status.equals("5")) {
				statusName = SystemEnv.getHtmlLabelName(6091, lanId);
			} else if (status.equals("6")) {
				statusName = SystemEnv.getHtmlLabelName(6092, lanId);
			} else if (status.equals("7")) {
				statusName = SystemEnv.getHtmlLabelName(2245, lanId);
			} else if (status.equals("10")) {
				statusName = SystemEnv.getHtmlLabelName(1831, lanId);
			}
		}
		return statusName;
	}
	
	protected void updateData() {
		
	}
	
	public boolean timeIsIn(String time, String beginTime, String endTime) {
		String cDate = currentDate + " " + time;
		String bDate = currentDate + " " + beginTime;
		boolean value = false;
		if(beginTime.compareTo(endTime) > 0) {
			cDate = (time.compareTo(beginTime) < 0 ? nextDate : currentDate) + " " + time;
			value = DateUtil.isInDateRange(cDate, bDate, currentDate + " 23:59:59") || DateUtil.isInDateRange(cDate, nextDate + " 00:00:00", nextDate + " " + endTime);
		} else {
			value = DateUtil.isInDateRange(cDate, bDate, currentDate + " " + endTime);
		}
		return value;
	}
	
	protected void initHrmSchedule(Map<String, Object> pMap, HrmSchedule sBean) {
		sBean.setResourceId(StringUtil.parseToInt((String)pMap.get("resourceId")));
		sBean.setResourceName(StringUtil.vString(pMap.get("resourceName")));
		sBean.setDepartmentId(StringUtil.parseToInt((String)pMap.get("departmentId")));
		sBean.setDepartmentName(StringUtil.vString(pMap.get("departmentName")));
		sBean.setSubCompanyId(StringUtil.parseToInt((String)pMap.get("subCompanyId")));
		sBean.setStatus(StringUtil.vString(pMap.get("pStatus")));
	}
	
	public void initHrmSchedule(HrmScheduleDate dateBean, HrmSchedule sBean) {
		if(dateBean.isSchedulePerson()) initHrmScheduleByDdBean(sBean, dateBean.getDBean());			
		else initHrmScheduleByDsBean(sBean, dateBean.getSBean());
	}
	
	private void initHrmScheduleByDdBean(HrmSchedule sBean, HrmScheduleSetDetail ddBean) {
		List<String[]> workTimes = null, signTimes = null;
		if(manager != null) {
			signTimes = manager.timeToList(ddBean.getSignTime());
			workTimes = manager.timeToList(ddBean.getWorkTime());
		}
		sBean.setSchedulePerson(true);
		sBean.setDsSignType("");
		initHrmSchedule(sBean, ddBean.getField001Name(), null, null, workTimes, signTimes);
	}
	
	private void initHrmScheduleByDsBean(HrmSchedule sBean, HrmSchedule dsBean) {
		List<String[]> workTimes = null, signTimes = null;
		workTimes = new ArrayList<String[]>();
		signTimes = new ArrayList<String[]>();
		if(dsBean.getSignType().equals("2")) {
			switch (type) {
			case SIGN_IN:
			case ABSENT:
				signTimes.add(new String[]{BEGIN_SIGN_TIME, dsBean.getOffDutyTimeAM()});
				signTimes.add(new String[]{dsBean.getSignStartTime(), END_SIGN_TIME});
				break;
			case BE_LATE:
				signTimes.add(new String[]{dsBean.getOnDutyTimeAM(), dsBean.getOffDutyTimeAM()});
				signTimes.add(new String[]{dsBean.getOnDutyTimePM(), dsBean.getOffDutyTimePM()});
				break;
			case NO_SIGN:
				signTimes.add(new String[]{BEGIN_SIGN_TIME, dsBean.getOnDutyTimePM()});
				signTimes.add(new String[]{dsBean.getSignStartTime(), END_SIGN_TIME});
				break;
			case SIGN_OUT:
			case LEAVE_EARLY:
				signTimes.add(new String[]{dsBean.getOffDutyTimeAM(), dsBean.getOnDutyTimePM()});
				signTimes.add(new String[]{dsBean.getOffDutyTimePM(), END_SIGN_TIME});
				break;
			}
			workTimes.add(new String[]{dsBean.getOnDutyTimeAM(), dsBean.getOffDutyTimeAM()});
			workTimes.add(new String[]{dsBean.getOnDutyTimePM(), dsBean.getOffDutyTimePM()});
		} else {
			signTimes.add(new String[]{BEGIN_SIGN_TIME, END_SIGN_TIME});
			workTimes.add(new String[]{dsBean.getOnDutyTimeAM(), dsBean.getOffDutyTimePM()});
		}
		sBean.setSchedulePerson(false);
		sBean.setDsSignType(dsBean.getSignType());
		sBean.setOnDutyTimeAM(dsBean.getOnDutyTimeAM());
		sBean.setOnDutyTimePM(dsBean.getOnDutyTimePM());
		sBean.setOffDutyTimeAM(dsBean.getOffDutyTimeAM());
		sBean.setOffDutyTimePM(dsBean.getOffDutyTimePM());
		initHrmSchedule(sBean, null, dsBean.getSignType(), dsBean.getSignStartTime(), workTimes, signTimes);
	}
	
	private void initHrmSchedule(HrmSchedule sBean, String scheduleName, String type, String signStartTime, List<String[]> workTimes, List<String[]> signTimes) {
		sBean.set$ScheduleName(StringUtil.vString(scheduleName));
		sBean.set$ScheduleTitle(StringUtil.vString(scheduleName));
		sBean.set$SignType(StringUtil.vString(type));
		sBean.set$SignStartTime(StringUtil.vString(signStartTime));
		sBean.setWorkTimes(workTimes);
		sBean.setSignTimes(signTimes);
		
		String resourceId = String.valueOf(sBean.getResourceId());
		Map<String, HrmSchedule> sMap = pSchedules.get(resourceId);
		if(sMap == null) sMap = new HashMap<String, HrmSchedule>();
		sMap.put(sBean.getSignDate(), sBean);
		pSchedules.put(resourceId, sMap);
	}
	
	private void checkScheduleList(List<HrmSchedule> list) {
		List<HrmSchedule> tempList = new ArrayList<HrmSchedule>();
		switch(type) {
		case BE_LATE:
		case LEAVE_EARLY:
			for(int i=0; i<list.size(); i++) {
				HrmSchedule bean = list.get(i);
				boolean isAllowAdd = isAllowAdd(bean), isOffset = !isOffset(bean);
				if(isAllowAdd && isOffset) {
					tempList.add(bean);
				}
			}
			list.clear();
			list.addAll(tempList);
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> toScheduleList(Map<String, Map<String, List<HrmSchedule>>> scheduleMap) {
		List<Map<String, Object>> scheduleList = new ArrayList<Map<String, Object>>();
		Iterator it = scheduleMap.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			Iterator hIt = ((Map<String, List<HrmSchedule>>)entry.getValue()).entrySet().iterator();
			while(hIt.hasNext()) {
				Map.Entry hEntry = (Map.Entry)hIt.next();
				List<HrmSchedule> list = (List<HrmSchedule>)hEntry.getValue();
				if(list == null) continue;
				
				checkScheduleList(list);
				
				for(HrmSchedule sBean : list) {
					scheduleList.add(toMap(sBean));
				}
			}
		}
		runTime();
		return getScheduleList(scheduleList, scheduleMap);
	}
	
	private void runTime() {
		
	}
	
	public List getScheduleList(User user, Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> getScheduleList(Map<String,String> map){
		String fromDate = StringUtil.vString(map.get("fromDate"));
		String toDate = StringUtil.vString(map.get("toDate"));
		int subCompanyId = StringUtil.parseToInt(map.get("subCompanyId"));
		int departmentId = StringUtil.parseToInt(map.get("departmentId"));
		String resourceId = StringUtil.vString(map.get("resourceId"));
		return getScheduleList(fromDate, toDate, subCompanyId, departmentId, resourceId);
	}
	
	protected List<Map<String, Object>> getScheduleList(List<Map<String, Object>> scheduleList, Map<String, Map<String, List<HrmSchedule>>> scheduleMap) {
		return scheduleList;
	}
	
	public List getScheduleList(String fromDate, String toDate, int subCompanyId, int departmentId, int resourceId) {
		return getScheduleList(fromDate, toDate, subCompanyId, departmentId, String.valueOf(resourceId));
	}
	
	public List getScheduleList(HrmReport bean){
		this.setBean(bean);
		return bean == null ? new ArrayList() : getScheduleList(bean.getFromDate(), bean.getToDate(), bean.getSubCompanyId(), bean.getDepartmentId(), bean.getResId());
	}
	
	public List getScheduleList(String fromDate, String toDate, int subCompanyId, int departmentId, String resourceId) {
		initBean(fromDate, toDate, subCompanyId, departmentId, resourceId);
		if (checkParam(fromDate, toDate)) return new ArrayList<Map<String, Object>>();
		
		if(type == SignType.DIFF_REPORT) {
			updateData();
			runTime();
			return personList;
		}
		String curDate = "", signTime = "", rId = "";
		HrmScheduleDate dateBean = null;
		Map<String, HrmScheduleDate> dateMap = null;
		Map<String, Map<String, List<HrmSchedule>>> scheduleMap = new LinkedHashMap<String, Map<String, List<HrmSchedule>>>();
		List<HrmSchedule> signList = getScheduleSignList();
		for(HrmSchedule sBean : signList) {
			curDate = sBean.getSignDate();
			signTime = sBean.getSignTime();
			rId = String.valueOf(sBean.getResourceId());
			if((dateMap = getPersonDateMap(rId)) == null) continue;
			String tempDate = curDate, lastDate = DateUtil.addDate(curDate, -1);
			if((dateBean = dateMap.get(curDate)) == null || !dateBean.isWorkDay()) {
				boolean isContinue = true;
				if(dateBean != null && dateBean.isSchedulePerson()) {
					HrmScheduleDate tempDateBean = dateMap.get(lastDate);
					if(tempDateBean != null && tempDateBean.isWorkDay()) {
						dateBean = tempDateBean;
						HrmScheduleSetDetail detailBean = dateBean.getDBean();
						if(detailBean != null) {
							String[] wTimes = manager.getAcrossDateTime(detailBean.getWorkTime());
							String[] sTimes = manager.getAcrossDateTime(detailBean.getSignTime());
							if(sTimes != null && sTimes.length == 2 && wTimes != null && wTimes.length == 2) {
								switch(type) {
								case SIGN_IN:
								case ABSENT:
									isContinue = (signTime.compareTo(sTimes[0]+":00") >= 0 || signTime.compareTo(wTimes[1]+":00") < 0) ? false : true;
									break;
								case SIGN_OUT:
									isContinue = (signTime.compareTo(wTimes[0]+":00") >= 0 || signTime.compareTo(sTimes[1]+":00") < 0) ? false : true;
									break;
								case NO_SIGN:
									isContinue = (signTime.compareTo(sTimes[0]+":00") >= 0 || signTime.compareTo(sTimes[1]+":00") < 0) ? false : true;
									break;
								case BE_LATE:
									isContinue = (signTime.compareTo(wTimes[0]+":00") > 0 || signTime.compareTo(wTimes[1]+":00") < 0) ? false : true;
									break;
								case LEAVE_EARLY:
									isContinue = (signTime.compareTo(wTimes[0]+":00") >= 0 || signTime.compareTo(sTimes[1]+":00") < 0) ? false : true;
									break;
								}
							}
						}
					}
				}
				if(isContinue) continue;
				else tempDate = lastDate;
			} else {
				boolean isContinue = false;
				if(dateBean.isSchedulePerson()) {
					List<String[]> wTimes = manager.timeToList(dateBean.getDBean().getWorkTime());
					List<String[]> sTimes = manager.timeToList(dateBean.getDBean().getSignTime());
					String fWorkTime = wTimes.size() > 0 ? wTimes.get(0)[0] : "";
					String fSignTime = sTimes.size() > 0 ? sTimes.get(0)[0] : "";
					if(StringUtil.isNotNull(fWorkTime)) {
						switch(type) {
						case ABSENT:
						case NO_SIGN:
						case SIGN_IN:
						case BE_LATE:
							if(signTime.compareTo(fSignTime+":00") < 0) {
								tempDate = lastDate;
							}
							break;
						case SIGN_OUT:
						case LEAVE_EARLY:
							if(signTime.compareTo(fWorkTime+":00") < 0) {
								tempDate = lastDate;
							}
							break;
						}
					}
				}
				if(isContinue || tempDate.compareTo(fromDate) < 0) continue;
			}
			sBean.setTempSignDate(tempDate);
			Map<String, List<HrmSchedule>> userScheduleMap = scheduleMap.containsKey(rId) ? scheduleMap.get(rId) : null;
			List<HrmSchedule> sList = null;
			if(userScheduleMap == null) {
				userScheduleMap = new LinkedHashMap<String, List<HrmSchedule>>();
			} else {
				sList = userScheduleMap.get(tempDate);
			}
			if(sList == null) sList = new ArrayList<HrmSchedule>();
			initHrmSchedule(dateBean, sBean);
			handle(sBean, sList);
			userScheduleMap.put(tempDate, sList);
			scheduleMap.put(rId, userScheduleMap);
		}
		return toScheduleList(scheduleMap);
	}
	
	private void resetName(HrmSchedule sBean, String[] workTime) {
		String title = StringUtil.vString(sBean.get$ScheduleTitle());
		String time = workTime[0] + "-" + workTime[1];
		if(StringUtil.isNull(title)) {
			title += time;
		} else {
			title += bracket(time);
		}
		sBean.set$ScheduleName(title);
	}
	
	private void handle(HrmSchedule sBean, List<HrmSchedule> sList) {
		List<String[]> workTimes = sBean.getWorkTimes(), signTimes = sBean.getSignTimes();
		String signDate = sBean == null ? currentDate : sBean.getSignDate();
		String nextDate = DateUtil.addDate(signDate, 1);
		String cDate = "", bDate = "", eDate = "";
		for(int i=0; i<workTimes.size(); i++) {
			String[] signTime = signTimes.get(i);
			String[] workTime = workTimes.get(i);
			String[] times = getTimeRange(signTime, workTime, NoSignType.SING_IN);
			
			cDate = sBean.getOldSignDate()+" "+sBean.getSignTime();
			bDate = signDate+" "+times[0];
			eDate = (times[0].compareTo(times[1]) > 0 ? nextDate : signDate)+" "+times[1];
			if(DateUtil.isInDateRange(cDate, bDate, eDate)) {
				resetName(sBean, workTime);
				if(sList.size() == 0) {
					sList.add(sBean);
				} else {
					HrmSchedule tempBean = null;
					int index = -1;
					for(int j=0; j<sList.size(); j++) {
						HrmSchedule temp = (HrmSchedule)sList.get(j);
						cDate = temp.getOldSignDate()+" "+temp.getSignTime();
						if(DateUtil.isInDateRange(cDate, bDate, eDate)) {
							tempBean = temp;
							index = j;
							break;
						}
					}
					if(tempBean == null || index < 0) {
						sList.add(sBean);
					} else if(allowChange(sBean, tempBean)) {
						sList.set(index, sBean);
					}
				}
				break;
			}
		}
	}
	
	protected String[] getWorkTime(HrmSchedule sBean) {
		String[] result = null;
		List<String[]> workTimes = sBean.getWorkTimes(), signTimes = sBean.getSignTimes();
		for(int i=0; i<workTimes.size(); i++) {
			String[] signTime = signTimes.get(i);
			String[] workTime = workTimes.get(i);
			String[] rTime = getTimeRange(signTime, workTime, NoSignType.SING_TIME);
			if(timeIsIn(sBean.getSignTime(), rTime[0], rTime[1])) {
				result = workTime;
				break;
			}
			
		}
		return result;
	}
	
	private boolean isAllowAdd(HrmSchedule sBean) {
		String[] time = getWorkTime(sBean);
		return time != null && time.length == 2 ? allowAdd(sBean.getSignTime(), time[0], time[1]) : false;
	}
	
	protected void addValue(List<Map<String, Object>> list, List<HrmSchedule> inList, HrmSchedule sBean, HrmScheduleDate dateBean) {
		addValue(list, inList, sBean, dateBean, true);
	}
	
	protected void addValue(List<Map<String, Object>> list, List<HrmSchedule> inList, HrmSchedule sBean, HrmScheduleDate dateBean, boolean isInit) {
		addValue(list, inList, null, sBean, dateBean, isInit);
	}
	
	protected void addValue(List<Map<String, Object>> list, List<HrmSchedule> inList, List<Map<String, Object>> outList, HrmSchedule sBean, HrmScheduleDate dateBean) {
		addValue(list, inList, outList, sBean, dateBean, true);
	}
	
	protected void addValue(List<Map<String, Object>> list, List<HrmSchedule> inList, List<Map<String, Object>> outList, HrmSchedule sBean, HrmScheduleDate dateBean, boolean isInit) {
		if(isInit) initHrmSchedule(dateBean, sBean);
		List<String[]> wList = sBean.getWorkTimes();
		List<String[]> sList = sBean.getSignTimes();
		int size = wList == null ? 0 : wList.size();
		for(int i=0; i<size; i++) {
			String[] workTime = wList.get(i);
			String[] inTimes = getTimeRange(sList.get(i), workTime, NoSignType.SING_IN);
			String[] times = getTimeRange(sList.get(i), workTime, NoSignType.SING_OUT);
			boolean isCheck = !checkScheduleIsIn(sBean, inList, outList, times, inTimes), isOffset = !isOffset(sBean, workTime);
			if(isCheck && isOffset) {
				resetName(sBean, workTime);
				list.add(toMap(sBean));
			}
		}
	}
	
	private boolean checkScheduleIsIn(HrmSchedule sBean, List<HrmSchedule> inList, List<Map<String, Object>> outList, String[] times, String[] inTimes) {
		boolean result = false;
		String signDate = sBean == null ? currentDate : sBean.getSignDate();
		String nextDate = DateUtil.addDate(signDate, 1);
		String cDate = "", bDate = "", eDate = "";
		if(inList != null) {
			for(HrmSchedule bean : inList) {
				cDate = bean.getOldSignDate()+" "+bean.getSignTime();
				bDate = signDate+" "+inTimes[0];
				eDate = (inTimes[0].compareTo(inTimes[1]) > 0 ? nextDate : signDate)+" "+inTimes[1];
				if(DateUtil.isInDateRange(cDate, bDate, eDate)) {
					result = true;
					break;
				}
			}
		}
		switch(type) {
		case NO_SIGN:
			if(result) {
				result = false;
				if(outList != null) {
					for(Map<String, Object> map : outList) {
						if(!StringUtil.vString(sBean.getResourceId()).equals(StringUtil.vString(map.get("resourceId")))) continue;
						cDate = StringUtil.vString(map.get("oldSignDate"))+" "+StringUtil.vString(map.get("signTime"));
						bDate = signDate+" "+times[0];
						eDate = (times[0].compareTo(times[1]) > 0 ? nextDate : signDate)+" "+times[1];
						if(DateUtil.isInDateRange(cDate, bDate, eDate)) {
							result = true;
							break;
						}
					}
				}
			} else {
				result = true;
			}
			break;
		}
		return result;
	}
	
	private String[] getTimeRange(String[] sTime, String[] wTime, NoSignType noSignType) {
		String[] times = new String[2];
		switch(type) {
		case SIGN_IN:
		case ABSENT:
			times[0] = sTime[0];
			times[1] = wTime[1];
			break;
		case BE_LATE:
			times[0] = wTime[0];
			times[1] = wTime[1];
			break;
		case NO_SIGN:
			switch(noSignType) {
			case SING_IN:
				times[0] = sTime[0];
				times[1] = wTime[1];
				break;
			case SING_OUT:
				times[0] = wTime[0];
				times[1] = sTime[1];
				break;
			case SING_TIME:
				times[0] = sTime[0];
				times[1] = sTime[1];
				break;
			}
			break;
		case SIGN_OUT:
			times[0] = wTime[0];
			times[1] = sTime[1];
			break;
		case LEAVE_EARLY:
			times[0] = wTime[0];
			times[1] = sTime[1];
			break;
		}
		if(StringUtil.isNotNull(times[0], times[1])) {
			times[0] = getFullTime(times[0]);
			times[1] = getFullTime(times[1]);
		}
		return times;
	}
	
	private String getFullTime(String time) {
		time = StringUtil.vString(time);
		if(time.length() == 5) time += ":00";
		return time;
	}
	
	private boolean allowAdd(String signTime, String beginWorkTime, String endWorkTime) {
		beginWorkTime = getFullTime(beginWorkTime);
		endWorkTime = getFullTime(endWorkTime);
		boolean result = false;
		switch(type) {
		case BE_LATE:
			if(beginWorkTime.compareTo(endWorkTime) > 0) {
				result = signTime.compareTo(beginWorkTime) > 0 || signTime.compareTo(endWorkTime) < 0;
			} else {
				result = signTime.compareTo(beginWorkTime) > 0;
			}
			break;
		case LEAVE_EARLY:
			if(beginWorkTime.compareTo(endWorkTime) > 0) {
				result = signTime.compareTo(endWorkTime) < 0 || signTime.compareTo(beginWorkTime) > 0;
			} else {
				result = signTime.compareTo(endWorkTime) < 0;
			}
			break;
		}
		return result;
	}
	
	private boolean allowChange(HrmSchedule sBean, HrmSchedule tempBean) {
		boolean result = false;
		switch(type) {
		case SIGN_IN:
		case BE_LATE:
		case ABSENT:
		case NO_SIGN:
			result = sBean.compareTo(tempBean) < 0;
			break;
		case SIGN_OUT:
		case LEAVE_EARLY:
			result = sBean.compareTo(tempBean) > 0;
			break;
		}
		return result;
	}
	
	private int getSignType() {
		int signType = 0;
		switch(type) {
		case SIGN_IN:
		case BE_LATE:
		case ABSENT:
		case NO_SIGN:
			signType = 1;
			break;
		case SIGN_OUT:
		case LEAVE_EARLY:
			signType = 2;
			break;
		}
		return signType;
	}
	
	/**
	 * 获得签到时间的下一个整分钟
	 * 09:30:00 --> 09:30
	 * 09:30:01 --> 09:31
	 */ 	
	protected String getNextMin(String signTime) {
		String result = "";
		if(StringUtil.isNotNull(signTime)) {
			if(signTime.length() >= 19) {
				signTime = signTime.substring(17).equals("00") ? signTime : DateUtil.addSecond(signTime, 60);
			}
			if(signTime.length() >= 16) {
				result = signTime.substring(11, 16);
			} else {
				result = signTime;
			}
		}
		return result;
	}
	
	/**
	 * 获得签退时间的上一个整分钟
	 * 09:30:00 --> 09:30
	 * 09:30:01 --> 09:30
	 */ 	
	protected String getLastMin(String signTime) {
		String result = "";
		if(StringUtil.isNotNull(signTime)) {
			if(signTime.length() >= 17) {
				result = signTime.substring(11, 16);
			} else {
				result = signTime;
			}
		}
		return result;
	}
	
	private boolean isOffset(HrmSchedule bean) {
		return isOffset(bean, getWorkTime(bean));
	}
	
	private boolean isOffset(HrmSchedule bean, String[] workTime) {
		boolean result = false;
		if(workTime != null && workTime.length == 2) {
			String signDate = bean.getSignDate();
			String signTime = bean.getSignTime();
			if(signTime.compareTo(workTime[1]) > 0 
					&& (workTime[0].compareTo(workTime[1]) > 0 && !workTime[1].equals("00:00") ? signTime.compareTo(workTime[0]) < 0 : true)) {
				signTime = workTime[1];
			} else {
				switch(type) {
				case BE_LATE:
				case ABSENT:
				case NO_SIGN:
					signTime = getNextMin(bean.getSignFullTime());
					break;
				case LEAVE_EARLY:
					signTime = getLastMin(bean.getSignFullTime());
					break;
				}
				int eIndex = StringUtil.vString(signTime).indexOf(" ");
				if(eIndex != -1) signTime = signTime.split(" ")[1];
			}
			String startWTime = "", endWTime = "";
			switch(type) {
			case BE_LATE:
				startWTime = workTime[0];
				endWTime = signTime;
				break;
			case LEAVE_EARLY:
				startWTime = signTime;
				endWTime = workTime[1];
				break;
			default:
				startWTime = workTime[0];
				endWTime = workTime[1];
				break;
			}
			String tempStartDate = "", tempEndDate = "";
			if(startWTime.compareTo(endWTime) > 0) {
				tempEndDate = DateUtil.addDate(signDate, 1) + " " + endWTime;
			} else {
				tempEndDate = signDate + " " + endWTime;
			}
			tempStartDate = signDate + " " + startWTime;
			String[] tempSDates = {};
			if(!bean.isSchedulePerson() && !bean.getDsSignType().equals("2")) {
				List<String> list = new ArrayList<String>();
				list.addAll(Arrays.asList(DateUtil.getDateMinutes(tempStartDate, tempEndDate)));
				tempSDates = list.toArray(new String[list.size()]);
				String[] restTimes = DateUtil.getDateMinutes(signDate + " "+bean.getOffDutyTimeAM(), signDate + " "+bean.getOnDutyTimePM());
				list.clear();
				for(int i=0; i<tempSDates.length; i++) {
					boolean isRest = false;
					for(int y=0; y<restTimes.length; y++) {
						if(tempSDates[i].equals(restTimes[y])) {
							isRest = true;
							break;
						}
					}
					if(!isRest) list.add(tempSDates[i]);
				}
				tempSDates = list.toArray(new String[list.size()]);
			} else {
				tempSDates = DateUtil.getDateMinutes(tempStartDate, tempEndDate);
			}
			String[] sDates = new String[tempSDates.length];
			for(int i=0; i<tempSDates.length; i++) {
				sDates[i] = tempSDates[i].split(" ")[1];
			}
			String[] fDates = toArray(getFlowTime(String.valueOf(bean.getResourceId()), signDate, new String[]{startWTime, endWTime}));
			if(sDates.length <= fDates.length) result = Arrays.asList(fDates).containsAll(Arrays.asList(sDates));
		}
		return result;
	}
	
	protected long totalTime(String resourceId, String signDate, String[] workTime) {
		long time = totalTime(resourceId, signDate, workTime, leaveMap);
		time += totalTime(resourceId, signDate, workTime, evectionMap);
		time += totalTime(resourceId, signDate, workTime, outMap);
		time += totalTime(resourceId, signDate, workTime, otherMap);
		return time;
	}
	
	private long totalTime(String resourceId, String signDate, String[] workTime, Map<String, List<HrmReport>> map) {
		String fromDate = "", fromTime = "", toDate = "", toTime = "";
		long time = 0;
		List<HrmReport> list = map == null ? null : map.get(resourceId);
		if(list != null) {
			for(HrmReport rBean : list) {
				fromDate = rBean.getFromDate();
				fromTime = rBean.getFromTime();
				toDate = rBean.getToDate();
				toTime = rBean.getToTime();
				if(!(fromDate.compareTo(signDate) <= 0 && toDate.compareTo(signDate) >= 0)) continue;
				
				if (fromDate.compareTo(signDate) < 0) {
					fromDate = signDate;
					fromTime = workTime[0];
				} else if(fromTime.compareTo(workTime[1]) > 0) {
					continue;
				} else if(fromTime.compareTo(workTime[0]) < 0) {
					fromTime = workTime[0];
				}
				if (toDate.compareTo(signDate) > 0) {
					toDate = signDate;
					toTime = workTime[1];
				} else if(toTime.compareTo(workTime[0]) <= 0) {
					continue;
				} else if(toTime.compareTo(workTime[1]) > 0) {
					toTime = workTime[1];
				}
				time += totalTime(fromDate, fromTime, toDate, toTime);
			}
		}
		return time;
	}
	
	protected Map<String, String> getFlowTime(String resourceId, String signDate, String[] workTime) {
		Map<String, String> map = getFlowTime(resourceId, signDate, workTime, leaveMap,true);//目前只有请假流程是支持考勤应用设置 半天，全天单位的 
		map.putAll(getFlowTime(resourceId, signDate, workTime, evectionMap));
		map.putAll(getFlowTime(resourceId, signDate, workTime, outMap));
		map.putAll(getFlowTime(resourceId, signDate, workTime, otherMap));
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private String[] toArray(Map<String, String> map) {
		String[] dates = new String[map == null ? 0 : map.size()];
		if(map != null) {
			Iterator it = map.entrySet().iterator();
			Map.Entry<String, String> entry = null;
			int index = 0;
			while(it.hasNext()) {
				entry = (Map.Entry<String, String>)it.next();
				dates[index++] = entry.getValue().split(" ")[1];
			}
		}
		return dates;
	}

	private Map<String, String> getFlowTime(String resourceId, String signDate, String[] workTime, Map<String, List<HrmReport>> map) {
		return getFlowTime(resourceId,signDate,workTime,map,false);
	}
	/**
	 * 
	 * @param resourceId 人员id
	 * @param signDate 签到日期
	 * @param workTime 当天的一般工作时间 worktime[0]表示上午上班时间 worktime[1]表示下午下班时间
	 * @param map 请假、出差、公出、其他 四种抵扣流程集合
	 * @param useScheduleApp 是否开启 考勤应用设置（半天、全天考勤单位）
	 * @return
	 */
	private Map<String, String> getFlowTime(String resourceId, String signDate, String[] workTime, Map<String, List<HrmReport>> map,boolean useScheduleApp) {
		String fromDate = "", fromTime = "", toDate = "", toTime = "", nextDate = "", lastDate = "";
		Map<String, String> dMap = new LinkedHashMap<String, String>();
		List<HrmReport> list = map == null ? null : map.get(resourceId);

		HrmSchedule hrmSchedule = new HrmSchedule();
		int index = StringUtil.parseToInt(indexMap.get(resourceId), -1);
		String unitType = "";
		
		
		if(list != null) {
			boolean isAcrossTime = workTime != null && workTime.length == 2 && workTime[0].compareTo(workTime[1]) > 0 && !workTime[1].equals("00:00");
			for(HrmReport rBean : list) {
				fromDate = rBean.getFromDate();
				fromTime = rBean.getFromTime();
				toDate = rBean.getToDate();
				toTime = rBean.getToTime();
				nextDate = DateUtil.addDate(signDate, 1);
				lastDate = DateUtil.addDate(signDate, -1);
				
				//如果开启了考勤应用设置的单位
				if(useScheduleApp){
					if(this.scheduleUnit == 1){//如果是半天为单位
						
						if (index >= 0) { //
							Map<String, Object> scheduleMap = personList.get(index);
							if(((HrmScheduleDate)((Map)scheduleMap.get("dateMap")).get(signDate)) != null) {
								hrmSchedule = ((HrmScheduleDate)((Map)scheduleMap.get("dateMap")).get(signDate)).getSBean();
								if(hrmSchedule != null && !hrmSchedule.isEmpty()){
									unitType = getUnitType(hrmSchedule,fromTime,toTime).toLowerCase();
									if("am".equalsIgnoreCase(unitType)){
										fromTime = hrmSchedule.getOnDutyTimeAM();
										toTime = hrmSchedule.getOffDutyTimeAM();
									}else if("pm".equalsIgnoreCase(unitType)){
										fromTime = hrmSchedule.getOnDutyTimePM();
										toTime = hrmSchedule.getOffDutyTimePM();
									}else if("all".equalsIgnoreCase(unitType)){
										fromTime = hrmSchedule.getOnDutyTimeAM();
										toTime = hrmSchedule.getOffDutyTimePM();
									}
								}
							}
						}
					}else if(this.scheduleUnit == 2){//如果是全天为单位,则只要是请假，开始和结束时间都是包含全天的
						fromTime = workTime[0];
						toTime = workTime[1];
					}
				}
				if(isAcrossTime) {
					if((fromDate.compareTo(signDate) > 0 && (!fromDate.equals(nextDate) || (fromDate.equals(nextDate) && fromTime.compareTo(workTime[1]) > 0))) || toDate.compareTo(lastDate) < 0) {
						continue;
					}
				} else if(fromDate.compareTo(signDate) > 0 || toDate.compareTo(signDate) < 0) {
					continue;
				}
				if (fromDate.compareTo(signDate) < 0) {
					if(isAcrossTime) {
						if(fromDate.compareTo(lastDate) < 0) {
							fromDate = signDate;
							fromTime = workTime[0];
						} else if(fromDate.equals(lastDate)) {
							if(fromTime.compareTo(workTime[0]) < 0 && fromTime.compareTo(workTime[1]) > 0) {
								fromTime = workTime[0];
							}
						}
					} else {
						fromDate = signDate;
						fromTime = workTime[0];
					}
				} else if(fromTime.compareTo(workTime[1]) > 0 && workTime[0].compareTo(workTime[1]) < 0) {
					continue;
				} else if(fromTime.compareTo(workTime[0]) < 0) {
					if(isAcrossTime) {
						if(fromTime.compareTo(workTime[1]) > 0) {
							fromTime = workTime[0];
						}
					} else {
						fromTime = workTime[0];
					}
				}
				if (toDate.compareTo(signDate) > 0) {
					if(isAcrossTime) {
						if(toDate.compareTo(nextDate) > 0) {
							toDate = nextDate;
							toTime = workTime[1];
						} else if(toTime.compareTo(workTime[0]) < 0 && toTime.compareTo(workTime[1]) > 0){
							toTime = workTime[1];
						}
					} else {
						toDate = signDate;
						toTime = workTime[1];
					}
				} else if(toTime.compareTo(workTime[0]) <= 0) {
					if(isAcrossTime) {
						if(toTime.compareTo(workTime[1]) > 0 && toTime.compareTo(workTime[0]) <= 0) {
							toTime = workTime[1];
						}
					} else {
						continue;
					}
				} else if(toTime.compareTo(workTime[1]) > 0) {
					if(!(isAcrossTime)) {
						toTime = workTime[1];
					}
				}
				String[] dates = DateUtil.getDateMinutes(fromDate + " " + fromTime, toDate + " " + toTime);
				for(String d : dates) dMap.put(d, d);
			}
		}
		return dMap;
	}
	
	/**
	 *  获取当前考勤应用设置的 单位（半天、全天）
	 */
	private int getUnit(){

		HrmScheduleApplicationManager scheduleApplicationManager = new HrmScheduleApplicationManager();
		HrmScheduleApplication scheduleApplication = new HrmScheduleApplication();
		scheduleApplication = (HrmScheduleApplication)scheduleApplicationManager.get(scheduleApplicationManager.getMapParam("type:0"));
		scheduleApplication = scheduleApplication == null ? new HrmScheduleApplication() : scheduleApplication;
		
		return scheduleApplication.getUnit();
	}
	


	/**
	 * 根据传入的开始时间和结束时间，判断请假时段是上午，下午还是全天
	 * @param bean
	 * @param curDate
	 * @param fromTime
	 * @param toTime
	 * @return am(上午),pm(下午),all(全天)
	 */
	private String getUnitType(HrmSchedule bean, String fromTime, String toTime){

		String leaveAt = ""; //请假时间判断 am 是上午 pm是下午 allday是全天
		String onAM = bean.getOnDutyTimeAM();
		String offAM = bean.getOffDutyTimeAM();
		String onPM = bean.getOnDutyTimePM();
		String offPM = bean.getOffDutyTimePM();
		
		String type = ""; 
		
		//请假开始时间在上午上班之前
		if(fromTime.compareTo(onAM) <= 0) {
			if(toTime.compareTo(onAM) <= 0){
				//结束时间也在上午上班时间之前，不需要考虑
			}else if(toTime.compareTo(onAM) > 0 && toTime.compareTo(onPM) < 0){
				//结束时间在上午上班时间之后，下午上班之前
				type = "am";
			}else if(toTime.compareTo(onPM) >= 0){
				//结束时间在下午上班时间之后
				type = "all";
			}
		}else if(fromTime.compareTo(onAM) > 0 && fromTime.compareTo(offAM) <= 0){
			//请假开始时间在上午上班之后，上午下班之前
			if(toTime.compareTo(onAM) <= 0){
				//结束时间也在上午上班时间之前，不需要考虑
			}else if(toTime.compareTo(onAM) > 0 && toTime.compareTo(onPM) < 0){
				//结束时间在上午上班时间之后，下午上班之前
				type = "am";
			}else if(toTime.compareTo(onPM) >= 0){
				//结束时间在下午上班时间之后
				type = "all";
			}
		}else if(fromTime.compareTo(offAM) > 0 && fromTime.compareTo(onPM) < 0){
			//请假开始时间在上午下班之后，下午上班之前
			if(toTime.compareTo(onAM) <= 0){
				//结束时间也在上午上班时间之前，不需要考虑
			}else if(toTime.compareTo(onAM) > 0 && toTime.compareTo(onPM) < 0){
				//结束时间在上午上班时间之后，下午上班之前，不需要考虑
			}else if(toTime.compareTo(onPM) >= 0){
				//结束时间在下午上班时间之后
				type = "pm";
			}
		}else if(fromTime.compareTo(onPM) >= 0 && fromTime.compareTo(offPM) <= 0){
			//请假开始时间在下午上班班之后，下午下班之前
			if(toTime.compareTo(onAM) <= 0){
				//结束时间也在上午上班时间之前，不需要考虑
			}else if(toTime.compareTo(onAM) > 0 && toTime.compareTo(onPM) < 0){
				//结束时间在上午上班时间之后，下午上班之前，不需要考虑
			}else if(toTime.compareTo(onPM) >= 0){
				//结束时间在下午上班时间之后
				type = "pm";
			}
		}else if(fromTime.compareTo(offPM) > 0 ){
			//请假开始时间在下午下班之后，不需要考虑
		}

    	return type;
	}

}
