package com.example.todolistdb.exception;

@SuppressWarnings("serial")
public class UserIQToLowException extends RuntimeException
{
	public UserIQToLowException(String message)
	{
		super(message);
	}
}
