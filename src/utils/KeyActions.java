package utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import net.bytebuddy.implementation.bind.annotation.Default;

import utils.Constants;

@SuppressWarnings("unused") 
public class KeyActions {
	
	public static void findAndClick(WebDriver driver, By elementLocator) throws InterruptedException  {
		driver.findElement(elementLocator).click();
		Thread.sleep(2000);
	}
	
	public static void find(WebDriver driver, By elementLocator) throws InterruptedException  {
		driver.findElement(elementLocator);
	}
	
	public static void findAndSendKey(WebDriver driver, By elementLocator, String key) throws InterruptedException {
		driver.findElement(elementLocator).sendKeys(key);
		Thread.sleep(2000);
		
	}

	public static void dragAndDrop(WebDriver driver, By elementLocator, int xOffset, int yOffset, int count )   {
		Actions action = new Actions(driver);		
		WebElement el = driver.findElement(elementLocator);
		for (int i = 0; i < count; i++) {		
			action.dragAndDropBy(el, xOffset, yOffset).pause(Duration.ofSeconds(1)).click().pause(Duration.ofSeconds(2)).build().perform();
//			Thread.sleep(5000);
//			findAndClick(driver,By.cssSelector("div.hrz-player-controls-overlay"));
//			findAndClick(driver,By.cssSelector("div.hrz-player-controls-overlay"));

		}
	}
}
