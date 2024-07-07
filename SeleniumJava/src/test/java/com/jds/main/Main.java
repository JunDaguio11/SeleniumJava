package com.jds.main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.Test;

import com.jds.config.InitializeDriver;
import com.jds.keywords.Keywords;

public class Main extends InitializeDriver{
	public Keywords keyword;
	
	public Main() {
		keyword = new Keywords(); 
	}
	 
	
	public static String root = System.getProperty("user.dir"); 
	
	@Test
	public void startDriver() throws Exception {
		LinkedList<String> moduleToExecute = readMainDriverExcel();
		
		for (String module:moduleToExecute){
			this.readModuleExcelAndExecute(module);
			this.updateOverallStatus(module);
		}
	}
	
	public void updateOverallStatus(String module) throws IOException {
		String moduleExcelPath = "Config\\" + module + ".xlsx";
		FileInputStream fileInputStream = new FileInputStream(moduleExcelPath);

		String mainExcelPath = "Config\\MainDriver.xlsx";

		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet worksheet = workbook.getSheet("TestCases");
		
		XSSFCell passCell;
		XSSFCell failCell;
		XSSFCell totalCell;
		
		int totalRow = worksheet.getLastRowNum();
		int totalPass = 0;
		int totalFail = 0;

		for (int curRow = 1; curRow <= totalRow; curRow++) {
			String status = worksheet.getRow(curRow).getCell(3).getStringCellValue();
			
			if (status.compareTo("Pass") == 0) {
				totalPass += 1;
			} else {
				totalFail += 1;
			}

		}
		workbook.close();
		fileInputStream.close();

		fileInputStream = new FileInputStream(mainExcelPath);
		workbook = new XSSFWorkbook(fileInputStream);
		worksheet = workbook.getSheet("Main");
		totalRow = worksheet.getLastRowNum();

		for (int curRow = 1; curRow <= totalRow; curRow++) {
			String curModule = worksheet.getRow(curRow).getCell(1).getStringCellValue();
			if(curModule.compareTo(module) == 0) {
				passCell = worksheet.getRow(curRow).getCell(3);
				if(passCell==null) {
					passCell = worksheet.getRow(curRow).createCell(3);
				}
				passCell.setCellValue(totalPass);
				
				failCell = worksheet.getRow(curRow).getCell(4);
				if(failCell==null) {
					failCell = worksheet.getRow(curRow).createCell(4);
				}
				failCell.setCellValue(totalFail);
				
				totalCell = worksheet.getRow(curRow).getCell(5);
				if(totalCell==null) {
					totalCell = worksheet.getRow(curRow).createCell(5);
				}
				totalCell.setCellValue(totalPass + totalFail);
			}
		}

		fileInputStream.close();
		FileOutputStream fileOutputStream = new FileOutputStream(mainExcelPath);	
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		workbook.close();
	}
	
	public LinkedList<String> readMainDriverExcel() throws IOException {
		String excelPath = "Config\\MainDriver.xlsx";
		FileInputStream fileInputStream = new FileInputStream(excelPath);
		
		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet worksheet = workbook.getSheet("Main");
		int totalRow =  worksheet.getLastRowNum();

		LinkedList<String> ModuleExecutionList = new LinkedList<>();
		
		for (int curRow = 1; curRow <= totalRow; curRow++) {
			String module = worksheet.getRow(curRow).getCell(1).getStringCellValue();
			String executeTag = worksheet.getRow(curRow).getCell(2).getStringCellValue();
			if (executeTag.compareTo("Yes") == 0) {
				ModuleExecutionList.add(module);
			}
		}
		workbook.close();
		fileInputStream.close();
		return ModuleExecutionList;
	}
	
	
	public void readModuleExcelAndExecute(String module) throws Exception {
		//String module = "Register";
		String excelPath = "Config\\" + module + ".xlsx";
		FileInputStream fileInputStream = new FileInputStream(excelPath);

		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet worksheet = workbook.getSheet("TestCases");
		int totalRow = worksheet.getLastRowNum();
		// loop through testcases worksheet to get the testcases marked for execution
		for (int curRow = 1; curRow <= totalRow; curRow++) {
			String testcaseID = worksheet.getRow(curRow).getCell(1).getStringCellValue();
			String executeTag = worksheet.getRow(curRow).getCell(2).getStringCellValue();
			String testCaseStatus = "Pass";

			if (executeTag.compareTo("Yes") == 0) {
				XSSFSheet testCaseWorksheet = workbook.getSheet(testcaseID);
				int totalSteps = testCaseWorksheet.getLastRowNum();
				int totalCol = testCaseWorksheet.getRow(0).getLastCellNum();
				
				// loop through each row in the specific testase worksheet
				for (int stepRow = 1; stepRow <= totalSteps; stepRow++) {
					LinkedList<String> stepParams = new LinkedList<>();
					
					XSSFCell statusCell = testCaseWorksheet.getRow(stepRow).getCell(6);
					XSSFCell actualCell = testCaseWorksheet.getRow(stepRow).getCell(5);
					
					if(statusCell==null) {
						statusCell = testCaseWorksheet.getRow(stepRow).createCell(6);
					}
					if(actualCell==null) {
						actualCell = testCaseWorksheet.getRow(stepRow).createCell(5);
					}
					
					for (int curCol = 0; curCol < totalCol; curCol++) {
						XSSFCell curCell = testCaseWorksheet.getRow(stepRow).getCell(curCol);
						String cellValue;
						
						if (curCell==null) {
							cellValue = null;
						}else {
							cellValue = curCell.getStringCellValue();
						}
						stepParams.add(cellValue);
						
						//initialize step status = ""
						statusCell.setCellValue("");
					}
					HashMap<String, String> stepStatus = executeStep(module,stepParams);
					//testCaseWorksheet.getRow(stepRow).getCell(6).setCellValue(stepStatus);
						
					statusCell.setCellValue(stepStatus.get("Status"));
					actualCell.setCellValue(stepStatus.get("Actual"));
					if(stepStatus.get("Status")!="Pass") {
						testCaseStatus = "Fail";
					}
				}
				XSSFCell tcStatusCell = worksheet.getRow(curRow).getCell(3);
				if(tcStatusCell==null) {
					tcStatusCell = worksheet.getRow(curRow).createCell(3);
				}
				tcStatusCell.setCellValue(testCaseStatus);
			}
		}
		
		fileInputStream.close();
		FileOutputStream fileOutputStream = new FileOutputStream(excelPath);	
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		workbook.close();
	}
	
