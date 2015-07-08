package com.icedtea.wps2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
    private Paint paint = new Paint();
    private int x = 0, y = 0;

	public DrawView(Context context) {
        super(context);
        paint.setColor(Color.GREEN);
    }

    @Override
    public void onDraw(Canvas canvas) {
    	if(x == 0 && y == 0) return;
    	
        canvas.drawCircle(x, y, 4, paint);
    }
    
    public void updatePosition(int x, int y) {
    	this.x = x;
    	this.y = y;
    	invalidate();
    }
}