
function installFlutterModule() {
    var process = require('child_process');
    var fs = require('fs');
    if (!fs.existsSync("flutter_module")) {
        console.info("\033[33m *** 安装Flutter模块 *** \033[0m")
        var exportPath = "";
        //var exportPath = "export PUB_HOSTED_URL=https://pub.flutter-io.cn && export FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn && ";

        try {
            var result = process.execSync(exportPath + 'flutter create -t module flutter_module');
            var error = result[0];
            var stdout = result[1];
            var stderr = result[2];
            //execSync 如果子进程结束的时候返回值不为0 则会提示错误信息
            if (error !== null && error != 67) {
                console.error('exec error: ' + error);
            }
            console.log(stdout);
        } catch (e) {
            console.error(e)
        }

        copyFlutterCordovaFile();
    }

    function copyFlutterCordovaFile() {
        try {
            var fs = require('fs');
            var targetPath = "flutter_module/lib";
            var path = "plugins/cordova-plugin-flutter/src/flutter/cordova.dart";
            if (fs.existsSync(targetPath)) {
                var data = fs.readFileSync(path);
                fs.writeFileSync(targetPath + "/cordova.dart", data);
            }
        } catch (error) {
            console.info("\033[33m 报错： \033[0m");
            console.error(error);
        }
    }
}


function checkRequireEnvironment(callback) {
    console.log("*** flutter ≥ 1.17.0 ***");
    console.log("*** cordova ≥ 10.0.0 ***");
    console.log("*** cordova-android ≥ 9.0.0 ***");
    console.log("*** cordova-ios ≥ 5.0.0 ***");

    var process = require('child_process');
    process.exec('cordova --version',
        function (error, stdout, stderr) {
            try {
                console.log(stdout);
                var cordovaVersion = /\d+\.\d+\.\d+/.exec(stdout)[0];
                if (parseInt(cordovaVersion.substr(0, cordovaVersion.indexOf("."))) < 9) {
                    console.error("\033[31m 不支持低于9.0.0版本的cordova \033[0m");
                }
            } catch (error) {
                console.error("\033[31m 检测必须的环境失败 \033[0m");
                console.error(error);
            }
            if (error !== null) {
                console.log('exec error: ' + error);
            }
            next1();
        }
    );
    function next1() {
        process.exec('cordova platform',
            function (error, stdout, stderr) {
                console.log(stdout);
                try {
                    var androidVersion = /android \d\.\d\.\d/.exec(stdout)[0];
                    if (parseInt(androidVersion.replace("android ", "").sub(0, 1)) < 9) {
                        console.error("\033[31m 不支持低于10.0.0版本的cordova-android \033[0m");
                    }
                } catch (error) {
                    console.error("***\033[31m 检测必须的环境失败，cordova-android没有安装，请使用命令cordova platform add android安装 *** \033[0m");

                }

                try {
                    var iosVersion = /ios \d\.\d\.\d/.exec(stdout)[0];
                    if (parseInt(iosVersion.replace("ios ", "").sub(0, 1)) < 5) {
                        console.error("\033[31m 不支持低于5.1.1版本的cordova-ios \033[0m");
                    }
                } catch (error) {
                    console.error("***\033[31m 检测必须的环境失败，cordova-ios没有安装，请使用命令cordova platform add ios安装 *** \033[0m");
                }
                if (error !== null) {
                    console.log('exec error: ' + error);
                }
                next2();
            }
        );
    }

    function next2() {
        process.exec('flutter --version',
            function (error, stdout, stderr) {
                try {
                    console.log(stdout);
                    var flutterVersion = /Flutter \d/.exec(stdout)[0];
                    if (parseInt(flutterVersion.replace("Flutter ", "").sub(0, 1)) < 1) {
                        console.error("\033[31m 不支持低于1.17.0版本的flutter \033[0m");
                    }
                } catch (error) {
                    console.error("\033[31m 检测必须的环境失败 \033[0m");
                    console.error(error);
                }
                if (error !== null) {
                    console.log('exec error: ' + error);
                }
                callback();
            }
        );
    }


}
console.log("\033[33m *** 检测必需的环境 ***\033[0m");
checkRequireEnvironment(function () {
    installFlutterModule();
});
