package com.example.todolistdb;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable (tableName="todo_category")
public class ToDo_Category
{
	@DatabaseField(uniqueCombo=true, foreign=true)
	private ToDo todo_id;
	@DatabaseField(uniqueCombo=true, foreign=true)
	private Category category_id;
	
	public ToDo_Category()
	{
		
	}
	
	public ToDo_Category(ToDo todo_id, Category category_id)
	{
		setTodo_id(todo_id);
		setCategory_id(category_id);
	}

	public ToDo getTodo_id() {
		return todo_id;
	}

	public void setTodo_id(ToDo todo_id) {
		this.todo_id = todo_id;
	}

	public Category getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Category category_id) {
		this.category_id = category_id;
	}
}
