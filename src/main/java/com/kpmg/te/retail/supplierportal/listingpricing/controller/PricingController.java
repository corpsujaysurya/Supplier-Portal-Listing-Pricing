package com.kpmg.te.retail.supplierportal.listingpricing.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kpmg.te.retail.supplierportal.listingpricing.dao.PricingDao;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.PriceChangeMaster;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.PriceMaster;
import com.kpmg.te.retail.supplierportal.listingpricing.repos.PricingInterface;

@Component
public class PricingController implements PricingInterface {
	
	@Autowired
	PricingDao pricingDao;

	public ArrayList<PriceMaster> getAllItemPricingData() throws ClassNotFoundException, SQLException {
		return pricingDao.getAllItemPricingDataFromMaster();
	}

	public ArrayList<PriceMaster> getAllItemDetailsData(String[] itemId) throws ClassNotFoundException, SQLException {
		 return pricingDao.getAllcostChangeDetailsFromMaster(itemId);
	}

	public HashMap<String, String> updateCostChange(List<PriceMaster> priceMasterList) {
		HashMap<String,String> priceUpdateStatusMap = new HashMap<String,String>();
		priceMasterList.stream().forEach((entry) -> {
						String status = "Invalid";
						try {
							status = pricingDao.updateCostChangeDetails(entry);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						priceUpdateStatusMap.put(entry.getItemId(), status);
		});
		return priceUpdateStatusMap;
	}

	public HashMap<String, String> updatePriceChange(List<PriceChangeMaster> priceChangeMasterList)
			throws ClassNotFoundException, SQLException {
		HashMap<String, String> priceUpdateStatusMap = new HashMap<String, String>();
		priceChangeMasterList.stream().forEach((entry) -> {
			String status = "Invalid";
			try {
				pricingDao.updateCostChangeReqStatus(entry);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			priceUpdateStatusMap.put(entry.getItemid(), status);
		});
		return priceUpdateStatusMap;
	}
}
