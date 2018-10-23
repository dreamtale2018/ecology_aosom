/**
 * OAServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class OAServiceLocator extends org.apache.axis.client.Service implements org.tempuri.OAService {

    public OAServiceLocator() {
    }


    public OAServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OAServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BasicHttpBinding_OAServiceContract
    private java.lang.String BasicHttpBinding_OAServiceContract_address = "http://173.193.196.235:1527/OAService.svc";

    public java.lang.String getBasicHttpBinding_OAServiceContractAddress() {
        return BasicHttpBinding_OAServiceContract_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BasicHttpBinding_OAServiceContractWSDDServiceName = "BasicHttpBinding_OAServiceContract";

    public java.lang.String getBasicHttpBinding_OAServiceContractWSDDServiceName() {
        return BasicHttpBinding_OAServiceContractWSDDServiceName;
    }

    public void setBasicHttpBinding_OAServiceContractWSDDServiceName(java.lang.String name) {
        BasicHttpBinding_OAServiceContractWSDDServiceName = name;
    }

    public org.tempuri.OAServiceContract getBasicHttpBinding_OAServiceContract() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BasicHttpBinding_OAServiceContract_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBasicHttpBinding_OAServiceContract(endpoint);
    }

    public org.tempuri.OAServiceContract getBasicHttpBinding_OAServiceContract(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.BasicHttpBinding_OAServiceContractStub _stub = new org.tempuri.BasicHttpBinding_OAServiceContractStub(portAddress, this);
            _stub.setPortName(getBasicHttpBinding_OAServiceContractWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBasicHttpBinding_OAServiceContractEndpointAddress(java.lang.String address) {
        BasicHttpBinding_OAServiceContract_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.OAServiceContract.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.BasicHttpBinding_OAServiceContractStub _stub = new org.tempuri.BasicHttpBinding_OAServiceContractStub(new java.net.URL(BasicHttpBinding_OAServiceContract_address), this);
                _stub.setPortName(getBasicHttpBinding_OAServiceContractWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("BasicHttpBinding_OAServiceContract".equals(inputPortName)) {
            return getBasicHttpBinding_OAServiceContract();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "OAService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "BasicHttpBinding_OAServiceContract"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BasicHttpBinding_OAServiceContract".equals(portName)) {
            setBasicHttpBinding_OAServiceContractEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
