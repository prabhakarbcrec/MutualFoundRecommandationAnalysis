package com.finaculer.readDataFromExcel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.finacular.connectionInstanceofPostgres.ConnectToAwsPostgres;

public class InsertProcessedDataIntoPostgresdb {
	
	/**
	 * 
	 * @param EpsFinalCagr this is eps final value
	 * @param SalesFinalCagr
	 * @param avgRoceValueAftreCalculation
	 * @param SchemNameTOUpdatedatabase
	 * @param isinCodeToUpdateDatabase
	 * @param DeRotio
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * using this function we are updating data into mutual_f_data able according g to their isin code and schemename 
	 */
	
	public static void insert(String EpsFinalCagr, String SalesFinalCagr, String avgRoceValueAftreCalculation,
			String SchemNameTOUpdatedatabase, String isinCodeToUpdateDatabase, String DeRotio)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection cn = ConnectToAwsPostgres.getConnectionInstance();
		Statement smt = cn.createStatement();

		/**
		 * Table will be like Columns[isin, allocation,
		 * IsThisFinaceStocks,Sales_Cagr,Eps_Cagr,Roce,De_Ratio,Stock_Returns,Profit_Cagr]
		 */

		String updateQuery = "UPDATE public.mutual_f_data SET sales_cagr=?, eps_cagr=?, de_ratio=?, stock_returns=?, profit_cagr=?, roce=?"
				+ " WHERE isin=" + "\'" + isinCodeToUpdateDatabase + "\'" + "and schemename=" + "\'"
				+ SchemNameTOUpdatedatabase + "\'";
		System.out.println(updateQuery);

		PreparedStatement updatetStmt = cn.prepareStatement(updateQuery);

		updatetStmt.setString(1, SalesFinalCagr);
		updatetStmt.setString(2, EpsFinalCagr);
		updatetStmt.setString(3, DeRotio);
		updatetStmt.setString(4, "");
		updatetStmt.setString(5, "");
		updatetStmt.setString(6, avgRoceValueAftreCalculation);
		updatetStmt.executeUpdate();
		System.out.println("Inserted succesfully");

	}

}
