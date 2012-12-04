package com.lol;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;


public class MainActivity extends SpeechActivity implements OnCheckedChangeListener{
    private RadioGroup mode;
    private String option_value;

    /* Called when the activity is first created */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        mode = (RadioGroup)findViewById(R.id.playjokes);
        mode.setOnCheckedChangeListener(this);
        
    }
    
    @Override
    protected void onSpeechToTextEvent(String recognizedWord)
    {
    	if (recognizedWord.equals("random")) {
    		Intent optionselected_rand = new Intent(MainActivity.this,JokeActivity.class);
            startActivity(optionselected_rand);
		}
    	else if (recognizedWord.equals("category")) {
    		Intent optionselected_cat = new Intent(MainActivity.this,CategoryActivity.class);
            startActivity(optionselected_cat);
		}
    	
    	super.onSpeechToTextEvent(recognizedWord);
    }
    
    @Override
    protected void onLoaded()
    {
    	super.onLoaded();
        this.speak(
        		"Welcom to LOL," +
        		"At any point as long as I am not speaking, you can say home to go back to this menu," +
        		"To play random jokes, say random," +
        		"To select category, say category,"
        		);   
    }
 
    
    
    //@Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
         
        switch(checkedId){
            case R.id.PlayRandom:
            	option_value="Play Random Jokes";
                Intent optionselected_rand = new Intent(MainActivity.this,JokeActivity.class); 
                startActivity(optionselected_rand);
                break;
            case R.id.SelectCategory:
            	option_value="Play by Category";
                Intent optionselected_cat = new Intent(MainActivity.this,CategoryActivity.class);
                startActivity(optionselected_cat);
                break;     
        }           
              
    }
   
 }