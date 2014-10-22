package gov.jrj.ui;

import gov.jrj.R;
import gov.jrj.ui.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ProcedureActivity extends Activity {
	private ListView mProcedureList;
	private TextView mTitle;
	private Button mBackButton;
	private String mSubTitle;
	private String mSubject;
	private String[] mItemTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.procedure_activity);

		this.mItemTitle = this.getResources().getStringArray(
				R.array.procedure_list);

		this.mBackButton = (Button) findViewById(R.id.about_back_btn);
		this.mBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProcedureActivity.this.finish();
			}
		});

		this.mTitle = (TextView) findViewById(R.id.txt_title);
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			try {
				this.mSubTitle = getIntent().getExtras().getString(
						Constants.KEY_ITEM_NAME);
				this.mSubject = getIntent().getExtras().getString(
						Constants.KEY_SUBJECT_NAME);
				this.mTitle.setText(this.mSubject + " > " + this.mSubTitle);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < this.mItemTitle.length; i++) {
			HashMap<String, Object> temp = new HashMap<String, Object>();
			temp.put("subject", this.mItemTitle[i]);
			temp.put("ic_operate", R.drawable.gev_info_dis);

			listItem.add(temp);
		}
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,
				R.layout.listview_item,
				new String[] { "subject", "ic_operate" }, new int[] {
						R.id.text_summary, R.id.ic_operatate });
		this.mProcedureList = (ListView) findViewById(R.id.procedureList);
		this.mProcedureList.setAdapter(listItemAdapter);
		this.mProcedureList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(ProcedureActivity.this,
						ProcedureList.class);
				int arrayID = 0;
				switch (arg2) {
				case 0:
					arrayID = R.array.procedure_list_first;
					break;
				case 1:
					arrayID = R.array.procedure_list_second;
					break;
				case 2:
					arrayID = R.array.procedure_list_third;
					break;
				case 3:
					arrayID = R.array.procedure_list_forth;
					break;
				case 4:
					arrayID = R.array.procedure_list_fifth;
					break;
				case 5:
					arrayID = R.array.procedure_list_sixth;
					break;
				}
				intent.putExtra(Constants.KEY_SUBJECT_NAME, mSubject);
				intent.putExtra(Constants.KEY_ITEM_NAME, mSubTitle);
				intent.putExtra(Constants.KEY_ARRAY, arrayID);
				intent.putExtra(Constants.SUBJECT_TITLE,
						ProcedureActivity.this.mItemTitle[arg2]);
				intent.putExtra(Constants.CONTENT_FILE,
						"procedure" + Integer.toString(arg2));
				startActivity(intent);
			}
		});
	}
}
