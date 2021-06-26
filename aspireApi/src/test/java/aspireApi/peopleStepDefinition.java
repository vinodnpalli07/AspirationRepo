package aspireApi;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.And;
import cucumber.api.junit.Cucumber;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
public class peopleStepDefinition {

	private static final String BASE_URL = "https://swapi.dev/api";

	private static String path;
	private static String token;
	private RequestSpecification request;
	private static Response response;
	private static String jsonString;
	private static String bookId;



	@Given("^the peoples endpoint exists$")
	public void the_peoples_endpoint_exists() throws Throwable {

		RestAssured.baseURI = BASE_URL;
		request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.contentType(ContentType.JSON);


	}

	@When("^requested for list of people$")
	public void requested_for_list_of_people() throws Throwable {
		path = "/people";
		response = request
				.post(path)
				.then().extract().response();
		
		System.out.println(response);
	}

	@Then("^response status code should be 200$")
	public void response_status_code_should_be_200() throws Throwable {

	}

	@And("^the total number of people equals to 82$")
	public void the_total_number_of_people_equals_to_82() throws Throwable {

	}

	@And("^ there are 10 people whose height is greater than 200 $")
	public void there_are_10_people_whose_height_is_greater_than_200() throws Throwable {

	}

	@And("^the peoples names contains Darth Vader, Chewbacca, Roos Tarpals, Rugor Nass, Yarael Poof, Lama Su, Tuan Wu, Grievous, Tarfful, Tion Medon$")
	public void the_peoples_names_contains_darth_vader_chewbacca_roos_tarpals_rugor_nass_yarael_poof_lama_su_tuan_wu_grievous_tarfful_tion_medon() throws Throwable {

	}

}