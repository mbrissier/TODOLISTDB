package com.example.todolistdb;

import java.io.Serializable;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="todo")
public class ToDo implements Serializable
{
	public static final String TODO_ID_FIELD = "id";
	
	private static final long serialVersionUID = 90311570164858987L;
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
	
	private List<Category> categories;


	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

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
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder("------------------------------------\n" +
											"title: " + title + "\n" +
											"description: " + description + "\n" +
											"date: " + date + "\n");
		if(categories != null)
			sb.append("categories: " + categories + "\n");
		else
			sb.append("categories: " + "-" + "\n");
		if(priority != null)
			sb.append("priority: " + priority + "\n");
		else
			sb.append("priority: " + "-" + "\n");
		return 	sb.append("------------------------------------\n").toString();
	}
	
}
