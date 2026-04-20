package com.toppan.test;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.toppan.base.BaseClass;
import com.toppan.pages.HomePage;
import com.toppan.pages.LoginPage;
import com.toppan.utilities.DataProviders;
import com.toppan.utilities.ExtentManager;
import com.toppan.utilities.LoggerManager;

public class LoginPageTest extends BaseClass {
	private LoginPage loginPage;
	private HomePage homePage;

	private static final Logger logger = LoggerManager.getLogger(LoginPageTest.class);

	public LoginPageTest() {
		System.out.println("LoginPageTest is created");
	}

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage();
		homePage = new HomePage();
	}

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String userName, String password) {
		logger.info("login with valid credentials");
		// ExtentManager.startTest("Valid Login Test");
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginPage.login(userName, password);
		ExtentManager.logStep("Verifying Admin tab is visible or not");
		Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab should be visible after successfull login ");
		ExtentManager.logStep("Validation Successful");
		homePage.logout();
		ExtentManager.logStep("Logged out Successfully!");
		staticWait(2);
	}

	@Test(dataProvider = "inValidLoginData", dataProviderClass = DataProviders.class)
	public void inValidLoginTest(String userName, String password) {
		// ExtentManager.startTest("In-valid Login Test!")
		logger.info("login with in valid credentials");
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginPage.login(userName, password);
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test Failed: Invalid error message");
		ExtentManager.logStep("Validation Successful");
	}
}
