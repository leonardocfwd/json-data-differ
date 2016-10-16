package com.waesst.jsondiffer.controller;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.waesst.jsondiffer.constants.MessagesConstants;
import com.waesst.jsondiffer.dto.ResponseObject;
import com.waesst.jsondiffer.model.Diff;
import com.waesst.jsondiffer.model.ResourceSide;
import com.waesst.jsondiffer.service.DiffService;

@RunWith(MockitoJUnitRunner.class)
public class DiffControllerTest {
	
	private final Integer OFFSET_VALUE = 5;
	private final Integer OBJECT_LENGTH = 20;
	
	@Mock
	DiffService diffService;
	
	@InjectMocks
	@Spy
	DiffController diffController;
	
	@Test
	public void test_getDiffResults_returns_not_empty_result(){
		Mockito.when(diffService.getDiff(Mockito.anyString())).thenReturn(new Diff());
		Mockito.when(diffService.performDiff(Mockito.any(Diff.class))).thenReturn(getResponseObjectInstance());
		
		ResponseObject response = diffController.getDiffResults("123");
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getMessage());
		Assert.assertNotNull(response.getDiffOffsets());
		Assert.assertFalse(response.getDiffOffsets().isEmpty());
		Assert.assertNotNull(response.getLength());
		Assert.assertEquals(MessagesConstants.EQUAL, response.getMessage());
		Assert.assertEquals(response.getDiffOffsets().iterator().next(), OFFSET_VALUE);
		Assert.assertEquals(response.getLength(), OBJECT_LENGTH);
	}
	
	@Test
	public void test_getDiffResults_returns_error_message_when_diff_id_is_empty(){
		ResponseObject response = diffController.getDiffResults(null);
		Assert.assertEquals(MessagesConstants.DIFF_ID_CANT_BE_EMPTY, response.getMessage());
		Mockito.verify(diffService, Mockito.times(0)).performDiff(Mockito.any(Diff.class));
		response = diffController.getDiffResults(" ");
		Assert.assertEquals(MessagesConstants.DIFF_ID_CANT_BE_EMPTY, response.getMessage());
		Mockito.verify(diffService, Mockito.times(0)).performDiff(Mockito.any(Diff.class));
	}
	
	@Test
	public void test_getDiffResults_returns_error_message_when_diff_is_null(){
		Mockito.when(diffService.getDiff(Mockito.anyString())).thenReturn(null);
		ResponseObject response = diffController.getDiffResults("123");
		Assert.assertEquals(MessagesConstants.DIFF_NOT_FOUND, response.getMessage());
		Mockito.verify(diffService, Mockito.times(0)).performDiff(Mockito.any(Diff.class));
	}
	
	@Test
	public void test_storeLeftData_forwards_to_file_store_process(){
		diffController.storeLeftData("123", new byte[]{});
		Mockito.verify(diffController,Mockito.times(1)).processStoreRequest(Mockito.anyString(), Mockito.any(byte[].class), Mockito.any(ResourceSide.class));
	}
	
	@Test
	public void test_storeRightData_forwards_to_file_store_process(){
		diffController.storeRightData("123", new byte[]{});
		Mockito.verify(diffController,Mockito.times(1)).processStoreRequest(Mockito.anyString(), Mockito.any(byte[].class), Mockito.any(ResourceSide.class));
	}
	
	@Test
	public void test_processStoreRequest_doesnt_call_persistDiff_when_invalid_request(){
		Mockito.doReturn(false).when(diffController).isRequestValid(Mockito.anyString(), Mockito.any(byte[].class), Mockito.any(ResponseObject.class));
		ResponseObject response = diffController.processStoreRequest("", new byte[]{}, ResourceSide.LEFT);
		Assert.assertNotNull(response);
		Mockito.verify(diffController,Mockito.times(0)).persistDiff(Mockito.anyString(),Mockito.any(byte[].class), Mockito.any(ResourceSide.class));
	}
	
	@Test
	public void test_processStoreRequest_calls_persistDiff_when_valid_request(){
		Mockito.doReturn(true).when(diffController).isRequestValid(Mockito.anyString(), Mockito.any(byte[].class), Mockito.any(ResponseObject.class));
		Mockito.doReturn(new ResponseObject()).when(diffController).persistDiff(Mockito.anyString(),Mockito.any(byte[].class), Mockito.any(ResourceSide.class));
		ResponseObject response = diffController.processStoreRequest("", new byte[]{}, ResourceSide.LEFT);
		Assert.assertNotNull(response);
		Mockito.verify(diffController,Mockito.times(1)).persistDiff(Mockito.anyString(),Mockito.any(byte[].class), Mockito.any(ResourceSide.class));
	}
	
	@Test
	public void test_isRequestValid_returns_false_when_diff_id_is_null_or_blank(){
		ResponseObject response = Mockito.spy(new ResponseObject());
		Assert.assertFalse(diffController.isRequestValid(null, new byte[]{}, response));
		Assert.assertFalse(diffController.isRequestValid("    ", new byte[]{}, response));
		Mockito.verify(response, Mockito.times(2)).setMessage(MessagesConstants.DIFF_ID_CANT_BE_EMPTY);
	}
	
	@Test
	public void test_isRequestValid_returns_false_when_bytearray_is_null_or_empty(){
		ResponseObject response = Mockito.spy(new ResponseObject());
		Assert.assertFalse(diffController.isRequestValid("123", null, response));
		Assert.assertFalse(diffController.isRequestValid("123", new byte[]{}, response));
		Mockito.verify(response, Mockito.times(2)).setMessage(MessagesConstants.MUST_PROVIDE_FILE);
	}
	
	@Test
	public void test_isRequestValid_returns_true_when_valid_request(){
		ResponseObject response = Mockito.spy(new ResponseObject());
		Assert.assertTrue(diffController.isRequestValid("123", new byte[]{0,10,20,30}, response));
	}
	
	@Test
	public void test_persistDiff_persists_new_Diff_when_diff_not_found(){
		Mockito.when(diffService.getDiff(Mockito.anyString())).thenReturn(null);
		
		ResponseObject response = diffController.persistDiff("123",  Base64.getEncoder().encode(new byte[]{72, 101, 108, 108, 111, 32}), ResourceSide.LEFT);
		Assert.assertEquals(MessagesConstants.FILE_SAVED_SUCCESSFULLY, response.getMessage());
		Mockito.verify(diffService, Mockito.times(1)).saveDiff(Mockito.any(Diff.class));
	}
	
	@Test
	public void test_persistDiff_persists_diff_when_found(){
		Diff diff = getDiffInstance();
		Mockito.when(diffService.getDiff(Mockito.anyString())).thenReturn(diff);
		ResponseObject response = diffController.persistDiff("123", Base64.getEncoder().encode(new byte[]{72, 101, 108, 108, 111, 32, 109, 121, 32, 110, 97}), ResourceSide.LEFT);
		Assert.assertEquals(MessagesConstants.FILE_SAVED_SUCCESSFULLY, response.getMessage());
		Mockito.verify(diffService, Mockito.times(1)).saveDiff(diff);
	}
	
	@Test
	public void test_persistDiff_sets_left_on_diff(){
		Diff diff = Mockito.spy(getDiffInstance());
		byte[] jsonBase64ByteArray = Base64.getEncoder().encode(new byte[]{72, 101, 108, 108, 111, 32, 109, 121, 32, 110, 97});
		byte[] decodedByteArray = Base64.getDecoder().decode(jsonBase64ByteArray);
		Mockito.when(diffService.getDiff(Mockito.anyString())).thenReturn(diff);
		ResponseObject response = diffController.persistDiff("123",jsonBase64ByteArray , ResourceSide.LEFT);
		Assert.assertEquals(MessagesConstants.FILE_SAVED_SUCCESSFULLY, response.getMessage());
		Mockito.verify(diffService, Mockito.times(1)).saveDiff(diff);
		Mockito.verify(diff).setLeft(decodedByteArray);
		Mockito.verify(diff,Mockito.times(0)).setRight(Mockito.any(byte[].class));
	}
	
	@Test
	public void test_persistDiff_sets_right_on_diff(){
		Diff diff = Mockito.spy(getDiffInstance());
		byte[] jsonBase64ByteArray = Base64.getEncoder().encode(new byte[]{72, 101, 108, 108, 111, 32, 109, 121, 32, 110, 97});
		byte[] decodedByteArray = Base64.getDecoder().decode(jsonBase64ByteArray);
		Mockito.when(diffService.getDiff(Mockito.anyString())).thenReturn(diff);
		ResponseObject response = diffController.persistDiff("123",jsonBase64ByteArray , ResourceSide.RIGHT);
		Assert.assertEquals(MessagesConstants.FILE_SAVED_SUCCESSFULLY, response.getMessage());
		Mockito.verify(diffService, Mockito.times(1)).saveDiff(diff);
		Mockito.verify(diff).setRight(decodedByteArray);
		Mockito.verify(diff,Mockito.times(0)).setLeft(Mockito.any(byte[].class));
	}
	
	private ResponseObject getResponseObjectInstance(){
		ResponseObject response = new ResponseObject();
		response.setMessage(MessagesConstants.EQUAL);
		Set<Integer> offsets = new HashSet<>();
		offsets.add(OFFSET_VALUE);
		response.setDiffOffsets(offsets);
		response.setLength(OBJECT_LENGTH);
		return response;
	}
	
	private Diff getDiffInstance(){
		Diff diff = new Diff();
		diff.setId("16102016");
		diff.setLeft(new byte[]{72, 101, 108, 108, 111, 32, 109, 121, 32, 110, 97});
		diff.setRight(new byte[]{72, 101, 108, 108, 111, 32, 109, 121, 32, 110, 97});
		return diff;
	}
	
}
