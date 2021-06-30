package web.aspiration.pages;
 
import org.javatuples.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.aspiration.framework.selenium.SeleniumHelper;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

public class ProductsPage extends PageObject{
	
	WebDriver driver;
	
	private By divProduct = By.xpath("//div[@class='plan-content']"); 
	private By btnPlanPlus = By.cssSelector(".plan-a-plus .btn"); 
	private By btnOrginal = By.cssSelector(".plan-original .btn"); 
	private By btnOrginalPlus = By.cssSelector(".plan-a-plus .btn");
	private By btnClose = By.cssSelector(".plan-a-plus .btn")
	
	@FindBy(xpath = "(//button[contains(.,'Get Aspiration')])[1]")
	private WebElementFacade btnGetAspiration;		
	
	@FindBy(xpath = "//button[contains(.,'Get Aspiration Plus')]")
	private WebElementFacade btnGetAspirationPlus;	
	
	@FindBy(xpath = "//div[@class='inside-form']")
	private WebElementFacade modalPopup;
	
	public ProductsPage(WebDriver driver) { 

		this.driver = driver;
		//PageFactory.initElements(driver, this);  // Commented out on purpose to show to Factory Initialization
	}
	
	/**
	 * 
	 * @param driver
	 * @return
	 */
	public int getNumberOfProducts(WebDriver driver) {
		return SeleniumHelper.findElements(driver, divProduct).size();
	}
	
	/**
	 * 
	 * @param driver
	 * @return
	 */
	public boolean isModalPopUpDisplayed_Aspiration(WebDriver driver) {
		return SeleniumHelper.isWebElementVisible(driver, btnPlanPlus ,3500);
	}
	
	/**
	 * 
	 * @param driver
	 * @return
	 */
	public boolean isModalPopUpDisplayed_AspirationPlus(WebDriver driver) {
		return SeleniumHelper.isWebElementVisible(driver, btnPlanPlus ,3500);
	}
	
	/**
	 * Returns the Prices
	 * @param driver
	 * @return
	 */
	public Pair<String,String> getSubscriptionPrice(WebDriver driver)
	{
		SeleniumHelper.click(driver, By.xpath("//div[2]/div"));
		String yearly = SeleniumHelper.getText(driver, By.xpath("//div[2]/div/p")); 
		
		SeleniumHelper.click(driver, By.xpath("//div[2]/div[2]/div"));		
		String monthly = SeleniumHelper.getText(driver, By.xpath("//div[2]/p"));
		 
		return  new Pair<String,String>(yearly,monthly) ; 
	}

	/**
	 * 
	 * @param driver
	 */
	public void clickOnGetAspiration(WebDriver driver) { 
		driver.findElement(btnOrginal).click();
	}
	
	/**
	 *
	 * @param driver
	 */
	public void clickOnGetAspirationPlus(WebDriver driver) { 
		driver.findElement(btnOrginalPlus).click();
	}
	
	/**
	 * 
	 * @param driver
	 */
	public void closePopUp(WebDriver driver) {
		SeleniumHelper.findElement(driver, btnClose).sendKeys(Keys.ESCAPE);
	}
	
}
