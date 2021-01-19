
# cordova应用与flutter混合开发

让你的cordova应用能与flutter进行混合开发

| 开发环境        | 版本                |
| --------------- | ------------------- |
| cordova         | ≥ 9.0.0             |
| cordova-android | ≥ 8.0.0 and < 9.0.0 |
| cordova-ios     | ≥ 5.0.0             |
| flutter         | ≥ 1.17.0            |


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

## 在`cordova`调用`flutter`

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
flutter.open(
undefined,
function(){
    console.log("打开flutter成功");
},function(err){
})

flutter.open(
'/HomePage' , //flutter路由名称    
function(){
    console.log("打开flutter成功");
},function(err){
})
```

对应flutter路由名称
```flutter
 @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        platform: TargetPlatform.iOS,
      ),
      routes: {
        '/HomePage': (BuildContext context) => new HomePage(),
        '/NextPage': (BuildContext context) => new NextPage(),
      },
      initialRoute: '/HomePage', //初始化路由为first页面
      // onGenerateRoute: AppRouter.router.generator,
    );
  }
```

## `flutter`调用`cordova`的js方法

先在`cordova`定义方法

`javascript`代码：

``` javascript
window.bridgeFlutter.getDate = function (jsonObj, callback) {
    var format = jsonObj.format;
    callback({
        date: "日期格式是：" + format
    });
}
```

然后在`flutter`调用

`dart`代码：

``` dart
import 'package:flutter_module/cordova.dart';
//在需要的地方调用
var result = await CordovaPlatform.invokeMethod("getDate", {"format": "yyyy年MM月dd日"});
```

## 示例代码

``` js
flutter.init(function () {
    window.bridgeFlutter.getDate = function (jsonObj, callback) {
        var format = jsonObj.format;
        callback({   date: "日期格式是：" + format });
        alert("日期格式是：" + format)
    }
    console.log("初始化成功");
    flutter.open(function () {
        console.log("打开flutter成功");
    }, function (err) {

    })
}, function (err) {

})
```

## 编译

编译ios时，需要在虚拟机的调试请使用`cordova build ios`编译

开发完成后，需要上架到app store，请使用`cordova build ios --release` 编译

> 如果使用`cordova build ios --release`命令编译，使用xcode运行在虚拟机会提示`Building for iOS Simulator, but the embedded framework 'Flutter.framework' was built for iOS`的错误

## 如何调试`flutter`代码

使用 `visual studio code`，安装`flutter`扩展，打开`cordova项目路径/flutter_module`目录，使用`visual studio code`调试工具创建`Flutter: Attach to Device`，运行app后附加调试。

![因网络原因，截图'/screenshot/1590851583248.jpg'无法加载](/screenshot/1590851583248.jpg "截图")

![因网络原因，截图'/screenshot/1590851885254.jpg'无法加载](/screenshot/1590851885254.jpg "截图")

## 其它

感谢 `@zhangjianying` 大神编写的ios代码和ios构建脚本.

目前仍有许多不足之处，由于工作繁忙，其它细节，你可以 [fork一份代码](https://github.com/waitaction/cordova-plugin-flutter) 进行优化。
