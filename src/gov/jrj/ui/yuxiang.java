package gov.jrj.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import gov.jrj.R;
import gov.jrj.ui.util.Constants;

public class yuxiang extends Activity implements OnClickListener{
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
	private String title = "";
	private String subject = "";
	private String context = "";	
	String names = "西城区金融街街道办事处";

	@Override  
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yuxiang);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		mTextSubject = (TextView) findViewById(R.id.detail_txt_subject);
		mContent = (TextView) findViewById(R.id.detail_txt_content);
		btnBack = (Button) findViewById(R.id.detail_back_btn);
		WebView webView = (WebView)this.findViewById(R.id.webView1);
	    //String url = "file:///android_asset/11.html";
		String url = "file:///android_asset/" + getIntent().getExtras().getString("buildingId")+ ".html";
		webView.loadUrl(url);
		btnBack.setOnClickListener(new OnClickListener() {
		WebView webView = (WebView)findViewById(R.id.webView1);
		
			@Override
			public void onClick(View arg0) {
				yuxiang.this.finish();
			}
		});
		setTitle();

				
	}
			
	

			
	private void setTitle() {
		
		
		String subTitle = "", subject = "";
		subTitle = getIntent().getExtras().getString(Constants.KEY_ITEM_NAME);
		subject = getIntent().getExtras().getString(Constants.KEY_SUBJECT_NAME);
		mTextTitle.setText(subject + " > " + subTitle);
		



	}




	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.about_back_btn){
			this.finish();
		}	
	}
	
}