package com.orangeHrm.runners;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.orangeHrm.utils.ExtentManager;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
	plugin = { "json:target/cucumber-report.json", "html:target/cucumber-report", "rerun:rerun/failed_scenarios.txt" }, 
	features = {"src/test/resources" }, 
	glue = { "com.orangeHrm.stepDefinitions" }, 
	tags = "@TC_ADM_006",
	dryRun = false,
	monochrome = true
	)

@Test
public class OrangeHrmMasterRunner extends AbstractTestNGCucumberTests {

	    public static String Environment = "test";

	@BeforeClass
	public void reportSetup() throws IOException {

		String dirPath = System.getProperty("user.dir");
		String testFilePath = dirPath + "/config/" + "test.properties";
		String configFilePath = dirPath + "/config/" + "configuration.properties";

		File sourceFile = null;
		FileInputStream fileInputStream = null;
		BufferedReader bufferReader = null;
		FileWriter fileWriter = null;
		BufferedWriter bufferWriter = null;

		if (Environment.equalsIgnoreCase("Test")) {
			sourceFile = new File(testFilePath);
			fileInputStream = new FileInputStream(sourceFile);
			bufferReader = new BufferedReader(new InputStreamReader(fileInputStream));
		}

		fileWriter = new FileWriter(configFilePath);
		bufferWriter = new BufferedWriter(fileWriter);
		String aLine = null;
		
		if (bufferReader != null) {
			while ((aLine = bufferReader.readLine()) != null) {
				bufferWriter.write(aLine);
				bufferWriter.newLine();
			}
		}
		
		bufferWriter.flush();
		if (bufferReader != null) {
			bufferReader.close();
		}
		bufferWriter.close();
		System.out.println("Config file generated for :- " + Environment + " environment");
		
		// Set system information for ExtentReport
		ExtentManager.setSystemInfo();

	}

	@AfterClass
	public void teardown() {
		ExtentManager.getExtent().setSystemInfo("user", System.getProperty("user.name"));
		ExtentManager.getExtent().setSystemInfo("Environment", Environment);
		if (System.getProperty("os.name").contains("Windows")) {
			ExtentManager.getExtent().setSystemInfo("os", "Windows");
		} else {
			ExtentManager.getExtent().setSystemInfo("os", "Linux");
		}
		ExtentManager.flush();
	}

}
