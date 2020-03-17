/**
 * 桥接cordova，该文件由cordova-plugin-flutter生成，请不要修改此文件
 * 示例:
 * cordova中的javascript:
 *      window.bridgeFlutter.getDate = function (jsonObj, callback) {
 *           var format = jsonObj.format;
 *           callback({
 *              date: '2020/03/17'
 *           });
 *      }
 * 在flutter中调用，上面的getDate方法会被触发，callback用于回调结果给flutter:
 *      var result = await CordovaPlatform.invokeMethod( "getDate", {"format": "yyyy年MM月dd日"});
 * 
 */

import 'package:flutter/services.dart';

class CordovaPlatform {
  static const platform =
      const MethodChannel('app.channel.shared.cordova.data');

  static Future<dynamic> invokeMethod(
      String methodName, Map<String, Object> argsJsonObj) async {
    var data = await platform.invokeMethod(methodName, argsJsonObj);
    return data;
  }
}
