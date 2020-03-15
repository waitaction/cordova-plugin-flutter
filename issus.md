flutter 与 android 互相调用
https://www.jianshu.com/p/310c28abc9f3

https://flutter.dev/docs/get-started/flutter-for/android-devs





flutter 调 安卓数据的方法

flutter端：
  static const platform = const MethodChannel('app.channel.shared.data');
  String dataShared = "No data";

getSharedText() async {
    var sharedData = await platform.invokeMethod("getSharedText");
    if (sharedData != null) {
      setState(() {
        dataShared = sharedData;
      });
    }
  }




安卓端：
GeneratedPluginRegistrant.registerWith(FlutterCordovaPlugin.instance.flutterEngine);
                                new MethodChannel(FlutterCordovaPlugin.instance.flutterEngine.getDartExecutor().getBinaryMessenger(), "app.channel.shared.data").setMethodCallHandler(
                                        new MethodChannel.MethodCallHandler() {
                                            @Override
                                            public void onMethodCall(MethodCall call, MethodChannel.Result result) {
                                                if (call.method.contentEquals("getSharedText")) {

                                                    result.success("abbbb");

                                                }
                                            }
                                        });




            JSONObject object = new JSONObject();
            object.put("log", msg);
            String format = "window.cordova.plugins.huaweipush.log(%s);";
            final String js = String.format(format, object.toString());
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    instance.webView.loadUrl("javascript:" + js);
                }
            });


             cordova.fireDocumentEvent('huaweipush.receiveRegisterResult', this.receiveRegisterResult);