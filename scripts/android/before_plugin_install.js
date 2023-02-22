
var fs = require('fs');
var projectBuilderJsPath = "node_modules/cordova-android/lib/builders/ProjectBuilder.js"
var gradleProperties = "platforms/android/gradle.properties";
var buildGradleFilePath = "platforms/android/build.gradle";
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
        depsList += '    implementation ' + "'androidx.annotation:annotation:1.1.0'" + '\\n';
        depsList += '    implementation ' + "project(':flutter')" + '\\n';
        //#CORDOVA-PLUGIN-FLUTTER#END
        buildGradle = buildGradle.replace(/(SUB-PROJECT DEPENDENCIES START)[\\s\\S]*(\\/\\/ SUB-PROJECT DEPENDENCIES END)/, '$1\\n' + depsList + '    $2');
    `);
    writeProjectBuilderJsFile(data);
}


function updateToBuildGradle() {
    var data = fs.readFileSync(buildGradleFilePath, 'utf8');
    data = data.replace(
        `allprojects {
    repositories {
        google()
        jcenter()
    }`,
        `allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://storage.googleapis.com/download.flutter.io" }
    }`);
    fs.writeFileSync(buildGradleFilePath, data);
}

console.log("*** 注入 platforms/android/cordova/lib/builders/ProjectBuilder.js 与flutter相关的脚本 ***");
writeFlutterSettingsGradleToBuilderJsFile();
writeImplementationToBuilderJsFile();

// console.log("*** 注入 platforms/android/gradle.properties 与flutter相关的设置 ***");


console.log("*** 注入 platforms/android/build.gradle 与flutter相关的设置 ***");
updateToBuildGradle();

