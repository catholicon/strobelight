package com.stwalkerster.android.apps.strobelight;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.NumberPicker.OnValueChangeListener;

public class StrobeLightConfig extends Activity {
	
	Camera cam;
	StrobeRunner runner;
	Thread bw;
	
	public final Handler mHandler = new Handler();
	
    public final Runnable mShowToastRunnable = new Runnable() {
        public void run() {
            showMessage();
        }
    };
	
    /** Called when the activity is first created. */
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final ToggleButton togglebutton = (ToggleButton) findViewById(R.id.ToggleButton01);
        
        runner = StrobeRunner.getInstance();
        runner.controller = this;
        
        if(runner.isRunning)
        {	
        	
        }
        else
        {
        	try
        	{
        		
		        cam = Camera.open();
		        
		        if(cam==null)
		        {
		        	togglebutton.setEnabled(false);
		        	TextView t = (TextView)findViewById(R.id.TextView01);
		        	t.setText(R.string.nocamera);
		        	return;
		        }
		        
		        cam.release();
        	}
        	catch(RuntimeException ex)
        	{
	        	togglebutton.setEnabled(false);
	        	TextView t = (TextView)findViewById(R.id.TextView01);
	        	t.setText(R.string.nocamera);
	        	Toast.makeText(getApplicationContext(), "Error connecting to camera flash.", Toast.LENGTH_LONG).show();
	        	return;
        	}
        } 
        
        
        togglebutton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks
                if (togglebutton.isChecked()) {
                    bw = new Thread(runner);
                    bw.start();
                } else {
                    runner.requestStop = true;
                }
            }
        });
        
        final SeekBar skbar = (SeekBar)findViewById(R.id.SeekBar01);
        final NumberPicker skbarVal = (NumberPicker)findViewById(R.id.seekBarValue01);
        skbarVal.setMaxValue(skbar.getMax());
        skbarVal.setValue(skbar.getProgress());
        skbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				runner.delay=progress;
				if(skbarVal.getValue()!=progress)
					skbarVal.setValue(progress);
			}
		});
        skbarVal.setOnValueChangedListener(new OnValueChangeListener() {
        	
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				skbar.setProgress(newVal);
			}
		});
        
        final SeekBar skbaroff = (SeekBar)findViewById(R.id.SeekBar02);
        final NumberPicker skbaroffVal = (NumberPicker)findViewById(R.id.seekBarValue02);
        skbaroffVal.setMaxValue(skbaroff.getMax());
        skbaroffVal.setValue(skbaroff.getProgress());
        skbaroff.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				runner.delayoff=progress;
				if(skbaroffVal.getValue()!=progress)
					skbaroffVal.setValue(progress);
			}
		});
        skbaroffVal.setOnValueChangedListener(new OnValueChangeListener() {
        	
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				skbaroff.setProgress(newVal);
			}
		});

        
    }

    @Override
    protected void onStop() {
    	runner.requestStop=true;
        ToggleButton togglebutton = (ToggleButton) findViewById(R.id.ToggleButton01);
        togglebutton.setChecked(false);
    	
    	super.onStop();
    }

    public void showMessage()
    {
    	String err = runner.errorMessage;
    	runner.errorMessage="";
    	if(!err.equals(""))
    	{
	    	Context context = getApplicationContext();
	    	int duration = Toast.LENGTH_SHORT;
	
	    	Toast toast = Toast.makeText(context, err, duration);
	    	toast.show();
    	}
    	
        ToggleButton togglebutton = (ToggleButton) findViewById(R.id.ToggleButton01);
        togglebutton.setChecked(false);
    }
}