package com.flutter.android;

import android.app.Activity;
import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class FlutterCordovaPlugin extends CordovaPlugin {

    public static FlutterCordovaPlugin instance;
    public FlutterEngine flutterEngine;
    public String engineId;

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
            //JSONObject jsObj = args.getJSONObject(0);
            //this.engineId = jsObj.getString("engineId");
            this.engineId = "d785461d-7d1f-44e1-8d5c-cee5980e7dca";
            this.cordova.getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Instantiate a FlutterEngine.
                                flutterEngine = new FlutterEngine(FlutterCordovaPlugin.instance.cordova.getContext());

                                // Start executing Dart code to pre-warm the FlutterEngine.
                                flutterEngine.getDartExecutor().executeDartEntrypoint(
                                        DartExecutor.DartEntrypoint.createDefault()
                                );

                                // Cache the FlutterEngine to be used by FlutterActivity.
                                FlutterEngineCache.getInstance().put(engineId, flutterEngine);

                                FlutterCordovaPlugin.instance.cordova.getThreadPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callbackContext.success();
                                    }
                                });
                            } catch (Exception ex) {
                                FlutterCordovaPlugin.instance.cordova.getThreadPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callbackContext.error(ex.getMessage());
                                    }
                                });
                            }
                        }
                    }
            );
            return true;
        }

        if (action.equals("openPage")) {
            try {
                this.cordova.getActivity().startActivity(
                        FlutterActivity.withCachedEngine(this.engineId).build(this.cordova.getActivity())
                );
                callbackContext.success();
            } catch (Exception ex) {
                callbackContext.error(ex.getMessage());
            }
            return true;
        }
        return false;
    }
    
}
