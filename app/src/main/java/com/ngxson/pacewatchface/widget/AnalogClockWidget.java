package com.ngxson.pacewatchface.widget;

import android.app.Service;
import android.graphics.Canvas;

/**
 * Abstract clock
 */
public abstract class AnalogClockWidget implements ClockWidget {

    @Override
    public void init(Service service) {
        // empty
    }

    public abstract void onDrawAnalog(Canvas canvas, float width, float height, float centerX, float centerY, float secRot, float minRot, float hrRot);
}
