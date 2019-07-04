package demo.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BasedActions {

	public static ExtentReports mapextents;
	public static ExtentTest rp;
	public static String name;
	public BasedActions(ExtentReports extents, String rpname) {
		// htmlReporter = new ExtentHtmlReporter("./my_report/AutomationReport.html");
		mapextents = extents;
		name = rpname;
		// extents.attachReporter(htmlReporter);
	}

	public void StartTestCase(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		if (rp == null)
			rp = mapextents.createTest(input1);
	}

	public WebDriver OpenBrowser(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {

		if (browserColumn == DataCommons.columnChrome) {
			System.out.println("Running Chrome");
			WebDriverManager.chromedriver().setup();
			wdriver = new ChromeDriver();

		} else if (browserColumn == DataCommons.columnFireFox) {
			System.out.println("Running FF");
			WebDriverManager.firefoxdriver().setup();
			wdriver = new FirefoxDriver();
		} else {
			System.out.println("Running IE");
			WebDriverManager.iedriver().setup();
			wdriver = new InternetExplorerDriver();
		}
		rp.log(Status.INFO, "OpenBrowser");
		wdriver.get(input1);
		wdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		try {
			rp.log(Status.INFO,"details", MediaEntityBuilder.createScreenCaptureFromPath(captureEachStep(wdriver)).build());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			System.out.println("cannot capture " + e.getStackTrace());
		}
		
		return wdriver;
	}

	public void EndTestCase(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		// wdriver.quit();
		try {
			rp.addScreenCaptureFromPath(captureEachStep(wdriver));
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			System.out.println("cannot capture " + e.getStackTrace());
		}
		mapextents.flush();
		rp = null;
	}

	public void openURL(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2, String input3,
			String expect) {
		rp.log(Status.INFO, "openURL");
		wdriver.get(input1);
		
	}

	public void EnterText(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		rp.log(Status.INFO, "EnterText to " + xpath + " with value " + input1);
		
		WebElement element = wdriver.findElement(By.xpath(xpath));
		element.clear();
		element.sendKeys(input1);
		try {
			rp.log(Status.INFO,"details", MediaEntityBuilder.createScreenCaptureFromPath(captureEachStep(wdriver)).build());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			System.out.println("cannot capture " + e.getStackTrace());
		}
	}

	public void SendEnterKey(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		rp.log(Status.INFO, "SendEnterKey to " + xpath);
		WebElement element = wdriver.findElement(By.xpath(xpath));
		element.sendKeys(Keys.ENTER);
		try {
			rp.log(Status.INFO,"details", MediaEntityBuilder.createScreenCaptureFromPath(captureEachStep(wdriver)).build());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			System.out.println("cannot capture " + e.getStackTrace());
		}
	}

	public void clickBtn(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		rp.log(Status.INFO, "clickBtn to " + xpath);
		WebElement element = wdriver.findElement(By.xpath(xpath));
		element.click();
	}

	public void acceptAlert(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		Alert myAlert = wdriver.switchTo().alert();
		myAlert.accept();
	}

	public String getValue(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		WebElement element = wdriver.findElement(By.xpath(xpath));
		return element.getText();
	}

	public String getAlertMsg(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		Alert myAlert = wdriver.switchTo().alert();
		return myAlert.getText();
	}

	public void assertValue(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		WebElement element = wdriver.findElement(By.xpath(xpath));
		Assert.assertEquals(element.getText(), expect);
	}

	// Capture each step

	public static String captureEachStep(WebDriver wdriver) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMDDhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) wdriver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String dest = System.getProperty("user.dir") + "/my_report/" +name + dateName + ".png";
		//String dest = "./" + dateName + ".png";
		File destination = new File(dest);
		FileUtils.copyFile(source, destination);

		return name + dateName + ".png";
	}

	public String getTitle(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		return wdriver.getTitle();
	}

	public String getCurrentURL(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		return wdriver.getCurrentUrl();
	}

	public void back(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2, String input3,
			String expect) {
		wdriver.navigate().back();
	}

	public void forward(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2, String input3,
			String expect) {
		wdriver.navigate().forward();
	}

	public void refresh(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2, String input3,
			String expect) {
		wdriver.navigate().refresh();
	}

	/* Alert */
	public void cancelAlert(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		Alert myAlert = wdriver.switchTo().alert();
		myAlert.dismiss();
	}

	public void sendkeyToAlert(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		Alert myAlert = wdriver.switchTo().alert();
		myAlert.sendKeys(input1);
	}

	/* Web Element */
	public void clickToEleByJs(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		JavascriptExecutor executor = (JavascriptExecutor) wdriver;
		WebElement element = wdriver.findElement(By.xpath(xpath));
		executor.executeScript("arguments[0].click();", element);
	}

	public void clickToEleByAction(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		WebElement element = wdriver.findElement(By.xpath(xpath));
		Actions action = new Actions(wdriver);
		action.click(element).build().perform();
	}

	public void selectItemInHtmlDropDownByIndex(int browserColumn, WebDriver wdriver, String xpath, String input1,
			String input2, String input3, String expect) {
		Select dropDown = new Select(wdriver.findElement(By.xpath(xpath)));
		dropDown.selectByIndex(Integer.parseInt(input1));
	}

	public void selectItemInHtmlDropDownByValue(int browserColumn, WebDriver wdriver, String xpath, String input1,
			String input2, String input3, String expect) {
		Select dropDown = new Select(wdriver.findElement(By.xpath(xpath)));
		dropDown.selectByValue(input1);
	}

	public void selectItemInHtmlDropDownByInvisibleText(int browserColumn, WebDriver wdriver, String xpath,
			String input1, String input2, String input3, String expect) {
		Select dropDown = new Select(wdriver.findElement(By.xpath(xpath)));
		dropDown.selectByVisibleText(input1);
	}

	public String getSelectedItemInHtmlDropDown(int browserColumn, WebDriver wdriver, String xpath, String input1,
			String input2, String input3, String expect) {
		Select dropDown = new Select(wdriver.findElement(By.xpath(xpath)));
		WebElement select = dropDown.getFirstSelectedOption();
		return select.getText();
	}

	public void checkTheCheckBox(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {

		WebElement elementToClick = wdriver.findElement(By.xpath(xpath));
		elementToClick.click();
	}

	public void UncheckTheCheckBox(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		// If type = 'checkbox'
		WebElement checkBox = wdriver.findElement(By.xpath(xpath));
		boolean checkStatus;
		checkStatus = checkBox.isSelected();
		if (checkStatus == true) {
			checkBox.click();
		} else {
			System.out.println("it has been uncheck already");
		}
	}

	public boolean isControlDisplayed(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		WebElement checkItem = wdriver.findElement(By.xpath(xpath));
		return checkItem.isDisplayed();
	}

	public boolean isControlSelected(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		WebElement checkItem = wdriver.findElement(By.xpath(xpath));
		return checkItem.isSelected();
	}

	public boolean isControlEnabled(int browserColumn, WebDriver wdriver, String xpath, String input1, String input2,
			String input3, String expect) {
		WebElement checkItem = wdriver.findElement(By.xpath(xpath));
		return checkItem.isEnabled();
	}

}
