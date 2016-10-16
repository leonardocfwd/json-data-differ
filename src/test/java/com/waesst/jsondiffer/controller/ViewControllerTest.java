package com.waesst.jsondiffer.controller;

import org.junit.Assert;
import org.junit.Test;


public class ViewControllerTest {

	ViewController viewController = new ViewController();
	
	@Test
	public void test_indexView_returns_index_page(){
		String INDEX_PAGE = "index";
		Assert.assertEquals(viewController.indexView(), INDEX_PAGE);
	}
	
}
