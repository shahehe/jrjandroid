package gov.jrj.ui.map;

import java.util.ArrayList;
import java.util.List;

import gov.jrj.AppEx;
import gov.jrj.Log;
import gov.jrj.R;
import gov.jrj.ui.util.Constants;
import gov.jrj.ui.views.TouchMapView;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.OverlayItem;

public class MapViewActivity extends MapActivity {
	private static final String TAG = MapViewActivity.class.getSimpleName();
	private TouchMapView mapView;
	private GeoPoint centerPoint;
	private AppEx app;
	// createʱע���listener��Destroyʱ��ҪRemove
	private ProgressDialog dialog;
	private boolean isErr;
	private CurrentPositionOverlay myLocOverlay;
	private LocationManager locationManager;
	// ��ǰ�û��ľ�γ��
	private double latitude = Constants.DEFAULT_LAT;
	private double longitude = Constants.DEFAULT_LON;
	// Ĭ�ϵĵ�ͼ�����ż���ȡֵ��Χ��[3,18]
	private static final int defaultZoomLevel = 15;
	private boolean first = false;
	private Button btnBack;
	private Spinner spinner;
	private Spinner spinnerOverlay;
	MKSearch mSearch = null;
	private int currentView = 0; // 0 ��ͨ��ͼ�� 1 ������ͼ
	private int currentOverlay = 0; // 0 û�У� 1 ItemizdOverlay 2 Overlay
	private GestureDetector gestureScanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		first = true;
		// ע��android�Ķ�λ�¼�
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1000, 0, locationCallback);

		setContentView(R.layout.mapview);
		btnBack = (Button) findViewById(R.id.map_back_btn);
		spinner = (Spinner) findViewById(R.id.spinner_switch_view);
		spinnerOverlay = (Spinner) findViewById(R.id.spinner_display_ovelay);
		spinnerOverlay.setVisibility(View.GONE);
		initViews();

		app = (AppEx) this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(AppEx.KEY, new AppEx.MyGeneralListener());
		}
		app.mBMapMan.start();
		// ���ʹ�õ�ͼSDK�����ʼ����ͼActivity
		super.initMapActivity(app.mBMapMan);

		this.mapView = (TouchMapView) findViewById(R.id.bmapView);
		this.mapView.setBuiltInZoomControls(true);
		mapView.setDrawOverlayWhenZooming(true);
		// mapView.setTraffic(true);
		// mapView.setSatellite(true);
//		mapView.getOverlays().add(myLocOverlay);
		Drawable marker = getResources().getDrawable(R.drawable.da_marker_red);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		myLocOverlay = new CurrentPositionOverlay(marker, this);
		Bundle bundle = getIntent().getExtras();
		


		mapView.getOverlays().add(myLocOverlay);
		mapView.setCurrPosOverlay(myLocOverlay);
		try{
			System.out.print(bundle.getDouble("Lat"));
			myLocOverlay.setmLat1(bundle.getDouble("Lat") * 1E6);
			myLocOverlay.setmLon1(bundle.getDouble("Lon") * 1E6);
			myLocOverlay.update();
			mapView.refreshDrawableState();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// �����ϴε�λ��
		// addCachedLocationToMap();
		//
		ensureMyLocationOverlayWorks();
//		if (mapView.getOverlays() != null) {
//			mapView.getOverlays().add(myLocOverlay);
//		}

//		dialog = new ProgressDialog(this);
//		dialog.setMessage(getString(R.string.locating));
//		dialog.show();
	}

	// ��λ�¼�����
