package com.jds.keywords;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.jds.config.InitializeDriver;

public class Keywords{
	
	WebDriver driver;
	//WebDriver driver = InitializeDriver.initializeDriver();
	
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
				String newProperty = property.replace("xpath=",""); 
				return By.xpath(newProperty);
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
			driver = InitializeDriver.initializeDriver();
			driver.get("https://demowebshop.tricentis.com/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to close the current driver
	 */
	public void closeApplication() {
		try {
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
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
		} catch (Exception e) {
			e.printStackTrace();
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
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Method to verify an specified element and what is expected
	 * @param locator -- The properties of the element from Object Repository
	 * @param elementName -- Name of the element
	 * @param assertType -- Type of assertion(assertEquals, assertNotNul, assertEnabled, assertSelected, assertDisplayed)
	 * @param expetedValue - The value of the element to be expected for assertEquals
	 */
	@SuppressWarnings("finally")
	public String verify(String locator, String elementName, String assertType, String expetedValue) throws Exception {
		String actualValue = "";
		try {
			WebElement currElement = driver.findElement(this.getObject(locator));
			switch(assertType){
			case "assertEquals":
				Assert.assertEquals(expetedValue,currElement.getText());
				actualValue = currElement.getText();
				break;
			case "assertNotNull":
				Assert.assertNotNull(currElement);
				break;
			case "assertNull":
				Assert.assertNull(currElement);
				break;
			case "assertEnabled":
				Assert.assertTrue(currElement.isEnabled());
				break;
			case "assertSelected":
				Assert.assertTrue(currElement.isSelected());
				break;
			case "assertDisplayed":
				Assert.assertTrue(currElement.isDisplayed());
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			return actualValue;
		}	
		//return actualValue;
	}
	
	/**
	 * Method to select a the radio button of the identified Value
	 * @param locator -- The properties of the element from Object Repository 
	 * @param testData -- The test data to use coming from Data Sheet
	 * @param elementName -- Name of the element
	 * @throws Exception
	 */
	public void selectRadio(String locator, String testData, String elementName) throws Exception {
		String newLocator = locator + "-" + testData.toLowerCase();
		try {
			driver.findElement(this.getObject(newLocator)).click();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
