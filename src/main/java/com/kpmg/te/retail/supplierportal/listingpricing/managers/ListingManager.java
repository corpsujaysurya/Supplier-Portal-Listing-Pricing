package com.kpmg.te.retail.supplierportal.listingpricing.managers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kpmg.te.retail.supplierportal.listingpricing.dao.ListingDao;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.ItemMaster;

@Component
public class ListingManager {
	
	@Autowired
	ListingDao listingDao;

	public String pushDataToDb(List<ItemMaster> itemMasterList) throws ClassNotFoundException {
				return listingDao.batchInsertExcelRecords(itemMasterList);
	}

}
