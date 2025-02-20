

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		JsonPath js = new JsonPath(Payload.coursePrice());
		//print number of courses returned by API
		int count = js.getInt("courses.size()");
		System.out.println(count);
		//print purchase amount
		int purchaseAmt = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmt);
		//print first course title
		String titleFirstCourse = js.getString("courses[0].title");
		System.out.println(titleFirstCourse);
		//print all course title and their respective prices
		for(int i = 0;i<count;i++) {
			String courseTitles = js.get("courses["+i+"].title");
			int coursePrices = js.getInt("courses["+i+"].price");
			System.out.println(courseTitles);
			System.out.println(coursePrices);
			//System.out.println(js.get("courses["+i+"].price").toString());
		}
		System.out.println("Print no of copies sold by RPA course:");
		
		for(int i=0;i<count;i++) {
			String courseTitles = js.get("courses["+i+"].title");
			if(courseTitles.equalsIgnoreCase("cypress")) {
				System.out.println(js.get("courses["+i+"].copies").toString());
				break;
			}
		}

	}

}
