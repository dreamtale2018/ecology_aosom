package weaver.hrm.webservice;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class HrmInfoServiceImpl extends BaseBean implements HrmInfoService {

	/**
	 * 获取人员ID
	 * @param code 工号
	 * @return
	 * @throws Exception
	 */
	public String getHrmID(String code) throws Exception {
		
		String id = "";
		RecordSet rs = new RecordSet();
		String sql = "select id from hrmresource " +
				"where loginid = '" + code + "' and status < 4 ";
		rs.execute(sql);
		if(rs.next()){
			id = Util.null2String(rs.getString("id"));
		}
		
		return id;
	}
	
	/**
	 * 获取离职人员ID
	 * @param code 工号
	 * @return
	 * @throws Exception
	 */
	public String getHrmResignedID(String code) throws Exception {
		
		String id = "";
		RecordSet rs = new RecordSet();
		String sql = "select id from hrmresource " +
				"where workcode = '" + code + "'";
		rs.execute(sql);
		if(rs.next()){
			id = Util.null2String(rs.getString("id"));
		}
		
		return id;
	}
	
	/**
	 * 获取部门ID
	 * @param code 工号
	 * @return
	 * @throws Exception
	 */
	public String getHrmDepartmentID(String code) throws Exception {
		String id = "";
		RecordSet rs = new RecordSet();
		String sql = "select (select case a.supdepid when 0 then a.id else "
				+ "(select b.id from hrmdepartment b where b.id = a.supdepid) end as id "
				+ "from hrmdepartment a where id = departmentid) departmentid, "
				+ "departmentid departmentidB "
				+ "from hrmresource "
				+ "where loginid = '" + code + "' and status < 4 ";
		rs.execute(sql);
		if(rs.next()){
			id = Util.null2String(rs.getString("departmentid"));
		}
		return id;
	}

	@Override
	public String getHrmSecondDepartmentID(String code) throws Exception {
		String idB = "";
		RecordSet rs = new RecordSet();
		String sql = "select (select case a.supdepid when 0 then a.id else "
				+ "(select b.id from hrmdepartment b where b.id = a.supdepid) end as id "
				+ "from hrmdepartment a where id = departmentid) departmentid, "
				+ "departmentid departmentidB "
				+ "from hrmresource "
				+ "where loginid = '" + code + "' and status < 4 ";
		rs.execute(sql);
		if(rs.next()){
			idB = Util.null2String(rs.getString("departmentidB"));
		}
		return idB;
	}

}