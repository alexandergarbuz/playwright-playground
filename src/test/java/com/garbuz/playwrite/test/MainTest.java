package com.garbuz.playwrite.test;

import org.junit.Test;

import com.garbuz.playwrite.page.HomePage;
import com.garbuz.playwrite.page.OtherPage;
import com.garbuz.playwrite.page.PageFactory;

public class MainTest {

	HomePage homePage = PageFactory.lookup(HomePage.class);
	OtherPage otherPage = PageFactory.lookup(OtherPage.class);
	
	@Test
	public void test() throws Exception {
		

		
	}
}
