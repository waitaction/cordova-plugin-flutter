
var fs = require('fs');
var projectBuilderJsPath = "platforms/android/cordova/lib/builders/ProjectBuilder.js"
var gradleProperties = "platforms/android/gradle.properties";
/**
 * 读取cordova项目 ProjectBuilder.js 的内容
 */
function readProjectBuilderJsFile() {
    var data = fs.readFileSync(projectBuilderJsPath, 'utf8');
    return data;
}

/** 
 * 写入cordova项目 ProjectBuilder.js 的内容 
 */
function writeProjectBuilderJsFile(data) {
    fs.writeFileSync(projectBuilderJsPath, data, 'utf8');
}

/**
 * 写入flutter 到 settings.gradle
 */
function writeFlutterSettingsGradleToBuilderJsFile() {
    var txt = `fs.writeFileSync(path.join(this.root, 'settings.gradle'),`;
    var data = readProjectBuilderJsFile();
    data = data.replace(txt, `
        //#CORDOVA-PLUGIN-FLUTTER#START
        settingsGradlePaths.push("\\n");
        settingsGradlePaths.push("setBinding(new Binding([gradle: this]))\\n");
        settingsGradlePaths.push("evaluate(new File(settingsDir.parentFile.parentFile, 'flutter_module/.android/include_flutter.groovy'))\\n");
        //#CORDOVA-PLUGIN-FLUTTER#END
        fs.writeFileSync(path.join(this.root, 'settings.gradle'),
    `);
    writeProjectBuilderJsFile(data);
}



/**
 * 写入flutter 需要的库到  ProjectBuilder.js
 */
function writeImplementationToBuilderJsFile() {
    var txt = `buildGradle = buildGradle.replace(/(SUB-PROJECT DEPENDENCIES START)[\\s\\S]*(\\/\\/ SUB-PROJECT DEPENDENCIES END)/, '$1\\n' + depsList + '    $2');`;
    var data = readProjectBuilderJsFile();
    data = data.replace(txt, `
        //#CORDOVA-PLUGIN-FLUTTER#START
        depsList += '    implementation ' + "'androidx.annotation:annotation:1.0.0'" + '\\n';
        depsList += '    implementation ' + "project(':flutter')" + '\\n';
        //#CORDOVA-PLUGIN-FLUTTER#END
        buildGradle = buildGradle.replace(/(SUB-PROJECT DEPENDENCIES START)[\\s\\S]*(\\/\\/ SUB-PROJECT DEPENDENCIES END)/, '$1\\n' + depsList + '    $2');
    `);
    writeProjectBuilderJsFile(data);

}



/**
 * 更新 gradle
 */
function updateFlutterGradleToBuilderJsFile() {
    //先匹配到文本
    var data = readProjectBuilderJsFile();
    var txt = data.match(/var.*?\-all\.zip';/)[0];
    data = data.replace(txt, `
        ${txt};
        //#CORDOVA-PLUGIN-FLUTTER#START
        distributionUrl = process.env['CORDOVA_ANDROID_GRADLE_DISTRIBUTION_URL'] || 'https\\\\://services.gradle.org/distributions/gradle-5.6.4-all.zip';
        //#CORDOVA-PLUGIN-FLUTTER#END
    `);
    writeProjectBuilderJsFile(data);
}
function updateToGradlePropertiesJsFile() {
    var data = fs.readFileSync(gradleProperties, 'utf8');
    data += `

#CORDOVA-PLUGIN-FLUTTER#START
android.useAndroidX=true
android.enableJetifier=true
#CORDOVA-PLUGIN-FLUTTER#END
`;
    fs.writeFileSync(gradleProperties, data);
}

function installFlutterModule() {
    var process = require('child_process');
    var fs = require('fs');
    if (!fs.existsSync("flutter_module")) {
        console.info("\033[33m *** 安装Flutter模块 *** \033[0m")
        process.exec('flutter create -t module flutter_module',
            function (error, stdout, stderr) {
                if (error !== null) {
                    console.log('exec error: ' + error);
                }
                console.log(stdout);
                copyFlutterCordovaFile();
            }
        );
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
            console.log(error);
        }
    }



}

function checkRequireEnvironment(callback) {
    var process = require('child_process');
    process.exec('cordova --version',
        function (error, stdout, stderr) {
            try {
                console.log(stdout);
                var cordovaVersion = /\d\.\d\.\d/.exec(stdout)[0]
                if (parseInt(cordovaVersion.substr(0, 1)) < 9) {
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
                try {
                    console.log(stdout);
                    var androidVersion = /android \d\.\d\.\d/.exec(stdout)[0];
                    if (parseInt(androidVersion.replace("android ", "").sub(0, 1)) < 8) {
                        console.error("\033[31m 不支持低于8.0.0版本的cordova-android \033[0m");
                    }
                } catch (error) {
                    console.error("***\033[31m 检测必须的环境失败 *** \033[0m");
                    console.error(error);
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
                        console.error("\033[31m 不支持低于1.12.13版本的flutter \033[0m");
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


//需要检测版本环境
//console.log("\033[33m *** 检测必需的环境 *** \033[0m");
console.log("*** 检测必需的环境 ***");
checkRequireEnvironment(function () {
    installFlutterModule();
    //console.log("\033[33m *** 注入 platforms/android/cordova/lib/builders/ProjectBuilder.js 与flutter相关的脚本 *** \033[0m");
    console.log("*** 注入 platforms/android/cordova/lib/builders/ProjectBuilder.js 与flutter相关的脚本 ***");
    writeFlutterSettingsGradleToBuilderJsFile();
    writeImplementationToBuilderJsFile();
    updateFlutterGradleToBuilderJsFile();
    //console.log("\033[33m *** 注入 platforms/android/gradle.properties 与flutter相关的设置 *** \033[0m");
    console.log("*** 注入 platforms/android/gradle.properties 与flutter相关的设置 ***");
    updateToGradlePropertiesJsFile();







});

