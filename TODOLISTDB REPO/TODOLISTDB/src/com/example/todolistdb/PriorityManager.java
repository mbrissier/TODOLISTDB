package com.example.todolistdb;
import java.util.ArrayList;




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


public class PriorityManager extends Activity {
 
	ListView priorityList;
	ArrayList<String> priorityArray = new ArrayList<String>();
	
	private ArrayAdapter<String> arrayAdapter;

	

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_priority_manager);

  
  priorityList = (ListView) findViewById(R.id.listViewPriority);
  priorityList.setOnItemClickListener(priorityListListener);
  priorityList.setLongClickable(true);

  priorityList.setOnItemLongClickListener(priorityListLongListener);
  createListView();
  
  
  

 }
 
 public void createListView() {
	 
	
	  
	  arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, priorityArray);
	  
	  
	  priorityList.setItemsCanFocus(false);
	  priorityList.setAdapter(arrayAdapter);
	 
 }
 
 
 OnItemLongClickListener priorityListLongListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos,
				long id) {

			priorityArray.remove(pos);
			createListView();

			return true;
		}
	};
	
	
	OnItemClickListener priorityListListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
		
		}
	};

 
 @Override
 public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.priority_manager, menu);
		return true;
	}
 
 
 @Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.addPriority:
			
			final AlertDialog.Builder editalert = new AlertDialog.Builder(this);

			editalert.setTitle(getResources().getString(R.string.priorities));


			final EditText input = new EditText(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			        LinearLayout.LayoutParams.FILL_PARENT,
			        LinearLayout.LayoutParams.FILL_PARENT);
			input.setLayoutParams(lp);
			editalert.setView(input);
			
			editalert.setPositiveButton(getResources().getString(R.string.addPriorities), new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	
			    	if(!input.getText().toString().equals(""))
			    	priorityArray.add(input.getText().toString());
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
