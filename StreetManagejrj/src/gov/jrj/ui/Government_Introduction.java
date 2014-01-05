package gov.jrj.ui;

import gov.jrj.R;
import gov.jrj.ui.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Government_Introduction extends Activity {
	String mTitle;
	String mContentFileName;

/*	
	TextView mTextTitle;
	TextView nTextTitle;
	TextView nTextView;
	Button btnBack;
*/
	TextView mTextTitle;
	TextView mTextSubject;
	TextView mContent;
	TextView mDate;
	Button btnBack;
	@SuppressWarnings("unused")
	private String title = "";
	@SuppressWarnings("unused")
	private String subject = "";
	@SuppressWarnings("unused")
	private String context = "";	
	String names = "西城区金融街街道办事处";

	@Override  
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.text_item_detail_di);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		mTextSubject = (TextView) findViewById(R.id.detail_txt_subject);
		mContent = (TextView) findViewById(R.id.detail_txt_content);
		//mDate = (TextView) findViewById(R.id.textView1);
		btnBack = (Button) findViewById(R.id.detail_back_btn);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Government_Introduction.this.finish();
			}
		});
		setTitle();
		mContent.setText(getAssetsString(this,getIntent().getExtras().getString(Constants.CONTENT_FILE)));
		mContent.setMovementMethod(new ScrollingMovementMethod());
		mTextSubject.setText(getIntent().getExtras().getString(Constants.SUBJECT_TITLE));
				
	}
			
	
	public String getAssetsString(Context context,String fileName){
		StringBuffer sb = new StringBuffer();
		
		//根据语言选择加载
		try {
			AssetManager am = context.getAssets();
			InputStream in = am.open(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = reader.readLine())!=null){
				line += ("\n");
				sb.append(line);
			}

			reader.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
			
	private void setTitle() {
		
		
		String subTitle = "", subject = "";
		subTitle = getIntent().getExtras().getString(Constants.KEY_ITEM_NAME);
		subject = getIntent().getExtras().getString(Constants.KEY_SUBJECT_NAME);
		mTextTitle.setText(subject + " > " + subTitle);
		



	}//
	
}