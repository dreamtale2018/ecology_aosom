package org.tempuri.erp;

public class OAServiceContractProxy implements org.tempuri.erp.OAServiceContract {
  private String _endpoint = null;
  private org.tempuri.erp.OAServiceContract oAServiceContract = null;
  
  public OAServiceContractProxy() {
    _initOAServiceContractProxy();
  }
  
  public OAServiceContractProxy(String endpoint) {
    _endpoint = endpoint;
    _initOAServiceContractProxy();
  }
  
  private void _initOAServiceContractProxy() {
    try {
      oAServiceContract = (new org.tempuri.erp.ERPOAServiceLocator()).getBasicHttpBinding_OAServiceContract();
      if (oAServiceContract != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)oAServiceContract)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)oAServiceContract)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (oAServiceContract != null)
      ((javax.xml.rpc.Stub)oAServiceContract)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.tempuri.erp.OAServiceContract getOAServiceContract() {
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract;
  }
  
  public java.lang.String updateOAApprovalStatus(org.datacontract.schemas._2004._07.MH_ERPOAWcfService.UpdateOAApprovalStatusInfo pinfo) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.updateOAApprovalStatus(pinfo);
  }
  
  public java.lang.String test(java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.test(name, pwd);
  }
  
  public java.lang.String pushPoApproval(java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.pushPoApproval(name, pwd);
  }
  
  public java.lang.String pushPaymentApproval(java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.pushPaymentApproval(name, pwd);
  }
  
}