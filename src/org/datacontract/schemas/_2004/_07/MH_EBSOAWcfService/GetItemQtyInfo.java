/**
 * GetItemQtyInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService;

public class GetItemQtyInfo  implements java.io.Serializable {
    private java.lang.Integer ORGID;

    private java.lang.Integer standardItemID;

    private java.lang.String productID;

    private java.lang.Integer inventoryItemID;

    private java.lang.String operater;

    private java.util.Calendar operate_Date;

    public GetItemQtyInfo() {
    }

    public GetItemQtyInfo(
           java.lang.Integer ORGID,
           java.lang.Integer standardItemID,
           java.lang.String productID,
           java.lang.Integer inventoryItemID,
           java.lang.String operater,
           java.util.Calendar operate_Date) {
           this.ORGID = ORGID;
           this.standardItemID = standardItemID;
           this.productID = productID;
           this.inventoryItemID = inventoryItemID;
           this.operater = operater;
           this.operate_Date = operate_Date;
    }


    /**
     * Gets the ORGID value for this GetItemQtyInfo.
     * 
     * @return ORGID
     */
    public java.lang.Integer getORGID() {
        return ORGID;
    }


    /**
     * Sets the ORGID value for this GetItemQtyInfo.
     * 
     * @param ORGID
     */
    public void setORGID(java.lang.Integer ORGID) {
        this.ORGID = ORGID;
    }


    /**
     * Gets the standardItemID value for this GetItemQtyInfo.
     * 
     * @return standardItemID
     */
    public java.lang.Integer getStandardItemID() {
        return standardItemID;
    }


    /**
     * Sets the standardItemID value for this GetItemQtyInfo.
     * 
     * @param standardItemID
     */
    public void setStandardItemID(java.lang.Integer standardItemID) {
        this.standardItemID = standardItemID;
    }


    /**
     * Gets the productID value for this GetItemQtyInfo.
     * 
     * @return productID
     */
    public java.lang.String getProductID() {
        return productID;
    }


    /**
     * Sets the productID value for this GetItemQtyInfo.
     * 
     * @param productID
     */
    public void setProductID(java.lang.String productID) {
        this.productID = productID;
    }


    /**
     * Gets the inventoryItemID value for this GetItemQtyInfo.
     * 
     * @return inventoryItemID
     */
    public java.lang.Integer getInventoryItemID() {
        return inventoryItemID;
    }


    /**
     * Sets the inventoryItemID value for this GetItemQtyInfo.
     * 
     * @param inventoryItemID
     */
    public void setInventoryItemID(java.lang.Integer inventoryItemID) {
        this.inventoryItemID = inventoryItemID;
    }


    /**
     * Gets the operater value for this GetItemQtyInfo.
     * 
     * @return operater
     */
    public java.lang.String getOperater() {
        return operater;
    }


    /**
     * Sets the operater value for this GetItemQtyInfo.
     * 
     * @param operater
     */
    public void setOperater(java.lang.String operater) {
        this.operater = operater;
    }


    /**
     * Gets the operate_Date value for this GetItemQtyInfo.
     * 
     * @return operate_Date
     */
    public java.util.Calendar getOperate_Date() {
        return operate_Date;
    }


    /**
     * Sets the operate_Date value for this GetItemQtyInfo.
     * 
     * @param operate_Date
     */
    public void setOperate_Date(java.util.Calendar operate_Date) {
        this.operate_Date = operate_Date;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetItemQtyInfo)) return false;
        GetItemQtyInfo other = (GetItemQtyInfo) obj;
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
            ((this.standardItemID==null && other.getStandardItemID()==null) || 
             (this.standardItemID!=null &&
              this.standardItemID.equals(other.getStandardItemID()))) &&
            ((this.productID==null && other.getProductID()==null) || 
             (this.productID!=null &&
              this.productID.equals(other.getProductID()))) &&
            ((this.inventoryItemID==null && other.getInventoryItemID()==null) || 
             (this.inventoryItemID!=null &&
              this.inventoryItemID.equals(other.getInventoryItemID()))) &&
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
        if (getORGID() != null) {
            _hashCode += getORGID().hashCode();
        }
        if (getStandardItemID() != null) {
            _hashCode += getStandardItemID().hashCode();
        }
        if (getProductID() != null) {
            _hashCode += getProductID().hashCode();
        }
        if (getInventoryItemID() != null) {
            _hashCode += getInventoryItemID().hashCode();
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
        new org.apache.axis.description.TypeDesc(GetItemQtyInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "GetItemQtyInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ORGID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "ORGID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("standardItemID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "StandardItemID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("productID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "ProductID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inventoryItemID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "InventoryItemID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
