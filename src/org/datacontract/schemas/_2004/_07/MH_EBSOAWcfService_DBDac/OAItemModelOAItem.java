/**
 * OAItemModelOAItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac;

public class OAItemModelOAItem  implements java.io.Serializable {
    private org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelItem_Content[] item_content;

    public OAItemModelOAItem() {
    }

    public OAItemModelOAItem(
           org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelItem_Content[] item_content) {
           this.item_content = item_content;
    }


    /**
     * Gets the item_content value for this OAItemModelOAItem.
     * 
     * @return item_content
     */
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelItem_Content[] getItem_content() {
        return item_content;
    }


    /**
     * Sets the item_content value for this OAItemModelOAItem.
     * 
     * @param item_content
     */
    public void setItem_content(org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelItem_Content[] item_content) {
        this.item_content = item_content;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OAItemModelOAItem)) return false;
        OAItemModelOAItem other = (OAItemModelOAItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_content==null && other.getItem_content()==null) || 
             (this.item_content!=null &&
              java.util.Arrays.equals(this.item_content, other.getItem_content())));
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
        if (getItem_content() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getItem_content());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getItem_content(), i);
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
        new org.apache.axis.description.TypeDesc(OAItemModelOAItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "OAItemModel.OAItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_content");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "item_content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "OAItemModel.Item_Content"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "OAItemModel.Item_Content"));
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
