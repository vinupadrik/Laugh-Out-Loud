package com.lol;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;


public class JokeActivity extends SpeechActivity {

	int buffSize = 10;
	int jokeCount = 0;
	int idx = 0;
	String str="";
	String str1 = "--------------";
	StringBuffer[] buf = new StringBuffer[buffSize];
	TextView joke;
	Handler handler;
	Runnable runnable;
	private ImageButton homeButton = null;
	private ImageButton playButton = null;
	private ImageButton nextButton = null;
	private ImageButton prevButton = null;
	
	Boolean startTTS = false;
	Timer retryTTS = new Timer();
	
	@Override
	public void onLoaded()
	{
		Log.v("JokeActivity", "onLoad");
		retryTTS.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (startTTS)
				{
					startTTS = false;
					speak(buf[idx].toString());
				}
			}
			
		}, 0, 100);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jokes_activity);
		
		joke = (TextView) this.findViewById(R.id.txt_joke);	
		homeButton = (ImageButton) findViewById(R.id.btn_home);
		playButton = (ImageButton) findViewById(R.id.btn_play);
		nextButton = (ImageButton) findViewById(R.id.btn_next);
		prevButton = (ImageButton) findViewById(R.id.btn_prev);
		
		try {
			PlayWithRawFiles();
		} 
		catch (IOException e) {
			Toast.makeText(getApplicationContext(), 
						 "Problems: " + e.getMessage(), 1).show();
		}
		
		joke.setText(buf[idx]);
		startTTS = true;
		
		
		playButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {	           	
				runnable = new Runnable() {		
					public void run() {				
							if(idx<(jokeCount-1)){
							handler.postDelayed(runnable, 500); //5000
							idx++;
							joke.setText(buf[idx]);
							}
					}
				};
				handler = new Handler();
				handler.postDelayed(runnable, 500); //5000
				joke.setText(buf[idx]);
			}
		});
		
		homeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {	           	
				Intent goto_home = new Intent(JokeActivity.this,MainActivity.class);
				JokeActivity.this.finish();
				startActivity(goto_home);
				
			}
		});
		
		nextButton.setOnClickListener(new View.OnClickListener() {
			//@Override
			public void onClick(View v) {	           	
				if(idx < (jokeCount-1)){
					idx++;
					joke.setText(buf[idx]);
					startTTS = true;
				}
				else{

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JokeActivity.this);

					alertDialogBuilder.setTitle("Lol Alert");
		
					alertDialogBuilder
						.setMessage("Sorry, you have reached the very last joke. " +
								"Please choose continue to read/hear more jokes from the beginning" +
								" or select exit to exit the application")
								.setCancelable(false)
								.setPositiveButton("Continue",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										idx=0;
										joke.setText(buf[idx]);
										startTTS = true;
		    					}
		    				})
		    				.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {																	
										Intent reselect = new Intent(JokeActivity.this,ThankYouActivity.class);
										startActivity(reselect);
										JokeActivity.this.finish();
									}
							});
		     
		    				AlertDialog alertDialog = alertDialogBuilder.create();
		    				alertDialog.show();
							
				}
			}
		});
		
		
		prevButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {	           	
				if(idx > 0){
					idx--;
					joke.setText(buf[idx]);
					startTTS = true;
				}
				else
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JokeActivity.this);

					alertDialogBuilder.setTitle("Lol Alert");
		
					alertDialogBuilder
						.setMessage("Sorry, you have reached the very first joke. " +
								"Please choose continue to read/hear more jokes from the last" +
								" or select exit to exit the application")
								.setCancelable(false)
								.setPositiveButton("Continue",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										idx=jokeCount-1;
										joke.setText(buf[idx]);	
										startTTS = true;
		    					}
		    				})
		    				.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
																	
										Intent reselect = new Intent(JokeActivity.this,ThankYouActivity.class);
										startActivity(reselect);
										JokeActivity.this.finish();
									}
							});
		     
		    				AlertDialog alertDialog = alertDialogBuilder.create();
		    				alertDialog.show();
							}								

			}
		});
			
	}
	
	public void PlayWithRawFiles() throws IOException {      
		
		int i=0;
		String fileName="jokes";
		
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	fileName = extras.getString("file");
        }

		int resId = getResources().getIdentifier(fileName, "drawable","com.lol");
		InputStream istream = this.getResources().openRawResource(resId);
		BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
		buf[i] = new StringBuffer();
		
		if (istream!=null) {							
			while ((str = reader.readLine()) != null) {	
				if(str.equals(str1)){
					i++;
					buf[i] = new StringBuffer();
					continue;
				}
				buf[i].append(str + "\n" );
			}				
		}
		jokeCount = i+1;
		istream.close();
        
	}
}
