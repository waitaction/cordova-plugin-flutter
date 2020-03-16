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




  // https://mvnrepository.com/artifact/commons-beanutils/commons-beanutils


compile group: 'commons-beanutils', name: 'commons-beanutils', version: '1.9.2'
compile group: 'commons-collections', name: 'commons-collections', version: '3.2.1'
compile group: 'commons-lang', name: 'commons-lang', version: '2.6'
compile group: 'commons-logging', name: 'commons-logging', version: '1.2'
compile group: 'ezmorph', name: 'ezmorph', version: '1.0.6'
compile group: 'net.sf.json-lib', name: 'json-lib', version: '2.4'


    implementation group: 'commons-beanutils', name: 'commons-beanutils', version: '1.9.2'
    implementation group: 'commons-collections', name: 'commons-collections', version: '3.2.1'
    implementation group: 'commons-lang', name: 'commons-lang', version: '2.6'
    implementation group: 'commons-logging', name: 'commons-logging', version: '1.2'
    implementation group: 'ezmorph', name: 'ezmorph', version: '1.0.6'
    implementation group: 'net.sf.json-lib', name: 'json-lib', version: '2.4'



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


flutter.init(function () {
            // alert("初始成功")
        }, function () { })
        window.bridgeFlutter.testFunction = function (uuid, args, callback) {
            callback({"abcdefg":"用中文传"});
        }
        document.getElementById("test").addEventListener("click", function () {
            //alert("test")
            flutter.open(function () {

            })
        });