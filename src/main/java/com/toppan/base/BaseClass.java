package com.toppan.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.toppan.actiondriver.ActionDriver;
import com.toppan.utilities.ExtentManager;
import com.toppan.utilities.LoggerManager;

public class BaseClass {

	/*
	 * Base class of our automation framework developed by Karthik on April 8 2026
	 * This class is responsible for browser setup /tear down , browser init , load
	 * config properties
	 */

	// protected static WebDriver driver; //turning to threadlocal
	protected static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	protected static Properties prop;
	// private static ActionDriver actionDriver;
	protected static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<ActionDriver>();

	private static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	@BeforeSuite
	public void loadConfig() throws IOException {
		logger.trace("Beforesuite: Entry of method loadConfig");
		// Load properties file
		prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/resources/Config.properties");
			logger.debug("Attmpting to load config file");
			prop.load(fis);
			logger.info("Success - Config.property file is loaded");

		} catch (IOException e) {
			logger.fatal("Unable to load config file");
			throw new IOException("Unable to laod config file");
		}
		// Start the Extent Report
		// ExtentManager.getReporter(); //--This has been implemented in TestListener
		logger.trace("Exit-from before suite");
	}

	private void launchBrowser() {
		// Load browser details from config properties file
		logger.trace("Exit-from before suite");
		String browser = prop.getProperty("browser");
		if (browser == null || browser.isEmpty()) {
			logger.error("Value of browser is not intialsed in peorpeties file");
			throw new IllegalArgumentException("Invalid browser in config properties");
		}
		logger.debug("Setting up the browser - Based on Browser type");
		/*
		 * if (browser.equalsIgnoreCase("chrome")) { //driver = new ChromeDriver();
		 * driver.set(new ChromeDriver()); System.out.println(
		 * Thread.currentThread().getId());
		 * logger.info("Driver is initialised with chrome browser");
		 * ExtentManager.registerDriver(getDriver()); } else if
		 * (browser.equalsIgnoreCase("firefox")) { //driver = new FirefoxDriver();
		 * driver.set(new FirefoxDriver()); System.out.println(
		 * Thread.currentThread().getId());
		 * logger.info("Driver is initialised with FireFox browser");
		 * ExtentManager.registerDriver(getDriver()); } else if
		 * (browser.equalsIgnoreCase("edge")) { //driver = new EdgeDriver();
		 * driver.set(new EdgeDriver()); System.out.println(
		 * Thread.currentThread().getId());
		 * logger.info("Driver is initialised with Edge browser");
		 * ExtentManager.registerDriver(getDriver()); } else {
		 * logger.error("Invalid browsername in config"); throw new
		 * IllegalArgumentException("Invalid Browser Parameter " + browser); }
		 */
		if (browser.equalsIgnoreCase("chrome")) {

			// Create ChromeOptions
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless=new");
			options.addArguments("--window-size=1920,1080");
			options.addArguments("--force-device-scale-factor=1");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");

			// driver = new ChromeDriver();
			driver.set(new ChromeDriver(options)); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is created.");
		} else if (browser.equalsIgnoreCase("firefox")) {

			// Create FirefoxOptions
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless"); // Run Firefox in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
			options.addArguments("--width=1920"); // Set browser width
			options.addArguments("--height=1080"); // Set browser height
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD environments
			options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments

			// driver = new FirefoxDriver();
			driver.set(new FirefoxDriver(options)); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Instance is created.");
		} else if (browser.equalsIgnoreCase("edge")) {

			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); // Run Edge in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU acceleration
			options.addArguments("--window-size=3440,1440"); // Set window size
			options.addArguments("--disable-notifications"); // Disable pop-up notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD
			options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes

			// driver = new EdgeDriver();
			driver.set(new EdgeDriver(options)); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver Instance is created.");
		} else {
			throw new IllegalArgumentException("Browser Not Supported:" + browser);
		}
	}

	private void configureBrowser() {
		// implementing implicit wait
		logger.trace("Entry of method configureBrowser");
		logger.debug("Configuring implicit wait");
		int iWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(iWait));

		// maximize the browser
		getDriver().manage().window().maximize();
		logger.info("browser window is maximsied");

		// navigate to url
		String url = prop.getProperty("url");
		if (url == null || url.isEmpty()) {
			logger.error("Invalid URL in config properties");
			throw new IllegalArgumentException("Invalid URL in config properties");
		}
		getDriver().get(url);
		logger.info("navigated to the application url");
	}

	// wait method for small nano seconds
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	// Getter Method for WebDriver
	public static WebDriver getDriver() {

		if (driver.get() == null) {
			logger.error("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();

	}

	// Getter Method for ActionDriver
	public static ActionDriver getActionDriver() {

		if (actionDriver.get() == null) {
			logger.error("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver.get();

	}

	/*
	 * // Set Driver method public void setDriver(WebDriver setDriver) { driver =
	 * setDriver; }
	 */

	// get config property method and make it static
	public static Properties getProp() {
		return prop;
	}

	@BeforeMethod
	public void setup() throws IOException {
		System.out.println("Setting up Wbedriver for " + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);

		/*
		 * // Initialize the actionDriver only once if (actionDriver == null) {
		 * //actionDriver = new ActionDriver(getDriver()); actionDriver.set(new
		 * ActionDriver(getDriver()));
		 * logger.info("ActionDriver initlialized for thread: " +
		 * Thread.currentThread().getId()); System.out.println(
		 * Thread.currentThread().getId()); }
		 */

		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initlialized for thread: " + Thread.currentThread().getId());
		System.out.println(Thread.currentThread().getId());

	}

	@AfterMethod
	public void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				logger.error("Unable to quit the browser");
				System.out.println("Unable to quit the browser " + e.getMessage());
			}

			// ExtentManager.endTest(); // -This has been implemented in TestListener
		}
		driver.remove();
		actionDriver.remove();
	}

}
