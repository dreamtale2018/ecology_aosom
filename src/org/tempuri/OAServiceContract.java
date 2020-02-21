/**
 * OAServiceContract.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface OAServiceContract extends java.rmi.Remote {
    public java.lang.String getProduct(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetProductInfo productinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getProductV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetProductInfo productinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getSeasProduct(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetProductInfo productinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getVendor(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getVendorAddress(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorAddressInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getVendorContact(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetVendorContactInfo vendorinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getPoList(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetPoListInfo piListinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String getItemQty(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetItemQtyInfo itemQtyInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String createWorkflow() throws java.rmi.RemoteException;
    public java.lang.String updatePoStatus(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.UpdatePoStatusInfo poStatusInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String importOAItem(String OAItemJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String returnOAItemSKU(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.ReturnOAItemSKUInfo OAItemSKUInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String importOAORGItem(java.lang.String OAORGItemJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String importOAQuote(java.lang.String OAQuoteJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String updateOAQuote(java.lang.String OAQuoteJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String importOAVendor(java.lang.String OAVendorJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String createPhotoWorkflow() throws java.rmi.RemoteException;
    public java.lang.String returnSKU() throws java.rmi.RemoteException;
    public java.lang.String getPoQuoteList(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetPoQuoteListInfo poQuoteInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String updateElectronicLineStates(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.Electronic[] electronicInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String updateElectronicIsCreateSo(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.Electronic[] electronicInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String OAUpdatePaymentStatus(java.lang.String paymentStatusJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String OAPoLineClose(java.lang.String poLoneCloseJson, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String updateStatus(java.lang.String json, java.lang.String updateType, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel updateStatusV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.UpdateStatusJson pinfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.ReturnModel getItemORGQtyV2(org.datacontract.schemas._2004._07.MH_EBSOAWcfService.GetItemORGQtyInfo itemORGQtyInfo, java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
}
