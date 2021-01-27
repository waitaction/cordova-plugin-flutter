
function iOSFlutterFrameworkBuild() {
    var child_process = require('child_process');
    var fs = require('fs');

    if (fs.existsSync("flutter_module")) {
        console.info("\033[33m *** flutter build ios-framework *** \033[0m");
        console.log("编译过程可能需要几分钟的时间...");
        child_process.execSync('cd flutter_module && flutter clean && flutter packages get && flutter build ios-framework --output=ios-framework', { stdio: [0, 1, 2] });
        if (process.argv != null && process.argv.length > 0) {
            var result = process.argv.find(m => m == "--release");
            if (result != null) {
                copyiOSFlutterFramework('Release');
            } else {
                copyiOSFlutterFramework('Debug');
            }
        } else {
            copyiOSFlutterFramework('Debug');
        }
    }

    function copyiOSFlutterFramework(version) {
        try {
            console.info("\033[33m *** copy iOS FlutterFramework *** \033[0m");
            var fs = require('fs');
            var configXmlPath = "config.xml";
            var configXml = fs.readFileSync(configXmlPath);
            var name = /<name>.*?<\/name>/.exec(configXml)[0];
            name = name.replace("<name>", "").replace("</name>", "");
            var targetPath = "platforms/ios/" + name + "/Plugins/cordova-plugin-flutter";
            var path = "flutter_module/ios-framework/" + version;
            console.info("\033[33m *** name *** \033[0m");
            copy(path, targetPath);
        } catch (error) {
            console.info("\033[33m 报错： \033[0m");
            console.log(error);
        }
    }

}


function copy(src, dst) {
    var fs = require('fs');
    let paths = fs.readdirSync(src); //同步读取当前目录
    paths.forEach(function (path) {
        var _src = src + '/' + path;
        var _dst = dst + '/' + path;
        //stats  该对象 包含文件属性
        fs.stat(_src, function (err, stats) {
            if (err) throw err;
            if (stats.isFile()) { //如果是个文件则拷贝 
                let readable = fs.createReadStream(_src);//创建读取流
                let writable = fs.createWriteStream(_dst);//创建写入流
                readable.pipe(writable);
            } else if (stats.isDirectory()) { //是目录则 递归 
                checkDirectory(_src, _dst, copy);
            }
        });
    });
}
function checkDirectory(src, dst, callback) {
    var fs = require('fs');
    fs.access(dst, fs.constants.F_OK, (err) => {
        if (err) {
            fs.mkdirSync(dst);
            callback(src, dst);
        } else {
            callback(src, dst);
        }
    });
};


iOSFlutterFrameworkBuild();