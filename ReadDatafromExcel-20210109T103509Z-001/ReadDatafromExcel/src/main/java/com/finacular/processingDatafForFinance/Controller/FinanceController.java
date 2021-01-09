package com.finacular.processingDatafForFinance.Controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import com.finacular.connectionInstanceofPostgres.GetPostgresConnection;
import com.finaculer.readDataFromExcel.InsertProcessedDataIntoPostgresdb;

public class FinanceController {

	static ArrayList<String> isinList = new ArrayList<String>();
	static ArrayList<String> allocationList = new ArrayList<String>();
	static ArrayList<String> schemName = new ArrayList<String>();
	public static int AvrgeOfRoceValue = 0;
	static int tempCount = 0;

	static ArrayList<String> salse = new ArrayList<String>();
	static ArrayList<String> eps = new ArrayList<String>();
	static ArrayList<String> year = new ArrayList<String>();
	static ArrayList<String> roce = new ArrayList<String>();
	static ArrayList<String> borrowings = new ArrayList<String>();
	static ArrayList<String> reserves = new ArrayList<String>();
	static ArrayList<String> shareCapital = new ArrayList<String>();

	// -------------final variables which i am going to use to inser data into


	public static String avgRoceValueAftreCalculation;
	public static String SalesFinalCagr;
	public static String EpsFinalCagr;
	public static String SchemNameTOUpdatedatabase;
	public static String isinCodeToUpdateDatabase;
	public static String DeRotio;

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection con = GetPostgresConnection.getConnectionInstance();
		String getDataQuery = "select * from Mutual_F_data";

		ResultSet DataFromPostgres = getQueryData(getDataQuery, con);
		/**
		 * Table will be like Columns[isin, allocation,
		 * IsThisFinaceStocks,Sales_Cagr,Eps_Cagr,Roce,De_Ratio,Stock_Returns,Profit_Cagr]
		 */
		while (DataFromPostgres.next()) {
			isinList.add(DataFromPostgres.getString("isin"));
			allocationList.add(DataFromPostgres.getString("allocation"));
			schemName.add(DataFromPostgres.getString("schemename"));

		}

		for (int isinCodeFromList = 0; isinCodeFromList < isinList.size(); isinCodeFromList++) {
			SchemNameTOUpdatedatabase = schemName.get(isinCodeFromList);
			isinCodeToUpdateDatabase = isinList.get(isinCodeFromList);
			String CustomQuery = "select * from public.\"Profit_loss_Finance\" where isin=\'" + isinCodeToUpdateDatabase
					+ "\'";
			System.out.println(" This a isin: " + isinList.get(isinCodeFromList));
			ResultSet result = getQueryData(CustomQuery, con);
			try {
				/***
				 * calculating data for sales and eps from inside this function
				 * 
				 * @calculateSalesEpsAnd
				 */
				calculateSalesEpsAnd(result);
			} catch (Exception e) {
				// TODO: handle exception
			}

			String CustomQuery1 = "select * from public.\"Ratios_Finance\" where isin=" + "\'"
					+ isinCodeToUpdateDatabase + "\'";
			ResultSet result1 = getQueryData(CustomQuery1, con);
			try {
				/***
				 * calculating roce vale inside this function(method)
				 * 
				 * @calculateRoce
				 */
				calculateRoce(result1);
			} catch (Exception e) {
				// TODO: handle exception
			}
			System.out.println(roce);

			String CustomQuery2 = "select * from public.\"Balance_Sheet_Finance\" where isin=" + "\'"
					+ isinList.get(isinCodeFromList) + "\'";
			ResultSet result2 = getQueryData(CustomQuery2, con);
			try {
				/**
				 * calculation DeRatios inside this function
				 * 
				 * @calculateDeRatio
				 */
				calculateDeRatio(result2);
			} catch (Exception e) {
				// TODO: handle exception
			}
			/**
			 * This function is responsible to push data into Postgres_db
			 */
			if (!salse.isEmpty() && !eps.isEmpty()) {
				InsertProcessedDataIntoPostgresdb.insert(String.valueOf(EpsFinalCagr), String.valueOf(SalesFinalCagr),
						String.valueOf(avgRoceValueAftreCalculation), String.valueOf(SchemNameTOUpdatedatabase),
						isinCodeToUpdateDatabase, String.valueOf(DeRotio));
				EpsFinalCagr = "";
				SalesFinalCagr = "";
				avgRoceValueAftreCalculation = "";
				SchemNameTOUpdatedatabase = "";
				isinCodeToUpdateDatabase = "";
				isinCodeToUpdateDatabase = "";
				

			} else {
				System.out.println("This is finance stocks:" + isinCodeFromList);
				EpsFinalCagr = "";
				SalesFinalCagr = "";
				avgRoceValueAftreCalculation = "";
				SchemNameTOUpdatedatabase = "";
				isinCodeToUpdateDatabase = "";
				isinCodeToUpdateDatabase = "";

			}
			/**
			 * after each iteration clearing list here
			 */
			salse.clear();
			year.clear();
			eps.clear();
			roce.clear();
			reserves.clear();
			borrowings.clear();
			shareCapital.clear();

		}

		System.out.println(isinList.size());
		System.out.println(allocationList.size());
		System.out.println(schemName.size());
	}

	private static void calculateDeRatio(ResultSet result2) throws SQLException {
		while (result2.next()) {
			try {
				borrowings.add(result2.getString("Borrowings"));
				reserves.add(result2.getString("Reserves"));
				shareCapital.add(result2.getString("Share"));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		/**
		 * this function is calculating DE_ratios and returning back
		 */
		DeRotio = "_";
	}

	public static void calculateRoce(ResultSet result) throws SQLException {
		while (result.next()) {
			try {

				roce.add(result.getString("ROE"));

			} catch (Exception e) {

			}
		}
		/**
		 * removing null value from list @roce
		 */
		roce.removeAll(Collections.singleton(null));
		/**
		 * this function is calculating Roce and returning back
		 */
		if (!roce.isEmpty()) {
			avgRoceValueAftreCalculation = String.valueOf(CalculationService_finace.Service_ProcessData_for_Roce(roce));
		}
	}

	public static void calculateSalesEpsAnd(ResultSet result) throws SQLException {
		// TODO Auto-generated method stub
		while (result.next()) {
			try {

				salse.add(result.getString("Revenue"));
				eps.add(result.getString("EPS"));
				year.add(result.getString("year"));

			} catch (Exception e) {

			}
		}

		System.out.println("Revenue: " + salse);
		System.out.println("Raw_Data: " + eps);
		/**
		 * this function is calculating sales_cagr and returning back
		 */
		if (!salse.isEmpty()) {
			SalesFinalCagr = String.valueOf(
					CalculationService_finace.Service_ProcessData_For_Sales(salse.subList(0, salse.size() - 1)));
		}
		/**
		 * this function is calculating Eps_cagr and returning back
		 */
		if (!eps.isEmpty()) {
			EpsFinalCagr = String
					.valueOf(CalculationService_finace.Service_ProcessData_For_Eps(eps.subList(0, eps.size() - 1)));
		}
		System.out.println("sales_cager: " + SalesFinalCagr + "   Eps_cager: " + EpsFinalCagr);
		System.out.println("-----------------------------------------------------------------");

	}

	public static ResultSet getQueryData(String query, Connection con) throws SQLException {
		String getDataQuery = query;
		Statement getDataStmt = con.createStatement();
		ResultSet DataFromPostgres = getDataStmt.executeQuery(getDataQuery);
		return DataFromPostgres;

	}
}
