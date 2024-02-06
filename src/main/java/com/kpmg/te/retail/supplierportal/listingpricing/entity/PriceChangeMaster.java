package com.kpmg.te.retail.supplierportal.listingpricing.entity;

public class PriceChangeMaster {

	private String sku;
	private String itemid;
	private String approvalStatus;
	private String newPrice;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	@Override
	public String toString() {
		return "PriceChangeMaster [sku=" + sku + ", itemid=" + itemid + ", approvalStatus=" + approvalStatus
				+ ", newPrice=" + newPrice + "]";
	}

}
