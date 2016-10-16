package com.waesst.jsondiffer.service;

import com.waesst.jsondiffer.dto.ResponseObject;
import com.waesst.jsondiffer.model.Diff;

public interface DiffService {

	public ResponseObject performDiff(Diff diff);
	
	public Boolean saveDiff(Diff diff);
	
	public Diff getDiff(String id);
}
