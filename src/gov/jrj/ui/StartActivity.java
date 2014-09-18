package gov.jrj.ui;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import gov.jrj.R;
import gov.jrj.library.http.Config;
import gov.jrj.ui.views.AnimationView;
import gov.jrj.ui.views.AnimationView.IAnimationCalback;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

/**
 * 
 * �������ؽ���
 */
public class StartActivity extends Activity {

	private AnimationView mAnimationView = null;
	private DisplayMetrics displayMetrics = null;
	private Animation logoAnimation;
	private ImageView logoImageView;
	private Bitmap bitmap;
	Handler mHandler;
	RelativeLayout startRL;

	private class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

//			if (Config.isConnected(StartActivity.this)) {
//				boolean result = false;
//				try {
//					HttpGet request = new HttpGet(Config.URL);
//
//					HttpParams httpParameters = new BasicHttpParams();
//
//					HttpConnectionParams.setConnectionTimeout(httpParameters,
//							1000);
//					HttpClient httpClient = new DefaultHttpClient(
//							httpParameters);
//					HttpResponse response = httpClient.execute(request);
//
//					int status = response.getStatusLine().getStatusCode();
//
//					if (status == HttpStatus.SC_OK) {
//						result = true;
//					}
//
//				} catch (SocketTimeoutException e) {
//					result = false; // this is somewhat expected
//				}
//
//				catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if (!result)
//					Config.URL = "http://64.150.161.193/jrj";
//
//			}

			return null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_page);
		startRL = (RelativeLayout) findViewById(R.id.start_relative);
		AssetManager asm = getAssets();
		try {
			InputStream is = asm.open("startbackground.png");
			Drawable da = Drawable.createFromStream(is, null);
			startRL.setBackgroundDrawable(da);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logoImageView = (ImageView) findViewById(R.id.startImageView);
		logoImageView.setScaleType(ScaleType.FIT_XY);
		logoAnimation = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.wel_logo);
		logoImageView.startAnimation(logoAnimation);
		/*
		 * SharedPreferences sp =
		 * PreferenceManager.getDefaultSharedPreferences(this); // Obtain the
		 * sharedPreference, default to true if not available boolean
		 * isSplashEnabled = sp.getBoolean("isSplashEnabled", true); if (
		 * isSplashEnabled ) {
		 */
		mAnimationView = (AnimationView) this
				.findViewById(R.id.start_view_play);

		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		bitmap = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.startpagedotorange)).getBitmap();

		ViewGroup.MarginLayoutParams lp;
		lp = (ViewGroup.MarginLayoutParams) mAnimationView.getLayoutParams();
		lp.leftMargin = displayMetrics.widthPixels / 5;
		lp.rightMargin = displayMetrics.widthPixels / 5;
		lp.topMargin = displayMetrics.heightPixels / 20;
		lp.height = bitmap.getWidth();

		mHandler = new Handler();
		mAnimationView.setLayoutParams(lp);
		mAnimationView.width = 3 * displayMetrics.widthPixels / 5;
		mAnimationView.mHandler = mHandler;
		mAnimationView.setAnimatinoCallback(new IAnimationCalback() {

			@Override
			public void animationComplete() {

				mAnimationView.stop();

				Intent intent = new Intent(StartActivity.this,
						MainActivity.class);
				startActivity(intent);

				finish();
			}
		});
		mAnimationView.start();

		new LongOperation().execute("");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAnimationView.stop();
		bitmap.recycle();
	}

}
