package com.test;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Cleartrip {
	private WebDriver driver;
	private WebDriverWait wait;
	private String firstExpectedFlightId, firstActualFlightId, firstExpectedFlightName, firstActualFlightname;
	private String returnExpectedFlightId, returnActualFlightId, returnExpectedFlightName, returnActualFlightname;
	private String expectedStartCityName, expectedEndCityName,actualStartCityName, actualEndCityName
	;
	
	private static Logger log = Logger.getLogger(Cleartrip.class);
	
	@BeforeClass
	private void setup() {
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		
		log.info("Navigate to https://www.cleartrip.com/");
		driver.get("https://www.cleartrip.com/");
		
		wait= new WebDriverWait(driver, 50);
	}
	
	@Test 
	private void verifyTheSameFlightSelected() throws InterruptedException {
		log.info("Click on Flights menu");
		driver.findElement(By.xpath("//*[@id=\"Home\"]/div/aside[1]/nav/ul[1]/li[1]/a[1]")).click();
		
		log.info("Select Round Trip");
		driver.findElement(By.id("RoundTrip")).click();
		
		log.info("Give From and TO City");
		driver.findElement(By.id("FromTag")).sendKeys("Ban");
		threadWait();
		getVisibleElement("//*[@id=\"ui-id-1\"]/li[1]/a").click();		
		driver.findElement(By.id("ToTag")).sendKeys("Chennai");
		threadWait();
		getVisibleElement("//*[@id=\"ui-id-2\"]/li[1]/a").click();
			
		log.info("Select Depart On Date");
		driver.findElement(By.id("DepartDate")).click();
		threadWait();
		getVisibleElement("//*[@id=\"ui-datepicker-div\"]/div[1]/table/tbody/tr[2]/td[6]/a").click();;
		log.info("Select Return date");
		driver.findElement(By.id("ReturnDate")).click();
		threadWait();
		getVisibleElement("//*[@id=\"ui-datepicker-div\"]/div[1]/table/tbody/tr[3]/td[4]/a").click();
		
		log.info("Click on Search Flights");
		driver.findElement(By.id("SearchBtn")).click();	
		
		log.info("Select the lowest price from Indigo and Air India Airlines only");		
		getVisibleElement("//button[text()='Book']").getText();
		
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("window.scrollBy(0,5000)");
		getClickableElement("//p[text()='GoAir']").click();
		//driver.findElement(By.xpath("//p[text()='GoAir']")).click();
		js.executeScript("window.scrollBy(0,5000)");
		getClickableElement("//p[text()='SpiceJet']").click();
		js.executeScript("window.scrollBy(0,5000)");
		getClickableElement("//p[text()='Vistara']").click();
		js.executeScript("window.scrollBy(0,5000)");
		getClickableElement("//p[text()='Air Asia']").click();
	
	    //By default prices are in asc order, below code can be use 
		//for checking the price order correct or not  
	    /*List<WebElement> prices=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"root\"]/div/main/div/div/div[2]/div[2]/div[last()]/div[1]/div[1]/div/div/div[1]/div[2]/div[3]/div[2]/p")));
	
	    Assert.assertTrue(prices!=null && prices.size()>0);
	
	    log.debug("prices size"+ prices.size());
	    for(WebElement price:prices) {
			log.debug(price.getText().substring(1).replace(",", ""));
		}*/
		//Select the lowest price from Indigo and Air India Airlines only
		Actions actions = new Actions(driver);
		
		WebElement we=getVisibleElement("//*[@id=\"root\"]/div/main/div/div/div[2]/div[2]/div[last()]/div[1]/div[1]/div/div[1]/div[1]/div[2]/div[3]/div[2]/p");
		actions.moveToElement(we).click().build().perform();
		log.debug("first flight is selected");
		we=getVisibleElement("//*[@id=\"root\"]/div/main/div/div/div[2]/div[2]/div[last()]/div[2]/div[1]/div/div[1]/div[1]/div[2]/div[3]/div[2]/p");
		
		actions.moveToElement(we).click().build().perform();
		log.debug("second flight is selected");
		
		//click on details to get flight id and name
		getClickableElement("//span[text()='Details']").click();
		log.debug("details link is clicked");             
		firstExpectedFlightId = driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div[2]/div/div[2]/div/div/div[1]/div/div[2]/p[1]")).getText();
		firstExpectedFlightName = driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div[2]/div/div[2]/div/div/div[1]/div/p")).getText();
		log.debug(firstExpectedFlightId+" "+firstExpectedFlightName);
		
		returnExpectedFlightId=driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div[3]/div/div[2]/div/div/div[1]/div/div[2]/p[1]")).getText();
		returnExpectedFlightName=driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div[3]/div/div[2]/div/div/div[1]/div/p")).getText();
		log.debug(returnExpectedFlightId+" "+returnExpectedFlightName);
		
		expectedStartCityName=getVisibleElement("/html/body/div[3]/div/div[1]/div[2]/div/div[2]/div/div/div[2]/div[1]/span[1]").getText();
		
		expectedEndCityName=getVisibleElement("/html/body/div[3]/div/div[1]/div[2]/div/div[2]/div/div/div[2]/div[7]/span[1]").getText();
		log.debug(expectedStartCityName+" "+expectedEndCityName);
				
		//Click on Book
		getClickableElement("/html/body/div[3]/div/div[2]/div/button").click();
				
		
		//Verify the same Flights selected
		String currentHandle=driver.getWindowHandle();
		log.debug(currentHandle);
		Set<String> handles = driver.getWindowHandles();
		for(String s: handles) {
			log.debug(s);
			if(!s.equals(currentHandle)) {
				driver.switchTo().window(s);
				
			}
		}
		
		firstActualFlightId = getVisibleElement("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[2]/div/div/div[4]/div[2]/div/div[1]/p[1]").getText();
		firstActualFlightname = getVisibleElement(("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[2]/div/div/div[4]/div[1]/div[1]/div/div[2]/p")).getText();
		log.debug(firstActualFlightId+" "+firstActualFlightname);
		Assert.assertEquals(firstActualFlightId, firstExpectedFlightId,"flight id is different");
		Assert.assertEquals(firstActualFlightname, firstExpectedFlightName,"flight name is different");
		
		returnActualFlightId = getVisibleElement("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[2]/div/div/div[10]/div[2]/div/div[1]/p[1]").getText();
		returnActualFlightname = getVisibleElement(("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[2]/div/div/div[10]/div[1]/div[1]/div/div[2]/p")).getText();
		log.debug(returnActualFlightId+" "+returnActualFlightname);
		Assert.assertEquals(returnActualFlightId, returnExpectedFlightId,"return flight id is different");
		Assert.assertEquals(returnActualFlightname, returnExpectedFlightName,"return flight name is different");
				
	
	}
	
	@Test (dependsOnMethods = { "verifyTheSameFlightSelected" })
	private void checkTheFlightItineraryInPaymentDetails() {
		//Click on Continue
		getClickableElement("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[2]/div/div/div[25]/div[1]/button").click();
		log.debug("clicked on continue");
		
		getClickableElement("/html/body/div[3]/div/div/div[5]/div[1]/div/div[3]/button").click();
		
		//add contact details
		getVisibleElement("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[5]/div/div[1]/div[1]/div[2]/div[1]/input").sendKeys("2345678921");
		getVisibleElement("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[5]/div/div[1]/div[3]/div/div[1]/input").sendKeys("abc@gmail.com");
		log.debug("email addressed entered");
		getClickableElement("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[5]/div/div[1]/div[7]/button").click();
		log.debug("clicked on continue");
		
		//Add details in Traveler Details section
		
		getVisibleElement("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[8]/div/div/div[2]/div[3]/div[1]/div/input").sendKeys("Swati");
		log.debug("first name entered");
		getVisibleElement("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[8]/div/div/div[2]/div[3]/div[2]/div/input").sendKeys("Verma");
		log.debug("last name entered");
		
		getClickableElement("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[8]/div/div/div[2]/div[3]/div[3]/div/div/button/div").click();
		getVisibleElement("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[8]/div/div/div[2]/div[3]/div[3]/div/div/div/ul/li[2]").click();
		log.debug("gender is selected");
		
		getClickableElement("//*[@id=\"root\"]/div/div[2]/div/div[1]/main/div[8]/div/div/div[6]/div/button").click();
		log.debug("clicked on payment");
		
		actualStartCityName = getVisibleElement("//*[@id=\"root\"]/main/div[1]/section/div/div/div[3]/div/div[2]/div[2]/div[1]/div[1]/div[1]/p[2]").getText().replace("(", "").replace(")", "");
		actualEndCityName = getVisibleElement("//*[@id=\"root\"]/main/div[1]/section/div/div/div[3]/div/div[2]/div[2]/div[1]/div[1]/div[1]/p[5]").getText().replace("(", "").replace(")", "");
		log.debug(actualStartCityName+" "+ actualEndCityName);
		Assert.assertEquals(actualStartCityName, expectedStartCityName,"Start city name is different");
		Assert.assertEquals(actualEndCityName, expectedEndCityName,"End city name is different");
		
	}
	
	
	//picking first element from price list as, list is already sorted. 
	
	
	private WebElement getVisibleElement(String xpath) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		
	}
	
	private WebElement getClickableElement(String xpath) {
		return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		
	}
	
	private void threadWait() throws InterruptedException  {
		Thread.sleep(1000);
	}
	
	@AfterClass
	private void quit() {
		driver.close();
	}
	

}
