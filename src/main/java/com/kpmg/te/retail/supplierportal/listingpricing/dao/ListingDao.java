package com.kpmg.te.retail.supplierportal.listingpricing.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kpmg.te.retail.supplierportal.listingpricing.constants.ListingPricingConstants;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.ItemMaster;
import com.kpmg.te.retail.supplierportal.listingpricing.utils.ListingPricingUtils;

@Component
public class ListingDao {
	
	@Autowired ListingPricingUtils listingPricingUtils;

	private static final Logger logger = Logger.getLogger(ListingDao.class.getName());

	public Connection getConnectioDetails() throws ClassNotFoundException, SQLException {
		String myDriver = ListingPricingConstants.myDriver;
		String myUrl = ListingPricingConstants.myUrl;
		Class.forName(myDriver);
		Connection conn = DriverManager.getConnection(myUrl, ListingPricingConstants.mySQL_ID,
				ListingPricingConstants.mySQL_pass);
		return conn;
	}

	public void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}

	public ArrayList<ItemMaster> getAllItemListingDataFromMaster() throws ClassNotFoundException, SQLException {
		ItemMaster itemMasterObj;
		ArrayList<ItemMaster> itemMasterList = new ArrayList<ItemMaster>();
		Connection conn = getConnectioDetails();
		String query = "SELECT  * from SUPPLIER_PORTAL.ITEM_LISTING_MASTER ORDER BY CREATED_DATETIME DESC LIMIT 20";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			itemMasterObj = new ItemMaster();
			itemMasterObj.setItemId(rs.getString("item_id"));
			itemMasterObj.setItemName(rs.getString("item_name"));
			itemMasterObj.setSku(rs.getString("sku"));
			itemMasterObj.setPrice(rs.getString("price"));
			itemMasterObj.setStatus(rs.getString("status"));
			itemMasterList.add(itemMasterObj);
		}
		logger.info("[C]ListingDao::[M]getAllItemListingData -> The Item list is: " + itemMasterList.toString());
		return itemMasterList;
	}

	public ArrayList<ItemMaster> getAllItemDetailsFromMaster(String[] itemId)
			throws ClassNotFoundException, SQLException {
		ItemMaster itemMasterObj;
		ArrayList<ItemMaster> itemMasterList = new ArrayList<ItemMaster>();
		Connection conn = getConnectioDetails();
		String[] strarr = itemId;
		StringBuilder itemIdString  = listingPricingUtils.toString(strarr);
		String query = "SELECT  * from SUPPLIER_PORTAL.ITEM_LISTING_MASTER WHERE ITEM_ID IN" + itemIdString + "ORDER BY CREATED_DATETIME DESC LIMIT 20";
		logger.info(query);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			itemMasterObj = new ItemMaster();
			itemMasterObj.setItemId(rs.getString("item_Id"));
			itemMasterObj.setItemDescription(rs.getString("item_Description"));
			itemMasterObj.setSku(rs.getString("sku"));
			itemMasterObj.setCategory(rs.getString("category"));
			itemMasterObj.setPrice(rs.getString("price"));
			itemMasterObj.setStatus(rs.getString("status"));
			itemMasterObj.setImageUrl(rs.getString("image_Url"));
			itemMasterObj.setRemoveItemFlag(rs.getString("remove_Item_Flag"));
			itemMasterList.add(itemMasterObj);
		}
		logger.info("[C]ListingDao::[M]getAllItemDetails -> The Item details list is: " + itemMasterList.toString());
		return itemMasterList;
	}

	
	//test
	public String updateItemListingDetails(ItemMaster iue) throws SQLException, ClassNotFoundException {
		Connection conn = getConnectioDetails();
		String updateStatus = "Invalid";
		try {
			conn = getConnectioDetails();
			if (iue.getRemoveItemFlag().equalsIgnoreCase("Y")) {
				String query = "DELETE FROM SUPPLIER_PORTAL.ITEM_LISTING_MASTER WHERE ITEM_ID = ?";
				logger.info(query);
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, iue.getItemId());
				int updateStatusCode = pstmt.executeUpdate();
				logger.info(Integer.toString(updateStatusCode));
				updateStatus = (updateStatusCode == 1) ? ("SUCCESS") : ("FAILURE");
				pstmt.close();
			} else {
				String query = "UPDATE SUPPLIER_PORTAL.ITEM_LISTING_MASTER SET STATUS = ? , IMAGE_URL = ? WHERE ITEM_ID = ?  ";
				logger.info(query);
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, iue.getStatus());
				pstmt.setString(2, iue.getImageUrl());
				pstmt.setString(3, iue.getItemId());
				int updateStatusCode = pstmt.executeUpdate();
				logger.info(Integer.toString(updateStatusCode));
				updateStatus = (updateStatusCode == 1) ? ("SUCCESS") : ("FAILURE");
				pstmt.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return updateStatus;
	}

	public String batchInsertExcelRecords(List<ItemMaster> itemMasterList) throws ClassNotFoundException {
		PreparedStatement preparedStatement;
		try {
			Connection conn = getConnectioDetails();
			conn.setAutoCommit(true);
			String insertQuery = "INSERT INTO SUPPLIER_PORTAL.ITEM_LISTING_MASTER(UNIQUE_ID, ITEM_ID, ITEM_NAME, ITEM_DESCRIPTION, SKU,PRICE,STATUS,CATEGORY,IMAGE_URL,REMOVE_ITEM_FLAG,CREATED_DATETIME)"
					+ " VALUES" + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = conn.prepareStatement(insertQuery);
			for (ItemMaster itemMaster : itemMasterList) {
				preparedStatement.setString(1, itemMaster.getUniqueId());
				preparedStatement.setString(2, itemMaster.getItemId());
				preparedStatement.setString(3, itemMaster.getItemName());
				preparedStatement.setString(4, itemMaster.getItemDescription());
				preparedStatement.setString(5, itemMaster.getSku());
				preparedStatement.setString(6, itemMaster.getPrice());
				preparedStatement.setString(7, itemMaster.getStatus());
				preparedStatement.setString(8, itemMaster.getCategory());
				preparedStatement.setString(9, itemMaster.getImageUrl());
				preparedStatement.setString(10, itemMaster.getRemoveItemFlag());
				preparedStatement.setString(11, itemMaster.getCreatedDateTime());
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			preparedStatement.close();
			conn.close();
			return "Batch Records Inserterd Successfully";

		} catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error msg: " + ex.getMessage());
				ex = ex.getNextException();
			}
			throw new RuntimeException("Error");
		}
	}
}
