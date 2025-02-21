package eCommerce;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.OrderDetail;
import pojo.Orders;

public class ECommerceAPITest {

	public static void main(String[] args) {
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("sweta.singh0322@gmail.com");
		loginRequest.setUserPassword("Sweta@12345");
		RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);
		LoginResponse loginResponse = reqLogin.when().post("api/ecom/auth/login")
								.then().log().all().extract().response().as(LoginResponse.class);
		String token = loginResponse.token;
		String userId = loginResponse.userId;
		
		//Add Product
		RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).build();
		RequestSpecification addProductReq	= given().log().all().spec(addProductBaseReq).formParam("productName", "Laptop").formParam("productAddedBy", userId)
				.formParam("productCategory", "Everyday Computing").formParam("productSubCategory", "Hybrid")
				.formParam("productPrice", 13500).formParam("productDescription", "Apple M3")
				.formParam("productFor", "Students").multiPart("productImage", new File("C:\\Users\\Administrator\\Pictures\\Laptop.jpg"));
		String addProductResp = addProductReq.when().post("api/ecom/product/add-product")
				.then().log().all().extract().response().asPrettyString(); 
		JsonPath js = new JsonPath(addProductResp);
		String productId = js.get("productId");
		
		//Create Order
		RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).setContentType(ContentType.JSON).build();
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setCountry("India");
		orderDetail.setProductOrderedId(productId);
		
		List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
		orderDetailList.add(orderDetail);
		Orders orders = new Orders();
		orders.setOrders(orderDetailList);
		
		RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseReq).body(orders);
		String responseCreateOrder = createOrderReq.when().post("api/ecom/order/create-order")
				.then().log().all().extract().response().asPrettyString();
		JsonPath js1 = new JsonPath(responseCreateOrder);
		String productOrderId = js1.get("productOrderId");
		System.out.println(productOrderId);
		
		//Delete Order
		RequestSpecification deleteOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).build();
		RequestSpecification deleteOrderReq = given().log().all().spec(deleteOrderBaseReq).pathParam("productId", productId);
		String deleteProductResponse = deleteOrderReq.when().delete("api/ecom/product/delete-product/{productId}")
				.then().log().all().extract().response().asPrettyString();
		JsonPath js2 = new JsonPath(deleteProductResponse);
		Assert.assertEquals("Product Deleted Successfully", js2.get("message"));
	}

}
