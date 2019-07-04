package demo.common;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class ParseExcel {
	public static Method method[];
	public static BasedActions actionKeywords ;
	
	
	public static WebDriver runTCs(String filePathConfig, String fileNameConfig,WebDriver mapdriver,
			ExtentReports extents,ExtentHtmlReporter htmlReporter ) {
		try {
			for (int i = DataCommons.columnFireFox; i <= DataCommons.columnIE; i++) {
				ArrayList<String> runColumn = ExcelUtils.readExcelFileAtColumn(filePathConfig, fileNameConfig,
						DataCommons.sheetTestConfig, i);

				// lay duoc column cua tung browser
				// firfox
				// tc1 yes
				// tc2 no
				// ===>> runff yes no no
				System.out.println(DataCommons.narrow + "\n" + runColumn.toString());
				
				for ( int j =2;j <= runColumn.size(); j++) {
					String sheet = ExcelUtils.getDataAtCell(filePathConfig, fileNameConfig, DataCommons.sheetTestConfig,
							j, DataCommons.columnSheet);
					String rpName = runColumn.get(0).split(" ")[1];
					System.out.println(DataCommons.narrow + "\n" + rpName);
					System.out.println(DataCommons.narrow + "\n" + sheet); // get danh sach sheet testcase

					if (runColumn.get(j-1).toLowerCase().equalsIgnoreCase(DataCommons.yes)) {
						htmlReporter = new ExtentHtmlReporter("./my_report/"+rpName +".html");
						extents = new ExtentReports();
						extents.attachReporter(htmlReporter);
						actionKeywords = new BasedActions(extents,rpName);
						mapdriver = readExcelFile(mapdriver, i, filePathConfig, fileNameConfig, sheet,DataCommons.TOTAL_COLUMN_NUMBER);
					} else
						j++;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("khong doc duoc");
		}

		return mapdriver;
	}

	/*
	 * read excel test case with sheetName
	 */
	public static  WebDriver readExcelFile( WebDriver mapdriver,int browserColumn, String filePath, String fileName,
			String readSheetName,int max ) throws Exception {
		
		try {
			ArrayList<String> column = ExcelUtils.readExcelFileAtColumn(filePath, fileName, readSheetName, 1);
			System.out.println(column);
			
			for (int i = 1; i < column.size(); i++) {
				System.out.println(column.get(i));
				String key = column.get(i);
				String sActions = "";
				if(!key.isEmpty()) {
					sActions = key.substring(0, key.indexOf("("));
					mapdriver = executeActions(mapdriver,browserColumn,sActions,filePath, fileName, readSheetName,i);
				}
					
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			/* write result if fail to excel */
			System.out.println("error : " + e.getMessage());
		}
		return mapdriver;
	}
	

	public static  WebDriver executeActions(WebDriver mapdriver,int browserColumn,String sActions,
			String filePath,String fileName,String readSheetName, int row) {
		
		System.out.println(sActions);
		method = actionKeywords.getClass().getMethods();
		
		ArrayList<String> eachRow = ExcelUtils.readExcelFileAtRow(filePath, fileName, readSheetName, row,
				DataCommons.Testcase_Step_COL_NUM,DataCommons.TOTAL_COLUMN_NUMBER);
		
		System.out.println(eachRow);
		for (int i = 0; i < method.length; i++) {
			if (method[i].getName().equals(sActions)) {
				try {
					Object obj = method[i].invoke(actionKeywords,browserColumn, mapdriver,
							eachRow.get(DataCommons.Testcase_ByValue_COL_NUM -1),
							eachRow.get(DataCommons.Testcase_Input1_COL_NUM -1),
							eachRow.get(DataCommons.Testcase_Input2_COL_NUM -1),
							eachRow.get(DataCommons.Testcase_Input3_COL_NUM -1),
							eachRow.get(DataCommons.Testcase_Expect_COL_NUM -1));
					if(obj!=null) mapdriver = (WebDriver)obj;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
					System.out.println("error with action " +sActions + " " +e.getMessage());
				} 
				break;
			}
		}
		return mapdriver;
		
	}
}
