package com.example.todolistdb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="todo")
public class ToDo
{
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField
	private long date;
	@DatabaseField
	private String title;
	@DatabaseField(canBeNull=true)
	private String description;
	@DatabaseField(foreign=true)
	private int priority_id;
	
	public ToDo()
	{
		
	}
	
	public ToDo(long date, String title, int priority_id, String description)
	{
		setDate(date);
		setTitle(title);
		setPriority_id(priority_id);
		setDescription(description);
	}
	
	public ToDo(long date, String title, int priority_id)
	{
		this(date, title, priority_id, null);
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority_id() {
		return priority_id;
	}

	public void setPriority_id(int priority_id) {
		this.priority_id = priority_id;
	}

	public int getId() {
		return id;
	}
	
}
