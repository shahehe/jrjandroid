package gov.jrj.ui.map;

import gov.jrj.Log;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;

/*
 * �Զ���ͼ��
 */
public class StreetEventOverlay extends Overlay {
	private Object TAG = StreetEventOverlay.class;
    private GeoPoint geoPoint; // λ��
    Paint paint = new Paint();
    // MapView mapView;
    Context context;
    String picName; //��Ҫ��ʾ��ͼƬƽ��
    StreetEventRecord record;
    public StreetEventOverlay(StreetEventRecord record, Context context) {
        super();
		double latV = Double.valueOf(record.getLatitude());
		double longV = Double.valueOf(record.getLongitude());
		int latInt = (int) (latV * 1E6);
		int longInt = (int) (longV * 1E6);
		geoPoint = new GeoPoint(latInt, longInt);
		picName = record.getAnnotationPicture();
		this.record = record;
        this.context = context;
    }

    @Override
    public boolean onTap(GeoPoint arg0, MapView arg1) {
        double latV = arg0.getLatitudeE6()/1E6;
        double longV = arg0.getLongitudeE6()/1E6; 
        double latV2 = geoPoint.getLatitudeE6()/1E6;
        double longV2 = geoPoint.getLongitudeE6()/1E6;
        // if(geoPoint.getLatitudeE6() - latV<100 && geoPoint.getLongitudeE6() -
        // longV<100)
        // {
        double dis = MapUtil.GetDistance(latV, longV, latV2, longV2); 
        Log.d(TAG, String.valueOf(dis));
        Log.d(TAG, "TAP");
        Log.d(TAG, latV + " " + longV);
        Log.d(TAG, latV2 + " " + longV2);   
        if(dis<200){
            Toast.makeText(context.getApplicationContext(), record.getTitleDesc(), Toast.LENGTH_SHORT).show();  
        }
        return true;
    }
    
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        Resources res = context.getResources();
        picName = picName.replace(".png", "");
        int picResId = res.getIdentifier(picName, "drawable", context.getPackageName());
        Drawable marker = res.getDrawable(picResId);
        marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
				.getIntrinsicHeight());
        Point point = mapView.getProjection().toPixels(geoPoint, null);
        int width = marker.getIntrinsicWidth();
        int height = marker.getIntrinsicHeight();
        Overlay.drawAt(canvas, marker, point.x-width/2, point.y-height/2, false);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent arg0, MapView arg1) {
    	return super.onTouchEvent(arg0, arg1);
    }
    
    // @Override
    // public void draw(Canvas canvas, MapView mapView, boolean shadow) {
    // //���찲�ŵ�λ�û���һ��String
    // Resources r = context.getResources();
    // //�����ڴ��е�һ��ͼƬ
    // Bitmap bitmap = BitmapFactory.decodeResource(r,R.drawable.east_1st);
    // Point point = mapView.getProjection().toPixels(this.geoPoint, null);
    // // canvas.drawText("���������찲��", point.x, point.y, paint);
    // Paint paint = new Paint();
    // canvas.drawBitmap(bitmap,point.x,point.y, paint);
    // }
    
    // @Override
    // public boolean onTap(GeoPoint point, MapView mapview) {
    // // TODO Auto-generated method stub
    // String title = this.record.getTitleDesc();
    // Log.d(Log.APP_TAG, this.toString());
    // Log.d(Log.APP_TAG, title);
    // return super.onTap(point, mapview);
    // }
    
}
