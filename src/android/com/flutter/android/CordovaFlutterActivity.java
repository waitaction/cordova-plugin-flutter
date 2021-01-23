package com.flutter.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONObject;

import java.util.HashMap;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;



public class CordovaFlutterActivity extends io.flutter.embedding.android.FlutterActivity {

    private final static String CHANNEL_NAME = "app.channel.shared.cordova.data";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void configureFlutterEngine(FlutterEngine flutterEngine) {

        MethodChannel methodChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL_NAME);
        methodChannel.setMethodCallHandler((call, result) -> {
            if (call.method.equals("finish")) {
                Intent intent = new Intent(this, FlutterCordovaPlugin.instance.cordova.getActivity().getClass());
                if (call.arguments != null) {
                    HashMap<String, Object> argHashMap = (HashMap<String, Object>) call.arguments;
                    JSONObject argJSONObject = new JSONObject(argHashMap);
                    intent.putExtra("result", argJSONObject.toString());
                } else {
                    intent.putExtra("result", new HashMap<String, Object>());
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
                result.success(true);
            }
        });
    }

    public static NewMyEngineIntentBuilder withNewEngine(Class<? extends FlutterActivity> activityClass) {
        return new NewMyEngineIntentBuilder(activityClass);
    }

    public static class NewMyEngineIntentBuilder extends NewEngineIntentBuilder {
        protected NewMyEngineIntentBuilder(Class<? extends FlutterActivity> activityClass) {
            super(activityClass);
        }
    }
}
