package weaver.interfaces.workflow.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import com.weaver.ningb.core.util.WorkflowUtils;
import com.weaver.ningb.direct.dao.workflow.ProductCodeDao;
import com.weaver.ningb.direct.dao.workflow.impl.ProductCodeDaoImpl;
import com.weaver.ningb.direct.entity.integration.OracleProductCode;
import com.weaver.ningb.direct.entity.integration.OracleResult;
import com.weaver.ningb.direct.manager.integration.OracleManager;
import com.weaver.ningb.logging.LogFactory;
import com.weaver.ningb.soa.workflow.action.support.ActionInfo;
import com.weaver.ningb.soa.workflow.action.support.ActionUtils;

/**
 * 货号申请流程<br>
 * 归档, 将数据推送给 Oracle
 * 
 * @author liberal
 *
 */
public class ProductCodeArchiveAction implements Action {
	
	private static final Log logger = LogFactory.getLog(ProductCodeArchiveAction.class);

	private OracleManager oracleManager = new OracleManager();
	private ProductCodeDao dao = new ProductCodeDaoImpl();
	
	@Override
	public String execute(RequestInfo request) {
		try {
			String workflowid = request.getWorkflowid();
			String requestid = request.getRequestid();
			
			// 获取流程主表数据
			ActionInfo info = ActionUtils.getActionInfo(request);
			Map<String, String> mainMap = info.getMainMap();
			String kfygh = mainMap.get("KFYGH");		// 开发员工号
			String zwpm = mainMap.get("ZWPM");			// 中文品名
			String ywpm = mainMap.get("YWPM");			// 英文品名
			String zwcpgg = mainMap.get("ZWCPGG");		// 中文产品规格
			String ywcpgg = mainMap.get("YWCPGG");		// 英文产品规格
			String cplb = mainMap.get("CPLB");			// 产品类型
			String tjlb = mainMap.get("TJLB");			// 统计类别
			String cpmd = mainMap.get("CPMD");			// 产品卖点
			String cph = mainMap.get("CPH");			// 产品号
			String zwcpsxA = mainMap.get("ZWCPSX1");	// 中文产品属性 1
			String sfynhh = mainMap.get("SFYNHH");		// 是否越南货号
			String ynhgbm = mainMap.get("YNHGBM");		// 越南海关编码
			String ynywbgpm = mainMap.get("YNYWBGPM");	// 越南英文报关品名
			String zwcpsxB = mainMap.get("ZWCPSX2");	// 中文产品
			String ywcpsxA = mainMap.get("YWCPSX1");	// 英文产品属性 1
			String ywcpsxB = mainMap.get("YWCPSX2");	// 英文产品属性 2
			String sfzswl = Util.null2String(mainMap.get("SFZSWL"), "N");
														// 是否招商物料：默认 N
			String sfyljb = mainMap.get("SFYLJB");		// 是否有零件包
			String sfys = WorkflowUtils.getFieldSelectName(workflowid, "SFYS", mainMap.get("SFYS"));			
														// 易碎
			String sfxs = WorkflowUtils.getFieldSelectName(workflowid, "SFXS", mainMap.get("SFXS"));			
														// 向上
			String sfkxjsb = WorkflowUtils.getFieldSelectName(workflowid, "SFKXJSB", mainMap.get("SFKXJSB"));	
														// 开箱警示标	
			
			
			List<Map<String, String>> boxContentList = new ArrayList<Map<String, String>>();
			// 获取流程明细表 2
			List<Map<String, String>> detailBList = info.getDetailMap("2");
			for (Map<String, String> detailBMap : detailBList) {
				String xhDetailB = detailBMap.get("XH");			// 箱号
				String slDetailB = detailBMap.get("SL");			// 数量
				String cDetailB = detailBMap.get("C");				// 长 (CM)
				String kDetailB = detailBMap.get("K");				// 宽 (CM)
				String gDetailB = detailBMap.get("G");				// 高 (CM)
				String wcDetailB = detailBMap.get("WC");			// 外长 (CM)
				String wkDetailB = detailBMap.get("WK");			// 外宽 (CM)
				String wgDetailB = detailBMap.get("WG");			// 外高 (CM)
				String ccdwDetailB = oracleManager.getDwmc(detailBMap.get("CCDW"));
																	// 尺寸单位：默认 厘米
				String tjDteailB = detailBMap.get("TJ");			// 体积
				String tjdwDetailB = oracleManager.getDwmc(detailBMap.get("TJDW"));
																	// 体积单位：默认 立方厘米
				String mzDetailB = detailBMap.get("MZ");			// 毛重 (KG)
				String wmzDetailB = detailBMap.get("WMZ");			// 外毛重 (KG)
				String jzDetailB = detailBMap.get("JZ");			// 净重 (KG)
				String zldwDetailB = oracleManager.getDwmc(detailBMap.get("ZLDW"));
																	// 重量单位：默认值 克
				String xlDetailB = detailBMap.get("XL");			// 箱率
				
				Map<String, String> boxContentMap = new HashMap<String, String>();
				boxContentMap.put("box_number", xhDetailB);
				boxContentMap.put("box_quantity", slDetailB);
				boxContentMap.put("box_length", cDetailB);
				boxContentMap.put("box_width", kDetailB);
				boxContentMap.put("box_height", gDetailB);
				boxContentMap.put("box_out_length", wcDetailB);
				boxContentMap.put("box_out_width", wkDetailB);
				boxContentMap.put("box_out_height", wgDetailB);
				boxContentMap.put("box_size_uom_code", ccdwDetailB);
				boxContentMap.put("box_volume", tjDteailB);
				boxContentMap.put("box_volume_uom_code", tjdwDetailB);
				boxContentMap.put("box_gross_weight", mzDetailB);
				boxContentMap.put("box_out_gross_weight", wmzDetailB);
				boxContentMap.put("box_net_weight", jzDetailB);
				boxContentMap.put("box_weight_uom_code", zldwDetailB);
				boxContentMap.put("box_outsourcing_box_qty", xlDetailB);
				
				boxContentList.add(boxContentMap);
			}
			
			List<Map<String, String>> bomContentList = new ArrayList<Map<String, String>>();
			// 获取流程明细表 3
			List<Map<String, String>> detailCList = info.getDetailMap("3");
			if (detailCList != null && detailCList.size() > 0) {
				for (int i = 0; i < detailCList.size(); i++) {
					Map<String, String> detailCMap = detailCList.get(i);
					
					String hhDetailC = (i + 1) + "";					// 行序列
					String bgpmDetailC = detailCMap.get("BGPM");		// 报关品名
					String slDetailC = detailCMap.get("SL");			// 数量
					String cbblDetailC = detailCMap.get("CBBL");		// 成本比例 (%)
					String hgbmDetailC = detailCMap.get("HGBM");		// 海关编码
					String zldwDetailC = oracleManager.getDwmc(detailCMap.get("ZLDW"));
																		// 重量单位：默认值 克
					String tjdwDetailC = oracleManager.getDwmc(detailCMap.get("TJDW"));
																		// 体积单位：默认值 立方厘米
					
					Map<String, String> bomContentMap = new HashMap<String, String>();
					bomContentMap.put("bom_line_num", hhDetailC);
					bomContentMap.put("bom_name", bgpmDetailC);
					bomContentMap.put("bom_declared_item_name_cn", bgpmDetailC);
					bomContentMap.put("bom_quantity", slDetailC);
					bomContentMap.put("bom_cost_percentage", cbblDetailC);
					bomContentMap.put("bom_item_declare_code", hgbmDetailC);
					bomContentMap.put("bom_weight_uom_code", zldwDetailC);
					bomContentMap.put("bom_volume_uom_code", tjdwDetailC);
					bomContentMap.put("bom_outer_box_percentage", "");
					
					bomContentList.add(bomContentMap);
				}
			}
			
			// 货号信息
			List<OracleProductCode> pcList = new ArrayList<OracleProductCode>();
			boolean bomFlag = checkBom(info);		// 检测是否管理 bom
			// 获取流程明细表 1
			List<Map<String, String>> detailAList = info.getDetailMap("1");
			if (detailAList != null && detailAList.size() > 0) {
				for (int i = 0; i < detailAList.size(); i++) {
					Map<String, String> detailAMap = detailAList.get(i);
					
					OracleProductCode pc = new OracleProductCode();
					
					String hhDetailA = requestid;						// OA 中唯一标识码, 用于区分流程
					String mxhhDetailA = detailAMap.get("id");			// 行号, OA 中唯一标识码, 用于区分流程行
					String lshDetailA = detailAMap.get("LSH");			// 流水号
					String bbhDetailA = detailAMap.get("BBH");			// 版本号
					String kfyghDetailA = kfygh;						// 开发员工号
					String zwpmDetailA = zwpm;							// 中文品名
					String ywpmDetailA = ywpm;							// 英文品名
					String zwcpggDetailA = zwcpgg;						// 中文产品规格
					String ywcpggDetailA = ywcpgg;						// 英文产品规格
					String ysjsmDetailA = detailAMap.get("YSJSM");		// 颜色加说明
					String jbdwDetailA = oracleManager.getDwmc(detailAMap.get("JBDW"));		
																		// 基本单位
					String yscDetailA = oracleManager.getYsmc(detailAMap.get("YSC"));			
																		// 颜色 (传 Oracle)
					String cpmdDetailA = cpmd;							// 产品卖点
					String cphDetailA = cph;							// 产品号
					String glbomDetailA = "";							// 管理 bom
					String hgbmDetailA = "";							// 海关编码
					String ynhgbmDetailA = ynhgbm;						// 越南海关编码
					String ynywbgpmDetailA = ynywbgpm;					// 越南英文报关品名
					String bgpmDetailA = "";							// 报关品名
					String cplbDetailA = cplb;							// 产品类别
					String tjlbDetailA = tjlb;							// 统计类别
					String zwcpsxADetailA = zwcpsxA;					// 中文产品属性 1
					String zwcpsxBDetailA = zwcpsxB;					// 中文产品属性 2
					String ywcpsxADetailA = ywcpsxA;					// 英文产品属性 1
					String ywcpsxBDetailA = ywcpsxB;					// 英文产品属性 2
					String gcxhDetailA = detailAMap.get("GCXH");		// 工厂型号
					String sfzswlDetailA = sfzswl;						// 是否招商物料
					String sfysDetailA = sfys;							// 易碎
					String sfxsDetailA = sfxs;							// 向上
					String sfceDetailA = WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "SFCE", detailAMap.get("SFCE"));			
																		// CE
					String sfkxjsbDetailA = sfkxjsb;					// 开箱警示标
					String sfyljbDetailA = WorkflowUtils.getFieldSelectName(workflowid, "SFYLJB", sfyljb);
																		// 是否有零件包
					String sfqdbbhDetailA = WorkflowUtils.getDetailFieldSelectName(workflowid, 1, "SFQDBBH", detailAMap.get("SFQDBBH"));
																		// 是否启动版本号
					
					if (!bomFlag) {
						glbomDetailA = "N";
						hgbmDetailA = getBomInfo(info, "HGBM");
						bgpmDetailA = getBomInfo(info, "BGPM");
					} else {
						glbomDetailA = "Y";
					}
					
					Map<String, String> itemContentMap = new HashMap<String, String>();
					itemContentMap.put("oa_item_code", hhDetailA);
					itemContentMap.put("oa_item_line_seq", mxhhDetailA);
					itemContentMap.put("serial_number", lshDetailA);
					itemContentMap.put("item_version", bbhDetailA);
					itemContentMap.put("developer_user", kfyghDetailA);
					itemContentMap.put("developer_olduser", null);
					itemContentMap.put("item_name_cn", zwpmDetailA);
					itemContentMap.put("item_name_us", ywpmDetailA);
					itemContentMap.put("item_specification_cn", zwcpggDetailA);
					itemContentMap.put("item_specification_us", ywcpggDetailA);
					itemContentMap.put("item_desc", ysjsmDetailA);
					itemContentMap.put("primary_uom_code", jbdwDetailA);
					itemContentMap.put("color", yscDetailA);
					itemContentMap.put("selling_point", cpmdDetailA);
					itemContentMap.put("product_id", cphDetailA);
					itemContentMap.put("include_bom_flag", glbomDetailA);
					itemContentMap.put("is_fragile_flag", sfysDetailA);
					itemContentMap.put("is_up_flag", sfxsDetailA);
					itemContentMap.put("l_is_ce_flag", sfceDetailA);
					itemContentMap.put("is_warning_flag", sfkxjsbDetailA);
					if (glbomDetailA.equals("N")) {
						itemContentMap.put("hs_code", hgbmDetailA);
						itemContentMap.put("declared_item_name_cn", bgpmDetailA);
					}
					if ("0".equals(sfynhh)) {
						itemContentMap.put("hs_code_vet", ynhgbmDetailA);
						itemContentMap.put("item_declare_name_vet", ynywbgpmDetailA);
					}
					itemContentMap.put("category_code", cplbDetailA);
					itemContentMap.put("develop_category_code", tjlbDetailA);
					itemContentMap.put("item_attached1", zwcpsxADetailA);
					itemContentMap.put("item_attached2", zwcpsxBDetailA);
					itemContentMap.put("item_attached3", ywcpsxADetailA);
					itemContentMap.put("item_attached4", ywcpsxBDetailA);
					itemContentMap.put("factory_number", gcxhDetailA);
					itemContentMap.put("investment_item", sfzswlDetailA);
					itemContentMap.put("include_package_flag", sfyljbDetailA);
					itemContentMap.put("item_version_enable", sfqdbbhDetailA);
					
					/*String usDetailA = detailAMap.get("US");			// US
					String usppDetailA = mainMap.get("USPP");			// US 品牌
					String uspmDetailA = mainMap.get("USPM");			// US 清关英文品名
					String caDetailA = detailAMap.get("CA");			// CA
					String cappDetailA = mainMap.get("CAPP");			// CA 品牌
					String capmDetailA = mainMap.get("CAPM");			// CA 清关英文品名
					String ukDetailA = detailAMap.get("UK");			// UK
					String ukppDetailA = mainMap.get("UKPP");			// UK 品牌
					String ukpmDetailA = mainMap.get("UKPM");			// UK 清关英文品名
					String deDetailA = detailAMap.get("DE");			// DE
					String deppDetailA = mainMap.get("DEPP");			// DE 品牌
					String depmDetailA = mainMap.get("DEPM");			// DE 清关英文品名
					String frDetailA = detailAMap.get("FR");			// FR
					String frppDetailA = mainMap.get("FRPP");			// FR 品牌
					String frpmDetailA = mainMap.get("FRPM");			// FR 清关英文品名
					String itDetailA = detailAMap.get("IT");			// IT
					String itppDetailA = mainMap.get("ITPP");			// IT 品牌
					String itpmDetailA = mainMap.get("ITPM");			// IT 清关英文品名
					String esDetailA = detailAMap.get("ES");			// ES
					String esppDetailA = mainMap.get("ESPP");			// ES 品牌
					String espmDetailA = mainMap.get("ESPM");			// ES 清关英文品名
					
					String usjjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "USJJSX", mainMap.get("USJJSX")));		
																		// US季节属性
					usjjsxDetailA = oracleManager.getChineseMsg(usjjsxDetailA);
					String usjrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "USJRSX", mainMap.get("USJRSX")));	
																		// US节日属性
					usjrsxDetailA = oracleManager.getChineseMsg(usjrsxDetailA);
					String cajjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "CAJJSX", mainMap.get("CAJJSX")));		
																		// CA季节属性
					cajjsxDetailA = oracleManager.getChineseMsg(cajjsxDetailA);
					String cajrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "CAJRSX", mainMap.get("CAJRSX")));	
																		// CA节日属性
					cajrsxDetailA = oracleManager.getChineseMsg(cajrsxDetailA);
					String ukjjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "UKJJSX", mainMap.get("UKJJSX")));		
																		// UK季节属性
					ukjjsxDetailA = oracleManager.getChineseMsg(ukjjsxDetailA);
					String ukjrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "UKJRSX", mainMap.get("UKJRSX")));	
																		// UK节日属性
					ukjrsxDetailA = oracleManager.getChineseMsg(ukjrsxDetailA);
					String dejjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "DEJJSX", mainMap.get("DEJJSX")));		
																		// DE季节属性
					dejjsxDetailA = oracleManager.getChineseMsg(dejjsxDetailA);
					String dejrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "DEJRSX", mainMap.get("DEJRSX")));	
																		// DE节日属性
					dejrsxDetailA = oracleManager.getChineseMsg(dejrsxDetailA);
					String frjjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "FRJJSX", mainMap.get("FRJJSX")));		
																		// FR季节属性
					frjjsxDetailA = oracleManager.getChineseMsg(frjjsxDetailA);
					String frjrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "FRJRSX", mainMap.get("FRJRSX")));	
																		// FR节日属性
					frjrsxDetailA = oracleManager.getChineseMsg(frjrsxDetailA);
					String itjjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "ITJJSX", mainMap.get("ITJJSX")));		
																		// IT季节属性
					itjjsxDetailA = oracleManager.getChineseMsg(itjjsxDetailA);
					String itjrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "ITJRSX", mainMap.get("ITJRSX")));	
																		// IT节日属性
					itjrsxDetailA = oracleManager.getChineseMsg(itjrsxDetailA);
					String esjjsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "ESJJSX", mainMap.get("ESJJSX")));		
																		// ES季节属性
					esjjsxDetailA = oracleManager.getChineseMsg(esjjsxDetailA);
					String esjrsxDetailA = Util.null2String(WorkflowUtils.getFieldSelectName(workflowid, "ESJRSX", mainMap.get("ESJRSX")));	
																		// ES节日属性
					esjrsxDetailA = oracleManager.getChineseMsg(esjrsxDetailA);
					
					// 内容处理
					List<Map<String, String>> ouContentList = new ArrayList<Map<String, String>>(1);
					if (!StringUtils.isBlank(usDetailA)) {
						ouContentList.add(addOu("US", WorkflowUtils.getFieldSelectName(workflowid, "USPP", usppDetailA), uspmDetailA, usjjsxDetailA, usjrsxDetailA));
					}
					if (!StringUtils.isBlank(caDetailA)) {
						ouContentList.add(addOu("CA", WorkflowUtils.getFieldSelectName(workflowid, "CAPP", cappDetailA), capmDetailA, cajjsxDetailA, cajrsxDetailA));
					}
					if (!StringUtils.isBlank(ukDetailA)) {
						ouContentList.add(addOu("GB", WorkflowUtils.getFieldSelectName(workflowid, "UKPP", ukppDetailA), ukpmDetailA, ukjjsxDetailA, ukjrsxDetailA));
					}
					if (!StringUtils.isBlank(deDetailA)) {
						ouContentList.add(addOu("DE", WorkflowUtils.getFieldSelectName(workflowid, "DEPP", deppDetailA), depmDetailA, dejjsxDetailA, dejrsxDetailA));
					}
					if (!StringUtils.isBlank(frDetailA)) {
						ouContentList.add(addOu("FR", WorkflowUtils.getFieldSelectName(workflowid, "FRPP", frppDetailA), frpmDetailA, frjjsxDetailA, frjrsxDetailA));
					}
					if (!StringUtils.isBlank(itDetailA)) {
						ouContentList.add(addOu("IT", WorkflowUtils.getFieldSelectName(workflowid, "ITPP", itppDetailA), itpmDetailA, itjjsxDetailA, itjrsxDetailA));
					}
					if (!StringUtils.isBlank(esDetailA)) {
						ouContentList.add(addOu("ES", WorkflowUtils.getFieldSelectName(workflowid, "ESPP", esppDetailA), espmDetailA, esjjsxDetailA, esjrsxDetailA));
					}*/
					
					pc.setItemContentMap(itemContentMap);
					pc.setBoxContentList(boxContentList);
					if (glbomDetailA.equals("Y")) pc.setBomContentList(bomContentList);
					//pc.setOuContentList(ouContentList);
					
					pcList.add(pc);
				}
			}
			
			if (pcList == null || pcList.size() <= 0) {
				request.getRequestManager().setMessage("操作失败 (-2)");
				request.getRequestManager().setMessagecontent("数据推送失败; {流程信息为空}; 如有疑问, 请联系系统管理员.");
			}
			
			OracleResult<String, String> result = oracleManager.pushProductCode(pcList);
			if (result == null || !"0".equals(result.getCode())) {
				request.getRequestManager().setMessage("操作失败 (-3)");
				if (result == null) {
					request.getRequestManager().setMessagecontent("数据推送失败, 请联系系统管理员.");
				} else {
					request.getRequestManager().setMessagecontent(String.format("数据推送失败; {%s}; 如有疑问, 请联系系统管理员.",
							result.getMessage()));
				}
				return Action.FAILURE_AND_CONTINUE;
			} else {
				// 更新 Oracle 生成编号到 OA 流程中
				String tablename = WorkflowUtils.getTablename(workflowid);
				boolean flag = dao.update(tablename, requestid, "ORACLEFHCS", result.getResponse());
				if (!flag) {
					request.getRequestManager().setMessage("操作失败 (-4)");
					request.getRequestManager().setMessagecontent("数据更新失败, {Oracle 流程编号更新失败}; 如有疑问, 请联系系统管理员.");
					return Action.FAILURE_AND_CONTINUE;
				}
			}
		} catch (Exception e) {
			logger.error("Failure: ", e);
			request.getRequestManager().setMessage("操作失败 (-1)");
			request.getRequestManager().setMessagecontent("提交异常, 请联系系统管理员.");
			return Action.FAILURE_AND_CONTINUE;
		}
		return Action.SUCCESS;
	}
	
	
	/**
	 * 检测是否管理 bom<br>
	 * 1.不管理 bom
	 * 	a.include_bom_flag 值为 N
	 * 	b.获取明细表 3 第一行海关编码并对应 Oracle hs_code
	 * 	c.bom_content 不传
	 * 2.管理 bom
	 * 	a.include_bom_flag 值为 Y
	 * 	b.hs_code 不传
	 * 	c.bom_content 传
	 * 
	 * @param info
	 * 					流程信息
	 * @return boolean：true.是      false.否
	 */
	private boolean checkBom(ActionInfo info) {
		boolean flag = false;
		List<Map<String, String>> detailList = info.getDetailMap("3");
		if (detailList == null || detailList.size() <= 0) return flag;
		return detailList.size() > 1;
	}
	
	/**
	 * 获取物料 Bom 指定信息, 目前包括
	 * 1.海关编码, 明细表 3, 第一行 HGBM 字段
	 * 2.报关品名, 明细表 3, 第一行 BGPM 字段
	 * 
	 * @param info
	 * 					流程信息
	 * @param key
	 * 					属性信息
	 * @return 海关编码
	 */
	private String getBomInfo(ActionInfo info, String key) {
		List<Map<String, String>> detailList = info.getDetailMap("3");
		if (detailList == null || detailList.size() <= 0) return "";
		Map<String, String> detailMap = detailList.get(0);
		if (detailMap == null || detailMap.size() <= 0) return "";
		return Util.null2String(detailMap.get(key));
	}
	
	/**
	 * 添加国别信息项
	 * 
	 * @param ouName
	 * 					国别
	 * @param brandCode
	 * 					品牌
	 * @return 国别信息项
	 */
	private Map<String, String> addOu(String ouName, String brandCode, String customsName, String seasonCode, String festivalAttribute1) {
		Map<String, String> ouContentMap = new HashMap<String, String>();
		ouContentMap.put("ou_name", ouName);
		ouContentMap.put("brand_code", brandCode);
		ouContentMap.put("customs_name", customsName);
		ouContentMap.put("season_code", seasonCode);
		ouContentMap.put("festival_attribute1", festivalAttribute1);
		return ouContentMap;
	}

}
