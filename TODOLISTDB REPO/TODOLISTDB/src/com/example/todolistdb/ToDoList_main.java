package com.example.todolistdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.*;

public class ToDoList_main extends OrmLiteBaseActivity<MyHelper> {

	public static final String DATA_TITEL 	= "titel";
	public static final String DATA_COMMENT = "beschreibung";
	public static final String DATA_SPINNER = "spinner";
	public static final String DATA_DELETE 	= "delete";
	public static final String LOG_TAG 		= "ToDoList_main";

	// Wird beim start einer Subactivity mitgegeben, um Antworten interpretieren
	// zu koennen
	public static final int REQUEST_CODE_DETAIL_MAIN 	= 30;
	public static final int ADD_REQUEST_CODE 			= 10;
	

	ArrayList<String> 	values 			= new ArrayList<String>();
	ArrayList<String> 	comment 		= new ArrayList<String>();
	ArrayList<Integer> 	spinnerposition = new ArrayList<Integer>();

	// Um beim start der activity dem benutzer ein beispiel zu zeigen
	String 	beispiel_titel 			= "Neuer Eintrag";
	String 	beispiel_beschreibung 	= "";
	int 	beispiel_spinner 		= 0;

	// Speichern die positionen der titel-, beschreibungs- und spinnerwerte
	int spinnerPosition;
	int positionString;
	int commentPostionString;

	// ListView und Adapter
	private ListView todoListView;

