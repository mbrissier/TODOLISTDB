package com.example.todolistdb;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="priority")
public class Priority implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6923180940189458781L;


	public static final String PRIORITY_NAME_FIELD 	= "name";
	public static final String PRIORITY_ID_FIELD 	= "id";
	
	
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
