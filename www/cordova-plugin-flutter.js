var exec = require('cordova/exec');
var Flutter = function () { }
 
Flutter.prototype.initFlutter = function (opt,success,error){
    exec(success, error, "CordovaFlutter", "initFlutter", [opt]);
}

Flutter.prototype.openFlutterPage = function (opt, success, error) {
    exec(success, error, "CordovaFlutter", "openFlutterPage", [opt]);
}

module.exports = new Flutter();

