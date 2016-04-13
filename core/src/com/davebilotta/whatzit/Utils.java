package com.davebilotta.whatzit;

import java.util.ArrayList;
import java.util.Iterator;

public class Utils {
	
	private static long lastTime;

	public static void log (String msg) {
		System.out.println(msg);
	}
	
	public static void log(int num) { 
		System.out.println(num);
	}
	
	public static boolean inList(int num, int[] array) {
		if (array == null) {
			return false;
		}
		else { 
		boolean in = false;
		for (int i : array) {
			// TODO: Can be made more efficient - can drop out as soon as we've found something
			if (num == i) in = true; 
		}
		return in;
		}
	}
	
	public static String pretty(String[] str) {
		return pretty(str,"");
	}
	
	public static String pretty(String[] str,String delim) {
		String retVal = "";
		
		if (str != null) {
			 
			for (String i : str) {
				retVal += i + delim;
			}
		}
		
		return retVal.trim();
	}
		
	public static String pretty(int[] str) {
		return pretty(str,"");
	}
	
	public static String pretty(int[] str, String delim) {
		String retVal = "";
		
		if (str != null) {
			for (int i : str) {
				retVal += i + delim;
			}
		}
		
		return retVal.trim();
	}
	
	public static String pretty(ArrayList list,String delim) { 
		String retVal = "";
		Iterator iter = list.iterator();
		
		while (iter.hasNext()) {
			retVal += iter.next() +"" + delim;
		}
		
		return retVal.trim();
		
	}
	
	// Timer - eventually move these into own class?
	public static void startTimer() { 
		lastTime = System.currentTimeMillis();
		
	}
	
	public static void stopTimer() { 
		long d = System.currentTimeMillis() - lastTime;
		log("Operation took " + d +"ms (" + (float)d/1000 + "s)");
	}
	
}

