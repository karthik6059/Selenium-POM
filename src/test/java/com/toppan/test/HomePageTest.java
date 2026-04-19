package com.toppan.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.toppan.base.BaseClass;
import com.toppan.pages.HomePage;
import com.toppan.pages.LoginPage;
import com.toppan.utilities.DataProviders;
import com.toppan.utilities.ExtentManager;

public class HomePageTest extends BaseClass {
	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage();
		homePage = new HomePage();
	}

	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyOrangeHRMLogo(String userName, String password) {
		//ExtentManager.startTest("Home Page Verify Logo Test"); // --This has been implemented in TestListener
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginPage.login(userName,password);
		ExtentManager.logStep("Verifying Logo is visible or not");
		Assert.assertTrue(homePage.verifyOrangeHRMlogo(),"Logo is not visible");
		ExtentManager.logStep("Validation Successful");
		homePage.logout();
		ExtentManager.logStep("Logged out Successfully!");
	}
	
	
}
