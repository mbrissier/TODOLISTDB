package com.example.database;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable (tableName="todo_category")
public class ToDo_Category
{
	@DatabaseField(foreign=true)
	private int todo_id;
	@DatabaseField(foreign=true)
	private int category_id;
	
	public ToDo_Category()
	{
		
	}
	
	public ToDo_Category(int todo_id, int category_id)
	{
		setTodo_id(todo_id);
		setCategory_id(category_id);
	}

	public int getTodo_id() {
		return todo_id;
	}

	public void setTodo_id(int todo_id) {
		this.todo_id = todo_id;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
}
