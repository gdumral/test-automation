package tests;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import net.lightbody.bmp.BrowserMobProxy;
import utils.CommonFunc;
import utils.Utilities;
import utils.Constants;

//@SuppressWarnings("unused") 
public class CaptureLogTest implements Constants {
	
	@Test
	public void openBrowser() throws InterruptedException, IOException {
		//Initiate proxy server
		BrowserMobProxy bmp = Utilities.startProxy();
		WebDriver driver = Utilities.createWebDriver(bmp);
							
		CommonFunc.goTo(driver, BASE_URL);
		CommonFunc.playoutLiveChannel(driver);
		CommonFunc.channelSwap(driver, 2);
		
		Utilities.captureNetworkTrace(bmp);
		driver.quit();	
	}	

}
