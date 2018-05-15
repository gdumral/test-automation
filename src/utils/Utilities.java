package utils;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import org.junit.Assert;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;
import net.lightbody.bmp.proxy.CaptureType;


public class Utilities implements Constants {
	
	//WebDriver is interface, ChromeDriver is class which implements the interface
//	private WebDriver driver;
	
	//Create a new proxy server. Get the proxy server running. Return the instance to calling method
	public static BrowserMobProxy startProxy() {
		BrowserMobProxy bmp = new BrowserMobProxyServer();
		bmp.setTrustAllServers(true); //need for invalid certificate
        bmp.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT);
		bmp.newHar(HAR_FILE_LABEL);
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
		System.setProperty("webdriver.chrome.driver", DRIVER_PATH);	
		WebDriver driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	
		return driver;
	}
	
	public static void captureNetworkTrace(BrowserMobProxy bmp) throws IOException {


		writeToHar(bmp);
		bmp.stop();
	}
	
	public static void writeToHar(BrowserMobProxy bmp ) throws IOException {
		//get the har data
		Har har = bmp.getHar();	

		List<HarEntry> entries = har.getLog().getEntries();
		
		for(HarEntry entry :entries) {
			HarRequest request = entry.getRequest();
			HarResponse response= entry.getResponse();
			int statusCode = response.getStatus();
			String keyword = "errorCode";
			String errorCode= new String();
			int i;

//			//if (request.getUrl().contains("https://cws.conviva.com") && request.getMethod().equals("POST"))  {			
//			if (request.getUrl().contains("https://cws.conviva.com") && request.getPostData() != null) {
//				System.out.format("%s %s %n %s %n", request.getMethod(), request.getUrl(), request.getPostData().getText());
//			}
			
			try{
				Assert.assertEquals(200, statusCode);
			}catch (AssertionError e) {
				if (statusCode == 400){
					System.out.println(request.getUrl() + " - " + HTTP_400_ERROR);
				} else if (statusCode == 401) {
					System.out.println(request.getUrl() + " - " +  HTTP_401_ERROR);
				} else if (statusCode == 403) {
					System.out.println(request.getUrl() + " - " +  HTTP_403_ERROR);
				} else if (statusCode == 404) {
					System.out.println(request.getUrl() + " - " +  HTTP_404_ERROR);
				} else if (statusCode == 500) {
					System.out.println(request.getUrl() + " - " +  HTTP_500_ERROR);
				} else if (statusCode == 502) {
					System.out.println(request.getUrl() + " - " +  HTTP_502_ERROR);
				} else if (statusCode == 503) {
					System.out.println(request.getUrl() + " - " +  HTTP_503_ERROR);
				} else if (statusCode == 504) {
					System.out.println(request.getUrl() + " - " +  HTTP_504_ERROR);
				}			
			}
						
			if (request.getUrl().contains(CONVIVA_REQUEST) && request.getPostData() != null) {
				if (request.getPostData().getText().contains(keyword)) {
					//System.out.format("%s %s %s %n", request.getMethod(), request.getUrl(), statusCode);
					//System.out.println(request.getPostData().getText().substring(keyword.lastIndexOf(":")));
					i = request.getPostData().getText().indexOf(keyword);
					errorCode=request.getPostData().getText().substring(i+11, i+18);
					System.out.format("Conviva errorCode: %s %n %n", errorCode);
				}
			}
		}
	    har.writeTo(new File(HAR_FILE_PATH));   

	}

}