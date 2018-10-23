/**
 * UpdatePoStatusInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService;

public class UpdatePoStatusInfo  implements java.io.Serializable {
    private java.lang.Integer ORGID;

    private java.lang.Integer poHeaderID;

    private java.lang.String poHeaderNO;

    private java.lang.String poStatus;

    public UpdatePoStatusInfo() {
    }

    public UpdatePoStatusInfo(
           java.lang.Integer ORGID,
           java.lang.Integer poHeaderID,
           java.lang.String poHeaderNO,
           java.lang.String poStatus) {
           this.ORGID = ORGID;
           this.poHeaderID = poHeaderID;
           this.poHeaderNO = poHeaderNO;
           this.poStatus = poStatus;
    }


    /**
     * Gets the ORGID value for this UpdatePoStatusInfo.
     * 
     * @return ORGID
     */
    public java.lang.Integer getORGID() {
        return ORGID;
    }


    /**
     * Sets the ORGID value for this UpdatePoStatusInfo.
     * 
     * @param ORGID
     */
    public void setORGID(java.lang.Integer ORGID) {
        this.ORGID = ORGID;
    }


    /**
     * Gets the poHeaderID value for this UpdatePoStatusInfo.
     * 
     * @return poHeaderID
     */
    public java.lang.Integer getPoHeaderID() {
        return poHeaderID;
    }


    /**
     * Sets the poHeaderID value for this UpdatePoStatusInfo.
     * 
     * @param poHeaderID
     */
    public void setPoHeaderID(java.lang.Integer poHeaderID) {
        this.poHeaderID = poHeaderID;
    }


    /**
     * Gets the poHeaderNO value for this UpdatePoStatusInfo.
     * 
     * @return poHeaderNO
     */
    public java.lang.String getPoHeaderNO() {
        return poHeaderNO;
    }


    /**
     * Sets the poHeaderNO value for this UpdatePoStatusInfo.
     * 
     * @param poHeaderNO
     */
    public void setPoHeaderNO(java.lang.String poHeaderNO) {
        this.poHeaderNO = poHeaderNO;
    }


    /**
     * Gets the poStatus value for this UpdatePoStatusInfo.
     * 
     * @return poStatus
     */
    public java.lang.String getPoStatus() {
        return poStatus;
    }


    /**
     * Sets the poStatus value for this UpdatePoStatusInfo.
     * 
     * @param poStatus
     */
    public void setPoStatus(java.lang.String poStatus) {
        this.poStatus = poStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UpdatePoStatusInfo)) return false;
        UpdatePoStatusInfo other = (UpdatePoStatusInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ORGID==null && other.getORGID()==null) || 
             (this.ORGID!=null &&
              this.ORGID.equals(other.getORGID()))) &&
            ((this.poHeaderID==null && other.getPoHeaderID()==null) || 
             (this.poHeaderID!=null &&
              this.poHeaderID.equals(other.getPoHeaderID()))) &&
            ((this.poHeaderNO==null && other.getPoHeaderNO()==null) || 
             (this.poHeaderNO!=null &&
              this.poHeaderNO.equals(other.getPoHeaderNO()))) &&
            ((this.poStatus==null && other.getPoStatus()==null) || 
             (this.poStatus!=null &&
              this.poStatus.equals(other.getPoStatus())));
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
        if (getORGID() != null) {
            _hashCode += getORGID().hashCode();
        }
        if (getPoHeaderID() != null) {
            _hashCode += getPoHeaderID().hashCode();
        }
        if (getPoHeaderNO() != null) {
            _hashCode += getPoHeaderNO().hashCode();
        }
        if (getPoStatus() != null) {
            _hashCode += getPoStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UpdatePoStatusInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "UpdatePoStatusInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ORGID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "ORGID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("poHeaderID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "PoHeaderID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("poHeaderNO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "PoHeaderNO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("poStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "PoStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
