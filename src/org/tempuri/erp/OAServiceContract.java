/**
 * OAServiceContract.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri.erp;

public interface OAServiceContract extends java.rmi.Remote {
    public java.lang.String test(java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String pushPoApproval(java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String pushPaymentApproval(java.lang.String name, java.lang.String pwd) throws java.rmi.RemoteException;
    public java.lang.String updateOAApprovalStatus(org.datacontract.schemas._2004._07.MH_ERPOAWcfService.UpdateOAApprovalStatusInfo pinfo) throws java.rmi.RemoteException;
}
