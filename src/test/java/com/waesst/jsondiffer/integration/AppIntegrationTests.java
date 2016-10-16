package com.waesst.jsondiffer.integration;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.waesst.jsondiffer.constants.MessagesConstants;
import com.waesst.jsondiffer.dto.ResponseObject;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Category(IntegrationTest.class)
public class AppIntegrationTests {

	private final String DIFF_SERVICE = "/v1/diff/";
	private final String DIFF_ID_PATH = "{id}";
	private final String LEFT_SERVICE = "/left";
	private final String RIGHT_SERVICE = "/right";
	private final String DIFF_ID = "123";
	
	@Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void test_1_leftService_stores_left_data_successfully(){
    	HttpEntity<byte[]> base64ByteArray = new HttpEntity<byte[]>(getLeftByteArray());
    	ResponseEntity<ResponseObject> response = this.restTemplate.exchange(
    			DIFF_SERVICE+DIFF_ID_PATH+LEFT_SERVICE, HttpMethod.POST, base64ByteArray,
    			ResponseObject.class, DIFF_ID);
    	Assert.assertEquals(MessagesConstants.FILE_SAVED_SUCCESSFULLY, response.getBody().getMessage());
    	
    }
    
    @Test
    public void test_2_rightService_stores_right_data_successfully(){
    	HttpEntity<byte[]> base64ByteArray = new HttpEntity<byte[]>(getRightByteArray());
    	ResponseEntity<ResponseObject> response = this.restTemplate.exchange(
    			DIFF_SERVICE+DIFF_ID_PATH+RIGHT_SERVICE, HttpMethod.POST, base64ByteArray,
    			ResponseObject.class, DIFF_ID);
    	Assert.assertEquals(MessagesConstants.FILE_SAVED_SUCCESSFULLY, response.getBody().getMessage());
    }
    

    @Test
    public void test_3_diffService_returns_diff_details() {
    	Set<Integer> offsets = new HashSet<>();
		offsets.add(4);
		offsets.add(6);
		offsets.add(7);
        ResponseEntity<ResponseObject> response = this.restTemplate.getForEntity(
            DIFF_SERVICE+DIFF_ID_PATH, ResponseObject.class, DIFF_ID);
        Assert.assertEquals(MessagesConstants.SAME_SIZE, response.getBody().getMessage());
        Assert.assertArrayEquals(response.getBody().getDiffOffsets().toArray(), offsets.toArray());
        Assert.assertEquals(response.getBody().getLength(), new Integer(9));
    }
    
    @Test
    public void test_4_diffService_returns_equal_message_when_files_are_equal(){
    	//GIVEN
    	HttpEntity<byte[]> base64ByteArray = new HttpEntity<byte[]>(getSimpleByteArray());
    	this.restTemplate.exchange(
    			DIFF_SERVICE+DIFF_ID_PATH+RIGHT_SERVICE, HttpMethod.POST, base64ByteArray,
    			ResponseObject.class, DIFF_ID);
    	this.restTemplate.exchange(
    			DIFF_SERVICE+DIFF_ID_PATH+LEFT_SERVICE, HttpMethod.POST, base64ByteArray,
    			ResponseObject.class, DIFF_ID);
    	
    	//WHEN
    	ResponseEntity<ResponseObject> response = this.restTemplate.getForEntity(
                DIFF_SERVICE+DIFF_ID_PATH, ResponseObject.class, DIFF_ID);
    	
    	//THEN
        Assert.assertEquals(MessagesConstants.EQUAL, response.getBody().getMessage());
    }
    
    @Test
    public void test_5_diffService_returns_not_equal_size_message_when_files_do_not_have_same_size(){
    	//GIVEN
    	HttpEntity<byte[]> base64ByteArray = new HttpEntity<byte[]>(getSimpleByteArray());
    	HttpEntity<byte[]> largerbase64ByteArray = new HttpEntity<byte[]>(getLargerByteArray());
    	this.restTemplate.exchange(
    			DIFF_SERVICE+DIFF_ID_PATH+RIGHT_SERVICE, HttpMethod.POST, base64ByteArray,
    			ResponseObject.class, DIFF_ID);
    	this.restTemplate.exchange(
    			DIFF_SERVICE+DIFF_ID_PATH+LEFT_SERVICE, HttpMethod.POST, largerbase64ByteArray,
    			ResponseObject.class, DIFF_ID);
    	
    	//WHEN
    	ResponseEntity<ResponseObject> response = this.restTemplate.getForEntity(
                DIFF_SERVICE+DIFF_ID_PATH, ResponseObject.class, DIFF_ID);
    	
    	//THEN
        Assert.assertEquals(MessagesConstants.NOT_EQUAL_SIZE, response.getBody().getMessage());
    }
    
    private byte[] getLeftByteArray(){
    	byte[] byteArray = new byte[]{0,10,20,30,40,50,60,70,80};
    	return Base64.getEncoder().encode(byteArray);
    }
   
    private byte[] getRightByteArray(){
    	byte[] byteArray = new byte[]{0,10,20,30,55,50,88,99,80};
    	return Base64.getEncoder().encode(byteArray);
    }
    
    private byte[] getSimpleByteArray(){
    	byte[] byteArray = new byte[]{0,10,20,30,55,50,88,99,80};
    	return Base64.getEncoder().encode(byteArray);
    }
    
    private byte[] getLargerByteArray(){
    	byte[] byteArray = new byte[]{0,10,20,30,55,50,88,99,80,108,98,27,87};
    	return Base64.getEncoder().encode(byteArray);
    }
    
}
