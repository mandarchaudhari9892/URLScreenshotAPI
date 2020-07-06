package com.test.screenshot.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

@RestController
public class IncomingRequest 	
{
	final String URL_VALIDATOR = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})" ;
	
	@GetMapping(value="get_screenshot",produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[]getURLRequest(@RequestParam String url, @RequestParam float scalling)
	{
		  WebDriver driver;                                   
		  System.setProperty("webdriver.chrome.driver", "src/main/resources/driver/chromedriver"); // Set driver of browser (Here  chrome used.)
		  //System.setProperty("webdriver.gecko.driver", "driver/geckodriver"); // Use for Firefox
		  BufferedImage tempImage = null;
		  ByteArrayOutputStream baos = null;
		  if(!url.matches(URL_VALIDATOR))  // Check URL
		  {
			  try
			  {
				  tempImage= ImageIO.read(new File("src/main/resources/images/URLInvalid.png")); // Set static image of "invalid url" to show user.
				
			  } 
			  catch (IOException e)
			  {
				e.printStackTrace();
			  }	

		  }
		  else
		  {
			  driver=new ChromeDriver();  											// User for Chrome
			  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);   
			  driver.manage().window().maximize();                 
			  driver.get(url);
			  
			  //take screenshot of the entire page             
			  Screenshot screenshot=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(scalling), 1000)).takeScreenshot(driver);             
			  
			  driver.quit();  
			  tempImage = screenshot.getImage();	
		  }
		  try 
		  {              			  
			  baos = new ByteArrayOutputStream();
			  ImageIO.write(tempImage, "png", baos);
		  }
		  catch (IOException e)
		  {                         
			 e.printStackTrace();
		  }                            
		  return  baos.toByteArray();
		
	}
}
