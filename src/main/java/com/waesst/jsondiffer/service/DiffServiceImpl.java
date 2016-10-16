package com.waesst.jsondiffer.service;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.waesst.jsondiffer.constants.MessagesConstants;
import com.waesst.jsondiffer.dto.ResponseObject;
import com.waesst.jsondiffer.model.Diff;
import com.waesst.jsondiffer.persistence.DiffDao;

/**
 * Service class with operations to handle with Diffs. 
 * 
 * @author Leonardo Nelson
 *
 */
@Component
public class DiffServiceImpl implements DiffService {

	@Autowired
	DiffDao diffDao;

	/**
	 * Performs the diff between two byte arrays.
	 * @param diff The diff instance containing the two byte arrays (left and right).
	 * @return The response object containing the diff results.
	 */
	@Override
	public ResponseObject performDiff(Diff diff) {
		ResponseObject response = new ResponseObject();
		
		String validateMessage = validate(diff);
		
		if(!StringUtils.isEmpty(validateMessage)){
			response.setMessage(validateMessage);
			return response;
		}

		//If equal return that
		if(Arrays.equals(diff.getLeft(), diff.getRight())){
			response.setMessage(MessagesConstants.EQUAL);
		} else if(diff.getLeft().length != diff.getRight().length){
			//If not of equal size just return that
			response.setMessage(MessagesConstants.NOT_EQUAL_SIZE);
		} else if(diff.getLeft().length == diff.getRight().length){
			/*If of same size provide insight in where the diffs are, actual diffs are not needed.
			  -So mainly offsets + length in the data */
			Integer objectsLength = diff.getLeft().length;
			response.setMessage(MessagesConstants.SAME_SIZE);
			response.setLength(objectsLength);
			for(int index = 0; index < objectsLength; index++){
				if(diff.getLeft()[index] != diff.getRight()[index]){
					response.getDiffOffsets().add(index);
				}
			}
		}

		return response;
	}
	
	/**
	 * Validates if the Diff has both files to be compared.
	 * @param diff The diff instance to be validated.
	 * @return Error message if one of the files were not persisted or blank if both are persisted.
	 */
	protected String validate(Diff diff){
		if(diff.getLeft() == null){
			return MessagesConstants.MUST_PROVIDE_LEFT;
		}
		if(diff.getRight() == null){
			return MessagesConstants.MUST_PROVIDE_RIGHT;
		}
		return "";
	}

	@Override
	public Boolean saveDiff(Diff diff) {
		return diffDao.save(diff);
	}

	@Override
	public Diff getDiff(String id) {
		Diff diff = diffDao.load(id);
		return diff;
	}

}