	public HashMap<String, String> executeStep(String module, LinkedList<String> params) throws Exception {
		Properties prop = new Properties();
		HashMap<String, String> resultSet = new HashMap<>();
		
		String operation = params.get(0);
		String objectName = params.get(1);
		String testData = params.get(2);
		String assertion = params.get(3);
		String expectedValue = params.get(4);
		
		prop = getProperties(module);
		
		switch(operation) {
		case "OpenApplication":
			try {
				keyword.openApplication(prop.getProperty("url"));
				resultSet.put("Status","Pass");
				resultSet.put("Actual","");
			} catch (Exception e) {
				e.printStackTrace();
				resultSet.put("Status","Fail");
				resultSet.put("Actual",e.toString().split(":")[0]);
			}
			break;
		case "Click":
			try {
				keyword.click(prop.getProperty(objectName), objectName);
				resultSet.put("Status","Pass");
				resultSet.put("Actual","");
			} catch (Exception e) {
				e.printStackTrace();
				resultSet.put("Status","Fail");
				resultSet.put("Actual",e.toString().split(":")[0]);
			}
			break;
		case "Input":
			try {
				keyword.input(prop.getProperty(objectName), testData, objectName);
				resultSet.put("Status","Pass");
				resultSet.put("Actual","");
			} catch (Exception e) {
				e.printStackTrace();
				resultSet.put("Status","Fail");
				resultSet.put("Actual",e.toString().split(":")[0]);
			}
			break;
		case "Verify":
			try {
				String actual = keyword.verify(prop.getProperty(objectName), objectName,assertion,expectedValue);
				resultSet.put("Status","Pass");
				resultSet.put("Actual",actual);
			} catch (Exception e) {
				e.printStackTrace();
				resultSet.put("Status","Fail");
				resultSet.put("Actual",e.toString().split(":")[0]);
			}
			break;
		case "SelectRadio":
			try {
				keyword.selectRadio(prop.getProperty(objectName), testData, objectName);
				resultSet.put("Status","Pass");
				resultSet.put("Actual","");
			} catch (Exception e) {
				e.printStackTrace();
				resultSet.put("Status","Fail");
				resultSet.put("Actual",e.toString().split(":")[0]);
			}
			break;
		case "CloseApplication":
			try {
				keyword.closeApplication();
				resultSet.put("Status","Pass");
				resultSet.put("Actual","");
			} catch (Exception e) {
				e.printStackTrace();
				resultSet.put("Status","Fail");
				resultSet.put("Actual",e.toString().split(":")[0]);
			}
			break;
		}
		
		return resultSet;
	}
	
	
	public Properties getProperties(String module) {
		Properties prop = new Properties();
		try {
			// load common properties file
			InputStream input = new FileInputStream("Config/Configuration.properties");
			prop.load(input);
			input = new FileInputStream("Object Repository/Header.properties");
			prop.load(input);
			input = new FileInputStream("Object Repository/LeftNavMenu.properties");
			prop.load(input);
			input = new FileInputStream("Object Repository/TopNavifationMenu.properties");
			prop.load(input);
			
			//load module specific file
			input = new FileInputStream("Object Repository/" + module + "Page.properties");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}		
		return prop;
	}
	
	
	public void testElement() {
		WebDriver testDriver;
		EdgeOptions edgeOptions = new EdgeOptions();
		testDriver = new EdgeDriver(edgeOptions);
		testDriver.get("https://demowebshop.tricentis.com/");
		testDriver.findElement(By.className("ico-login")).click();
		testDriver.findElement(By.xpath("//input[@value='Log in']")).click();
	}
}