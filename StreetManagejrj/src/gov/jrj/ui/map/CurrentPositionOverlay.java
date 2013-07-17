package gov.jrj.ui.map;

import gov.jrj.ui.views.TouchMapView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;

public class CurrentPositionOverlay extends ItemizedOverlay<OverlayItem> {
	private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
	private TouchMapView mContext;

	private double mLat1 = 116365768;
	private double mLon1 = 39998742;
    private Drawable marker;  

	public double getmLat1() {
		return mLat1;
	}

	public void setmLat1(double mLat1) {
		this.mLat1 = mLat1;
	}

	public double getmLon1() {
		return mLon1;
	}

	public void setmLon1(double mLon1) {
		this.mLon1 = mLon1;
	}



	public CurrentPositionOverlay(Drawable marker, TouchMapView touchMapView,GeoPoint pt) {
		// TODO Auto-generated constructor stub
		super(boundCenterBottom(marker));
		this.marker = marker;
//		this.mContext = touchMapView;
		setmLat1(pt.getLatitudeE6());
		setmLon1(pt.getLongitudeE6());
		// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
		GeoPoint p1 = new GeoPoint((int) (mLat1 ), (int) (mLon1 ));
		

		GeoList.add(new OverlayItem(p1, "P1", "point1"));
		GeoList.clear();
		populate();
	}

	public CurrentPositionOverlay(Drawable marker,
			MapViewActivity mapViewActivity) {
		// TODO Auto-generated constructor stub

		super(boundCenterBottom(marker));
		this.marker = marker;
//		this.mContext = mapViewActivity;
//		setmLat1(pt.getLatitudeE6());
//		setmLon1(pt.getLongitudeE6());
		mLat1 = mLon1 = 0;
		// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
		GeoPoint p1 = new GeoPoint((int) (mLat1 ), (int) (mLon1 ));
		GeoList.clear();
		GeoList.add(new OverlayItem(p1, "P1", "point1"));

		populate();
	}


	@Override
	protected OverlayItem createItem(int i) {
		return GeoList.get(i);
	}

	@Override
	public int size() {
		return GeoList.size();
	}

	@Override
	// 处理当点击事件
	protected boolean onTap(int i) {
//		Toast.makeText(this.mContext, GeoList.get(i).getSnippet(),
//				Toast.LENGTH_SHORT).show();
		return true;
	}
	
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {  
        // 投影，用于屏幕像素点坐标系统与地球经纬度点坐标系统的转换  
        Projection projection = mapView.getProjection();  
        for (int index = size() - 1; index >= 0; index--) {  
            OverlayItem overlayItem = this.getItem(index);  
            String title = overlayItem.getTitle();  
            Point point = projection.toPixels(overlayItem.getPoint(), null);  

            Paint painttext = new Paint();  
            painttext.setColor(Color.BLACK);  
            painttext.setTextSize(15);  
            canvas.drawText(title, point.x - 30, point.y - 25, painttext);  

        }  

        super.draw(canvas, mapView, shadow);  
        boundCenterBottom(marker);  

    }

	public void update() {
		// TODO Auto-generated method stub
		GeoPoint p1 = new GeoPoint((int) (mLat1 ), (int) (mLon1 ));
		GeoList.clear();
		GeoList.add(new OverlayItem(p1, "我的位置", "这是我的位置"));
		populate();
	}  
}
