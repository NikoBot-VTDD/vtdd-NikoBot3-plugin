package com.github.smallru8.util;

public class RegularExpression {
	public static boolean isDigitOnly(String str) {
		return str.matches("[0-9]*");
	}
	
	public static boolean isUpperOnly(String str) {
		return str.matches("[A-Z]*");
	}
	
	public static boolean isLowerOnly(String str) {
		return str.matches("[a-z]*");
	}
	
	public static boolean isLetterOnly(String str) {
		return str.matches("[A-Za-z]*");
	}
	
	public static boolean isLetterDigitOnly(String str) {
		return str.matches("[A-Za-z0-9]*");
	}
}
