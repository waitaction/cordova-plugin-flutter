import 'package:flutter/services.dart';

class CordovaPlatform {
  static const platform =
      const MethodChannel('app.channel.shared.cordova.data');

  static Object invokeMethod(
      String methodName, Map<String, Object> args) async {
    Object data = await platform.invokeMethod(methodName, args);
    return data;
  }
}
