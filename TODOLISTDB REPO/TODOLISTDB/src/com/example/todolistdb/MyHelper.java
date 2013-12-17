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
	private static final String DATABASE_NAME 			= "todo_database.db";
	private static final int	DATABASE_VERSION 		= 1;
	private static final String DATABASE_ACTION_TAG 	= "Databaseaction";
	private static final String CANTDELETENONEXISTENT	= "can't delete non existent item in database";
	private static final String TOMANYARGUMENTS			= "to many arguments as result from query";
	
	
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
		Log.v(DATABASE_ACTION_TAG, "created ToDo: " + toDo.toString());
	}
	
	public void createPriority(String name)
	{
		if(priorityRuntimeDao == null)
			getPriorityDao();
		Priority prio = new Priority(name);
		priorityRuntimeDao.create(prio);
		Log.v(DATABASE_ACTION_TAG, "created Priority id: " + prio.getId() + " name: " + prio.getName());
	}
	
	public void createCategory(String name)
	{
		if(categoryRuntimeDao == null)
			getCategoryDao();
		Category cat = new Category(name);
		categoryRuntimeDao.create(cat);
		Log.v(DATABASE_ACTION_TAG, "created Category id: " + cat.getId() + " name: " + cat.getName());
	}
	
	public void createToDo_Category(ToDo toDo, Category cat)
	{
		if(toDoCategoryRuntimeDao == null)
			getToDo_CategoryDao();
		ToDo_Category tdc = new ToDo_Category(toDo, cat);
		toDoCategoryRuntimeDao.create(tdc);
		Log.v(DATABASE_ACTION_TAG, "created ToDo_Category todo_id: " + tdc.getTodo_id() + " category_id: " + tdc.getCategory_id());
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
				tdQb.where().eq(ToDo.TODO_ID_FIELD, t.getId());
				QueryBuilder<Priority, Integer> pQb = priorityRuntimeDao.queryBuilder();
				QueryBuilder<ToDo_Category, Integer> tdcQb = toDoCategoryRuntimeDao.queryBuilder();
				tdcQb.join(tdQb);
				QueryBuilder<Category, Integer> catQb = categoryRuntimeDao.queryBuilder();
				t.setCategories(catQb.join(tdcQb).query());
				tdQb = toDoRuntimeDao.queryBuilder();
				pQb.where().eq(ToDo.TODO_ID_FIELD, t.getPriority().getId());
				List<Priority> prios = pQb.join(tdQb).query();
				if(!prios.isEmpty())
					t.setPriority(prios.get(0));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return todos;
	}
	
	public void deleteCategory(String name){
		if(categoryRuntimeDao == null)
			getCategoryDao();
		if(toDoCategoryRuntimeDao == null)
			getToDo_CategoryDao();
		QueryBuilder<Category, Integer> catQb = categoryRuntimeDao.queryBuilder();
		QueryBuilder<ToDo_Category, Integer> tdcqb = toDoCategoryRuntimeDao.queryBuilder();
		try {
			catQb.where().eq(Category.CATEGORY_NAME_FIELD, name);
			List<Category> categories = catQb.query();
			if(categories.isEmpty()) throw new UserIQToLowException(CANTDELETENONEXISTENT);
			if(categories.size() != 1) throw new UserIQToLowException(TOMANYARGUMENTS);
			Category category = categories.get(0);
			tdcqb.where().eq(ToDo_Category.CATEGORY_ID_FIELD, category.getId());
			List<ToDo_Category> tdcs = tdcqb.query();
			for(ToDo_Category t : tdcs)
			{
				toDoCategoryRuntimeDao.delete(t);
				Log.v(DATABASE_ACTION_TAG, "ToDo_Category deleted: Todo: " + t.getTodo_id() + " Category: " + t.getCategory_id());
			}
			categoryRuntimeDao.delete(category);
			Log.v(DATABASE_ACTION_TAG, "category deleted: " + category.getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void deletePriority(String name){
		if(priorityRuntimeDao == null)
			getPriorityDao();
		QueryBuilder<Priority, Integer> catQb = priorityRuntimeDao.queryBuilder();
		try {
			catQb.where().eq(Priority.PRIORITY_NAME_FIELD, name);
			List<Priority> priorities = catQb.query();
			if(priorities.isEmpty()) throw new UserIQToLowException(CANTDELETENONEXISTENT);
			if(priorities.size() != 1) throw new UserIQToLowException(TOMANYARGUMENTS);
			Priority priority = priorities.get(0);
			priorityRuntimeDao.delete(priority);
			Log.v(DATABASE_ACTION_TAG, "priority deleted: " + priority.getName());
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteToDo_Category(int toDoId,int catId){
		if(toDoCategoryRuntimeDao == null)
			getToDo_CategoryDao();
		QueryBuilder<ToDo_Category, Integer> qb = toDoCategoryRuntimeDao.queryBuilder();
		try {
			qb.where().eq(ToDo_Category.TODO_ID_FIELD, toDoId);
			qb.where().eq(ToDo_Category.CATEGORY_ID_FIELD, catId);
			List<ToDo_Category> tdcs = qb.query();
			if(tdcs.isEmpty()) throw new UserIQToLowException(CANTDELETENONEXISTENT);
			if(tdcs.size() != 1) throw new UserIQToLowException(TOMANYARGUMENTS);
			ToDo_Category tdc = tdcs.get(0);
			toDoCategoryRuntimeDao.delete(tdc);
			Log.v(DATABASE_ACTION_TAG, "ToDo_Category deleted: Todo: " + tdc.getTodo_id() + " Category: " + tdc.getCategory_id());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteToDo(int id){
		if(toDoRuntimeDao == null)
			getToDoDao();
		if(toDoCategoryRuntimeDao == null)
			getToDo_CategoryDao();
		QueryBuilder<ToDo, Integer> qb = toDoRuntimeDao.queryBuilder();
		QueryBuilder<ToDo_Category, Integer> tdcqb = toDoCategoryRuntimeDao.queryBuilder();
		try {
			qb.where().eq(ToDo.TODO_ID_FIELD, id);
			List<ToDo> tds = qb.query();
			if(tds.isEmpty()) throw new UserIQToLowException(CANTDELETENONEXISTENT);
			if(tds.size() != 1) throw new UserIQToLowException(TOMANYARGUMENTS);
			tdcqb.where().eq(ToDo_Category.TODO_ID_FIELD, id);
			List<ToDo_Category> tdcs = tdcqb.query();
			for(ToDo_Category t : tdcs)
			{
				toDoCategoryRuntimeDao.delete(t);
				Log.v(DATABASE_ACTION_TAG, "ToDo_Category deleted: Todo: " + t.getTodo_id() + " Category: " + t.getCategory_id());
			}
			ToDo td = tds.get(0);
			toDoRuntimeDao.delete(td);
			Log.v(DATABASE_ACTION_TAG, "ToDo deleted: " + td.getId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void close()
	{
		super.close();
		toDoRuntimeDao = null;
	}
}
