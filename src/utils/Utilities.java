package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.littleshoot.proxy.MitmManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.seleniumhq.jetty9.http.HttpMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.corba.se.spi.activation.Server;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.Assert;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarPostData;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;
import net.lightbody.bmp.mitm.KeyStoreFileCertificateSource;
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager;
import net.lightbody.bmp.proxy.CaptureType;


public class Utilities {
	
//	System.setProperty("webdriver.chrome.driver", "/TestAutomation/chromedriver.exe");

	//WebDriver is interface, ChromeDriver is class which implements the interface
	private WebDriver driver;
	
	//Create a new proxy server. Get the proxy server running. Return the instance to calling method
	public static BrowserMobProxy startProxy() {
		BrowserMobProxy bmp = new BrowserMobProxyServer();
		bmp.setTrustAllServers(true); //need for invalid certificate
        bmp.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT);
		bmp.newHar("ziggo.nl");			
		bmp.start();
		return bmp;
	}

	public static Proxy getSeleniumProxy(BrowserMobProxy bmp) throws UnknownHostException {
		//Return a Selenium proxy object generated from BMP server. which can later be passed as browser capability	
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(bmp);
		String hostIp = Inet4Address.getLocalHost().getHostAddress();
		System.out.println(hostIp+": " + bmp.getPort());
		return seleniumProxy;
	}
	
	public static WebDriver createWebDriver(BrowserMobProxy bmp) throws UnknownHostException{
		//Return a Selenium proxy object generated from BMP server. which can later be passed as browser capability	
		Proxy seleniumProxy = getSeleniumProxy(bmp);
		//pass seleniumproxy instance as capability
		ChromeOptions options = new ChromeOptions();
		
		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.BROWSER, Level.ALL);
		logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
		logPrefs.enable(LogType.DRIVER, Level.ALL);
		options.setCapability(CapabilityType.LOGGING_PREFS,logPrefs);
		options.setCapability(CapabilityType.PROXY, seleniumProxy);
		System.setProperty("webdriver.chrome.driver", "C:\\TestAutomation\\chromedriver.exe");	
		WebDriver driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	
		return driver;
	}
	
	public static void captureNetworkTrace(BrowserMobProxy bmp) throws IOException {


		writeToHar(bmp);
	//	printLog(bmp);
		bmp.stop();
	}
	
	public static void writeToHar(BrowserMobProxy bmp ) throws IOException {
		//get the har data
		Har har = bmp.getHar();	

		List<HarEntry> entries = har.getLog().getEntries();
		
		for(HarEntry entry :entries){
			HarRequest request = entry.getRequest();
			HarResponse response= entry.getResponse();
//			String keyword = "\"category\":";
			String keyword = "errorCode:";
			String errorCode= new String();
			int i;

			//if (request.getUrl().contains("https://cws.conviva.com") && request.getMethod().equals("POST"))  {			
				if (request.getPostData() != null ) {
					if (request.getPostData().getText().contains(keyword)) {
						System.out.format("%s %s %s %n", request.getMethod(), request.getUrl(), response.getStatus());
						//System.out.println(request.getPostData().getText().substring(keyword.lastIndexOf(":")));
						i = request.getPostData().getText().indexOf(keyword);
//						errorCode=request.getPostData().getText().substring(i+12, i+16);
						errorCode=request.getPostData().getText().substring(i+11, i+16);
						System.out.format("Conviva errorCode: %s %n", errorCode);
					}
				}
			}
//		}		
	    har.writeTo(new File("C:\\\\TestAutomation\\\\NetworkTraffic\\\\ziggo.har"));
	    

	}
	  //    submitPerformanceResult("Test.testGoogleSearch", entries);

	


}



//int statusCode = entry.getResponse().getStatus();
//try{
//	Assert.assertEquals(200, statusCode);
//}catch (AssertionError e) {
//	System.out.format("Error StatusCode: %d %s %n", statusCode, entry.getRequest().getUrl());
//}

	
	



