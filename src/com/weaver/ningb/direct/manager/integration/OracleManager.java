package com.weaver.ningb.direct.manager.integration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.datacontract.schemas._2004._07.MH_EBSOAWcfService.Electronic;
import org.datacontract.schemas._2004._07.MH_EBSOAWcfService.UpdatePoStatusInfo;
import org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelBom_Content;
import org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelBox_Content;
import org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelItem_Content;
import org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelOAItem;
import org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelOu_Content;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tempuri.OAServiceContract;
import org.tempuri.OAServiceContractProxy;

import weaver.conn.RecordSet;
import weaver.general.Util;

import com.weaver.ningb.core.Environment;
import com.weaver.ningb.core.util.FormModeUtils;
import com.weaver.ningb.direct.entity.integration.OracleProductCode;
import com.weaver.ningb.direct.entity.integration.OracleProductOrder;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.formmode.core.FormModeManager;
import com.weaver.ningb.soa.formmode.core.FormModeManagerImpl;
import com.weaver.ningb.soa.formmode.entity.FormModeResult;

/**
 * Oracle 集成 Manager
 * 
 * @author liberal
 *
 */
public class OracleManager {
	
	private static final Log logger = LogFactory.getLog(OracleManager.class);
	
	private Map<String, Object> envMap = null;
	private List<Map<String, String>> envQuerys = null;
	private OAServiceContract proxy;
	
