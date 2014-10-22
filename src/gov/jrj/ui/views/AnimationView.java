package gov.jrj.ui.views;

import gov.jrj.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class AnimationView extends View implements Runnable{
	private Bitmap bitMap1;
	private Bitmap bitMap2;
	private int margin;
	public  int width;
	private  int numTag=0;
	private int mTimes = 0;
	public Handler mHandler = null;
	private Context context;
	private IAnimationCalback callback;
	private boolean mStopSign=false;
	public AnimationView(Context context,AttributeSet attrs) {
		
		super(context,attrs);
		this.context = context;
	
		bitMap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.startpagedotblue)).getBitmap();
		bitMap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.startpagedotorange)).getBitmap();
		margin = bitMap1.getWidth();
		
	}
	public void start(){
		mHandler.postDelayed(this, 50);
	}
	public void stop(){
		mStopSign=true;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int x = 0;
		int y = 0;
		for(int i=1;i<=width/margin;i++){
			if(i == numTag){
				canvas.drawBitmap(bitMap2, x, y, null);
				x+=margin;
			}if(i==width/margin){
				continue;//��������һ�������ⲻ��
			}
			canvas.drawBitmap(bitMap1, x, y, null);
			x +=margin;
			
		}
	}
	 
	@Override
	public void run() {
			if(numTag>=width/margin){
				numTag=0;
			}
			mTimes++;
			this.postInvalidate();
			numTag++;
			if (mTimes >= 5 && !mStopSign) {
				if(callback!=null)
					callback.animationComplete();
			}
			start();		
	}
	
	public static interface IAnimationCalback{
		void animationComplete();
	}
	
	public void setAnimatinoCallback(IAnimationCalback callback) {
		this.callback = callback;
	}
}