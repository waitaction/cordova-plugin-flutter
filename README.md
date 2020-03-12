
# cordova应用与flutter混合开发

> 该插件开发中，目前暂时无法正常使用，请不要使用

让你的cordova应用能与flutter进行混合开发

| 开发环境        | 版本      |
| --------------- | --------- |
| cordova         | ≥ 9.0.0   |
| cordova-android | ≥ 8.0.0   |
| flutter         | ≥ 1.12.13 |

## 安装

1.先确认你的cordova项目已添加android平台

``` shell
cordova platform add android
```

2.然后在你的cordova项目中创建flutter模块

``` shell
flutter create -t module flutter_module
```

> 不要去修改flutter_module文件夹名称

3.然后安装cordova-plugin-flutter插件

``` shell
cordova plugin add cordova-plugin-flutter
```

> 安装过程可能会卡住，等一小会就正常

4.编译android

``` shell
cordova build android
```

> 编译过程中会下载jar包，请耐心等待