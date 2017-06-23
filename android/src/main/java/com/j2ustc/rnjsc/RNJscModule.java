package com.j2ustc.rnjsc;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by yenole on 2017/6/23.
 */

public class RNJscModule extends ReactContextBaseJavaModule {
    public RNJscModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "Jsc";
    }


    @ReactMethod
    public void Call(String name){
        Log.d("TAG",name);
    }
}
