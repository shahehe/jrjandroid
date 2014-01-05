package gov.jrj.ui;

import gov.jrj.R;
import gov.jrj.ui.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

@SuppressLint("ValidFragment")
public class GovInfoFragment extends Fragment {
	private View view ;
	private ImageButton award;

	public static int[] subjects = new int[] { R.string.gover_discription,
			R.string.work_procedure, R.string.contact_phone, R.string.book, R.string.gongshang };

	private static int[] icons = new int[] { R.drawable.street,
			R.drawable.ic_procedure, R.drawable.contact, R.drawable.book1, R.drawable.info_dis };
/*
	private static int[] icons = new int[] { R.drawable.gover_discription_new,
		R.drawable.work_procedure_new, R.drawable.ic_contact, R.drawable.book };*/
	private OnItemClickListener goverItemClick = new OnItemClickListener() {
		Intent intent = new Intent();
  
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (arg2) { // arg2 is item position in listview
			case 0: //�������
				
				intent.setClass(mContext, Government_Introduction.class);
				intent.putExtra(Constants.KEY_SUBJECT_NAME, getString(R.string.tab1));
				intent.putExtra(Constants.KEY_ITEM_NAME, getString(mSubjects[arg2]));
				intent.putExtra(Constants.SUBJECT_TITLE, "西城区金融街街道办事处");
				intent.putExtra(Constants.CONTENT_FILE, "test.txt");
				mContext.startActivity(intent);

				break; 
			case 1:
				intent.setClass(mContext, ProcedureActivity.class);
				intent.putExtra(Constants.KEY_SUBJECT_NAME, getString(R.string.tab1));
				intent.putExtra(Constants.KEY_ITEM_NAME, getString(mSubjects[arg2]));
				mContext.startActivity(intent);				
				break;
			case 2: // ��ϵ�绰
				
				intent.setClass(mContext, ContactPhoneActivity.class);
				intent.putExtra(Constants.KEY_SUBJECT_NAME, getString(R.string.tab1));
				intent.putExtra(Constants.KEY_ITEM_NAME, getString(mSubjects[arg2]));
				mContext.startActivity(intent);
				break;
			case 3: // ��ϵ�绰
				
				intent.setClass(mContext, CustomizedListView.class);
				intent.putExtra(Constants.KEY_SUBJECT_NAME, getString(R.string.tab1));
				intent.putExtra(Constants.KEY_ITEM_NAME, getString(mSubjects[arg2]));
				mContext.startActivity(intent);
				break;				
			case 4: //gongshang
				intent.setClass(mContext, ProcedureList.class);
				intent.putExtra(Constants.KEY_SUBJECT_NAME, getString(R.string.tab1));
				intent.putExtra(Constants.KEY_ITEM_NAME, getString(mSubjects[arg2]));
				intent.putExtra(Constants.KEY_ARRAY, R.array.gongshang_array);
				mContext.startActivity(intent);				
				break;
				
//			case 5:
//				intent.setClass(mContext, yuxiang.class);
//				intent.putExtra(Constants.KEY_SUBJECT_NAME,  getString(R.string.tab1));
//				intent.putExtra(Constants.KEY_ITEM_NAME, getString(mSubjects[arg2]));
//				intent.putExtra(Constants.SUBJECT_TITLE, "西城区金融街街道办事处");
//				intent.putExtra(Constants.CONTENT_FILE, "test.txt");
//				intent.putExtra("buildingId", "20130124");
//				ListViewFragment.this.startActivity(intent);
//				break;
				
			default:
				break;
			}
		}  
	};  

	// ��̬����
	private static int[] annonce_subjects = new int[] { R.string.gover_news,
			R.string.traffic_news, R.string.street_news };

	private static int[] annonce_icons = new int[] { R.drawable.news,
			R.drawable.traffic_news, R.drawable.street_news };

	private OnItemClickListener boardItemClick = new OnItemClickListener() {
		 
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (arg2) { // arg2 is item position in listview
			case 0:
				break;
			case 1:
				break;
			case 2: //�ֵ���̬
				Intent intent = new Intent();
				intent.setClass(mContext, TextNewsListActivity.class);
				intent.putExtra(Constants.KEY_SUBJECT_NAME, getString(R.string.tab2));
				intent.putExtra(Constants.KEY_ITEM_NAME, getString(mSubjects[arg2]));
				mContext.startActivity(intent);
				break;
			default:
				break;
			}
		}
	};

	private int[] mSubjects;
	private int[] mIcons;
	private Context mContext;

	/*
	 * Ϊfalse�����������Ϣ��fragmentΪtrue����Ƕ�̬�����fragment Ĭ��Ϊfalse
	 */
	private boolean stateIsAnnonce = false;

	private ListView listView;

	public GovInfoFragment(boolean isAnnonce) {
		stateIsAnnonce = isAnnonce;
		mSubjects = isAnnonce ? annonce_subjects : subjects;
		mIcons = isAnnonce ? annonce_icons : icons;
	}

	public GovInfoFragment() {
		super();
		boolean isAnnonce = false;
		stateIsAnnonce = isAnnonce;
		mSubjects = isAnnonce ? annonce_subjects : subjects;
		mIcons = isAnnonce ? annonce_icons : icons;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = this.getActivity();
		initViews();
		listView.setAdapter(getAdapter());
		listView.setOnItemClickListener(goverItemClick);
		award.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, AwardListActivity.class);
				intent.putExtra(Constants.KEY_SUBJECT_NAME, getString(R.string.tab1));
				intent.putExtra(Constants.KEY_ITEM_NAME,getString(R.string.award));
				intent.putExtra(Constants.SUBJECT_TITLE, getString(R.string.award));
				intent.putExtra(Constants.CONTENT_FILE, "award.txt");
				mContext.startActivity(intent);
			}
			
		});
		}

	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i("FragmentList", "Item clicked: " + id);
	}

	private SimpleAdapter getAdapter() {
		SimpleAdapter basicSetAdapter = new ItemAdapter(this.getActivity(),
				createBasicListData(), R.layout.gov_listview_item, new String[] {
						"gov"}, new int[] {
						R.id.govbackground });

		return basicSetAdapter;
	}

	private List createBasicListData() {
		List<Map> listData = new ArrayList<Map>();
		for (int i = 0; i < subjects.length; i++) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("gov", String.valueOf(mIcons[i]));
			listData.add(temp);
		}
		return listData;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view =  inflater.inflate(R.layout.listview_fragment,
				container, false);
		return view;
	}

	private class ItemAdapter extends SimpleAdapter {

		public ItemAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return super.getView(position, convertView, parent);
		}

	}

	private void initViews() {
		listView = (ListView) view.findViewById(R.id.mainMessage);
//		listView.setDivider(this.getActivity().getResources()
//				.getDrawable(R.drawable.app_divider_h_gray));
		listView.setDividerHeight(0);
		Resources rs = getResources();
		Drawable dr = rs.getDrawable(R.drawable.backgroundrep);
		listView.setBackgroundDrawable(dr);
		listView.setOnItemClickListener(stateIsAnnonce ? boardItemClick
				: goverItemClick);
		award = (ImageButton)view.findViewById(R.id.awawd);
	}
}