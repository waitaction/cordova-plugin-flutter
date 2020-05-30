
# cordova应用与flutter混合开发

让你的cordova应用能与flutter进行混合开发

| 开发环境        | 版本     |
| --------------- | -------- |
| cordova         | ≥ 9.0.0  |
| cordova-android | ≥ 8.0.0  |
| cordova-ios     | ≥ 5.0.0  |
| flutter         | ≥ 1.17.0 |

> flutter代码与cordova代码在同个cordova项目里，`flutter`代码所在的文件夹`flutter_module`
> 调试flutter请使用 `vs code` 的调试器 `Flutter: Attach to Device`，调试`flutter`相关方法参考`flutter`官网

## 安装

安装cordova-plugin-flutter插件

``` shell
cordova plugin add cordova-plugin-flutter
```

> 安装完之后会产生`flutter_module`文件夹，不要去修改flutter_module文件夹名称
> 安装过程可能会卡住，等一小会就正常

编译android

``` shell
cordova build android
```

> 编译过程中会下载jar包，可能需要几分钟时间...

编译ios

``` shell
cordova build ios
```

> 编译过程会下载缺失的flutter framework，可能需要几分钟时间...

## 调用flutter

**flutter.init** `使用前初始化，尽可能早的初始化，且只初始一次`

``` javascript
flutter.init(function(){
    console.log("初始化成功");
},function(err){

})
```

下面的方法都要在`flutter.init`之后才能调用

**flutter.open** `打开flutter`

``` javascript
//请在flutter.init方法之后调用
flutter.open(function(){
    console.log("打开flutter成功");
},function(err){

})
```

## `flutter`调用`cordova`的js方法

先在`cordova`定义方法

``` javascript
window.bridgeFlutter.getDate = function (jsonObj, callback) {
    var format = jsonObj.format;
    callback({
        date: "日期格式是：" + format
    });
}
```

然后在`flutter`调用

``` dart
import 'package:flutter_module/cordova.dart';
//在需要的地方调用
var result = await CordovaPlatform.invokeMethod("getDate", {"format": "yyyy年MM月dd日"});
```

## 编译

编译ios时，需要在虚拟机的调试请使用`cordova build ios`编译

开发完成后，需要上架到app store，请使用`cordova build ios --release` 编译

> 如果使用`cordova build ios --release`命令编译，使用xCode运行在虚拟机会提示`Building for iOS Simulator, but the embedded framework 'Flutter.framework' was built for iOS`的错误


## 其它

感谢 `@zhangjianying` 大神编写的ios代码和ios构建脚本.

目前仍有许多不足之处，由于工作繁忙，其它细节，你可以fork代码优化。
