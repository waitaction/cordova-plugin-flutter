
# cordova应用与flutter混合开发

> 该插件开发中，目前暂时无法正常使用，请不要使用，暂时android可以使用

让你的cordova应用能与flutter进行混合开发

| 开发环境        | 版本      |
| --------------- | --------- |
| cordova         | ≥ 9.0.0   |
| cordova-android | = 8.0.0   |
| flutter         | ≥ 1.12.13 |

## 安装

安装cordova-plugin-flutter插件

``` shell
cordova plugin add cordova-plugin-flutter
```

> 安装完之后会产生`flutter_module`文件夹，不要去修改flutter_module文件夹名称
> 安装过程可能会卡住，等一小会就正常

4.编译android

``` shell
cordova build android
```

> 编译过程中会下载jar包，请耐心等待

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

## 其它功能

开发中...


allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://storage.googleapis.com/download.flutter.io" }
    }
}