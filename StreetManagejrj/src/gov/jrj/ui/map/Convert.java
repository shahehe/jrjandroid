package gov.jrj.ui.map;

import com.baidu.mapapi.Bounds;
import com.baidu.mapapi.CoordinateConvert;
import com.baidu.mapapi.GeoPoint;

import android.location.Location;
import android.os.Bundle;


/**
 * 
 * ��ͼ��ص�ת��������
 *
 */
public class Convert {

	public static GeoPoint toGeoPoint(double latitude,double longitude){
		int latV = (int) (latitude * 1E6);
		int longV = (int) (longitude * 1E6);
		return new GeoPoint(latV, longV);
	}
	
	public static GeoPoint locationToGeoPoint(Location location) {
		int latV = (int) (location.getLatitude() * 1E6);
		int longV = (int) (location.getLongitude() * 1E6);
		return new GeoPoint(latV, longV);
	}
	
	/**
	 * ��ͼƫ�Ƶľ���ӹ�ʱ�׼ת��Ϊ�ٶ����
	 * @param point
	 * @return
	 */
	public static GeoPoint fixGeoPoint(GeoPoint point) {
		Bundle newB = CoordinateConvert.fromWgs84ToBaidu(point);
		return  CoordinateConvert.bundleDecode(newB);
	}
}
