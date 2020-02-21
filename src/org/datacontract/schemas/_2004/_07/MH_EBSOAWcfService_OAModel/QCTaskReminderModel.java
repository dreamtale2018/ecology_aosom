/**
 * QCTaskReminderModel.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel;

public class QCTaskReminderModel  implements java.io.Serializable {
    private org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.Qctask[] QCTaskAll;

    public QCTaskReminderModel() {
    }

    public QCTaskReminderModel(
           org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.Qctask[] QCTaskAll) {
           this.QCTaskAll = QCTaskAll;
    }


    /**
     * Gets the QCTaskAll value for this QCTaskReminderModel.
     * 
     * @return QCTaskAll
     */
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.Qctask[] getQCTaskAll() {
        return QCTaskAll;
    }


    /**
     * Sets the QCTaskAll value for this QCTaskReminderModel.
     * 
     * @param QCTaskAll
     */
    public void setQCTaskAll(org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel.Qctask[] QCTaskAll) {
        this.QCTaskAll = QCTaskAll;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QCTaskReminderModel)) return false;
        QCTaskReminderModel other = (QCTaskReminderModel) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.QCTaskAll==null && other.getQCTaskAll()==null) || 
             (this.QCTaskAll!=null &&
              java.util.Arrays.equals(this.QCTaskAll, other.getQCTaskAll())));
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
        if (getQCTaskAll() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getQCTaskAll());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getQCTaskAll(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QCTaskReminderModel.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "QCTaskReminderModel"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("QCTaskAll");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "QCTaskAll"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "Qctask"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "Qctask"));
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
