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
	private Priority priority;
	
	public ToDo()
	{
		
	}
	
	public ToDo(long date, String title, String description)
	{
		setDate(date);
		setTitle(title);
		setDescription(description);
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

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public int getId() {
		return id;
	}
	
}
