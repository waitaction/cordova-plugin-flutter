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

public class FlutterCordovaPlugin extends CordovaPlugin {

    public static FlutterCordovaPlugin instance;
    public static CallbackContext cdvCallbackContetxt;
    private Activity activity;

    public FlutterCordovaPlugin() {
        FlutterCordovaPlugin.instance = this;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        activity = cordova.getActivity();

    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {


        if (action.equals("openFlutterPage")) {
            // JSONObject jsObj = args.getJSONObject(0);
            // String path = jsObj.getString("path");
            // String outPath = jsObj.getString("outPath");
            // Intent intent = new Intent();
            // intent.setClass(activity, VideoSelectActivity.class);
            // intent.putExtra("path", path);
            // intent.putExtra("savePath", outPath);
            // TrimmerCordovaPlugin.this.activity.startActivity(intent);
            // cdvCallbackContetxt = callbackContext;
            return true;
        }

         
        return false;
    }

 

   
}
