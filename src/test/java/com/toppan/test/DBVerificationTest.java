package com.toppan.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.toppan.base.BaseClass;
import com.toppan.pages.HomePage;
import com.toppan.pages.LoginPage;
import com.toppan.utilities.DBConnection;
import com.toppan.utilities.DataProviders;
import com.toppan.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass {
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage();
		homePage  = new HomePage();
	}
	
	@Test(dataProvider="emplVerification", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameVerificationFromDB(String emplID, String empName) {
		

		
		ExtentManager.logStep("Logging with Admin Credentails");
		loginPage.login(prop.getProperty("usenName"), prop.getProperty("password"));
		
		ExtentManager.logStep("click on PIM tab");
		homePage.clickOnPIMTab();
		
		ExtentManager.logStep("Search for Employee");
		homePage.employeeSearch(empName);
		staticWait(1);
		
		ExtentManager.logStep("Get the Employee Name from DB");
		String employee_id=emplID;
		
		//Fetch the data into a map
		
		Map<String,String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);
		
		String emplFirstName = employeeDetails.get("firstName");
		String emplMiddleName = employeeDetails.get("middleName");
		String emplLastName = employeeDetails.get("lastName");
		
		String emplFirstAndMiddleName =(emplFirstName+" "+emplMiddleName).trim();
		
		//Validation for first and middle name
		ExtentManager.logStep("Verify the employee first and middle name");
		Assert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(emplFirstAndMiddleName),"First and Middle name are not Matching");
		
		//validation for last name
		ExtentManager.logStep("Verify the employee last name");
		Assert.assertTrue(homePage.verifyEmployeeLastName(emplLastName));
		
		ExtentManager.logStep("DB Validation Completed");
	}

	}
