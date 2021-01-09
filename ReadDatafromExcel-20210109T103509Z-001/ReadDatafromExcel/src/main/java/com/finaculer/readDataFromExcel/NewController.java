package com.finaculer.readDataFromExcel;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import com.finacular.connectionInstanceofPostgres.GetPostgresConnection;

public class NewController {

	/**
	 * all list variable i have declared here
	 */
	// .........................................................................................
	public static ArrayList<String> isinList = new ArrayList<String>();
	static ArrayList<String> allocationList = new ArrayList<String>();
	static ArrayList<String> schemName = new ArrayList<String>();

	static ArrayList<String> salse = new ArrayList<String>();
	static ArrayList<String> eps = new ArrayList<String>();
	static ArrayList<String> year = new ArrayList<String>();
	static ArrayList<String> roce = new ArrayList<String>();
	static ArrayList<String> borrowings = new ArrayList<String>();
	static ArrayList<String> reserves = new ArrayList<String>();
	static ArrayList<String> shareCapital = new ArrayList<String>();

	// ........................................................................................

	/**
	 * variables
	 */
	public static int AvrgeOfRoceValue = 0;
	static int tempCount = 0;
	public static String avgRoceValueAftreCalculation;
	public static String SalesFinalCagr;
	public static String EpsFinalCagr;
	public static String SchemNameTOUpdatedatabase;
	public static String isinCodeToUpdateDatabase;
	public static String DeRotio;

	/**
	 * 
	 * @param args
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection con = GetPostgresConnection.getConnectionInstance();
		String getDataQuery = "select * from Mutual_F_data";

		/**
		 * exicute query using GetQueryData()
		 */
		ResultSet DataFromPostgres = getQueryData(getDataQuery, con);
		/***
		 * storing isin ,schemename and allocation value so that we can update correct
		 * row or we will get data from specific row where isin is x
		 */
		while (DataFromPostgres.next()) {
			isinList.add(DataFromPostgres.getString("isin"));
			allocationList.add(DataFromPostgres.getString("allocation"));
			schemName.add(DataFromPostgres.getString("schemename"));
		}

		/**
		 * iteration each and every isin code, for each isin code trying to get data
		 * from screener profit_loss table
		 */
		for (int isinCodeFromList = 0; isinCodeFromList < isinList.size(); isinCodeFromList++) {
			System.out.println("-----------------------------------------------------------------\n");
			SchemNameTOUpdatedatabase = schemName.get(isinCodeFromList);
			isinCodeToUpdateDatabase = isinList.get(isinCodeFromList);
			String CustomQuery = "select * from public.\"Profit_loss\" where isin=\'" + isinCodeToUpdateDatabase + "\'";
			System.out.println("isin_no: " + isinCodeFromList + " " + isinCodeToUpdateDatabase + " from isinList"
					+ "  scemname: " + SchemNameTOUpdatedatabase);
			ResultSet result = getQueryData(CustomQuery, con);
			try {
				/***
				 * calculating data for sales and eps from inside this function
				 * 
				 * @calculateSalesEpsAnd
				 */
				calculateSalesEpsAnd(result);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String CustomQuery1 = "select * from public.\"Ratios\" where isin=" + "\'" + isinCodeToUpdateDatabase
					+ "\'";
			ResultSet result1 = getQueryData(CustomQuery1, con);
			try {
				/***
				 * calculating roce vale inside this function(method)
				 * 
				 * @calculateRoce
				 */
				calculateRoce(result1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// System.out.println(roce);

			String CustomQuery2 = "select * from public.\"Balance_Sheet\" where isin=" + "\'" + isinCodeToUpdateDatabase
					+ "\'";
			ResultSet result2 = getQueryData(CustomQuery2, con);
			try {
				/**
				 * calculation DeRatios inside this function
				 * 
				 * @calculateDeRatio
				 */
				calculateDeRatio(result2);
			} catch (Exception e) {
				e.printStackTrace();
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
				DeRotio="";


			} else {
				System.out.println("This is finance stocks:" + isinCodeFromList);
				EpsFinalCagr = "";
				SalesFinalCagr = "";
				avgRoceValueAftreCalculation = "";
				SchemNameTOUpdatedatabase = "";
				isinCodeToUpdateDatabase = "";
				isinCodeToUpdateDatabase = "";
				DeRotio="";


			}

			System.out.println("-----------------------------------------------------------------\n");

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

	}

	private static void calculateDeRatio(ResultSet result2) throws SQLException {
		while (result2.next()) {
			try {
				borrowings.add(result2.getString("Borrowings"));
				reserves.add(result2.getString("Reserves"));
				shareCapital.add(result2.getString("Share"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**
		 * this function is calculating DE_ratios and returning back
		 */
		borrowings.removeAll(Collections.singleton(null));
		reserves.removeAll(Collections.singleton(null));
		shareCapital.removeAll(Collections.singleton(null));

		DeRotio = String
				.valueOf(CalCulation_service.Service_processData_for_DeRatios(borrowings, reserves, shareCapital));
		System.out.println("finalDeRationValueAftreCalculation: " + DeRotio);
	}

	public static void calculateRoce(ResultSet result) throws SQLException {
		while (result.next()) {
			try {

				roce.add(result.getString("roce"));

			} catch (Exception e) {
				e.printStackTrace();
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
			avgRoceValueAftreCalculation = String.valueOf(CalCulation_service.Service_ProcessData_for_Roce(roce));
		}
	}

	public static void calculateSalesEpsAnd(ResultSet result) throws SQLException {
		// TODO Auto-generated method stub
		while (result.next()) {
			try {

				salse.add(result.getString("Sales"));
				eps.add(result.getString("EPS"));
				year.add(result.getString("year"));

			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		System.out.println("RawListOfSales: " + salse);
		System.out.println("RawListOfEps: " + eps);
		System.out.println("RawListOfYear: " + year);
		/**
		 * this function is calculating sales_cagr and returning back
		 */
		if (!salse.isEmpty()) {
			SalesFinalCagr = String
					.valueOf(CalCulation_service.Service_ProcessData_For_Sales(salse.subList(0, salse.size() - 1)));
		}
		/**
		 * this function is calculating Eps_cagr and returning back
		 */
		if (!eps.isEmpty()) {
			EpsFinalCagr = String
					.valueOf(CalCulation_service.Service_ProcessData_For_Eps(eps.subList(0, eps.size() - 1)));
		}
		System.out.println(
				"AfterCalculation_Sales_cager: " + SalesFinalCagr + "   AfterCalculation_Eps_cager: " + EpsFinalCagr);

	}

	public static ResultSet getQueryData(String query, Connection con) throws SQLException {
		String getDataQuery = query;
		Statement getDataStmt = con.createStatement();
		ResultSet DataFromPostgres = getDataStmt.executeQuery(getDataQuery);
		return DataFromPostgres;

	}
}
