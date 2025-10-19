package com.garbuz.playwrite.page;

import com.microsoft.playwright.Locator;

public class HomePage extends BasePage {

    public void openHomePage() {
    	log("Navigating to home page");
        page.navigate("https://www.wikipedia.org");
    }

    public void clickOnSearchIcon() {
    	log("Clicking on seach icon");
    	page.locator("button[type=\"submit\"]").click();
    	
    }
    public void enterSeachText(final String textToEnter) {
    	log("Entering search text: '" + textToEnter +"'");
    	Locator locator = this.page.locator("input[id=\"searchInput\"]");
    	locator.fill(textToEnter);
    }
	
}
