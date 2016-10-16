package com.waesst.jsondiffer.persistence;

import com.waesst.jsondiffer.model.Diff;


public interface DiffDao {
	public Boolean save(final Diff diff);
    public Diff load(final String id);
    
}
	