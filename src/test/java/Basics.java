import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class Basics {
	public static void main(String[] args) throws IOException {
		//validate if add place api is working as expected
		//given - all input details
		//when - submit the api --resource and http method
		//then - validate the response
		//Convert the content of the file to String -> convert the content of file in byte -> Byte data to String
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
		.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\Administrator\\Documents\\addPlace.json")))).when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.52 (Ubuntu)").extract().asString();
		//add place ->update place with new address ->Get place to validate if new address is present in response
		System.out.println(response);
		JsonPath jp = ReUsableMethods.rawToJson(response);	//for parsing json
		String placeId = jp.getString("place_id");
		System.out.println(placeId);
		
		//update place
		String newAddress = "Summer Walk, Africa";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "").when().put("/maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//get place
		String getPlaceResp = given().log().all().queryParam("key", "qaclick123").queryParam("place_id",placeId)
		.when().get("/maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		JsonPath js = ReUsableMethods.rawToJson(getPlaceResp);
		String ActualAddress = js.getString("address");
		Assert.assertEquals(ActualAddress, newAddress);
	}

}
