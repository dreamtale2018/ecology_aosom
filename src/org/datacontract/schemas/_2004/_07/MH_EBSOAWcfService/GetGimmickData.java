/**
 * GetGimmickData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService;

public class GetGimmickData  implements java.io.Serializable {
    private java.util.Calendar lastupdate_datetime;

    public GetGimmickData() {
    }

    public GetGimmickData(
           java.util.Calendar lastupdate_datetime) {
           this.lastupdate_datetime = lastupdate_datetime;
    }


    /**
     * Gets the lastupdate_datetime value for this GetGimmickData.
     * 
     * @return lastupdate_datetime
     */
    public java.util.Calendar getLastupdate_datetime() {
        return lastupdate_datetime;
    }


    /**
     * Sets the lastupdate_datetime value for this GetGimmickData.
     * 
     * @param lastupdate_datetime
     */
    public void setLastupdate_datetime(java.util.Calendar lastupdate_datetime) {
        this.lastupdate_datetime = lastupdate_datetime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetGimmickData)) return false;
        GetGimmickData other = (GetGimmickData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
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
        if (getLastupdate_datetime() != null) {
            _hashCode += getLastupdate_datetime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetGimmickData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "GetGimmickData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
