package com.awrtechnologies.valor.valorfireplace.helper;

import android.graphics.Bitmap;
import android.os.Handler;

import com.activeandroid.ActiveAndroid;
import com.awrtechnologies.valor.valorfireplace.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by awr005 on 03/06/15.
 */
public class Application extends android.app.Application{


    private GoogleAnalytics analytics;
    private Tracker tracker;

    @Override
    public void onCreate() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()

                .resetViewBeforeLoading(false)
                        // default
                .showImageOnLoading(R.drawable.loading).delayBeforeLoading(20)
                .cacheOnDisk(true).cacheInMemory(false) // default
                .considerExifParams(false) // default
                .bitmapConfig(Bitmap.Config.RGB_565) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()

                .memoryCache(new WeakMemoryCache()).threadPoolSize(1)
                        // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // .writeDebugLogs() // Remove for release app
                .defaultDisplayImageOptions(options).build();

        ImageLoader.getInstance().init(config);
        ActiveAndroid.initialize(this);
        ActiveAndroid.setLoggingEnabled(true);

        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-65704002-1"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }


}
