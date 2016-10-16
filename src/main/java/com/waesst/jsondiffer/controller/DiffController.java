package com.waesst.jsondiffer.controller;

import java.util.Base64;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.waesst.jsondiffer.constants.MessagesConstants;
import com.waesst.jsondiffer.dto.ResponseObject;
import com.waesst.jsondiffer.model.Diff;
import com.waesst.jsondiffer.model.ResourceSide;
import com.waesst.jsondiffer.service.DiffService;

/**
 * Rest Controller with the HTTP Endpoints to store byte arrays, and get the diff results.
 * I'm assuming that the ID in the assessment refers to a Diff entity, which represents the results of a certain comparison 
 * between two binaries. Thats also why I have created a Diff class to store the contents.
 * 
 * @author Leonardo Nelson
 *
 */
@RestController
@RequestMapping("/v1/diff")
public class DiffController {

	@Autowired
	private DiffService diffService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseObject getDiffResults(@PathVariable("id") String id){
		ResponseObject response = new ResponseObject();

		if(StringUtils.isEmpty(id) || StringUtils.isBlank(id)){
			response.setMessage(MessagesConstants.DIFF_ID_CANT_BE_EMPTY);
			return response;
		}

		Diff diff = diffService.getDiff(id);
		if(diff == null){
			response.setMessage(MessagesConstants.DIFF_NOT_FOUND);
			return response;
		}

		response = diffService.performDiff(diff);
		return response;
	}

	@RequestMapping(value = "/{id}/left", method = RequestMethod.POST)
	public @ResponseBody ResponseObject storeLeftData(@PathVariable("id") String id, @RequestBody byte[] base64JsonByteAray){
		System.out.println("Entering Left Endpoint");
		return processStoreRequest(id, base64JsonByteAray, ResourceSide.LEFT);
	}

	@RequestMapping(value = "/{id}/right", method = RequestMethod.POST)
	public @ResponseBody ResponseObject storeRightData(@PathVariable("id") String id, @RequestBody byte[] base64JsonByteAray){
		System.out.println("Entering Right Endpoint");
		return processStoreRequest(id, base64JsonByteAray, ResourceSide.RIGHT);
	}
	
	/**
	 * Flow-control method to proceed or refuse a given request.
	 * @param id The diff ID
	 * @param base64JsonByteAray The json bytearray
	 * @param side Enum indicating what is the file side
	 * @return The response object indicating error message if request is not valid or save result message if request is valid.
	 */
	protected ResponseObject processStoreRequest(String diffId, byte[] base64JsonByteAray, ResourceSide side){
		ResponseObject response = new ResponseObject();
		if(!isRequestValid(diffId, base64JsonByteAray, response)){
			return response;
		}
		return persistDiff(diffId, base64JsonByteAray, side);
	}

	/**
	 * Checks whether a request is valid or not. If user doesn't provide the diff id or the byte array then it won't be valid.
	 * @param id The diff ID
	 * @param base64JsonByteAray The json bytearray
	 * @param response The responseobject to set the response message if the request is not valid.
	 * @return Boolean indicating if request is valid
	 */
	protected Boolean isRequestValid(String id, byte[] base64JsonByteAray, ResponseObject response) {
		if(StringUtils.isEmpty(id) || StringUtils.isBlank(id)){
			response.setMessage(MessagesConstants.DIFF_ID_CANT_BE_EMPTY);
			return false;
		}
		if(base64JsonByteAray == null || base64JsonByteAray.length == 0){
			response.setMessage(MessagesConstants.MUST_PROVIDE_FILE);
			return false;
		}
		return true;
	}

	/**
	 * Persists a new Diff instance on the database if it doesn't exist or just persists the byte array sent.
	 * @param id The diff ID
	 * @param base64JsonByteAray The json bytearray
	 * @param side Enum indicating what is the file side
	 * @return The ResponseObject with the results of saving the diff instance
	 */
	protected ResponseObject persistDiff(String id, byte[] base64JsonByteArray, ResourceSide side) {
		Diff diff = diffService.getDiff(id);
		
		//if its the first post request, let's create a new Diff instance and persist it.
		if(diff == null){
			diff = new Diff();
		}
		diff.setId(id);
		byte[] decodedBytes = Base64.getDecoder().decode(base64JsonByteArray);
		if(side.equals(ResourceSide.RIGHT)){
			diff.setRight(decodedBytes);
		} else {
			diff.setLeft(decodedBytes);
		}
		diffService.saveDiff(diff);
		ResponseObject response = new ResponseObject();
		response.setMessage(MessagesConstants.FILE_SAVED_SUCCESSFULLY);
		return response;
	}

}
