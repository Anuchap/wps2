package com.icedtea.wps2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.media.MediaPlayer;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	private DrawView drawView;

	private SharedPreferences sp;
	
	private ArrayList<AccessPoint> ap = new ArrayList<AccessPoint>();
	
	private WifiManager scaner;
	private WifiReceiver receiver;
	
	private SensorManager sensorManager;
	
	private ProgressDialog progressDlg;
	
	private int x = 0;
	private int y = 0;
	
	private int azimuth = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        drawView = new DrawView(this);
        drawView.setBackgroundResource(R.drawable.map);
        
        sp = getSharedPreferences("global", 0);
        
        scaner = (WifiManager)getSystemService(WIFI_SERVICE);
        receiver = new WifiReceiver();
        
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
       
        registerReceiver(receiver , new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        
        drawView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ap.clear();
				
				if(sp.getString("mac1", "") != "" && sp.getString("x1", "") != "" && sp.getString("y1", "") != "") {
					AccessPoint a = new AccessPoint();
					a.setBSSID(sp.getString("mac1", ""));
					a.setX(Integer.parseInt(sp.getString("x1", "")));
					a.setY(Integer.parseInt(sp.getString("y1", "")));
					ap.add(a);
				}
				
				if(sp.getString("mac2", "") != "" && sp.getString("x2", "") != "" && sp.getString("y2", "") != "") {
					AccessPoint a = new AccessPoint();
					a.setBSSID(sp.getString("mac2", ""));
					a.setX(Integer.parseInt(sp.getString("x2", "")));
					a.setY(Integer.parseInt(sp.getString("y2", "")));
					ap.add(a);
				}
				
				if(sp.getString("mac3", "") != "" && sp.getString("x3", "") != "" && sp.getString("y3", "") != "") {
					AccessPoint a = new AccessPoint();
					a.setBSSID(sp.getString("mac3", ""));
					a.setX(Integer.parseInt(sp.getString("x3", "")));
					a.setY(Integer.parseInt(sp.getString("y3", "")));
					ap.add(a);
				}
				
				if(sp.getString("mac4", "") != "" && sp.getString("x4", "") != "" && sp.getString("y4", "") != "") {
					AccessPoint a = new AccessPoint();
					a.setBSSID(sp.getString("mac4", ""));
					a.setX(Integer.parseInt(sp.getString("x4", "")));
					a.setY(Integer.parseInt(sp.getString("y4", "")));
					ap.add(a);
				}
				
				if(sp.getString("mac5", "") != "" && sp.getString("x5", "") != "" && sp.getString("y5", "") != "") {
					AccessPoint a = new AccessPoint();
					a.setBSSID(sp.getString("mac5", ""));
					a.setX(Integer.parseInt(sp.getString("x5", "")));
					a.setY(Integer.parseInt(sp.getString("y5", "")));
					ap.add(a);
				}

				scaner.startScan();
				
				progressDlg = ProgressDialog.show(MainActivity.this, null, "scaning...");
			}
		});
        
        setContentView(drawView); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_settings) {
			Intent i = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(i);
		}
		return true;
	}
	
	class AccessPoint {
    	private String bssid;
    	private int x;
    	private int y;
    	private int level = 0;
    	
    	public String getBSSID() {
    		return bssid;
    	}
    	
    	public void setBSSID(String bssid) {
    		this.bssid = bssid;
    	}
    	
    	public int getX() {
    		return x;
    	}
    	
    	public void setX(int x) {
    		this.x = x;
    	}
    	
    	public int getY() {
    		return y;
    	}
    	
    	public void setY(int y) {
    		this.y = y;
    	}
    	
    	public int getLevel() {
    		return level;
    	}
    	
    	public void setLevel(int level) {
    		this.level = level;
    	}
    }
	
	class WifiReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			progressDlg.dismiss();
			
			List<ScanResult> result = scaner.getScanResults();
			
			for (ScanResult r : result) {
				for(int i = 0; i < ap.size(); i++) {
					if(ap.get(i).getBSSID().equals(r.BSSID)) {
						ap.get(i).setLevel(Math.abs(r.level));
					}
				}
			}
			
			Collections.sort(ap, new LevelComparator());
			
			ArrayList<AccessPoint> top3 = new ArrayList<AccessPoint>();
			int j = 0;
			
			for(int i = 0; i < ap.size(); i++) {
				if(ap.get(i).getLevel() != 0) {
					top3.add(ap.get(i)); 
					j++;
				}
				
				if(j == 3) break;
			}
			
			if(top3.size() < 3) return;
			
			int p1 = top3.get(0).getLevel();
			int p2 = top3.get(1).getLevel();
			int p3 = top3.get(2).getLevel();
			
			double r1 = 10 * (((p1 - 104.164) / 20) + k(p1));
			double r2 = 10 * (((p2 - 104.164) / 20) + k(p2));
			double r3 = 10 * (((p3 - 104.164) / 20) + k(p3));
			
			int x1 = top3.get(0).getX(), y1 = top3.get(0).getY();
			int x2 = top3.get(1).getX(), y2 = top3.get(1).getY();
			int x3 = top3.get(2).getX(), y3 = top3.get(2).getY();
			
			x = (int)Math.round(calX(r1, r2, r3, x1, y1, x2, y2, x3, y3));
			y = (int)Math.round(calY(r1, r2, r3, x1, y1, x2, y2, x3, y3, x));
			
			int rx = (int)((35 - y) * 13.72);
	        int ry = (int)(x * 12.08);
			
			drawView.updatePosition(rx, ry);
			
			getLocaion();
		}
    }
	
	private float k(int p) {
	    	float k = 0;
	    	
	    	if(p > 20 && p < 60) {
	    		k = 3.0f;
	    	} else if(p >= 60 && p < 65) {
	    		k = 3.2f;
	    	} else if(p >= 65 && p < 70) {
	    		k = 3.4f;
	    	} else if(p >= 70 && p < 75) {
	     		k = 3.7f;
	     	} else if(p >= 75 && p < 80) {
	     		k = 4.0f;
	     	} else if(p >= 80 && p < 85) {
	     		k = 4.7f;
	     	} else if(p >= 85 && p < 90) {
	     		k = 5.0f;
	     	} else if(p >= 90 && p < 95) {
	     		k = 5.2f;
	     	} else if(p >= 95 && p <= 100) {
	     		k = 5.5f;
	     	} else if(p > 100) {
	     		k = 5.8f;
	     	}
	    	
	    	return k;
	    }
	    
    private double calX(double r1, double r2, double r3, int x1, int y1, int x2, int y2, int x3, int y3) {
    	double a = (y3 - y2) * (y2 - y1) * (y1 - y3);
		double b = (y2 - y1) * ((pow(r3, 2) - pow(r1, 2)) - (pow(x3, 2) - pow(x1, 2)));
		double c = (y3 - y1) * ((pow(r2, 2) - pow(r1, 2)) - (pow(x2, 2) - pow(x1, 2)));
		double d = 2 * ((y3 - y1) * (x2 - x1) - (y2 - y1) * (x3 - x1));
		
		return (a + b - c) / d;
    }
    
    private double calY(double r1, double r2, double r3, int x1, int y1, int x2, int y2, int x3, int y3, double x) {
    	double y = y1 + sqrt(abs(pow(r1, 2) - pow((x - x1), 2)));
    	double y_ = y1 - sqrt(abs(pow(r1, 2) - pow((x - x1), 2)));
    	
    	y_ = y_ < 0 ? 1 : y_;
    	
    	double cr2 = pow((x - x2), 2) + pow((y - y2), 2);
    	double cr2_ = pow((x - x2), 2) + pow((y_ - y2), 2);
    	
    	double cr3 = pow((x - x3), 2) + pow((y - y3), 2);
    	double cr3_ = pow((x - x3), 2) + pow((y_ - y3), 2);
    	
    	double diff2 = 0, diff2_ = 0, diff3 = 0, diff3_ = 0;
    	
    	double ansDiff2 = 0, ansDiff3 = 0;
    	double ansY2 = 0, ansY3 = 0;
    	
    	boolean haveR2 = false, haveR3 = false;
    	
    	if(Math.round(cr2) != Math.round(cr2_)) {
    		diff2 = r2 - cr2;
    		diff2_ = r2 - cr2_;
    		
    		if(diff2 > diff2_) {
    			ansDiff2 = diff2_;
    			ansY2 = y_;
    		} else {
    			ansDiff2 = diff2;
    			ansY2 = y;
    		}
    		
    		haveR2 = true;
    	}
    	
    	if(Math.round(cr3) != Math.round(cr3_)) {
    		diff3 = r3 - cr3;
    		diff3_ = r3 - cr3_;
    		
    		if(diff3 > diff3_) {
    			ansDiff3 = diff3_;
    			ansY3 = y_;
    		} else {
    			ansDiff3 = diff3;
    			ansY3 = y;
    		}
    		
    		haveR3 = true;
    	}
    	
    	if(haveR2 && !haveR3) return ansY2;
    	else if(!haveR2 && haveR3) return ansY3;
    	else if(haveR2 && haveR3) return ansDiff2 < ansDiff3 ? ansY2 : ansY3;
    	
    	return 0;
    }
    
    private double pow(double a, double b) {
    	return Math.pow(a, b);
    }
    
    private double sqrt(double a) {
    	return Math.sqrt(a);
    }
    
    private double abs(double a) {
    	return Math.abs(a);
    }
    
    class LevelComparator implements Comparator<AccessPoint> {
		public int compare(AccessPoint a0, AccessPoint a1) {
			return a0.getLevel() - a1.getLevel();
		}
	}

	private void getLocaion() {
		int sound = 0;
		String location = "Unknown";
		
        if(x >= 45 && x <= 60 && y >= 11 && y <= 25) {
        	location = "Room 1801";
        	sound = (azimuth >= 90 && azimuth <= 270) ? R.raw.l1801 : R.raw.r1801;
        } else if(x >= 51 && x <= 60 && y >= 26 && y <= 35) {
        	location = "Room 1802";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.r1802 : R.raw.l1802;
        } else if(x >= 43 && x <= 50 && y >= 26 && y <= 35) {
        	location = "Room 1803";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.r1803 : R.raw.l1803;
        } else if(x >= 35 && x <= 52 && y >= 26 && y <= 35) {
        	location = "Room 1804";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.r1804 : R.raw.l1804;
        } else if(x >= 26 && x <= 34 && y >= 26 && y <= 35) {
        	location = "Room 1805";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.r1805 : R.raw.l1805;
        } else if(x >= 18 && x <= 25 && y >= 26 && y <= 35) {
        	location = "Room 1806";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.r1806 : R.raw.l1806;
        } else if(x >= 10 && x <= 17 && y >= 26 && y <= 35) {
        	location = "Room 1807";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.r1807 : R.raw.l1807;
        } else if(x >= 0 && x <= 9 && y >= 26 && y <= 35) {
        	location = "Room 1808";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.r1808 : R.raw.l1808;
        } else if(x >= 0 && x <= 15 && y >= 11 && y <= 25) {
        	location = "Room 1809";
        	sound = (azimuth >= 90 && azimuth <= 270) ? R.raw.r1809 : R.raw.l1809;
        } else if(x >= 10 && x <= 17 && y >= 0 && y <= 10) {
        	location = "Room 1810";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.l1810 : R.raw.r1810;
        } else if(x >= 18 && x <= 25 && y >= 0 && y <= 10) {
        	location = "Room 1811";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.l1811 : R.raw.r1811;
        } else if(x >= 26 && x <= 34 && y >= 0 && y <= 10) {
        	location = "Room 1812";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.l1812 : R.raw.r1812;
        } else if(x >= 35 && x <= 42 && y >= 0 && y <= 10) {
        	location = "Room 1813";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.l1813 : R.raw.r1813;
        } else if(x >= 43 && x <= 50 && y >= 0 && y <= 10) {
        	location = "Room 1814";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.l1814 : R.raw.r1814;
        } else if(x >= 16 && x <= 44 && y >= 18 && y <= 25) {
        	location = "Lift";
        	sound = R.raw.lift;
        } else if(x >= 51 && x <= 60 && y >= 0 && y <= 10) {
        	location = "Men Toilet";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.lmtoilet : R.raw.rmtoilet;
        } else if(x >= 0 && x <= 9 && y >= 0 && y <= 9) {
        	location = "Women Toilet";
        	sound = (azimuth >= 0 && azimuth <= 180) ? R.raw.lwtoilet : R.raw.rwtoilet;
        } 
        
        Toast.makeText(this, location + "(" + x + "," + y + ")", Toast.LENGTH_LONG).show(); 
        
        MediaPlayer.create(MainActivity.this, sound).start();
	}
	
	private SensorEventListener sensorListener = new SensorEventListener() {

	    @Override
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    }

	    @Override
	    public void onSensorChanged(SensorEvent event) {
	      azimuth = Math.round(event.values[0]);
	    }
	};
	  
	@Override
    protected void onResume() {
      super.onResume();
      registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
      sensorManager.registerListener(sensorListener, 
              sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
              SensorManager.SENSOR_DELAY_NORMAL);
    }
	
    @Override
    protected void onPause() {
      super.onPause();
      unregisterReceiver(receiver);
      sensorManager.unregisterListener(sensorListener);
    }
}
