package com.cex.util;

import java.util.List;

import net.sf.oval.ConstraintViolation;

public class OvalUtil {
	
	private static final String sep = "\n";
	
	public static String getViolations(List<ConstraintViolation> violations) {
		StringBuilder rString = new StringBuilder();
		for (ConstraintViolation each : violations) {
			rString.append(sep).append(each.getMessage());
		}
		return rString.toString();
	}
}
