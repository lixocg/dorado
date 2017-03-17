package com.experian.dto.entity.litigation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 映射表： litigation_temp_parsed_data_detail
 */
public class LitigationTempParsedDataDetail implements Serializable {
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
     * 案件编号
     * 映射字段： parsed_data_id
     */
    private String parsedDataId;

    /**
     *  供应商案件编号，如optix的案件编号
     * 映射字段： supplier_parsed_data_id
     */
    private String supplierParsedDataId;

    /**
     * 抓取数据表主键id
     * 映射字段： crawled_data_id
     */
    private String crawledDataId;

    /**
     * 案号
     * 映射字段： case_number
     */
    private String caseNumber;

    /**
     * 简单案号，用于判重
     * 映射字段： simple_case_number
     */
    private String simpleCaseNumber;

    /**
     * 上期案号
     * 映射字段： previous_case_number
     */
    private String previousCaseNumber;

    /**
     * 简单上期案号
     * 映射字段： simple_previous_case_number
     */
    private String simplePreviousCaseNumber;

    /**
     * 是否勾选（作报告用
     * 映射字段： if_be_court
     */
    private String ifBeCourt;

    /**
     * 映射字段： case_category_id
     */
    private Integer caseCategoryId;

    /**
     * 案件审理日期
     * 映射字段： accuse_date
     */
    private Date accuseDate;

    /**
     *  受理法院 / 执行法院
     * 映射字段： court_id
     */
    private Integer courtId;

    /**
     * 涉案金额
     * 映射字段： total_amount_involved
     */
    private BigDecimal totalAmountInvolved;

    /**
     *  胜/败诉
     * 映射字段： win
     */
    private String win;

    /**
     * 映射字段： deduplicated
     */
    private Boolean deduplicated;

    /**
     * （“0”表示该条数据解析成功；“1”，表示该条数据有用但未解析成功；“3”表示无用数据）
     * 映射字段： status
     */
    private Integer status;

    /**
     * 创建日期
     * 映射字段： created_date
     */
    private Date createdDate;

    /**
     * 更新时间
     * 映射字段： updated_date
     */
    private Date updatedDate;

    /**
     * 数据入库时间
     * 映射字段： insert_date
     */
    private Date insertDate;

    /**
     * 判断是否有效用，若无效，则又有值，重复的和被替代的 数据 则该字段有值
     * 映射字段： expiry_date
     */
    private Date expiryDate;

    /**
     * 与诉讼平台数据关联用
     * 映射字段： pk_id
     */
    private Long pkId;

    /**
     * 数据来源,0-optix,1-db2主表,2-db2进程表,3-sqlserver诉讼平台
     * 映射字段： data_from
     */
    private Integer dataFrom;

    /**
     * 数据提取标示，0-未被提取过，1-已被提取
     * 映射字段： fetch_flag
     */
    private Integer fetchFlag;

    /**
     * 案件标题
     * 映射字段： case_title
     */
    private String caseTitle;

    /**
     * 案件状态
     * 映射字段： accuse_status
     */
    private String accuseStatus;

    /**
     * 映射字段： accuser_list_no_anonymous
     */
    private String accuserListNoAnonymous;

    /**
     * 映射字段： appuser_list_no_anonymous
     */
    private String appuserListNoAnonymous;

    /**
     * 映射字段： party_list_no_anonymous
     */
    private String partyListNoAnonymous;

    /**
     * 简单案由
     * 映射字段： simple_detail
     */
    private String simpleDetail;

    /**
     * 详细案由
     * 映射字段： details
     */
    private String details;

    /**
     * 映射字段： trial_procedure
     */
    private String trialProcedure;

    /**
     * 映射字段： reason
     */
    private String reason;

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

    public String getParsedDataId() {
        return parsedDataId;
    }

    public void setParsedDataId(String parsedDataId) {
        this.parsedDataId = parsedDataId == null ? null : parsedDataId.trim();
    }

    public String getSupplierParsedDataId() {
        return supplierParsedDataId;
    }

    public void setSupplierParsedDataId(String supplierParsedDataId) {
        this.supplierParsedDataId = supplierParsedDataId == null ? null : supplierParsedDataId.trim();
    }

    public String getCrawledDataId() {
        return crawledDataId;
    }

    public void setCrawledDataId(String crawledDataId) {
        this.crawledDataId = crawledDataId == null ? null : crawledDataId.trim();
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber == null ? null : caseNumber.trim();
    }

