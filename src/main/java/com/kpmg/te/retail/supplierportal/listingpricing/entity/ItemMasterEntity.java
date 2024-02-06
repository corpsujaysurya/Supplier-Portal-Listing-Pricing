package com.kpmg.te.retail.supplierportal.listingpricing.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item_listing_master")
public class ItemMasterEntity {

	@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		public String unique_id;
		
		public String item_id;
		public String item_name;
		public String item_description;
		public String sku;
		public String price;
		public String status;
		public String category;
		public String image_url;
		public String remove_item_flag;
		public String created_datetime;
		public String getUnique_id() {
			return unique_id;
		}
		public void setUnique_id(String unique_id) {
			this.unique_id = unique_id;
		}
		public String getItem_id() {
			return item_id;
		}
		public void setItem_id(String item_id) {
			this.item_id = item_id;
		}
		public String getItem_name() {
			return item_name;
		}
		public void setItem_name(String item_name) {
			this.item_name = item_name;
		}
		public String getItem_description() {
			return item_description;
		}
		public void setItem_description(String item_description) {
			this.item_description = item_description;
		}
		public String getSku() {
			return sku;
		}
		public void setSku(String sku) {
			this.sku = sku;
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
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getImage_url() {
			return image_url;
		}
		public void setImage_url(String image_url) {
			this.image_url = image_url;
		}
		public String getRemove_item_flag() {
			return remove_item_flag;
		}
		public void setRemove_item_flag(String remove_item_flag) {
			this.remove_item_flag = remove_item_flag;
		}
		public String getCreated_datetime() {
			return created_datetime;
		}
		public void setCreated_datetime(String created_datetime) {
			this.created_datetime = created_datetime;
		}
		
		@Override
		public String toString() {
			return "ItemMasterEntity [unique_id=" + unique_id + ", item_id=" + item_id + ", item_name=" + item_name
					+ ", item_description=" + item_description + ", sku=" + sku + ", price=" + price + ", status="
					+ status + ", category=" + category + ", image_url=" + image_url + ", remove_item_flag="
					+ remove_item_flag + ", created_datetime=" + created_datetime + "]";
		}
		
		
		
		

}
