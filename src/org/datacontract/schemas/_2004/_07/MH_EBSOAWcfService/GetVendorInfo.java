/**
 * GetVendorInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService;

public class GetVendorInfo  implements java.io.Serializable {
    private java.lang.Integer vendorID;

    private java.lang.String vendorName;

    private java.util.Calendar lastupdate_datetime;

    private java.lang.String operater;

    private java.util.Calendar operate_Date;

    public GetVendorInfo() {
    }

    public GetVendorInfo(
           java.lang.Integer vendorID,
           java.lang.String vendorName,
           java.util.Calendar lastupdate_datetime,
           java.lang.String operater,
           java.util.Calendar operate_Date) {
           this.vendorID = vendorID;
           this.vendorName = vendorName;
           this.lastupdate_datetime = lastupdate_datetime;
           this.operater = operater;
           this.operate_Date = operate_Date;
    }


    /**
     * Gets the vendorID value for this GetVendorInfo.
     * 
     * @return vendorID
     */
    public java.lang.Integer getVendorID() {
        return vendorID;
    }


    /**
     * Sets the vendorID value for this GetVendorInfo.
     * 
     * @param vendorID
     */
    public void setVendorID(java.lang.Integer vendorID) {
        this.vendorID = vendorID;
    }


    /**
     * Gets the vendorName value for this GetVendorInfo.
     * 
     * @return vendorName
     */
    public java.lang.String getVendorName() {
        return vendorName;
    }


    /**
     * Sets the vendorName value for this GetVendorInfo.
     * 
     * @param vendorName
     */
    public void setVendorName(java.lang.String vendorName) {
        this.vendorName = vendorName;
    }


    /**
     * Gets the lastupdate_datetime value for this GetVendorInfo.
     * 
     * @return lastupdate_datetime
     */
    public java.util.Calendar getLastupdate_datetime() {
        return lastupdate_datetime;
    }


    /**
     * Sets the lastupdate_datetime value for this GetVendorInfo.
     * 
     * @param lastupdate_datetime
     */
    public void setLastupdate_datetime(java.util.Calendar lastupdate_datetime) {
        this.lastupdate_datetime = lastupdate_datetime;
    }


    /**
     * Gets the operater value for this GetVendorInfo.
     * 
     * @return operater
     */
    public java.lang.String getOperater() {
        return operater;
    }


    /**
     * Sets the operater value for this GetVendorInfo.
     * 
     * @param operater
     */
    public void setOperater(java.lang.String operater) {
        this.operater = operater;
    }


    /**
     * Gets the operate_Date value for this GetVendorInfo.
     * 
     * @return operate_Date
     */
    public java.util.Calendar getOperate_Date() {
        return operate_Date;
    }


    /**
     * Sets the operate_Date value for this GetVendorInfo.
     * 
     * @param operate_Date
     */
    public void setOperate_Date(java.util.Calendar operate_Date) {
        this.operate_Date = operate_Date;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetVendorInfo)) return false;
        GetVendorInfo other = (GetVendorInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.vendorID==null && other.getVendorID()==null) || 
             (this.vendorID!=null &&
              this.vendorID.equals(other.getVendorID()))) &&
            ((this.vendorName==null && other.getVendorName()==null) || 
             (this.vendorName!=null &&
              this.vendorName.equals(other.getVendorName()))) &&
            ((this.lastupdate_datetime==null && other.getLastupdate_datetime()==null) || 
             (this.lastupdate_datetime!=null &&
              this.lastupdate_datetime.equals(other.getLastupdate_datetime()))) &&
            ((this.operater==null && other.getOperater()==null) || 
             (this.operater!=null &&
              this.operater.equals(other.getOperater()))) &&
            ((this.operate_Date==null && other.getOperate_Date()==null) || 
             (this.operate_Date!=null &&
              this.operate_Date.equals(other.getOperate_Date())));
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
        if (getVendorID() != null) {
            _hashCode += getVendorID().hashCode();
        }
        if (getVendorName() != null) {
            _hashCode += getVendorName().hashCode();
        }
        if (getLastupdate_datetime() != null) {
            _hashCode += getLastupdate_datetime().hashCode();
        }
        if (getOperater() != null) {
            _hashCode += getOperater().hashCode();
        }
        if (getOperate_Date() != null) {
            _hashCode += getOperate_Date().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetVendorInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "GetVendorInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "VendorID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "VendorName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operater");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "Operater"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operate_Date");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "Operate_Date"));
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
