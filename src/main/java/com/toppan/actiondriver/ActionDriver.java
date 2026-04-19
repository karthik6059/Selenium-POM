package com.toppan.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.toppan.base.BaseClass;
import com.toppan.utilities.ExtentManager;
import com.toppan.utilities.LoggerManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;

	private static final Logger logger = LoggerManager.getLogger(ActionDriver.class);

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	}
  /*  //perform optimisation code 
	private WebElement getElement(By by) {

		try {
			return driver.findElement(by);
		} catch (Exception e) {

			WebDriverWait smartWait = new WebDriverWait(driver, Duration.ofSeconds(3));

			return smartWait.until(ExpectedConditions.presenceOfElementLocated(by));
		}
	}*/

	// Method to click an element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeClickable(by);
			applyBorder(by, "green");
			driver.findElement(by).click();
			logger.info("Double-clicked on element --> " + elementDescription);
			ExtentManager.logStep("clicked an element: " + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element:",
					elementDescription + "_unable to click");
			logger.error("Element is not clickable " + e.getMessage());
		}
	}

	// Method to enter text in to an element
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on " + getElementDescription(by) + "-->" + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to enter the value " + e.getMessage());
		}
	}

	// Method to getText
	public String getText(By by) {
		try {
			logger.info("Retrivong text from element");
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			return driver.findElement(by).getText();

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to retrive the text " + e.getMessage());
			return "";
		}
	}

	// Method to compare Two text -- changed the return type
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				System.out.println("Texts are Matching:" + actualText + " equals " + expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Text Verified Successfully! " + actualText + " equals " + expectedText);
				return true;
			} else {
				applyBorder(by, "red");
				System.out.println("Texts are not Matching:" + actualText + " not equals " + expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparison Failed!",
						"Text Comparison Failed! " + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to compare Texts:" + e.getMessage());
			return false;
		}
	}

	// wait for element to be displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element is displayed " + getElementDescription(by));
			ExtentManager.logStep("Element is displayed: " + getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed: ",
					"Element is displayed: " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed: ",
					"Elemenet is not displayed: " + getElementDescription(by));
			logger.error("Element is not displayed: " + e.getMessage());
			return false;
		}
	}

	// Wait for the page to load
	/*
	 * wait.withTimeout(Duration.ofSeconds(timeOutInSec)) .until(new
	 * Function<WebDriver, Boolean>() {
	 * 
	 * @Override public Boolean apply(WebDriver driver) { // Cast driver to
	 * JavascriptExecutor to run JS JavascriptExecutor js = (JavascriptExecutor)
	 * driver;
	 * 
	 * // Execute JS and check if page is fully loaded return
	 * js.executeScript("return document.readyState").equals("complete"); } });
	 */
	public void waitForPageLoad(int timeOutInSec) {
		try {
			// overwriting existing wait
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded successfully.");
		} catch (Exception e) {
			logger.error("Page did not load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
		}
	}

	// Scroll to an element -- Added a semicolon ; at the end of the script string
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to locate element:" + e.getMessage());
		}
	}

	// wait condition for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("element is not clickable " + e.getMessage());
		}
	}

	// wait condition for element to be visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("element is not visible " + e.getMessage());
		}
	}

	// Method to get the description of an element using By locator
	public String getElementDescription(By locator) {
		// Check for null driver or locator to avoid NullPointerException
		if (driver == null) {
			return "Driver is not initialized.";
		}
		if (locator == null) {
			return "Locator is null.";
		}

		try {
			// Find the element using the locator
			WebElement element = driver.findElement(locator);

			// Get element attributes
			String name = element.getDomProperty("name");
			String id = element.getDomProperty("id");
			String text = element.getText();
			String className = element.getDomProperty("class");
			String placeholder = element.getDomProperty("placeholder");

			// Return a description based on available attributes
			if (isNotEmpty(name)) {
				return "Element with name: " + name;
			} else if (isNotEmpty(id)) {
				return "Element with ID: " + id;
			} else if (isNotEmpty(text)) {
				return "Element with text: " + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with class: " + className;
			} else if (isNotEmpty(placeholder)) {
				return "Element with placeholder: " + placeholder;
			} else {
				return "Element located using: " + locator.toString();
			}
		} catch (Exception e) {
			// Log exception for debugging
			e.printStackTrace(); // Replace with a logger in a real-world scenario
			logger.error("Unable to describe element due to error: " + e.getMessage());
			return "Unable to describe element due to error: " + e.getMessage();
		}
	}

	// Utility method to check if a string is not null or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility method to truncate long strings
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}

	// Utility Method to Border an element
	public void applyBorder(By by, String color) {
		try {
			// Locate the element
			WebElement element = driver.findElement(by);
			// Apply the border
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied the border with color " + color + " to element: " + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply the border to an element: " + getElementDescription(by), e);
		}
	}
}
