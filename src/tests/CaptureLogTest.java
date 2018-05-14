package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.CapabilityType;

import com.google.common.collect.ImmutableSet;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import utils.CommonFunc;
import utils.KeyActions;
import utils.Utilities;
import utils.Constants;

@SuppressWarnings("unused") 
public class CaptureLogTest implements Constants {
	
	@Test
	public void openBrowser() throws InterruptedException, IOException {
		//Initiate proxy server
		BrowserMobProxy bmp = Utilities.startProxy();
		WebDriver driver = Utilities.createWebDriver(bmp);
							
		CommonFunc.goTo(driver, BASE_URL);
		CommonFunc.playoutLiveChannel(driver);
		CommonFunc.channelSwap(driver, 100);
		
		Utilities.captureNetworkTrace(bmp);
		driver.quit();	
	}	

}
