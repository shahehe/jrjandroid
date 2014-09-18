package gov.jrj.ui.map;

public class MapUtil {

	/*
	 * 地球半径
	 */
    private static final double EARTH_RADIUS = 6378.137;

    /*
     * 化作弧度单位
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两站点的经纬度求两站点间的距离,弧度距离
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        //s = Math.round(s * 10000) / 10000;
        s = s*1000;
        return s;
    }

    /**
     * 根据两站点的经纬度求两站点间的距离
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double D_jw(double wd1, double jd1, double wd2, double jd2) {
        double x, y, out;
        double PI = 3.14159265;
        double R = 6.371229 * 1e6;

        x = (jd2 - jd1) * PI * R * Math.cos(((wd1 + wd2) / 2) * PI / 180) / 180;
        y = (wd2 - wd1) * PI * R / 180;
        out = Math.hypot(x, y);
        return out / 1000;
    }

}
