package com.experian.dto.entity.litigation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 映射表： litigation_temp_party
 */
public class LitigationTempParty implements Serializable {
    /**
     * 主键
     * 映射字段： id
     */
    private String id;

    /**
     * 供应商主键
     * 映射字段： supplier_id
     */
    private String supplierId;

    /**
     * litigation_parsed_data_detail 诉讼案件详情表主键
     * 映射字段： parsed_data_detail_id
     */
    private String parsedDataDetailId;

    /**
     * 映射字段： name
     */
    private String name;

    /**
     * 映射字段： sbdnum
     */
    private String sbdnum;

    /**
     * 若企业/自然人为原告，则置值“1”；若为被告，则置值“2”，若为当事人，则置值“3”
     * 映射字段： party_category_id
     */
    private Integer partyCategoryId;

    /**
     * 法定代表人
     * 映射字段： legal_representative
     */
    private String legalRepresentative;

    /**
     * 组织机构代码
     * 映射字段： organization_code
     */
    private String organizationCode;

    /**
     * 身份证号码--加密
     * 映射字段： identification_number
     */
    private String identificationNumber;

    /**
     * 出生日期--加密
     * 映射字段： date_of_birth
     */
    private String dateOfBirth;

    /**
     * 地址
     * 映射字段： address
     */
    private String address;

    /**
     * 单位名称
     * 映射字段： organization_name
     */
    private String organizationName;

    /**
     * 履行金额
     * 映射字段： amount_involved
     */
    private BigDecimal amountInvolved;

    /**
     * 未执行金额
     * 映射字段： amount_to_be_executed
     */
    private BigDecimal amountToBeExecuted;

    /**
     * 更新时间
     * 映射字段： updated_date
     */
    private Date updatedDate;

    /**
     * 案件审理日期
     * 映射字段： accuse_date
     */
    private Date accuseDate;

    /**
     * 数据入库时间
     * 映射字段： insert_date
     */
    private Date insertDate;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getParsedDataDetailId() {
        return parsedDataDetailId;
    }

    public void setParsedDataDetailId(String parsedDataDetailId) {
        this.parsedDataDetailId = parsedDataDetailId == null ? null : parsedDataDetailId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSbdnum() {
        return sbdnum;
    }

    public void setSbdnum(String sbdnum) {
        this.sbdnum = sbdnum == null ? null : sbdnum.trim();
    }

    public Integer getPartyCategoryId() {
        return partyCategoryId;
    }

    public void setPartyCategoryId(Integer partyCategoryId) {
        this.partyCategoryId = partyCategoryId;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative == null ? null : legalRepresentative.trim();
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode == null ? null : organizationCode.trim();
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber == null ? null : identificationNumber.trim();
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth == null ? null : dateOfBirth.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName == null ? null : organizationName.trim();
    }

    public BigDecimal getAmountInvolved() {
        return amountInvolved;
    }

    public void setAmountInvolved(BigDecimal amountInvolved) {
        this.amountInvolved = amountInvolved;
    }

    public BigDecimal getAmountToBeExecuted() {
        return amountToBeExecuted;
    }

    public void setAmountToBeExecuted(BigDecimal amountToBeExecuted) {
        this.amountToBeExecuted = amountToBeExecuted;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getAccuseDate() {
        return accuseDate;
    }

    public void setAccuseDate(Date accuseDate) {
        this.accuseDate = accuseDate;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LitigationTempParty other = (LitigationTempParty) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getParsedDataDetailId() == null ? other.getParsedDataDetailId() == null : this.getParsedDataDetailId().equals(other.getParsedDataDetailId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getSbdnum() == null ? other.getSbdnum() == null : this.getSbdnum().equals(other.getSbdnum()))
            && (this.getPartyCategoryId() == null ? other.getPartyCategoryId() == null : this.getPartyCategoryId().equals(other.getPartyCategoryId()))
            && (this.getLegalRepresentative() == null ? other.getLegalRepresentative() == null : this.getLegalRepresentative().equals(other.getLegalRepresentative()))
            && (this.getOrganizationCode() == null ? other.getOrganizationCode() == null : this.getOrganizationCode().equals(other.getOrganizationCode()))
            && (this.getIdentificationNumber() == null ? other.getIdentificationNumber() == null : this.getIdentificationNumber().equals(other.getIdentificationNumber()))
            && (this.getDateOfBirth() == null ? other.getDateOfBirth() == null : this.getDateOfBirth().equals(other.getDateOfBirth()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getOrganizationName() == null ? other.getOrganizationName() == null : this.getOrganizationName().equals(other.getOrganizationName()))
            && (this.getAmountInvolved() == null ? other.getAmountInvolved() == null : this.getAmountInvolved().equals(other.getAmountInvolved()))
            && (this.getAmountToBeExecuted() == null ? other.getAmountToBeExecuted() == null : this.getAmountToBeExecuted().equals(other.getAmountToBeExecuted()))
            && (this.getUpdatedDate() == null ? other.getUpdatedDate() == null : this.getUpdatedDate().equals(other.getUpdatedDate()))
            && (this.getAccuseDate() == null ? other.getAccuseDate() == null : this.getAccuseDate().equals(other.getAccuseDate()))
            && (this.getInsertDate() == null ? other.getInsertDate() == null : this.getInsertDate().equals(other.getInsertDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getParsedDataDetailId() == null) ? 0 : getParsedDataDetailId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getSbdnum() == null) ? 0 : getSbdnum().hashCode());
        result = prime * result + ((getPartyCategoryId() == null) ? 0 : getPartyCategoryId().hashCode());
        result = prime * result + ((getLegalRepresentative() == null) ? 0 : getLegalRepresentative().hashCode());
        result = prime * result + ((getOrganizationCode() == null) ? 0 : getOrganizationCode().hashCode());
        result = prime * result + ((getIdentificationNumber() == null) ? 0 : getIdentificationNumber().hashCode());
        result = prime * result + ((getDateOfBirth() == null) ? 0 : getDateOfBirth().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getOrganizationName() == null) ? 0 : getOrganizationName().hashCode());
        result = prime * result + ((getAmountInvolved() == null) ? 0 : getAmountInvolved().hashCode());
        result = prime * result + ((getAmountToBeExecuted() == null) ? 0 : getAmountToBeExecuted().hashCode());
        result = prime * result + ((getUpdatedDate() == null) ? 0 : getUpdatedDate().hashCode());
        result = prime * result + ((getAccuseDate() == null) ? 0 : getAccuseDate().hashCode());
        result = prime * result + ((getInsertDate() == null) ? 0 : getInsertDate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", parsedDataDetailId=").append(parsedDataDetailId);
        sb.append(", name=").append(name);
        sb.append(", sbdnum=").append(sbdnum);
        sb.append(", partyCategoryId=").append(partyCategoryId);
        sb.append(", legalRepresentative=").append(legalRepresentative);
        sb.append(", organizationCode=").append(organizationCode);
        sb.append(", identificationNumber=").append(identificationNumber);
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append(", address=").append(address);
        sb.append(", organizationName=").append(organizationName);
        sb.append(", amountInvolved=").append(amountInvolved);
        sb.append(", amountToBeExecuted=").append(amountToBeExecuted);
        sb.append(", updatedDate=").append(updatedDate);
        sb.append(", accuseDate=").append(accuseDate);
        sb.append(", insertDate=").append(insertDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}