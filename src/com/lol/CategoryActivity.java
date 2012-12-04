package com.lol;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;


public class CategoryActivity extends SpeechActivity implements OnCheckedChangeListener{
    private RadioGroup mode1;
    private ImageButton homeButton = null;

    @Override
    protected void onLoaded()
    {
    	super.onLoaded();
        this.speak(
        		"Select one of the following categories," +
        		"Anti jokes," +
        		"Chuck Norris Jokes,"
        		);   
    }
    
    /* Called when the activity is first created */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);
        
        homeButton = (ImageButton) findViewById(R.id.btn_home);
       
        mode1 = (RadioGroup)findViewById(R.id.jokecategory);
        mode1.setOnCheckedChangeListener(this);        
        
        homeButton.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {	           	
    			Intent goto_home = new Intent(CategoryActivity.this,MainActivity.class);
    			CategoryActivity.this.finish();
    			startActivity(goto_home);
    			
    		}
    	});
        
    }
    
    @Override
    protected void onSpeechToTextEvent(String recognizedWord)
    {
    	Intent category_selected = new Intent(CategoryActivity.this,JokeActivity.class);
    	
    	Log.v("choice", recognizedWord);
    	if (recognizedWord.contains("anti")) {
    		category_selected.putExtra("file", "jokes");
    		startActivity(category_selected);
		}
    	else if (recognizedWord.contains("chuck")) {
    		category_selected.putExtra("file", "jokes1");
    		startActivity(category_selected);
		}
    	else
    	{
    		super.sttDone = false;
    	}
    	
    	
    	super.onSpeechToTextEvent(recognizedWord);
    }
 
    //@Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
         
    	Intent category_selected = new Intent(CategoryActivity.this,JokeActivity.class); 
    	switch(checkedId)
    	{
    		case R.id.AntiJokes:
    			category_selected.putExtra("file", "jokes");
    			break;
    		case R.id.ChuckNorrisJokes:
        		category_selected.putExtra("file", "jokes1");
        		break;  		
    	}
        startActivity(category_selected);       
              
    }
    
   
   
 }