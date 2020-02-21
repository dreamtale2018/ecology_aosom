/**
 * Qctask.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService_OAModel;

public class Qctask  implements java.io.Serializable {
    private java.lang.String end_Date_Active;

    private java.lang.String group_id;

    private java.lang.String line_Num;

    private java.lang.String po_Number;

    private java.lang.String qc_Task_Number;

    private java.lang.String remarks;

    private java.lang.String SKU;

    private java.lang.String vendorName;

    public Qctask() {
    }

    public Qctask(
           java.lang.String end_Date_Active,
           java.lang.String group_id,
           java.lang.String line_Num,
           java.lang.String po_Number,
           java.lang.String qc_Task_Number,
           java.lang.String remarks,
           java.lang.String SKU,
           java.lang.String vendorName) {
           this.end_Date_Active = end_Date_Active;
           this.group_id = group_id;
           this.line_Num = line_Num;
           this.po_Number = po_Number;
           this.qc_Task_Number = qc_Task_Number;
           this.remarks = remarks;
           this.SKU = SKU;
           this.vendorName = vendorName;
    }


    /**
     * Gets the end_Date_Active value for this Qctask.
     * 
     * @return end_Date_Active
     */
    public java.lang.String getEnd_Date_Active() {
        return end_Date_Active;
    }


    /**
     * Sets the end_Date_Active value for this Qctask.
     * 
     * @param end_Date_Active
     */
    public void setEnd_Date_Active(java.lang.String end_Date_Active) {
        this.end_Date_Active = end_Date_Active;
    }


    /**
     * Gets the group_id value for this Qctask.
     * 
     * @return group_id
     */
    public java.lang.String getGroup_id() {
        return group_id;
    }


    /**
     * Sets the group_id value for this Qctask.
     * 
     * @param group_id
     */
    public void setGroup_id(java.lang.String group_id) {
        this.group_id = group_id;
    }


    /**
     * Gets the line_Num value for this Qctask.
     * 
     * @return line_Num
     */
    public java.lang.String getLine_Num() {
        return line_Num;
    }


    /**
     * Sets the line_Num value for this Qctask.
     * 
     * @param line_Num
     */
    public void setLine_Num(java.lang.String line_Num) {
        this.line_Num = line_Num;
    }


    /**
     * Gets the po_Number value for this Qctask.
     * 
     * @return po_Number
     */
    public java.lang.String getPo_Number() {
        return po_Number;
    }


    /**
     * Sets the po_Number value for this Qctask.
     * 
     * @param po_Number
     */
    public void setPo_Number(java.lang.String po_Number) {
        this.po_Number = po_Number;
    }


    /**
     * Gets the qc_Task_Number value for this Qctask.
     * 
     * @return qc_Task_Number
     */
    public java.lang.String getQc_Task_Number() {
        return qc_Task_Number;
    }


    /**
     * Sets the qc_Task_Number value for this Qctask.
     * 
     * @param qc_Task_Number
     */
    public void setQc_Task_Number(java.lang.String qc_Task_Number) {
        this.qc_Task_Number = qc_Task_Number;
    }


    /**
     * Gets the remarks value for this Qctask.
     * 
     * @return remarks
     */
    public java.lang.String getRemarks() {
        return remarks;
    }


    /**
     * Sets the remarks value for this Qctask.
     * 
     * @param remarks
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }


    /**
     * Gets the SKU value for this Qctask.
     * 
     * @return SKU
     */
    public java.lang.String getSKU() {
        return SKU;
    }


    /**
     * Sets the SKU value for this Qctask.
     * 
     * @param SKU
     */
    public void setSKU(java.lang.String SKU) {
        this.SKU = SKU;
    }


    /**
     * Gets the vendorName value for this Qctask.
     * 
     * @return vendorName
     */
    public java.lang.String getVendorName() {
        return vendorName;
    }


    /**
     * Sets the vendorName value for this Qctask.
     * 
     * @param vendorName
     */
    public void setVendorName(java.lang.String vendorName) {
        this.vendorName = vendorName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Qctask)) return false;
        Qctask other = (Qctask) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.end_Date_Active==null && other.getEnd_Date_Active()==null) || 
             (this.end_Date_Active!=null &&
              this.end_Date_Active.equals(other.getEnd_Date_Active()))) &&
            ((this.group_id==null && other.getGroup_id()==null) || 
             (this.group_id!=null &&
              this.group_id.equals(other.getGroup_id()))) &&
            ((this.line_Num==null && other.getLine_Num()==null) || 
             (this.line_Num!=null &&
              this.line_Num.equals(other.getLine_Num()))) &&
            ((this.po_Number==null && other.getPo_Number()==null) || 
             (this.po_Number!=null &&
              this.po_Number.equals(other.getPo_Number()))) &&
            ((this.qc_Task_Number==null && other.getQc_Task_Number()==null) || 
             (this.qc_Task_Number!=null &&
              this.qc_Task_Number.equals(other.getQc_Task_Number()))) &&
            ((this.remarks==null && other.getRemarks()==null) || 
             (this.remarks!=null &&
              this.remarks.equals(other.getRemarks()))) &&
            ((this.SKU==null && other.getSKU()==null) || 
             (this.SKU!=null &&
              this.SKU.equals(other.getSKU()))) &&
            ((this.vendorName==null && other.getVendorName()==null) || 
             (this.vendorName!=null &&
              this.vendorName.equals(other.getVendorName())));
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
        if (getEnd_Date_Active() != null) {
            _hashCode += getEnd_Date_Active().hashCode();
        }
        if (getGroup_id() != null) {
            _hashCode += getGroup_id().hashCode();
        }
        if (getLine_Num() != null) {
            _hashCode += getLine_Num().hashCode();
        }
        if (getPo_Number() != null) {
            _hashCode += getPo_Number().hashCode();
        }
        if (getQc_Task_Number() != null) {
            _hashCode += getQc_Task_Number().hashCode();
        }
        if (getRemarks() != null) {
            _hashCode += getRemarks().hashCode();
        }
        if (getSKU() != null) {
            _hashCode += getSKU().hashCode();
        }
        if (getVendorName() != null) {
            _hashCode += getVendorName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Qctask.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "Qctask"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("end_Date_Active");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "End_Date_Active"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "Group_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("line_Num");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "Line_Num"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("po_Number");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "Po_Number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qc_Task_Number");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "Qc_Task_Number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("remarks");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "Remarks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SKU");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "SKU"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.OAModel", "VendorName"));
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
