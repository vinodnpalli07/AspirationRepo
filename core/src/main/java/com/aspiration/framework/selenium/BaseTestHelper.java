package  com.aspiration.framework.selenium;

 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.aspiration.framework.utils.ConfigProperties;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;

//import com.automation.framework.utils.ConfigProperties;

/**
 * initialization of the browser is handled in this class<br><br>
 * 
 * <b>prerequisites</b><br>
 * 1. chrome drive should be there in the project's "driver/chromedriver.exe" location<br><br>
 * 
 * <b>note</b>: tests file must extend this class its a mandatory<br><br>
 *  
 * @author Vinod Kumar
 *
 */
public abstract class BaseTestHelper {

	Logger log = LogManager.getLogger(BaseTestHelper.class.getName());
	private WebDriver driver;

	public WebDriver getWebDriver() {
		return driver;
	} 
	 
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public WebDriver openBrowser() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		log.info("========== launching browser =======");		
		
	 	switch (ConfigProperties.getBrowserType()) {
		case Chrome:
			DriverManagerType chrome = DriverManagerType.CHROME;
			WebDriverManager.getInstance(chrome).setup();
			Class<?> chromeClass =  Class.forName(chrome.browserClass());
			driver = (WebDriver) chromeClass.newInstance(); 			 
			break;
			
		case Firefox:			 
			System.setProperty("webdriver.gecko.driver", "driver/geckodriver.exe");
			driver = new FirefoxDriver();
			break;
			
		case Safari:
			SafariOptions options = new SafariOptions(); 
			driver = new SafariDriver(options);
			break;
			
		case Edge:
			break;
		
		default:
			break;
		}
		driver.manage().window().maximize();
		return driver;
	}
}