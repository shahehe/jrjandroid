package gov.jrj.ui;

import gov.jrj.R;
import gov.jrj.ui.util.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class Tab2MenuFragment extends Fragment implements OnClickListener{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}
		View view = inflater.inflate(R.layout.tab2menu_fragment,
				container, false);
		ImageButton btn1 = (ImageButton)view.findViewById(R.id.imageButton1);
		btn1.setOnClickListener(this);
		ImageButton btn2 = (ImageButton)view.findViewById(R.id.imageButton2);
		btn2.setOnClickListener(this);
		
		ImageButton btn3 = (ImageButton)view.findViewById(R.id.imageButton3);
		ImageButton btn4 = (ImageButton)view.findViewById(R.id.imageButton4);
		ImageButton btn5 = (ImageButton)view.findViewById(R.id.imageButton5);
		
//		btn5.setVisibility(ImageButton.GONE);
		btn5.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Bundle bundle = new Bundle();
//				bundle.putString("url", "http://jrjsq.chinaec.net/");
//				bundle.putString("title", "社区商圈网");
				Intent intent = new Intent();
//				intent.putExtras(bundle);
				intent.setClass(Tab2MenuFragment.this.getActivity(), HealthViewActivity.class);
				startActivity(intent);
				
			}
			
		});
		
		btn3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("url", "http://jrjsq.chinaec.net/");
				bundle.putString("title", "社区商圈网");
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(Tab2MenuFragment.this.getActivity(), WebViewActivity.class);
				startActivity(intent);
				
			}
			
		});
		
		btn4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("url", "http://www3.bjxch.gov.cn/jsp/theme/index.jsp");
				bundle.putString("title", "社区服务网");
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(Tab2MenuFragment.this.getActivity(), WebViewActivity.class);
				startActivity(intent);
				
			}
			
		});
		
		return view;
	}

	private AnnonceViewFragment mFragAnnonce;
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()){
		case R.id.imageButton1:
			//mFragAnnonce = new AnnonceViewFragment();
			//this.changeContentFragment(mFragAnnonce);
			intent = new Intent();
			intent.setClass(this.getActivity(), AnnonceViewActivity.class);
			startActivity(intent);
			break;
		case R.id.imageButton2:
			intent = new Intent();
			intent.setClass(this.getActivity(), CommerceListActivity.class);
			intent.putExtra(Constants.KEY_ITEM_NAME, "列表");
			intent.putExtra(Constants.KEY_SUBJECT_NAME, "下架商品");
			startActivity(intent);
			break;
		}
	}
	
	public void changeContentFragment(Fragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.pf_main_content_fragment, fragment);
		ft.commitAllowingStateLoss();
	}

	public Tab2MenuFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
}
