package com.ngxson.pacewatchface;

import android.content.res.Resources;
import android.graphics.Canvas;

import java.util.Arrays;
import java.util.LinkedList;

import com.ngxson.pacewatchface.data.DataType;
import com.ngxson.pacewatchface.data.MultipleWatchDataListenerAdapter;
import com.ngxson.pacewatchface.widget.AnalogClockWidget;
import com.ngxson.pacewatchface.widget.ClockWidget;
import com.ngxson.pacewatchface.widget.DigitalClockWidget;
import com.ngxson.pacewatchface.widget.Widget;

/**
 * Abstract base class for watch faces
 */
public abstract class AbstractWatchFace extends com.huami.watch.watchface.AbstractWatchFace {


    private class DigitalEngine extends com.huami.watch.watchface.AbstractWatchFace.DigitalEngine {

        private final DigitalClockWidget widget;

        public DigitalEngine(DigitalClockWidget widget) {
            this.widget = widget;
        }

        @Override
        protected void onPrepareResources(Resources resources) {
            this.widget.init(AbstractWatchFace.this);
            for (Widget widget : AbstractWatchFace.this.widgets) {
                widget.init(AbstractWatchFace.this);
                for (DataType type : widget.getDataTypes()) {
                    registerWatchDataListener(new MultipleWatchDataListenerAdapter(widget, type));
                }
            }
        }

        @Override
        protected void onDrawDigital(Canvas canvas, float width, float height, float centerX, float centerY, int seconds, int minutes, int hours, int year, int month, int day, int week, int ampm) {
            for (Widget widget : AbstractWatchFace.this.widgets) {
                canvas.translate(widget.getX(), widget.getY());
                widget.draw(canvas, width, height, centerX, centerY);
                canvas.translate(-widget.getX(), -widget.getY());
            }
            widget.onDrawDigital(canvas, width, height, centerX, centerY, seconds, minutes, hours, year, month, day, week, ampm);
        }
    }

    private class AnalogEngine extends com.huami.watch.watchface.AbstractWatchFace.AnalogEngine {

        private final AnalogClockWidget widget;

        public AnalogEngine(AnalogClockWidget widget) {
            this.widget = widget;
        }

        @Override
        protected void onPrepareResources(Resources resources) {
            this.widget.init(AbstractWatchFace.this);
            for (Widget widget : AbstractWatchFace.this.widgets) {
                widget.init(AbstractWatchFace.this);
                for (DataType type : widget.getDataTypes()) {
                    registerWatchDataListener(new MultipleWatchDataListenerAdapter(widget, type));
                }
            }
        }

        @Override
        protected void onDrawAnalog(Canvas canvas, float width, float height, float centerX, float centerY, float secRot, float minRot, float hrRot) {
            for (Widget widget : AbstractWatchFace.this.widgets) {
                canvas.translate(widget.getX(), widget.getY());
                widget.draw(canvas, width, height, centerX, centerY);
                canvas.translate(-widget.getX(), -widget.getY());
            }
            widget.onDrawAnalog(canvas, width, height, centerX, centerY, secRot, minRot, hrRot);
        }
    }

    final ClockWidget clock;
    final LinkedList<Widget> widgets = new LinkedList<>();

    protected AbstractWatchFace(final ClockWidget clock, final Widget... widgets) {
        this.clock = clock;
        this.widgets.addAll(Arrays.asList(widgets));
    }

    public final Engine onCreateEngine() {
        notifyStatusBarPosition(12.0f);
        return AnalogClockWidget.class.isInstance(clock) ? new AnalogEngine((AnalogClockWidget) clock) : new DigitalEngine((DigitalClockWidget) clock);
    }
}