	private RecordSet rs = new RecordSet();
	
	
	public OracleManager() {
		try {
			this.envMap = load();
			this.proxy = new OAServiceContractProxy();
		} catch (Exception e) {
			logger.error("init Exception: ", e);
		}
	}
	
	
	/**
	 * 同步基础数据到 OA 中间表中
	 * 
	 * <p>通用接口, 支持多个 ERP 基础数据接口同步; 具体配置说明参考 <code>oracle.properties</code>
	 * 
	 * @param calendar
	 * 					同步开始时间
	 * @return boolean：true.操作成功      false.操作失败
	 */
	public boolean syncQuery(Calendar calendar) {
		boolean flag = true;
		String task = "syncQuery";
		try {
			if (this.envQuerys == null || this.envQuerys.size() <= 0) {
				logger.info( task + " envQuerys is null");
				return flag;
			}
			
			String username = Util.null2String(envMap.get("username"));
			String password = Util.null2String(envMap.get("password"));
			
			for (Map<String, String> map : this.envQuerys) {
				String name = map.get("name");				// 接口名称
				String rmiName = map.get("rminame");		// 接口调用名称
				String primaryKey = map.get("primarykey");	// 主键名称
				String rmiargbean = map.get("rmiargbean");	// 调用接口参数对象路径
				String rmiargname = map.get("rmiargname");	// 调用接口参数对象传值名称
				String modeid = map.get("modeid");			// OA 建模模块 id
				String modeCreator = map.get("modecreator");// OA 中创建同步数据人员 id
				
				logger.info(name + " request start");
				
				// 创建请求对象参数信息
				if (StringUtils.isBlank(rmiargbean) || StringUtils.isBlank(rmiargname)) continue;
				String[] rmiargnameArr = rmiargname.split(",");
				List<String> args = new ArrayList<String>();
				List<Object> argsValue = new ArrayList<Object>();
				List<Class<?>> argsValueClass = new ArrayList<Class<?>>();
				for (int i = 0; i < rmiargnameArr.length; i++) {
					String argname = rmiargnameArr[i];
					if (StringUtils.isBlank(argname)) continue;
					if ("Lastupdate_datetime".equals(argname) || "lastupdate_datetime".equals(argname)) {
//						Calendar calendar = Calendar.getInstance();
//						calendar.setTime(new Date(System.currentTimeMillis()));
//						calendar.add(Calendar.DAY_OF_YEAR, -10);
						
						args.add(argname);
						argsValue.add(calendar);
						argsValueClass.add(Calendar.class);
					}
				}
				Object info = invokeArgsByReflect(rmiargbean,
						args.toArray(new String[args.size()]),
						argsValue.toArray(new Object[argsValue.size()]),
						argsValueClass.toArray(new Class<?>[argsValueClass.size()]));
				if (info == null) {
					logger.info("invokeArgsByReflect info is null or create failure");
					continue;
				}
				
				// 请求接口
				String response = invokeByReflect(proxy, rmiName,
						new Object[]{info, username, password}, new Class[]{info.getClass(), String.class, String.class});
				// 判断请求错误, 目前请求错误后返回对应的错误字符串
				if (StringUtils.isBlank(response) || response.indexOf("<?xml version") == -1) {
					logger.info(name + " request Failure: " + response);
					return flag;
				}
				
				// 解析响应信息
				List<Map<String, String>> responseList = parseReponse(response,
						new OracleFilter() {

					@Override
					public boolean accepts(Map<String, String> query,
							String name, String value) {
						boolean flag = true;
						if (query == null || query.size() <= 0) return flag;
						String rmifiltername = query.get("rmifiltername");	// 调用接口过滤掉指定值的字段名称
						String rmifiltervalue = query.get("rmifiltervalue");// 调用接口过滤掉指定值的字段值
						if (StringUtils.isBlank(rmifiltername) || StringUtils.isBlank(rmifiltervalue)) return flag;
						
						String[] rmifilternameArr = rmifiltername.split(",");
						String[] rmifiltervalueArr = rmifiltervalue.split(",");
						if (rmifilternameArr.length != rmifiltervalueArr.length) return flag;
						
						// 如果接口返回字段与指定字段相等, 且值相等, 则不需要同步到 OA
						for (int i = 0; i < rmifilternameArr.length; i++) {
							String filtername = rmifilternameArr[i];
							if (StringUtils.isBlank(filtername)) continue;
							if (name.equals(filtername)) {
								if (value.equals(Util.null2String(rmifiltervalueArr[i]))) {
									flag = false;
									break;
								}
							}
						}
						return flag;
					}
					
				}, map);
				if (responseList == null || responseList.size() <= 0) {
					logger.info(name + " responseList is null");
					continue;
				}
				
				// 保存数据到建模中
				// 考虑到建模数据会被流程中数据进行引用, 因此对于存在的数据只进行更新不进行删除操作
				String right = "Y";
				FormModeManager formModeManager = new FormModeManagerImpl();
				for (Map<String, String> mainMap : responseList) {
					String id = query(modeid + "", primaryKey, mainMap);
					if (StringUtils.isBlank(id)) {
						FormModeResult<Integer> result = formModeManager.save(modeid, modeCreator, right, mainMap);
						if (result == null || !"0".equals(result.getCode())) {
							if (result == null) {
								logger.info(name + " save Failure");
							} else {
								logger.info(name + " save Exception: " + result.getMessage());
							}
						}
					} else {
						// 更新数据更新时间
						Calendar execCalendar = Calendar.getInstance();
						String execDate = Util.add0(execCalendar.get(Calendar.YEAR), 4) + "-" + Util.add0(execCalendar.get(Calendar.MONTH) + 1, 2) + "-" + Util.add0(execCalendar.get(Calendar.DAY_OF_MONTH), 2);
				        String execTime = Util.add0(execCalendar.getTime().getHours(), 2) + ":" + Util.add0(execCalendar.getTime().getMinutes(), 2) + ":" + Util.add0(execCalendar.getTime().getSeconds(), 2);
				        mainMap.put("modedatacreatedate", execDate);
						mainMap.put("modedatacreatetime", execTime);
				        
						FormModeResult<Integer> result = formModeManager.update(modeid, id, modeCreator, right, mainMap);
						if (result == null || !"0".equals(result.getCode())) {
							if (result == null) {
								logger.info(name + " update Failure");
							} else {
								logger.info(name + " update Exception: " + result.getMessage());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(task + " Exception: ", e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 推送单据状态到 Oracle
	 * 
	 * @param contractNumber
	 * 					采购合同号
	 * @param status
	 * 					状态：BACK.撤回采购合同   REJECT.拒绝   APPROVED.通过   WF_OK_BUTTON.有效
	 * @return 推送结果
	 */
	public OracleResult<String, String> pushBill(String contractNumber, String status) {
		String task = "pushBill";
		String code = "0";
		String message = "成功";
		String request = null;
		String response = null;
		try {
			String username = Util.null2String(envMap.get("username"));
			String password = Util.null2String(envMap.get("password"));
			
			request = contractNumber;
			
			UpdatePoStatusInfo statusInfo = new UpdatePoStatusInfo();
			statusInfo.setPoHeaderNO(contractNumber);
			statusInfo.setPoStatus(status);
			String msg = proxy.updatePoStatus(statusInfo, username, password);
			if (!StringUtils.isBlank(msg)) {
				code = "-3";
				message = msg;
				response = msg;
			}
		} catch (RemoteException e) {
			logger.error(task + " Failure: ", e);
			code = "-2";
			message = "RemoteException.";
		} catch (Exception e) {
			logger.error(task + " Failure: ", e);
			code = "-1";
			message = "Failure.";
		}
		return callback(null, task, code, message, request, response);
	}
	
	/**
	 * 推送货号单据给 Oracle
	 * 
	 * @param list
	 * 					货号信息
	 * @return 推送结果
	 */
	public OracleResult<String, String> pushProductCode(List<OracleProductCode> list) {
		String task = "pushProductCode";
		String code = "0";
		String message = "成功";
		String request = null;
		String response = null;
		try {
			String username = Util.null2String(envMap.get("username"));
			String password = Util.null2String(envMap.get("password"));
			
			request = createRequestJson(list);
			logger.info("request: " + request);
			if (StringUtils.isBlank(request)) {
				code = "-3";
				message = "创建请求信息失败";
				return callback(null, task, code, message, request, response);
			}
			
			String oaItemJson = createRequestJson(list);
			response = proxy.importOAItem(oaItemJson, username, password);
			logger.info("response: " + response);
			if (!isNumeric(response)) {
				code = "-4";
				message = response;
			}
		} catch (RemoteException e) {
			logger.error(task + " Failure: ", e);
			code = "-2";
			message = "RemoteException.";
		} catch (Exception e) {
			logger.error(task + " Failure: ", e);
			code = "-1";
			message = "Failure.";
		}
		return callback(null, task, code, message, request, response);
	}
	
	/**
	 * 推送季节属性节日属性给 Oracle
	 * 
	 * @param list
	 * 					货号信息
	 * @return 推送结果
	 * @author ycj
	 */
	public OracleResult<String, String> pushProductCode1(List<OracleProductCode> list) {
		String task = "pushProductCode1";
		String code = "0";
		String message = "成功";
		String request = null;
		String response = null;
		try {
			String username = Util.null2String(envMap.get("username"));
			String password = Util.null2String(envMap.get("password"));
			
			request = createRequestJson1(list);
			logger.info("request: " + request);
			if (StringUtils.isBlank(request)) {
				code = "-3";
				message = "创建请求信息失败";
				return callback(null, task, code, message, request, response);
			}
			
			String oaItemJson = createRequestJson1(list);
			response = proxy.importOAORGItem(oaItemJson, username, password);
			logger.info("response: " + response);
			if (!isNumeric(response)) {
				code = "-4";
				message = response;
			}
		} catch (RemoteException e) {
			logger.error(task + " Failure: ", e);
			code = "-2";
			message = "RemoteException.";
		} catch (Exception e) {
			logger.error(task + " Failure: ", e);
			code = "-1";
			message = "Failure.";
		}
		return callback(null, task, code, message, request, response);
	}
	
	/**
	 * 推送报价单给 Oracle
	 * 
	 * @param list
	 * 					货号信息
	 * @return 推送结果
	 * @author ycj@20180814
	 */
	public OracleResult<String, String> pushProductOrder(List<OracleProductOrder> list) {
		String task = "pushProductOrder";
		String code = "0";
		String message = "成功";
		String request = null;
		String response = null;
		try {
			String username = Util.null2String(envMap.get("username"));
			String password = Util.null2String(envMap.get("password"));
			
			request = createRequestJson2(list);
			logger.info("request: " + request);
			if (StringUtils.isBlank(request)) {
				code = "-3";
				message = "创建请求信息失败";
				return callback(null, task, code, message, request, response);
			}
			
			String oaItemJson = createRequestJson2(list);
			response = proxy.importOAQuote(oaItemJson, username, password);
			logger.info("response: " + response);
			if (response.length()>20) {
				code = "-4";
				message = response;
			}
		} catch (RemoteException e) {
			logger.error(task + " Failure: ", e);
			code = "-2";
			message = "RemoteException.";
		} catch (Exception e) {
			logger.error(task + " Failure: ", e);
			code = "-1";
			message = "Failure.";
		}
		return callback(null, task, code, message, request, response);
	}
	
	/**
	 * 更新 Oracle报价单
	 * 
	 * @param list
	 * 					货号信息
	 * @return 推送结果
	 * @author ycj@20181231
	 */
	public OracleResult<String, String> updateProductOrder(List<OracleProductOrder> list) {
		String task = "updateProductOrder";
		String code = "0";
		String message = "成功";
		String request = null;
		String response = null;
		try {
			String username = Util.null2String(envMap.get("username"));
			String password = Util.null2String(envMap.get("password"));
			
			request = createRequestJson2(list);
			logger.info("request: " + request);
			if (StringUtils.isBlank(request)) {
				code = "-3";
				message = "创建请求信息失败";
				return callback(null, task, code, message, request, response);
			}
			
			String oaItemJson = createRequestJson2(list);
			response = proxy.updateOAQuote(oaItemJson, username, password);
			logger.info("response: " + response);
			if (!"S".equals(response)) {
				code = "-4";
				message = response;
			}
		} catch (RemoteException e) {
			logger.error(task + " Failure: ", e);
			code = "-2";
			message = "RemoteException.";
		} catch (Exception e) {
			logger.error(task + " Failure: ", e);
			code = "-1";
			message = "Failure.";
		}
		return callback(null, task, code, message, request, response);
	}
	
	/**
	 * 退税通流程更新行信息
	 * 
	 * @param list
	 * 					报关单号和分提单号
	 * @return 推送结果
	 * @author ycj@20181129
	 */
	public OracleResult<String, String> pushElectronicLineStates(List<Map<String, String>> list) {
		String task = "pushElectronicLineStates";
		String code = "0";
		String message = "成功";
		String request = null;
		String response = null;
		try {
			String username = Util.null2String(envMap.get("username"));
			String password = Util.null2String(envMap.get("password"));
			
			request = list.toString();
			
			Electronic[] electronicLineStates = electronicToArray(list);
			response = proxy.updateElectronicLineStates(electronicLineStates, username, password);
			logger.info("response: " + response);
			if (response.indexOf("False")!=-1) {
				code = "-4";
				message = response;
			}
		} catch (RemoteException e) {
			logger.error(task + " Failure: ", e);
			code = "-2";
			message = "RemoteException.";
		} catch (Exception e) {
			logger.error(task + " Failure: ", e);
			code = "-1";
			message = "Failure.";
		}
		return callback(null, task, code, message, request, response);
	}
	
	/**
	 * 退税通流程更新临时表信息
	 * 
	 * @param list
	 * 					报关单号和分提单号
	 * @return 推送结果
	 * @author ycj@20181129
	 */
	public OracleResult<String, String> pushElectronicIsCreateSo(List<Map<String, String>> list) {
		String task = "pushElectronicIsCreateSo";
		String code = "0";
		String message = "成功";
		String request = null;
		String response = null;
		try {
			String username = Util.null2String(envMap.get("username"));
			String password = Util.null2String(envMap.get("password"));
			
			request = list.toString();
			
			Electronic[] electronicIsCreateSo = electronicToArray(list);
			response = proxy.updateElectronicIsCreateSo(electronicIsCreateSo, username, password);
			logger.info("response: " + response);
			if (response.indexOf("False")!=-1) {
				code = "-4";
				message = response;
			}
		} catch (RemoteException e) {
			logger.error(task + " Failure: ", e);
			code = "-2";
			message = "RemoteException.";
		} catch (Exception e) {
			logger.error(task + " Failure: ", e);
			code = "-1";
			message = "Failure.";
		}
		return callback(null, task, code, message, request, response);
	}
	
	
	/**
	 * 通过反射创建对象, 并设置对应的字段值
	 * 
	 * @param className
	 * 					调用对象完整路径
	 * @param args
	 * 					调用对象方法
	 * @param argsValue
	 * 					调用对象方法值
	 * @param argsValueClass
	 * 					调用对象方法值类型
	 * @return 调用返回信息
	 */
	@SuppressWarnings("unchecked")
	private <T> T invokeArgsByReflect(String className, String[] args, 
			Object[] argsValue, Class<?>[] argsValueClass) {
		T result = null;
		String task = "invokeArgs";
		try {
			if (StringUtils.isBlank(className)) return result;
			
			Class<?> clazz = Class.forName(className);
			Object clazzObject = clazz.newInstance();
			
			// 设置对象参数
			if (args != null && args.length > 0
					&& argsValue != null && argsValue.length > 0
					&& args.length == argsValue.length) {
				for (int i = 0; i < args.length; i++) {
					String arg = args[i];
					Object argValue = argsValue[i];
					Class<?> argValueClass = argsValueClass[i];
					
					arg = "set" + toUpperCaseFirstOne(arg);	// 获取 set 方法
					
					Method method = clazz.getMethod(arg, argValueClass);
					method.invoke(clazzObject, argValue);
				}
			}
			if (clazzObject != null) result = (T) clazzObject;
		} catch (ClassNotFoundException e) {
			logger.error(task + " ClassNotFoundException:", e);
		} catch (NoSuchMethodException e) {
			logger.error(task + " NoSuchMethodException:", e);
		} catch (SecurityException e) {
			logger.error(task + " SecurityException:", e);
		} catch (IllegalAccessException e) {
			logger.error(task + " IllegalAccessException:", e);
		} catch (IllegalArgumentException e) {
			logger.error(task + " IllegalArgumentException:", e);
		} catch (InvocationTargetException e) {
			logger.error(task + " InvocationTargetException:", e);
		} catch (Exception e) {
			logger.error(task + " Exception:", e);
		}
		return result;
	}
	
	/**
	 * 通过反射调用方法
	 * 
	 * @param clazzBean
	 * 					调用对象实例
	 * @param methodName
	 * 					调用方法
	 * @param argsValue
	 * 					传入参数
	 * @param argsValueClass
	 * 					传入参数类型
	 * @return 调用返回信息
	 */
	private <T> String invokeByReflect(T clazzBean, String methodName,
			Object[] argsValue, Class<?>[] argsValueClass) {
		String result = null;
		String task = "invokeByReflect";
		try {
			Method method = clazzBean.getClass().getMethod(methodName, argsValueClass);
			Object obj = method.invoke(clazzBean, argsValue);
			result = obj == null ? null : obj + "";
		} catch (NoSuchMethodException e) {
			logger.error(task + " NoSuchMethodException:", e);
		} catch (SecurityException e) {
			logger.error(task + " SecurityException:", e);
		} catch (IllegalAccessException e) {
			logger.error(task + " IllegalAccessException:", e);
		} catch (IllegalArgumentException e) {
			logger.error(task + " IllegalArgumentException:", e);
		} catch (InvocationTargetException e) {
			logger.error(task + " InvocationTargetException:", e);
		} catch (Exception e) {
			logger.error(task + " Exception: ", e);
		}
		return result;
	}
	
	/**
	 * 创建请求信息
	 * 
	 * @param list
	 * 					货号信息
	 * @return 请求信息
	 */
	private String createRequestJson(List<OracleProductCode> list) {
		String result = null;
		try {
			JSONObject resultJsonObject = new JSONObject();
			
			if (list == null || list.size() <= 0) return result;
			
			JSONArray itemContentArray = new JSONArray();
			for (OracleProductCode bean : list) {
				JSONObject itemContentObject = createRequestJsonMap(bean.getItemContentMap());
				if (itemContentObject == null) continue;
				
				JSONArray boxContentArray = createRequestJsonList(bean.getBoxContentList());
				if (boxContentArray != null) itemContentObject.put("box_content", boxContentArray);
				
				JSONArray bomContentArray = createRequestJsonList(bean.getBomContentList());
				if (bomContentArray != null) itemContentObject.put("bom_content", bomContentArray);
				
				JSONArray ouContentArray = createRequestJsonList(bean.getOuContentList());
				if (ouContentArray != null) itemContentObject.put("ou_content", ouContentArray);
				
				itemContentArray.add(itemContentObject);
			}
			
			resultJsonObject.put("item_content", itemContentArray);
			
			result = resultJsonObject.toString();
		} catch (Exception e) {
			logger.error("createRequestJson Failure: ", e);
		}
		return result;
	}
	
	/**
	 * 创建请求信息
	 * 
	 * @param list
	 * 					季节属性节日属性
	 * @return 请求信息
	 * @author ycj
	 */
	private String createRequestJson1(List<OracleProductCode> list) {
		String result = null;
		try {
			JSONObject resultJsonObject = new JSONObject();
			
			if (list == null || list.size() <= 0) return result;
			
			JSONArray itemContentArray = new JSONArray();
			for (OracleProductCode bean : list) {
				JSONObject itemContentObject = createRequestJsonMap(bean.getItemContentMap());
				if (itemContentObject == null) continue;
				
				JSONArray ouContentArray = createRequestJsonList(bean.getOuContentList());
				if (ouContentArray != null) itemContentObject.put("ou_content", ouContentArray);
				
				itemContentArray.add(itemContentObject);
			}
			
			resultJsonObject.put("item_content", itemContentArray);
			
			result = resultJsonObject.toString();
		} catch (Exception e) {
			logger.error("createRequestJson1 Failure: ", e);
		}
		return result;
	}
	
	/**
	 * 创建请求信息
	 * 
	 * @param list
	 * 					报价单信息
	 * @return 请求信息
	 * @author ycj
	 */
	private String createRequestJson2(List<OracleProductOrder> list) {
		String result = null;
		try {
			JSONObject resultJsonObject = new JSONObject();
			
			if (list == null || list.size() <= 0) return result;
			
			JSONArray headContentArray = new JSONArray();
			for (OracleProductOrder bean : list) {
				JSONObject headContentObject = createRequestJsonMap(bean.getHeadContentMap());
				if (headContentObject == null) continue;
				
				JSONArray detailContentArray = createRequestJsonList(bean.getDetailContentList());
				if (detailContentArray != null) headContentObject.put("detail_content", detailContentArray);
				
				headContentArray.add(headContentObject);
			}
			
			resultJsonObject.put("head_content", headContentArray);
			
			result = resultJsonObject.toString();
		} catch (Exception e) {
			logger.error("createRequestJson2 Failure: ", e);
		}
		return result;
	}
	
	/**
	 * 创建请求数组
	 * 
	 * @param map
	 * 					请求信息
	 * @return JSON 数组
	 */
	private JSONArray createRequestJsonList(List<Map<String, String>> list) {
		JSONArray resultJsonArray = null;
		try {
			resultJsonArray = new JSONArray();
			
			if (list == null || list.size() <= 0) return resultJsonArray;
			
			for (Map<String, String> map : list) {
				JSONObject resultJsonObject = createRequestJsonMap(map);
				if (resultJsonObject != null) resultJsonArray.add(resultJsonObject);
			}
		} catch (Exception e) {
			logger.error("createRequestJsonList Failure: ", e);
		}
		return resultJsonArray;
	}
	
	/**
	 * 创建请求对象
	 * 
	 * @param map
	 * 					请求信息
	 * @return JSON 对象
	 */
	private JSONObject createRequestJsonMap(Map<String, String> map) {
		JSONObject resultJsonObject = null;
		try {
			resultJsonObject = new JSONObject();
			
			if (map == null || map.size() <= 0) return resultJsonObject;
			
			Set<String> doubleSet = new HashSet<String>();
			doubleSet.add("unit_price");
			doubleSet.add("delivery_num");
			doubleSet.add("oa_item_line_seq");
			doubleSet.add("box_number");
			doubleSet.add("box_quantity");
			doubleSet.add("box_length");
			doubleSet.add("box_width");
			doubleSet.add("box_height");
			doubleSet.add("box_volume");
			doubleSet.add("box_net_weight");
			doubleSet.add("box_gross_weight");
			doubleSet.add("box_outsourcing_box_qty");
			doubleSet.add("bom_line_num");
			doubleSet.add("bom_quantity");
			doubleSet.add("bom_cost_percentage");
			doubleSet.add("bom_net_weight");
			doubleSet.add("bom_gross_weight");
			doubleSet.add("bom_volume");
			doubleSet.add("bom_outer_box_percentage");
			for (String key : map.keySet()) {
				String value = Util.null2String(map.get(key));
				if (!StringUtils.isBlank(value)) {
					value = value.replaceAll("&nbsp;", " ");
					value = value.replaceAll("\r", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("<br>", " ");
					value = value.replaceAll("&quot;", "\"");
					value = value.replaceAll("'", "''");
				}
				if(!StringUtils.isBlank(value) && doubleSet.contains(key)){
					resultJsonObject.put(key, Double.parseDouble(value));
				}else{
					resultJsonObject.put(key, value);
				}
			}
		} catch (Exception e) {
			logger.error("createRequestJsonMap Failure: ", e);
		}
		return resultJsonObject;
	}
	
	/**
	 * 创建请求信息
	 * 
	 * @param list
	 * 					货号信息
	 * @return 请求信息
	 */
	private OAItemModelOAItem createRequest(List<OracleProductCode> list) {
		OAItemModelOAItem result = null;
		try {
			result = new OAItemModelOAItem();
			
			if (list == null || list.size() <= 0) return result;
			
			List<OAItemModelItem_Content> itemContents = new ArrayList<OAItemModelItem_Content>(list.size());
			for (OracleProductCode bean : list) {
				OAItemModelItem_Content itemContent = createObject(bean.getItemContentMap(), OAItemModelItem_Content.class);
				if (itemContent == null) continue;
				
				List<OAItemModelBox_Content> boxContentArray = createList(bean.getBoxContentList(), OAItemModelBox_Content.class);
				if (boxContentArray != null && boxContentArray.size() > 0) {
					itemContent.setBox_content(boxContentArray.toArray(new OAItemModelBox_Content[boxContentArray.size()]));
				}
				
				List<OAItemModelBom_Content> bomContentArray = createList(bean.getBomContentList(), OAItemModelBom_Content.class);
				if (bomContentArray != null && bomContentArray.size() > 0) {
					itemContent.setBom_content(bomContentArray.toArray(new OAItemModelBom_Content[bomContentArray.size()]));
				}
				
				List<OAItemModelOu_Content> ouContentArray = createList(bean.getOuContentList(), OAItemModelOu_Content.class);
				if (ouContentArray != null && ouContentArray.size() > 0) {
					itemContent.setOu_content(ouContentArray.toArray(new OAItemModelOu_Content[ouContentArray.size()]));
				}
				
				itemContents.add(itemContent);
			}
			
			if (itemContents != null && itemContents.size() > 0) 
				result.setItem_content(itemContents.toArray(new OAItemModelItem_Content[itemContents.size()]));
		} catch (Exception e) {
			logger.error("createRequest Failure: ", e);
		}
		return result;
	}
	
	/**
	 * 将 Map 数据集合转换为对象数据集合
	 * 
	 * @param list
	 * 					Map 数据集合
	 * @param bean
	 * 					转换对象类型
	 * @return 对象数据集合
	 */
	public <T> List<T> createList(List<Map<String, String>> list, Class<T> bean) {
		if (list == null || list.size() <= 0) return null;
		List<T> result = new ArrayList<T>(list.size());
		for (Map<String, String> map : list) {
			T resultBean = createObject(map, bean);
			if (resultBean != null) result.add(resultBean);
		}
		return result;
	}

	/**
	 * 将 Map 数据转换为对象数据
	 * 
	 * @param data
	 * 					Map 数据
	 * @param bean
	 * 					对象数据
	 * @return 对象数据
	 */
	public <T> T createObject(Map<String, String> data, Class<T> bean) {
		if (data == null || data.size() <= 0) {
			return null;
		} else {
			return packageObject(data, bean);
		}
	}
	
	/**
	 * 创建指定类型对象实例
	 * 
	 * @param data
	 * 					数据信息
	 * @param bean
	 * 					对象 class
	 * @return 对象实例
	 */
	public <T> T packageObject(Map<String, String> data, Class<T> bean) {
		T result = null;
		String task = "packageObject";
		try {
			result = bean.newInstance();
			BeanWrapper beanWrapper = new BeanWrapper(result);
			Map<String, PropertyDescriptor> propertyDescriptorMap = beanWrapper.getPropertyDescriptorMap();
			Set<String> propertyNames = propertyDescriptorMap.keySet();
			for (String propertyName : propertyNames) {
				for (String key : data.keySet()) {
					if (propertyName.toUpperCase().equals(key.toUpperCase())) {
						PropertyDescriptor propertyDescriptor = propertyDescriptorMap.get(propertyName);
						if (propertyDescriptor == null) break;
						Method writeMethod = propertyDescriptor.getWriteMethod();
						Class<?> requiredType = propertyDescriptor.getPropertyType();
						String tempData = data.get(key);				// 值信息
						if (!StringUtils.isBlank(tempData)) {
							tempData = tempData.replaceAll("&nbsp;", " ");
							tempData = tempData.replaceAll("\r", " ");
							tempData = tempData.replaceAll("\n", " ");
							tempData = tempData.replaceAll("<br>", " ");
							tempData = tempData.replaceAll("&quot;", "\"");
							tempData = tempData.replaceAll("'", "''");
						}
						// Explicitly extract typed value, as far as possible.
						if (String.class.equals(requiredType)) {
							writeMethod.invoke(result, tempData);
						} else if (boolean.class.equals(requiredType) || Boolean.class.equals(requiredType)) {
							writeMethod.invoke(result, Boolean.parseBoolean(tempData));
						} else if (byte.class.equals(requiredType) || Byte.class.equals(requiredType)) {
							writeMethod.invoke(result, Byte.parseByte(tempData));
						} else if (short.class.equals(requiredType) || Short.class.equals(requiredType)) {
							writeMethod.invoke(result, Short.parseShort(tempData));
						} else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
							String temp = tempData;
							if (StringUtils.isBlank(temp)) continue;
							Integer tempInteger = null;
							if (temp.indexOf(".") != -1) {
								Double tempDouble = Double.parseDouble(temp);
								tempInteger = tempDouble.intValue();
							} else {
								tempInteger = Integer.parseInt(temp);
							}
							writeMethod.invoke(result, tempInteger);
						} else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
							String temp = tempData;
							if (StringUtils.isBlank(temp)) continue;
							writeMethod.invoke(result, Long.parseLong(tempData));
						} else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
							String temp = tempData;
							if (StringUtils.isBlank(temp)) continue;
							writeMethod.invoke(result, Float.parseFloat(tempData));
						} else if (double.class.equals(requiredType) || Double.class.equals(requiredType)
								|| Number.class.equals(requiredType)) {
							String temp = tempData;
							if (StringUtils.isBlank(temp)) continue;
							writeMethod.invoke(result, Double.parseDouble(tempData));
						} else if (BigDecimal.class.equals(requiredType)) {
							String temp = tempData;
							if (StringUtils.isBlank(temp)) continue;
							writeMethod.invoke(result, BigDecimal.valueOf(Double.parseDouble(tempData)));
						} else {
							writeMethod.invoke(result, tempData);
						}
						break;
					}
				}
			}
		} catch (IllegalAccessException e) {
			logger.error(task + " IllegalAccessException: ", e);
		} catch (IllegalArgumentException e) {
			logger.error(task + " IllegalArgumentException: ", e);
		} catch (InvocationTargetException e) {
			logger.error(task + " InvocationTargetException: ", e);
		} catch (Exception e) {
			logger.error(task + " Exception: ", e);
		}
		return result;
	}
	
	/**
	 * 匹配是否包含数字
	 * 
	 * @param str
	 * 					可能为中文, 也可能是-19162431.1254, 不使用BigDecimal的话, 变成-1.91624311254E7
	 * @return boolean：true.是      false.否
	 */
	private boolean isNumeric(String str) {
		// 该正则表达式可以匹配所有的数字 包括负数
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		String bigStr;
		try {
			bigStr = new BigDecimal(str).toString();
		} catch (Exception e) {
			return false;	// 异常, 说明包含非数字
		}
		
		Matcher isNum = pattern.matcher(bigStr);	//	matcher是全匹配
		if (!isNum.matches()) return false;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String, String>> parseReponse(String response, OracleFilter filter, 
			Map<String, String> query) {
		List<Map<String, String>> result = null;
		String task = "parseReponse";
		try {
			if (StringUtils.isBlank(response)) return result;
			
			// TODO XML 特殊字符转义
			response = response.replaceAll("&", "&amp;");
			Document root = DocumentHelper.parseText(response);
			
			// <Lists></Lists>
			Element productListsElement = root.getRootElement();
			if (productListsElement == null) return result;
			
			// <List></List>, 可能存在多个
			List<Element> productListElementList = productListsElement.elements();
			if (productListElementList == null || productListElementList.size() <= 0) return result;
			
			result = new ArrayList<Map<String, String>>();
			for (Element productListElement : productListElementList) {
				List<Element> elementList = productListElement.elements();
				if (elementList == null || elementList.size() <= 0) continue;
				
				// 解析单行记录具体字段信息
				Map<String, String> map = new HashMap<String, String>();
				for (Element element : elementList) {
					String name = element.getName();
					String value = Util.null2String(element.getText());	// TODO 需要去空格?
					
					// 处理接口同步过滤指定条件不同步的问题
					boolean filterFlag = filter.accepts(query, name, value);
					if (!filterFlag) continue;
					
					map.put(name, value);
				}
				
				result.add(map);
			}
		} catch (DocumentException e) {
			logger.error(task + " response: " + response);
			logger.error(task + " DocumentException: ", e);
		} catch (Exception e) {
			logger.error(task + " response: " + response);
			logger.error(task + " Exception: ", e);
		}
		return result;
	}
	
	
	/**
	 * 查询数据是否存在
	 * 
	 * @param modeid
	 * 					模块 id
	 * @param key
	 * 					主键名称
	 * @param map
	 * 					数据信息
	 * @return 主键值：空.不存在      不为空.存在
	 */
	private String query(String modeid, String key, Map<String, String> map) {
		String id = null;
		try {
			String tablename = FormModeUtils.getTablename(modeid);
			if (StringUtils.isBlank(tablename)) return id;
			
			String sqlWhere = "";
			if (key.indexOf(",") == -1) {
				sqlWhere = "where " + key + " = '" + Util.null2String(map.get(key)) + "' ";
			} else {
				// 处理存在多主键的问题
				String[] keyArr = key.split(",");
				if (keyArr == null || keyArr.length <= 0) return id;
				for (String ka : keyArr) {
					if (StringUtils.isBlank(ka)) continue;
					if (StringUtils.isBlank(sqlWhere)) {
						sqlWhere = "where " + ka + " = '" + Util.null2String(map.get(ka)) + "' ";
					} else {
						sqlWhere += "and " + ka + " = '" + Util.null2String(map.get(ka)) + "' ";
					}
				}
			}
			
			RecordSet rs = new RecordSet();
			rs.executeQuery("select id from " + tablename + " " + sqlWhere);
			if (rs.next()) id = rs.getString("id");
		} catch (Exception e) {
			logger.error("query Exception: ", e);
		}
		return id;
	}
	
	/**
	 * 将字符串首字母转换为大写
	 * 
	 * @param str
	 * 					字符串信息
	 * @return 转换后的字符串信息
	 */
	private String toUpperCaseFirstOne(String str){
		if (StringUtils.isBlank(str)) return str;
		if (Character.isUpperCase(str.charAt(0)))
			return str;
		else
			return (new StringBuilder())
					.append(Character.toUpperCase(str.charAt(0)))
					.append(str.substring(1)).toString();
	}
	
	/**
	 * 取出字符串中的中文信息
	 * 
	 * @param str
	 * 					字符串信息
	 * @return 转换后的字符串信息
	 * @author ycj@20180816
	 */
	public String getChineseMsg(String str){
		if (StringUtils.isBlank(str)) return str;
		if(str.indexOf("`~`7")!=-1 && str.indexOf("`~`8")!=-1){
			str = str.split("`~`7")[1].split("`~`8")[0].trim(); 
		}else{
			str = str.split("`~`7")[0].split("`~`8")[0].trim();
		}
		return str;
	}
	
	
	/**
	 * 获取单位名称, 根据单位 id
	 * 
	 * @param id
	 * 					单位 id
	 * @return 单位名称
	 */
	public String getDwmc(String id) {
		String result = "";
		String task = "getDwmc";
		try {
			if (StringUtils.isBlank(id)) return result;
			rs.executeQuery("select dw from uf_DW where id = ?", id);
			if (rs.next()) result = rs.getString(1);
		} catch (Exception e) {
			logger.error(task + " Exception: ", e);
		}
		return result;
	}
	
	/**
	 * 获取颜色名称, 根据颜色 id
	 * 
	 * @param id
	 * 					颜色 id
	 * @return 单位名称
	 */
	public String getYsmc(String id) {
		String result = "";
		String task = "getYsmc";
		try {
			if (StringUtils.isBlank(id)) return result;
			rs.executeQuery("select oracleys from uf_color where id = ?", id);
			if (rs.next()) result = rs.getString(1);
		} catch (Exception e) {
			logger.error(task + " Exception: ", e);
		}
		return result;
	}
	
	/**
	 * 获取货号名称, 根据货号 id
	 * 
	 * @param id
	 * 					货号 id
	 * @return 货号名称
	 * @author ycj@20180816
	 */
	public String getHhmc(String id) {
		String result = "";
		String task = "getHhmc";
		try {
			if (StringUtils.isBlank(id)) return result;
			rs.executeQuery("select segment1 from uf_product where id = ?", id);
			if (rs.next()) result = rs.getString(1);
		} catch (Exception e) {
			logger.error(task + " Exception: ", e);
		}
		return result;
	}
	
	/**
	 * 获取人员名称, 根据人员 id
	 * 
	 * @param id
	 * 					人员 id
	 * @return 人员名称
	 * @author ycj@20180816
	 */
	public String getRymc(String id) {
		String result = "";
		String task = "getRymc";
		try {
			if (StringUtils.isBlank(id)) return result;
			rs.executeQuery("select lastname from hrmresource where id = ?", id);
			if (rs.next()) result = rs.getString(1);
		} catch (Exception e) {
			logger.error(task + " Exception: ", e);
		}
		return result;
	}
	
	/**
	 * 获取供应商名称, 根据供应商 id
	 * 
	 * @param id
	 * 					供应商 id
	 * @return 供应商名称
	 * @author ycj@20180816
	 */
	public String getGysmc(String id) {
		String result = "";
		String task = "getGysmc";
		try {
			if (StringUtils.isBlank(id)) return result;
			rs.executeQuery("select vendor_name from uf_vendor where id = ?", id);
			if (rs.next()) result = rs.getString(1);
		} catch (Exception e) {
			logger.error(task + " Exception: ", e);
		}
		return result;
	}
	
	/**
	 * 将报价单错误信息List转成数组。
	 * 
	 * @param id
	 * 					报价单错误信息List
	 * @return 报价单错误信息数组
	 * @author ycj@20181130
	 */
	public Electronic[] electronicToArray(List<Map<String, String>> list) {
		Electronic[] electronics = new Electronic[list.size()];
		for(int i=0;i<list.size();i++){
			Map<String, String> map = list.get(i);
			String CUSTOMS_NO = map.get("CUSTOMS_NO");
			String SUB_BL_NO = map.get("SUB_BL_NO");
			Electronic electronic = new Electronic();
			electronic.setCUSTOMS_NO(CUSTOMS_NO);
			electronic.setSUB_BL_NO(SUB_BL_NO);
			electronics[i] = electronic;
		}
		return electronics;
	}
	
	
	/**
	 * 加载配置文件
	 */
	private Map<String, Object> load() {
		Map<String, Object> map = new HashMap<String, Object>();
		InputStream in = null;
		try {
			String path = Environment.getInstance().getAbsolutePathToProp("oracle.properties");
//			String path = "E:\\Workspace\\ecology_aosen\\WebRoot\\ningb\\conf\\prop\\oracle.properties";
			
			Properties prop = new Properties();
			in = new BufferedInputStream(new FileInputStream(new File(path)));
			prop.load(new InputStreamReader(in, "UTF-8"));
			
			map.put("username", prop.get("oracle.username"));
			map.put("password", prop.get("oracle.password"));
			
			loadCustom(prop);
		} catch (Exception e) {
			logger.error("load Exception: ", e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error("load InputStream close: ", e);
				}
		}
		return map;
	}
	
	/**
	 * 加载调用接口自定义信息
	 * 
	 * @param prop
	 */
	private void loadCustom(Properties prop) {
		try {
			String queryName = Util.null2String(prop.get("oracle.query.name"));
			if (StringUtils.isBlank(queryName)) return;
				
			// 解析调用接口数据
			String[] queryArr = queryName.split(",");
			if (queryArr == null || queryArr.length <= 0) return;
			
			if (this.envQuerys == null) {
				this.envQuerys = new ArrayList<Map<String, String>>();
			} else {
				this.envQuerys.clear();
			}
			
			for (String query : queryArr) {
				Map<String, String> map = new HashMap<String, String>(4);
				map.put("name", query);
				map.put("rminame", Util.null2String(prop.get("oracle.query." + query + ".rminame")));
				map.put("primarykey", Util.null2String(prop.get("oracle.query." + query + ".primarykey")));
				map.put("rmiargbean", Util.null2String(prop.get("oracle.query." + query + ".rmiargbean")));
				map.put("rmiargname", Util.null2String(prop.get("oracle.query." + query + ".rmiargname")));
				map.put("rmifiltername", Util.null2String(prop.get("oracle.query." + query + ".rmifiltername")));
				map.put("rmifiltervalue", Util.null2String(prop.get("oracle.query." + query + ".rmifiltervalue")));
				map.put("modeid", Util.null2String(prop.get("oracle.query." + query + ".modeid")));
				map.put("modecreator", Util.null2String(prop.get("oracle.query." + query + ".modecreator")));
				
				this.envQuerys.add(map);
			}
		} catch (Exception e) {
			logger.error("loadCustom Exception: ", e);
		}
	}
	
	/**
	 * 创建返回信息
	 * 
	 * @param type
	 * 					调用类型
	 * @param task
	 * 					调用方法
	 * @param code
	 * 					返回编码
	 * @param message
	 * 					返回信息
	 * @param request
	 * 					请求信息
	 * @param response
	 * 					响应信息
	 * @return 执行结果
	 */
	private OracleResult<String, String> callback(
			String type, String task, String code, String message, 
			String request, String response) {
		OracleResult<String, String> result = new OracleResult<String, String>();
		result.setType(type);
		result.setTask(task);
		result.setCode(code);
		result.setMessage(message);
		result.setRequest(request);
		result.setResponse(response);
		return result;
	}
	
}
