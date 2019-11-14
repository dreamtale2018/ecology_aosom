/**
 * UpdateStatusJson.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService;

public class UpdateStatusJson  implements java.io.Serializable {
    private java.lang.String workFlowID;

    private java.lang.String spldID;

    private java.lang.String jsonContent;

    private java.lang.String updateType;

    private java.lang.String primaryKey;

    public UpdateStatusJson() {
    }

    public UpdateStatusJson(
           java.lang.String workFlowID,
           java.lang.String spldID,
           java.lang.String jsonContent,
           java.lang.String updateType,
           java.lang.String primaryKey) {
           this.workFlowID = workFlowID;
           this.spldID = spldID;
           this.jsonContent = jsonContent;
           this.updateType = updateType;
           this.primaryKey = primaryKey;
    }


    /**
     * Gets the workFlowID value for this UpdateStatusJson.
     * 
     * @return workFlowID
     */
    public java.lang.String getWorkFlowID() {
        return workFlowID;
    }


    /**
     * Sets the workFlowID value for this UpdateStatusJson.
     * 
     * @param workFlowID
     */
    public void setWorkFlowID(java.lang.String workFlowID) {
        this.workFlowID = workFlowID;
    }


    /**
     * Gets the spldID value for this UpdateStatusJson.
     * 
     * @return spldID
     */
    public java.lang.String getSpldID() {
        return spldID;
    }


    /**
     * Sets the spldID value for this UpdateStatusJson.
     * 
     * @param spldID
     */
    public void setSpldID(java.lang.String spldID) {
        this.spldID = spldID;
    }


    /**
     * Gets the jsonContent value for this UpdateStatusJson.
     * 
     * @return jsonContent
     */
    public java.lang.String getJsonContent() {
        return jsonContent;
    }


    /**
     * Sets the jsonContent value for this UpdateStatusJson.
     * 
     * @param jsonContent
     */
    public void setJsonContent(java.lang.String jsonContent) {
        this.jsonContent = jsonContent;
    }


    /**
     * Gets the updateType value for this UpdateStatusJson.
     * 
     * @return updateType
     */
    public java.lang.String getUpdateType() {
        return updateType;
    }


    /**
     * Sets the updateType value for this UpdateStatusJson.
     * 
     * @param updateType
     */
    public void setUpdateType(java.lang.String updateType) {
        this.updateType = updateType;
    }


    /**
     * Gets the primaryKey value for this UpdateStatusJson.
     * 
     * @return primaryKey
     */
    public java.lang.String getPrimaryKey() {
        return primaryKey;
    }


    /**
     * Sets the primaryKey value for this UpdateStatusJson.
     * 
     * @param primaryKey
     */
    public void setPrimaryKey(java.lang.String primaryKey) {
        this.primaryKey = primaryKey;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UpdateStatusJson)) return false;
        UpdateStatusJson other = (UpdateStatusJson) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.workFlowID==null && other.getWorkFlowID()==null) || 
             (this.workFlowID!=null &&
              this.workFlowID.equals(other.getWorkFlowID()))) &&
            ((this.spldID==null && other.getSpldID()==null) || 
             (this.spldID!=null &&
              this.spldID.equals(other.getSpldID()))) &&
            ((this.jsonContent==null && other.getJsonContent()==null) || 
             (this.jsonContent!=null &&
              this.jsonContent.equals(other.getJsonContent()))) &&
            ((this.updateType==null && other.getUpdateType()==null) || 
             (this.updateType!=null &&
              this.updateType.equals(other.getUpdateType()))) &&
            ((this.primaryKey==null && other.getPrimaryKey()==null) || 
             (this.primaryKey!=null &&
              this.primaryKey.equals(other.getPrimaryKey())));
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
        if (getWorkFlowID() != null) {
            _hashCode += getWorkFlowID().hashCode();
        }
        if (getSpldID() != null) {
            _hashCode += getSpldID().hashCode();
        }
        if (getJsonContent() != null) {
            _hashCode += getJsonContent().hashCode();
        }
        if (getUpdateType() != null) {
            _hashCode += getUpdateType().hashCode();
        }
        if (getPrimaryKey() != null) {
            _hashCode += getPrimaryKey().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UpdateStatusJson.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "UpdateStatusJson"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workFlowID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "WorkFlowID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spldID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "SpldID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jsonContent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "JsonContent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updateType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "UpdateType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("primaryKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "PrimaryKey"));
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
