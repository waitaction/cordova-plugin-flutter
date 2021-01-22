package com.flutter.android;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
// import androidx.annotation.NonNull;
// import androidx.annotation.Nullable;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

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
    public String engineId;
    public static Map<String, MethodChannel.Result> methodMap = new HashMap<String, MethodChannel.Result>();
    public static  CordovaInterface _cordova;
    public static CordovaWebView _webView;

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

            _webView = this.webView;
            _cordova = this.cordova;
            this.engineId = "d785461d";
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        initSingeFlutterEngine();
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
                String routerName=null;
                if(args.length()>0){
                    routerName = args.getString(0);
                }

                if(TextUtils.isEmpty(routerName)){
                    this.cordova.getActivity().startActivity(
                            FlutterActivity
                                    .withCachedEngine(this.engineId)
                                    .build(this.cordova.getActivity()));
                }else{
                    Intent intent = CordovaFlutterActivity
                            .withNewEngine(CordovaFlutterActivity.class)
                            .initialRoute(routerName)
                            .build(this.cordova.getActivity());
                    this.cordova.getActivity().startActivity(intent);
                }

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
                            JSONArray jsonArray=new JSONArray(rstList);
                            methodChannelResult.success(jsonArray.toString());
                        } else {
                            methodChannelResult.success(null);
                        }

                    }
                });
            }
        }

        return false;
    }

    private FlutterEngine initSingeFlutterEngine() {
        FlutterEngine  flutterEngine = new FlutterEngine(cordova.getContext());
        flutterEngine.getDartExecutor()
                .executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault());

        FlutterEngineCache instance = FlutterEngineCache.getInstance();
        instance.remove(engineId);
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

        return flutterEngine;
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
