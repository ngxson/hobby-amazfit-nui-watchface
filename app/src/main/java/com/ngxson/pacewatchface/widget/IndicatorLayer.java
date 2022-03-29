package com.ngxson.pacewatchface.widget;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ngxson.pacewatchface.PaceWatchFaceSplt;
import com.ngxson.pacewatchface.resource.CalendarResource;
import com.ngxson.pacewatchface.resource.IndicatorResource;
import com.ngxson.pacewatchface.resource.ResourceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IndicatorLayer extends AbstractWidget {
    private Service service;
    private Paint mGPaint;
    private boolean isSplt;
    private boolean ready = false;
    public static PaceWatchFaceSplt mainService;

    private final static long ONE_HR = 1*60*60000L + 10000;
    private final static long TWO_HRS = 2*60*60000L + 10000;

    private long lastUpdate = 0;

    public IndicatorLayer(PaceWatchFaceSplt service, boolean isSplt) {
        super();
        this.isSplt = isSplt;
        if (service != null) {
            mainService = service;
            IndicatorResource.preload(service, isSplt);
            this.ready = true;
        }
    }

    @Override
    public void init(Service service) {
        this.service = service;
        this.mGPaint = new Paint();
        IndicatorResource.preload(service, isSplt);
        this.ready = true;
    }

    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        ArrayList<CalendarResource.EventIndicator> indicators = CalendarResource.getIndicators(service);
        List<SlptViewComponent> slpt_objects = new ArrayList<>();
        int x;
        int y;

        for (CalendarResource.EventIndicator indicator : indicators) {
            long distance = Math.abs(indicator.milisec - currentTime);
            if (distance > TWO_HRS) continue;
            int prio = (distance <= ONE_HR) ? 0 : 1;
            int[] coord = IndicatorResource.getXY(indicator.minute);
            x = coord[0];
            y = coord[1];
            SlptPictureView indicatorImg = new SlptPictureView();
            indicatorImg.setImagePicture(IndicatorResource.getImage8c(indicator.minute, prio));
            indicatorImg.setStart(x, y);
            slpt_objects.add(indicatorImg);
        }

        return slpt_objects;
    }

    @Override
    public void draw(Canvas canvas, float width, float height, float centerX, float centerY) {
        if (!ready) return;
        long currentTime = Calendar.getInstance().getTimeInMillis();
        ArrayList<CalendarResource.EventIndicator> indicators = CalendarResource.getIndicators(service);
        int x;
        int y;

        for (CalendarResource.EventIndicator indicator : indicators) {
            long distance = Math.abs(indicator.milisec - currentTime);
            if (distance > TWO_HRS) continue;
            int prio = (distance <= ONE_HR) ? 0 : 1;
            int[] coord = IndicatorResource.getXY(indicator.minute);
            x = coord[0];
            y = coord[1];
            canvas.drawBitmap(IndicatorResource.getImage(indicator.minute, prio), x, y, mGPaint);
        }
    }
}
