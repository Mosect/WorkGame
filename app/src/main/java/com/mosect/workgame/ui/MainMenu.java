package com.mosect.workgame.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.mosect.workgame.base.GameKeyEvent;
import com.mosect.workgame.widget.Widget;

/**
 * 菜单
 */
class MainMenu extends Widget {

    private String name;
    private Bitmap icon;
    private int nameMarginTop;
    private int padding;
    private Runnable action;

    private Paint focusedPaint = new Paint();
    private int focusedLineWidth = 5;
    private Paint paint = new Paint();
    private Rect rect = new Rect();
    private Rect iconSrc = new Rect();
    private RectF iconDst = new RectF();
    private float nameX;
    private float nameY;

    MainMenu(String name, Bitmap icon) {
        this.name = name;
        this.icon = icon;
        focusedPaint.setStrokeWidth(focusedLineWidth);
        focusedPaint.setAntiAlias(true);
        focusedPaint.setStyle(Paint.Style.STROKE);
        focusedPaint.setColor(Color.WHITE);
    }

    void setPadding(int padding) {
        this.padding = padding;
    }

    void setNameMarginTop(int nameMarginTop) {
        this.nameMarginTop = nameMarginTop;
    }

    void setAction(Runnable action) {
        this.action = action;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isPressed()) {
            canvas.drawColor(Color.argb(0xFF, 0x44, 0x44, 0x44));
        }
        if (isFocused()) {
            int offset = focusedLineWidth / 2;
            canvas.drawRect(offset, offset, getWidth() - offset,
                    getHeight() - offset, focusedPaint);
        }
        if (!TextUtils.isEmpty(name)) {
            canvas.drawText(name, nameX, nameY, paint);
        }
        if (null != icon && !icon.isRecycled()) {
            canvas.drawBitmap(icon, iconSrc, iconDst, null);
        }
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        paint.reset();
        rect.setEmpty();
        int cw = Math.max(0, width - 2 * padding);
        int ch = Math.max(0, height - 2 * padding);
        float fontSize = ch / 5f;
        String name = TextUtils.isEmpty(this.name) ? "TEST" : this.name;
        paint.setTextSize(fontSize);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.getTextBounds(name, 0, name.length(), rect);
        nameX = (width - rect.width()) / 2f;
        nameY = height - rect.bottom - padding;

        int iconHeight = Math.max(0, ch - rect.height() - nameMarginTop);
        int iconSize = Math.min(cw, iconHeight);
        int iconX = (cw - iconSize) / 2;
        int iconY = (iconHeight - iconSize) / 2;
        if (null != icon && !icon.isRecycled()) {
            iconSrc.set(0, 0, icon.getWidth(), icon.getHeight());
        }
        iconDst.set(iconX, iconY, iconX + iconSize, iconY + iconSize);
    }

    @Override
    protected boolean onKeyEvent(GameKeyEvent event) {
        if (event.getCode() == KeyEvent.KEYCODE_ENTER) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                setPressed(true);
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                setPressed(false);
                doAction();
            }
            return true;
        }
        return super.onKeyEvent(event);
    }

    @Override
    protected void onPressedClick() {
        super.onPressedClick();
        doAction();
    }

    private void doAction() {
        if (null != action) {
            action.run();
        }
    }
}
