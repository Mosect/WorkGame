package com.mosect.workgame.ui;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.mosect.workgame.base.GameKeyEvent;
import com.mosect.workgame.base.GameTouchEvent;
import com.mosect.workgame.base.GameWindow;
import com.mosect.workgame.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public class BaseWindow extends GameWindow {

    private List<Widget> widgets;
    private List<Widget> touchWidgets;

    public void addWidget(Widget widget) {
        if (null == widgets) widgets = new ArrayList<>();
        widgets.add(widget);
    }

    public void removeWidget(Widget widget) {
        if (null != widgets) {
            widgets.remove(widget);
        }
    }

    public int getWidgetCount() {
        return null == widgets ? 0 : widgets.size();
    }

    public Widget getWidget(int index) {
        if (null != widgets && index >= 0 && index < widgets.size()) {
            return widgets.get(index);
        }
        return null;
    }

    public int indexOfWidget(Widget widget) {
        if (null != widgets) {
            return widgets.indexOf(widget);
        }
        return -1;
    }

    @Override
    protected void onTouchEvent(GameTouchEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("BaseWindow", "onTouch.down:" + event.getPointerId());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d("BaseWindow", "onTouch.up:" + event.getPointerId());
                break;
        }
        if (null != widgets) {
            Widget touchWidget = getTouchWidget(event.getPointerId());
            if (null == touchWidget) {
                for (int i = widgets.size() - 1; i >= 0; i--) {
                    Widget widget = widgets.get(i);
                    if (widget.dispatchTouchEvent(event)) {
                        int id = event.getPointerId();
                        setTouchWidget(id, widget);
                        break;
                    }
                }
            } else {
                touchWidget.dispatchTouchEvent(event);
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {
            setTouchWidget(event.getPointerId(), null);
        }
    }

    @Override
    protected void onKeyEvent(GameKeyEvent event) {
        super.onKeyEvent(event);
        if (null != widgets) {
            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget widget = widgets.get(i);
                if (widget.dispatchKeyEvent(event)) {
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        widgets = null;
    }

    @Override
    protected void onDrawWindowContent(Canvas canvas) {
        super.onDrawWindowContent(canvas);
        if (null != widgets) {
            for (Widget widget : widgets) {
                widget.dispatchDraw(canvas);
            }
        }
    }

    private void setTouchWidget(int index, Widget widget) {
        if (null == widget) {
            if (null != touchWidgets && index >= 0 && index < touchWidgets.size()) {
                touchWidgets.set(index, null);
            }
        } else {
            if (null == touchWidgets) touchWidgets = new ArrayList<>();
            while (touchWidgets.size() < index) {
                touchWidgets.add(null);
            }
            touchWidgets.add(widget);
        }
    }

    private Widget getTouchWidget(int index) {
        if (null != touchWidgets && index >= 0 && index < touchWidgets.size()) {
            return touchWidgets.get(index);
        }
        return null;
    }
}
