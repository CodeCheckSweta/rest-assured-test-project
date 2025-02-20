import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class AuthenticationTest {
    @Test
    public void testAuthenticationApi() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")   // Set API base url
                .setBasePath("/auth")   // Set endpoint with path parameters
                .setContentType(ContentType.JSON)   // Set Content-Type header
                .setAccept(ContentType.ANY)   // Set Accept header
                .setBody("{\n\"username\": \"admin\",\n    \"password\": \"password123\"\n}");

        RestAssured
                .given(requestSpecBuilder.build()).log().all()
                .when()
                .post()
                .then().log().all()
                .statusCode(200); // Validate expected status code
    }
}