package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class CommonFunc {

	public static void goTo(WebDriver driver, String baseUrl) throws InterruptedException  {
		driver.get(baseUrl);
		driver.manage().window().maximize();
	}
	
	public static void playoutLiveChannel(WebDriver driver) throws InterruptedException {
		login(driver);
		openTVGuide(driver);
		KeyActions.findAndClick(driver,By.cssSelector("div.epg-grid-program-cell.epg-grid-program-cell--live > div.epg-grid-program-cell__container > div.epg-grid-program-cell__title"));
		KeyActions.findAndClick(driver,By.cssSelector("button.button.button--primary.button-with-options"));
	}
	
	public static void channelSwap(WebDriver driver, int count) throws InterruptedException {
		KeyActions.dragAndDrop(driver, By.cssSelector("div.hrz-player-channelstrip-channel-picker"), 0, -80, count);
	}
	
	public static void openTVGuide(WebDriver driver) throws InterruptedException {
	
		KeyActions.findAndClick(driver,By.cssSelector("a[href='/nl/tv.html']"));
		KeyActions.findAndClick(driver,By.cssSelector("a[href='/nl/tv/tv-gids-replay.html']"));
	}
	
	public static void login(WebDriver driver) throws InterruptedException{
		KeyActions.findAndClick(driver,By.cssSelector("a.clickable-block.snippet-button.utility-bar-button"));
		KeyActions.findAndSendKey(driver, By.id("USERNAME"), "lgtest");
		KeyActions.findAndSendKey(driver, By.id("PASSWORD"), "S0ftware");
		KeyActions.findAndClick(driver, By.cssSelector("button.button.button--primary.login-form-button"));
	}
}
