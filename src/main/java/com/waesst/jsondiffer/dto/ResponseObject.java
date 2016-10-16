package com.waesst.jsondiffer.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The response wrapper class. I have created this class in order to wrap all of the Diff details, including offsets and the files lenght.
 * I found it would be more handful after converting it to JSON. Cleaner and organized response.
 * 
 * @author Leonardo Nelson
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class ResponseObject {

	String message;
	Set<Integer> diffOffsets;
	Integer length;

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Set<Integer> getDiffOffsets() {
		if(diffOffsets==null){
			diffOffsets = new HashSet<>();
		}
		return diffOffsets;
	}
	public void setDiffOffsets(Set<Integer> diffOffsets) {
		this.diffOffsets = diffOffsets;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	
}
