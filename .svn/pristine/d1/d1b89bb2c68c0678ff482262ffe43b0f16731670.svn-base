package com.experian.dto.entity.litigation;

import java.io.Serializable;
import java.util.Date;

/**
 * 映射表： litigation_transmission_log
 */
public class LitigationTransmissionLog implements Serializable {
    /**
     * 主键--自增
     * 映射字段： id
     */
    private Integer id;

    /**
     * 更新时间
     * 映射字段： updateDate
     */
    private Date updatedate;

    /**
     * 迁移开始时间
     * 映射字段： beginDate
     */
    private Date begindate;

    /**
     * 结束时间
     * 映射字段： endDate
     */
    private Date enddate;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
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
        LitigationTransmissionLog other = (LitigationTransmissionLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUpdatedate() == null ? other.getUpdatedate() == null : this.getUpdatedate().equals(other.getUpdatedate()))
            && (this.getBegindate() == null ? other.getBegindate() == null : this.getBegindate().equals(other.getBegindate()))
            && (this.getEnddate() == null ? other.getEnddate() == null : this.getEnddate().equals(other.getEnddate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUpdatedate() == null) ? 0 : getUpdatedate().hashCode());
        result = prime * result + ((getBegindate() == null) ? 0 : getBegindate().hashCode());
        result = prime * result + ((getEnddate() == null) ? 0 : getEnddate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", updatedate=").append(updatedate);
        sb.append(", begindate=").append(begindate);
        sb.append(", enddate=").append(enddate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}