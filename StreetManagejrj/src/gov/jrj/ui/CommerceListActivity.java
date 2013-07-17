package gov.jrj.ui;

import gov.jrj.R;
import gov.jrj.ui.util.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class CommerceListActivity extends Activity {
	Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.commerce_list_activity);
		ImageButton btn1 = (ImageButton) findViewById(R.id.imageButton1);
		btn1.setOnClickListener(new CommerceLisener());
		ImageButton btn2 = (ImageButton) findViewById(R.id.imageButton2);
		btn2.setOnClickListener(new CommerceLisener());
		btnBack = (Button) findViewById(R.id.about_back_btn);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CommerceListActivity.this.finish();
			}
		});
	}

	public CommerceListActivity() {
		super();
		// TODO Auto-generated constructor stub
	}

	class CommerceLisener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (v.getId()) {
			case R.id.imageButton1:
				intent = new Intent();
				intent.setClass(CommerceListActivity.this,
						ProductListActivity.class);
				intent.putExtra(Constants.KEY_ITEM_NAME, "工商动态");
				intent.putExtra(Constants.KEY_SUBJECT_NAME, "下架商品");
				startActivity(intent);
				break;
			case R.id.imageButton2:
				intent = new Intent();
				intent.setClass(CommerceListActivity.this,
						CustomerListActivity.class);
				intent.putExtra(Constants.KEY_ITEM_NAME, "工商动态");
				intent.putExtra(Constants.KEY_SUBJECT_NAME, "消费提醒");
				startActivity(intent);
				break;
			}
		}

	}
}
