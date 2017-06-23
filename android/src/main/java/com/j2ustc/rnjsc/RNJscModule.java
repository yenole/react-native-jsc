package com.j2ustc.rnjsc;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by yenole on 2017/6/23.
 */

public class RNJscModule extends ReactContextBaseJavaModule {

    String mObtainPackage;
    HashMap<Integer, Object> mObjectMap;

    static HashMap<Class, String> rnTypes = null;

    static {
        rnTypes = new HashMap<>();
        rnTypes.put(boolean.class, "getBoolean");
        rnTypes.put(Boolean.class, "getBoolean");
        rnTypes.put(int.class, "getInt");
        rnTypes.put(Integer.class, "getInt");
        rnTypes.put(double.class, "getDouble");
        rnTypes.put(Double.class, "getDouble");
        rnTypes.put(String.class, "getString");
        rnTypes.put(Dynamic.class, "getDynamic");
        rnTypes.put(ReadableMap.class, "ReadableArray");
        rnTypes.put(ReadableArray.class, "ReadableMap");

    }

    public RNJscModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "Jsc";
    }

    @ReactMethod
    public void obtainPackage(String obtainPackage) {
        this.mObtainPackage = obtainPackage;
    }

    @ReactMethod
    public void obtain(String name, Callback callback) {
        if (name != null) {
            try {
                Class clazz = Class.forName(String.format("%s.%s", mObtainPackage, name));
                Object obj = clazz.newInstance();
                invokeCallback(callback, toJavaScriptObject(obj));
            } catch (Exception e) {
                invokeCallback(callback, 0, e.getMessage());
            }
        } else {
            invokeCallback(callback, 0, "Argument is null!");
        }
    }

    @ReactMethod
    public void fun(ReadableMap jsObj, String fun, ReadableArray args, Callback callback) {
        if (jsObj != null && jsObj.getBoolean("_J") && mObjectMap.containsKey(jsObj.getInt("hash"))) {
            Object obj = mObjectMap.get(jsObj.getInt("hash"));
            Object ret = invokeMethod(obj.getClass(), fun, obj, args);
            if (ret != null && ret instanceof Exception) {
                invokeCallback(callback, null, ((Exception) ret).getMessage());
            } else {
                invokeCallback(callback, toJavaScriptObject(ret));
            }
        } else {
            invokeCallback(callback, null, "Not found java object!");
        }
    }

    @ReactMethod
    public void sFun(String className, String fun, ReadableArray args, Callback callback) {
        if (className != null) {
            try {
                Class clazz = Class.forName(String.format("%s.%s", mObtainPackage, className));
                Object ret = invokeMethod(clazz, fun, null, args);
                if (ret != null && ret instanceof Exception) {
                    invokeCallback(callback, null, ((Exception) ret).getMessage());
                } else {
                    invokeCallback(callback, toJavaScriptObject(ret));
                }
            } catch (ClassNotFoundException e) {
                invokeCallback(callback, "Not found Method!");
            }
        } else {
            invokeCallback(callback, null, "Not found java object!");
        }
    }

    @ReactMethod
    public void release(ReadableMap jsObj) {
        if (jsObj != null && jsObj.getBoolean("_J") && mObjectMap.containsKey(jsObj.getInt("hash"))) {
            mObjectMap.remove(jsObj.getInt("hash"));
        }
    }


    private Object toJavaScriptObject(Object obj) {
        if (obj != null && !rnTypes.containsKey(obj.getClass())) {
            int hash = obj.hashCode();
            if (mObjectMap == null) {
                mObjectMap = new HashMap<>();
            }
            mObjectMap.put(hash, obj);
            WritableMap map = new WritableNativeMap();
            map.putInt("hash", hash);
            map.putBoolean("_J", true);
            return map;
        }
        return obj;
    }

    private Object[] toJavaArgs(ReadableArray args, Class[] types) {
        if (args != null) {
            Object[] jArgs = new Object[args.size()];
            for (int i = 0; i < args.size(); i++) {
                if (rnTypes.containsKey(types[i])){
                    try {
                        jArgs[i]= args.getClass().getMethod(rnTypes.get(types[i]),int.class).invoke(args,i);
                    } catch (Exception e) {
                        throw new RuntimeException("This type cannot be converted!");
                    }
                }else {
                    throw new RuntimeException("This type cannot be converted!");
                }
            }
            return jArgs;
        }
        return new Object[0];
    }

    private Object invokeMethod(Class clazz, String fun, Object instance, ReadableArray args) {
        try {
            Method method = null;
            if (args == null) {
                method = clazz.getMethod(fun);
            } else {
                for (Method i : clazz.getMethods()) {
                    if (i.getName().equals(fun) && i.getParameterTypes().length == args.size()) {
                        method = i;
                        break;
                    }
                }
            }
            if (method != null) {
                Object[] jArgs = toJavaArgs(args, method.getParameterTypes());
                return method.invoke(instance, jArgs);
            }
            return new RuntimeException("Not found Method!");
        } catch (Exception e) {
            return e;
        }
    }

    private void invokeCallback(Callback callback, Object... args) {
        if (callback != null) {
            callback.invoke(args);
        }
    }
}
