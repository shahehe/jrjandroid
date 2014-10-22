package gov.jrj.ui;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import gov.jrj.R;
import gov.jrj.ui.util.Constants;

public class yuxiang extends Activity implements OnClickListener{
	String mTitle;
	String mContentFileName;

/*	
	TextView mTextTitle;
	TextView nTextTitle;
	TextView nTextView;
	Button btnBack;
*/
	TextView mTextTitle;
	TextView mTextSubject;
	TextView mContent;
	TextView mDate;
	Button btnBack;
	private String title = "";
	private String subject = "";
	private String context = "";	
	String names = "西城区金融街街道办事处";
	private ImageButton navigationButton;
	private String endPosition = null;
	private String endName = null;
	private double latitude = Constants.DEFAULT_LAT;
	private double longitude = Constants.DEFAULT_LON;
	
	private LocationClient mLocClient;
	private MyLocationListener listener = new MyLocationListener();
	
	private boolean mIsEngineInitSuccess = false;
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {  
        public void engineInitSuccess() {
            //导航初始化是异步的，需要一小段时间，以这个标志来识别引擎是否初始化成功，为true时候才能发起导航  
            mIsEngineInitSuccess = true;  
        }  
        public void engineInitStart(){}
        public void engineInitFail(){}  
    };  
	private String getSdcardDir() {  
	    if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {  
	        return Environment.getExternalStorageDirectory().toString();  
	    }  
	    return null;  
	 }
	
	@Override  
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(), mNaviEngineInitListener, "2vp9lUI08VpxCeHUu2GdS8vZ",null);
		initLocation();
		endPosition = getIntent().getStringExtra("position");
		endName = getIntent().getStringExtra("name");
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yuxiang);
		mTextTitle = (TextView) findViewById(R.id.txt_title);
		mTextSubject = (TextView) findViewById(R.id.detail_txt_subject);
		mContent = (TextView) findViewById(R.id.detail_txt_content);
		btnBack = (Button) findViewById(R.id.detail_back_btn);
		WebView webView = (WebView)this.findViewById(R.id.webView1);
		navigationButton = (ImageButton) findViewById(R.id.navigationBtn);
	    //String url = "file:///android_asset/11.html";
		String url = "file:///android_asset/" + getIntent().getExtras().getString("buildingId")+ ".html";
		webView.loadUrl(url);
		btnBack.setOnClickListener(new OnClickListener() {
		WebView webView = (WebView)findViewById(R.id.webView1);
			@Override
			public void onClick(View arg0) {
				yuxiang.this.finish();
			}
		});
		setTitle();
		navigationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//116.36677,39.914123
				String[] endPositionTemp = endPosition.split(",");
				if(endPositionTemp.length!=0){
					try {
						launchNavigator2(longitude,latitude,"",Double.parseDouble(endPositionTemp[0]), Double.parseDouble(endPositionTemp[1]), endName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
//				launchNavigator();
			}
		});
				
	}
			
	
	/**
	 * 启动GPS导航. 前置条件：导航引擎初始化成功
	 * double startLatitude, double startLongitude, String startName, double endLatitude, double endLongitude, String endName
	 *40.05087, 116.30142
	 *
	 *double startLatitude,double startLongitude,String startName,
	 */
	private void launchNavigator(double startLatitude,double startLongitude,String startName,double endLatitude,double endLongitude,String endName){
		//这里给出一个起终点示例，实际应用中可以通过POI检索、外部POI来源等方式获取起终点坐标
		BaiduNaviManager.getInstance().launchNavigator(yuxiang.this,40.05087, 116.30142,"百度大厦",endLatitude, endLongitude,endName,
				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, 		 //算路方式
				true, 									   		 //真实导航
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, //在离线策略
				new OnStartNavigationListener() {				 //跳转监听
					@Override
					public void onJumpToNavigator(Bundle configParams) {
						Intent intent = new Intent(yuxiang.this, BNavigatorActivity.class);
						intent.putExtras(configParams);
				        startActivity(intent);
					}
					@Override
					public void onJumpToDownloader() {
			}
		});
	}
	
	/**
     * 指定导航起终点启动GPS导航.起终点可为多种类型坐标系的地理坐标。
     * 前置条件：导航引擎初始化成功
     */
    private void launchNavigator2(double startLatitude,double startLongitude,String startName,double endLatitude,double endLongitude,String endName){
        //这里给出一个起终点示例，实际应用中可以通过POI检索、外部POI来源等方式获取起终点坐标
        BNaviPoint startPoint = new BNaviPoint(startLatitude,startLongitude,startName, BNaviPoint.CoordinateType.BD09_MC);
        BNaviPoint endPoint = new BNaviPoint(endLatitude,endLongitude,endName, BNaviPoint.CoordinateType.BD09_MC);
        BaiduNaviManager.getInstance().launchNavigator(this,
                startPoint,                                      //起点（可指定坐标系）
                endPoint,                                        //终点（可指定坐标系）
                NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME,       //算路方式
                true,                                            //真实导航
                BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, //在离线策略
                new OnStartNavigationListener() {                //跳转监听
                    @Override
                    public void onJumpToNavigator(Bundle configParams) {
                        Intent intent = new Intent(yuxiang.this, BNavigatorActivity.class);
                        intent.putExtras(configParams);
                        startActivity(intent);
                    }
                    
                    @Override
                    public void onJumpToDownloader() {
                    }
                });
    }
			
	private void setTitle() {
		
		String subTitle = "", subject = "";
		subTitle = getIntent().getExtras().getString(Constants.KEY_ITEM_NAME);
		subject = getIntent().getExtras().getString(Constants.KEY_SUBJECT_NAME);
		mTextTitle.setText(subject + " > " + subTitle);
		
	}




	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.about_back_btn){
			this.finish();
		}	
	}
	
	public void initLocation(){
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
//	    option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setOpenGps(true);
	    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
	    option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
//	    option.setIsNeedAddress(true);//返回的定位结果包含地址信息
//	    option.setProdName("bctid-android-standard");
        option.disableCache(true);
	    mLocClient.setLocOption(option);
		mLocClient.start();
		mLocClient.requestLocation();
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("time : ");
//			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
//			sb.append("\naddr : ");
//			sb.append(location.getAddrStr());
//			if(location.getAddrStr()==null){
////				localAddress.setText("暂无位置信息");
//			}else {
////				localAddress.setText(location.getAddrStr()+"");
////				local.setText();
//				System.out.println(location.getLongitude()+","+location.getLatitude());
//				latitude = location.getLatitude();
//				longitude = location.getLongitude();
//				mLocClient.stop();
//			}
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			mLocClient.stop();
//			Log.i("=================", sb.toString());
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
}