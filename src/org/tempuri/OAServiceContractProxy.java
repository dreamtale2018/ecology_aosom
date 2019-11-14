package org.tempuri;

public class OAServiceContractProxy implements org.tempuri.OAServiceContract {
  private String _endpoint = null;
  private org.tempuri.OAServiceContract oAServiceContract = null;
  
  public OAServiceContractProxy() {
    _initOAServiceContractProxy();
  }
  
  public OAServiceContractProxy(String endpoint) {
    _endpoint = endpoint;
    _initOAServiceContractProxy();
  }
  
  private void _initOAServiceContractProxy() {
    try {
      oAServiceContract = (new org.tempuri.OAServiceLocator()).getBasicHttpBinding_OAServiceContract();
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
  
  public org.tempuri.OAServiceContract getOAServiceContract() {
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract;
  }
  
  public java.lang.String getProduct(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetProductInfo productinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.getProduct(productinfo, name, pwd);
  }
  
  public java.lang.String getProductV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetProductInfo productinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.getProductV2(productinfo, name, pwd);
  }
  
  public java.lang.String getSeasProduct(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetProductInfo productinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.getSeasProduct(productinfo, name, pwd);
  }
  
  public java.lang.String getVendor(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.getVendor(vendorinfo, name, pwd);
  }
  
  public java.lang.String getVendorAddress(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorAddressInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.getVendorAddress(vendorinfo, name, pwd);
  }
  
  public java.lang.String getVendorContact(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorContactInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.getVendorContact(vendorinfo, name, pwd);
  }
  
  public java.lang.String getPoList(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetPoListInfo piListinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.getPoList(piListinfo, name, pwd);
  }
  
  public java.lang.String getItemQty(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetItemQtyInfo itemQtyInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.getItemQty(itemQtyInfo, name, pwd);
  }
  
  public java.lang.String createWorkflow() throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.createWorkflow();
  }
  
  public java.lang.String updatePoStatus(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.UpdatePoStatusInfo poStatusInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.updatePoStatus(poStatusInfo, name, pwd);
  }
  
  public java.lang.String importOAItem(String OAItemJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.importOAItem(OAItemJson, name, pwd);
  }
  
  public java.lang.String returnOAItemSKU(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.ReturnOAItemSKUInfo OAItemSKUInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.returnOAItemSKU(OAItemSKUInfo, name, pwd);
  }
  
  public java.lang.String importOAORGItem(java.lang.String OAORGItemJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.importOAORGItem(OAORGItemJson, name, pwd);
  }
  
  /**
   * 推送报价单到Oracle
   * 
   * @author ycj@20180814
   */
  public java.lang.String importOAQuote(java.lang.String OAQuoteJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.importOAQuote(OAQuoteJson, name, pwd);
  }
  
  /**
   * 更新报价单到Oracle
   * 
   * @author ycj@20180814
   */
  public java.lang.String updateOAQuote(java.lang.String OAQuoteJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.updateOAQuote(OAQuoteJson, name, pwd);
  }
  
  public java.lang.String getPoQuoteList(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetPoQuoteListInfo poQuoteInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.getPoQuoteList(poQuoteInfo, name, pwd);
  }
  
  public java.lang.String updateElectronicLineStates(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.Electronic[] electronicInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.updateElectronicLineStates(electronicInfo, name, pwd);
  }
  
  public java.lang.String updateElectronicIsCreateSo(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.Electronic[] electronicInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.updateElectronicIsCreateSo(electronicInfo, name, pwd);
  }
  
  public java.lang.String OAUpdatePaymentStatus(java.lang.String paymentStatusJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.OAUpdatePaymentStatus(paymentStatusJson, name, pwd);
  }
  
  public java.lang.String OAPoLineClose(java.lang.String poLineCloseJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.OAPoLineClose(poLineCloseJson, name, pwd);
  }
  
  public java.lang.String updateStatus(java.lang.String QCTaskStatusJson, java.lang.String updateType, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.updateStatus(QCTaskStatusJson, updateType, name, pwd);
  }
  
  public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_Function.ReturnModel updateStatusV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.UpdateStatusJson pinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
	  if (oAServiceContract == null)
		  _initOAServiceContractProxy();
	  return oAServiceContract.updateStatusV2(pinfo, name, pwd);
  }
  
  public java.lang.String importOAVendor(java.lang.String OAVendorJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.importOAVendor(OAVendorJson, name, pwd);
  }
  
  public java.lang.String createPhotoWorkflow() throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.createPhotoWorkflow();
  }
  
  public java.lang.String returnSKU() throws java.rmi.RemoteException{
    if (oAServiceContract == null)
      _initOAServiceContractProxy();
    return oAServiceContract.returnSKU();
  }
  
  
}