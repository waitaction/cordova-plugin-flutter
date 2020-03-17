var fs = require('fs');
var projectBuilderJsPath = "platforms/android/cordova/lib/builders/ProjectBuilder.js"
var gradleProperties = "platforms/android/gradle.properties";

function removeFromGradlePropertiesJsFile() {
    var data = fs.readFileSync(gradleProperties, 'utf8');
    data = data.replace(/#CORDOVA-PLUGIN-FLUTTER#START[\s\S]*?#CORDOVA-PLUGIN-FLUTTER#END/ig, "");
    fs.writeFileSync(gradleProperties, data);
}

/**
 * 移除已写入flutter 的 settings.gradle
 */
function removeFlutterFromBuilderJsFile() {
    var data = fs.readFileSync(projectBuilderJsPath, 'utf8');
    data = data.replace(/\/\/#CORDOVA-PLUGIN-FLUTTER#START[\s\S]*?\/\/#CORDOVA-PLUGIN-FLUTTER#END/ig, "");
    fs.writeFileSync(projectBuilderJsPath, data, 'utf8');
}
console.log("*** 移除 platforms/android/cordova/lib/builders/ProjectBuilder.js 与flutter相关的脚本 ***");
removeFlutterFromBuilderJsFile();
console.log("*** 移除 platforms/android/gradle.properties 与flutter相关的设置 ***");
removeFromGradlePropertiesJsFile();
