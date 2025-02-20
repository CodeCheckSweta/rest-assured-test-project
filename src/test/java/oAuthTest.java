import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;
import pojo.API;
import pojo.GetCourse;
import pojo.WebAutomation;

public class oAuthTest {

	public static void main(String[] args) {
		String[] courseTitles = {"Selenium Webdriver Java","Cypress","Protractor"}; 
		String resp = given()
			.formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
			.formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
			.formParam("grant_type", "client_credentials")
			.formParam("scope", "trust")
			.when().log().all()
			.post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token").asString();
		System.out.println(resp);
		
		JsonPath jp = new JsonPath(resp);
		String accessToken = jp.get("access_token");
		
		GetCourse gc = given().queryParams("access_token", accessToken)
		.when().log().all()
		.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(GetCourse.class);
		System.out.println(gc);
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
		List<API> apiCourses = gc.getCourses().getApi();
		for(int i=0;i<apiCourses.size();i++) {
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(apiCourses.get(i).getPrice());
			}
		}
		
		ArrayList<String> al = new ArrayList<>();
		List<WebAutomation> w = gc.getCourses().getWebAutomation();
		
		for(int j=0;j<w.size();j++) {
			al.add(w.get(j).getCourseTitle());
		}
		
		List<String> expectedList =Arrays.asList(courseTitles);
		Assert.assertTrue(al.equals(expectedList));
	}

}
