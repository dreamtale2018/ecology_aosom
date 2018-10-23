/**
 * ReturnOAItemSKUInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService;

public class ReturnOAItemSKUInfo  implements java.io.Serializable {
    private java.math.BigDecimal OAFlowID;

    private java.util.Calendar lastupdate_datetime;

    public ReturnOAItemSKUInfo() {
    }

    public ReturnOAItemSKUInfo(
           java.math.BigDecimal OAFlowID,
           java.util.Calendar lastupdate_datetime) {
           this.OAFlowID = OAFlowID;
           this.lastupdate_datetime = lastupdate_datetime;
    }


    /**
     * Gets the OAFlowID value for this ReturnOAItemSKUInfo.
     * 
     * @return OAFlowID
     */
    public java.math.BigDecimal getOAFlowID() {
        return OAFlowID;
    }


    /**
     * Sets the OAFlowID value for this ReturnOAItemSKUInfo.
     * 
     * @param OAFlowID
     */
    public void setOAFlowID(java.math.BigDecimal OAFlowID) {
        this.OAFlowID = OAFlowID;
    }


    /**
     * Gets the lastupdate_datetime value for this ReturnOAItemSKUInfo.
     * 
     * @return lastupdate_datetime
     */
    public java.util.Calendar getLastupdate_datetime() {
        return lastupdate_datetime;
    }


    /**
     * Sets the lastupdate_datetime value for this ReturnOAItemSKUInfo.
     * 
     * @param lastupdate_datetime
     */
    public void setLastupdate_datetime(java.util.Calendar lastupdate_datetime) {
        this.lastupdate_datetime = lastupdate_datetime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReturnOAItemSKUInfo)) return false;
        ReturnOAItemSKUInfo other = (ReturnOAItemSKUInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.OAFlowID==null && other.getOAFlowID()==null) || 
             (this.OAFlowID!=null &&
              this.OAFlowID.equals(other.getOAFlowID()))) &&
            ((this.lastupdate_datetime==null && other.getLastupdate_datetime()==null) || 
             (this.lastupdate_datetime!=null &&
              this.lastupdate_datetime.equals(other.getLastupdate_datetime())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getOAFlowID() != null) {
            _hashCode += getOAFlowID().hashCode();
        }
        if (getLastupdate_datetime() != null) {
            _hashCode += getLastupdate_datetime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReturnOAItemSKUInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "ReturnOAItemSKUInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("OAFlowID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "OAFlowID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastupdate_datetime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "Lastupdate_datetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
