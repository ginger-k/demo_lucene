package com.ceair.lucene.test;

import java.util.List;

import org.junit.Test;

import com.ceair.lucene.service.HomeSearch;

public class HomeSearchTest {
	
	@Test
	public void homeSearchTest() throws Exception {
		HomeSearch homeSearch = new HomeSearch();
		List<String> ids = homeSearch.getIdsByKeyword("修改");
		for (String id : ids) {
			System.out.println("id: " + id);
		}
	}

}
