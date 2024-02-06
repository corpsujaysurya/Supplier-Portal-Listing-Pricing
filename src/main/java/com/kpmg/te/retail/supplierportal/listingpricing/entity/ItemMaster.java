package com.kpmg.te.retail.supplierportal.listingpricing.entity;

public class ItemMaster {
	
	private String uniqueId;
	private String itemId;
	private String itemName;
	private String itemDescription;
	private String sku;
	private String price;
	private String status;
	private String category;
	private String imageUrl;
	private String removeItemFlag;
	private String createdDateTime;
	
	
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getRemoveItemFlag() {
		return removeItemFlag;
	}
	public void setRemoveItemFlag(String removeItemFlag) {
		this.removeItemFlag = removeItemFlag;
	}
	
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	
	@Override
	public String toString() {
		return "ItemMaster [uniqueId=" + uniqueId + ", itemId=" + itemId + ", itemName=" + itemName
				+ ", itemDescription=" + itemDescription + ", sku=" + sku + ", price=" + price + ", status=" + status
				+ ", category=" + category + ", imageUrl=" + imageUrl + ", removeItemFlag=" + removeItemFlag
				+ ", createdDateTime=" + createdDateTime + "]";
	}
	
	
	
	

}
