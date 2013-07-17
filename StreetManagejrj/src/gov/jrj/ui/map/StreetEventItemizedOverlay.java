package gov.jrj.ui.map;

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

public class StreetEventItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
	private Drawable marker;
	private Context mContext;

	public StreetEventItemizedOverlay(Drawable marker, Context context,
			List<OverlayItem> list) {
		super(boundCenterBottom(marker));

		this.marker = marker;
		this.mContext = context;
		GeoList = list;
		populate(); 
	}

	public void updateOverlay()
	{
		populate();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection(); 
		for (int index = size() - 1; index >= 0; index--) {
			OverlayItem overLayItem = getItem(index); 
			String title = overLayItem.getTitle();
			Point point = projection.toPixels(overLayItem.getPoint(), null); 
			Paint paintText = new Paint();
			paintText.setColor(Color.BLUE);
			paintText.setTextSize(24);
			paintText.setFakeBoldText(true);
			canvas.drawText(title, point.x-30, point.y, paintText); 
		}

		super.draw(canvas, mapView, shadow);
		boundCenterBottom(marker);
	}
	
	@Override
	protected boolean onTap(int i) {
		Toast.makeText(this.mContext, GeoList.get(i).getPoint().toString(),
				Toast.LENGTH_SHORT).show();
		return true;
	}
	
	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		return super.onTap(arg0, arg1);
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return GeoList.get(i);
	}

	@Override
	public int size() {
		return GeoList.size();
	}
	
}