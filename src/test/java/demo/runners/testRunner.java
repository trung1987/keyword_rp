package demo.runners;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import demo.common.ParseExcel;


public class testRunner {
	String filepath = System.getProperty("user.dir")+ File.separator + "test_input" + File.separator ;
	public static WebDriver wdriver = null;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extents;
	
	@BeforeSuite
	public static void beforesuie() {
		File f = new File(System.getProperty("user.dir")+"/my_report");
		if (!f.exists()) {
			f.mkdirs();
		}

	}
	
	@Test
	public void runTCs() {
		String filename =  "TestCase.xls";
		wdriver=ParseExcel.runTCs(filepath,filename,wdriver,extents,htmlReporter);
	}
}
