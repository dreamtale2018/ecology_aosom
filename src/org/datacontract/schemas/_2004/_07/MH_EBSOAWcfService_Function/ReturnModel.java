/**
 * ReturnModel.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService_Function;

public class ReturnModel  implements java.io.Serializable {
    private java.lang.String jsonID;

    private java.lang.String processMessage;

    private java.lang.String processNote;

    private java.lang.Boolean processStatus;

    private java.lang.String spldID;

    private java.lang.String workFlowID;

    public ReturnModel() {
    }

    public ReturnModel(
           java.lang.String jsonID,
           java.lang.String processMessage,
           java.lang.String processNote,
           java.lang.Boolean processStatus,
           java.lang.String spldID,
           java.lang.String workFlowID) {
           this.jsonID = jsonID;
           this.processMessage = processMessage;
           this.processNote = processNote;
           this.processStatus = processStatus;
           this.spldID = spldID;
           this.workFlowID = workFlowID;
    }


    /**
     * Gets the jsonID value for this ReturnModel.
     * 
     * @return jsonID
     */
    public java.lang.String getJsonID() {
        return jsonID;
    }


    /**
     * Sets the jsonID value for this ReturnModel.
     * 
     * @param jsonID
     */
    public void setJsonID(java.lang.String jsonID) {
        this.jsonID = jsonID;
    }


    /**
     * Gets the processMessage value for this ReturnModel.
     * 
     * @return processMessage
     */
    public java.lang.String getProcessMessage() {
        return processMessage;
    }


    /**
     * Sets the processMessage value for this ReturnModel.
     * 
     * @param processMessage
     */
    public void setProcessMessage(java.lang.String processMessage) {
        this.processMessage = processMessage;
    }


    /**
     * Gets the processNote value for this ReturnModel.
     * 
     * @return processNote
     */
    public java.lang.String getProcessNote() {
        return processNote;
    }


    /**
     * Sets the processNote value for this ReturnModel.
     * 
     * @param processNote
     */
    public void setProcessNote(java.lang.String processNote) {
        this.processNote = processNote;
    }


    /**
     * Gets the processStatus value for this ReturnModel.
     * 
     * @return processStatus
     */
    public java.lang.Boolean getProcessStatus() {
        return processStatus;
    }


    /**
     * Sets the processStatus value for this ReturnModel.
     * 
     * @param processStatus
     */
    public void setProcessStatus(java.lang.Boolean processStatus) {
        this.processStatus = processStatus;
    }


    /**
     * Gets the spldID value for this ReturnModel.
     * 
     * @return spldID
     */
    public java.lang.String getSpldID() {
        return spldID;
    }


    /**
     * Sets the spldID value for this ReturnModel.
     * 
     * @param spldID
     */
    public void setSpldID(java.lang.String spldID) {
        this.spldID = spldID;
    }


    /**
     * Gets the workFlowID value for this ReturnModel.
     * 
     * @return workFlowID
     */
    public java.lang.String getWorkFlowID() {
        return workFlowID;
    }


    /**
     * Sets the workFlowID value for this ReturnModel.
     * 
     * @param workFlowID
     */
    public void setWorkFlowID(java.lang.String workFlowID) {
        this.workFlowID = workFlowID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReturnModel)) return false;
        ReturnModel other = (ReturnModel) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.jsonID==null && other.getJsonID()==null) || 
             (this.jsonID!=null &&
              this.jsonID.equals(other.getJsonID()))) &&
            ((this.processMessage==null && other.getProcessMessage()==null) || 
             (this.processMessage!=null &&
              this.processMessage.equals(other.getProcessMessage()))) &&
            ((this.processNote==null && other.getProcessNote()==null) || 
             (this.processNote!=null &&
              this.processNote.equals(other.getProcessNote()))) &&
            ((this.processStatus==null && other.getProcessStatus()==null) || 
             (this.processStatus!=null &&
              this.processStatus.equals(other.getProcessStatus()))) &&
            ((this.spldID==null && other.getSpldID()==null) || 
             (this.spldID!=null &&
              this.spldID.equals(other.getSpldID()))) &&
            ((this.workFlowID==null && other.getWorkFlowID()==null) || 
             (this.workFlowID!=null &&
              this.workFlowID.equals(other.getWorkFlowID())));
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
        if (getJsonID() != null) {
            _hashCode += getJsonID().hashCode();
        }
        if (getProcessMessage() != null) {
            _hashCode += getProcessMessage().hashCode();
        }
        if (getProcessNote() != null) {
            _hashCode += getProcessNote().hashCode();
        }
        if (getProcessStatus() != null) {
            _hashCode += getProcessStatus().hashCode();
        }
        if (getSpldID() != null) {
            _hashCode += getSpldID().hashCode();
        }
        if (getWorkFlowID() != null) {
            _hashCode += getWorkFlowID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReturnModel.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.Function", "ReturnModel"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jsonID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.Function", "JsonID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.Function", "ProcessMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processNote");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.Function", "ProcessNote"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.Function", "ProcessStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spldID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.Function", "SpldID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workFlowID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.Function", "WorkFlowID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
