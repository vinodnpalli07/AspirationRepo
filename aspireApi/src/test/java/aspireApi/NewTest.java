package aspireApi;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.Logger;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class NewTest { 
	
	private static final String BASE_URL = "https://swapi.dev/api/";

	private static String path;
	private static String token;
	private RequestSpecification request;
	private static Response response;
	private static String jsonString;
	private static String bookId;
	
	
  @Test
  public void getPeopleAPI() {
	  
	  
	  	List<String> listOfPeople = Arrays.asList("Darth Vader", "Chewbacca", "Roos Tarpals", "Rugor Nass", "Yarael Poof", "Lama Su",
	  			"Tuan Wu", "Grievous", "Tarfful", "Tion Medon"); 
		
	  	String getUrl = "https://swapi.dev/api/people";
		
		Response response = RestAssured.given().header("APIVersion", "3").accept(ContentType.JSON)
				.contentType(ContentType.JSON).urlEncodingEnabled(false).when().get(getUrl);

		System.out.println("REQUEST OF API : " + getUrl);
		System.out.println("RESPONSE OF API : " + response.then().log().all().toString());
		
		response.then().assertThat().statusCode(200);
		int totalNumberOfPeople = response.then().extract().path("count");
		assertEquals(totalNumberOfPeople, 82, "Mismatch in total number of People");
		
		//Height is sent as String, we need to parse to Int and then verify.
		List<String> peoplesHeight = JsonPath.read(response.asString(), "$.results[*].height");
		
		List<String> people = JsonPath.read(response.asString(), "$.results[*].name");
		
		//Verifing the list of people to API response.
		assertTrue(people.equals(listOfPeople));
		
		//printing all people from API response.
		for(String a:people){
		    System.out.println("People Names : "+a);
		     
		} 
		 
		
  }
}
