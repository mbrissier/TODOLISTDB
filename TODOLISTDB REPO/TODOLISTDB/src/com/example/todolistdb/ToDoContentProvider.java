package com.example.todolistdb;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class ToDoContentProvider extends ContentProvider {
	
	//defining general URI-elements
	public static final String SCHEME		= "content://";
	public static final String AUTHORITY 	= "de.htwds.mada.todo";
	
	//defining constants for the matcher and the logging
	private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int		TODO	= 100;
	private static final int		TODO_ID	= 101;
	private static final int		PRIO	= 102;
	private static final int		PRIO_ID	= 103;
	private static final String		LOG		= "Content Provider";
	
	private	MyHelper 	helper;
	
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
		public static final String	DATE 		= "datetime";
		public static final String	DESCRIPTION	= ToDo.TODO_DESCRIPTION_FIELD;
		public static final String	ID	 		= "_id";
		public static final String	PRIORITY	= "prioriy_id";
		//MIME types
		public static final String CONTENT_TYPE 		= "vnd.android.cursor.dir/vnd.de.htwds.mada.todo";
		public static final String CONTENT_ITEM_TYPE 	= "vnd.android.cursor.item/vnd.de.htwds.mada.todo";
	}
	
	public static final class ContractPriority implements BaseColumns {
		//defining URI
		public static final String 	PATH 		= "priority";
		public static final Uri 	CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/" + PATH);
		//column names
		public static final String	NAME 		= Priority.PRIORITY_NAME_FIELD;
		public static final String	ID	 		= "_id";
		//MIME types
		public static final String CONTENT_TYPE 		= "vnd.android.cursor.dir/vnd.de.htwds.mada.priority";
		public static final String CONTENT_ITEM_TYPE 	= "vnd.android.cursor.item/vnd.de.htwds.mada.priority";
	}
	
	

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public String getType(Uri uri) {
		int code = matcher.match(uri);
		
		switch(code) {
			case TODO:		return ContractToDo.CONTENT_TYPE;
			case TODO_ID:	return ContractToDo.CONTENT_ITEM_TYPE;
			case PRIO:		return ContractPriority.CONTENT_TYPE;
			case PRIO_ID:	return ContractPriority.CONTENT_ITEM_TYPE;
			default:		return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int code = matcher.match(uri);
		
		switch(code) {
			case TODO:
					long id = insertTodo(values);
					return ContentUris.appendId(uri.buildUpon(), id).build();
			default:
					return null;
		}			
	}

	private long insertTodo(ContentValues values) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onCreate() {
		//initializing helper
		helper = new MyHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		int code = matcher.match(uri);
		
		//deciding which query-method to call
		switch(code) {
			case TODO:
				return queryAllTodos(projection, selection, selectionArgs, sortOrder);
			case TODO_ID:
				long todoId = ContentUris.parseId(uri);
				return queryTodo(todoId, projection, selection, selectionArgs, sortOrder);
			case PRIO:
				return queryAllPriorities(projection, selection, selectionArgs, sortOrder);
			case PRIO_ID:
				long prioId = ContentUris.parseId(uri);
				return queryPriority(prioId, projection, selection, selectionArgs, sortOrder);
			default:
				return null;
		}
		
	}

	//query for single priority
	private Cursor queryPriority(long prioId, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		Cursor 			cursor 	= null;
		SQLiteDatabase 	db 		= null;
		
		try {
			db = helper.getReadableDatabase();
			String sel = ToDo.TODO_ID_FIELD + "=" + prioId;
			if (! selection.isEmpty()) {
				sel += " AND " + selection;
			}
			cursor = db.query(Priority.TABLE_NAME, projection, sel, selectionArgs, null, null, sortOrder);
		} catch (Exception e) {
			Log.wtf(LOG, "error querying priority");
		}
		return cursor;
	}

	//query for all priorities
	private Cursor queryAllPriorities(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor 			cursor 	= null;
		SQLiteDatabase 	db 		= null;
		
		try {
			db = helper.getReadableDatabase();
			cursor = db.query(Priority.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
		} catch (Exception e) {
			Log.wtf(LOG, "error querying all priorities");
		}
		return cursor;
	}

	//query for todo
	private Cursor queryTodo(long todoId, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		Cursor 			cursor 	= null;
		SQLiteDatabase 	db 		= null;
		
		try {
			db = helper.getReadableDatabase();
			String sel = ToDo.TODO_ID_FIELD + "=" + todoId;
			if (! selection.isEmpty()) {
				sel += " AND " + selection;
			}
			cursor = db.query(ToDo.TABLE_NAME, projection, sel, selectionArgs, null, null, sortOrder);
		} catch (Exception e) {
			Log.wtf(LOG, "error querying todo");
		}
		return cursor;
	}

	//query for all todos
	private Cursor queryAllTodos(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor 			cursor 	= null;
		SQLiteDatabase 	db 		= null;
		
		try {
			db = helper.getReadableDatabase();
			cursor = db.query(ToDo.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
		} catch (Exception e) {
			Log.wtf(LOG, "error querying all todos");
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
