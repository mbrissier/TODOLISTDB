package com.example.todolistdb;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class ToDoContentProvider extends ContentProvider {
	
	public static final String SCHEME		= "content://";
	public static final String AUTHORITY 	= "de.htwds.mada.todo";
	
	private static final UriMatcher matcher = UriMatcher(UriMatcher.NO_MATCH);
	private static final int		TODO	= 100;
	private static final int		TODO_ID	= 101;
	private static final int		PRIO	= 102;
	private static final int		PRIO_ID	= 103;
	
	static {
		matcher.addURI(AUTHORITY, ContractToDo.PATH, TODO);
		matcher.addURI(AUTHORITY, ContractToDo.PATH + "/#", TODO_ID);
		matcher.addURI(AUTHORITY, ContractPriority.PATH, PRIO);
		matcher.addURI(AUTHORITY, ContractPriority.PATH + "/#", PRIO_ID);
	}
	
	public static final class ContractToDo implements BaseColumns {
		//defining URI
		public static final String 	PATH 		= "todo";
		public static final Uri 	CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/" + PATH);
		//column names
		public static final String	TITLE 		= ToDo.TODO_TITLE_FIELD;
		public static final String	DATE 		= ToDo.TODO_DATE_FIELD;
		public static final String	DESCRIPTION	= ToDo.TODO_DESCRIPTION_FIELD;
		public static final String	ID	 		= ToDo.TODO_ID_FIELD;
		public static final String	PRIORITY	= ToDo.TODO_PRIORITY_FIELD;
	}
	
	public static final class ContractPriority implements BaseColumns {
		//defining URI
		public static final String 	PATH 		= "priority";
		public static final Uri 	CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/" + PATH);
		//column names
		public static final String	NAME 		= Priority.PRIORITY_NAME_FIELD;
		public static final String	ID	 		= Priority.PRIORITY_ID_FIELD;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static UriMatcher UriMatcher(int noMatch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
