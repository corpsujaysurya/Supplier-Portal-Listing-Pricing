package com.kpmg.te.retail.supplierportal.listingpricing.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kpmg.te.retail.supplierportal.listingpricing.constants.ListingPricingConstants;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.PriceChangeMaster;
import com.kpmg.te.retail.supplierportal.listingpricing.entity.PriceMaster;
import com.kpmg.te.retail.supplierportal.listingpricing.utils.ListingPricingUtils;

@Component
public class PricingDao {
	
	@Autowired
	ListingPricingUtils listingPricingUtils;
	
	private static final Logger logger = Logger.getLogger(PricingDao.class.getName());

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

	public ArrayList<PriceMaster> getAllItemPricingDataFromMaster() throws ClassNotFoundException, SQLException {
		PriceMaster priceMasterObj;
		ArrayList<PriceMaster> priceMasterList = new ArrayList<PriceMaster>();
		Connection conn = getConnectioDetails();
		String query = "SELECT  * from SUPPLIER_PORTAL.COST_CHANGE_MASTER ORDER BY CREATED_DATETIME DESC LIMIT 20";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			priceMasterObj = new PriceMaster();
			priceMasterObj.setItemId(rs.getString("item_id"));
			priceMasterObj.setItemName(rs.getString("item_name"));
			priceMasterObj.setCategory(rs.getString("category"));
			priceMasterObj.setPrice(rs.getString("price"));
			priceMasterObj.setStatus(rs.getString("status"));
			priceMasterList.add(priceMasterObj);
		}
		logger.info("[C]ListingDao::[M]getAllItemListingData -> The Item list is: " + priceMasterList.toString());
		return priceMasterList;
	}

	public ArrayList<PriceMaster> getAllcostChangeDetailsFromMaster(String[] costChangeArray) throws ClassNotFoundException, SQLException {
		PriceMaster priceMasterObj;
		ArrayList<PriceMaster> priceMasterList = new ArrayList<PriceMaster>();
		Connection conn = getConnectioDetails();
		String[] strarr = costChangeArray;
		StringBuilder costChangeIdString  = listingPricingUtils.toString(strarr);
		String query = "SELECT  * from SUPPLIER_PORTAL.COST_CHANGE_MASTER WHERE ITEM_ID IN" + costChangeIdString + "ORDER BY CREATED_DATETIME DESC LIMIT 20";
		logger.info(query);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			priceMasterObj = new PriceMaster();
			priceMasterObj.setItemId(rs.getString("item_Id"));
			priceMasterObj.setItemName(rs.getString("item_Name"));
			priceMasterObj.setItemDescription(rs.getString("item_Description"));
			priceMasterObj.setSku(rs.getString("sku"));
			priceMasterObj.setCategory(rs.getString("category"));
			priceMasterObj.setPrice(rs.getString("price"));
			priceMasterObj.setStatus(rs.getString("status"));
			priceMasterObj.setImageUrl(rs.getString("image_Url"));
			priceMasterObj.setRemoveItemFlag(rs.getString("remove_Item_Flag"));
			priceMasterList.add(priceMasterObj);
		}
		logger.info("[C]PricingDao::[M]getAllcostChangeDetailsFromMaster -> The Product details list is: " + priceMasterList.toString());
		return priceMasterList;
	}

	public String updateCostChangeDetails(PriceMaster priceMasterObj) throws SQLException, ClassNotFoundException {
		Connection conn = getConnectioDetails();
		String updateStatus = "Invalid";
		try {
			if (priceMasterObj.getRemoveItemFlag().equalsIgnoreCase("Y")) {
				String query = "DELETE FROM SUPPLIER_PORTAL.COST_CHANGE_MASTER WHERE ITEM_ID = ?";
				logger.info(query);
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, priceMasterObj.getItemId());
				int updateStatusCode = pstmt.executeUpdate();
				logger.info(Integer.toString(updateStatusCode));
				updateStatus = (updateStatusCode == 1) ? ("SUCCESS") : ("FAILURE");
				pstmt.close();
			} else {
				String query = "UPDATE SUPPLIER_PORTAL.COST_CHANGE_MASTER SET NEW_SUBMITTED_PRICE = ? WHERE ITEM_ID = ?  ";
				logger.info(query);
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, priceMasterObj.getNewSubmittedPrice());
				pstmt.setString(2, priceMasterObj.getItemId());
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

	public String batchInsertExcelRecords(ArrayList<PriceMaster> priceMasterDBList) throws ClassNotFoundException {
		PreparedStatement preparedStatement;
		try {
			Connection conn = getConnectioDetails();
			conn.setAutoCommit(true);
			String insertQuery = "INSERT INTO SUPPLIER_PORTAL.ITEM_LISTING_MASTER(UNIQUE_ID, ITEM_ID, ITEM_NAME, ITEM_DESCRIPTION, SKU,PRICE,NEW_SUBMITTED_PRICE,STATUS,CATEGORY,IMAGE_URL,REMOVE_ITEM_FLAG,CREATED_DATETIME)"
					+ " VALUES" + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = conn.prepareStatement(insertQuery);
			for(PriceMaster priceMaster: priceMasterDBList) {
				preparedStatement.setString(1, priceMaster.getUniqueId());
				preparedStatement.setString(2, priceMaster.getItemId());
				preparedStatement.setString(3, priceMaster.getItemName());
				preparedStatement.setString(4, priceMaster.getItemDescription());
				preparedStatement.setString(5, priceMaster.getSku());
				preparedStatement.setString(6, priceMaster.getPrice());
				preparedStatement.setString(7, priceMaster.getNewSubmittedPrice());
				preparedStatement.setString(8, priceMaster.getStatus());
				preparedStatement.setString(9, priceMaster.getCategory());
				preparedStatement.setString(10, priceMaster.getImageUrl());
				preparedStatement.setString(11, priceMaster.getRemoveItemFlag());
				preparedStatement.setString(12, priceMaster.getCreatedDateTime());
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

	public String updateCostChangeReqStatus(PriceChangeMaster priceChangeMasterObj) throws SQLException, ClassNotFoundException {
		Connection conn = getConnectioDetails();
		String updateStatus = "Invalid";
		try {
			if(priceChangeMasterObj.getApprovalStatus().equalsIgnoreCase("APPROVED")) {
				
			}else {
				
			}
			String query = "UPDATE SUPPLIER_PORTAL.COST_CHANGE_MASTER SET PRICE =? NEW_SUBMITTED_PRICE = ?, STATUS = ? WHERE ITEM_ID = ?  ";
			logger.info(query);
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, priceChangeMasterObj.getNewPrice());
			pstmt.setString(2, "NOT REQUESTED");
			pstmt.setString(3, priceChangeMasterObj.getApprovalStatus());
			pstmt.setString(4, priceChangeMasterObj.getItemid());
			int updateStatusCode = pstmt.executeUpdate();
			logger.info(Integer.toString(updateStatusCode));
			updateStatus = (updateStatusCode == 1) ? ("SUCCESS") : ("FAILURE");
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return updateStatus;
		
		
	}
	
}
