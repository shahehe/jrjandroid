package gov.jrj.ui;

import gov.jrj.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomerInfoActivity extends Activity {

	@SuppressWarnings("unused")
	private JSONArray jsons = new JSONArray();
	@SuppressWarnings("unused")
	private Activity mContext;
	@SuppressWarnings("unused")
	private ProgressDialog pd;

	ProgressBar progressBar;
	TextView mTextTitle;
	Button btnBack;
	JSONObject data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.health_activity);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		mTextTitle.setText("健康服务");
		btnBack = (Button) findViewById(R.id.about_back_btn);
		this.progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		initViews();
	}


	private void initViews() {


		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CustomerInfoActivity.this.finish();
			}
		});
		try {
			data = new JSONObject(this.getIntent().getStringExtra("data"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}