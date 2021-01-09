package com.finaculer.readDataFromExcel;

public class test {
	public static void main(String[] args) {
		float max = 10000;
		float min = 1000;
		float temp = (max / min);
		float val1 = 10;
		float time = (1 / val1);
		
		double final1 = Math.pow(temp, time);
		System.out.println(final1 - 1);


		
		
		float endValueOfEps = 10000;
		float statartValueOfEps = 1000;
		// System.out.println(endValueOfEps + " / " + statartValueOfEps);
		float divEndStartOfEps = endValueOfEps / statartValueOfEps;
		
		float timeDurationOfEps =10;
		float EpsFinalCagr = (float) (Math.pow(divEndStartOfEps, (1 / timeDurationOfEps)) - 1);
		System.out.println(EpsFinalCagr);
	}

}
