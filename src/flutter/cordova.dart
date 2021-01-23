import 'package:flutter/services.dart';
class CordovaPlatform {
  static const platform =
      const MethodChannel('app.channel.shared.cordova.data');

  static Future<dynamic> invokeMethod(
      String methodName, Map<String, Object> argsJsonObj) async {
    var data = await platform.invokeMethod(methodName, argsJsonObj);
    return data;
  }

  static Future<dynamic> finish(Map<String, Object> para) async {
    var data = await invokeMethod("finish", para);
    return data;
  }
}
