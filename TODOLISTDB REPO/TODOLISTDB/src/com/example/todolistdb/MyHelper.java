package com.example.todolistdb;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.todolistdb.exception.UserIQToLowException;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class MyHelper extends OrmLiteSqliteOpenHelper
{
	private static final String DATABASE_NAME 		= "todo_database.db";
	private static final int	DATABASE_VERSION 	= 1;
	
	
	//dao's for every table
	private Dao<ToDo, Integer> 							toDoDao 				= null;
	private RuntimeExceptionDao<ToDo, Integer> 			toDoRuntimeDao	 		= null;
	private RuntimeExceptionDao<ToDo_Category, Integer>	toDoCategoryRuntimeDao	= null;
	private RuntimeExceptionDao<Category, Integer> 		categoryRuntimeDao		= null;
	private RuntimeExceptionDao<Priority, Integer> 		priorityRuntimeDao 		= null;
	
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
			TableUtils.createTable(connectionSource, Category.class);
			TableUtils.createTable(connectionSource, Priority.class);
			TableUtils.createTable(connectionSource, ToDo_Category.class);
		} catch (SQLException e)
		{
			Log.e(MyHelper.class.getName(), "Can't create database", e);
			throw new UserIQToLowException(e.getMessage());
		}

		// here we try inserting data in the on-create as a test
		RuntimeExceptionDao<ToDo, Integer> dao = getToDoDao();
		long millis = System.currentTimeMillis();
		// create an entry in the onCreate
		ToDo toDo = new ToDo(millis, "test", null);
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
			throw new UserIQToLowException(e.getMessage());
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
		if (toDoRuntimeDao == null)
			toDoRuntimeDao = getRuntimeExceptionDao(ToDo.class);
		return toDoRuntimeDao;
	}
	
	public RuntimeExceptionDao<ToDo_Category, Integer> getToDo_CategoryDao()
	{
		if (toDoCategoryRuntimeDao == null)
			toDoCategoryRuntimeDao = getRuntimeExceptionDao(ToDo_Category.class);
		return toDoCategoryRuntimeDao;
	}
	
	public RuntimeExceptionDao<Category, Integer> getCategoryDao()
	{
		if (categoryRuntimeDao == null)
			categoryRuntimeDao = getRuntimeExceptionDao(Category.class);
		return categoryRuntimeDao;
	}
	
	public RuntimeExceptionDao<Priority, Integer> getPriorityDao()
	{
		if (priorityRuntimeDao == null)
			priorityRuntimeDao = getRuntimeExceptionDao(Priority.class);
		return priorityRuntimeDao;
	}

	public void createToDo(String title, String description, Priority prio, long date)
	{
		if(toDoRuntimeDao == null)
			getToDoDao();
		ToDo toDo = new ToDo(date, title, description);
		toDo.setPriority(prio);
		toDoRuntimeDao.create(toDo);
	}
	
	public void createPriority(String name)
	{
		if(priorityRuntimeDao == null)
			getPriorityDao();
		Priority prio = new Priority(name);
		priorityRuntimeDao.create(prio);
	}
	
	public void createCategory(String name)
	{
		if(categoryRuntimeDao == null)
			getCategoryDao();
		Category cat = new Category(name);
		categoryRuntimeDao.create(cat);
	}
	
	public void createToDo_Category(ToDo toDo, Category cat)
	{
		if(toDoCategoryRuntimeDao == null)
			getToDo_CategoryDao();
		ToDo_Category tdc = new ToDo_Category(toDo, cat);
		toDoCategoryRuntimeDao.create(tdc);
	}
	
	public List<Category> getAllCategories()
	{
		if(categoryRuntimeDao == null)
			getCategoryDao();
		return categoryRuntimeDao.queryForAll();
	}
	
	public List<Priority> getAllPriorities()
	{
		if(priorityRuntimeDao == null)
			getPriorityDao();
		return priorityRuntimeDao.queryForAll();
	}
	
	public List<ToDo_Category> getAllToDo_Categories()
	{
		if(toDoCategoryRuntimeDao == null)
			getToDo_CategoryDao();
		return toDoCategoryRuntimeDao.queryForAll();
	}
	
	public List<ToDo> getAllToDos()
	{
		if(toDoRuntimeDao == null)
			getToDoDao();
		if(toDoCategoryRuntimeDao == null)
			getToDo_CategoryDao();
		if(categoryRuntimeDao == null)
			getCategoryDao();
		if(priorityRuntimeDao == null)
			getPriorityDao();
		List<ToDo> todos = toDoRuntimeDao.queryForAll();
		for(ToDo t : todos)
		{
			try
			{
				QueryBuilder<ToDo, Integer> tdQb = toDoRuntimeDao.queryBuilder();
				tdQb.where().eq("id", t.getId());
				QueryBuilder<Priority, Integer> pQb = priorityRuntimeDao.queryBuilder();
				QueryBuilder<ToDo_Category, Integer> tdcQb = toDoCategoryRuntimeDao.queryBuilder();
				//tdcQb.where().eq("todo_id", t.getId());
				tdcQb.join(tdQb);
				QueryBuilder<Category, Integer> catQb = categoryRuntimeDao.queryBuilder();
				t.setCategories(catQb.join(tdcQb).query());
				//t.setPriority(pQb.join(tdQb).query().get(0));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return todos;
	}
	
	@Override
	public void close()
	{
		super.close();
		toDoRuntimeDao = null;
	}
}
