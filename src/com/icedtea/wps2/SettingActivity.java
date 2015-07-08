package com.icedtea.wps2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingActivity extends Activity {

	private EditText txt_mac1;
	private EditText txt_mac2;
	private EditText txt_mac3;
	private EditText txt_mac4;
	private EditText txt_mac5;
	
	private EditText txt_x1;
	private EditText txt_x2;
	private EditText txt_x3;
	private EditText txt_x4;
	private EditText txt_x5;
	
	private EditText txt_y1;
	private EditText txt_y2;
	private EditText txt_y3;
	private EditText txt_y4;
	private EditText txt_y5;
	
	private Button btn_done;
	private Button btn_clear;
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		init();
		
		btn_done.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SharedPreferences.Editor editor = sp.edit();
				
				editor.putString("mac1", txt_mac1.getText().toString());
				editor.putString("mac2", txt_mac2.getText().toString());
				editor.putString("mac3", txt_mac3.getText().toString());
				editor.putString("mac4", txt_mac4.getText().toString());
				editor.putString("mac5", txt_mac5.getText().toString());
				
				editor.putString("x1", txt_x1.getText().toString());
				editor.putString("x2", txt_x2.getText().toString());
				editor.putString("x3", txt_x3.getText().toString());
				editor.putString("x4", txt_x4.getText().toString());
				editor.putString("x5", txt_x5.getText().toString());
				
				editor.putString("y1", txt_y1.getText().toString());
				editor.putString("y2", txt_y2.getText().toString());
				editor.putString("y3", txt_y3.getText().toString());
				editor.putString("y4", txt_y4.getText().toString());
				editor.putString("y5", txt_y5.getText().toString());
				
				editor.commit();
				
				finish();
			}
		});
        
        btn_clear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				txt_mac1.setText("");
				txt_mac2.setText("");
				txt_mac3.setText("");
				txt_mac4.setText("");
				txt_mac5.setText("");
		    	
				txt_x1.setText("");
				txt_x2.setText("");
				txt_x3.setText("");
				txt_x4.setText("");
				txt_x5.setText("");
		    	
				txt_y1.setText("");
				txt_y2.setText("");
				txt_y3.setText("");
				txt_y4.setText("");
				txt_y5.setText("");
			}
		});
	}
	
    private void init() {
    	txt_mac1 = (EditText)findViewById(R.id.mac1);
    	txt_mac2 = (EditText)findViewById(R.id.mac2);
    	txt_mac3 = (EditText)findViewById(R.id.mac3);
    	txt_mac4 = (EditText)findViewById(R.id.mac4);
    	txt_mac5 = (EditText)findViewById(R.id.mac5);
    	
    	txt_x1 = (EditText)findViewById(R.id.x1);
    	txt_x2 = (EditText)findViewById(R.id.x2);
    	txt_x3 = (EditText)findViewById(R.id.x3);
    	txt_x4 = (EditText)findViewById(R.id.x4);
    	txt_x5 = (EditText)findViewById(R.id.x5);
    	
    	txt_y1 = (EditText)findViewById(R.id.y1);
    	txt_y2 = (EditText)findViewById(R.id.y2);
    	txt_y3 = (EditText)findViewById(R.id.y3);
    	txt_y4 = (EditText)findViewById(R.id.y4);
    	txt_y5 = (EditText)findViewById(R.id.y5);
    	
    	btn_done = (Button)findViewById(R.id.done);
    	btn_clear = (Button)findViewById(R.id.clear);
    	
    	sp = getSharedPreferences("global", 0);
    	
    	txt_mac1.setText(sp.getString("mac1", ""));
    	txt_mac2.setText(sp.getString("mac2", ""));
    	txt_mac3.setText(sp.getString("mac3", ""));
    	txt_mac4.setText(sp.getString("mac4", ""));
    	txt_mac5.setText(sp.getString("mac5", ""));
    	
    	txt_x1.setText(sp.getString("x1", ""));
    	txt_x2.setText(sp.getString("x2", ""));
    	txt_x3.setText(sp.getString("x3", ""));
    	txt_x4.setText(sp.getString("x4", ""));
    	txt_x5.setText(sp.getString("x5", ""));
    	
    	txt_y1.setText(sp.getString("y1", ""));
    	txt_y2.setText(sp.getString("y2", ""));
    	txt_y3.setText(sp.getString("y3", ""));
    	txt_y4.setText(sp.getString("y4", ""));
    	txt_y5.setText(sp.getString("y5", ""));
    }
}
