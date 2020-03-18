var exec = require('cordova/exec');
var Flutter = function () { }

Flutter.prototype.init = function (success, error) {
    exec(success, error, "CordovaFlutter", "init", []);
}

Flutter.prototype.open = function (success, error) {
    exec(success, error, "CordovaFlutter", "open", []);
}

Flutter.prototype.invokeCallback = function (callbackData, uuidMethod, success, error) {
    var result = {
        result: callbackData,
        uuid: uuidMethod
    };
    exec(success, error, "CordovaFlutter", "invokeCallback", [result]);
}

if (window.bridgeFlutter == null) {
    window.bridgeFlutter = {};
}

window.bridgeFlutterInvoke = function (uuid, methodName, args) {
    if (window.bridgeFlutter[methodName] == null) {
        this.alert("window.bridgeFlutter." + methodName + " 方法不存在");
        return;
    }
    window.bridgeFlutter[methodName](args, function (result) {
        var uuidMethod = methodName + "#" + uuid + "#";
        flutter.invokeCallback(result, uuidMethod, function () {

        }, function () {

        });
    });
}


module.exports = new Flutter();

