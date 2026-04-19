package com.toppan.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;


import com.toppan.actiondriver.ActionDriver;
import com.toppan.base.BaseClass;
import com.toppan.utilities.LoggerManager;

public class LoginPage {

	private ActionDriver actionDriver;

	// Initialize action driver class by passing driver;
	public LoginPage() {
		this.actionDriver = BaseClass.getActionDriver();
	}
	// Define locators using By class
	private By userNameField = By.name("username");
	private By passworField = By.cssSelector("input[type='password']");
	private By loginButton = By.xpath("//button[text()=' Login ']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
	
	private static final Logger logger = LoggerManager.getLogger(LoginPage.class); 

	// method to perform login
	public void login(String userName, String password) {
		logger.info("Login with credentials "+ userName + ":"+ password);
		actionDriver.enterText(userNameField, userName);
		actionDriver.enterText(passworField, password);
		actionDriver.click(loginButton);
	}

	// Method to check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}

	// Method to get the text from Error message
	public String getErrorMessageText() {
		return actionDriver.getText(errorMessage);
	}

	// Verify if error is correct or not
	public boolean verifyErrorMessage(String expectedError) {
		return actionDriver.compareText(errorMessage, expectedError);
	}
}
