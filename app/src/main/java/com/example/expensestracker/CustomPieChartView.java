package com.example.expensestracker;

// CustomPieChartView.java

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Locale;

public class CustomPieChartView extends View {

    private List<PieData> pieDataList;
    private Paint paint;
    private RectF rectF;
    private float strokeWidth = 60f; // Adjust for ring thickness

    public CustomPieChartView(Context context) {
        super(context);
        init();
    }

    public CustomPieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE); // Crucial for ring shape
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        rectF = new RectF();
    }

    public void setPieData(List<PieData> data) {
        this.pieDataList = data;
        invalidate(); // Force redraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("PieChart", "onDraw() called");

        if (pieDataList == null || pieDataList.isEmpty()) {
            Log.d("PieChart", "pieDataList is null or empty");
            return;
        }

        float total = 0;
        for (PieData data : pieDataList) {
            total += data.value;
        }

        float startAngle = -90; // Start at top
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // Ensure a perfect circle
        float radius = Math.min(getWidth(), getHeight()) / 2f - strokeWidth / 2f;
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // Set paint style to STROKE before drawing arcs
        paint.setStyle(Paint.Style.STROKE);

        for (PieData data : pieDataList) {
            float sweepAngle = (data.value / total) * 360f;
            paint.setColor(data.color);
            canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);
            Log.d("PieChart", "Drawing arc: startAngle=" + startAngle + ", sweepAngle=" + sweepAngle + ", color=" + data.color);
            startAngle += sweepAngle;
        }

        // Draw center text
        String centerText = String.format(Locale.getDefault(), "$%.0f", total);
        paint.setStyle(Paint.Style.FILL); // Switch to FILL for text
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float yOffset = (fm.descent + fm.ascent) / 2;
        canvas.drawText(centerText, centerX, centerY - yOffset, paint);
    }


    public static class PieData {
        public float value;
        public int color;

        public PieData(float value, int color) {
            this.value = value;
            this.color = color;
        }
    }
}