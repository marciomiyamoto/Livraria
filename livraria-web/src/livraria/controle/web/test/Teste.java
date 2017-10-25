package livraria.controle.web.test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class Teste {
	
	@Test
	public void teste(){
		
		WebDriver driver;
		System.setProperty("webdriver.chrome.driver", "C:\\Livraria\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("http://www.google.com.br/");
		
		WebElement campo = driver.findElement(By.name("q"));
		campo.sendKeys("overwatch");
		
		campo.submit();
		
		driver.close();
	}

}
