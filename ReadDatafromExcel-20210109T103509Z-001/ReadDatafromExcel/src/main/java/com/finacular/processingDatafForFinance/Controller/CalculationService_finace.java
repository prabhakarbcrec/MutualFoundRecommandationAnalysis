package com.finacular.processingDatafForFinance.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CalculationService_finace {
	/**
	 * this variable will store the position from list which we have need like last
	 * 10 value
	 */
	public static int startPositionOfListToGetOnlyTenValueFromSales, startPositionOfListToGetOnlyTenValueFromSalesEps;

	/**
	 * 
	 * @param salesList this is list which is coming from main Class here we are
	 *                  calculation th sales_cager value and returning it back to
	 *                  the main clss
	 * @return
	 */
	public static float Service_ProcessData_For_Sales(List<String> salesList) {
		// System.out.println("SALES: " + salesList);
		int tempCount = 0;
		for (int i = salesList.size() - 1; i >= 0; i--) {
			if (((tempCount <= 10) && (i >= 0))) {

				tempCount++;
				startPositionOfListToGetOnlyTenValueFromSales = i;
			}

		}

		float firstValue = Float.valueOf(salesList.get(startPositionOfListToGetOnlyTenValueFromSales));
		float lastValue = Float.valueOf(salesList.get(salesList.size() - 1));
		float timeDuration = Float.valueOf(
				(salesList.subList(startPositionOfListToGetOnlyTenValueFromSales, salesList.size()).size() - 1));
		System.out.println("finalListFromWhichWeWillGetFirstValuAndLastValueSales:"
				+ salesList.subList(startPositionOfListToGetOnlyTenValueFromSales, salesList.size())
				
				+ " startValueOfSales: " + firstValue + " lastValueFomList: " + lastValue + " totalTimeDuration: "
				+ timeDuration);
		float divData = (float) lastValue / firstValue;
		System.out.println("forSales,  lastValue / firstValue=" + divData);
		float timediv = (float) 1 / timeDuration;
		System.out.println("1 / timeDuration= " + timediv + "  from indexPostionTotakeTenValueFromListOfSales: "
				+ startPositionOfListToGetOnlyTenValueFromSales);
		float Cagr_Of_salse = (float) Math.pow(divData, timediv);
		System.out.println("Cagr_Of_salse: " + (Cagr_Of_salse - 1));
		return (Cagr_Of_salse - 1);

	}

	/**
	 * 
	 * @param epsList this is list which is coming from main Class here we are
	 *                calculation the eps_cager value and returning it back to the
	 *                main clss
	 * @return
	 */
	public static float Service_ProcessData_For_Eps(List<String> epsList) {
		int tempCount1 = 0;
		for (int i = epsList.size() - 1; i >= 0; i--) {
			if (((tempCount1 <= 10) && (i >= 0))) {

				tempCount1++;
				startPositionOfListToGetOnlyTenValueFromSalesEps = i;
			}

		}
	//	System.out.println("startValueOf_eps: " + epsList.get(startPositionOfListToGetOnlyTenValueFromSalesEps) + " lastValueFromList_eps: " + epsList.get(epsList.size() - 1));
		float firstValue = Float.valueOf(epsList.get(startPositionOfListToGetOnlyTenValueFromSalesEps));
		float lastValue = Float.valueOf(epsList.get(epsList.size() - 1));
		float timeDuration = Float.valueOf((epsList.subList(startPositionOfListToGetOnlyTenValueFromSalesEps, epsList.size()).size() - 1));
		System.out.println("FinalData" + epsList.subList(startPositionOfListToGetOnlyTenValueFromSalesEps, epsList.size()) + " startValueOf_eps: " + firstValue + " lastValueFromList_eps: "
				+ lastValue + " totalTimeDuration: " + timeDuration);
		float divData = (float) lastValue / firstValue;
		System.out.println("eps lastValue / firstValue= " + divData);
		float timediv = (float) 1 / timeDuration;
		System.out.println("1 / timeDuration=  " + timediv);
		float Cagr_Of_eps = (float) Math.pow(divData, timediv);
		System.out.println("Cagr_Of_eps: " + (Cagr_Of_eps - 1));

		return Cagr_Of_eps - 1;
	}

	/**
	 * 
	 * @param roce this is list which is coming from main Class here we are
	 *             calculation the Avg roce for last 5 year and returning it back to
	 *             the main clss
	 * @return
	 */
	public static float Service_ProcessData_for_Roce(ArrayList<String> roce) {
		float AvrgeOfRoceValue1 = 0;
		int tempCount3 = 0;
		System.out.println("ListOf_roce: "+roce);
		for (int i = roce.size() - 1; i >= 0; i--) {
			try {
				if ((tempCount3 < 5 && i >= 0) && !roce.isEmpty()) {

					AvrgeOfRoceValue1 += Float.valueOf(roce.get(i));

					tempCount3++;

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		float roceAvg = AvrgeOfRoceValue1 / tempCount3;
		System.out.println("some of all Roce Value: " + AvrgeOfRoceValue1 + "  total count: " + tempCount3
				+ "  final_roce: " + roceAvg);
		return roceAvg;

	}

	
	
}
