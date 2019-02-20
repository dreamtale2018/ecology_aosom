package weaver.hrm.webservice;

public interface HrmInfoService {
	
	/**
	 * 获取人员ID
	 * @param code 工号
	 * @return
	 * @throws Exception
	 */
	public String getHrmID(String code) throws Exception;
	
	/**
	 * 获取离职人员ID
	 * @param code 工号
	 * @return
	 * @throws Exception
	 */
	public String getHrmResignedID(String code) throws Exception;
	
	/**
	 * 获取部门ID
	 * @param code 工号
	 * @return
	 * @throws Exception
	 */
	public String getHrmDepartmentID(String code) throws Exception;

	/**
	 * 获取部门ID
	 * @param code 工号
	 * @return
	 * @throws Exception
	 */
	public String getHrmSecondDepartmentID(String code) throws Exception;
	
}
