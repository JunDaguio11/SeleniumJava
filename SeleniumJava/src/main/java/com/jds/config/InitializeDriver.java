package com.jds.config;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.annotations.AfterClass;

public class InitializeDriver {
	public static WebDriver driver;
	public static String root = System.getProperty("user.dir");
		
	public static WebDriver initializeDriver() {
		Properties prop = new Properties();
		String browser = "";
		Long implicitWait = (long) 10;

		// Load Property
		try {
			// load a properties file from class path, inside static method
			InputStream input = new FileInputStream("Config/Configuration.properties");
			prop.load(input);

			// get the property value and print it out
			browser = prop.getProperty("driver");
			//url = prop.getProperty("url");
			implicitWait = Long.parseLong(prop.getProperty("implicitlyWait"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Initialize Driver
		switch (browser) {
		case "Chrome":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		case "Edge":
			WebDriverManager.edgedriver().setup();
			EdgeOptions edgeOptions = new EdgeOptions();
			driver = new EdgeDriver(edgeOptions);
			break;
		case "IE":
			WebDriverManager.iedriver().setup();
			InternetExplorerOptions options = new InternetExplorerOptions();
			driver = new InternetExplorerDriver(options);
			break;
		case "Firefox":
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			driver = new FirefoxDriver(firefoxOptions);
			break;
		case "Safari":
			WebDriverManager.safaridriver().setup();
			SafariOptions safariOptions = new SafariOptions();
			driver = new SafariDriver(safariOptions);
			break;
		default:
			break;
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		driver.manage().window().maximize();
		//driver.get(url);
		return driver;
	}
	
	  @AfterClass public void afterClass() { driver.quit(); }
	  
}