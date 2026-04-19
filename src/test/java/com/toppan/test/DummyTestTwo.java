package com.toppan.test;


import org.testng.annotations.Test;

import com.toppan.base.BaseClass;
import com.toppan.utilities.ExtentManager;

public class DummyTestTwo extends BaseClass {
	@Test
	public void dummyTest2() {
		//ExtentManager.startTest("DummyTest2 Test"); //-This has been implemented in TestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("verifying the title");
		assert title.equals("OrangeHRM") : "Test Failed - Title is Not Matching";

        System.out.println("Test Passed - Title is Matching");
        ExtentManager.logStep("Validation Successful");
	}
}

