import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.AddPlace;
import pojo.Location;

public class SerializeTest {
	public static void main(String[] args) {
		AddPlace ap = new AddPlace();
		ap.setAccuracy(50);
		ap.setAddress("29, side layout, cohen 09");
		ap.setLanguage("French-IN");
		ap.setPhone_number("(+91) 983 893 3937");
		ap.setWebsite("http://google.com");
		ap.setName("Frontline house");
		List<String> myList = new ArrayList<>();
		myList.add("shoe park");
		myList.add("shop");
		ap.setType(myList);
		Location l = new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		ap.setLocation(l);
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		Response res = given().queryParam("key","qaclick123")
				.body(ap)
				.when().post("/maps/api/place/add/json")
				.then().extract().response();
		String resString = res.asPrettyString();
		System.out.println(resString);
		
	}
}
