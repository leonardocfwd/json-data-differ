package com.waesst.jsondiffer.service;

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
import com.waesst.jsondiffer.persistence.DiffDao;


@RunWith(MockitoJUnitRunner.class)
public class DiffServiceImplTest {

	@Mock
	DiffDao diffDao;
	
	@InjectMocks
	@Spy
	DiffServiceImpl diffService;
	
	@Test
	public void test_getDiff_calls_dao_load_diff(){
		diffService.getDiff("123");
		Mockito.verify(diffDao, Mockito.times(1)).load(Mockito.anyString());
	}
	
	@Test
	public void test_saveDiff_calls_dao_save_diff(){
		diffService.saveDiff(new Diff());
		Mockito.verify(diffDao, Mockito.times(1)).save(Mockito.any(Diff.class));
	}
	
	@Test
	public void test_validate_returns_error_message_when_left_is_null(){
		String message = diffService.validate(new Diff());
		Assert.assertEquals(MessagesConstants.MUST_PROVIDE_LEFT, message);
	}
	
	@Test
	public void test_validate_returns_error_message_when_right_is_null(){
		Diff diff = new Diff();
		diff.setLeft(new byte[]{0,10,20,30});
		String message = diffService.validate(diff);
		Assert.assertEquals(MessagesConstants.MUST_PROVIDE_RIGHT, message);
	}
	
	@Test
	public void test_performDiff_returns_validate_error_message_when_diff_is_not_valid(){
		Diff diff = new Diff();
		Mockito.doReturn(MessagesConstants.MUST_PROVIDE_LEFT).when(diffService).validate(diff);
		ResponseObject response = diffService.performDiff(diff);
		Assert.assertEquals(MessagesConstants.MUST_PROVIDE_LEFT, response.getMessage());
	}
	
	@Test
	public void test_performDiff_returns_equal_when_files_are_equal(){
		Diff diff = new Diff();
		byte[] byteArray = new byte[]{0,10,20,30};
		diff.setLeft(byteArray);
		diff.setRight(byteArray);
		ResponseObject response = diffService.performDiff(diff);
		Assert.assertEquals(MessagesConstants.EQUAL, response.getMessage());
	}
	
	@Test
	public void test_performDiff_returns_not_equal_size_when_files_have_diff_sizes(){
		Diff diff = new Diff();
		byte[] byteArrayLeft = new byte[]{0,10,20,30};
		byte[] byteArrayRight = new byte[]{0,10,20,30,40,50};
		diff.setLeft(byteArrayLeft);
		diff.setRight(byteArrayRight);
		ResponseObject response = diffService.performDiff(diff);
		Assert.assertEquals(MessagesConstants.NOT_EQUAL_SIZE, response.getMessage());
	}
	
	@Test
	public void test_performDiff_returns_diff_insights_when_files_have_same_size(){
		Diff diff = new Diff();
		byte[] byteArrayLeft = new byte[]{0,10,20,30,80,90};
		byte[] byteArrayRight = new byte[]{0,10,20,30,40,50};
		diff.setLeft(byteArrayLeft);
		diff.setRight(byteArrayRight);
		Set<Integer> offsets = new HashSet<>();
		offsets.add(4);
		offsets.add(5);
		ResponseObject response = diffService.performDiff(diff);
		Assert.assertEquals(response.getMessage(), MessagesConstants.SAME_SIZE);
		Assert.assertEquals(response.getLength(), new Integer(byteArrayLeft.length));
		Assert.assertArrayEquals(response.getDiffOffsets().toArray(), offsets.toArray());
	}
	
}
