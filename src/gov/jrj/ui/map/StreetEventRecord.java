package gov.jrj.ui.map;

/*
 * ÿ���ֵ��¼���¼���࣬�����Ķ�����ע����ͼ�ϵ�ͼ���ж�Ӧ��ϵ
 * 
 */
public class StreetEventRecord {
	private double latitude; // ά��
	private double longitude;// ����
	private String titleDesc;// ����
	private String annotationPicture;// ��ע��ͼƬ���
	
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
	public String getTitleDesc() {
		return titleDesc;
	}
	public void setTitleDesc(String titleDesc) {
		this.titleDesc = titleDesc;
	}
	public String getAnnotationPicture() {
		return annotationPicture;
	}
	public void setAnnotationPicture(String annotationPicture) {
		this.annotationPicture = annotationPicture;
	}
	
	@Override
	public String toString()
	{
		return " latitude:"+latitude+" longitude:"+longitude+" titleDesc:"+titleDesc +  " annotationPicture:" + annotationPicture;
	}
}
