package aspireTests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.javatuples.Pair;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aspiration.framework.selenium.BaseTestHelper;
import com.aspiration.framework.selenium.SeleniumHelper;
import com.aspiration.framework.utils.ConfigProperties;

import web.aspiration.pages.HomePage;
import web.aspiration.pages.ProductsPage;

public class NewTest extends BaseTestHelper {

	WebDriver driver;
	HomePage homePage;
	ProductsPage productsPage;
	
	@BeforeMethod
	public void beforeMethod() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println("the_user_is_on_aspiration_home_page");
		driver = openBrowser(); 
		SeleniumHelper.loadPage(driver, ConfigProperties.getHostUrl("aspiration"));
	}

	@AfterMethod
	public void afterMethod() {
		driver.quit();
	}
	
	@Test
	public void f() throws InterruptedException {

		System.out.println("the_user_clicks_on_the_spend_and_save");
		homePage = new HomePage(driver);
		//homePage.clickOnLink(Links.SpendAndSave);
		homePage.clickOnClickAndSave(driver);
		Thread.sleep(5000);
		
		productsPage = new ProductsPage(driver);
		int cnt=  productsPage.getNumberOfProducts(driver);
		assertEquals(cnt, 2);
		
		 
		productsPage.clickOnGetAspiration(driver); 
		assertTrue(productsPage.isModalPopUpDisplayed_Aspiration(driver));
		productsPage.closePopUp(driver);
		
		 
		productsPage.clickOnGetAspirationPlus(driver); 
		assertTrue(productsPage.isModalPopUpDisplayed_AspirationPlus(driver));
		Pair<String, String> subscriptionValues = productsPage.getSubscriptionPrice(driver);
		
		System.out.println( subscriptionValues.getValue0()  + subscriptionValues.getValue1());
		assertTrue(subscriptionValues.getValue0().contains("71.88"));
		assertTrue(subscriptionValues.getValue1().contains("7.99"));
		
		productsPage.closePopUp(driver); 
		 
	}
 

}