    public String getSimpleCaseNumber() {
        return simpleCaseNumber;
    }

    public void setSimpleCaseNumber(String simpleCaseNumber) {
        this.simpleCaseNumber = simpleCaseNumber == null ? null : simpleCaseNumber.trim();
    }

    public String getPreviousCaseNumber() {
        return previousCaseNumber;
    }

    public void setPreviousCaseNumber(String previousCaseNumber) {
        this.previousCaseNumber = previousCaseNumber == null ? null : previousCaseNumber.trim();
    }

    public String getSimplePreviousCaseNumber() {
        return simplePreviousCaseNumber;
    }

    public void setSimplePreviousCaseNumber(String simplePreviousCaseNumber) {
        this.simplePreviousCaseNumber = simplePreviousCaseNumber == null ? null : simplePreviousCaseNumber.trim();
    }

    public String getIfBeCourt() {
        return ifBeCourt;
    }

    public void setIfBeCourt(String ifBeCourt) {
        this.ifBeCourt = ifBeCourt == null ? null : ifBeCourt.trim();
    }

    public Integer getCaseCategoryId() {
        return caseCategoryId;
    }

    public void setCaseCategoryId(Integer caseCategoryId) {
        this.caseCategoryId = caseCategoryId;
    }

    public Date getAccuseDate() {
        return accuseDate;
    }

