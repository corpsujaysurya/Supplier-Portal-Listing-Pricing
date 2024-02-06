package com.kpmg.te.retail.supplierportal.listingpricing.repos;

import java.util.List;


import com.kpmg.te.retail.supplierportal.listingpricing.entity.ItemMasterEntity;

public interface ListingPricingRepo {
	
	List<ItemMasterEntity> findAll();

	void saveAll(List<ItemMasterEntity> itemMasterEntityList);
}
