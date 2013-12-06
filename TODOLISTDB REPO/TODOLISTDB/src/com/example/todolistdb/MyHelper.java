package com.example.todolistdb;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class MyHelper extends OrmLiteSqliteOpenHelper
{
	private static final String DATABASE_NAME 		= "todo_database.db";
	private static final int	DATABASE_VERSION 	= 1;
	
	private Dao<ToDo, Integer> 							toDoDao 			= null;
	private RuntimeExceptionDao<ToDo, Integer> 			toDoRuntimeDao	 	= null;
	
	public MyHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			Log.i(MyHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, ToDo.class);
		} catch (SQLException e)
		{
			Log.e(MyHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

		// here we try inserting data in the on-create as a test
		RuntimeExceptionDao<ToDo, Integer> dao = getToDoDao();
		long millis = System.currentTimeMillis();
		// create an entry in the onCreate
		ToDo toDo = new ToDo(millis, "test", 2);
		dao.create(toDo);
		Log.i(MyHelper.class.getName(), "created new entries in onCreate: " + millis);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try
		{
			Log.i(MyHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, ToDo.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e)
		{
			Log.e(MyHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
	
	public Dao<ToDo, Integer> getDao() throws SQLException
	{
		if (toDoDao == null)
			toDoDao = getDao(ToDo.class);
		return toDoDao;
	}
	
	public RuntimeExceptionDao<ToDo, Integer> getToDoDao()
	{
		if (toDoRuntimeDao == null) {
			toDoRuntimeDao = getRuntimeExceptionDao(ToDo.class);
		}
		return toDoRuntimeDao;
	}

	@Override
	public void close()
	{
		super.close();
		toDoRuntimeDao = null;
	}
}
