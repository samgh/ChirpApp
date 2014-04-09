package com.samgavis.chirpapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ChirpAppActivity extends SingleFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	protected Fragment createFragment() {
		//return new ChirpAppFragment();
		return new ChirpAppFragment();
	}

}
