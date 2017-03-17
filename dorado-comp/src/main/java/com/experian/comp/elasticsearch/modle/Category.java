package com.experian.comp.elasticsearch.modle;

public class Category {
	private String id;
	private String name;
	private String parentId;
	private String productPlanId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getProductPlanId() {
		return productPlanId;
	}

	public void setProductPlanId(String productPlanId) {
		this.productPlanId = productPlanId;
	}

}
