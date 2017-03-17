package com.experian.dto.entity.litigation;

import java.io.Serializable;
import java.util.Date;

/**
 * 映射表： litigation_error_crawled_data
 */
public class LitigationErrorCrawledData implements Serializable {
    /**
     * 主键
     * 映射字段： id
     */
    private String id;

    /**
     * 映射字段： supplier_id
     */
    private String supplierId;

    /**
     * 来源网址id
     * 映射字段： source_url_id
     */
    private Integer sourceUrlId;

    /**
     * 发布日期
     * 映射字段： publish_date
     */
    private Date publishDate;

    /**
     * 创建时间
     * 映射字段： created_date
     */
    private Date createdDate;

    /**
     * 数据入库时间
     * 映射字段： insert_date
     */
    private Date insertDate;

    /**
     * 抓取数据内容页url
     * 映射字段： content_url
     */
    private String contentUrl;

    /**
     * 抓取的诉讼数据
     * 映射字段： data
     */
    private String data;

    /**
     * 标题
     * 映射字段： title
     */
    private String title;

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

    public Integer getSourceUrlId() {
        return sourceUrlId;
    }

    public void setSourceUrlId(Integer sourceUrlId) {
        this.sourceUrlId = sourceUrlId;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl == null ? null : contentUrl.trim();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
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
        LitigationErrorCrawledData other = (LitigationErrorCrawledData) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSourceUrlId() == null ? other.getSourceUrlId() == null : this.getSourceUrlId().equals(other.getSourceUrlId()))
            && (this.getPublishDate() == null ? other.getPublishDate() == null : this.getPublishDate().equals(other.getPublishDate()))
            && (this.getCreatedDate() == null ? other.getCreatedDate() == null : this.getCreatedDate().equals(other.getCreatedDate()))
            && (this.getInsertDate() == null ? other.getInsertDate() == null : this.getInsertDate().equals(other.getInsertDate()))
            && (this.getContentUrl() == null ? other.getContentUrl() == null : this.getContentUrl().equals(other.getContentUrl()))
            && (this.getData() == null ? other.getData() == null : this.getData().equals(other.getData()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSourceUrlId() == null) ? 0 : getSourceUrlId().hashCode());
        result = prime * result + ((getPublishDate() == null) ? 0 : getPublishDate().hashCode());
        result = prime * result + ((getCreatedDate() == null) ? 0 : getCreatedDate().hashCode());
        result = prime * result + ((getInsertDate() == null) ? 0 : getInsertDate().hashCode());
        result = prime * result + ((getContentUrl() == null) ? 0 : getContentUrl().hashCode());
        result = prime * result + ((getData() == null) ? 0 : getData().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
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
        sb.append(", sourceUrlId=").append(sourceUrlId);
        sb.append(", publishDate=").append(publishDate);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", insertDate=").append(insertDate);
        sb.append(", contentUrl=").append(contentUrl);
        sb.append(", data=").append(data);
        sb.append(", title=").append(title);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}