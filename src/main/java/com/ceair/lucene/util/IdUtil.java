package com.ceair.lucene.util;

import java.util.UUID;

public class IdUtil {

	private IdUtil() {
		
	}
	
	public static String generatorId() {
		String id = UUID.randomUUID().toString().replace("-", "");
		return id;
	}
}
