package com.example.todolistdb;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;


public class ToDo_Category_Manager extends OrmLiteBaseActivity<MyHelper> {
 
	
	
	
	private ArrayAdapter<String> 		arrayAdapter;
	
	private List<Category>				categories;
	private Spinner 					prioritySpinner;
	ListView 							categoryList;
	private	ArrayList<String>			priorities = new ArrayList<String>();
	ArrayList<String> 					categoryArray = new ArrayList<String>();

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_to_do__category__manager);
  
  
  
  categoryList = (ListView) findViewById(R.id.listViewCategory);
  categoryList.setOnItemClickListener(categoryListListener);
  categoryList.setLongClickable(true);

  categoryList.setOnItemLongClickListener(categoryListLongListener);
  categories = getHelper().getAllCategories();
  for(Category c : categories)
	  categoryArray.add(c.getName());
  
  createListView();
  
  
  

 }
 
 
 
 @Override
protected void onResume() {
	 createListView();
	super.onResume();
}



public void createListView() {
	 
	
	  
	  arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, categoryArray);
	  
	  
	  categoryList.setItemsCanFocus(false);
	  categoryList.setAdapter(arrayAdapter);
	 
 }
 
 
 OnItemLongClickListener categoryListLongListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos,
				long id) {

			getHelper().deleteCategory(categoryArray.get(pos));
			categoryArray.remove(pos);
			
			createListView();

			return true;
		}
	};
	
	
	OnItemClickListener categoryListListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
		
		}
	};

 
 @Override
 public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.to_do__category__manager, menu);
		return true;
	}
 
 
 @Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.addCategoryToDo:
			
			final AlertDialog.Builder editalert = new AlertDialog.Builder(this);

			editalert.setTitle(getResources().getString(R.string.categories));


			final EditText input = new EditText(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			        LinearLayout.LayoutParams.FILL_PARENT,
			        LinearLayout.LayoutParams.FILL_PARENT);
			input.setLayoutParams(lp);
			editalert.setView(input);
			
			prioritySpinner= (Spinner) findViewById(R.id.spinner_priority_todo_category);
			  
			  List<Priority> priorityList = getHelper().getAllPriorities();
				
				for(Priority p : priorityList)
					priorities.add(p.getName());
				
			  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);
			  prioritySpinner.setAdapter(adapter);
			  editalert.setView(prioritySpinner);
			  prioritySpinner.performClick();
			  editalert.setPositiveButton(getResources().getString(R.string.addCategory), new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	
			    	if(!input.getText().toString().equals("")){
			    		getHelper().createCategory(input.getText().toString());
			    		categoryArray.add(input.getText().toString());
			    	}
			    	createListView();
			    }
			});
			editalert.show();

			
			
			
			

			return true;

		default:

			return super.onOptionsItemSelected(item);
		}
	}

}