package gov.jrj.ui;

import java.net.URLEncoder;

import org.apache.http.protocol.HTTP;

import gov.jrj.R;
import gov.jrj.ui.util.MyWebViewClient;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class WebViewActivity extends Activity {
	Button btnBack;
	TextView mTextTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview_activity);
		WebView wv = (WebView)findViewById(R.id.webView);
		 wv.getSettings().setJavaScriptEnabled(true);  
		wv.setWebViewClient(new MyWebViewClient());
		//wv.getSettings().setUseWideViewPort(true); 
//		wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		//wv.setInitialScale(100);
//		wv.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
		 try{
			 Bundle bundle = getIntent().getExtras();
			 String url = bundle.getString("url");
			 wv.loadUrl(url);       
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 wv.getSettings().setDefaultTextEncodingName("utf-8") ;
//		 wv.loadData(URLEncoder.encode(data, "utf-8"), "text/html; charset=UTF-8", HTTP.UTF_8);

		 btnBack = (Button) findViewById(R.id.about_back_btn);
			btnBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					WebViewActivity.this.finish();
				}
			});
			
		 mTextTitle = (TextView)findViewById(R.id.txt_title);
		 try{
			 Bundle bundle = getIntent().getExtras();
			 String title = bundle.getString("title");
			 mTextTitle.setText(title);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	}
}
