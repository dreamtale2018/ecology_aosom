/**
 * Electronic.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService;

public class Electronic  implements java.io.Serializable {
    private java.lang.String CUSTOMS_NO;

    private java.lang.String SUB_BL_NO;

    public Electronic() {
    }

    public Electronic(
           java.lang.String CUSTOMS_NO,
           java.lang.String SUB_BL_NO) {
           this.CUSTOMS_NO = CUSTOMS_NO;
           this.SUB_BL_NO = SUB_BL_NO;
    }


    /**
     * Gets the CUSTOMS_NO value for this Electronic.
     * 
     * @return CUSTOMS_NO
     */
    public java.lang.String getCUSTOMS_NO() {
        return CUSTOMS_NO;
    }


    /**
     * Sets the CUSTOMS_NO value for this Electronic.
     * 
     * @param CUSTOMS_NO
     */
    public void setCUSTOMS_NO(java.lang.String CUSTOMS_NO) {
        this.CUSTOMS_NO = CUSTOMS_NO;
    }


    /**
     * Gets the SUB_BL_NO value for this Electronic.
     * 
     * @return SUB_BL_NO
     */
    public java.lang.String getSUB_BL_NO() {
        return SUB_BL_NO;
    }


    /**
     * Sets the SUB_BL_NO value for this Electronic.
     * 
     * @param SUB_BL_NO
     */
    public void setSUB_BL_NO(java.lang.String SUB_BL_NO) {
        this.SUB_BL_NO = SUB_BL_NO;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Electronic)) return false;
        Electronic other = (Electronic) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CUSTOMS_NO==null && other.getCUSTOMS_NO()==null) || 
             (this.CUSTOMS_NO!=null &&
              this.CUSTOMS_NO.equals(other.getCUSTOMS_NO()))) &&
            ((this.SUB_BL_NO==null && other.getSUB_BL_NO()==null) || 
             (this.SUB_BL_NO!=null &&
              this.SUB_BL_NO.equals(other.getSUB_BL_NO())));
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
        if (getCUSTOMS_NO() != null) {
            _hashCode += getCUSTOMS_NO().hashCode();
        }
        if (getSUB_BL_NO() != null) {
            _hashCode += getSUB_BL_NO().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Electronic.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "Electronic"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CUSTOMS_NO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "CUSTOMS_NO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SUB_BL_NO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "SUB_BL_NO"));
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
