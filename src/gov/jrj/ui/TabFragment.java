package gov.jrj.ui;

import gov.jrj.R;
import gov.jrj.ui.util.Constants;
import gov.jrj.ui.util.Util;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TabFragment extends Fragment {

	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private View mParentView;
	private RadioGroup mBottomRadioGroup;
	private RadioButton mGoverInfo;
	private RadioButton mAnnoounce;
	private RadioButton mFeedback;
	private RadioButton mMyCenter;
	private Fragment mContentFragment;
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;

	private AnnonceViewFragment mFragAnnonce;
	private FeedbackFragment mFragFeedback;
	private GovInfoFragment mFragGoverInfo;
	private LoginFragment mLoginFragment;
	private RegisterFragment mRegisterFragment;
	private MyCenterFragment mMyCenterFragment;
	private MessageFragment mMessageFragment;
	private Tab2MenuFragment mTab2MenuFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mLayoutInflater = getLayoutInflater(savedInstanceState);
		return inflater.inflate(R.layout.main_tab_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mParentView = view;
		mBottomRadioGroup = (RadioGroup) view
				.findViewById(R.id.main_bottom_radiogroup);
		mGoverInfo = (RadioButton) view.findViewById(R.id.tab1);
		mAnnoounce = (RadioButton) view.findViewById(R.id.tab2);
		mFeedback = (RadioButton) view.findViewById(R.id.tab3);
		mMyCenter = (RadioButton) view.findViewById(R.id.tab4);

		mFragAnnonce = new AnnonceViewFragment();
		if (!Util.isSessionValiad(getActivity())) {
			mLoginFragment = new LoginFragment(mHandler);
			mRegisterFragment = new RegisterFragment(mHandler);
		} else {
			mMyCenterFragment = new MyCenterFragment(mHandler);
		}
		mFragGoverInfo = new GovInfoFragment(false);
		mFragFeedback = new FeedbackFragment();

		// 新加的mtab2
		mTab2MenuFragment = new Tab2MenuFragment();
		mContentFragment = getFragmentManager().findFragmentById(
				R.id.pf_main_content_fragment);
		int messageType = this.getActivity().getIntent().getIntExtra("type", 0);

		if (messageType == 1) {
			mBottomRadioGroup.check(R.id.tab4);
			if (!Util.isSessionValiad(getActivity())) {
				mContentFragment = (mLoginFragment == null ? new LoginFragment(
						mHandler) : mLoginFragment);
			} else {
				mContentFragment = (mMyCenterFragment == null ? new MyCenterFragment(
						mHandler) : mMyCenterFragment);
			}
		} else {
			mBottomRadioGroup.check(R.id.tab1);
			mContentFragment = mFragGoverInfo;
		}
		mBottomRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.pf_main_content_fragment, mContentFragment);
		ft.commit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public interface ChangeRadioTab {
		public void changeTab();
	}

	// 此方法在onClick之前触发
	RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.tab1:
				mContentFragment = (mFragGoverInfo == null ? new GovInfoFragment(
						false) : mFragGoverInfo);
				break;
			case R.id.tab2:
				// mContentFragment = mFragAnnonce;
				mContentFragment = mTab2MenuFragment == null ? new Tab2MenuFragment()
						: mTab2MenuFragment;
				break;
			case R.id.tab3:
				mContentFragment = mFragFeedback == null ? new FeedbackFragment()
						: mFragFeedback;
				break;
			case R.id.tab4:
				if (!Util.isSessionValiad(getActivity())) {
					mContentFragment = (mLoginFragment == null ? new LoginFragment(
							mHandler) : mLoginFragment);
				} else {
					mContentFragment = (mMyCenterFragment == null ? new MyCenterFragment(
							mHandler) : mMyCenterFragment);
				}
				break;
			default:
				break;
			}
			changeContentFragment();

		}
	};

	/*
	 * 改变内容Fragment，切换不同的列表内容
	 */
	void changeContentFragment() {
//		if (!mContentFragment.isAdded()) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			try {
//				ft.remove(mContentFragment);
				Fragment fragment =getFragmentManager().findFragmentById(R.id.pf_main_content_fragment);
				ft.remove(fragment);
				ft.replace(R.id.pf_main_content_fragment, mContentFragment);
				 ft.addToBackStack(null);
//				ft.commitAllowingStateLoss();
				ft.commit();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
//		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constants.WHAT_LOGIN:
				mContentFragment = (mMyCenterFragment == null ? new MyCenterFragment(
						mHandler) : mMyCenterFragment);
				changeContentFragment();
				break;
			case Constants.WHAT_LOGOUT:
				mContentFragment = (mLoginFragment == null ? new LoginFragment(
						mHandler) : mLoginFragment);
				changeContentFragment();
				break;

			case Constants.WHAT_REGISTER:
				mContentFragment = (mRegisterFragment == null ? new RegisterFragment(
						mHandler) : mRegisterFragment);
				changeContentFragment();
				break;
			}
		}
	};

	public TabFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

}