//	private LocationListener mLocationListener = new LocationListener() {
//
//		@Override
//		public void onLocationChanged(Location location) {
//			Log.d("onLocationChanged");
//			if (location != null) {
//				app.setNewLocation(location); // Ӧ�ü��������µ�λ��
//				latitude = location.getLatitude();
//				longitude = location.getLongitude();
//				if (first) {
//					if (Log.NO_DATA) { // ��� �ж�λ�ź� û�е�ͼ��ݵ�����
//						Log.d("No data set default location");
//						setDefaultLocation();
//					} else {
//						setMapCenter(location, true);
//					}
//					first = false;
//				}
//				if (Log.NO_DATA) {
//					latitude = Constants.DEFAULT_LAT;
//					longitude = Constants.DEFAULT_LON;
//				}
//				ensureMyLocationOverlayWorks();
//				// Log.d("mylocationoverlay",
//				// getGeoPointFormat(myLocOverlay.getMyLocation()));
//			} else {
//				Log.d(TAG, "get location error");
//			}
//			if (dialog != null)
//				dialog.dismiss();
//		}
//
//	};

	private void ensureMyLocationOverlayWorks() {
		if (myLocOverlay == null) {
			Drawable marker = getResources().getDrawable(
					R.drawable.da_marker_red);

			myLocOverlay = new CurrentPositionOverlay(marker, this);
		}
	}

	protected void addCachedLocationToMap() {
		Location currentLocation = app.getNewLocation();
		if (currentLocation != null) { // ��ȡӦ�û���
			Log.d("appCachedLocation", getLocationFormat(currentLocation));
			setMapCenter(currentLocation, false);
			return;
		}

		Location lastLocation = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (lastLocation != null) {
			Log.d("lastLocation", getLocationFormat(lastLocation));
			// Wgs84��׼��� ת��Ϊ�ٶ����
			GeoPoint newPoint = Convert.locationToGeoPoint(lastLocation);
			GeoPoint newBaidu = Convert.fixGeoPoint(newPoint);
			setMapCenter(newBaidu, false);
			// Log.d("lastLocation baidu", getGeoPointFormat(newBaidu));
			return;
		}

		// Ϊ����ʾ���㣬 �����ڴ˱���ض��ľ�γ�ȵ�ַ, ��ʾʱ��Log.PRIVIEW����Ϊtrue
		// �޸�Ĭ�ϵĵ�ַ��γ��
		// ���û�ж�λ�ź�
		if (Log.PRIVIEW) {
			setDefaultLocation();
		}

	}

	private void setDefaultLocation() {
		latitude = Constants.DEFAULT_LAT;
		longitude = Constants.DEFAULT_LON;
		GeoPoint point = new GeoPoint((int) (latitude * 1E6),
				(int) (longitude * 1E6));
		// GeoPoint newBaidu = Convert.fixGeoPoint(point);
		setMapCenter(point, false);
		Log.d("setDefaultLocation", getGeoPointFormat(point));
	}

	private void setMapCenter(Location location, boolean animate) {
		GeoPoint myPoint = Convert.locationToGeoPoint(location);
		setMapCenter(myPoint, animate);

	}

	private void setMapCenter(GeoPoint myPoint, boolean animate) {
		// Log.d("setMapCenter", getGeoPointFormat(myPoint));
		centerPoint = myPoint;
		MapController mMapController = mapView.getController();
		mMapController.setZoom(defaultZoomLevel);
		if (animate) {
			mMapController.animateTo(myPoint);
		} else {
			mMapController.setCenter(myPoint);
		}
	}

	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			if (mapView.getPt() != null) {
				latitude = mapView.getPt().getLatitudeE6() * 0.000001;
				longitude = mapView.getPt().getLongitudeE6() * 0.000001;
			}
			bundle.putDouble(Constants.KEY_LATITUDE, latitude);
			bundle.putDouble(Constants.KEY_LONGITUDE, longitude);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
		}
	};

	@Override
	protected void onPause() {
		app.mBMapMan.stop();
//		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		// if (myLocOverlay == null) {
		// myLocOverlay.disableMyLocation();
		// myLocOverlay.disableCompass();
		// }
		this.mapView.getOverlays().clear();
		super.onPause();
	}

	@Override
	protected void onResume() {
		isErr = false;
		app = (AppEx) this.getApplication();
//		app.mBMapMan.getLocationManager().requestLocationUpdates(
//				mLocationListener);
		// app.mBMapMan.getLocationManager().enableProvider(
		// MKLocationManager.MK_NETWORK_PROVIDER);
		app.mBMapMan.getLocationManager().enableProvider(
				MKLocationManager.MK_GPS_PROVIDER);
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (app.mBMapMan != null) {
			app.mBMapMan.destroy();
			app.mBMapMan = null;
		}
		super.onDestroy();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private android.location.LocationListener locationCallback = new android.location.LocationListener() {

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		}

		@Override
		public void onProviderEnabled(String arg0) {
		}

		@Override
		public void onProviderDisabled(String arg0) {
		}

		@Override
		public void onLocationChanged(Location arg0) {
			Log.d("android_onLocationChanged", arg0.toString());
		}
	};

	// String util

	private String getLocationFormat(Location location) {
		return String.format(getString(R.string.current_location_f),
				location.getLatitude(), location.getLongitude());
	}

	private String getGeoPointFormat(GeoPoint point) {
		return String.format(getString(R.string.current_location_d),
				point.getLatitudeE6(), point.getLongitudeE6());
	}

	private boolean IsGPSOpen() {
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))
			return true;
		return false;
	}

	private void showOpenGPSDialog() {
		new AlertDialog.Builder(this).setTitle(R.string.title_open_gps)
				.setMessage(R.string.message_open_gps)
				.setPositiveButton(R.string.dialog_ok, dialogClickListener)
				.setNegativeButton(R.string.dialog_cancel, dialogClickListener)
				.show();
	}

	private android.content.DialogInterface.OnClickListener dialogClickListener = new android.content.DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_NEGATIVE:
				Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
				// ��Ϊ������ɺ󷵻ص���ȡ����
				startActivityForResult(intent, 0);
				break;
			case DialogInterface.BUTTON_POSITIVE:
				break;
			}
		}
	};

	private void initViews() {
		btnBack.setOnClickListener(click);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.views, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (currentView != position) {
					currentView = position;
					switch (position) {
					case 0:
						mapView.setSatellite(false);
						mapView.invalidate();
						break;
					case 1:
						mapView.setSatellite(true);
						mapView.invalidate();
						break;
					default:
						break;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		ArrayAdapter<CharSequence> adapterOverlay = ArrayAdapter
				.createFromResource(this, R.array.overlays,
						android.R.layout.simple_spinner_item);
		adapterOverlay
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerOverlay.setAdapter(adapterOverlay);
		spinnerOverlay.setSelection(0);
		spinnerOverlay
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						if (currentOverlay != position) {
							currentOverlay = position;
							switch (position) {
							case 0: // none
								if (!mapView.isEnabled()) {
									return;
								}
								if (mapView.getOverlays() == null)
									return;
								mapView.getOverlays().clear();
								ensureMyLocationOverlayWorks();
//								mapView.getOverlays().add(myLocOverlay);
								mapView.invalidate();
								break;
							case 1:// itemized
								testAddItemizedOverlay();
								break;
							case 2: // overlay
								testAddMark();
								break;
							default:
								break;
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

	}

	private void addFlagToMap(List<StreetEventRecord> lstRecord) {
		if (!mapView.isEnabled()) {
			return;
		}
		if (mapView.getOverlays() == null)
			return;
		mapView.getOverlays().clear();
		for (StreetEventRecord record : lstRecord) {
			mapView.getOverlays().add(new StreetEventOverlay(record, this));
		}

		ensureMyLocationOverlayWorks();
//		this.mapView.getOverlays().add(myLocOverlay);
	}

	private void testAddMark() {
		List<StreetEventRecord> lstRecord = new ArrayList<StreetEventRecord>();
		StreetEventRecord record = new StreetEventRecord();
		record.setAnnotationPicture("mark.png");
		record.setTitleDesc("Overlay����\r\n���ﾮ�ǻ���");
		record.setLatitude(latitude);
		record.setLongitude(longitude + 0.005);
		lstRecord.add(record);
		addFlagToMap(lstRecord);
		mapView.invalidate();
	}

	private void testAddItemizedOverlay() {
		List<OverlayItem> lstItems = new ArrayList<OverlayItem>();
		lstItems.add(new OverlayItem(Convert.toGeoPoint(latitude + 0.002,
				longitude), "P1", "Itemized point1"));
		lstItems.add(new OverlayItem(Convert.toGeoPoint(latitude + 0.004,
				longitude), "P2", "Itemized point2"));
		lstItems.add(new OverlayItem(Convert.toGeoPoint(latitude + 0.006,
				longitude), "P3", "Itemized point3"));
		if (!mapView.isEnabled()) {
			return;
		}
		if (mapView.getOverlays() == null)
			return;
		mapView.getOverlays().clear();
		Drawable marker = getResources().getDrawable(R.drawable.mark_itemized);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		mapView.getOverlays().add(
				new StreetEventItemizedOverlay(marker, this, lstItems));
		ensureMyLocationOverlayWorks();
//		this.mapView.getOverlays().add(myLocOverlay);
		mapView.invalidate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		spinnerOverlay.setSelection(currentOverlay);
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			if (mapView.getPt() != null) {
				latitude = mapView.getPt().getLatitudeE6() * 0.000001;
				longitude = mapView.getPt().getLongitudeE6() * 0.000001;
			}
			bundle.putDouble(Constants.KEY_LATITUDE, latitude);
			bundle.putDouble(Constants.KEY_LONGITUDE, longitude);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
			return false;
		}
		return false;
	}
}
