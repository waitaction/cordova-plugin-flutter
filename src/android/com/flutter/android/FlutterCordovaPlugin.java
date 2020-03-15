package com.flutter.android;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterCordovaActivity;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class FlutterCordovaPlugin extends CordovaPlugin {

    public static FlutterCordovaPlugin instance;
    public  FlutterEngine flutterEngine;
    public String engineId;
    public Map<String,MethodChannel.Result> methodMap = new HashMap<String,MethodChannel.Result>();

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
            this.engineId = "d785461d";
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

                                GeneratedPluginRegistrant.registerWith(flutterEngine);

                                new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "app.channel.shared.cordova.data")
                                        .setMethodCallHandler(new MethodChannel.MethodCallHandler() {
                                            @Override
                                            public void onMethodCall(MethodCall call, MethodChannel.Result result) {
                                               if ( !methodMap.containsKey(call.method)){
                                                   methodMap.put(call.method,result);
                                               }
                                            }
                                        });

                                cordova.getThreadPool().execute(new Runnable() {
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

        if (action.equals("open")) {
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
