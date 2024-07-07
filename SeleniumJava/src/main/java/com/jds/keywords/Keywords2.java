package com.jds.keywords;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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
import org.testng.annotations.BeforeClass;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.jds.config.InitializeDriver;
import com.jds.config.InitializeDriver2;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Keywords2 extends InitializeDriver2{
	public WebDriver driver;
	public InitializeDriver aDriver;
	
	public Keywords2() {
		//driver = InitializeDriver.driver;
		driver = aDriver.initializeDriver();
	}
	
	/**
	 * Method to get the object property as By
	 * @param property -- string value from properties file
	 * @return
	 */
	public By getObject(String property) {
		String[] arr = property.split("=");
		
		switch(arr[0]) {
			case "id":
				return By.id(arr[1]);
			case "xpath":
				return By.xpath(arr[1]);
			case "class":
				return By.className(arr[1]);
			case "name":
				return By.name(arr[1]);
			case "cssSelector":
				return By.cssSelector(arr[1]);
			case "link":
				return By.linkText(arr[1]);
		}
		return null;
	}
	
	/**
	 * Method to open the url
	 * @param url -- URL of the application
	 */
	public void openApplication (String url) {	
		try {
			driver.get("https://demowebshop.tricentis.com/");
			InitializeDriver2.childTest.pass("PASS :: Successfully opened Application - " + url);
		} catch (Exception e) {
			e.printStackTrace();
			InitializeDriver2.childTest.fail("FAIL :: Unable to  open Application - " + url);
		}
	}
	
	/**
	 * Method to perform Click on a specific element
	 * @param locator -- The properties of the element from Object Repository
	 * @param elementName -- Name of the element
	 */
	public void click(String locator, String elementName) throws Exception{
		try {
			driver.findElement(this.getObject(locator)).click();
			InitializeDriver2.childTest.pass("PASS :: Successfully performed Click on element - " + elementName);
		} catch (Exception e) {
			e.printStackTrace();
			InitializeDriver2.childTest.fail("FAIL :: Unable to perform Click on element -  " + elementName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(captureScreen()).build());
			InitializeDriver2.childTest.info(e);
			throw e;
		}
	}
	
	/**
	 * Method to perform SendKey/Enter to an input field 
	 * @param locator -- The properties of the element from Object Repository
	 * @param testData -- The test data to use coming from Data Sheet
	 * @param elementName -- Name of the element
	 * @throws Exception
	 */
	public void input(String locator, String testData, String elementName) throws Exception{
		try {
			driver.findElement(this.getObject(locator)).sendKeys(testData);
			InitializeDriver2.childTest.pass("PASS :: Successfully performed SendKey on element - " + elementName + " : " + testData);
		} catch (Exception e) {
			e.printStackTrace();
			InitializeDriver2.childTest.fail("FAIL :: Unable to perform SendKey on element -  " + elementName + " : " + testData,
					MediaEntityBuilder.createScreenCaptureFromBase64String(captureScreen()).build());
			InitializeDriver2.childTest.info(e);
			throw e;
		}
	}
	
	public String captureScreen() {
		return ((TakesScreenshot) driver).getScreenshotAs (OutputType.BASE64);
	}
}
