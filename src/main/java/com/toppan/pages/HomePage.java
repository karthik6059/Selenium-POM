package com.toppan.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.toppan.actiondriver.ActionDriver;
import com.toppan.base.BaseClass;
import com.toppan.utilities.LoggerManager;

public class HomePage {

	private ActionDriver actionDriver;

	// Define locators using By class
	private By menuButton = By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[1]/div[1]/i");
	private By closeMenu = By.xpath("//*[@id=\"app\"]/div[1]/div[1]/aside/nav/div[1]/i");
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIDButton = By.className("oxd-userdropdown-name");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By oranageHRMlogo = By.xpath("//div[contains(@class, 'oxd-brand-banner')]//img");

	private By pimTab = By.xpath("//span[text()='PIM']");
	private By employeeSearch = By
			.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
	private By searchButton = By.xpath("//button[@type='submit']");
	private By emplFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private By emplLastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");

	private static final Logger logger = LoggerManager.getLogger(HomePage.class);

	// Initialize the ActionDriver object by passing WebDriver instance
	public HomePage() {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public void expandMenu() {
		actionDriver.click(menuButton);
	}
	public void closeMenu() {
		actionDriver.click(closeMenu);
	}
	
	// Method to verify if Admin tab is visible
	public boolean isAdminTabVisible() {
		logger.info("verify admin tab is visible");
		expandMenu();
		return actionDriver.isDisplayed(adminTab);
	}

	public boolean verifyOrangeHRMlogo() {
		expandMenu();
		return actionDriver.isDisplayed(oranageHRMlogo);
	}

	// Method to Navigate to PIM tab
	public void clickOnPIMTab() {
		actionDriver.click(pimTab);
	}

	// Employee Search
	public void employeeSearch(String value) {
		actionDriver.enterText(employeeSearch, value);
		actionDriver.click(searchButton);
		actionDriver.scrollToElement(emplFirstAndMiddleName);
	}

	// Verify employee first and middle name
	public boolean verifyEmployeeFirstAndMiddleName(String emplFirstAndMiddleNameFromDB) {
		return actionDriver.compareText(emplFirstAndMiddleName, emplFirstAndMiddleNameFromDB);
	}

	// Verify employee first and middle name
	public boolean verifyEmployeeLastName(String emplLastFromDB) {
		return actionDriver.compareText(emplLastName, emplLastFromDB);
	}

	// Method to perform logout operation
	public void logout() {
		closeMenu();
		actionDriver.click(userIDButton);
		actionDriver.click(logoutButton);
	}
}
