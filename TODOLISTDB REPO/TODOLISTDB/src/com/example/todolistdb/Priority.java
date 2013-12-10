package com.example.todolistdb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="priority")
public class Priority
{
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField
	private String name;
	
	public Priority()
	{
		
	}
	
	public Priority(String name)
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
	
	public String toString()
	{
		return name;
	}
}
