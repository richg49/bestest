package hu.promarkvf.besttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class AugmentedView extends View {

	public AugmentedView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(0x44FF0000);
		int winX = 300;
		int winY = 300;
		int maxX = canvas.getWidth();
		int maxY = canvas.getHeight();
		int x0 = (maxX - winX)/2; 
		int y0 = (maxY - winY)/2; 
		int x1 = x0 + winX; 
		int y1 = y0 + winY; 
        canvas.drawRect(x0, y0, x1, y1, mPaint );
/*		mPaint.setColor(0xFFFFFFFF);

		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(0xFF44FF00);
		canvas.drawRect(0, 100, 300, 300, mPaint);
		mPaint.setColor(0xFF005500);
		canvas.drawRect(50, 150, 50, 150, mPaint);
		mPaint.setColor(0xFFFFFFFF);
		mPaint.setTextSize(26);
		String s = "H:" + String.valueOf(canvas.getHeight()) + "  W:" +String.valueOf(canvas.getWidth());
		canvas.drawText(s, 20, 130, mPaint);
		mPaint.setTextSize(22);
		canvas.drawText(new Date(System.currentTimeMillis()).toString(), 20, 160, mPaint);
*/	}
}
