package com.samgavis.chirpapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Stack;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChirpAppFragment extends Fragment {
	static final String TAG = "ChirpApp";
	
	private TextView mUsernameTextBox;
	private EditText mTweetTextBox;
	private Button mSubmitButton;
	private Button mUndoButton;
	private TextView mCharCountTextBox;
	private Button mTweetButton;
	private CheckBox mHashCheckbox;
	private CheckBox mTagCheckbox;
	private CheckBox mLinkCheckbox;
	private CheckBox mShortenCheckbox;
	
	private int flags;
	private AppData appData;
	private Stack<String> undoStack;
	private TwitterAsyncClient twitter;
	
	private int defaultColor;
	private int highlightColor = Color.parseColor("#d44950");
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.chirpapp_context, menu);
	}
	
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_logout:
			appData.setIsTwitterLoggedIn(false);
        	Intent i = new Intent(getActivity(), LoginActivity.class);
    		startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_chirpapp, parent, false);
        
		appData = new AppData(getActivity());
		undoStack = new Stack<String>();
		twitter = new TwitterAsyncClient(appData);
		
		mUndoButton = (Button)v.findViewById(R.id.undo_button);
		mUndoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!undoStack.empty()) {
					mTweetTextBox.setText(undoStack.pop());
				} else {
					mTweetTextBox.setText("");
				}
			}
		});
		
		mCharCountTextBox = (TextView)v.findViewById(R.id.char_count_textbox);
		mCharCountTextBox.setText("0/140");
		defaultColor = mCharCountTextBox.getCurrentTextColor();
		
        mTweetTextBox = (EditText)v.findViewById(R.id.tweet_text);
        mTweetTextBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				int len = arg0.length();
				mCharCountTextBox.setText(len + "/140");
				if (len <= 140) {
					mCharCountTextBox.setTextColor(defaultColor);
				} else {
					mCharCountTextBox.setTextColor(highlightColor);
				}
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}
        });
        
        mSubmitButton = (Button)v.findViewById(R.id.compute_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String input = mTweetTextBox.getText().toString();
				if (undoStack.empty()) undoStack.push(input);
				else if (!undoStack.peek().equals(input)) undoStack.push(input);
				flags = 0b0000;
				if (appData.isHashtags()) flags = (flags | Constants.HASHTAG_FLAG);
				if (appData.isTagPeople()) flags = (flags | Constants.TAG_FLAG);
				if (appData.isShortenURLs()) flags = (flags | Constants.URL_FLAG);
				if (appData.isShortenText()) flags = (flags | Constants.SHORT_FLAG);
				String output = GenerateTweet.generate(input, flags);
				mTweetTextBox.setText(output);
			}
        });
        
        mTweetButton = (Button)v.findViewById(R.id.tweet_button);
        mTweetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String tweetUrl = "https://twitter.com/intent/tweet?text=" + 
							URLEncoder.encode(mTweetTextBox.getText().toString().replace("%", Constants.PERCENT_ENCODING), "UTF-8");
					//Log.d(TAG, tweetUrl);
					Uri uri = Uri.parse(tweetUrl);
					startActivity(new Intent(Intent.ACTION_VIEW, uri));
				} catch (UnsupportedEncodingException e) {
					//Log.d(TAG, "Encoding error: " + e);
				}
			}
		});
        
        mHashCheckbox = (CheckBox)v.findViewById(R.id.hash_checkbox);
        //mHashCheckbox.setEnabled(false);
        mHashCheckbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(getActivity(), "Coming soon!", Toast.LENGTH_SHORT);
				toast.show();
				mHashCheckbox.setChecked(false);
			}
		});
/*        mHashCheckbox.setChecked(appData.isHashtags());
        mHashCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appData.setHashtags(isChecked);
			}
		});*/
        
        mTagCheckbox = (CheckBox)v.findViewById(R.id.tag_checkbox);
        //mTagCheckbox.setEnabled(false);
        mTagCheckbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(getActivity(), "Coming soon!", Toast.LENGTH_SHORT);
				toast.show();
				mTagCheckbox.setChecked(false);
			}
		});
/*        mTagCheckbox.setChecked(appData.isTagPeople());
        mTagCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appData.setTagPeople(isChecked);
			}
		});*/
        
        mLinkCheckbox = (CheckBox)v.findViewById(R.id.link_checkbox);
        mLinkCheckbox.setChecked(appData.isShortenURLs());
        mLinkCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appData.setShortenURLs(isChecked);
			}
		});
        
        mShortenCheckbox = (CheckBox)v.findViewById(R.id.shorten_checkbox);
        mShortenCheckbox.setChecked(appData.isShortenText());
        mShortenCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appData.setShortenText(isChecked);
			}
		});
        
		ConnectionDetector cd = new ConnectionDetector(getActivity());
		if (!cd.isConnectingToInternet()) {
			AlertDialogManager.showAlertDialog(getActivity(), "No Internet Connection",
                    "You must enable network connection to connect to Twitter.", false);
			return v;
		}
		
		mUsernameTextBox = (TextView)v.findViewById(R.id.username_textbox);
		mUsernameTextBox.setText("@" + twitter.getHandle());
        
        return v;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		appData.saveAppData();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ConnectionDetector cd = new ConnectionDetector(getActivity());
		if (!cd.isConnectingToInternet()) return;
		mUsernameTextBox.setText("@" + twitter.getHandle());
	}
}
