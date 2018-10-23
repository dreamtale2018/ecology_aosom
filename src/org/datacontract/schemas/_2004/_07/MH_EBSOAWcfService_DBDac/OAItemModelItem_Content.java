/**
 * OAItemModelItem_Content.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac;

public class OAItemModelItem_Content  implements java.io.Serializable {
    private org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelBom_Content[] bom_content;

    private org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelBox_Content[] box_content;

    private java.lang.String category_code;

    private java.lang.String color;

    private java.lang.String declared_item_name_cn;

    private java.lang.String developer_olduser;

    private java.lang.String developer_user;

    private java.lang.String factory_number;

    private java.lang.String hs_code;

    private java.lang.String include_bom_flag;

    private java.lang.String include_package_flag;

    private java.lang.String investment_item;

    private java.lang.String item_attached1;

    private java.lang.String item_attached2;

    private java.lang.String item_desc;

    private java.lang.String item_name_cn;

    private java.lang.String item_name_us;

    private java.lang.String item_specification_cn;

    private java.lang.String item_specification_us;

    private java.lang.String item_version;

    private java.lang.String item_version_enable;

    private java.lang.String oa_item_code;

    private java.math.BigDecimal oa_item_line_seq;

    private org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelOu_Content[] ou_content;

    private java.lang.String primary_uom_code;

    private java.lang.String product_id;

    private java.lang.String selling_point;

    private java.lang.String serial_number;

    public OAItemModelItem_Content() {
    }

    public OAItemModelItem_Content(
           org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelBom_Content[] bom_content,
           org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelBox_Content[] box_content,
           java.lang.String category_code,
           java.lang.String color,
           java.lang.String declared_item_name_cn,
           java.lang.String developer_olduser,
           java.lang.String developer_user,
           java.lang.String factory_number,
           java.lang.String hs_code,
           java.lang.String include_bom_flag,
           java.lang.String include_package_flag,
           java.lang.String investment_item,
           java.lang.String item_attached1,
           java.lang.String item_attached2,
           java.lang.String item_desc,
           java.lang.String item_name_cn,
           java.lang.String item_name_us,
           java.lang.String item_specification_cn,
           java.lang.String item_specification_us,
           java.lang.String item_version,
           java.lang.String item_version_enable,
           java.lang.String oa_item_code,
           java.math.BigDecimal oa_item_line_seq,
           org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelOu_Content[] ou_content,
           java.lang.String primary_uom_code,
           java.lang.String product_id,
           java.lang.String selling_point,
           java.lang.String serial_number) {
           this.bom_content = bom_content;
           this.box_content = box_content;
           this.category_code = category_code;
           this.color = color;
           this.declared_item_name_cn = declared_item_name_cn;
           this.developer_olduser = developer_olduser;
           this.developer_user = developer_user;
           this.factory_number = factory_number;
           this.hs_code = hs_code;
           this.include_bom_flag = include_bom_flag;
           this.include_package_flag = include_package_flag;
           this.investment_item = investment_item;
           this.item_attached1 = item_attached1;
           this.item_attached2 = item_attached2;
           this.item_desc = item_desc;
           this.item_name_cn = item_name_cn;
           this.item_name_us = item_name_us;
           this.item_specification_cn = item_specification_cn;
           this.item_specification_us = item_specification_us;
           this.item_version = item_version;
           this.item_version_enable = item_version_enable;
           this.oa_item_code = oa_item_code;
           this.oa_item_line_seq = oa_item_line_seq;
           this.ou_content = ou_content;
           this.primary_uom_code = primary_uom_code;
           this.product_id = product_id;
           this.selling_point = selling_point;
           this.serial_number = serial_number;
    }


    /**
     * Gets the bom_content value for this OAItemModelItem_Content.
     * 
     * @return bom_content
     */
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelBom_Content[] getBom_content() {
        return bom_content;
    }


    /**
     * Sets the bom_content value for this OAItemModelItem_Content.
     * 
     * @param bom_content
     */
    public void setBom_content(org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelBom_Content[] bom_content) {
        this.bom_content = bom_content;
    }


    /**
     * Gets the box_content value for this OAItemModelItem_Content.
     * 
     * @return box_content
     */
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelBox_Content[] getBox_content() {
        return box_content;
    }


    /**
     * Sets the box_content value for this OAItemModelItem_Content.
     * 
     * @param box_content
     */
    public void setBox_content(org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelBox_Content[] box_content) {
        this.box_content = box_content;
    }


    /**
     * Gets the category_code value for this OAItemModelItem_Content.
     * 
     * @return category_code
     */
    public java.lang.String getCategory_code() {
        return category_code;
    }


    /**
     * Sets the category_code value for this OAItemModelItem_Content.
     * 
     * @param category_code
     */
    public void setCategory_code(java.lang.String category_code) {
        this.category_code = category_code;
    }


    /**
     * Gets the color value for this OAItemModelItem_Content.
     * 
     * @return color
     */
    public java.lang.String getColor() {
        return color;
    }


    /**
     * Sets the color value for this OAItemModelItem_Content.
     * 
     * @param color
     */
    public void setColor(java.lang.String color) {
        this.color = color;
    }


    /**
     * Gets the declared_item_name_cn value for this OAItemModelItem_Content.
     * 
     * @return declared_item_name_cn
     */
    public java.lang.String getDeclared_item_name_cn() {
        return declared_item_name_cn;
    }


    /**
     * Sets the declared_item_name_cn value for this OAItemModelItem_Content.
     * 
     * @param declared_item_name_cn
     */
    public void setDeclared_item_name_cn(java.lang.String declared_item_name_cn) {
        this.declared_item_name_cn = declared_item_name_cn;
    }


    /**
     * Gets the developer_olduser value for this OAItemModelItem_Content.
     * 
     * @return developer_olduser
     */
    public java.lang.String getDeveloper_olduser() {
        return developer_olduser;
    }


    /**
     * Sets the developer_olduser value for this OAItemModelItem_Content.
     * 
     * @param developer_olduser
     */
    public void setDeveloper_olduser(java.lang.String developer_olduser) {
        this.developer_olduser = developer_olduser;
    }


    /**
     * Gets the developer_user value for this OAItemModelItem_Content.
     * 
     * @return developer_user
     */
    public java.lang.String getDeveloper_user() {
        return developer_user;
    }


    /**
     * Sets the developer_user value for this OAItemModelItem_Content.
     * 
     * @param developer_user
     */
    public void setDeveloper_user(java.lang.String developer_user) {
        this.developer_user = developer_user;
    }


    /**
     * Gets the factory_number value for this OAItemModelItem_Content.
     * 
     * @return factory_number
     */
    public java.lang.String getFactory_number() {
        return factory_number;
    }


    /**
     * Sets the factory_number value for this OAItemModelItem_Content.
     * 
     * @param factory_number
     */
    public void setFactory_number(java.lang.String factory_number) {
        this.factory_number = factory_number;
    }


    /**
     * Gets the hs_code value for this OAItemModelItem_Content.
     * 
     * @return hs_code
     */
    public java.lang.String getHs_code() {
        return hs_code;
    }


    /**
     * Sets the hs_code value for this OAItemModelItem_Content.
     * 
     * @param hs_code
     */
    public void setHs_code(java.lang.String hs_code) {
        this.hs_code = hs_code;
    }


    /**
     * Gets the include_bom_flag value for this OAItemModelItem_Content.
     * 
     * @return include_bom_flag
     */
    public java.lang.String getInclude_bom_flag() {
        return include_bom_flag;
    }


    /**
     * Sets the include_bom_flag value for this OAItemModelItem_Content.
     * 
     * @param include_bom_flag
     */
    public void setInclude_bom_flag(java.lang.String include_bom_flag) {
        this.include_bom_flag = include_bom_flag;
    }


    /**
     * Gets the include_package_flag value for this OAItemModelItem_Content.
     * 
     * @return include_package_flag
     */
    public java.lang.String getInclude_package_flag() {
        return include_package_flag;
    }


    /**
     * Sets the include_package_flag value for this OAItemModelItem_Content.
     * 
     * @param include_package_flag
     */
    public void setInclude_package_flag(java.lang.String include_package_flag) {
        this.include_package_flag = include_package_flag;
    }


    /**
     * Gets the investment_item value for this OAItemModelItem_Content.
     * 
     * @return investment_item
     */
    public java.lang.String getInvestment_item() {
        return investment_item;
    }


    /**
     * Sets the investment_item value for this OAItemModelItem_Content.
     * 
     * @param investment_item
     */
    public void setInvestment_item(java.lang.String investment_item) {
        this.investment_item = investment_item;
    }


    /**
     * Gets the item_attached1 value for this OAItemModelItem_Content.
     * 
     * @return item_attached1
     */
    public java.lang.String getItem_attached1() {
        return item_attached1;
    }


    /**
     * Sets the item_attached1 value for this OAItemModelItem_Content.
     * 
     * @param item_attached1
     */
    public void setItem_attached1(java.lang.String item_attached1) {
        this.item_attached1 = item_attached1;
    }


    /**
     * Gets the item_attached2 value for this OAItemModelItem_Content.
     * 
     * @return item_attached2
     */
    public java.lang.String getItem_attached2() {
        return item_attached2;
    }


    /**
     * Sets the item_attached2 value for this OAItemModelItem_Content.
     * 
     * @param item_attached2
     */
    public void setItem_attached2(java.lang.String item_attached2) {
        this.item_attached2 = item_attached2;
    }


    /**
     * Gets the item_desc value for this OAItemModelItem_Content.
     * 
     * @return item_desc
     */
    public java.lang.String getItem_desc() {
        return item_desc;
    }


    /**
     * Sets the item_desc value for this OAItemModelItem_Content.
     * 
     * @param item_desc
     */
    public void setItem_desc(java.lang.String item_desc) {
        this.item_desc = item_desc;
    }


    /**
     * Gets the item_name_cn value for this OAItemModelItem_Content.
     * 
     * @return item_name_cn
     */
    public java.lang.String getItem_name_cn() {
        return item_name_cn;
    }


    /**
     * Sets the item_name_cn value for this OAItemModelItem_Content.
     * 
     * @param item_name_cn
     */
    public void setItem_name_cn(java.lang.String item_name_cn) {
        this.item_name_cn = item_name_cn;
    }


    /**
     * Gets the item_name_us value for this OAItemModelItem_Content.
     * 
     * @return item_name_us
     */
    public java.lang.String getItem_name_us() {
        return item_name_us;
    }


    /**
     * Sets the item_name_us value for this OAItemModelItem_Content.
     * 
     * @param item_name_us
     */
    public void setItem_name_us(java.lang.String item_name_us) {
        this.item_name_us = item_name_us;
    }


    /**
     * Gets the item_specification_cn value for this OAItemModelItem_Content.
     * 
     * @return item_specification_cn
     */
    public java.lang.String getItem_specification_cn() {
        return item_specification_cn;
    }


    /**
     * Sets the item_specification_cn value for this OAItemModelItem_Content.
     * 
     * @param item_specification_cn
     */
    public void setItem_specification_cn(java.lang.String item_specification_cn) {
        this.item_specification_cn = item_specification_cn;
    }


    /**
     * Gets the item_specification_us value for this OAItemModelItem_Content.
     * 
     * @return item_specification_us
     */
    public java.lang.String getItem_specification_us() {
        return item_specification_us;
    }


    /**
     * Sets the item_specification_us value for this OAItemModelItem_Content.
     * 
     * @param item_specification_us
     */
    public void setItem_specification_us(java.lang.String item_specification_us) {
        this.item_specification_us = item_specification_us;
    }


    /**
     * Gets the item_version value for this OAItemModelItem_Content.
     * 
     * @return item_version
     */
    public java.lang.String getItem_version() {
        return item_version;
    }


    /**
     * Sets the item_version value for this OAItemModelItem_Content.
     * 
     * @param item_version
     */
    public void setItem_version(java.lang.String item_version) {
        this.item_version = item_version;
    }


    /**
     * Gets the item_version_enable value for this OAItemModelItem_Content.
     * 
     * @return item_version_enable
     */
    public java.lang.String getItem_version_enable() {
        return item_version_enable;
    }


    /**
     * Sets the item_version_enable value for this OAItemModelItem_Content.
     * 
     * @param item_version_enable
     */
    public void setItem_version_enable(java.lang.String item_version_enable) {
        this.item_version_enable = item_version_enable;
    }


    /**
     * Gets the oa_item_code value for this OAItemModelItem_Content.
     * 
     * @return oa_item_code
     */
    public java.lang.String getOa_item_code() {
        return oa_item_code;
    }


    /**
     * Sets the oa_item_code value for this OAItemModelItem_Content.
     * 
     * @param oa_item_code
     */
    public void setOa_item_code(java.lang.String oa_item_code) {
        this.oa_item_code = oa_item_code;
    }


    /**
     * Gets the oa_item_line_seq value for this OAItemModelItem_Content.
     * 
     * @return oa_item_line_seq
     */
    public java.math.BigDecimal getOa_item_line_seq() {
        return oa_item_line_seq;
    }


    /**
     * Sets the oa_item_line_seq value for this OAItemModelItem_Content.
     * 
     * @param oa_item_line_seq
     */
    public void setOa_item_line_seq(java.math.BigDecimal oa_item_line_seq) {
        this.oa_item_line_seq = oa_item_line_seq;
    }


    /**
     * Gets the ou_content value for this OAItemModelItem_Content.
     * 
     * @return ou_content
     */
    public org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelOu_Content[] getOu_content() {
        return ou_content;
    }


    /**
     * Sets the ou_content value for this OAItemModelItem_Content.
     * 
     * @param ou_content
     */
    public void setOu_content(org.datacontract.schemas._2004._07.MH_EBSOAWcfService_DBDac.OAItemModelOu_Content[] ou_content) {
        this.ou_content = ou_content;
    }


    /**
     * Gets the primary_uom_code value for this OAItemModelItem_Content.
     * 
     * @return primary_uom_code
     */
    public java.lang.String getPrimary_uom_code() {
        return primary_uom_code;
    }


    /**
     * Sets the primary_uom_code value for this OAItemModelItem_Content.
     * 
     * @param primary_uom_code
     */
    public void setPrimary_uom_code(java.lang.String primary_uom_code) {
        this.primary_uom_code = primary_uom_code;
    }


    /**
     * Gets the product_id value for this OAItemModelItem_Content.
     * 
     * @return product_id
     */
    public java.lang.String getProduct_id() {
        return product_id;
    }


    /**
     * Sets the product_id value for this OAItemModelItem_Content.
     * 
     * @param product_id
     */
    public void setProduct_id(java.lang.String product_id) {
        this.product_id = product_id;
    }


    /**
     * Gets the selling_point value for this OAItemModelItem_Content.
     * 
     * @return selling_point
     */
    public java.lang.String getSelling_point() {
        return selling_point;
    }


    /**
     * Sets the selling_point value for this OAItemModelItem_Content.
     * 
     * @param selling_point
     */
    public void setSelling_point(java.lang.String selling_point) {
        this.selling_point = selling_point;
    }


    /**
     * Gets the serial_number value for this OAItemModelItem_Content.
     * 
     * @return serial_number
     */
    public java.lang.String getSerial_number() {
        return serial_number;
    }


    /**
     * Sets the serial_number value for this OAItemModelItem_Content.
     * 
     * @param serial_number
     */
    public void setSerial_number(java.lang.String serial_number) {
        this.serial_number = serial_number;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OAItemModelItem_Content)) return false;
        OAItemModelItem_Content other = (OAItemModelItem_Content) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.bom_content==null && other.getBom_content()==null) || 
             (this.bom_content!=null &&
              java.util.Arrays.equals(this.bom_content, other.getBom_content()))) &&
            ((this.box_content==null && other.getBox_content()==null) || 
             (this.box_content!=null &&
              java.util.Arrays.equals(this.box_content, other.getBox_content()))) &&
            ((this.category_code==null && other.getCategory_code()==null) || 
             (this.category_code!=null &&
              this.category_code.equals(other.getCategory_code()))) &&
            ((this.color==null && other.getColor()==null) || 
             (this.color!=null &&
              this.color.equals(other.getColor()))) &&
            ((this.declared_item_name_cn==null && other.getDeclared_item_name_cn()==null) || 
             (this.declared_item_name_cn!=null &&
              this.declared_item_name_cn.equals(other.getDeclared_item_name_cn()))) &&
            ((this.developer_olduser==null && other.getDeveloper_olduser()==null) || 
             (this.developer_olduser!=null &&
              this.developer_olduser.equals(other.getDeveloper_olduser()))) &&
            ((this.developer_user==null && other.getDeveloper_user()==null) || 
             (this.developer_user!=null &&
              this.developer_user.equals(other.getDeveloper_user()))) &&
            ((this.factory_number==null && other.getFactory_number()==null) || 
             (this.factory_number!=null &&
              this.factory_number.equals(other.getFactory_number()))) &&
            ((this.hs_code==null && other.getHs_code()==null) || 
             (this.hs_code!=null &&
              this.hs_code.equals(other.getHs_code()))) &&
            ((this.include_bom_flag==null && other.getInclude_bom_flag()==null) || 
             (this.include_bom_flag!=null &&
              this.include_bom_flag.equals(other.getInclude_bom_flag()))) &&
            ((this.include_package_flag==null && other.getInclude_package_flag()==null) || 
             (this.include_package_flag!=null &&
              this.include_package_flag.equals(other.getInclude_package_flag()))) &&
            ((this.investment_item==null && other.getInvestment_item()==null) || 
             (this.investment_item!=null &&
              this.investment_item.equals(other.getInvestment_item()))) &&
            ((this.item_attached1==null && other.getItem_attached1()==null) || 
             (this.item_attached1!=null &&
              this.item_attached1.equals(other.getItem_attached1()))) &&
            ((this.item_attached2==null && other.getItem_attached2()==null) || 
             (this.item_attached2!=null &&
              this.item_attached2.equals(other.getItem_attached2()))) &&
            ((this.item_desc==null && other.getItem_desc()==null) || 
             (this.item_desc!=null &&
              this.item_desc.equals(other.getItem_desc()))) &&
            ((this.item_name_cn==null && other.getItem_name_cn()==null) || 
             (this.item_name_cn!=null &&
              this.item_name_cn.equals(other.getItem_name_cn()))) &&
            ((this.item_name_us==null && other.getItem_name_us()==null) || 
             (this.item_name_us!=null &&
              this.item_name_us.equals(other.getItem_name_us()))) &&
            ((this.item_specification_cn==null && other.getItem_specification_cn()==null) || 
             (this.item_specification_cn!=null &&
              this.item_specification_cn.equals(other.getItem_specification_cn()))) &&
            ((this.item_specification_us==null && other.getItem_specification_us()==null) || 
             (this.item_specification_us!=null &&
              this.item_specification_us.equals(other.getItem_specification_us()))) &&
            ((this.item_version==null && other.getItem_version()==null) || 
             (this.item_version!=null &&
              this.item_version.equals(other.getItem_version()))) &&
            ((this.item_version_enable==null && other.getItem_version_enable()==null) || 
             (this.item_version_enable!=null &&
              this.item_version_enable.equals(other.getItem_version_enable()))) &&
            ((this.oa_item_code==null && other.getOa_item_code()==null) || 
             (this.oa_item_code!=null &&
              this.oa_item_code.equals(other.getOa_item_code()))) &&
            ((this.oa_item_line_seq==null && other.getOa_item_line_seq()==null) || 
             (this.oa_item_line_seq!=null &&
              this.oa_item_line_seq.equals(other.getOa_item_line_seq()))) &&
            ((this.ou_content==null && other.getOu_content()==null) || 
             (this.ou_content!=null &&
              java.util.Arrays.equals(this.ou_content, other.getOu_content()))) &&
            ((this.primary_uom_code==null && other.getPrimary_uom_code()==null) || 
             (this.primary_uom_code!=null &&
              this.primary_uom_code.equals(other.getPrimary_uom_code()))) &&
            ((this.product_id==null && other.getProduct_id()==null) || 
             (this.product_id!=null &&
              this.product_id.equals(other.getProduct_id()))) &&
            ((this.selling_point==null && other.getSelling_point()==null) || 
             (this.selling_point!=null &&
              this.selling_point.equals(other.getSelling_point()))) &&
            ((this.serial_number==null && other.getSerial_number()==null) || 
             (this.serial_number!=null &&
              this.serial_number.equals(other.getSerial_number())));
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
        if (getBom_content() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBom_content());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBom_content(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBox_content() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBox_content());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBox_content(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCategory_code() != null) {
            _hashCode += getCategory_code().hashCode();
        }
        if (getColor() != null) {
            _hashCode += getColor().hashCode();
        }
        if (getDeclared_item_name_cn() != null) {
            _hashCode += getDeclared_item_name_cn().hashCode();
        }
        if (getDeveloper_olduser() != null) {
            _hashCode += getDeveloper_olduser().hashCode();
        }
        if (getDeveloper_user() != null) {
            _hashCode += getDeveloper_user().hashCode();
        }
        if (getFactory_number() != null) {
            _hashCode += getFactory_number().hashCode();
        }
        if (getHs_code() != null) {
            _hashCode += getHs_code().hashCode();
        }
        if (getInclude_bom_flag() != null) {
            _hashCode += getInclude_bom_flag().hashCode();
        }
        if (getInclude_package_flag() != null) {
            _hashCode += getInclude_package_flag().hashCode();
        }
        if (getInvestment_item() != null) {
            _hashCode += getInvestment_item().hashCode();
        }
        if (getItem_attached1() != null) {
            _hashCode += getItem_attached1().hashCode();
        }
        if (getItem_attached2() != null) {
            _hashCode += getItem_attached2().hashCode();
        }
        if (getItem_desc() != null) {
            _hashCode += getItem_desc().hashCode();
        }
        if (getItem_name_cn() != null) {
            _hashCode += getItem_name_cn().hashCode();
        }
        if (getItem_name_us() != null) {
            _hashCode += getItem_name_us().hashCode();
        }
        if (getItem_specification_cn() != null) {
            _hashCode += getItem_specification_cn().hashCode();
        }
        if (getItem_specification_us() != null) {
            _hashCode += getItem_specification_us().hashCode();
        }
        if (getItem_version() != null) {
            _hashCode += getItem_version().hashCode();
        }
        if (getItem_version_enable() != null) {
            _hashCode += getItem_version_enable().hashCode();
        }
        if (getOa_item_code() != null) {
            _hashCode += getOa_item_code().hashCode();
        }
        if (getOa_item_line_seq() != null) {
            _hashCode += getOa_item_line_seq().hashCode();
        }
        if (getOu_content() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOu_content());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOu_content(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPrimary_uom_code() != null) {
            _hashCode += getPrimary_uom_code().hashCode();
        }
        if (getProduct_id() != null) {
            _hashCode += getProduct_id().hashCode();
        }
        if (getSelling_point() != null) {
            _hashCode += getSelling_point().hashCode();
        }
        if (getSerial_number() != null) {
            _hashCode += getSerial_number().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OAItemModelItem_Content.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "OAItemModel.Item_Content"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bom_content");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "bom_content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "OAItemModel.Bom_Content"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "OAItemModel.Bom_Content"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("box_content");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "box_content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "OAItemModel.Box_Content"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "OAItemModel.Box_Content"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("category_code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "category_code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("color");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "color"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("declared_item_name_cn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "declared_item_name_cn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("developer_olduser");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "developer_olduser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("developer_user");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "developer_user"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("factory_number");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "factory_number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hs_code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "hs_code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("include_bom_flag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "include_bom_flag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("include_package_flag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "include_package_flag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("investment_item");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "investment_item"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_attached1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "item_attached1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_attached2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "item_attached2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_desc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "item_desc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_name_cn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "item_name_cn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_name_us");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "item_name_us"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_specification_cn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "item_specification_cn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_specification_us");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "item_specification_us"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "item_version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_version_enable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "item_version_enable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oa_item_code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "oa_item_code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oa_item_line_seq");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "oa_item_line_seq"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ou_content");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "ou_content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "OAItemModel.Ou_Content"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "OAItemModel.Ou_Content"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("primary_uom_code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "primary_uom_code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("product_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "product_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selling_point");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "selling_point"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serial_number");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MH.EBSOAWcfService.DBDac", "serial_number"));
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
