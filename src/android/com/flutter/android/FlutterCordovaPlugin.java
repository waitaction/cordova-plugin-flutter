package com.flutter.android;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class FlutterCordovaPlugin extends CordovaPlugin {

    public static FlutterCordovaPlugin instance;
    CallbackContext openCallbackContext = null;

    public FlutterCordovaPlugin() {
        FlutterCordovaPlugin.instance = this;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        // 参考文档 https://flutter.dev/docs/development/add-to-app/android/add-flutter-screen
        if (action.equals("init")) {
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 初始化成功
                        cordova.getThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                callbackContext.success();
                            }
                        });
                    } catch (Exception ex) {
                        // 初始化失败
                        cordova.getThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                callbackContext.error(ex.getMessage());
                            }
                        });
                    }
                }
            });
            return true;
        }

        if (action.equals("open")) {
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String routerName = null;
                        if (args.length() > 0) {
                            routerName = args.getString(0);
                        }
                        initFlutterEngine(routerName);
                        if (TextUtils.isEmpty(routerName)) {
                            Intent intent = CordovaFlutterActivity.withNewEngine(CordovaFlutterActivity.class).build(FlutterCordovaPlugin.this.cordova.getActivity());
                            FlutterCordovaPlugin.this.cordova.startActivityForResult((CordovaPlugin) FlutterCordovaPlugin.this, intent, 10);
                        } else {
                            Intent intent = CordovaFlutterActivity.withNewEngine(CordovaFlutterActivity.class).initialRoute(routerName).build(FlutterCordovaPlugin.this.cordova.getActivity());
                            FlutterCordovaPlugin.this.cordova.startActivityForResult((CordovaPlugin) FlutterCordovaPlugin.this, intent, routerName.hashCode());
                        }
                        openCallbackContext = callbackContext;
                    } catch (Exception ex) {
                        callbackContext.error(ex.getMessage());
                    }
                }
            });

            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 这个方法桥接js，返回flutter的结果
        if (resultCode == Activity.RESULT_OK) {
            String result = intent.getStringExtra("result");
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    openCallbackContext.success(result);
                }
            });
        } else if (resultCode == Activity.RESULT_CANCELED) {

        }
    }

    private void initFlutterEngine(String routerName) {
        String enginId = "00000";
        if (!TextUtils.isEmpty(routerName)) {
            enginId = routerName;
        }
        FlutterEngineCache instance = FlutterEngineCache.getInstance();
        if (!instance.contains(enginId)) {
            FlutterEngine flutterEngine = new FlutterEngine(cordova.getContext());
            flutterEngine.getDartExecutor().executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault());
            instance.put(enginId, flutterEngine);
            GeneratedPluginRegistrant.registerWith(flutterEngine);
        }
    }
}
