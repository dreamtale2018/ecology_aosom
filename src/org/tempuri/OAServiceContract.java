/**
 * OAServiceContract.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface OAServiceContract extends java.rmi.Remote {
    public java.lang.String cliendTest() throws java.rmi.RemoteException;
    public java.lang.String getProductV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetProductInfo productinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getSeasProduct(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetProductInfo productinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel getSeasProductV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetProductInfo productinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel getPoQuoteListV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetPoQuoteListInfo poQuoteInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel getItemORGQtyV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetItemORGQtyInfo itemORGQtyInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel getVendorV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel getVendorAddressV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorAddressInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel getVendorContactV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorContactInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel getItemQtyV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetItemQtyInfo itemQtyInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel getGimmickDataV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetGimmickData pinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String createPoLists() throws java.rmi.RemoteException;
    public java.lang.String returnSKU() throws java.rmi.RemoteException;
    public java.lang.String createPhotoWorkflow() throws java.rmi.RemoteException;
    public java.lang.String getElectronic() throws java.rmi.RemoteException;
    public java.lang.String createPaymentPoListsNew() throws java.rmi.RemoteException;
    public java.lang.String createPaymentPay() throws java.rmi.RemoteException;
    public java.lang.String createQCTask() throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel createQCTaskReminder(org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.QCTaskReminderModel QCTaskReminder, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel updateStatusV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.UpdateStatusJson pinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getVendor(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getVendorAddress(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorAddressInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getVendorContact(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorContactInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getItemQty(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetItemQtyInfo itemQtyInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String updatePoStatus(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.UpdatePoStatusInfo poStatusInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String OAPoLineClose(java.lang.String poLoneCloseJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String importOAItem(java.lang.String OAItemJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String importOAORGItem(java.lang.String OAORGItemJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String importOAQuote(java.lang.String OAQuoteJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String updateOAQuote(java.lang.String OAQuoteJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getPoQuoteList(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetPoQuoteListInfo poQuoteInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String updateElectronicLineStates(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.Electronic[] electronicInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String updateElectronicIsCreateSo(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.Electronic[] electronicInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String OAUpdatePaymentStatus(java.lang.String paymentStatusJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String OAUpdateQCTaskStatus(java.lang.String QCTaskStatusJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String updateStatus(java.lang.String json, java.lang.String updateType, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String importOAVendor(java.lang.String OAVendorJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public void oracleIDTest(java.lang.String userID) throws java.rmi.RemoteException;
}
