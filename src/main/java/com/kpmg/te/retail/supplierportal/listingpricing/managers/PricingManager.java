package com.kpmg.te.retail.supplierportal.listingpricing.managers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kpmg.te.retail.supplierportal.listingpricing.dao.PricingDao;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.PriceMaster;

@Component
public class PricingManager {
	
	@Autowired
	PricingDao pricingDao;

	public void pushDataToDb(ArrayList<PriceMaster> priceMasterDBList) throws ClassNotFoundException {
		pricingDao.batchInsertExcelRecords(priceMasterDBList);
		
	}

}
