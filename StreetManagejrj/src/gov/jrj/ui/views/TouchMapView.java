package gov.jrj.ui.views;

import java.util.ArrayList;
import java.util.List;

import gov.jrj.R;
import gov.jrj.ui.map.CurrentPositionOverlay;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;

public class TouchMapView extends MapView {

	CurrentPositionOverlay currPosOverlay;
	GeoPoint pt;
	Context context;
	
	int startX;
	int startY;
	int endX;
	int endY;

	public CurrentPositionOverlay getCurrPosOverlay() {
		return currPosOverlay;
	}

	public void setCurrPosOverlay(CurrentPositionOverlay currPosOverlay) {
		this.currPosOverlay = currPosOverlay;
	}

	public GeoPoint getPt() {
		return pt;
	}

	public void setPt(GeoPoint pt) {
		this.pt = pt;
	}

	public TouchMapView(Context arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public TouchMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public TouchMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub

		if (arg0.getAction() == MotionEvent.ACTION_UP) {
			endX = (int) arg0.getX();
			endY = (int) arg0.getY();
			if(Math.abs((endX-startX) + (endY - startY)) == 0){
			pt = this.getProjection().fromPixels(endX,endY);
			currPosOverlay.setmLat1(pt.getLatitudeE6());
			currPosOverlay.setmLon1(pt.getLongitudeE6());
			currPosOverlay.update();
			System.out.println(pt.getLatitudeE6());
			System.out.println(pt.getLongitudeE6());
			this.refreshDrawableState();}
		} else if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			startX = (int) arg0.getX();
			startY = (int) arg0.getY();
			
		}
		return super.onTouchEvent(arg0);

	}
}
