/**
 * UpdateOAApprovalStatusInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_ERPOAWcfService;

public class UpdateOAApprovalStatusInfo  implements java.io.Serializable {
    private java.lang.String type;

    private java.lang.String primaryValue;

    private java.lang.String OAApprovalStatus;

    private java.lang.String OAApprovalUserName;

    private java.lang.String name;

    private java.lang.String pwd;

    public UpdateOAApprovalStatusInfo() {
    }

    public UpdateOAApprovalStatusInfo(
           java.lang.String type,
           java.lang.String primaryValue,
           java.lang.String OAApprovalStatus,
           java.lang.String OAApprovalUserName,
           java.lang.String name,
           java.lang.String pwd) {
           this.type = type;
           this.primaryValue = primaryValue;
           this.OAApprovalStatus = OAApprovalStatus;
           this.OAApprovalUserName = OAApprovalUserName;
           this.name = name;
           this.pwd = pwd;
    }


    /**
     * Gets the type value for this UpdateOAApprovalStatusInfo.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this UpdateOAApprovalStatusInfo.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the primaryValue value for this UpdateOAApprovalStatusInfo.
     * 
     * @return primaryValue
     */
    public java.lang.String getPrimaryValue() {
        return primaryValue;
    }


    /**
     * Sets the primaryValue value for this UpdateOAApprovalStatusInfo.
     * 
     * @param primaryValue
     */
    public void setPrimaryValue(java.lang.String primaryValue) {
        this.primaryValue = primaryValue;
    }


    /**
     * Gets the OAApprovalStatus value for this UpdateOAApprovalStatusInfo.
     * 
     * @return OAApprovalStatus
     */
    public java.lang.String getOAApprovalStatus() {
        return OAApprovalStatus;
    }


    /**
     * Sets the OAApprovalStatus value for this UpdateOAApprovalStatusInfo.
     * 
     * @param OAApprovalStatus
     */
    public void setOAApprovalStatus(java.lang.String OAApprovalStatus) {
        this.OAApprovalStatus = OAApprovalStatus;
    }


    /**
     * Gets the OAApprovalUserName value for this UpdateOAApprovalStatusInfo.
     * 
     * @return OAApprovalUserName
     */
    public java.lang.String getOAApprovalUserName() {
        return OAApprovalUserName;
    }


    /**
     * Sets the OAApprovalUserName value for this UpdateOAApprovalStatusInfo.
     * 
     * @param OAApprovalUserName
     */
    public void setOAApprovalUserName(java.lang.String OAApprovalUserName) {
        this.OAApprovalUserName = OAApprovalUserName;
    }


    /**
     * Gets the name value for this UpdateOAApprovalStatusInfo.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this UpdateOAApprovalStatusInfo.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the pwd value for this UpdateOAApprovalStatusInfo.
     * 
     * @return pwd
     */
    public java.lang.String getPwd() {
        return pwd;
    }


    /**
     * Sets the pwd value for this UpdateOAApprovalStatusInfo.
     * 
     * @param pwd
     */
    public void setPwd(java.lang.String pwd) {
        this.pwd = pwd;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UpdateOAApprovalStatusInfo)) return false;
        UpdateOAApprovalStatusInfo other = (UpdateOAApprovalStatusInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.primaryValue==null && other.getPrimaryValue()==null) || 
             (this.primaryValue!=null &&
              this.primaryValue.equals(other.getPrimaryValue()))) &&
            ((this.OAApprovalStatus==null && other.getOAApprovalStatus()==null) || 
             (this.OAApprovalStatus!=null &&
              this.OAApprovalStatus.equals(other.getOAApprovalStatus()))) &&
            ((this.OAApprovalUserName==null && other.getOAApprovalUserName()==null) || 
             (this.OAApprovalUserName!=null &&
              this.OAApprovalUserName.equals(other.getOAApprovalUserName()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.pwd==null && other.getPwd()==null) || 
             (this.pwd!=null &&
              this.pwd.equals(other.getPwd())));
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
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getPrimaryValue() != null) {
            _hashCode += getPrimaryValue().hashCode();
        }
        if (getOAApprovalStatus() != null) {
            _hashCode += getOAApprovalStatus().hashCode();
        }
        if (getOAApprovalUserName() != null) {
            _hashCode += getOAApprovalUserName().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getPwd() != null) {
            _hashCode += getPwd().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UpdateOAApprovalStatusInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.ERPOAWcfService", "UpdateOAApprovalStatusInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.ERPOAWcfService", "Type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("primaryValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.ERPOAWcfService", "PrimaryValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("OAApprovalStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.ERPOAWcfService", "OAApprovalStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("OAApprovalUserName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.ERPOAWcfService", "OAApprovalUserName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.ERPOAWcfService", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pwd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.ERPOAWcfService", "Pwd"));
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
