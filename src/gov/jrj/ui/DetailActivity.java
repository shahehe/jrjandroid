package gov.jrj.ui;


import gov.jrj.R;
import gov.jrj.library.http.ImageLoader;
import gov.jrj.ui.util.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {
	TextView mTextTitle;
	TextView mTextSubject;
	TextView mContent;
	TextView mDate;

	private String title = "";
	private String subject = "";
	private String context = "";
	
	Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.text_item_detail_liao);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		mTextSubject = (TextView) findViewById(R.id.detail_txt_subject);
		mContent = (TextView) findViewById(R.id.detail_txt_content);
		mDate = (TextView) findViewById(R.id.textView1);
		btnBack = (Button) findViewById(R.id.detail_back_btn);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DetailActivity.this.finish();
			}
		});
		setTitle();
		setSubject();
		setContent();
		setDate();
		setImage();
	}

	private void setTitle() {
		title = getIntent().getExtras().getString(Constants.KEY_TITLE_BAR);
		mTextTitle.setText(title);
	}

	private void setSubject() {
		subject = getIntent().getExtras().getString(Constants.KEY_SUBJECT_NAME);
		mTextSubject.setText(subject);
	}

	private void setContent() {
		context = getIntent().getExtras().getString(Constants.KEY_ITEM_NAME);
		if(context.equals("[]"))
			context = "";
		mContent.setText(context);
	}
	private void setDate() {
		String date = getIntent().getExtras().getString("date");
		mDate.setText(date);
	}
	
	private void setImage() {
		ImageView iv = (ImageView)this.findViewById(R.id.imageView1);
		if(getIntent().getExtras().containsKey("image")){
			String image = getIntent().getExtras().getString("image");
			ImageLoader.loadImage(DetailActivity.this,iv, image);
		}else{
			iv.setVisibility(View.INVISIBLE);
		}
	}
}
