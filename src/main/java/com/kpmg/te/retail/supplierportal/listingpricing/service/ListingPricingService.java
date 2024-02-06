package com.kpmg.te.retail.supplierportal.listingpricing.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kpmg.te.retail.supplierportal.listingpricing.entity.DateAndTime;
import com.kpmg.te.retail.supplierportal.listingpricing.controller.ListingController;
import com.kpmg.te.retail.supplierportal.listingpricing.controller.PricingController;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.ItemMaster;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.PriceChangeMaster;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.PriceMaster;
import com.kpmg.te.retail.supplierportal.listingpricing.managers.ListingManager;
import com.kpmg.te.retail.supplierportal.listingpricing.managers.PricingManager;
import com.kpmg.te.retail.supplierportal.listingpricing.utils.ListingPricingUtils;

@RestController
@RequestMapping("/api/listingpricing/service")
public class ListingPricingService {
	
	
	@Autowired
	ListingController listingController;
	
	@Autowired
	ListingManager listingManager;
	
	@Autowired
	PricingManager pricingManager;

	@Autowired
	PricingController pricingController;
	
	@Autowired
	ListingPricingUtils listingPricingUtils;
	
	private static final Logger logger = Logger.getLogger(ListingPricingService.class.getName());

	/************************************************************************************************************************************************************************** */
	/*													ITEM LISTING MODULE REST END-POINTS                                                                                    */
	/**************************************************************************************************************************************************************************/
	@RequestMapping(path = "/listing/getAllListingData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<ItemMaster> getItemListingData() throws ClassNotFoundException, SQLException {
		ArrayList<ItemMaster> itemMasterList = new ArrayList<ItemMaster>();
		itemMasterList = listingController.getAllItemListingData();
		logger.info("[C]ListingPricingService::[M]getItemListingData -> The Item List to display is: "+itemMasterList.toString());
		return  itemMasterList;
	}
	
	@RequestMapping(path = "/listing/getItemDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<ItemMaster> getItemDetails(@RequestParam(value="itemId[]") String[] itemId) throws ClassNotFoundException, SQLException {
		logger.info(itemId.toString());
		ArrayList<ItemMaster> itemDetails = new ArrayList<ItemMaster>();
		itemDetails = listingController.getAllItemDetailsData(itemId);
		logger.info("[C]ListingPricingService::[M]getItemDetails -> The Item Details list to display is: "+itemDetails.toString());
		return  itemDetails;
	}
	
	@RequestMapping(path = "/listing/updateItemDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HashMap<String,String> updateItemDetails(@RequestBody List<ItemMaster> iueMasterList) throws ClassNotFoundException, SQLException {
		return listingController.updateItemDetailsData(iueMasterList);
	}
	
	@RequestMapping(path = "/listing/uploadListingData", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public List<ItemMaster> importListingDataFromExcel(@RequestPart("inpFile") MultipartFile files) throws IOException, ClassNotFoundException {

		List<ItemMaster> itemMasterList = new ArrayList<>();
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
			if (index > 0) {
				XSSFRow row = worksheet.getRow(index);
				ItemMaster itemMasterObj = new ItemMaster();

				itemMasterObj.setItemId(listingPricingUtils.getCellValue(row, 0));
				itemMasterObj.setItemName(listingPricingUtils.getCellValue(row, 1))  ;
				itemMasterObj.setItemDescription(listingPricingUtils.getCellValue(row, 2));
				itemMasterObj.setSku(listingPricingUtils.getCellValue(row, 3));
				itemMasterObj.setPrice(listingPricingUtils.getCellValue(row, 4));
				itemMasterObj.setStatus(listingPricingUtils.getCellValue(row, 5));
				itemMasterObj.setCategory(listingPricingUtils.getCellValue(row, 6));
				itemMasterObj.setImageUrl(listingPricingUtils.getCellValue(row, 7));
				
				itemMasterList.add(itemMasterObj);
			}
		}
		
		// To save the model data to Local DB
		ArrayList<ItemMaster> itemMasterDBList = new ArrayList<ItemMaster>();
		if (itemMasterList.size() > 0) {
			final DateAndTime dat = listingPricingUtils.getDateAndTimeForUploadData();
			System.out.println("The final date and time data for this batch upload is :" + dat.toString());

			itemMasterList.forEach(item -> {
				ItemMaster itemMasterObj = new ItemMaster();
				itemMasterObj.setUniqueId(listingPricingUtils.setRandomUUID());
				itemMasterObj.setItemId(item.getItemId());
				itemMasterObj.setItemName(item.getItemName());
				itemMasterObj.setItemDescription(item.getItemDescription());
				itemMasterObj.setSku(item.getSku());
				itemMasterObj.setPrice(item.getPrice());
				itemMasterObj.setCategory( item.getCategory());
				itemMasterObj.setStatus(item.getStatus());
				itemMasterObj.setImageUrl(item.getImageUrl());
				itemMasterObj.setRemoveItemFlag("N");
				itemMasterObj.setCreatedDateTime(dat.getTimestamp());
				
				itemMasterDBList.add(itemMasterObj);
			});
			logger.info(itemMasterDBList.toString());
			//listingPricingRepo.saveAll(itemMasterEntityList);
			listingManager.pushDataToDb(itemMasterDBList);
		}

		return itemMasterList;
	}
	
