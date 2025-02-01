package com.example.expensestracker.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {
    private Paint paint;
    private RectF rectF;
    private float[] data = {60f, 40f}; // Example data (Expense, Income)
    private int[] colors = {Color.parseColor("#81BB7B"), Color.parseColor("#D3D3D3")}; // Green, Light Gray

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
    }

    public void setData(float[] data) {
        this.data = data;
        invalidate(); // Request a redraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(centerX, centerY) * 0.8f; // Adjust radius as needed

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        float startAngle = 0;
        for (int i = 0; i < data.length; i++) {
            float sweepAngle = (data[i] / 100f) * 360f;
            paint.setColor(colors[i]);
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            startAngle += sweepAngle;
        }

        // Draw a white circle in the middle (optional) to make it look like a donut chart
        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, radius * 0.5f, paint); // Adjust inner radius as needed
    }
}