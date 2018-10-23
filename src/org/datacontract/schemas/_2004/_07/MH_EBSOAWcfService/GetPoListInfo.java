/**
 * GetPoListInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService;

public class GetPoListInfo  implements java.io.Serializable {
    private java.lang.Integer ORGID;

    private java.lang.Integer poHeaderID;

    private java.lang.String poHeaderNO;

    private java.lang.Integer vendorID;

    private java.lang.String vendorName;

    private java.util.Calendar lastupdate_datetime;

    public GetPoListInfo() {
    }

    public GetPoListInfo(
           java.lang.Integer ORGID,
           java.lang.Integer poHeaderID,
           java.lang.String poHeaderNO,
           java.lang.Integer vendorID,
           java.lang.String vendorName,
           java.util.Calendar lastupdate_datetime) {
           this.ORGID = ORGID;
           this.poHeaderID = poHeaderID;
           this.poHeaderNO = poHeaderNO;
           this.vendorID = vendorID;
           this.vendorName = vendorName;
           this.lastupdate_datetime = lastupdate_datetime;
    }


    /**
     * Gets the ORGID value for this GetPoListInfo.
     * 
     * @return ORGID
     */
    public java.lang.Integer getORGID() {
        return ORGID;
    }


    /**
     * Sets the ORGID value for this GetPoListInfo.
     * 
     * @param ORGID
     */
    public void setORGID(java.lang.Integer ORGID) {
        this.ORGID = ORGID;
    }


    /**
     * Gets the poHeaderID value for this GetPoListInfo.
     * 
     * @return poHeaderID
     */
    public java.lang.Integer getPoHeaderID() {
        return poHeaderID;
    }


    /**
     * Sets the poHeaderID value for this GetPoListInfo.
     * 
     * @param poHeaderID
     */
    public void setPoHeaderID(java.lang.Integer poHeaderID) {
        this.poHeaderID = poHeaderID;
    }


    /**
     * Gets the poHeaderNO value for this GetPoListInfo.
     * 
     * @return poHeaderNO
     */
    public java.lang.String getPoHeaderNO() {
        return poHeaderNO;
    }


    /**
     * Sets the poHeaderNO value for this GetPoListInfo.
     * 
     * @param poHeaderNO
     */
    public void setPoHeaderNO(java.lang.String poHeaderNO) {
        this.poHeaderNO = poHeaderNO;
    }


    /**
     * Gets the vendorID value for this GetPoListInfo.
     * 
     * @return vendorID
     */
    public java.lang.Integer getVendorID() {
        return vendorID;
    }


    /**
     * Sets the vendorID value for this GetPoListInfo.
     * 
     * @param vendorID
     */
    public void setVendorID(java.lang.Integer vendorID) {
        this.vendorID = vendorID;
    }


    /**
     * Gets the vendorName value for this GetPoListInfo.
     * 
     * @return vendorName
     */
    public java.lang.String getVendorName() {
        return vendorName;
    }


    /**
     * Sets the vendorName value for this GetPoListInfo.
     * 
     * @param vendorName
     */
    public void setVendorName(java.lang.String vendorName) {
        this.vendorName = vendorName;
    }


    /**
     * Gets the lastupdate_datetime value for this GetPoListInfo.
     * 
     * @return lastupdate_datetime
     */
    public java.util.Calendar getLastupdate_datetime() {
        return lastupdate_datetime;
    }


    /**
     * Sets the lastupdate_datetime value for this GetPoListInfo.
     * 
     * @param lastupdate_datetime
     */
    public void setLastupdate_datetime(java.util.Calendar lastupdate_datetime) {
        this.lastupdate_datetime = lastupdate_datetime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetPoListInfo)) return false;
        GetPoListInfo other = (GetPoListInfo) obj;
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
            ((this.poHeaderID==null && other.getPoHeaderID()==null) || 
             (this.poHeaderID!=null &&
              this.poHeaderID.equals(other.getPoHeaderID()))) &&
            ((this.poHeaderNO==null && other.getPoHeaderNO()==null) || 
             (this.poHeaderNO!=null &&
              this.poHeaderNO.equals(other.getPoHeaderNO()))) &&
            ((this.vendorID==null && other.getVendorID()==null) || 
             (this.vendorID!=null &&
              this.vendorID.equals(other.getVendorID()))) &&
            ((this.vendorName==null && other.getVendorName()==null) || 
             (this.vendorName!=null &&
              this.vendorName.equals(other.getVendorName()))) &&
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
        if (getORGID() != null) {
            _hashCode += getORGID().hashCode();
        }
        if (getPoHeaderID() != null) {
            _hashCode += getPoHeaderID().hashCode();
        }
        if (getPoHeaderNO() != null) {
            _hashCode += getPoHeaderNO().hashCode();
        }
        if (getVendorID() != null) {
            _hashCode += getVendorID().hashCode();
        }
        if (getVendorName() != null) {
            _hashCode += getVendorName().hashCode();
        }
        if (getLastupdate_datetime() != null) {
            _hashCode += getLastupdate_datetime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetPoListInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "GetPoListInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ORGID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "ORGID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("poHeaderID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "PoHeaderID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("poHeaderNO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "PoHeaderNO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "VendorID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService", "VendorName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
