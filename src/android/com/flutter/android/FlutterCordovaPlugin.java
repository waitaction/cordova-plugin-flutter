package com.flutter.android;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class FlutterCordovaPlugin extends CordovaPlugin {

    public static FlutterCordovaPlugin instance;
    public FlutterEngine flutterEngine;
    public String engineId;
    public Map<String, MethodChannel.Result> methodMap = new HashMap<String, MethodChannel.Result>();

    public FlutterCordovaPlugin() {
        FlutterCordovaPlugin.instance = this;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        // 参考文档
        // https://flutter.dev/docs/development/add-to-app/android/add-flutter-screen
        if (action.equals("init")) {
            this.engineId = "d785461d";
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Instantiate a FlutterEngine.
                        flutterEngine = new FlutterEngine(cordova.getContext());

                        // Start executing Dart code to pre-warm the FlutterEngine.
                        flutterEngine.getDartExecutor()
                                .executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault());

                        // Cache the FlutterEngine to be used by FlutterActivity.
                        FlutterEngineCache.getInstance().put(engineId, flutterEngine);

                        GeneratedPluginRegistrant.registerWith(flutterEngine);

                        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(),
                                "app.channel.shared.cordova.data")
                                        .setMethodCallHandler(new MethodChannel.MethodCallHandler() {
                                            @Override
                                            public void onMethodCall(MethodCall call, MethodChannel.Result result) {
                                                String uuid = UUID.randomUUID().toString();
                                                String uuidMethod = call.method + "#" + uuid + "#";
                                                methodMap.put(uuidMethod, result);
                                                // 触发调用js
                                                String js = "window.bridgeFlutterInvoke('%s','%s',%s)";
                                                String script;
                                                if (call.arguments != null) {
                                                    HashMap<String, Object> argHashMap = (HashMap<String, Object>) call.arguments;
                                                    JSONObject argJSONObject = new JSONObject(argHashMap);
                                                    script = String.format(js, uuid, call.method,
                                                            argJSONObject.toString());
                                                } else {
                                                    script = String.format(js, uuid, call.method, null);
                                                }

                                                cordova.getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        webView.loadUrl("javascript:" + script);
                                                    }
                                                });
                                            }
                                        });

                        cordova.getThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                callbackContext.success();
                            }
                        });
                    } catch (Exception ex) {
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
            try {
                this.cordova.getActivity().startActivity(
                        FlutterActivity.withCachedEngine(this.engineId).build(this.cordova.getActivity()));
                callbackContext.success();
            } catch (Exception ex) {
                callbackContext.error(ex.getMessage());
            }
            return true;
        }

        if (action.equals("invokeCallback")) {
            JSONObject jsObj = args.getJSONObject(0);
            String uuidMethod = jsObj.getString("uuid");
            JSONObject callbackData = jsObj.getJSONObject("result");
            ArrayList<Map<?, ?>> rstList = new ArrayList<Map<?, ?>>();
            this.JsonObject2HashMap(callbackData, rstList);
            if (methodMap.containsKey(uuidMethod)) {
                cordova.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MethodChannel.Result methodChannelResult = methodMap.get(uuidMethod);
                        if (rstList.size() > 0) {
                            methodChannelResult.success(rstList.get(0));
                        } else {
                            methodChannelResult.success(null);
                        }

                    }
                });
            }
        }

        return false;
    }

    public void JsonObject2HashMap(JSONObject jo, List<Map<?, ?>> rstList) {
        for (Iterator<String> keys = jo.keys(); keys.hasNext();) {
            try {
                String key1 = keys.next();
                System.out.println("key1---" + key1 + "------" + jo.get(key1) + (jo.get(key1) instanceof JSONObject)
                        + jo.get(key1) + (jo.get(key1) instanceof JSONArray));
                if (jo.get(key1) instanceof JSONObject) {

                    JsonObject2HashMap((JSONObject) jo.get(key1), rstList);
                    continue;
                }
                if (jo.get(key1) instanceof JSONArray) {
                    JsonArray2HashMap((JSONArray) jo.get(key1), rstList);
                    continue;
                }
                System.out.println("key1:" + key1 + "----------jo.get(key1):" + jo.get(key1));
                json2HashMap(key1, jo.get(key1), rstList);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void JsonArray2HashMap(JSONArray joArr, List<Map<?, ?>> rstList) {
        for (int i = 0; i < joArr.length(); i++) {
            try {
                if (joArr.get(i) instanceof JSONObject) {

                    JsonObject2HashMap((JSONObject) joArr.get(i), rstList);
                    continue;
                }
                if (joArr.get(i) instanceof JSONArray) {

                    JsonArray2HashMap((JSONArray) joArr.get(i), rstList);
                    continue;
                }
                System.out.println("Excepton~~~~~");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void json2HashMap(String key, Object value, List<Map<?, ?>> rstList) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        rstList.add(map);
    }

}
