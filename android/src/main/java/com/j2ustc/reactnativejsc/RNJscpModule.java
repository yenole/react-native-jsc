package com.j2ustc.reactnativejsc;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

/**
 * Created by yenole on 2017/6/23.
 */

public class RNJscpModule extends ReactContextBaseJavaModule {

    public RNJscpModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return  "Jscp";
    }
}