    public void setAccuseDate(Date accuseDate) {
        this.accuseDate = accuseDate;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public BigDecimal getTotalAmountInvolved() {
        return totalAmountInvolved;
    }

    public void setTotalAmountInvolved(BigDecimal totalAmountInvolved) {
        this.totalAmountInvolved = totalAmountInvolved;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win == null ? null : win.trim();
    }

    public Boolean getDeduplicated() {
        return deduplicated;
    }

    public void setDeduplicated(Boolean deduplicated) {
        this.deduplicated = deduplicated;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getPkId() {
        return pkId;
    }

    public void setPkId(Long pkId) {
        this.pkId = pkId;
    }

    public Integer getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(Integer dataFrom) {
        this.dataFrom = dataFrom;
    }

    public Integer getFetchFlag() {
        return fetchFlag;
    }

    public void setFetchFlag(Integer fetchFlag) {
        this.fetchFlag = fetchFlag;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle == null ? null : caseTitle.trim();
    }

    public String getAccuseStatus() {
        return accuseStatus;
    }

    public void setAccuseStatus(String accuseStatus) {
        this.accuseStatus = accuseStatus == null ? null : accuseStatus.trim();
    }

    public String getAccuserListNoAnonymous() {
        return accuserListNoAnonymous;
    }

    public void setAccuserListNoAnonymous(String accuserListNoAnonymous) {
        this.accuserListNoAnonymous = accuserListNoAnonymous == null ? null : accuserListNoAnonymous.trim();
    }

    public String getAppuserListNoAnonymous() {
        return appuserListNoAnonymous;
    }

    public void setAppuserListNoAnonymous(String appuserListNoAnonymous) {
        this.appuserListNoAnonymous = appuserListNoAnonymous == null ? null : appuserListNoAnonymous.trim();
    }

    public String getPartyListNoAnonymous() {
        return partyListNoAnonymous;
    }

    public void setPartyListNoAnonymous(String partyListNoAnonymous) {
        this.partyListNoAnonymous = partyListNoAnonymous == null ? null : partyListNoAnonymous.trim();
    }

    public String getSimpleDetail() {
        return simpleDetail;
    }

    public void setSimpleDetail(String simpleDetail) {
        this.simpleDetail = simpleDetail == null ? null : simpleDetail.trim();
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details == null ? null : details.trim();
    }

    public String getTrialProcedure() {
        return trialProcedure;
    }

    public void setTrialProcedure(String trialProcedure) {
        this.trialProcedure = trialProcedure == null ? null : trialProcedure.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
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
        LitigationTempParsedDataDetail other = (LitigationTempParsedDataDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getParsedDataId() == null ? other.getParsedDataId() == null : this.getParsedDataId().equals(other.getParsedDataId()))
            && (this.getSupplierParsedDataId() == null ? other.getSupplierParsedDataId() == null : this.getSupplierParsedDataId().equals(other.getSupplierParsedDataId()))
            && (this.getCrawledDataId() == null ? other.getCrawledDataId() == null : this.getCrawledDataId().equals(other.getCrawledDataId()))
            && (this.getCaseNumber() == null ? other.getCaseNumber() == null : this.getCaseNumber().equals(other.getCaseNumber()))
            && (this.getSimpleCaseNumber() == null ? other.getSimpleCaseNumber() == null : this.getSimpleCaseNumber().equals(other.getSimpleCaseNumber()))
            && (this.getPreviousCaseNumber() == null ? other.getPreviousCaseNumber() == null : this.getPreviousCaseNumber().equals(other.getPreviousCaseNumber()))
            && (this.getSimplePreviousCaseNumber() == null ? other.getSimplePreviousCaseNumber() == null : this.getSimplePreviousCaseNumber().equals(other.getSimplePreviousCaseNumber()))
            && (this.getIfBeCourt() == null ? other.getIfBeCourt() == null : this.getIfBeCourt().equals(other.getIfBeCourt()))
            && (this.getCaseCategoryId() == null ? other.getCaseCategoryId() == null : this.getCaseCategoryId().equals(other.getCaseCategoryId()))
            && (this.getAccuseDate() == null ? other.getAccuseDate() == null : this.getAccuseDate().equals(other.getAccuseDate()))
            && (this.getCourtId() == null ? other.getCourtId() == null : this.getCourtId().equals(other.getCourtId()))
            && (this.getTotalAmountInvolved() == null ? other.getTotalAmountInvolved() == null : this.getTotalAmountInvolved().equals(other.getTotalAmountInvolved()))
            && (this.getWin() == null ? other.getWin() == null : this.getWin().equals(other.getWin()))
            && (this.getDeduplicated() == null ? other.getDeduplicated() == null : this.getDeduplicated().equals(other.getDeduplicated()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreatedDate() == null ? other.getCreatedDate() == null : this.getCreatedDate().equals(other.getCreatedDate()))
            && (this.getUpdatedDate() == null ? other.getUpdatedDate() == null : this.getUpdatedDate().equals(other.getUpdatedDate()))
            && (this.getInsertDate() == null ? other.getInsertDate() == null : this.getInsertDate().equals(other.getInsertDate()))
            && (this.getExpiryDate() == null ? other.getExpiryDate() == null : this.getExpiryDate().equals(other.getExpiryDate()))
            && (this.getPkId() == null ? other.getPkId() == null : this.getPkId().equals(other.getPkId()))
            && (this.getDataFrom() == null ? other.getDataFrom() == null : this.getDataFrom().equals(other.getDataFrom()))
            && (this.getFetchFlag() == null ? other.getFetchFlag() == null : this.getFetchFlag().equals(other.getFetchFlag()))
            && (this.getCaseTitle() == null ? other.getCaseTitle() == null : this.getCaseTitle().equals(other.getCaseTitle()))
            && (this.getAccuseStatus() == null ? other.getAccuseStatus() == null : this.getAccuseStatus().equals(other.getAccuseStatus()))
            && (this.getAccuserListNoAnonymous() == null ? other.getAccuserListNoAnonymous() == null : this.getAccuserListNoAnonymous().equals(other.getAccuserListNoAnonymous()))
            && (this.getAppuserListNoAnonymous() == null ? other.getAppuserListNoAnonymous() == null : this.getAppuserListNoAnonymous().equals(other.getAppuserListNoAnonymous()))
            && (this.getPartyListNoAnonymous() == null ? other.getPartyListNoAnonymous() == null : this.getPartyListNoAnonymous().equals(other.getPartyListNoAnonymous()))
            && (this.getSimpleDetail() == null ? other.getSimpleDetail() == null : this.getSimpleDetail().equals(other.getSimpleDetail()))
            && (this.getDetails() == null ? other.getDetails() == null : this.getDetails().equals(other.getDetails()))
            && (this.getTrialProcedure() == null ? other.getTrialProcedure() == null : this.getTrialProcedure().equals(other.getTrialProcedure()))
            && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getParsedDataId() == null) ? 0 : getParsedDataId().hashCode());
        result = prime * result + ((getSupplierParsedDataId() == null) ? 0 : getSupplierParsedDataId().hashCode());
        result = prime * result + ((getCrawledDataId() == null) ? 0 : getCrawledDataId().hashCode());
        result = prime * result + ((getCaseNumber() == null) ? 0 : getCaseNumber().hashCode());
        result = prime * result + ((getSimpleCaseNumber() == null) ? 0 : getSimpleCaseNumber().hashCode());
        result = prime * result + ((getPreviousCaseNumber() == null) ? 0 : getPreviousCaseNumber().hashCode());
        result = prime * result + ((getSimplePreviousCaseNumber() == null) ? 0 : getSimplePreviousCaseNumber().hashCode());
        result = prime * result + ((getIfBeCourt() == null) ? 0 : getIfBeCourt().hashCode());
        result = prime * result + ((getCaseCategoryId() == null) ? 0 : getCaseCategoryId().hashCode());
        result = prime * result + ((getAccuseDate() == null) ? 0 : getAccuseDate().hashCode());
        result = prime * result + ((getCourtId() == null) ? 0 : getCourtId().hashCode());
        result = prime * result + ((getTotalAmountInvolved() == null) ? 0 : getTotalAmountInvolved().hashCode());
        result = prime * result + ((getWin() == null) ? 0 : getWin().hashCode());
        result = prime * result + ((getDeduplicated() == null) ? 0 : getDeduplicated().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreatedDate() == null) ? 0 : getCreatedDate().hashCode());
        result = prime * result + ((getUpdatedDate() == null) ? 0 : getUpdatedDate().hashCode());
        result = prime * result + ((getInsertDate() == null) ? 0 : getInsertDate().hashCode());
        result = prime * result + ((getExpiryDate() == null) ? 0 : getExpiryDate().hashCode());
        result = prime * result + ((getPkId() == null) ? 0 : getPkId().hashCode());
        result = prime * result + ((getDataFrom() == null) ? 0 : getDataFrom().hashCode());
        result = prime * result + ((getFetchFlag() == null) ? 0 : getFetchFlag().hashCode());
        result = prime * result + ((getCaseTitle() == null) ? 0 : getCaseTitle().hashCode());
        result = prime * result + ((getAccuseStatus() == null) ? 0 : getAccuseStatus().hashCode());
        result = prime * result + ((getAccuserListNoAnonymous() == null) ? 0 : getAccuserListNoAnonymous().hashCode());
        result = prime * result + ((getAppuserListNoAnonymous() == null) ? 0 : getAppuserListNoAnonymous().hashCode());
        result = prime * result + ((getPartyListNoAnonymous() == null) ? 0 : getPartyListNoAnonymous().hashCode());
        result = prime * result + ((getSimpleDetail() == null) ? 0 : getSimpleDetail().hashCode());
        result = prime * result + ((getDetails() == null) ? 0 : getDetails().hashCode());
        result = prime * result + ((getTrialProcedure() == null) ? 0 : getTrialProcedure().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
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
        sb.append(", parsedDataId=").append(parsedDataId);
        sb.append(", supplierParsedDataId=").append(supplierParsedDataId);
        sb.append(", crawledDataId=").append(crawledDataId);
        sb.append(", caseNumber=").append(caseNumber);
        sb.append(", simpleCaseNumber=").append(simpleCaseNumber);
        sb.append(", previousCaseNumber=").append(previousCaseNumber);
        sb.append(", simplePreviousCaseNumber=").append(simplePreviousCaseNumber);
        sb.append(", ifBeCourt=").append(ifBeCourt);
        sb.append(", caseCategoryId=").append(caseCategoryId);
        sb.append(", accuseDate=").append(accuseDate);
        sb.append(", courtId=").append(courtId);
        sb.append(", totalAmountInvolved=").append(totalAmountInvolved);
        sb.append(", win=").append(win);
        sb.append(", deduplicated=").append(deduplicated);
        sb.append(", status=").append(status);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", updatedDate=").append(updatedDate);
        sb.append(", insertDate=").append(insertDate);
        sb.append(", expiryDate=").append(expiryDate);
        sb.append(", pkId=").append(pkId);
        sb.append(", dataFrom=").append(dataFrom);
        sb.append(", fetchFlag=").append(fetchFlag);
        sb.append(", caseTitle=").append(caseTitle);
        sb.append(", accuseStatus=").append(accuseStatus);
        sb.append(", accuserListNoAnonymous=").append(accuserListNoAnonymous);
        sb.append(", appuserListNoAnonymous=").append(appuserListNoAnonymous);
        sb.append(", partyListNoAnonymous=").append(partyListNoAnonymous);
        sb.append(", simpleDetail=").append(simpleDetail);
        sb.append(", details=").append(details);
        sb.append(", trialProcedure=").append(trialProcedure);
        sb.append(", reason=").append(reason);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}