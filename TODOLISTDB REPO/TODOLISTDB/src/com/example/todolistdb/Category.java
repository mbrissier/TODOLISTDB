package com.example.todolistdb;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="category")
public class Category
{
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField
	private String name;

	public Category()
	{
		
	}
	
	public Category(String name)
	{
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
}