	@RequestMapping(path = "/listing/updatePriceChange", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HashMap<String,String> updatePriceItemListing(@RequestBody List<PriceChangeMaster> priceChangeMaster) throws ClassNotFoundException, SQLException {
		//TODO THIS MODULE WILL BE RECEIVING DATA FROM COST CHANGE MODULE FOR PRICE UPDATION
		return listingController.updatePriceChange(priceChangeMaster);
	}
	
/***************************************************************************************************************************************************************************/
/*													COST CHANGE MODULE REST END-POINTS                                                                                    */
/**************************************************************************************************************************************************************************/	
	@RequestMapping(path = "/pricing/getAllPricingData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<PriceMaster> getItemPricingData() throws ClassNotFoundException, SQLException {
		ArrayList<PriceMaster> priceMasterList;
		priceMasterList = pricingController.getAllItemPricingData();
		logger.info("[C]ListingPricingService[M]getItemPricingList ->The Item Pricing List to display is: " + priceMasterList.toString());
		return priceMasterList;
	}
	
	@RequestMapping(path = "/pricing/getPricingDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<PriceMaster> getPricingDetails(@RequestParam(value="itemId[]") String[] itemId) throws ClassNotFoundException, SQLException {
		logger.info(itemId.toString());
		ArrayList<PriceMaster> costChangeDetails = new ArrayList<PriceMaster>();
		costChangeDetails = pricingController.getAllItemDetailsData(itemId);
		logger.info("[C]ListingPricingService::[M]getItemDetails -> The Item Details list to display is: "+costChangeDetails.toString());
		return  costChangeDetails;
	}
	
	@RequestMapping(path = "/pricing/updateCost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HashMap<String,String> updateCost(@RequestBody List<PriceMaster> priceMasterList) throws ClassNotFoundException, SQLException {
		return pricingController.updateCostChange(priceMasterList);
	}
	
	@RequestMapping(path = "/pricing/uploadPricingData", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public List<PriceMaster> importCostChangeDataFromExcel(@RequestPart("inpFile") MultipartFile files) throws IOException, ClassNotFoundException {

		ArrayList<PriceMaster> priceMasterList = new ArrayList<PriceMaster>();
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
			if (index > 0) {
				XSSFRow row = worksheet.getRow(index);
				PriceMaster priceMasterObj = new PriceMaster();
				priceMasterObj.setItemId(listingPricingUtils.getCellValue(row, 0));
				priceMasterObj.setItemName(listingPricingUtils.getCellValue(row, 1))  ;
				priceMasterObj.setItemDescription(listingPricingUtils.getCellValue(row, 2));
				priceMasterObj.setSku(listingPricingUtils.getCellValue(row, 3));
				priceMasterObj.setPrice(listingPricingUtils.getCellValue(row, 4));
				priceMasterObj.setNewSubmittedPrice(listingPricingUtils.getCellValue(row, 5));
				priceMasterObj.setStatus(listingPricingUtils.getCellValue(row, 6));
				priceMasterObj.setCategory(listingPricingUtils.getCellValue(row, 7));
				priceMasterObj.setImageUrl(listingPricingUtils.getCellValue(row, 8));
				priceMasterList.add(priceMasterObj);
			}
		}
		
		// To save the model data to Local DB
		ArrayList<PriceMaster> priceMasterDBList = new ArrayList<PriceMaster>();
		if (priceMasterList.size() > 0) {
			final DateAndTime dat = listingPricingUtils.getDateAndTimeForUploadData();
			System.out.println("The final date and time data for this batch upload is :" + dat.toString());
			priceMasterList.forEach(item -> {
				PriceMaster priceMasterObj = new PriceMaster();
				priceMasterObj.setUniqueId(listingPricingUtils.setRandomUUID());
				priceMasterObj.setItemId(item.getItemId());
				priceMasterObj.setItemName(item.getItemName());
				priceMasterObj.setItemDescription(item.getItemDescription());
				priceMasterObj.setSku(item.getSku());
				priceMasterObj.setPrice(item.getPrice());
				priceMasterObj.setNewSubmittedPrice(item.getNewSubmittedPrice());
				priceMasterObj.setCategory( item.getCategory());
				priceMasterObj.setStatus(item.getStatus());
				priceMasterObj.setImageUrl(item.getImageUrl());
				priceMasterObj.setRemoveItemFlag("N");
				priceMasterObj.setCreatedDateTime(dat.getTimestamp());
				priceMasterDBList.add(priceMasterObj);
			});
			logger.info(priceMasterDBList.toString());
			//listingPricingRepo.saveAll(itemMasterEntityList);
			pricingManager.pushDataToDb(priceMasterDBList);
		}
		return priceMasterDBList;
	}
	
	@RequestMapping(path = "/pricing/updatePriceChange", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HashMap<String,String> updatePriceCostChange(@RequestBody List<PriceChangeMaster> priceChangeMaster) throws ClassNotFoundException, SQLException {
		//TODO THIS MODULE WILL BE RECEIVING DATA FROM COST CHANGE MODULE FOR PRICE UPDATION
		return pricingController.updatePriceChange(priceChangeMaster);
	}
	

}
