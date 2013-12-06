package com.example.todolistdb;



import com.example.todolist.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class Setting_main extends PreferenceActivity {
	
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState);        
        addPreferencesFromResource(R.xml.preferences); 
        
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        EditTextPreference ep = (EditTextPreference) findPreference("text_size");
        
        savePreferences("text_size",ep.getText() );
        StringBuilder builder = new StringBuilder();
        builder.append("\n" + sharedPrefs.getString("text_size", "NULL"));
        
        
        
    }
    
   

    
    private void savePreferences(String key, String value) {
    	
    	
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	Editor editor = sharedPreferences.edit();
    	editor.putString(key, value);
    	editor.commit();

    }
    
}