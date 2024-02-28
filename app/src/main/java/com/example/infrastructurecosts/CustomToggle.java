package com.example.infrastructurecosts;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class CustomToggle extends View {
    private Paint paint, textPaint;
    private int backgroundColor, buttonColor, textColor;
    private float buttonPosition; // 0.0f = left, 1.0f = right
    protected boolean isOn = false; // Accessible field

    private static final int COLOR_LIGHT_BACKGROUND = Color.parseColor("#E6E6E7");
    private static final int COLOR_DARK_BACKGROUND = Color.parseColor("#211F28");
    private static final int COLOR_BUTTON_LIGHT = Color.WHITE;
    private static final int COLOR_BUTTON_DARK = Color.parseColor("#262655");
    private static final int TEXT_COLOR_LIGHT = Color.BLACK;
    private static final int TEXT_COLOR_DARK = Color.WHITE;
    private static final float CORNER_RADIUS = 100f;
    private static final int ANIMATION_DURATION = 300;

    private OnToggleListener onToggleListener;

    public CustomToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);

        buttonColor = COLOR_BUTTON_LIGHT;
        textColor = TEXT_COLOR_LIGHT;
        backgroundColor = COLOR_LIGHT_BACKGROUND;
        buttonPosition = 0.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the toggle background
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), CORNER_RADIUS, CORNER_RADIUS, paint);

        // Calculate button position for animation
        float buttonWidth = getWidth() / 2f;
        float buttonLeft = buttonWidth * buttonPosition;

        // Draw the button
        paint.setColor(buttonColor);
        canvas.drawRoundRect(new RectF(buttonLeft, 0, buttonLeft + buttonWidth, getHeight()), CORNER_RADIUS, CORNER_RADIUS, paint);

        // Draw the text
        textPaint.setColor(textColor);
        float textY = getHeight() / 2f - (textPaint.descent() + textPaint.ascent()) / 2f;
        canvas.drawText("Street", getWidth() * 0.25f, textY, textPaint);
        canvas.drawText("UAM", getWidth() * 0.75f, textY, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isOn = !isOn;
            startToggleAnimation();
            if (onToggleListener != null) {
                onToggleListener.onToggle(isOn);
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void startToggleAnimation() {
        ValueAnimator positionAnimator = ValueAnimator.ofFloat(buttonPosition, isOn ? 1f : 0f);
        positionAnimator.addUpdateListener(animation -> {
            buttonPosition = (float) animation.getAnimatedValue();
            invalidate();
        });

        positionAnimator.setDuration(ANIMATION_DURATION);
        positionAnimator.setInterpolator(new DecelerateInterpolator());
        positionAnimator.start();

        animateColorChanges();
    }

    private void animateColorChanges() {
        animateBackgroundColor();
        animateButtonColor();
        animateTextColor();
    }

    private void animateBackgroundColor() {
        int fromColor = isOn ? COLOR_LIGHT_BACKGROUND : COLOR_DARK_BACKGROUND;
        int toColor = isOn ? COLOR_DARK_BACKGROUND : COLOR_LIGHT_BACKGROUND;
        animateColor(fromColor, toColor, color -> backgroundColor = color);
    }

    private void animateButtonColor() {
        int fromColor = isOn ? COLOR_BUTTON_LIGHT : COLOR_BUTTON_DARK;
        int toColor = isOn ? COLOR_BUTTON_DARK : COLOR_BUTTON_LIGHT;
        animateColor(fromColor, toColor, color -> buttonColor = color);
    }

    private void animateTextColor() {
        int fromColor = isOn ? TEXT_COLOR_LIGHT : TEXT_COLOR_DARK;
        int toColor = isOn ? TEXT_COLOR_DARK : TEXT_COLOR_LIGHT;
        animateColor(fromColor, toColor, color -> textColor = color);
    }

    private void animateColor(int fromColor, int toColor, ColorUpdateListener listener) {
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimator.addUpdateListener(animation -> {
            int animatedColor = (int) animation.getAnimatedValue();
            listener.onColorUpdate(animatedColor);
            invalidate();
        });
        colorAnimator.setDuration(ANIMATION_DURATION);
        colorAnimator.start();
    }

    public interface OnToggleListener {
        void onToggle(boolean isOn);
    }

    public void setOnToggleListener(OnToggleListener listener) {
        this.onToggleListener = listener;
    }

    private interface ColorUpdateListener {
        void onColorUpdate(int color);
    }

    public void setState(boolean isOn) {
        this.isOn = isOn;
        buttonPosition = isOn ? 1.0f : 0.0f;
        invalidate();
    }

    // New method to check if "Street" is selected
    public boolean isStreetSelected() {
        return isOn; // Assuming isOn is true when "Street" is selected
    }
}
