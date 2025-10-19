package com.garbuz.playwrite.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.garbuz.playwrite.page.HomePage;
import com.garbuz.playwrite.page.SeachResultPage;
import com.garbuz.playwrite.util.PlaywrightRule;

public class WikipediaSearchTest {

    @Rule
    public PlaywrightRule playwrightRule = new PlaywrightRule();
    
	private HomePage homePage;
	private SeachResultPage seachResultPage;
	
	
	@Before
	public void setUp() throws Exception {
		homePage = playwrightRule.getFactory().lookup(HomePage.class);
		seachResultPage = playwrightRule.getFactory().lookup(SeachResultPage.class);		
	}
    

	@Test
	public void verifyWikipediaSearchResults() throws Exception {
		homePage.openHomePage();
		homePage.enterSeachText("Playwrite");
		homePage.clickOnSearchIcon();
		final String title = seachResultPage.getTitle();
		
		Assert.assertEquals("Incorrect title displayed", "Playwright - Wikipedia", title);
	}
	
    

	@Test
	public void verifyWrongWikipediaSearchResults() throws Exception {
		homePage.openHomePage();
		homePage.enterSeachText("Playwrite (Software)");
		homePage.clickOnSearchIcon();
		final String title = seachResultPage.getTitle();
		
		Assert.assertEquals("Incorrect title displayed", "Playwright - Wikipedia", title);
	}
}
