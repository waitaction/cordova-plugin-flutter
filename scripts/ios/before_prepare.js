
function iOSFlutterFrameworkBuild() {
    var process = require('child_process');
    var fs = require('fs');

    if (fs.existsSync("flutter_module")) {
        console.info("\033[33m *** flutter build ios-framework *** \033[0m");
        console.log("安装过程中可能需要几分钟的时间...");
        //'cd flutter_module && flutter build ios-framework --output=../platforms/ios/${appName}/Plugins/cordova-plugin-flutter'
        process.exec('cd flutter_module && flutter build ios-framework --output=ios-framework',
            function (error, stdout, stderr) {
                if (error !== null) {
                    console.log('exec error: ' + error);
                }
                console.log(stdout);
                copyiOSFlutterFramework();
            }
        );
    }

    function copyiOSFlutterFramework() {
        try {
            var fs = require('fs');
            var configXmlPath = "config.xml";
            var configXml = fs.readFileSync(configXmlPath);
            var name = /<name>.*?<\/name>/.exec(configXml)[0];
            name = name.replace("<name>", "").replace("</name>", "");
            var targetPath = "../platforms/ios/" + name + "/Plugins/cordova-plugin-flutter";
            var path = "flutter_module/ios-framework";
            copy(path, targetPath);
        } catch (error) {
            console.info("\033[33m 报错： \033[0m");
            console.log(error);
        }
    }

}


function copy(src, dst) {
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