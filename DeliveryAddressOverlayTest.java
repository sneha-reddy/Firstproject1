package TestCases;

import java.lang.reflect.Method;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import Global.GlobalTest;

public class DeliveryAddressOverlayTest extends GlobalTest{

	@BeforeClass(alwaysRun = true)
	@Parameters({"browserName","version"})
	public void setup(String browserName, String version) throws InterruptedException{
		FrameworkLogger.InitiateClassLevelReport(getClass().getSimpleName());
		commonFunction.launchApp(browserName, version);
		homePage().login(getData.addAddressUser2, getData.Password);
		commonFunction.addProductOnlyWhenCartIsEmpty(getData.ProdTomatoes);
		navigation.navigateToCheckoutAndHandleOOS();
		checkoutPage().handleCheckoutPopups();	
		checkoutPage().handleCheckoutPopups();	

	}

	@BeforeMethod(alwaysRun = true)
	public void testcase(Method method){
		FrameworkLogger.InitiateMethodLevelReport(method,getClass().getSimpleName());
		checkoutPage().clickOnDeliveryAddressChange();
	}

	@AfterMethod(alwaysRun = true) 
	public void getResult(ITestResult result){
		FrameworkLogger.VerifyAfterTest(result);
	}

	@Test(priority = 1)
	public void clickOffTheModule(){
		checkoutPage().clickOnCheckoutHeader();
		myAssert.assertFalse(checkoutPage().isDeliveryAddressForm(),"Clicking the 'Cancel' button closes the Delivery address overlay");
		checkoutPage().clickOnDeliveryAddressChange();
	}

	@Test(priority = 2)
	public void validateDelievryInstructionForAddress(){
		String DelInstruction=checkoutPage().assertDeliveryInstructionsAvailable();
		myAssert.assertTrue(true,"Delivery Instruction is :"+DelInstruction);
	}

	@Test(priority = 3)
	public void validateLoadedAddress(){
		myAssert.assertTrue(checkoutPage().isDeliveryMethodLoaded(), "When opened all the Delivery address methods are loaded");
	}

	@Test(priority = 4)
	public void cancelButtonValidation(){
		checkoutPage().clickOnDeliveryAddressCancel();
		myAssert.assertFalse(checkoutPage().isDeliveryAddressForm(),"Clicking the 'Cancel' button closes the Delivery address overlay");
	}

	@Test(priority = 5)
	public void selectDeliveryAddress(){
		String DeliveryAddressForm=checkoutPage().selectDeliveryAddress();
		checkoutPage().clickOnDeliveryAddressDone();	
		myAssert.assertFalse(checkoutPage().isDeliveryAddressForm(),"Delivery Address form is closed");
		String DeliveryAddress=checkoutPage().txtDeliveryAddressTitle();
		myAssert.assertTrue(DeliveryAddressForm.contains(DeliveryAddress),"Selected delivery address selects the delivery address for checkout");
	}

	@Test(priority = 6)
	public void deleteDeliveryAddress(){
		checkoutPage().manageTwoAddresses();
		checkoutPage().clickOnDeliveryAddressDelete();
		myAssert.assertTrue(checkoutPage().isDeleteOverlayDisplayed()," 'Delete' button beneath a delivery address opens the 'delivery address deletion overlay'");
		checkoutPage().closeConfirmationPopup();
		int countBefore=checkoutPage().countOfAddressInCheckoutPage();
		checkoutPage().clickOnDeliveryAddressDelete();
		checkoutPage().clickCancelOnDeleteConfirmationPopup();
		int countAfter=checkoutPage().countOfAddressInCheckoutPage();
		myAssert.assertTrue(countBefore==countAfter, "Clicking 'Cancel' on Delete Confirmation popup did not delete any address");
		int AddressCountBefore=checkoutPage().countOfAddressInCheckoutPage();
		checkoutPage().clickOnDeliveryAddressDelete();
		checkoutPage().clickDeleteOnDeleteConfirmationPopup();
		int AddressCountAfter=checkoutPage().countOfAddressInCheckoutPage();
		myAssert.assertTrue(AddressCountAfter<AddressCountBefore, "Deleting a delivery address removes it from delivery address overlay");
		myAssert.assertFalse(checkoutPage().isDeleteButtonAvailable(), "Delete button is not present for an user with only one delivery address");
		checkoutPage().clickAddAddress();
		checkoutPage().provideDeliveryAddressDetails();
		checkoutPage().clickSaveNewDeliveryAddress();
		checkoutPage().selectDeliveryAddress();
		checkoutPage().clickOnDeliveryAddressDone();
	}

	@Test(priority = 7)
	public void editDeliveryAddress(){
		checkoutPage().clickOnDeliveryAddressEdit();
		myAssert.assertTrue(checkoutPage().isEditDeliveryAddressFormDisplayed(), "'Edit' button beneath a delivery address opens the 'Edit delivery address' overlay");
		checkoutPage().clickOnEditDeliveryAddressClose();
		checkoutPage().scrollDeliveryAddressDone();
	}

	@Test(priority = 8)
	public void addAddressButton(){
		myAssert.assertTrue(checkoutPage().isAddAddressButtonAvailableAtEnd(), "'Add address' button is available at the end");
		checkoutPage().clickOnAddAddress();
		myAssert.assertTrue(checkoutPage().isEditDeliveryAddressFormDisplayed(), "'Add address' button opens the 'Add address' overlay");
		checkoutPage().clickOnEditDeliveryAddressClose();
		checkoutPage().scrollDeliveryAddressDone();
	}
	
	@Test(priority = 9)
	public void messageValidationforChangingDeliveryAddress(){
		myAssert.assertTrue(checkoutPage().isRemindingMessageDisplayed(), "Messaging is displayed to remind users that changing their address may affect delivery times.");
		checkoutPage().clickOnDeliveryAddressCancel();
	}
	
	@Test(priority = 10)
	public void validateGrayedOutDeliveryAddressisOpen(){
		myAssert.assertTrue(checkoutPage().isGrayedOut(), "While Delivery Address Overlay is Open Below area is Grayed Out");
		myAssert.assertTrue(checkoutPage().isFooterEnabled(), "While Delivery Address Overlay is Open Footer area is Enabled");
		myAssert.assertTrue(checkoutPage().isHeaderEnabled(), "While Delivery Address Overlay is Open Header area is Enabled");
		checkoutPage().clickOnDeliveryAddressCancel();
	}
}
