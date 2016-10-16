package com.waesst.jsondiffer.constants;

/**
 * Messages to be used in the endpoints' responses.
 * 
 * @author Leonardo Nelson
 *
 */
public class MessagesConstants {

	public static final String EQUAL = "Files are equal.";
	public static final String NOT_EQUAL_SIZE = "Files do not have the same size.";
	public static final String SAME_SIZE = "Files have the same size.";
	public static final String DIFF_ID_CANT_BE_EMPTY = "You must provide the diff ID.";
	public static final String DIFF_NOT_FOUND = "Could not find the diff results for the given ID. Upload the files first.";
	public static final String MUST_PROVIDE_LEFT = "You must provide the left file before getting the results.";
	public static final String MUST_PROVIDE_RIGHT = "You must provide the right file before getting the results.";
	public static final String FILE_SAVED_SUCCESSFULLY = "File saved successfully.";
	public static final String MUST_PROVIDE_FILE = "File cannot be missing or empty.";

}
