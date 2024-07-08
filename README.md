**Note: This was created by me for my own learning**

This is an automation framework for web application using Selenium WebDriver with Java as it's language.

It utilizes a single driver/test function which utilizes the config/excel to determine which modules and test cases are marked to execute. 
With this approach, it makes it easier to manage what to execute(be it all modules, some modules, or just few test cases on specific modules)
As well as creating new testcases will be much more easier as only need to use the keywords to write the steps in the test case template without worrying about the code.

1. Execution starts with the test class Main.java
2. Read the MainDriver.xlsc to read which modules are marked to be executed. User has the flexibility to mark which module they want to run.
3. Based from the list of modules, it will read all the individual excel files that represents each module.
4. Each module .xlsx file have the list of the test cases it covers. User have the ability to mark which test cases to execute.
5. All the element properties are in a properties file for easy maintenance.