	private ArrayAdapter<String> arrayAdapter;
	// private MadaArrayAdapter<String> arrayAdaptersize;
	private AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_list_main);
		
		if(savedInstanceState != null && savedInstanceState.containsKey("values")){
		    values = savedInstanceState.getStringArrayList("values");
		    comment = savedInstanceState.getStringArrayList("beschreibung");
		    spinnerposition = savedInstanceState.getIntegerArrayList("spinner");
		}else{
		    values = new ArrayList <String>();
		    values.add(beispiel_titel);
			comment.add(beispiel_beschreibung);
			spinnerposition.add(beispiel_spinner);
		}

		// List View und Adapter Initialisierung
		todoListView = (ListView) findViewById(R.id.listView_todoList);

		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		todoListView.setAdapter(arrayAdapter);
		todoListView.setOnItemClickListener(todoListViewListener);
		todoListView.setLongClickable(true);

		todoListView.setOnItemLongClickListener(todoListViewLongListener);

		// die Beispiele werden hinzugefuegt
		

		// loadSavedPreferences();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Aufgabe wirklich loeschen?")
				.setCancelable(false)
				.setPositiveButton("JA", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						deleteItem();
					}
				})
				.setNegativeButton("Nein",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		alert = builder.create();
		
		
		setUpDatabase();
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        //Save the calendar instance in case the user changed it
        outState.putStringArrayList("values", values);
        outState.putStringArrayList("beschreibung", comment);
        outState.putIntegerArrayList("spinner", spinnerposition);
    }


	
	/**
	 * delete methode wird mit dialog ausgefuehrt, wenn lange auf ein item geklickt wird
	 */
	OnItemLongClickListener todoListViewLongListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos,
				long id) {

			positionString = pos;

			commentPostionString = pos;

			spinnerPosition = pos;

			alert.show();
			Log.v("long clicked", "pos: " + pos);

			return true;
		}
	};

	/**
	 * Wenn ein Item in der ListView ausgewaehlt wird
	 */
	OnItemClickListener todoListViewListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			// Positionen von titel und beschreibung
			String s = values.get(position);
			positionString = position;

			String c = comment.get(position);
			commentPostionString = position;

			int a = spinnerposition.get(position);
			spinnerPosition = position;

			// Detail_main wird mit den Daten des Items aufgerufen
			Intent intent = new Intent(ToDoList_main.this, Detail_main.class);
			intent.putExtra(DATA_TITEL, s);
			intent.putExtra(DATA_COMMENT, c);
			intent.putExtra(DATA_SPINNER, a);
			intent.putExtra(DATA_DELETE, false);
			startActivityForResult(intent, REQUEST_CODE_DETAIL_MAIN);
		}
	};

	private void deleteItem() {

		if (!values.isEmpty()) {

			values.remove(positionString);
			comment.remove(commentPostionString);
			spinnerposition.remove(spinnerPosition);

			arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, values);
			todoListView.setAdapter(arrayAdapter);

		}
	}

	/**
	 * Menu wird aufgebaut
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.to_do_list_main, menu);
		return true;
	}

	/**
	 * Wenn ein Menu-Item ausgewaehlt wird
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.add:

			// Start der Detail_main mit Rueckgabewert: Strings fuer task item
			startDetailActivity();

			return true;

		case R.id.setting:
			// Start der SettingsActivity mit Rueckgabewert: dimens fuer
			// Schriftgroesse
			startSettingsActivity();

			return true;

		default:

			return super.onOptionsItemSelected(item);
		}
	}

	private void startDetailActivity() {

		Intent intent = new Intent(this, Detail_main.class);
		// Start der Detail_main
		startActivityForResult(intent, ADD_REQUEST_CODE);

	}

	private void startSettingsActivity() {

		Intent intent = new Intent(this, Setting_main.class);
		// Start der Setting_main, der rueckgabe wert wird ueber die shared
		// preferences ausgelesen
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		// Wenn die Detail_main ihre Eingabe fuer das Erstellen einer Task
		// zurueck gibt
		case ADD_REQUEST_CODE:
			
			Log.v("ADD:", "REQUEST");

			// RESULT_OK konnte von Detail_main erfolgreich zurueckgegeben
			// werden oder nicht

			if (resultCode == RESULT_OK) {
				values.add(data.getExtras().getString("edit_titel"));
				comment.add(data.getExtras().getString("edit_beschreibung"));
				spinnerposition.add(data.getExtras().getInt("spinner"));

				arrayAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, values);
				todoListView.setAdapter(arrayAdapter);

			}

			break;

		// Wenn in Detail_main etwas veraendert wurde, ein Item in ListView
		// wurde ausgewaehlt und hier wird
		// die Antwort von Detail_main verarbeitet.
		case REQUEST_CODE_DETAIL_MAIN:
			
			Log.v("Selected Item:", "REQUEST");
			boolean delete = data.getBooleanExtra("delete_flag", false);
			
			if (resultCode == RESULT_OK && !delete) {

				values.set(positionString,
						(data.getExtras().getString("edit_titel")));
				comment.set(commentPostionString,
						(data.getExtras().getString("edit_beschreibung")));
				spinnerposition.set(spinnerPosition,
						(data.getExtras().getInt("spinner")));

				arrayAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, values);
				todoListView.setAdapter(arrayAdapter);

			} else if (resultCode == RESULT_OK && delete) {
				
				if(!values.isEmpty()) 
				deleteItem();
							
				
			}
			
			

			break;

		default:

			Toast.makeText(ToDoList_main.this, "Could not transfer data",
					Toast.LENGTH_LONG).show();

			break;
		}
	}

	private void setUpDatabase()
	{
		getHelper().createCategory("A");
		getHelper().createCategory("B");
		getHelper().createCategory("C");
		List<Category> cats = getHelper().getAllCategories();
		getHelper().createPriority("high");
		getHelper().createPriority("medium");
		getHelper().createPriority("low");
		List<Priority> prios = getHelper().getAllPriorities();
		getHelper().createToDo("Arzt", "Zahnarzt", prios.get(2), 10000);
		getHelper().createToDo("Snakes", "California Mountainsnakes", prios.get(0), 20000);
		getHelper().createToDo("Uni", "Vorlesung", prios.get(1), 30000);
		List<ToDo> todos = getHelper().getAllToDos();
		getHelper().createToDo_Category(todos.get(0), cats.get(0));
		getHelper().createToDo_Category(todos.get(1), cats.get(0));
		getHelper().createToDo_Category(todos.get(1), cats.get(1));
		getHelper().createToDo_Category(todos.get(1), cats.get(2));
		getHelper().createToDo_Category(todos.get(2), cats.get(2));
		todos = getHelper().getAllToDos();
		System.out.println(todos);
	}
}
