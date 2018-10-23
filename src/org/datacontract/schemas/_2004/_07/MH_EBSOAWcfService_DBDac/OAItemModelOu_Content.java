/**
 * OAItemModelOu_Content.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac;

public class OAItemModelOu_Content  implements java.io.Serializable {
    private java.lang.String brand_code;

    private java.lang.String ou_name;

    public OAItemModelOu_Content() {
    }

    public OAItemModelOu_Content(
           java.lang.String brand_code,
           java.lang.String ou_name) {
           this.brand_code = brand_code;
           this.ou_name = ou_name;
    }


    /**
     * Gets the brand_code value for this OAItemModelOu_Content.
     * 
     * @return brand_code
     */
    public java.lang.String getBrand_code() {
        return brand_code;
    }


    /**
     * Sets the brand_code value for this OAItemModelOu_Content.
     * 
     * @param brand_code
     */
    public void setBrand_code(java.lang.String brand_code) {
        this.brand_code = brand_code;
    }


    /**
     * Gets the ou_name value for this OAItemModelOu_Content.
     * 
     * @return ou_name
     */
    public java.lang.String getOu_name() {
        return ou_name;
    }


    /**
     * Sets the ou_name value for this OAItemModelOu_Content.
     * 
     * @param ou_name
     */
    public void setOu_name(java.lang.String ou_name) {
        this.ou_name = ou_name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OAItemModelOu_Content)) return false;
        OAItemModelOu_Content other = (OAItemModelOu_Content) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.brand_code==null && other.getBrand_code()==null) || 
             (this.brand_code!=null &&
              this.brand_code.equals(other.getBrand_code()))) &&
            ((this.ou_name==null && other.getOu_name()==null) || 
             (this.ou_name!=null &&
              this.ou_name.equals(other.getOu_name())));
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
        if (getBrand_code() != null) {
            _hashCode += getBrand_code().hashCode();
        }
        if (getOu_name() != null) {
            _hashCode += getOu_name().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OAItemModelOu_Content.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "OAItemModel.Ou_Content"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("brand_code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "brand_code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ou_name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "ou_name"));
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
