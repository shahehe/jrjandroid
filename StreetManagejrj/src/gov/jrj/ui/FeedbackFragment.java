package gov.jrj.ui;

import gov.jrj.Log;
import gov.jrj.R;
import gov.jrj.library.http.Alerts;
import gov.jrj.library.http.AsyncHttpClient;
import gov.jrj.library.http.Config;
import gov.jrj.library.http.JsonHttpResponseHandler;
import gov.jrj.library.http.RequestParams;
import gov.jrj.ui.map.MapViewActivity;
import gov.jrj.ui.map.Listener.MyLocationListenner;
import gov.jrj.ui.util.AudioRecorder;
import gov.jrj.ui.util.Constants;
import gov.jrj.ui.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

class RequestTask extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... uri) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try {
			response = httpclient.execute(new HttpGet(uri[0]));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			// TODO Handle problems..
		} catch (IOException e) {
			// TODO Handle problems..
		}
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		// Do anything with response..
	}
}

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class FeedbackFragment extends Fragment {

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Util.isSessionValiad(getActivity())) {
			@SuppressWarnings("unused")
			SharedPreferences prefs = getActivity().getSharedPreferences(
					Constants.KEY_SESSION_PREFS, 0);
		}
		if (!phoneId.equals(""))
			editPhoneNum.setText(phoneId);
	}

	private static final int REQUEST_CODE_MAP = 999;
	private final static int TAKE_PICTURE = 899;
	private final static int OPEN_GALLERY = 799;

	Context mContext;
	Button btnOpenMap;
	Button btnSubmit;
	private Button record;
	private Dialog dialog;
	private AudioRecorder mr;
	@SuppressWarnings("unused")
	private MediaPlayer mediaPlayer;
	private Thread recordThread;

	private static int MAX_TIME = 15; // 最长录制时间，单位秒，0为无时间限制
	private static int MIX_TIME = 1; // 最短录制时间，单位秒，0为无时间限制，建议设为1

	private static int RECORD_NO = 0; // 不在录音
	private static int RECORD_ING = 1; // 正在录音
	private static int RECODE_ED = 2; // 完成录音

	private static int RECODE_STATE = 0; // 录音的状态

	private static float recodeTime = 0.0f; // 录音的时间
	private static double voiceValue = 0.0; // 麦克风获取的音量值

	private ImageView dialog_img;
	@SuppressWarnings("unused")
	private static boolean playState = false; // 播放状态

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	// Button btnTest;
	ImageButton btnTakePhoto;
	ImageButton btnOpenGallery;
	String name;
	String phoneNum;
	EditText editDes;
	EditText editPhoneNum;
	boolean chkImage = false;
	String phoneId;
	CheckBox chkFeedback;
	public String filePath = null;
	public String uploadFilePath = null;

	private double latitude = -1.1;
	private double longitude = -1.1;
	private Uri imageUri;
	@SuppressWarnings("unused")
	private Spinner reportCategory = null;
	private boolean hasVoice = false;
	@SuppressWarnings("unused")
	private String voicePath;
	View view;

	private ProgressDialog pd;
	JsonHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(JSONObject json) {

			try {
				if (json.getInt("code") == 1) {
					Alerts.show("您上报的信息已收到，感谢您对金融街街道工作的支持！", getActivity());
					hasVoice = false;
					SharedPreferences prefs = getActivity()
							.getSharedPreferences(Constants.KEY_UID, 0);
					Editor editor = prefs.edit();
					int count = prefs.getInt(Constants.COUNT, 0);
					editor.putInt(Constants.COUNT, count + 1);
					editor.commit();
					editDes.setText("");
					LinearLayout submitScoreLayout = (LinearLayout) view
							.findViewById(R.id.image_thumb);
					submitScoreLayout.removeAllViews();
					longitude = -1.1;
					latitude = -1.1;
					// Create new LayoutInflater - this has to be done this way,
					// as you can't directly inflate an XML without creating an
					// inflater object first

					LayoutInflater inflater = FeedbackFragment.this
							.getActivity().getLayoutInflater();
					submitScoreLayout.removeView(inflater.inflate(
							R.layout.imagethumb, null));
					int uid = getActivity().getSharedPreferences(
							Constants.KEY_SESSION_PREFS, 0).getInt(
							Constants.KEY_UID, 0);
					Log.d("uid", String.valueOf(uid));
					String deviceID = prefs.getString(PushService.PREF_DEVICE_ID, "");
					new RequestTask().execute(Config.URL + "/feedback.php?uid="
							+ String.valueOf(uid) + "&deviceid="+deviceID );
					// submitScoreLayout.addView(inflater.inflate(R.layout.imagethumb,
					// null));
				}
			} catch (JSONException e) {
				Alerts.show("网络访问失败", getActivity());

			}

		}

		@Override
		public void onStart() {
			pd = ProgressDialog.show(getActivity(), null,
					getString(R.string.posting));
		}

		@Override
		public void onFinish() {
			pd.dismiss();
		}

		@Override
		public void onFailure(Throwable e) {
			Alerts.show("提交服务器无法访问", getActivity());
		}

	};

	protected void submit(boolean compress) {
		RequestParams rp = new RequestParams();
		SharedPreferences prefs = mContext.getSharedPreferences(
				Constants.KEY_SESSION_PREFS, 0);
		name = prefs.getString(Constants.KEY_USER_NAME, "");
		if (name.equals("")) {
			name = "None";
		}
		rp.put("name", name);
		phoneNum = editPhoneNum.getText().toString();
		rp.put("mobile", phoneNum);
		rp.put("address", editDes.getText().toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		rp.put("create_time", time);

		if (chkFeedback.isChecked())
			rp.put("feedback", "1");
		else
			rp.put("feedback", "0");
		rp.put("longitude", Double.toString(latitude));
		rp.put("latitude", Double.toString(longitude));
		boolean uploadImage = chkImage;
		if (!Util.isSessionValiad(getActivity())) {
			rp.put("uid", Integer.toString(0));
		} else {
			int uid = prefs.getInt(Constants.KEY_UID, 0);
			rp.put("uid", Integer.toString(uid));
		}

		if (hasVoice) {
			try {
				rp.put("hasvoice", Integer.toString(1));
				rp.put("voice",
						new File(Environment.getExternalStorageDirectory(),
								"jrj/voice.amr"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (uploadImage) {
			try {
				if (compress) {
					rp.put("haspic", Integer.toString(1));
					rp.put("file", new File(uploadFilePath));
				} else {
					rp.put("haspic", Integer.toString(1));
					rp.put("file", new File(filePath));
				}

			} catch (Exception ex) {
				uploadImage = false;
				Alerts.show("网络访问失败", getActivity());
			}
		}
		if (Config.isConnected(FeedbackFragment.this.getActivity())) {
			AsyncHttpClient client = new AsyncHttpClient();
//			if (uploadImage) {
//				client.post(Config.URL + "/server1.php", rp, mHandler);
//				// client.post("192.168.2.101/server1.php", rp, mHandler);
//			} else {
//				client.post(Config.URL + "/sni1.php", rp, mHandler);
//				// client.post("192.168.2.101/sni1.php", rp, mHandler);
//
//			}
			client.post(Config.URL + "/receiveMessage.php", rp,mHandler);
			} else {
			Toast.makeText(FeedbackFragment.this.getActivity(), "没有数据信号",
					Toast.LENGTH_SHORT).show();
		}

	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("您没有选择当前地理位置,是否继续提交? ");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						submit(true);
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.setNeutralButton("位置", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				Intent intent = new Intent(getActivity(), MapViewActivity.class);
				Bundle bundle = new Bundle();
				if (longitude > 0 && latitude > 0) {
					bundle.putDouble("Lon", longitude);
					bundle.putDouble("Lat", latitude);
				}
				intent.putExtras(bundle);
				startActivityForResult(intent, REQUEST_CODE_MAP);

				// dialog.cancel();

			}
		});
		builder.create().show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		view = inflater.inflate(R.layout.feedback_fragment, container, false);
		mContext = getActivity();
		voicePath = Environment.getExternalStorageState() + File.separator
				+ "jrj" + File.separator + "voice.amr";
		btnOpenMap = (Button) view.findViewById(R.id.btn_map);
		btnTakePhoto = (ImageButton) view.findViewById(R.id.btn_camera);
		btnOpenGallery = (ImageButton) view.findViewById(R.id.btn_gallery);
		// reportCategory = (Spinner) view.findViewById(R.id.categorySpinner);
		btnSubmit = (Button) view.findViewById(R.id.putin);
		// btnTest = (Button) view.findViewById(R.id.button1);
		// btnTest.setVisibility( View.GONE);
		// editName = (EditText) view.findViewById(R.id.contactedittext);
		if (Util.isSessionValiad(getActivity())) {
			@SuppressWarnings("unused")
			SharedPreferences prefs = getActivity().getSharedPreferences(
					Constants.KEY_SESSION_PREFS, 0);
			// editName.setText(prefs.getString(Constants.KEY_USER_NAME, ""));
		}
		editPhoneNum = (EditText) view.findViewById(R.id.phoneNumber);
		// 创建电话管理
		TelephonyManager tm = (TelephonyManager) getActivity()
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 获取手机号码
		phoneId = tm.getLine1Number();
		if (phoneId != null)
			editPhoneNum.setText(phoneId);

		editDes = (EditText) view.findViewById(R.id.describeedittext);
		// chkImage = (CheckBox) view.findViewById(R.id.chkbox_image);
		chkFeedback = (CheckBox) view.findViewById(R.id.chkbox_feedback);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(editDes.getText())) {
					Toast.makeText(getActivity(), "监督描述不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (editPhoneNum.getText().toString().equals("")) {
					Toast.makeText(getActivity(), "电话号不能为空", Toast.LENGTH_SHORT)
							.show();
					return;

				} else if (longitude < 0 || latitude < 0)
					FeedbackFragment.this.dialog();
				else
					submit(true);

			}
		});
		// btnTest.setOnClickListener(new OnClickListener() {
		//
		//
		// public void onClick(View v) {
		//
		// if(TextUtils.isEmpty(editDes.getText())){
		// Toast.makeText(getActivity(), "监督描述不能为空", Toast.LENGTH_SHORT).show();
		// return;
		// }else{
		//
		// if ( longitude < 0 || latitude < 0)
		// FeedbackFragment.this.dialog();
		// else
		// submit(false);
		//
		// }
		// }
		// });

		btnOpenMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MapViewActivity.class);
				Bundle bundle = new Bundle();
				if (longitude > 0 && latitude > 0) {
					bundle.putDouble("Lon", longitude);
					bundle.putDouble("Lat", latitude);
				}
				intent.putExtras(bundle);
				startActivityForResult(intent, REQUEST_CODE_MAP);
			}
		});

		btnTakePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				File photo = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "test.jpg");
				// File photo = new File(_path);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
				imageUri = Uri.fromFile(photo);
				startActivityForResult(intent, TAKE_PICTURE);

			}
		});

		btnOpenGallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Choose Picture"),
						OPEN_GALLERY);
			}
		});

		record = (Button) view.findViewById(R.id.record);
		record.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (RECODE_STATE != RECORD_ING) {
						String sdState = Environment.getExternalStorageState();// 获得sd卡的状态
						if (!sdState.equals(Environment.MEDIA_MOUNTED)) { // 判断SD卡是否存在
							// 提示sd卡不存在
							Toast.makeText(mContext, "请插入SD卡",
								     Toast.LENGTH_SHORT).show();
							return false;
						}
						scanOldFile();
						mr = new AudioRecorder("voice");
						RECODE_STATE = RECORD_ING;
						showVoiceDialog();
						try {
							mr.start();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mythread();
					}
					{
						Resources rs = getResources();
						Drawable dw = rs
								.getDrawable(R.drawable.buttonbackground_down);
						v.setBackgroundDrawable(dw);
					}
					break;
				case MotionEvent.ACTION_UP:
					if (RECODE_STATE == RECORD_ING) {
						RECODE_STATE = RECODE_ED;
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						try {
							mr.stop();
							voiceValue = 0.0;
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (recodeTime < MIX_TIME) {
							showWarnToast();
							record.setText("按住开始录音");
							RECODE_STATE = RECORD_NO;
						} else {
							// TO-DO
							record.setText("录音完成!点击重新录音");
							hasVoice = true;
						}
					}
					{
						Resources rs = getResources();
						Drawable dw = rs
								.getDrawable(R.drawable.buttonbackground_up);
						v.setBackgroundDrawable(dw);
					}
					break;
				}
				return false;
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		myListener = new MyLocationListenner(FeedbackFragment.this);
		mLocationClient = new LocationClient(mContext); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(0);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(1); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(false); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else
			Log.d("LocSDK3", "locClient is null or not started");
	}

	// 删除老文件
	void scanOldFile() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"jrj/voice.amr");
		if (file.exists()) {
			file.delete();
		}
	}

	// 录音时显示Dialog
	void showVoiceDialog() {
		dialog = new Dialog(mContext, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.my_dialog);
		dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
		dialog.show();
	}

	// 录音时间太短时Toast显示
	void showWarnToast() {
		Toast toast = new Toast(mContext);
		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setPadding(20, 20, 20, 20);

		// 定义一个ImageView
		ImageView imageView = new ImageView(mContext);
		imageView.setImageResource(R.drawable.voice_to_short); // 图标

		TextView mTv = new TextView(mContext);
		mTv.setGravity(0x01);
		mTv.setText("时间太短   录音失败");
		mTv.setTextSize(14);
		mTv.setTextColor(Color.WHITE);// 字体颜色
		// mTv.setPadding(0, 10, 0, 0);

		// 将ImageView和ToastView合并到Layout中
		linearLayout.addView(imageView);
		linearLayout.addView(mTv);
		linearLayout.setGravity(Gravity.CENTER);// 内容居中
		linearLayout.setBackgroundResource(R.drawable.record_bg);// 设置自定义toast的背景

		toast.setView(linearLayout);
		toast.setGravity(Gravity.CENTER, 0, 0);// 起点位置为中间 100为向下移100dp
		toast.show();
	}

	// 获取文件手机路径
	@SuppressWarnings("unused")
	private String getAmrPath() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"jrj/voice.amr");
		return file.getAbsolutePath();
	}

	// 录音计时线程
	void mythread() {
		recordThread = new Thread(ImgThread);
		recordThread.start();
	}

	// 录音Dialog图片随声音大小切换
	void setDialogImage() {
		if (voiceValue < 200.0) {
			dialog_img.setImageResource(R.drawable.record_animate_01);
		} else if (voiceValue > 200.0 && voiceValue < 400) {
			dialog_img.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue > 400.0 && voiceValue < 800) {
			dialog_img.setImageResource(R.drawable.record_animate_03);
		} else if (voiceValue > 800.0 && voiceValue < 1600) {
			dialog_img.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue > 1600.0 && voiceValue < 3200) {
			dialog_img.setImageResource(R.drawable.record_animate_05);
		} else if (voiceValue > 3200.0 && voiceValue < 5000) {
			dialog_img.setImageResource(R.drawable.record_animate_06);
		} else if (voiceValue > 5000.0 && voiceValue < 7000) {
			dialog_img.setImageResource(R.drawable.record_animate_07);
		} else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_08);
		} else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_09);
		} else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_10);
		} else if (voiceValue > 17000.0 && voiceValue < 20000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_11);
		} else if (voiceValue > 20000.0 && voiceValue < 24000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_12);
		} else if (voiceValue > 24000.0 && voiceValue < 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_13);
		} else if (voiceValue > 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_14);
		}
	}

	// 录音线程
	private Runnable ImgThread = new Runnable() {

		@Override
		public void run() {
			recodeTime = 0.0f;
			while (RECODE_STATE == RECORD_ING) {
				if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
					imgHandle.sendEmptyMessage(0);
				} else {
					try {
						Thread.sleep(200);
						recodeTime += 0.2;
						if (RECODE_STATE == RECORD_ING) {
							voiceValue = mr.getAmplitude();
							imgHandle.sendEmptyMessage(1);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		Handler imgHandle = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 0:
					// 录音超过15秒自动停止
					if (RECODE_STATE == RECORD_ING) {
						RECODE_STATE = RECODE_ED;
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						try {
							mr.stop();
							voiceValue = 0.0;
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (recodeTime < 1.0) {
							showWarnToast();
							record.setText("按住开始录音");
							RECODE_STATE = RECORD_NO;
						} else {
							record.setText("录音完成!点击重新录音");
							hasVoice = true;
						}
					}
					break;
				case 1:
					setDialogImage();
					break;
				default:
					break;
				}

			}
		};
	};

	private void addCapturePicLayout(int requestCode, Uri selectedImage) {

		LinearLayout submitScoreLayout = (LinearLayout) view
				.findViewById(R.id.image_thumb);
		submitScoreLayout.removeAllViews();
		// Create new LayoutInflater - this has to be done this way, as you
		// can't directly inflate an XML without creating an inflater object
		// first
		LayoutInflater inflater = FeedbackFragment.this.getActivity()
				.getLayoutInflater();
		submitScoreLayout.addView(inflater.inflate(R.layout.imagethumb, null));
		if (requestCode == TAKE_PICTURE) {
			filePath = Environment.getExternalStorageDirectory()
					+ File.separator + "test.jpg";
			FeedbackFragment.this.getActivity().getContentResolver()
					.notifyChange(selectedImage, null);
		} else if (requestCode == OPEN_GALLERY) {

			String[] filePathColumn = { MediaColumns.DATA };
			Cursor cursor = FeedbackFragment.this.getActivity()
					.getContentResolver()
					.query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			filePath = cursor.getString(columnIndex);
			cursor.close();
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		int maxSide = options.outWidth > options.outHeight ? options.outWidth
				: options.outHeight;
		int ratio = (int) ((double) maxSide / 400 + 0.5);

		options.inSampleSize = ratio;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filePath, options);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageViewPic);
		imageView.setImageBitmap(bitmap);
		imageView.setScaleType(ScaleType.CENTER_INSIDE);
		uploadFilePath = Environment.getExternalStorageDirectory()
				+ File.separator + "upload.jpg";
		chkImage = true;
		File file2 = new File(uploadFilePath);
		try {
			FileOutputStream out = new FileOutputStream(file2);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			Alerts.show("can not create uploaded file", getActivity());
			// TODO: handle exception
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_MAP) {
			switch (resultCode) {
			case Activity.RESULT_OK:
				latitude = data.getDoubleExtra(Constants.KEY_LATITUDE, 0.0d);
				longitude = data.getDoubleExtra(Constants.KEY_LONGITUDE, 0.0d);
				Log.d("onActivityResult", "latitude=" + latitude
						+ ", longitude=" + longitude);
				if (Log.DBG) {
					Toast.makeText(
							getActivity(),
							"latitude=" + latitude + ", longitude=" + longitude,
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
		} else if (requestCode == TAKE_PICTURE) {
			switch (resultCode) {
			case Activity.RESULT_OK:
				addCapturePicLayout(requestCode, imageUri);
				break;
			}
		} else if (requestCode == OPEN_GALLERY) {

			switch (resultCode) {
			case Activity.RESULT_OK:
				addCapturePicLayout(OPEN_GALLERY, data.getData());
				break;
			}

		}
	}

	public FeedbackFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
}
