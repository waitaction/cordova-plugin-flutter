package com.flutter.android;

import android.os.Bundle;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class CordovaFlutterActivity extends
        io.flutter.embedding.android.FlutterActivity {

    private final static String CHANNEL_NAME = "app.channel.shared.cordova.data";

    public static NewMyEngineIntentBuilder withNewEngine(Class<? extends FlutterActivity> activityClass) {
        return new NewMyEngineIntentBuilder(activityClass);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void configureFlutterEngine(FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        registerCordovaMethodChannel(flutterEngine);
    }

    //注册cordova桥
    private void registerCordovaMethodChannel(FlutterEngine flutterEngine) {
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL_NAME)
                .setMethodCallHandler(
                        (call, result) -> {
                            String uuid = UUID.randomUUID().toString();
                            String uuidMethod = call.method + "#" + uuid + "#";
                            FlutterCordovaPlugin.methodMap.put(uuidMethod, result);
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

                            FlutterCordovaPlugin._cordova.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    FlutterCordovaPlugin._webView.loadUrl("javascript:" + script);
                                }
                            });

                        });
    }

    //重写创建引擎方法
    public static class NewMyEngineIntentBuilder extends NewEngineIntentBuilder {
        protected NewMyEngineIntentBuilder(Class<? extends FlutterActivity> activityClass) {
            super(activityClass);
        }
    }

}
