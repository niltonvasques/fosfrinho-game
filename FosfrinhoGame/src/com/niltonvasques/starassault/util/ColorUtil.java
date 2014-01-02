package com.niltonvasques.starassault.util;

public class ColorUtil {
	
	public static int[] rgbToArray(int rgb){
		int[] arr = new int[3];
		arr[0] = (rgb >> 16);
		arr[1] = (rgb >> 8) & 0xff;
		arr[2] = rgb & 0xff;
		
		return arr;
	}

}
