package com.kpmg.te.retail.supplierportal.listingpricing.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kpmg.te.retail.supplierportal.listingpricing.dao.ListingDao;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.ItemMaster;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.PriceChangeMaster;
import com.kpmg.te.retail.supplierportal.listingpricing.repos.ListingInterface;

@Component
public class ListingController implements ListingInterface {

	@Autowired
	ListingDao listingDao;

	public ArrayList<ItemMaster> getAllItemListingData() throws ClassNotFoundException, SQLException {
		return listingDao.getAllItemListingDataFromMaster();

	}

	public ArrayList<ItemMaster> getAllItemDetailsData(String[] itemId) throws ClassNotFoundException, SQLException {
		 return listingDao.getAllItemDetailsFromMaster(itemId);
	}

	public String updateItemDetailsData(String updateStatus) {
		return null;
	}

	public HashMap<String, String> updateItemDetailsData(List<ItemMaster> iueUpdateMasterList) {
		HashMap<String,String> updateStatusMap = new HashMap<String,String>();
		iueUpdateMasterList.stream().forEach((entry) -> {
						String status = "Invalid";
						try {
							status = listingDao.updateItemListingDetails(entry);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						updateStatusMap.put(entry.getItemId(), status);
		});
		return updateStatusMap;
	}

	public HashMap<String, String> updatePriceChange(List<PriceChangeMaster> priceChangeMaster) {
		// TODO Auto-generated method stub
		return null;
	}

}
