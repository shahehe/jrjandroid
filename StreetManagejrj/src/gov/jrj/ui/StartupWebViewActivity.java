package gov.jrj.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.protocol.HTTP;

import gov.jrj.R;
import gov.jrj.library.http.AsyncHttpClient;
import gov.jrj.library.http.AsyncHttpResponseHandler;
import gov.jrj.library.http.Config;
import gov.jrj.ui.util.MyWebViewClient;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class StartupWebViewActivity extends Activity {
	Button btnBack;
	TextView mTextTitle;
	WebView wv;
	String url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_webview_activity);
		 wv = (WebView)findViewById(R.id.webView);
//		 wv.getSettings().setJavaScriptEnabled(true);  
		 wv.setBackgroundColor(0);
//		wv.setWebViewClient(new MyWebViewClient());
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("加载中");
		dialog.setCanceledOnTouchOutside(false);
	
		String data = new String();
		try{
			Bundle bundle = getIntent().getExtras();
			String cache = bundle.getString("cache");
			url = bundle.getString("url");
			if(cache.equals("Y")){
				data = Config.getCacheData(this, url);
				wv.getSettings().setDefaultTextEncodingName("utf-8") ;
				wv.loadData(URLEncoder.encode(data, "utf-8"), "text/html; charset=UTF-8", HTTP.UTF_8);
//				wv.loadDataWithBaseURL(null,data, null,  HTTP.UTF_8, null);
			}else{
				 AsyncHttpClient client = new AsyncHttpClient();
					dialog.show();
					client.get(url, new AsyncHttpResponseHandler(){

						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							super.onStart();
						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							super.onFinish();
							dialog.dismiss();
						}

						@Override
						public void onFailure(Throwable error, String content) {
							// TODO Auto-generated method stub
							super.onFailure(error, content);
							dialog.dismiss();
						}

						@Override
						public void onSuccess(String content) {
							// TODO Auto-generated method stub
//							super.onSuccess(content);
							wv.getSettings().setDefaultTextEncodingName("utf-8") ;
							try {
								wv.loadData(URLEncoder.encode(content, "utf-8"),  "text/html; charset=UTF-8",  HTTP.UTF_8);
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							wv.loadDataWithBaseURL(null,content, null,  HTTP.UTF_8, null);
							Config.setCacheData(StartupWebViewActivity.this, url, content);
							
							dialog.dismiss();
						}});     
			}

			
		}catch(Exception e){
			e.printStackTrace();
		}
		 btnBack = (Button) findViewById(R.id.about_back_btn);
		 btnBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					StartupWebViewActivity.this.finish();
				}
			});
			
		 mTextTitle = (TextView)findViewById(R.id.txt_title);
		 try{
			 Bundle bundle = getIntent().getExtras();
			 String title = bundle.getString("title");
			 mTextTitle.setText(title);
		 }catch(Exception e){
			 e.printStackTrace();
			 mTextTitle.setText(getString(R.string.startupmessage));
		 }
	}
}
