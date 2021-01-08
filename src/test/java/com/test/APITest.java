package com.test;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APITest {
	private static Logger log = Logger.getLogger(APITest.class);
	@BeforeClass
	private void baseURL() {
		RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
	}
	
	@Test(priority = 1)
	private void userId9Posts() {
		log.info("=================================================");
		log.info("User 9 total Number of post is "+ getPostCount(9));
		log.info("=================================================");
	}
	
	
	@Test(priority = 2)
	private void userId3AllCommentsOnHisPosts() {
		log.info("=================================================");
		log.info("User 3 total Number of comments is "+ getCommentCount(3));
		log.info("=================================================");
	}
	
	
	@Test(priority = 3)
	private void printUserInfo() {
		//format: |User Name     |Full Name      |Email Id               |Total number of posts  |Total number of comments received |
	
		Response resp = getUserDetails();
		JSONArray users  = new JSONArray(resp.asString());
		
		for(Object obj: users) {
			JSONObject user = (JSONObject)obj;
			System.out.print("|"+user.getString("username"));
			System.out.print("|"+user.getString("name"));
			System.out.print("|"+user.getString("email"));
			System.out.print("|"+getPostCount(user.getInt("id")));
			System.out.print("|"+getCommentCount(user.getInt("id")));
			System.out.println();
		}
	
	}
	
	
	private int getPostCount(int userId) {
		Response postsResp = getAllPost();
		JSONArray posts = new JSONArray(postsResp.asString());
		int totalNumOfPost = 0;
		for(Object p:posts) {
			JSONObject post = (JSONObject)p;
			if(post.getInt("userId")==userId) {
				totalNumOfPost = totalNumOfPost+1;
			}
		}
		
		return totalNumOfPost;
	}
	
	
	@Test
	private int getCommentCount(int userId) {
		Response postsResp = getAllPost();
		JSONArray posts = new JSONArray(postsResp.asString());
		int totalNumOfComment = 0;
		for(Object p:posts) {
			JSONObject post = (JSONObject)p;
			if(post.getInt("userId")==userId) {
				Response commnetsResp = getComments(post.getInt("id"));
				JSONArray comments = new JSONArray(commnetsResp.asString());
				totalNumOfComment= totalNumOfComment + comments.length();
			}
		}
		
		return totalNumOfComment;
	}
	
	
	
	
	private Response getUserDetails() {
		Response resp = request().get("/users");
		//resp.then().log().all();
		return resp;
	}
	
	
	private Response getAllPost() {
		Response resp = request().get("/posts");
		//resp.then().log().all();
		return resp;
	}
	
	private Response getComments(int postId) {		
		Response resp = request().pathParam("postId", postId)
				                 .get("/posts/{postId}/comments ");
		//resp.then().log().all();
		return resp;
	}
	
	
	private Response getAllComments() {
		Response resp = request().get("/comments ");
		//resp.then().log().all();
		return resp;
	}
	
	
	private RequestSpecification request() {
		return RestAssured.given();
	}

}
