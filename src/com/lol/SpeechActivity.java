package com.lol;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.speech.tts.UtteranceProgressListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;



//tutorial: implements OnClickListener
public abstract class SpeechActivity extends Activity implements  OnInitListener, OnUtteranceCompletedListener {
	
	
	//stt
	
	private Boolean speechRecognitionEnabled = false;
	private static final int VR_REQUEST = 999;
	private ListView wordList;
	private final String LOG_TAG = "SpeechActivity";
	
	//tts
    protected int MY_DATA_CHECK_CODE = 0;
    protected TextToSpeech myTTS;
    
    Timer checkSpeech = new Timer();
    Boolean sttDone = false;
    
    private void speechCheck()
    {
    	if (myTTS.isSpeaking())
    	{
    		
    	}
    	else if (!sttDone)
    	{
    		listenToSpeech();
    		sttDone = true;
    	}
    }
    public void startActivity(Intent intent)
    {
    	sttDone = false;
    	super.startActivity(intent);
    }
    
    //tts and stt methods
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	//stt
    	//find out whether speech recognition is supported
    	PackageManager packManager = getPackageManager();
    	List<ResolveInfo> intActivities = packManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
    	if (intActivities.size() != 0) {
    	    //speech recognition is supported - detect user button clicks
    	    this.speechRecognitionEnabled = true;
    		//speechBtn.setOnClickListener(this);
    	}
    	else
    	{
    	    //speech recognition not supported, disable button and output message
    		this.speechRecognitionEnabled = false;
    		Toast.makeText(this, "Oops - Speech recognition not supported!", Toast.LENGTH_LONG).show();
    		//speechBtn.setEnabled(false);
    	}
    	//tts
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
                
                

            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
            
            
        }
        else if (requestCode == VR_REQUEST && resultCode == RESULT_OK)
        {
            //store the returned word list as an ArrayList
            ArrayList<String> suggestedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //set the retrieved list to display in the ListView using an ArrayAdapter
            //wordList.setAdapter(new ArrayAdapter<String> (this, R.layout.word, suggestedWords));
            if (suggestedWords != null && suggestedWords.size() > 0)
//            	speak(suggestedWords.get(0));
            	onSpeechToTextEvent(suggestedWords.get(0));
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    protected void onSpeechToTextEvent(String recognizedWord)
    {
    	Log.v("speech", "super");
    }
    
    
    protected void onLoaded()
    {
    	
    }
    
    //stt methods
    private void listenToSpeech() {
        //start the speech recognition intent passing required data
        Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //indicate package
        listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        //message to display while listening
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a word!");
        //set speech model
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //specify number of results to retrieve
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        //start listening
        startActivityForResult(listenIntent, VR_REQUEST);
    }
    
    public void onUtteranceCompleted(String utteranceId) {
    	   Log.i("UT", utteranceId); //utteranceId == "SOME MESSAGE"
    	   }
    
    //tts methods
	public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            myTTS.setLanguage(Locale.US);
            checkSpeech.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					speechCheck();
				}
            }, 0, 500);
            
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
        
        onLoaded();
    }
    

    protected void speak(String words)
    {
    	myTTS.speak(words, TextToSpeech.QUEUE_FLUSH, null);
		
    }

}
