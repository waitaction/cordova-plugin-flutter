import 'package:flutter/services.dart';

class CordovaPlatform {
  static const platform =
      const MethodChannel('app.channel.shared.cordova.data');

  static Object invokeMethod(String methodName) async {
    var data = await platform.invokeMethod(methodName);
    return data;
  }
}
