package com.orangeHrm.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static synchronized ExtentReports getExtent() {
        if (extent == null) {
            // Create report path with timestamp
            String timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
            String reportPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "TestResult" + System.getProperty("file.separator") + "ExtentReport_" + timestamp + ".html";
            
            // Ensure directory exists
            new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "TestResult").mkdirs();
            
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("OrangeHRM Test Report");
            spark.config().setDocumentTitle("OrangeHRM Test Execution Report");
            
            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        return extent;
    }

    public static void setSystemInfo() {
        ExtentReports report = getExtent();
        report.setSystemInfo("User", System.getProperty("user.name"));
        report.setSystemInfo("Operating System", System.getProperty("os.name"));
        report.setSystemInfo("OS Version", System.getProperty("os.version"));
        report.setSystemInfo("Java Version", System.getProperty("java.version"));
        report.setSystemInfo("Environment", "Local OrangeHRM");
        report.setSystemInfo("Browser", "Chrome");
    }

    public static ExtentTest createTest(String name) {
        ExtentTest t = getExtent().createTest(name);
        test.set(t);
        return t;
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static String addScreenCapture(String path) {
        // keep signature for compatibility; return the path
        if (path == null || path.isEmpty()) return null;
        return path;
    }
    
    public static void removeTest() {
        test.remove();
    }
}
