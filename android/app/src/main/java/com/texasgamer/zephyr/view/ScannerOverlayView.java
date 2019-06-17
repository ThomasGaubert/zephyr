package com.texasgamer.zephyr.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.texasgamer.zephyr.R;

import java.util.Arrays;

/**
 * View to display QR code bounds as overlay on camera preview.
 */
public class ScannerOverlayView extends FrameLayout {

    private Rect[] mBoundingBoxes;
    private Paint mPaint;

    public ScannerOverlayView(@NonNull Context context) {
        super(context);
        init();
    }

    public ScannerOverlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScannerOverlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScannerOverlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (mBoundingBoxes == null || mPaint == null) {
            return;
        }

        for (Rect boundingBox : mBoundingBoxes) {
            canvas.drawRect(boundingBox, mPaint);
        }

        canvas.save();
    }

    public void setBoundingBoxes(Rect... boundingBoxes) {
        mBoundingBoxes = Arrays.copyOf(boundingBoxes, boundingBoxes.length);
        postInvalidate();
    }

    private void init() {
        setWillNotDraw(false);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.accent));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }
}
