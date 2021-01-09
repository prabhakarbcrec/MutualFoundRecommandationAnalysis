package com.finaculer.readDataFromExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.finacular.connectionInstanceofPostgres.ConnectToAwsPostgres;
import com.finacular.connectionInstanceofPostgres.GetPostgresConnection;
import com.finacular.constants.ConstantsOfProjects;

public class GetDataFromExcel {
	public static boolean isCreated = true;
	public static boolean isAllowforMe = false;
	public static boolean isAllow = true;
	public static String SchemName = null;

	public static void main(String[] args) throws InterruptedException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, IOException {
		TaskCompleter();
	}

	private static void TaskCompleter() throws IOException, InterruptedException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		FileInputStream file = new FileInputStream(new File(ConstantsOfProjects.FINAL_PATH_OF_EXCCELSHEET));
		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		String sheetsName = workbook.getSheetName(7);
		System.out.println(workbook.getNumberOfSheets() + " Ths is sheet which you are going read: " + sheetsName);
		for (int sheetNumber = 1; sheetNumber < workbook.getNumberOfSheets(); sheetNumber++) {
			System.out.println("This is sheetNumber:" + sheetNumber);
			try {
				isAllow = true;
				//System.out.println("Sleeping...");
				//Thread.sleep(100);

				// Get first sheet from the workbook
				HSSFSheet sheet = workbook.getSheetAt(sheetNumber);
				int RowNum = 0;
				// Iterate through each rows from first sheet
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();

					RowNum++;
					Iterator<Cell> cellforGetHeader = row.cellIterator();
					// For each row, iterate through each columns
					Iterator<Cell> cellIterator = row.cellIterator();

					if (isAllow) {
						// System.out.println("this is info " + cellforGetHeader.next());
						SchemName = cellforGetHeader.next().toString();
						isAllow = false;
					}
//
					ArrayList<Cell> dataToTable = new ArrayList<Cell>();
					Cell data;
					for (int i = 1; i < 9; i++) {

						Cell cell = cellIterator.next();
//						System.out.println("this is info " + cell.getRow().getCell(1));
						if (String.valueOf(cell).equals("EQUITY & EQUITY RELATED") || isAllowforMe) {
							isAllowforMe = true;
							if (cell.toString().startsWith("IN")) {

								for (int i1 = 1; i1 < 8; i1++) {
									data = row.getCell(i1);
									dataToTable.add(data);
									// System.out.println(data);

								}
								for (Cell c : dataToTable) {
									// System.out.print(c + "\t ");
								}
								// System.out.println(dataToTable.get(0));
								pushDataToDatabase(dataToTable);
							}
						} else {
							System.out.println("This file is not valid to inport into Postgres-DB");
						}

					}
					System.out.println("");
				}
				System.out.println("number Row  " + RowNum);
				file.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void pushDataToDatabase(ArrayList<Cell> dataToTable)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		Connection cn = ConnectToAwsPostgres.getConnectionInstance();
		Statement smt = cn.createStatement();
		// -----------------------------------------------------------------------------------------------
		DatabaseMetaData meta = cn.getMetaData();
		ResultSet res = meta.getTables(null, null, "Mutual_F_data", new String[] { "TABLE" });

		// query to create table Employees with fields
		// name(empid,empname,dob,city,salary)
		String q = "create table Mutual_F_data(ISIN varchar(200),Coupon varchar(200),Name_Of_the_Instrument varchar(200),Industry varchar(200),"
				+ "Quantity varchar(200), Fair_Value varchar(200),allocation varchar(200),schemename varchar(600),"
				+ "Sales_Cagr varchar(200),Eps_Cagr varchar(200), De_Ratio varchar(200),Stock_Returns varchar(200),Profit_Cagr varchar(200),Roce varchar(200),fund_house_name varchar(300))";
		String insertQuery = "insert into Mutual_F_data values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement insertStmt = cn.prepareStatement(insertQuery);

		// to execute the update
		if (isCreated) {
			try {
				smt.executeUpdate(q);
				System.out.println("Table Created ....");
				isCreated = false;
			} catch (Exception e) {
				System.out.println("Table Already exist ....");
				isCreated = false;
			}
		}
		insertStmt.setString(1, String.valueOf(dataToTable.get(0)));
		insertStmt.setString(2, String.valueOf(dataToTable.get(1)));
		insertStmt.setString(3, String.valueOf(dataToTable.get(2)));
		insertStmt.setString(4, String.valueOf(dataToTable.get(3)));
		insertStmt.setString(5, String.valueOf(dataToTable.get(4)));
		insertStmt.setString(6, String.valueOf(dataToTable.get(5)));
		insertStmt.setString(7, String.valueOf(dataToTable.get(6)));
		insertStmt.setString(8, SchemName);
		insertStmt.setString(9, "");
		insertStmt.setString(10, "");
		insertStmt.setString(11, "");
		insertStmt.setString(12, "");
		insertStmt.setString(13, "");
		insertStmt.setString(14, "");
		insertStmt.setString(15, "hdfs mutual fund");
		//insertStmt.setString(15, "Hdfc mutual fund");
		insertStmt.execute();

		// cn.close();

		// System.out.println(dataToTable);

	}

}
